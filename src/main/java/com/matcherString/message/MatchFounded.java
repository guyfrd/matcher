package com.matcherString.message;

public class MatchFounded extends Message {

    String key;
    long lineOffset;
    long charOffset;
    String msg;

    public MatchFounded(String key, long lineOffset, long charOffset, String msg) {
        super(msg);
        this.key = key;
        this.lineOffset = lineOffset;
        this.charOffset = charOffset;
        this.msg = msg;
    }

    public String getKey() {
        return key;
    }

    public long getLineOffset() {
        return lineOffset;
    }

    public long getCharOffset() {
        return charOffset;
    }

    @Override
    public void printMsg() {
        System.out.format("%s [lineOffset: %d charOffset: %d]\n", this.key, this.lineOffset, this.charOffset);
    }
}



