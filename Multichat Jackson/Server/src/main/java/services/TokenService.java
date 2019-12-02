package services;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import repositories.UsersRepositoryImpl;
import models.User;

import java.text.ParseException;

public class TokenService {
    public String getToken(Integer id, String role) throws JOSEException {
        UsersRepositoryImpl userDao = new UsersRepositoryImpl();
        RSAKey rsaKey = new RSAKeyGenerator(2048).keyID(role + id).generate();
        RSAKey rsaKeyPublic = rsaKey.toPublicJWK();
        userDao.updateVerifier(rsaKeyPublic.toString(), id);
        System.out.println(rsaKeyPublic);
        JWSSigner signer = new RSASSASigner(rsaKey);
        //jackson token data
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder().claim("id", id).claim("role", role).build();
        SignedJWT jwsObject = new SignedJWT(new JWSHeader.Builder(JWSAlgorithm.RS256).keyID(rsaKey.getKeyID()).build(), claimsSet);
        jwsObject.sign(signer);
        return jwsObject.serialize();
    }

    public User parse(String userToken) {
        User user = null;
        UsersRepositoryImpl userDao = new UsersRepositoryImpl();
        try {
            SignedJWT signedJWT = SignedJWT.parse(userToken);
            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
            Integer id = claimsSet.getIntegerClaim("id");
            String role = claimsSet.getStringClaim("role");
            String jsonKey = userDao.findVerifierById(id);
            RSAKey rsaKey = RSAKey.parse(jsonKey);
            JWSVerifier verifier = null;
            try {
                verifier = new RSASSAVerifier(rsaKey);
            } catch (JOSEException e) {
                //ignored
            }
            try {
                if (signedJWT.verify(verifier)) {
                    user = userDao.findById(id)
                                        .orElseThrow(IllegalStateException::new);
                }
            } catch (JOSEException e) {
                //ignore
            }
            return user;
        } catch (ParseException e) {
            return null;
        }
    }
}
