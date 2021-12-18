package com.matcher;

import com.matcher.message.FileDone;
import com.matcher.message.MatchFounded;
import com.matcher.message.Message;

import java.util.*;
import java.util.concurrent.BlockingQueue;

public class Aggregator extends Thread {
    BlockingQueue<Message> chunkQueue;
    Map<String, List<MatchFounded>> matchesMap;

    public Aggregator(BlockingQueue msgQueue) {
        this.chunkQueue = msgQueue;
        this.matchesMap = new LinkedHashMap<String, List<MatchFounded>>();
    }

    public void printMatches() {
        this.matchesMap.forEach((key, keyMatches) -> {
            System.out.print(key + "->");
            keyMatches.forEach((match) -> match.printMsg());
            System.out.println();
        });
    }

    public List<MatchFounded> getMatches(String key) {
        return this.matchesMap.get(key);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Message msg = chunkQueue.take();
                if (msg instanceof FileDone) {
                    break;
                } else if (msg instanceof MatchFounded) {
                    MatchFounded match = (MatchFounded)msg;
                    if (this.matchesMap.containsKey(match.key) == false) {
                        this.matchesMap.put(match.key, new ArrayList<MatchFounded>());
                    }
                    this.matchesMap.get(match.key).add(match);

//                    this.matchFoundeds.add((MatchFounded) msg);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
