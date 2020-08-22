package com.jfavilau.demologin.password;

import java.security.SecureRandom;

public class PasswordGenerator {

    public static final String NUMEROS = "0123456789";
    public static final String MAYUSCULAS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String MINUSCULAS = "abcdefghijklmnopqrstuvwxyz";

    private PasswordGenerator() {
    }

    public static String getPassword(int length) {

        final String key = NUMEROS+MAYUSCULAS+MINUSCULAS;
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(key.length());
            sb.append(key.charAt(randomIndex));
        }
        return sb.toString();
    }
}
