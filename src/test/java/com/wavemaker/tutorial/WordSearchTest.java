package com.wavemaker.tutorial;

import org.junit.Test;

import java.io.File;

import java.io.IOException;
import java.util.*;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by srujant on 14/6/16.
 */

public class WordSearchTest {

    private static Logger logger = LoggerFactory.getLogger(WordSearchTest.class.getName());

    @Test
    public void testSearchDirectoryLatency() throws IOException, InterruptedException {
        WordSearch wordSearch = new WordSearch();
        File file = new File("/home/srujant/temp/temp6");
        for (int i=0; i< 20; i++) {
            int numberOfThreads = i;
            long startTime = System.currentTimeMillis();
            wordSearch.searchDirectory(file, "random", true, numberOfThreads);
            long endTime = System.currentTimeMillis();
            logger.info("Latency for {} threads is {}ms", numberOfThreads, endTime - startTime);
        }
    }

    @Test
    public void testSearchFile() throws IOException, InterruptedException {
        boolean recursiveSearch = true;

        Map<File, List<SearchResult>> expectedSearchResults = new HashMap();

        List<SearchResult> expectedCoordinates = new ArrayList<>();
        expectedCoordinates.add(new SearchResult(1, 7));
        expectedCoordinates.add(new SearchResult(2, 0));
        expectedCoordinates.add(new SearchResult(3, 0));
        expectedCoordinates.add(new SearchResult(6, 0));
        expectedCoordinates.add(new SearchResult(6, 31));
        expectedCoordinates.add(new SearchResult(7, 85));
        expectedCoordinates.add(new SearchResult(8, 0));

        expectedSearchResults.put(new File("src/test/resources/data/file3.txt"), expectedCoordinates);
        expectedSearchResults.put(new File("src/test/resources/data/tempFile.txt"), expectedCoordinates);
        expectedSearchResults.put(new File("src/test/resources/data/test.txt"), expectedCoordinates);
        expectedSearchResults.put(new File("src/test/resources/data/file5"), expectedCoordinates);
        expectedSearchResults.put(new File("src/test/resources/data/file6"), expectedCoordinates);
        expectedSearchResults.put(new File("src/test/resources/data/file7"), expectedCoordinates);
        expectedSearchResults.put(new File("src/test/resources/data/innerDirectory/file1.txt"), expectedCoordinates);
        expectedSearchResults.put(new File("src/test/resources/data/innerDirectory/file2.txt"), expectedCoordinates);
        expectedSearchResults.put(new File("src/test/resources/data/innerDirectory/file3.txt"), expectedCoordinates);

        List<SearchResult> expectedCoordinatesForFile4 = new ArrayList<>();
        expectedCoordinatesForFile4.add(new SearchResult(3, 0));
        expectedCoordinatesForFile4.add(new SearchResult(5, 0));
        expectedSearchResults.put(new File("src/test/resources/data/file4.txt"), expectedCoordinatesForFile4);

        assertTestSearchString(new File("src/test/resources/data"), "java", expectedSearchResults, recursiveSearch, 3);
    }


    private void assertTestSearchString(File file, final String searchPattern, final Map<File, List<SearchResult>> expectedSearchResults, final boolean recursiveSearch, int concurrency) throws IOException, InterruptedException {

        File fileObj;
        final WordSearch obj = new WordSearch();
        Map<File, List<SearchResult>> result = new HashMap<>();
        result = obj.searchDirectory(file, searchPattern, recursiveSearch, concurrency);
        Assert.assertEquals(expectedSearchResults.size(), result.size());
        Set<File> keySet = result.keySet();
        Iterator keySetIterator = keySet.iterator();

        while (keySetIterator.hasNext()) {

            fileObj = (File) keySetIterator.next();
            List<SearchResult> resultList = result.get(fileObj);
            List<SearchResult> expectedCoordinates = expectedSearchResults.get(fileObj);

            Iterator resultListIterator = resultList.iterator();
            Iterator expectedCoordinatesIterator = expectedCoordinates.iterator();

            while (resultListIterator.hasNext()) {
                SearchResult resultObject = (SearchResult) resultListIterator.next();
                SearchResult expectedObject = (SearchResult) expectedCoordinatesIterator.next();
                Assert.assertEquals(expectedObject.getLineNumber(), resultObject.getLineNumber());
                Assert.assertEquals(expectedObject.getColumnNumber(), resultObject.getColumnNumber());
            }
        }



        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    File fileObj = new File("src/test/resources/data/file3.txt");
                    List<SearchResult> resultList;
                    resultList = obj.searchFile(fileObj, "java");

                    List<SearchResult> expectedCoordinates = expectedSearchResults.get(fileObj);

                    Assert.assertEquals(expectedCoordinates.size(), resultList.size());
                    Iterator expectedCoordinatesIterator = expectedCoordinates.iterator();
                    Iterator resultListIterator = resultList.iterator();

                    while (expectedCoordinatesIterator.hasNext()) {
                        SearchResult resultObject = (SearchResult) resultListIterator.next();
                        SearchResult expectedObject = (SearchResult) expectedCoordinatesIterator.next();
                        //System.out.println("Thread-1 "+resultObject.getLineNumber() + " : " + resultObject.getColumnNumber());
                        Assert.assertEquals(expectedObject.getLineNumber(), resultObject.getLineNumber());
                        Assert.assertEquals(expectedObject.getColumnNumber(), resultObject.getColumnNumber());
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    File fileObj = new File("src/test/resources/data/file4.txt");
                    List<SearchResult> resultList;

                    resultList = obj.searchFile(fileObj, "java");

                    List<SearchResult> expectedCoordinates = expectedSearchResults.get(fileObj);

                    Assert.assertEquals(expectedCoordinates.size(), resultList.size());

                    Iterator expectedCoordinatesIterator = expectedCoordinates.iterator();
                    Iterator resultListIterator = resultList.iterator();
                    while (expectedCoordinatesIterator.hasNext()) {
                        SearchResult resultObject = (SearchResult) resultListIterator.next();
                        SearchResult expectedObject = (SearchResult) expectedCoordinatesIterator.next();
                        Assert.assertEquals(expectedObject.getLineNumber(), resultObject.getLineNumber());
                        Assert.assertEquals(expectedObject.getColumnNumber(), resultObject.getColumnNumber());
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
    }
}

