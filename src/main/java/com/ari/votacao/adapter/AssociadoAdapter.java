package com.ari.votacao.adapter;

import com.ari.votacao.dto.AssociadoDTO;
import com.ari.votacao.dto.PautaDTO;
import com.ari.votacao.entity.Associado;
import com.ari.votacao.entity.Pauta;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Component
public class AssociadoAdapter implements Adapter<Associado, AssociadoDTO> {
	private final PautaAdapter pautaAdapter;

	public AssociadoAdapter(PautaAdapter pautaAdapter) {
		this.pautaAdapter = pautaAdapter;
	}

	@Override
	public Associado convertTo(AssociadoDTO dto) {
		if (isNull(dto)) {
			return null;
		}
		Pauta pauta = null;

		if (nonNull(dto.getPauta())) {
			pauta = pautaAdapter.convertTo(dto.getPauta());
		}

		return Associado.builder()
				.cpf(dto.getCpf())
				.id(dto.getId())
				.pauta(pauta)
				.build();
	}

	@Override
	public AssociadoDTO convertFrom(Associado entity) {
		PautaDTO pautaDTO = null;

		if (nonNull(entity.getPauta())) {
			pautaDTO = pautaAdapter.convertFrom(entity.getPauta());
		}

		return AssociadoDTO.builder()
				.cpf(entity.getCpf())
				.id(entity.getId())
				.pauta(pautaDTO)
				.build();
	}
}
