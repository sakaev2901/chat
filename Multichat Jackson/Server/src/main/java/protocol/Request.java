package protocol;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.Payload;

import java.io.IOException;
import java.util.LinkedHashMap;

public class Request {

    private Payload payload;
    LinkedHashMap<String, String> commandPayload;

    public Request(String jsonRequest) {
        decodeRequest(jsonRequest);
    }

    private void decodeRequest(String jsonRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            this.payload = objectMapper.readValue(jsonRequest, Payload.class);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public String getCommand() {
        if (this.payload.getHeader().equals("Command")) {
            this.commandPayload = (LinkedHashMap<String, String>)payload.getPayload();
            return commandPayload.get("command");
        } else {
            return payload.getHeader();
        }
    }

    public String getParameter(String name) {
        return commandPayload.get(name);
    }

}
