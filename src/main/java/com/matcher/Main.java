package com.matcher;

import com.matcher.message.FileDone;
import com.matcher.message.Message;
import org.ahocorasick.trie.Trie;

import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class Main {
    public static List<String> readSearchKeys(String path) throws FileNotFoundException {
        BufferedReader reader = null;
        List<String> keys = null;
        try {
            reader = new BufferedReader(new FileReader(path));
            keys = reader.lines().collect(Collectors.toList());
        } catch (IOException e) {
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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return props;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
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
        final List<String> keys = readSearchKeys(prop.getProperty("KEY_FILE"));
        Trie trie = Trie.builder().onlyWholeWords().addKeywords(keys).build();

        ExecutorService matcherPool = Executors.newFixedThreadPool(numMatchers);
        BlockingQueue<Message> aggrQueue = new ArrayBlockingQueue<Message>(queueCap);
        List<String> sliceData = new ArrayList<String>();
        Aggregator agg = new Aggregator(aggrQueue);
        URL bigFileUrl = new URL(prop.getProperty("URL"));
        BufferedReader data = new BufferedReader(new InputStreamReader(bigFileUrl.openStream()));

        agg.start();

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
        long endTime   = System.nanoTime();
        long totalTime = endTime - startTime;

        slice = new Slice(sliceData, rowsOffset, fileCharsOffset);
        matcherPool.execute(new Matcher(slice, aggrQueue, trie));
        matcherPool.shutdown();
        matcherPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        data.close();
        aggrQueue.put(new FileDone("file done"));
        agg.printMatches();
        System.out.println("total search time: " + totalTime / 1_000_000_000 + "sec");
    }
}



