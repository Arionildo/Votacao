package com.ari.votacao.service;

import com.ari.votacao.adapter.AssociadoAdapter;
import com.ari.votacao.dto.AssociadoDTO;
import com.ari.votacao.dto.PautaDTO;
import com.ari.votacao.dto.VotoDTO;
import com.ari.votacao.entity.Associado;
import com.ari.votacao.integration.CPFIntegration;
import com.ari.votacao.repository.AssociadoRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class AssociadoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AssociadoService.class);
    private final AssociadoRepository repository;
    private final AssociadoAdapter adapter;
    private final CPFIntegration cpfIntegration;

    /**
     * Realiza a validacao se o associado ja votou na pauta informada pelo seu ID.
     * <p>
     * Se nao existir um registro na base, entao e considerado como valido para seu voto ser computado
     *
     * @param cpf @{@link com.ari.votacao.entity.Associado} CPF Valido
     * @param idPauta     @{@link com.ari.votacao.entity.Pauta} ID
     * @return - boolean
     */
    @Transactional(readOnly = true)
    public boolean validarParticipacaoAssociadoVotacao(String cpf, Long idPauta) {
        LOGGER.debug("Validando participacao do associado na votacao da pauta  id = {}", idPauta);
        return !repository.existsByCpfAndPautaId(cpf, idPauta);
    }

    /**
     * Salva um novo Associado
     * @param dto @{@link AssociadoDTO}
     * @return Associado
     */
    @Transactional
    public Associado salvar(AssociadoDTO dto) {
        LOGGER.debug("Registrando participacao do associado na votacao idAssociado = {}, idPauta = {}", dto.getCpf(), dto.getPauta().getId());
       return repository.save(adapter.convertTo(dto));
    }

    /**
     * faz a chamada para metodo que realiza a consulta em API externa
     * para validar por meio de um cpf valido, se o associado esta habilitado para votar
     *
     * @param cpf - @{@link AssociadoDTO} CPF valido
     * @return - boolean
     */
    public boolean validarAssociadoPodeVotar(String cpf) {
        return cpfIntegration.isCpfValido(cpf);
    }

    /**
     * Apos voto ser computado. O associado e registrado na base de dados a fim de
     * evitar que o mesmo possa votar novamente na mesma sessao de votacao e na mesma pauta.
     * <p>
     * A opcao de voto nao e persistido na base de dados.
     *
     * @param dto - @{@link VotoDTO}
     * @return AssociadoDTO
     */
    @Transactional
    public AssociadoDTO registrarAssociadoVotou(VotoDTO dto) {
        PautaDTO pautaDTO = PautaDTO.builder().id(dto.getIdPauta()).build();
        AssociadoDTO associadoDTO = AssociadoDTO.builder()
                .cpf(dto.getCpfAssociado())
                .pauta(pautaDTO)
                .build();
        return adapter.convertFrom(salvar(associadoDTO));
    }
}