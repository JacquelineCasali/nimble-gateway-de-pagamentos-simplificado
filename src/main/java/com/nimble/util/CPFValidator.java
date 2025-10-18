package com.nimble.util;

public class CPFValidator {

    public static boolean isValid(String cpf) {
        if (cpf == null) return false;

        cpf = cpf.replaceAll("\\D", ""); // remove pontos e traços
        if (cpf.length() != 11) return false;

        // evita CPFs com todos os números iguais
        if (cpf.matches("(\\d)\\1{10}")) return false;

        try {
            int soma1 = 0, soma2 = 0;
            for (int i = 0; i < 9; i++) {
                int digito = Character.getNumericValue(cpf.charAt(i));
                soma1 += digito * (10 - i);
                soma2 += digito * (11 - i);
            }

            int digito1 = (soma1 * 10) % 11;
            if (digito1 == 10) digito1 = 0;

            soma2 += digito1 * 2;
            int digito2 = (soma2 * 10) % 11;
            if (digito2 == 10) digito2 = 0;

            return digito1 == Character.getNumericValue(cpf.charAt(9)) &&
                    digito2 == Character.getNumericValue(cpf.charAt(10));

        } catch (Exception e) {
            return false;
        }
    }
}
