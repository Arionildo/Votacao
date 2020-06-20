package com.ari.votacao.adapter;

import com.ari.votacao.dto.AssociadoDTO;
import com.ari.votacao.dto.PautaDTO;
import com.ari.votacao.entity.Associado;
import com.ari.votacao.entity.Pauta;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static java.util.Objects.nonNull;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Component
public class AssociadoAdapter implements Adapter<Associado, AssociadoDTO> {
	private PautaAdapter pautaAdapter;

	@Override
	public Associado convertTo(AssociadoDTO dto) {
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
