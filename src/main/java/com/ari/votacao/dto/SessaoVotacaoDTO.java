package com.ari.votacao.dto;

import com.ari.votacao.enums.SituacaoEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class SessaoVotacaoDTO {
	private Long id;
	private SituacaoEnum situacao;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss")
	private LocalDateTime dataHoraInicio;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss")
	private LocalDateTime dataHoraFim;
}
