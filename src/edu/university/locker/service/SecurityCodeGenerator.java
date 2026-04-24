package edu.university.locker.service;

import java.security.SecureRandom;

public class SecurityCodeGenerator {

    private final SecureRandom secureRandom = new SecureRandom();

    public String generateCode() {
        int code = 1000 + secureRandom.nextInt(9000);
        return String.valueOf(code);
    }
}
