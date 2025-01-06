package me.leon.intel;

import java.util.Base64;

public class DataEncoder {
    public static String ENC_BIN_(String sDc) {
        String s = new String(Base64.getEncoder().encode(sDc.getBytes()));
        byte[] bytes = s.getBytes();
        StringBuilder binary = new StringBuilder();
        for (byte b : bytes) {
            int val = b;
            for (int i = 0; i < 8; i++) {
                binary.append((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            }
            binary.append(' ');
        }
        return binary.toString();
    }

    public static String DEC_BIN_(String sEc) {
        StringBuilder sb = new StringBuilder();
        String[] bytestr = sEc.split(" ");
        for (String c : bytestr)
            sb.append((char) Byte.parseByte(c, 2));

        return new String(Base64.getDecoder().decode(sb.toString().getBytes()));
    }
}
