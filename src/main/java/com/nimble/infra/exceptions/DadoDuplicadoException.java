package com.nimble.infra.exceptions;



//@ResponseStatus(HttpStatus.NOT_FOUND)
public class DadoDuplicadoException extends RuntimeException{
    public DadoDuplicadoException(String mensagem) {
        super(mensagem);
    }
}
