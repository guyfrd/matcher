package com.bigID.message;

public class FileDone extends Message{
    String Msg;

    public FileDone(String msg) {
        super(msg);
    }

    @Override
    void printMsg() {
        System.out.println(msg);
    }
}
