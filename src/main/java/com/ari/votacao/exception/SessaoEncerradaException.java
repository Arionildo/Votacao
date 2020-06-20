package com.ari.votacao.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.LOCKED)
public class SessaoEncerradaException extends RuntimeException {

    public SessaoEncerradaException(String mensagem) {
        super(mensagem);
    }
}
