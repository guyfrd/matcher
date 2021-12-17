package com.bigID;

import java.util.List;
import java.util.UUID;

public class FileChunk {
    public UUID id;
    public List<String> data;
    public long rowsOffset;
    public long charsOffset;
    public List<String> searchKeys;
    public String status;

    public FileChunk(List<String> data, long rowsOffset, long charsOffset, List<String> searchKeys) {
        this.id = UUID.randomUUID();
        this.data = data;
        this.rowsOffset = rowsOffset;
        this.charsOffset = charsOffset;
        this.searchKeys = searchKeys;
        this.status = "created";
    }
}
