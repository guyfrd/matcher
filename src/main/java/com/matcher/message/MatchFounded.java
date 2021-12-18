package com.matcher.message;

public class MatchFounded extends Message {

    public String key;
    public long lineOffset;
    public long charOffset;
    String msg;

    public MatchFounded(String key, long lineOffset, long charOffset, String msg) {
        super(msg);
        this.key = key;
        this.lineOffset = lineOffset;
        this.charOffset = charOffset;
        this.msg = msg;
    }

    @Override
    public void printMsg() {
        System.out.format("[lineOffset: %d charOffset: %d],", this.lineOffset, this.charOffset);
    }
}



