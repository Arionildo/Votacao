package com.ari.votacao.dto;

import com.ari.votacao.enums.SituacaoEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class SessaoVotacaoDTO {
	private Integer id;
	private Date dataHoraInicio;
	private Date dataHoraFim;
	private SituacaoEnum situacao;
}
