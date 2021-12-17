package com.bigID;

import com.bigID.message.ChunkDone;
import com.bigID.message.Match;
import com.bigID.message.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;

public class Aggregator extends Thread {
    BlockingQueue<Message> chunkQueue;
    List<UUID>  chunks;
    List<Match> matches;
    public Aggregator(BlockingQueue msgQueue,  List<UUID> chunks) {
        this.chunkQueue = msgQueue;
        this.chunks = chunks;
        this.matches = new ArrayList<Match>();
    }

    @Override
    public void run() {
        while (!chunks.isEmpty()) {
            try {
                Message msg = chunkQueue.take();
                if (msg instanceof ChunkDone) {
//                    System.out.println("agg! ChunkDone " + ((ChunkDone) msg).chunkId);
                    chunks.remove(((ChunkDone) msg).chunkId);
                } else if (msg instanceof Match) {
//                    System.out.println("agg! Match");
                    this.matches.add((Match) msg);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("ALLLL FINSH");
        for (Match m: this.matches) {
            m.printMsg();
        }
    }
}
