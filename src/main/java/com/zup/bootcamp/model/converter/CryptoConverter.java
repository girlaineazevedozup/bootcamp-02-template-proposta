package com.zup.bootcamp.model.converter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.AttributeConverter;
import java.security.Key;
import java.util.Base64;

@Component
public class CryptoConverter implements AttributeConverter<String, String> {

    private final String ALGORITHM = "AES";
    private final byte[] KEY;

    public CryptoConverter(@Value("${secret.encryptor.key}") String secretKey) {
        KEY = secretKey.getBytes();
    }

    @Override
    public String convertToDatabaseColumn(String ccNumber) {
        Key key = new SecretKeySpec(KEY, ALGORITHM);
        try {
            Cipher c = Cipher.getInstance(ALGORITHM);
            c.init(Cipher.ENCRYPT_MODE, key);
            return Base64.getEncoder().encodeToString(c.doFinal(ccNumber.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException(e);
    }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        Key key = new SecretKeySpec(KEY, ALGORITHM);
        try {
            Cipher c = Cipher.getInstance(ALGORITHM);
            c.init(Cipher.DECRYPT_MODE, key);
            return new String(c.doFinal(Base64.getDecoder().decode(dbData)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}