package com.bigID;

import com.bigID.message.ChunkDone;
import com.bigID.message.Match;
import com.bigID.message.Message;

import java.util.concurrent.BlockingQueue;

public class Matcher implements Runnable{

    FileChunk chunk;
    BlockingQueue<Message> resQueue;

    public Matcher(FileChunk chunk, BlockingQueue<Message> resQueue) {
        this.chunk = chunk;
        this.resQueue = resQueue;
    }

    public void run()
        {
            long chunkCharOffset = this.chunk.charsOffset;
            long chunkRowsOffset = this.chunk.rowsOffset;
//            System.out.println("@@@@@matcher: " + chunkRowsOffset);
            for (String line: this.chunk.data) {
//                System.out.println("START LOOP OFFSET " + chunkCharOffset);
                for (String key : this.chunk.searchKeys) {
                    if (line.contains(key)) {
                        try {
//                            System.out.println("******match founded index " + line.indexOf(key) + "chunkCharOffset " + chunkCharOffset);

                            resQueue.put(new Match(key, chunkRowsOffset, chunkCharOffset + line.indexOf(key), "match founded"));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
//                System.out.println("line length " + line.length() + " chunkCharOffset " + chunkCharOffset);
                chunkRowsOffset ++;
                chunkCharOffset += line.length() + 1;

            }
            try {
                resQueue.put(new ChunkDone("chunk done",chunk.id));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
}

