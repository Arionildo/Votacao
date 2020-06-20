package com.ari.votacao.adapter;

public interface Adapter<R, T> {
	R convertTo(T dto);
	T convertFrom(R entity);
}
