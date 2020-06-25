package com.ari.votacao.dto;

import com.ari.votacao.enums.VotoEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class VotacaoDTO {

	@ApiModelProperty(value = "ID da votação aberta")
	private Long id;

	@ApiModelProperty(value = "Voto")
	private VotoEnum voto;

	@ApiModelProperty(value = "Pauta da votação aberta")
	private PautaDTO pauta;

	@ApiModelProperty(value = "Associado da votação aberta")
	private AssociadoDTO associado;

	@ApiModelProperty(value = "Sessão de votação aberta")
	private SessaoVotacaoDTO sessaoVotacao;

	@ApiModelProperty(value = "Quantidade de votos positivos")
	private Integer quantidadeVotosSim;

	@ApiModelProperty(value = "Quantidade de votos negativos")
	private Integer quantidadeVotosNao;
}
