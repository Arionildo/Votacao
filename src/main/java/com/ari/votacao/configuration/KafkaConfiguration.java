package com.ari.votacao.configuration;

import com.ari.votacao.enums.ServerEnum;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfiguration {

	/**
	 * Configurações a serem utilizadas pelo Kafka para a produção de mensagens
	 *
	 * @return ProducerFactory<String, String>
	 */
	@Bean
	public ProducerFactory<String, String> producerFactory() {
		Map<String, Object> config = new HashMap<>();

		config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, ServerEnum.KAFKA.ip);
		config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

		return new DefaultKafkaProducerFactory<>(config);
	}

	/**
	 * Representação da plataforma Kafka já configurada para a produção de mensagens
	 *
	 * @return KafkaTemplate<String, String>
	 */
	@Bean
	public KafkaTemplate<String, String> kafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}

	/**
	 * Configurações a serem utilizadas pelo Kafka para o consumo de mensagens
	 *
	 * @return ConsumerFactory<String, String>
	 */
	@Bean
	public ConsumerFactory<String, String> consumerFactory() {
		Map<String, Object> config = new HashMap<>();

		config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, ServerEnum.KAFKA.ip);
		config.put(ConsumerConfig.GROUP_ID_CONFIG, "group_id");
		config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

		return new DefaultKafkaConsumerFactory<>(config);
	}

	/**
	 * Configuração da plataforma Kafka para capturar imediatamente a geração de qualquer mensagem obtida
	 *
	 * @return ConcurrentKafkaListenerContainerFactory<String, String>
	 */
	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, String> listenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());
		return factory;
	}
}
