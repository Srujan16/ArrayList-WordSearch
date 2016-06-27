package com.wavemaker.tutorial;


import java.io.IOException;
import java.io.PushbackInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
/**
 * Created by srujant on 14/6/16.
 */
public class PushBackArrayDemo {


    public void demo(){


        byte[] arrByte = new byte[1024];

        // create an array for our message

        byte[] byteArray = new byte[]{'H', 'e', 'l', 'l', 'o',};

        // create object of PushbackInputStream class for specified stream

         InputStream is = new ByteArrayInputStream(byteArray);
         PushbackInputStream pis = new PushbackInputStream(is, 10);

        try {
            // read from the buffer one character at a time
            for (int i = 0; i < byteArray.length; i++) {
                arrByte[i] = (byte) pis.read();
                System.out.print((char) arrByte[i]);
            }

            System.out.println();
            byte[] b = {'W', 'o','r','l','d','a','b','c'};
            pis.unread(b);
            System.out.println();

            for (int i = 0; i < byteArray.length; i++) {

                arrByte[i] = (byte) pis.read();
                System.out.print((char) arrByte[i]);
            }

            System.out.println();

            byte a[]={'q','r','s','t','u','v','z'};
            pis.unread(a);

            for (int i = 0; i < byteArray.length+4; i++) {
                arrByte[i] = (byte) pis.read();
                System.out.print((char) arrByte[i]);
            }



        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }
}

