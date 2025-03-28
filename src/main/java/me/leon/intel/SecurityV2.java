package me.leon.intel;

import imgui.type.ImString;

public class SecurityV2 {
    public static ImString key = new ImString();

    public static String encode(String input) {
        byte[] inputBytes = input.getBytes();
        byte[] keyBytes = key.get().getBytes();

        byte[] outputBytes = new byte[inputBytes.length];

        for (int i = 0; i < inputBytes.length; i++) {
            outputBytes[i] = (byte) (inputBytes[i] ^ keyBytes[i % keyBytes.length]);
        }

        return java.util.Base64.getEncoder().encodeToString(outputBytes);
    }

    public static String decode(String encodedInput) {
        byte[] inputBytes = java.util.Base64.getDecoder().decode(encodedInput);
        byte[] keyBytes = key.get().getBytes();

        byte[] outputBytes = new byte[inputBytes.length];

        for (int i = 0; i < inputBytes.length; i++) {
            outputBytes[i] = (byte) (inputBytes[i] ^ keyBytes[i % keyBytes.length]);
        }

        return new String(outputBytes);
    }
}
