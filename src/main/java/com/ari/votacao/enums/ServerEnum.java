package com.ari.votacao.enums;

public enum ServerEnum {
	KAFKA("127.0.0.1:9092");

	public String ip;

	ServerEnum(String ip) {
		this.ip = ip;
	}
}
