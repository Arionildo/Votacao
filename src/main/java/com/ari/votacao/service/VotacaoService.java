package com.ari.votacao.service;

import com.ari.votacao.adapter.VotacaoAdapter;
import com.ari.votacao.dto.*;
import com.ari.votacao.entity.Associado;
import com.ari.votacao.enums.VotoEnum;
import com.ari.votacao.exception.NotFoundException;
import com.ari.votacao.exception.SessaoEncerradaException;
import com.ari.votacao.exception.VotoInvalidoException;
import com.ari.votacao.repository.VotacaoRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class VotacaoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VotacaoService.class);

    private final VotacaoRepository repository;
    private final VotacaoAdapter adapter;
    private final PautaService pautaService;
    private final SessaoVotacaoService sessaoVotacaoService;
    private final AssociadoService associadoService;

    /**
     * metodo responsavel por realizar as validacoes antes do voto ser computado
     * e persistido na base de dados
     *
     * @param dto - @{@link VotoDTO}
     * @return - boolean
     */
    @Transactional(readOnly = true)
    public boolean validarVoto(VotoDTO dto) {
        LOGGER.debug("Validando os dados para voto idSessao = {}, idPauta = {}, idAssiciado = {}", dto.getIdSessaoVotacao(), dto.getIdPauta(), dto.getCpfAssociado());

        if (!pautaService.isPautaValida(dto.getIdPauta())) {

            LOGGER.error("Pauta nao localizada para votacao idPauta {}", dto.getCpfAssociado());
            throw new NotFoundException("Pauta não localizada id " + dto.getIdPauta());

        } else if (!sessaoVotacaoService.isSessaoVotacaoValida(dto.getIdSessaoVotacao())) {

            LOGGER.error("Tentativa de voto para sessao encerrada ou inválida. idSessaoVotacao {}", dto.getIdSessaoVotacao());
            throw new SessaoEncerradaException("Sessão de votação já encerrada ou inválida");

        } else if (!associadoService.validarAssociadoPodeVotar(dto.getCpfAssociado())) {

            LOGGER.error("Associado não esta habilitado para votar {}", dto.getCpfAssociado());
            throw new VotoInvalidoException("Não é possível votar mais de 1 vez na mesma pauta");

        } else if (!associadoService.validarParticipacaoAssociadoVotacao(dto.getCpfAssociado(), dto.getIdPauta())) {

            LOGGER.error("Associado tentou votar mais de 1 vez idAssociado {}", dto.getCpfAssociado());
            throw new VotoInvalidoException("Não é possível votar mais de 1 vez na mesma pauta");
        }

        return Boolean.TRUE;
    }

    /**
     * Se os dados informados para o voto, forem considerados validos
     * entao o voto é computado e persistido na base de dados.
     *
     * @param dto - @{@link com.ari.votacao.dto.VotoDTO}
     * @return - String
     */
    @Transactional
    public String votar(VotoDTO dto) {
        if (validarVoto(dto)) {
            LOGGER.debug("Dados validos para voto idSessao = {}, idPauta = {}, idAssiciado = {}", dto.getIdSessaoVotacao(), dto.getIdPauta(), dto.getCpfAssociado());
            PautaDTO pautaDTO = PautaDTO.builder().id(dto.getIdPauta()).build();
            SessaoVotacaoDTO sessaoVotacaoDTO = SessaoVotacaoDTO.builder().id(dto.getIdSessaoVotacao()).build();
            AssociadoDTO associadoDTO = associadoService.registrarAssociadoVotou(dto);
            VotacaoDTO votacaoDTO = VotacaoDTO.builder()
                    .pauta(pautaDTO)
                    .sessaoVotacao(sessaoVotacaoDTO)
                    .voto(VotoEnum.valueOf(dto.getVoto()))
                    .associado(associadoDTO)
                    .build();

            registrarVoto(votacaoDTO);
            return "Voto validado";
        }
        return null;
    }

    /**
     * @param dto - @{@link VotacaoDTO}
     */
    @Transactional
    public void registrarVoto(VotacaoDTO dto) {
        LOGGER.debug("Salvando o voto para idPauta {}", dto.getPauta().getId());
        repository.save(adapter.convertTo(dto));
    }

    /**
     * Realiza a busca e contagem dos votos positivos e negativos para determinada sessao e pauta de votacao.
     *
     * @param idPauta         - @{@link com.ari.votacao.entity.Pauta} ID
     * @param idSessaoVotacao - @{@link com.ari.votacao.entity.SessaoVotacao} ID
     * @return - @{@link VotacaoDTO}
     */
    @Transactional(readOnly = true)
    public VotacaoDTO buscarResultadoVotacao(Long idPauta, Long idSessaoVotacao) {
        LOGGER.debug("Contabilizando os votos para idPauta = {}, idSessaoVotacao = {}", idPauta, idSessaoVotacao);
        PautaDTO pautaDTO = PautaDTO.builder().id(idPauta).build();
        SessaoVotacaoDTO sessaoVotacaoDTO = SessaoVotacaoDTO.builder().id(idSessaoVotacao).build();
        VotacaoDTO dto = VotacaoDTO.builder()
                .pauta(pautaDTO)
                .sessaoVotacao(sessaoVotacaoDTO)
                .build();

        dto.setQuantidadeVotosSim(repository.countVotacaoByPautaIdAndSessaoVotacaoIdAndVoto(idPauta, idSessaoVotacao, VotoEnum.SIM));
        dto.setQuantidadeVotosNao(repository.countVotacaoByPautaIdAndSessaoVotacaoIdAndVoto(idPauta, idSessaoVotacao, VotoEnum.NAO));

        return dto;
    }

    /**
     * Realiza a montagem dos objetos referente ao resultado de determinada sessao e pauta de votacao.
     * <p>
     * Contagem somente e realizada apos a finalizacao da sessao.
     *
     * @param idPauta         - @{@link com.ari.votacao.entity.Pauta} ID
     * @param idSessaoVotacao - @{@link com.ari.votacao.entity.SessaoVotacao} ID
     * @return - @{@link ResultadoDTO}
     */
    @Transactional(readOnly = true)
    public ResultadoDTO buscarDadosResultadoVotacao(Long idPauta, Long idSessaoVotacao) {

        if (validarDadosExistentes(idPauta, idSessaoVotacao) && sessaoVotacaoService.isSessaoValidaParaContagem(idSessaoVotacao)) {
            LOGGER.debug("Construindo o objeto de retorno do resultado para idPauta = {}, idSessaoVotacao = {}", idPauta, idSessaoVotacao);
            PautaDTO pautaDTO = pautaService.findById(idPauta);
            VotacaoDTO votacaoDTO = buscarResultadoVotacao(idPauta, idSessaoVotacao);
            return new ResultadoDTO(pautaDTO, votacaoDTO);
        }
        throw new NotFoundException("Sessão de votação ainda está aberta, não é possível obter a contagem do resultado.");
    }

    /**
     * Valida a existência de uma sessão ativa para a pauta específica
     * @param idPauta         - @{@link com.ari.votacao.entity.Pauta} ID
     * @param idSessaoVotacao - @{@link com.ari.votacao.entity.SessaoVotacao} ID
     * @return - boolean
     */
    @Transactional(readOnly = true)
    public boolean validarDadosExistentes(Long idPauta, Long idSessaoVotacao) {
        return sessaoVotacaoService.hasSessaoAtiva(idSessaoVotacao) && pautaService.isPautaValida(idPauta);
    }
}