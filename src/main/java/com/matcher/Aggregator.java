package com.matcher;

import com.matcher.events.FileDone;
import com.matcher.events.MatchFounded;
import com.matcher.events.Event;

import java.util.*;
import java.util.concurrent.BlockingQueue;

public class Aggregator extends Thread {
    BlockingQueue<Event> chunkQueue;
    Map<String, List<MatchFounded>> matchesMap;

    public Aggregator(BlockingQueue msgQueue) {
        this.chunkQueue = msgQueue;
        this.matchesMap = new LinkedHashMap<String, List<MatchFounded>>();
    }

    public void printMatches() {
        this.matchesMap.forEach((key, keyMatches) -> {
            System.out.print(key + "->");
            keyMatches.forEach((match) -> match.printMatch());
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
                Event msg = chunkQueue.take();
                if (msg instanceof FileDone) {
                    break;
                } else if (msg instanceof MatchFounded) {
                    MatchFounded match = (MatchFounded)msg;
                    if (this.matchesMap.containsKey(match.key) == false) {
                        this.matchesMap.put(match.key, new ArrayList<MatchFounded>());
                    }
                    this.matchesMap.get(match.key).add(match);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
