package services;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

public class TokenService {
    public void saveToken(String request) {
        try(FileWriter writer = new FileWriter("token.txt", false)) {
            writer.write(request);
            writer.flush();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
