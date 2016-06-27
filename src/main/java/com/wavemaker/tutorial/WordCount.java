package com.wavemaker.tutorial;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by srujant on 14/6/16.
 */

/**
   Programm to find number of occurence of each word in the specified file.
 */

public class WordCount {
    public void getWordCount(File file) throws FileNotFoundException {

        Map<String,Integer> mapObj= new TreeMap<String, Integer>();
        Scanner sc = new Scanner(new FileReader(file));
        while(sc.hasNext())
        {
            String word=sc.next();
            if(mapObj.containsKey(word)){
                mapObj.put(word,mapObj.get(word)+1);
            }
            else{
                mapObj.put(word,1);
            }
        }
        Set<String> keys = mapObj.keySet();

        for(String key:keys)
        {
            System.out.println(key+"    \t   "+ mapObj.get(key));
        }

    }
}
