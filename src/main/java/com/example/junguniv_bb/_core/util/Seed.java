package com.example.junguniv_bb._core.util;

import java.nio.charset.StandardCharsets;

/**
 * 순수하게 SEED 암호화/복호화만 수행하는 클래스
 * 한글 처리를 위해 UTF-8 인코딩/디코딩을 수행함.
 */
public class Seed {
    private static final byte[] pbszUserKey = "testCrypt2024!@#".getBytes(); // 사용자가 지정하는 입력 키 (16 bytes)
    private static final byte[] pbszIV = "1234567890123456".getBytes(); //  사용자가 지정하는 초기화 벡터 (16 bytes)

    /* SEED 암호화 */
    public static byte[] encrypt(String rawMessage) {

        if (rawMessage == null) 
            return null;

        // UTF-8 인코딩
        byte[] message = rawMessage.getBytes(StandardCharsets.UTF_8);

        // SEED 암호화(byte 배열 반환)
        return KISA_SEED_CBC.SEED_CBC_Encrypt(pbszUserKey, pbszIV, message, 0, message.length);
    }

    /* SEED 복호화 */
    public static String decrypt(byte[] encryptedMessage) {

        if (encryptedMessage == null) 
            return null;

        // SEED 복호화
        byte[] decryptedMessage = KISA_SEED_CBC.SEED_CBC_Decrypt(pbszUserKey, pbszIV, encryptedMessage, 0, encryptedMessage.length);

        // UTF-8 디코딩
        return new String(decryptedMessage, StandardCharsets.UTF_8);
    }
}
