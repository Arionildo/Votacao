package com.ari.votacao.adapter;

import com.ari.votacao.dto.PautaDTO;
import com.ari.votacao.entity.Pauta;
import org.springframework.stereotype.Component;

@Component
public class PautaAdapter implements Adapter<Pauta, PautaDTO> {
	@Override
	public Pauta convertTo(PautaDTO dto) {
		return Pauta.builder()
				.descricao(dto.getDescricao())
				.id(dto.getId())
				.build();
	}

	@Override
	public PautaDTO convertFrom(Pauta entity) {
		return PautaDTO.builder()
				.descricao(entity.getDescricao())
				.id(entity.getId())
				.build();
	}
}
