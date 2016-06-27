package com.wavemaker.tutorial;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by srujant on 14/6/16.
 */
public class WordCountTest {

    @Test
    public void test() throws FileNotFoundException {
        WordCount obj=new WordCount();
        obj.getWordCount(new File("/home/srujant/Java The Complete Reference - 7th Edition [DwzRG].txt"));
    }
}
