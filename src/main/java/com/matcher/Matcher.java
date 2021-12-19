package com.matcher;

import com.matcher.events.MatchFounded;
import com.matcher.events.Event;
import org.ahocorasick.trie.Emit;
import org.ahocorasick.trie.Trie;

import java.util.Collection;
import java.util.concurrent.BlockingQueue;

public class Matcher implements Runnable{

    Slice slice;
    BlockingQueue<Event> resQueue;
    Trie trie;

    public Matcher(Slice slice, BlockingQueue<Event> resQueue, Trie trie){
        this.slice = slice;
        this.resQueue = resQueue;
        this.trie = trie;
    }

    public void run() {
        long chunkCharOffset = this.slice.charsOffset;
        long chunkRowsOffset = this.slice.rowsOffset;

        for (String line: this.slice.data) {
                Collection<Emit> emits = trie.parseText(line);
                for (Emit e : emits) {
                    try {
                        resQueue.put(new MatchFounded(e.getKeyword(), chunkRowsOffset, chunkCharOffset + e.getStart(), "match founded"));
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                chunkRowsOffset++;
                chunkCharOffset += line.length() + 1;

            }
    }

}

