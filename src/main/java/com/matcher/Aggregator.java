package com.matcher;

import com.matcher.message.FileDone;
import com.matcher.message.MatchFounded;
import com.matcher.message.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class Aggregator extends Thread {
    BlockingQueue<Message> chunkQueue;

    List<MatchFounded> matchFoundeds;

    public Aggregator(BlockingQueue msgQueue) {
        this.chunkQueue = msgQueue;
        this.matchFoundeds = new ArrayList<MatchFounded>();
    }

    public void printMatches() {
        for (MatchFounded m: this.matchFoundeds) {
            m.printMsg();
        }
    }

    public List<MatchFounded> getMatches() {
        return matchFoundeds;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Message msg = chunkQueue.take();
                if (msg instanceof FileDone) {
//                    System.out.println("agg! ChunkDone " + ((ChunkDone) msg).chunkId);
                    break;
                } else if (msg instanceof MatchFounded) {
//                    System.out.println("agg! Match");
                    this.matchFoundeds.add((MatchFounded) msg);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
