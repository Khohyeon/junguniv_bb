package com.example.junguniv_bb._core.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Base64;

/**
 * SEED 암호화/복호화된 데이터를 Base64로 인코딩/디코딩하는 JPA AttributeConverter
 * 사용법 : Entity 클래스의 필드에 @Convert(converter = CrytoConverter.class) 어노테이션 추가하면
 * 해당 필드에 대해 암호화/복호화 처리가 자동으로 이루어짐.
 */
@Converter(autoApply = false)
public class CrytoConverter implements AttributeConverter<String, String> {

    // 평문(String) -> SEED 암호화(byte[]) -> 인코딩(base64_String) 후 DB 저장
    @Override
    public String convertToDatabaseColumn(String attribute) {

        if (attribute == null)
            return null;

        // SEED 암호화
        byte[] encryptedBytes = Seed.encrypt(attribute);

        // base64 인코딩
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // 저장된 데이터(base64_String) -> 디코딩(bytes[]) -> SEED 복호화(String) 후 반환
    @Override
    public String convertToEntityAttribute(String dbData) {

        if (dbData == null)
            return null;

        // base64 디코딩
        byte[] encryptedBytes = Base64.getDecoder().decode(dbData);

        // SEED 복호화
        return Seed.decrypt(encryptedBytes);
    }
}
