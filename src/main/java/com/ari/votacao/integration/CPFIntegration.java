package com.ari.votacao.integration;

import com.ari.votacao.integration.endpoint.CPFEndpoint;
import org.springframework.stereotype.Component;

@Component
public class CPFIntegration {

	private final CPFEndpoint endpoint = new CPFEndpoint();
	
	public boolean isCpfValido(String cpf) {
		return endpoint.consultarCPF(cpf).contains("ABLE_TO_VOTE");
	}
}
