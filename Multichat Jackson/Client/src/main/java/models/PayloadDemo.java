package models;

import java.util.LinkedHashMap;

public class PayloadDemo {
    private String header;
    private LinkedHashMap<String, Object> payload;

    public PayloadDemo(String header, LinkedHashMap<String, Object> payload) {
        this.header = header;
        this.payload = payload;
    }

    public PayloadDemo() {

    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public LinkedHashMap<String, Object> getPayload() {
        return payload;
    }

    public void setPayload(LinkedHashMap<String, Object> payload) {
        this.payload = payload;
    }
}
