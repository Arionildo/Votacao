package com.ari.votacao.service;

import com.ari.votacao.adapter.SessaoVotacaoAdapter;
import com.ari.votacao.dto.SessaoVotacaoAbrirDTO;
import com.ari.votacao.dto.SessaoVotacaoDTO;
import com.ari.votacao.entity.SessaoVotacao;
import com.ari.votacao.enums.SituacaoEnum;
import com.ari.votacao.exception.NotFoundException;
import com.ari.votacao.repository.SessaoVotacaoRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class SessaoVotacaoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SessaoVotacaoService.class);
    private static final Integer TEMPO_DEFAULT = 1;

    private final SessaoVotacaoRepository repository;
    private final SessaoVotacaoAdapter adapter;
    private final PautaService pautaService;

    /**
     * Se a sessao votacao é valida entao persiste os dados na base e inicia
     * a contagem para o encerramento da mesma.
     *
     * @param sessaoVotacaoAbrirDTO - @{@link com.ari.votacao.dto.SessaoVotacaoAbrirDTO}
     * @return - @{@link SessaoVotacaoDTO}
     */
    @Transactional
    public SessaoVotacaoDTO abrirSessaoVotacao(SessaoVotacaoAbrirDTO sessaoVotacaoAbrirDTO) {
        LOGGER.debug("Abrindo a sessao de votacao para a pauta {}", sessaoVotacaoAbrirDTO.getIdPauta());
        validarAberturaSessao(sessaoVotacaoAbrirDTO);

        LocalDateTime dataHoraInicio = LocalDateTime.now();
        LocalDateTime dataHoraFim = calcularTempo(sessaoVotacaoAbrirDTO.getTempo());
        SessaoVotacaoDTO dto = SessaoVotacaoDTO.builder()
                .dataHoraFim(dataHoraFim)
                .dataHoraInicio(dataHoraInicio)
                .situacao(SituacaoEnum.ATIVA)
                .build();
        return salvar(dto);
    }

    /**
     * valida se os dados para iniciar uma validacao são consistentes
     * e ja estao persistidos na base de dados
     *
     * @param sessaoVotacaoAbrirDTO - @{@link SessaoVotacaoAbrirDTO}
     */
    @Transactional(readOnly = true)
    public void validarAberturaSessao(SessaoVotacaoAbrirDTO sessaoVotacaoAbrirDTO) {
        if (!pautaService.isPautaValida(sessaoVotacaoAbrirDTO.getIdPauta())) {
            throw new NotFoundException("Pauta não localizada idPauta" + sessaoVotacaoAbrirDTO.getIdPauta());
        }
    }

    /**
     * Busca se há sessões em andamento, se houver, os dados sao retornados para o validador de tempo
     *
     * @return - List<@{@link SessaoVotacaoDTO}>
     */
    @Transactional(readOnly = true)
    public List<SessaoVotacaoDTO> buscarSessoesParaDesativar() {
        LOGGER.debug("Buscando sessoes em andamento");
        List<SessaoVotacaoDTO> list = repository.findBySituacao(SituacaoEnum.ATIVA)
                .stream()
                .map(adapter::convertFrom)
                .collect(Collectors.toList());

        return list
                .stream()
                .filter(dto -> dto.getDataHoraFim()
                        .isBefore(LocalDateTime.now()))
                .collect(Collectors.toList());
    }

    /**
     * Quando houver sessão de votação com o tempo data hora fim expirado,
     * a flag é setado SituacaoEnum.INATIVA e persistido a alteração na base de dados.
     *
     * @param dto - @{@link SessaoVotacaoDTO}
     */
    @Transactional
    public void encerrarSessaoVotacao(SessaoVotacaoDTO dto) {
        LOGGER.debug("Encerrando sessao com tempo de duracao expirado {}", dto.getId());
        dto.setSituacao(SituacaoEnum.INATIVA);
        salvar(dto);
    }

    /**
     * Se houver a sessao de ID informado e com a tag ativa igual a TRUE
     * entao e considerada como valida para votacao.
     *
     * @param id - @{@link SessaoVotacao} ID
     * @return - boolean
     */
    @Transactional(readOnly = true)
    public boolean isSessaoVotacaoValida(Long id) {
        return repository.existsBySituacaoAndId(SituacaoEnum.ATIVA, id);
    }

    /**
     * @param id - @{@link SessaoVotacao} ID
     * @return - boolean
     */
    @Transactional(readOnly = true)
    public boolean hasSessaoAtiva(Long id) {
        if (repository.existsById(id)) {
            return Boolean.TRUE;
        } else {
            LOGGER.error("Sessao de votacao nao localizada para o id {}", id);
            throw new NotFoundException("Sessão de votação não localizada para o id " + id);
        }
    }

    /**
     * Se houver a sessao de ID informado e com tag ativa igual a FALSE
     * entao e considerada como valida para contagem
     *
     * @param id - @{@link SessaoVotacao} ID
     * @return - boolean
     */
    @Transactional(readOnly = true)
    public boolean isSessaoValidaParaContagem(Long id) {
        return repository.existsBySituacaoAndId(SituacaoEnum.ATIVA, id);
    }

    /**
     * Com base no LocalDateTime inicial é calculada a LocalDateTime final somando-se o
     * tempo em minutos informado na chamada do servico.
     * <p>
     * Se o tempo nao for informado ou for informado com valor 0,
     * entao é considerado o tempo de 1 minuto como default.
     *
     * @param tempo - tempo em minutos
     * @return - localDateTime
     */
    private LocalDateTime calcularTempo(Integer tempo) {
        if (tempo != null && tempo != 0) {
            return LocalDateTime.now().plusMinutes(tempo);
        } else {
            return LocalDateTime.now().plusMinutes(TEMPO_DEFAULT);
        }
    }

    /**
     * @param dto - @{@link SessaoVotacaoDTO}
     * @return - @{@link SessaoVotacaoDTO}
     */
    @Transactional
    public SessaoVotacaoDTO salvar(SessaoVotacaoDTO dto) {
        LOGGER.debug("Salvando a sessao de votacao");
        if (ofNullable(dto).isPresent()) {
            SessaoVotacao entity = adapter.convertTo(dto);
            return adapter.convertFrom(repository.save(entity));
        }
        return null;
    }
}
