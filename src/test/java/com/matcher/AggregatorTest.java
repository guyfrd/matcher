package com.matcher;

import com.matcher.message.*;

import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class AggregatorTest {
    @Test
    public void testAggregatorMatch() throws InterruptedException {
        BlockingQueue<Message> aggrQueue = new ArrayBlockingQueue<Message>(100);
        Aggregator agg = new Aggregator(aggrQueue);
        agg.start();

        List<MatchFounded> input = new ArrayList<MatchFounded>();
        input.add(new MatchFounded("TEST1", 123, 456, "TEST MATCH1"));
        input.add(new MatchFounded("TEST2", 8888, 9999, "TEST MATCH2"));
        input.add(new MatchFounded("TEST3", 1212, 34343, "TEST MATCH4"));

        for (MatchFounded m : input) {
            aggrQueue.put(m);
        }

        List<MatchFounded> resMatchFoundeds = agg.getMatches();
        aggrQueue.put(new FileDone("done"));

        Assert.assertEquals(resMatchFoundeds, input);
    }
}