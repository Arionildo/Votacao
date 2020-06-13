package com.ari.votacao.controller;

import com.ari.votacao.enums.TopicEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "Notificacao", tags = "Notificacao")
@RestController
@RequestMapping(value = "/api/v1/notificacoes")
public class NotificacaoController {
	private static final Logger LOGGER = LoggerFactory.getLogger(NotificacaoController.class);
	private final KafkaTemplate<String, String> kafkaTemplate;

	@Autowired
	public NotificacaoController(KafkaTemplate<String, String> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	@GetMapping("{mensagem}")
	@ApiOperation(value = "Publica uma mensagem a ser transmitida pela plataforma Kafka")
	public String notificar(@PathVariable("mensagem") final String mensagem) {
		LOGGER.debug("Publicando mensagem = {}", mensagem);
		kafkaTemplate.send(TopicEnum.EXAMPLE.nome, mensagem);
		return "Mensagem Publicada com Sucesso!";
	}
}
