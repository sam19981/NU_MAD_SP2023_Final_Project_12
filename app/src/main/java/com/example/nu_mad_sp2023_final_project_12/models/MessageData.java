package com.example.nu_mad_sp2023_final_project_12.models;

import java.io.Serializable;

public class MessageData implements Serializable {

    String userId;
    String Message;
    String timestamp;
    String messageType;

    public MessageData() {
    }

    public MessageData(String userId, String message, String timestamp) {
        this.userId = userId;
        Message = message;
        this.timestamp = timestamp;
        messageType = "message";
    }

    public String getUserId() {
        return userId;
    }

    public String getMessage() {
        return Message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    @Override
    public String toString() {
        return "MessageData{" +
                "userId='" + userId + '\'' +
                ", Message='" + Message + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", messageType='" + messageType + '\'' +
                '}';
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
