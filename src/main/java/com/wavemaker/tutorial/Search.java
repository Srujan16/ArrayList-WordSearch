package com.wavemaker.tutorial;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by srujant on 16/6/16.
 */
public class Search extends Thread {
    Map<File, List<SearchResult>> result;
    List<SearchResult> resultList;
    File file;
    String searchPattern;

    Search(Map<File, List<SearchResult>> result, File file, String pattern) {
        this.result = result;
        this.file = file;
        this.searchPattern = pattern;
    }

    public void run() {
        try {
            resultList = searchFile(file, searchPattern);
        } catch (IOException e) {
            e.printStackTrace();
        }
        result.put(file, resultList);
    }

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

}
