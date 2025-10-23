package com.nimble.infra.exceptions;


public class CpfNotFoundException extends RuntimeException {
    public CpfNotFoundException(String campo, String cpf) {
        super("CPF " + campo+ " "+ "não encontrado");
    }
}
