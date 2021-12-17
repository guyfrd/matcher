package com.bigID.message;

public abstract class Message {
    public String msg;

    public Message(String msg) {
        this.msg = msg;
    }

    abstract void printMsg();
}
