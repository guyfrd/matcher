package com.bigID.message;

import com.bigID.message.Message;

public class Match extends Message {
    public String key;
    public long lineOffset;
    public long charOffset;
    public String msg;

    public Match(String key, long lineOffset, long charOffset, String msg) {
        super("new match");
        this.key = key;
        this.lineOffset = lineOffset;
        this.charOffset = charOffset;
        this.msg = msg;
    }

    @Override
    public void printMsg() {
        System.out.format("%s [lineOffset: %d charOffset: %d]\n", this.key, this.lineOffset, this.charOffset);
    }
}



