package com.ari.votacao.enums;

public enum VotoEnum {
	SIM,
	NAO;

	public static VotoEnum valueOf(Boolean voto) {
		return voto ? SIM : NAO;
	}
}
