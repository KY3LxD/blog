package com.kyel.blog.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256Utils {

    public static String code(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(str.getBytes());
            byte[] byteDigest = md.digest();
            StringBuffer buf = new StringBuffer("");
            for(byte b: byteDigest) {
                buf.append(String.format("%02x", b));
            }

            return buf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
