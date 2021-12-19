package com.matcher.events;

import java.util.UUID;

public class SliceDone extends Event {
    public UUID sliceId;

    public SliceDone(String msg, UUID chunkId) {
        super("SliceDone");
        this.sliceId = chunkId;
    }
}
