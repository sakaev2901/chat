package protocol;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Message;
import models.Payload;

public class Response<T> {

    private T data;
    private Payload payload;

    public Response(T data) {
        this.data = data;
        buildPayload();
    }

    private void buildPayload() {
        payload = new Payload();
        if (data instanceof Message) {
            payload.setHeader("Message");
            payload.setPayload(data);
        }
    }

    public static <T> Response<T> build(T data) {
        return new Response<>(data);
    }

    public String toJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }

}
