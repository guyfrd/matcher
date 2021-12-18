package com.matcher.message;

import java.util.UUID;

public class SliceDone extends Message {
    public UUID sliceId;
    String msg;

    public SliceDone(String msg, UUID chunkId) {
        super(msg);
        this.msg = msg;
        this.sliceId = chunkId;
    }

    @Override
    void printMsg() {
        System.out.println(this.msg);
    }
}
