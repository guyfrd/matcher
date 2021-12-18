package com.bigID.message;

import java.util.UUID;

public class SliceDone extends Message {
    public UUID chunkId;
    public String msg;

    public SliceDone(String msg, UUID chunkId) {
        super(msg);
        this.msg = msg;
        this.chunkId = chunkId;
    }

    @Override
    void printMsg() {
        System.out.println(this.msg);
    }
}
