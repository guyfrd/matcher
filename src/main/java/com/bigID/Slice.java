package com.bigID;

import java.util.List;
import java.util.UUID;

public class Slice {
    public UUID id;
    public List<String> data;
    public long rowsOffset;
    public long charsOffset;

    public Slice(List<String> data, long rowsOffset, long charsOffset) {
        this.id = UUID.randomUUID();
        this.data = data;
        this.rowsOffset = rowsOffset;
        this.charsOffset = charsOffset;
    }
}
