package com.wavemaker.tutorial;

import java.io.*;

/**
 * Created by srujant on 14/6/16.
 */

public class BufferedInputStreamDemo {

    public void demo() throws IOException {
        BufferedInputStream br = new BufferedInputStream(new FileInputStream("src/main/resources/temp"),3);

        br.mark(1);
        br.read();
        br.read();
        br.read();
        br.reset();
        System.out.println(br.read());

    }
}
