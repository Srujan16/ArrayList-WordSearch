package com.wavemaker.tutorial;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by srujant on 16/6/16.
 */
public class SearchInnerFile extends Thread {

    List<SearchResult> resultList;
    Map<File, List<SearchResult>> result;
    File file;
    String searchPattern;

    SearchInnerFile(Map<File, List<SearchResult>> result, List<SearchResult> resultList, File file, String pattern) {
        this.resultList = resultList;
        this.result = result;
        this.file = file;
        this.searchPattern = pattern;
    }

    public void run() {

        try {

            Thread t = new SearchFile(resultList, file, searchPattern);
            t.start();
            t.join();
            result.put(file, resultList);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
