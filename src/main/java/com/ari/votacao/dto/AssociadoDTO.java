package com.ari.votacao.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class AssociadoDTO {
	private Integer id;
	private String cpf;
	private PautaDTO pauta;
}
