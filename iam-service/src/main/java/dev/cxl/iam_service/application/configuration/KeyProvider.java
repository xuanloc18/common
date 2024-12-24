package dev.cxl.iam_service.application.configuration;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.encrypt.KeyStoreKeyFactory;
import org.springframework.stereotype.Component;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;

import dev.cxl.iam_service.application.dto.AuthenticationProperties;
import lombok.extern.slf4j.Slf4j;

@Component
@EnableConfigurationProperties(AuthenticationProperties.class)
@Slf4j
public class KeyProvider implements InitializingBean {
    private final AuthenticationProperties properties;
    private KeyPair keyPair;

    public KeyProvider(AuthenticationProperties properties) {
        this.properties = properties;
    }

    @Override
    public void afterPropertiesSet() {
        this.keyPair = keyPair(properties.getKeyStore(), properties.getKeyStorePassWord(), properties.getKeyAlias());
    }

    private KeyPair keyPair(String keyStore, String password, String alias) {
        KeyStoreKeyFactory keyStoreKeyFactory =
                new KeyStoreKeyFactory(new ClassPathResource(keyStore), password.toCharArray());
        return keyStoreKeyFactory.getKeyPair(alias);
    }

    public JWKSet jwkSet() {
        RSAKey.Builder builder = new RSAKey.Builder((RSAPublicKey) this.keyPair.getPublic())
                .keyUse(KeyUse.SIGNATURE)
                .algorithm(JWSAlgorithm.RS256)
                .keyID(UUID.randomUUID().toString());
        return new JWKSet(builder.build());
    }

    public KeyPair getKeyPair() {
        return keyPair; // Trả về keyPair đã được khởi tạo trong afterPropertiesSet()
    }
}
