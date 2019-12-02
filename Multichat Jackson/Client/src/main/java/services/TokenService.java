package services;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import models.Payload;
import net.Session;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.Scanner;

public class TokenService {
    public void saveToken(String request) {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType type = objectMapper.getTypeFactory().constructParametricType(Payload.class, String.class);
        Payload<String> payloadToken = null;
        try {
            payloadToken = objectMapper.readValue(request, type);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        String token = payloadToken.getPayload();
        try {
            SignedJWT signedJWT =SignedJWT.parse(token);
            JWTClaimsSet tokenSet = signedJWT.getJWTClaimsSet();
            String role = tokenSet.getStringClaim("role");
            Integer id = tokenSet.getIntegerClaim("id");
            Session.getInstance(role, id);
        } catch (ParseException e) {
            throw new IllegalStateException(e);
        }
        try(FileWriter writer = new FileWriter("token.txt", false)) {
            writer.write(token);
            writer.flush();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public String getToken() {
        String token = null;
        try(FileReader fileReader = new FileReader("token.txt")) {
            Scanner scanner = new Scanner(fileReader);
            if (scanner.hasNextLine()) {
                token = scanner.nextLine();
            }
        } catch (IOException e) {

        }
        return token;
    }

    public void deleteToken() {
        try(FileWriter writer = new FileWriter("token.txt", false)) {
            writer.write("");
            writer.flush();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
