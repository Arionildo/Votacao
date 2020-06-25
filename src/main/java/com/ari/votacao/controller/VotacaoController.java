package com.ari.votacao.controller;

import com.ari.votacao.dto.ResultadoDTO;
import com.ari.votacao.dto.VotoDTO;
import com.ari.votacao.service.VotacaoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(value = "Votacao", tags = "Votacao")
@RestController
@RequestMapping(value = "/api/v1/votacoes")
public class VotacaoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(VotacaoController.class);

    private final VotacaoService service;

    @ApiOperation(value = "Votar em determinada pauta, enquanto a sessão de votação estiver aberta")
    @PostMapping(value = "/votar")
    public ResponseEntity<String> votar(@Valid @RequestBody VotoDTO dto) {
        LOGGER.debug("Associado votando associado = {}", dto.getCpfAssociado());
        String mensagem = service.votar(dto);
        LOGGER.debug("Voto associado finalizado associado = {}", dto.getCpfAssociado());
        return ResponseEntity.status(HttpStatus.CREATED).body(mensagem);
    }

    @ApiOperation(value = "Resultado da votacao, somente após finalização da sessão de votação")
    @GetMapping(value = "/resultado/{idPauta}/{idSessaoVotacao}")
    public ResponseEntity<ResultadoDTO> resultadoVotacao(@PathVariable("idPauta") Long idPauta, @PathVariable("idSessaoVotacao") Long idSessaoVotacao) {
        LOGGER.debug("Buscando resultado da votacao idPauta = {} , idSessaoVotacao = {} ", idPauta, idSessaoVotacao);
        ResultadoDTO dto = service.buscarDadosResultadoVotacao(idPauta, idSessaoVotacao);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }
}
