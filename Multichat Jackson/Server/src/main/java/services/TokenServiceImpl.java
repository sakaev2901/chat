package services;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import context.Component;
import repositories.UsersRepository;

import models.User;

import java.text.ParseException;

public class TokenServiceImpl implements TokenService, Component {
    private UsersRepository usersRepository;

    public String getToken(Integer id, String role) throws JOSEException {
        RSAKey rsaKey = new RSAKeyGenerator(2048).keyID(role + id).generate();
        RSAKey rsaKeyPublic = rsaKey.toPublicJWK();
        usersRepository.updateVerifier(rsaKeyPublic.toString(), id);
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
        try {
            SignedJWT signedJWT = SignedJWT.parse(userToken);
            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
            Integer id = claimsSet.getIntegerClaim("id");
            String role = claimsSet.getStringClaim("role");
            String jsonKey = usersRepository.findVerifierById(id);
            RSAKey rsaKey = RSAKey.parse(jsonKey);
            JWSVerifier verifier = null;
            try {
                verifier = new RSASSAVerifier(rsaKey);
            } catch (JOSEException e) {
                //ignored
            }
            try {
                if (signedJWT.verify(verifier)) {
                    user = usersRepository.findById(id)
                                        .orElse(new User());
                } else {
                    user = new User();
                }
            } catch (JOSEException e) {
                    user = new User();
            }
            return user;
        } catch (ParseException e) {
            return new User();
        }
    }

    @Override
    public String getComponentName() {
        return "tokenServiceImpl";
    }


}
