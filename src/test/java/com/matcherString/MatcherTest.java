package com.matcherString;

import com.matcherString.message.MatchFounded;
import com.matcherString.message.Message;
import org.ahocorasick.trie.Trie;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class MatcherTest {
    @Test
    public void testMatchSmallText() {
        String[] rowData = ("The ArthurProjectby Sir Arthur Conan Doyle(#15 in our series by Sir Arthur Conan Doyle)\n" +
                "\n" +
                "Copyright laws are changing all over the world. Be sure to check the\n" +
                "copyright laws for your country before downloading or redistributing information\n" +
                "this or any other Project ArthurGutenberg eBook.\n" +
                "\n" +
                "This header should be the first thing seen when viewing this Project\n" +
                "Gutenberg file.  Please do not remove it.  Do not change or edit the\n" +
                "header without written permission.\n" +
                "\n" +
                "Please read the \"legal small print,\" and other information about the\n" +
                "eBook and Project Gutenberg at the bottom of this file.  Included is\n" +
                "important information about your specific rights and restrictions in\n" +
                "how the file may be used.  You can also find out about how to make a\n" +
                "donation to Project Gutenberg, and how to get involved.information\n" +
                "\n" +
                "ES OF SHERLOCK HOLMES ***information\n" +
                "\n" +
                "information").split("\\r?\\n");
        int exceptedMatches = 3;
        Map<String, MatchFounded> matchesMap = new HashMap<>();
        Trie trie = Trie.builder()
                .addKeyword("important")
                .addKeyword("downloading")
                .addKeyword("Included")
                .build();
        matchesMap.put("Included", new MatchFounded("Included", 11,589, "m1"));
        matchesMap.put("downloading", new MatchFounded("downloading", 3,197, "m2"));
        matchesMap.put("important", new MatchFounded("important", 12,601, "m3"));

        Slice s = new Slice(Arrays.asList(rowData), 0, 0);

        BlockingQueue<Message> queue = new ArrayBlockingQueue<Message>(100);

        Matcher newMatcher = new Matcher(s,queue, trie);
        newMatcher.run();
        for (int i = 0; i < exceptedMatches; i++) {
            try {
                Message msg = queue.take();
                String key = ((MatchFounded)msg).getKey();

                Assert.assertEquals(matchesMap.get(key).getCharOffset(),((MatchFounded) msg).getCharOffset());
                Assert.assertEquals(matchesMap.get(key).getLineOffset(),((MatchFounded) msg).getLineOffset());

                ((MatchFounded)msg).printMsg();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}