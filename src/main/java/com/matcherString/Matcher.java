package com.matcherString;

import com.matcherString.message.MatchFounded;
import com.matcherString.message.Message;
import org.ahocorasick.trie.Emit;
import org.ahocorasick.trie.Trie;

import java.util.Collection;
import java.util.concurrent.BlockingQueue;

public class Matcher implements Runnable{

    Slice slice;
    BlockingQueue<Message> resQueue;
    Trie trie;

    public Matcher(Slice slice, BlockingQueue<Message> resQueue, Trie trie){
        this.slice = slice;
        this.resQueue = resQueue;
        this.trie = trie;
    }

    public void run() {
        long chunkCharOffset = this.slice.charsOffset;
        long chunkRowsOffset = this.slice.rowsOffset;

        long startTime = System.nanoTime();
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

        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;
//        System.out.println("matcher: num lines:" + this.slice.data.size() + "time" + totalTime / 1_000_000 + "ms");

    }

}

