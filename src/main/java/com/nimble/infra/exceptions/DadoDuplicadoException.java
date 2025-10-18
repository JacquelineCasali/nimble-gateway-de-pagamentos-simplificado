package com.nimble.infra.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//@ResponseStatus(HttpStatus.NOT_FOUND)
public class DadoDuplicadoException extends RuntimeException{
    public DadoDuplicadoException(String mensagem) {
        super(mensagem);
    }
}
