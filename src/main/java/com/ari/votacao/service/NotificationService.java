package com.ari.votacao.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
	private static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);


	/**
	 * Notifica um usuário à partir de qualquer registro capturado pela plataforma Kafka
	 * @param mensagem
	 */
	@KafkaListener(topics = "example", groupId = "group_id")
	public void notificar(String mensagem) {
		LOGGER.info("Notificação: {}", mensagem);

	}
}
