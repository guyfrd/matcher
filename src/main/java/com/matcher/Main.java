package com.matcher;

import com.matcher.events.FileDone;
import com.matcher.events.Event;
import org.ahocorasick.trie.Trie;

import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class Main {
    public static List<String> readSearchKeys(String path) {
        BufferedReader reader = null;
        List<String> keys = null;
        try {
            reader = new BufferedReader(new FileReader(path));
            keys = reader.lines().collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return keys;
    }
    public static Properties readConfig() {
        File configFile = new File("src/main/matcher.config");
        FileReader reader = null;
        Properties props = null;
        try {
            reader = new FileReader(configFile);
            props = new Properties();
            props.load(reader);
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return props;
    }

    public static BufferedReader createReader (Properties prop) {
        BufferedReader data = null;
        try {
            if (prop.getProperty("INPUT_SOURCE").equals("FS")) {
                data = new BufferedReader(new FileReader(prop.getProperty("INPUT_PATH")));
            } else if (prop.getProperty("INPUT_SOURCE").equals("URL")) {
                URL bigFileUrl = new URL(prop.getProperty("INPUT_PATH"));
                data = new BufferedReader(new InputStreamReader(bigFileUrl.openStream()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

    public static void main(String[] args) {
        Properties prop = readConfig();
        int numMatchers = Integer.parseInt(prop.getProperty("MAX_MATCHERS"));
        int numLinesPerMatcher = Integer.parseInt(prop.getProperty("MAX_LINE_PER_MATCHER"));
        int queueCap = 3000;

        long fileCharsOffset = 0;
        long sliceCharsOffset = 0;
        long rowsOffset = 0;
        String line = null;
        Slice slice = null;
        int currRowCount = 0;

        List<String> keys = readSearchKeys(prop.getProperty("KEY_FILE"));
        Trie trie = null;
        if (prop.getProperty("CASE_SENSITIVE").equals("false")) {
            trie = Trie.builder().onlyWholeWords().ignoreCase().addKeywords(keys).build();
        } else {
            trie = Trie.builder().onlyWholeWords().addKeywords(keys).build();
        }
        ExecutorService matcherPool = Executors.newFixedThreadPool(numMatchers);
        BlockingQueue<Event> aggrQueue = new ArrayBlockingQueue<Event>(queueCap);
        List<String> sliceData = new ArrayList<String>();
        Aggregator agg = new Aggregator(aggrQueue);
        BufferedReader data = createReader(prop);
        agg.start();

        try {
            sliceData.add(data.readLine());
            currRowCount++;

            long startTime = System.nanoTime();
            while ((line = data.readLine()) != null) {
                if (currRowCount % numLinesPerMatcher == 0) {
                    slice = new Slice(sliceData, rowsOffset, fileCharsOffset);
                    matcherPool.execute(new Matcher(slice, aggrQueue, trie));
                    sliceData = new ArrayList<String>();
                    fileCharsOffset += sliceCharsOffset;
                    rowsOffset += numLinesPerMatcher;
                }
                currRowCount++;
                sliceCharsOffset += line.length();
                sliceData.add(line);
            }
            long endTime = System.nanoTime();
            long totalTime = endTime - startTime;

            System.out.println("last slice "+ rowsOffset +"--" +sliceData.size() );
            slice = new Slice(sliceData, rowsOffset, fileCharsOffset);
            matcherPool.execute(new Matcher(slice, aggrQueue, trie));

            matcherPool.shutdown();

            matcherPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

            data.close();
            aggrQueue.put(new FileDone());
            agg.printMatches();
            System.out.println("total search time: " + totalTime / 1_000_000 + "ms");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



