package services;

import com.nimbusds.jose.JOSEException;
import models.User;

public interface TokenService {
    String getToken(Integer id, String role) throws JOSEException;
    User parse(String userToken);
}
