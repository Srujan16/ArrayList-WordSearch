package com.wavemaker.tutorial;

import java.io.*;
import java.util.List;

/**
 * Created by srujant on 16/6/16.
 */
public class SearchFile extends Thread {

    List<SearchResult> resultList;
    File file;
    String searchPattern;

    SearchFile(List<SearchResult> resultList, File file, String searchPattern) {
        this.resultList = resultList;
        this.file = file;
        this.searchPattern = searchPattern;
    }


    public void run() {
        try {
            search(resultList, file, searchPattern);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void search(List<SearchResult> resultList, File file, String searchPattern) throws IOException {
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
    }
}
