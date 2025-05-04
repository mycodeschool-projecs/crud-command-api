package com.example.serv1.rabbitMQListener;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class MyMessage <T>{

    private T content;

    private String message;
    private int priority;

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public int getPriority() {
        return priority;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "MyMessage{" +
                "content=" + content +
                ", message='" + message + '\'' +
                ", priority=" + priority +
                '}';
    }
}
