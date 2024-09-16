package com.pickpaysimplificado.infra.exceptions;

public class UserFoundException extends RuntimeException {

    public UserFoundException(){
        super("Documneto ou email Já existe");
    }
    public UserFoundException(String message ){
        super(message);
    }
}
