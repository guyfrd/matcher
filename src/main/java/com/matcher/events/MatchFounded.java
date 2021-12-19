package com.matcher.events;

public class MatchFounded extends Event {

    public String key;
    public long lineOffset;
    public long charOffset;

    public MatchFounded(String key, long lineOffset, long charOffset, String msg) {
        super("MatchFounded");
        this.key = key;
        this.lineOffset = lineOffset;
        this.charOffset = charOffset;
    }

    public void printMatch() {
        System.out.format("[lineOffset: %d charOffset: %d],", this.lineOffset, this.charOffset);
    }
}



