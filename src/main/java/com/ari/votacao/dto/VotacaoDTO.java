package com.ari.votacao.dto;

import com.ari.votacao.enums.VotoEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class VotacaoDTO {
	private Integer id;
	private VotoEnum voto;
	private PautaDTO Pauta;
	private AssociadoDTO associado;
	private SessaoVotacaoDTO sessaoVotacao;
}
