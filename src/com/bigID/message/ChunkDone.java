package com.bigID.message;

import com.bigID.message.Message;

import java.util.UUID;

public class ChunkDone extends Message {
    public UUID chunkId;
    public String msg;

    public ChunkDone(String msg, UUID chunkId) {
        super(msg);
        this.msg = msg;
        this.chunkId = chunkId;
    }

    @Override
    void printMsg() {
        System.out.println(this.msg);
    }
}
