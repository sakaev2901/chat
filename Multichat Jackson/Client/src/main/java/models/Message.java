package models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Message {
    private String senderName;
    private String text;
    private String timeStamp;
    private Integer id;

    public Message(@JsonProperty("senderName")String senderName,
                   @JsonProperty("text")String text,
                   @JsonProperty("timeStamp")String timeStamp,
                   @JsonProperty("id")Integer id) {
        this.senderName = senderName;
        this.text = text;
        this.timeStamp = timeStamp;
        this.id = id;
    }

    public Message(String text) {
        this.text = text;
        timeStamp =new SimpleDateFormat("yyyy.MM.dd_HH:mm:ss").format(Calendar.getInstance().getTime());
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
