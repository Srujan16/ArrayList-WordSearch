package com.wavemaker.tutorial;


import java.io.*;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by srujant on 14/6/16.
 */

/*
Program to Search occurence of given word in a directory which may contain both directories and files.
Search operation can be done linearly or concurrently. Concurrent programming using reduces overall Computaion time.


User is provided with provision to specify number of threads<code>concurrentNumberOfThreads</code> that
can be invoked, to concurrently perform search Operation. A flag<code>recursiveSearch</code> is used
to specify whether, recursive search in directories and sub-directories is allowed or not.

searchFile gets a singleFile as input to search.
*/


public class WordSearch {

    private static Logger logger = LoggerFactory.getLogger(WordSearch.class.getName());

    public List<SearchResult> searchFile(File file, String searchPattern) throws IOException {

        List<SearchResult> resultList = new ArrayList<>();
        int index = 0;
        int lineNumber = 0;
        BufferedReader br = null;
        String line;
        try {
            br = new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null) {
                lineNumber++;
                while ((index = line.indexOf(searchPattern, index)) != -1) {
                    resultList.add(new SearchResult(lineNumber, index));
                    index++;
                }
                index = 0;
            }
        } catch (FileNotFoundException fe) {
            throw new RuntimeException("fileNotFound", fe);
        } finally {
            if (br != null) {
                br.close();
            }
        }
        return resultList;
    }

    public static class SearchContext {

        private String searchPattern;
        private File file;
        private boolean recursiveSearch;
        private int concurrentNumberOfThreads;
        final Map<File, List<SearchResult>> result = new HashMap();
        final BlockingQueue<File> queue = new ArrayBlockingQueue<>(10);
        boolean producersEOF = false;
        int x;

        SearchContext(String searchPattern, File file, boolean recursiveSearch, int concurrentNumberOfthreads) {
            this.searchPattern = searchPattern;
            this.file = file;
            this.recursiveSearch = recursiveSearch;
            this.concurrentNumberOfThreads = concurrentNumberOfthreads;
        }

        public String getSearchPattern() {
            return searchPattern;
        }

        public File getFile() {
            return file;
        }

        public boolean isRecursiveSearch() {
            return recursiveSearch;
        }

        public int getConcurrentNumberOfThreads() {
            return concurrentNumberOfThreads;
        }

        public Map<File, List<SearchResult>> getResult() {
            return result;
        }

        public BlockingQueue<File> getQueue() {
            return queue;
        }


        public void setProducersEOF(boolean producersEOF) {
            this.producersEOF = producersEOF;
        }

        public boolean isProducersEOF() {
            return producersEOF;
        }
    }

    public Map<File, List<SearchResult>> searchDirectory(final File file, String searchPattern, final boolean recursiveSearch, int concurrentNumberOfThreads) throws IOException, InterruptedException {

        if (concurrentNumberOfThreads > 1) {
            final WordSearch.SearchContext searchContext = new WordSearch.SearchContext(searchPattern, file, recursiveSearch, concurrentNumberOfThreads);

            Thread getFilesThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        enQueueTasks(searchContext, searchContext.getFile());
                    } catch (InterruptedException e) {
                        logger.warn("Producer Thread interrupted", e);
                    } finally {
                        searchContext.setProducersEOF(true);
                    }
                }
            });
            getFilesThread.start();
            return searchDirectoryConcurrently(searchContext);
        } else {
            final Map<File, List<SearchResult>> result = new HashMap();
            return searchDirectory(file, searchPattern, recursiveSearch, result);
        }
    }


    private void enQueueTasks(final SearchContext searchContext, File file) throws InterruptedException {
        String path = file.getPath();
        String[] list = file.list();
        File innerFile;
        for (int i = 0; i < list.length; i++) {
            innerFile = new File(path + "/" + list[i]);
            if (innerFile.isDirectory()) {
                if (searchContext.isRecursiveSearch()) {
                    enQueueTasks(searchContext, innerFile);
                }
            } else {
                searchContext.getQueue().put(innerFile);
            }
        }
    }

    private Map<File, List<SearchResult>> searchDirectoryConcurrently(final SearchContext searchContext) throws IOException, InterruptedException {
        ConsumerTask consumerTask = new ConsumerTask(searchContext);
        List<Thread> consumerThreads = new ArrayList<>();
        for (int i=0; i < searchContext.getConcurrentNumberOfThreads(); i++) {
            Thread t = new Thread(consumerTask);
            consumerThreads.add(t);
            t.start();
        }
        for (Thread thread : consumerThreads) {
            thread.join();
        }
        return searchContext.getResult();
    }

    private Map<File, List<SearchResult>> searchDirectory(final File file, final String searchPattern, boolean recursiveSearch, final Map<File, List<SearchResult>> result) throws IOException {
        String path = file.getPath();
        String[] list = file.list();
        for (String entry : list) {
            final File innerFile = new File(path + "/" + entry);
            if (innerFile.isDirectory()) {
                if (recursiveSearch) {
                    searchDirectory(innerFile, searchPattern, recursiveSearch, result);
                }
            } else result.put(innerFile, searchFile(innerFile, searchPattern));
        }
        return result;
    }

    private class ConsumerTask implements Runnable {

        private SearchContext searchContext;

        public ConsumerTask(SearchContext searchContext) {
            this.searchContext = searchContext;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    File fileToBeSearched = null;
                    synchronized (searchContext) {
                        if (searchContext.isProducersEOF() && searchContext.queue.size() == 0) {
                            return;
                        }
                        fileToBeSearched = searchContext.getQueue().take();
                    }
                    logger.debug(Thread.currentThread() + "  " + "searching for string "+ searchContext.getSearchPattern()+" in "+fileToBeSearched.getName());
                    try {
                        searchContext.getResult().put(fileToBeSearched, searchFile(fileToBeSearched, searchContext.searchPattern));
                    } catch (IOException e) {
                        logger.warn("Failed to search content {} in file {}", searchContext.getSearchPattern(), fileToBeSearched.getAbsolutePath(), e);
                    }
                } catch (InterruptedException e) {
                    logger.warn("InterruptedException while taking element from queue", e);
                    return;
                }
            }
        }
    }
}
