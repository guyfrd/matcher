package com.bigID;

import com.bigID.message.Message;

import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static List<String> readSearchKeys(String path) throws FileNotFoundException {
        BufferedReader reader = null;
        List<String> keys = null;
        try {
            reader = new BufferedReader(new FileReader(path));
            keys = reader.lines().collect(Collectors.toList());
        }catch (IOException e) {
        e.printStackTrace();
    }
        return keys;
    }

    public static void main(String[] args) throws IOException {

//        long numLines = (new BufferedReader(new FileReader("src/com/bigID/test"))).lines().count();
//        BufferedReader data = new BufferedReader(new FileReader("src/com/bigID/test"));
//        int numMatchers = 1;
//        int numLinesPerMatcher = 2;
        URL bigFile = new URL("http://norvig.com/big.txt");
        long numLines = (new BufferedReader(new InputStreamReader(bigFile.openStream()))).lines().count();
        BufferedReader data = new BufferedReader(new InputStreamReader(bigFile.openStream()));
        int numMatchers = 26;
        int numLinesPerMatcher = 1000;
        System.out.println("NUM LINES!! " + numLines);
        long charsOffset = 0;
        long rowsOffset = 0;
        String line = null;
        final List<String> keys = readSearchKeys("/home/guy/IdeaProjects/bigID/src/com/bigID/keys");
        ExecutorService pool = Executors.newFixedThreadPool(numMatchers);
        int numChunks = (int)Math.ceil(numLines/numLinesPerMatcher);
        System.out.println("numChunks: " + numChunks + "numLinesPerMatcher: " + numLinesPerMatcher);
        List<UUID> chunksId = new ArrayList<UUID>();
        BlockingQueue<Message> chunksQueue = new ArrayBlockingQueue<Message>(3000);

        for (int i = 0; i <= numChunks; i++) {
            int lineCount = 0;
            int currCharsOffset = 0;
            List<String> dataChunk = new ArrayList<String>();
            while (lineCount < numLinesPerMatcher && (line = data.readLine()) != null) {
                dataChunk.add(line);
//                System.out.println("line.length()" + line.length());
                currCharsOffset += line.length();
                lineCount++;
            }
            FileChunk chunk = new FileChunk(dataChunk,rowsOffset,charsOffset,keys);
            charsOffset += currCharsOffset;
//            System.out.println("charsOffset" + charsOffset +" currCharsOffset " + currCharsOffset);
            rowsOffset += numLinesPerMatcher;
            chunksId.add(chunk.id);
            Runnable m = new Matcher(chunk, chunksQueue);
            pool.execute(m);
        }

        Aggregator agg = new Aggregator(chunksQueue, chunksId);
        agg.start();
        pool.shutdown();
        data.close();
    }

}
