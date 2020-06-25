package com.ari.votacao.integration.endpoint;

import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class CPFEndpoint {

	public String consultarCPF(String cpf) {
		Map<String, String> params = new HashMap<>();
		params.put("cpf", cpf);
		RestTemplate restTemplate = new RestTemplate();
		String uri = "https://user-info.herokuapp.com/users/{cpf}";
		return restTemplate.getForObject(uri, String.class, params);
	}
}
