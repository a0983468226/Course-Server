package com.course.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import java.util.UUID;

public class AESUtil {

    private static final String key = "COURSE_KEY";
    private static final String ECB_INSTANCE = "AES/ECB/PKCS5Padding";
    private static final String SHA512 = "SHA-512";
    private static final String AES = "AES";

    public static void main(String[] args) {
        System.out.println(UUID.randomUUID().toString().replaceAll("-", ""));
    }


    public static String ECBencrypt(String str) throws Exception {
        byte[] keyBytes = getECBKey();
        SecretKeySpec key = new SecretKeySpec(keyBytes, AES);

        Cipher cipher = Cipher.getInstance(ECB_INSTANCE);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] ciphertext = cipher.doFinal(str.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(ciphertext);
    }

    public static String ECBdecode(String str) throws Exception {
        byte[] keyBytes = getECBKey();
        SecretKeySpec key = new SecretKeySpec(keyBytes, AES);
        Cipher cipher = Cipher.getInstance(ECB_INSTANCE);
        cipher.init(Cipher.DECRYPT_MODE, key);

        byte[] decodeBytes = Base64.getDecoder().decode(str);
        byte[] decryptedBytes = cipher.doFinal(decodeBytes);
        return new String(decryptedBytes);
    }

    public static String getKey() throws Exception {
        MessageDigest md = MessageDigest.getInstance(SHA512);
        md.reset();
        md.update(key.getBytes(StandardCharsets.UTF_8));
        return String.format("%0128x", new BigInteger(1, md.digest()));
    }

    public static byte[] getECBKey() throws Exception {
        MessageDigest md = MessageDigest.getInstance(SHA512);
        md.reset();
        md.update(key.getBytes(StandardCharsets.UTF_8));
        byte[] fullHash = md.digest();
        return Arrays.copyOf(fullHash, 16);
    }
}
