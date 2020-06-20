package com.ari.votacao.controller;

import com.ari.votacao.dto.PautaDTO;
import com.ari.votacao.service.PautaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(value = "Pauta", tags = "Pauta")
@RestController
@RequestMapping(value = "/api/v1/pauta")
public class PautaController {
	private final PautaService service;

	@PostMapping
	@ApiOperation(value = "Salva uma nova Pauta")
	public ResponseEntity<PautaDTO> salvar(PautaDTO dto) {
		PautaDTO retorno = service.save(dto);
		return ResponseEntity.ok(retorno);
	}

	@GetMapping
	@ApiOperation(value = "Busca todas as Pautas criadas")
	public ResponseEntity<List<PautaDTO>> buscarTodos() {
		List<PautaDTO> retorno = service.findAll();
		return ResponseEntity.ok(retorno);
	}

	@ApiOperation(value = "Buscar pauta pelo ID")
	@GetMapping(value = "/{id}")
	public ResponseEntity<PautaDTO> buscarPorId(@PathVariable("id") Long id) {
		return ResponseEntity.ok(service.findById(id));
	}
}
