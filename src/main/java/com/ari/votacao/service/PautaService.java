package com.ari.votacao.service;

import com.ari.votacao.adapter.PautaAdapter;
import com.ari.votacao.dto.PautaDTO;
import com.ari.votacao.entity.Pauta;
import com.ari.votacao.exception.NotFoundException;
import com.ari.votacao.repository.PautaRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class PautaService {
	private static final Logger LOGGER = LoggerFactory.getLogger(PautaService.class);
	private final PautaRepository repository;
	private final PautaAdapter adapter;

	@Transactional
	public PautaDTO save(PautaDTO dto) {
		Pauta entity = repository.save(adapter.convertTo(dto));
		return adapter.convertFrom(entity);
	}

	@Transactional(readOnly = true)
	public List<PautaDTO> findAll() {
		return repository.findAll().stream()
				.map(adapter::convertFrom)
				.collect(toList());
	}

	@Transactional(readOnly = true)
	public PautaDTO findById(Long id) {
		Optional<Pauta> pautaOptional = repository.findById(id);

		if (pautaOptional.isEmpty()) {
			String mensagem = "Pauta não localizada para o id " + id;
			LOGGER.error(mensagem);
			throw new NotFoundException(mensagem);
		}

		return adapter.convertFrom(pautaOptional.get());
	}

	@Transactional(readOnly = true)
	public boolean isPautaValida(Long id) {
		if (repository.existsById(id)) {
			return Boolean.TRUE;
		} else {
			String mensagem = "Pauta não localizada para o id " + id;
			LOGGER.error(mensagem);
			throw new NotFoundException(mensagem);
		}
	}
}
