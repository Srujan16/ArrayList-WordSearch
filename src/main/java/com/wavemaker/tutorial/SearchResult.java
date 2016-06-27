package com.wavemaker.tutorial;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by srujant on 15/6/16.
 */
/*
 lineNumber -- implies row number in the file at which, given sequence is found.
 */

public class SearchResult {
    int lineNumber;
    int columnNUmber;

     SearchResult(int lineNumber, int columnNUmber) {
        this.lineNumber = lineNumber;
        this.columnNUmber = columnNUmber;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getColumnNumber() {
        return columnNUmber;
    }


}
