package com.zmijewski.ecommerce.util;

import org.apache.commons.lang3.RandomStringUtils;

public class PasswordGenerator {
    private static final boolean USE_LETTER = true;
    private static final boolean USE_NUMBERS = true;
    private static final int LENGTH = 8;
    public static String generatePassword() {
        return RandomStringUtils.random(LENGTH, USE_LETTER, USE_NUMBERS);
    }
}
