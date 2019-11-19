package services;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jwt.JWTClaimsSet;
import models.TokenData;

public class TokenService {
    public String getToken(Integer id, String role) throws JOSEException {
        RSAKey rsaKey = new RSAKeyGenerator(2048).keyID(role).generate();
        RSAKey rsaKeyPublic = rsaKey.toPublicJWK();
        JWSSigner signer = new RSASSASigner(rsaKey);
        //jackson token data
        TokenData tokenData = new TokenData(id, role);
        ObjectMapper objectMapper = new ObjectMapper();
        String jacksonTokenData;
        try {
            jacksonTokenData = objectMapper.writeValueAsString(tokenData);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
        JWSObject jwsObject = new JWSObject(new JWSHeader.Builder(JWSAlgorithm.RS256).keyID(rsaKey.getKeyID()).build(), new Payload(jacksonTokenData));
        jwsObject.sign(signer);
        return jwsObject.serialize();
    }
}
