package models;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Message {
    private String  senderName;
    private String text;
    private String timeStamp;
    private Integer id;

    public Message() {

    }

    public Message(String text) {
        this.text = text;
        timeStamp =new SimpleDateFormat("yyyy.MM.dd_HH:mm:ss").format(Calendar.getInstance().getTime());
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String sender) {
        this.senderName = sender;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
