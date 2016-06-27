package com.wavemaker.tutorial;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;

/**
 * Created by srujant on 8/6/16.
 */




public class MyArrayListTest {



    @Test
    public void testSize() {
        final List<Object> list = new MyArrayList<Object>();

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                list.add(1);
                try {
                    Thread.sleep(2000);
                }catch(InterruptedException ie)
                {
                    ie.printStackTrace();
                }
                Assert.assertEquals(0,list.size());
            }
        });

        Thread t2=new Thread(new Runnable() {
            @Override
            public void run() {
                    list.remove(1);
            }
        });

        t1.start();
        t2.start();

        Assert.assertEquals(0, list.size());
        list.add(1);
        Assert.assertEquals(1, list.size());
        list.add(1);
        Assert.assertEquals(2, list.size());
    }

    @Test
    public void testIsEmpty() {
        List<String> list = new MyArrayList<String>();
        Assert.assertTrue(list.isEmpty());
        list.add("1");
        Assert.assertFalse(list.isEmpty());
    }

    @Test
    public void testContains() {
        List<Integer> list = new MyArrayList<Integer>();
        Assert.assertEquals(false, list.contains((1)));
        list.add(1);
        Assert.assertEquals(true, list.contains(1));
    }


    @Test
    public void testIterator() {
        List<Integer> list = new MyArrayList<Integer>();
        Iterator it = list.iterator();
        Assert.assertEquals(false, it.hasNext());

        list.add(1);
        list.add(2);
        Assert.assertEquals(true, it.hasNext());
        Assert.assertEquals(1, it.next());
        it.remove();

        Assert.assertEquals(false, list.contains(1));
        Assert.assertEquals(true, list.contains(2));
        /*
            IllegalStateException
        */
        it.remove();

        Assert.assertEquals(1, list.size());
        Assert.assertEquals(true, it.hasNext());
        Assert.assertEquals(2, it.next());
        it.remove();

        Assert.assertEquals(false, it.hasNext());
    }


    @Test
    public void toArray(){
        List<Integer> list=new MyArrayList<Integer>();
        Object[] y=new Object[list.size()];
        Assert.assertArrayEquals(y,list.toArray());

        list.add(1);
        list.add(2);

        Integer[] x=new Integer[list.size()];
        x[0]=new Integer(1);
        x[1]=new Integer(2);

        Integer[] z=new Integer[list.size()-1];
        Assert.assertArrayEquals(x,list.toArray( z));

    }



    @Test
    public void testAdd() {
        List<Integer> list = new MyArrayList<Integer>();
        list.add(1);
        Assert.assertEquals(1,list.size());
        Assert.assertEquals(new Integer(1),list.remove(0));
        Assert.assertEquals(0,list.size());
    }



    @Test
    public void testremove() {
        List<Integer> list= new MyArrayList<Integer>();
        Assert.assertEquals(false,list.remove(new Integer(1)));

        list.add(1);
        /*
            remove specified object
        */

        Assert.assertEquals(true,list.remove(new Integer(1)));

        list.add(1);
        /*
            remove element at specified index

         */
        Assert.assertEquals(new Integer(1),list.remove(0));
    }

    @Test
    public void containsAll() {

        List<Integer> list=new MyArrayList<Integer>();
        list.add(1);
        list.add(2);

        List<Integer> list2=new MyArrayList<Integer>();
        list2.add(2);
        Assert.assertEquals(true,list.containsAll(list2));
        list2.add(3);

        Assert.assertEquals(2,list2.size());

        Assert.assertEquals(false,list.containsAll(list2));

    }

    @Test
    public void addAll(){

        List<Integer> list=new MyArrayList<Integer>();
        list.add(1);

        List<Integer> list2=new MyArrayList<Integer>();
        list2.add(2);
        list.addAll(list2);
        Assert.assertEquals(2,list.size());


        list2.add(3);
        list.addAll(list2);
        Assert.assertEquals(4,list.size());

    }

    @Test
    public void clear() {
        List<Integer> list=new MyArrayList<Integer>();
        list.add(1);
        list.clear();
        Assert.assertEquals(0,list.size());

    }

    @Test
    public void get() {
        List<Integer> list=new MyArrayList<Integer>();
        list.add(1);
        list.add(2);
        Assert.assertEquals(null,list.get(3));
        Assert.assertEquals(new Integer(2),list.get(1));

    }

    @Test
    public void set() {
        List<Integer> list=new MyArrayList<Integer>();
        list.add(1);
        list.add(2);

        /*
        return the element previously at the specified position
         */
        Assert.assertEquals(new Integer(2),list.set(1,3));

        /*index out of bound*/
        Assert.assertEquals(null,list.set(3,4));

        /*
        return the element previously at the specified position
        */

        Assert.assertEquals(new Integer(3),list.set(1,2));
    }

    @Test
    public void retainAll() {
        List<Integer> list=new MyArrayList<Integer>();
        list.add(1);
        list.add(2);

        List<Integer> list2=new MyArrayList<Integer>();
        list2.add(2);

        /**
           Asserting Initial sizeof list
         **/

        Assert.assertEquals(2,list.size());

        Assert.assertEquals(true,list.retainAll(list2));
        Assert.assertEquals(false,list.retainAll(list2));

        /**
            Asserting sizeof list after performing retainAll Function
         **/
        Assert.assertEquals(1,list.size());


        List<Integer> list3=new MyArrayList<Integer>();
        list3.add(3);
        Assert.assertEquals(true,list.retainAll(list3));

        /**
            Asserting sizeof list after re performing retainAll method with new list.
         **/
        Assert.assertEquals(0,list.size());
    }

    @Test
    public void removeAll(){
        List<Integer> list=new MyArrayList<Integer>();
        list.add(1);
        list.add(2);
        list.add(1);
        Assert.assertEquals(3,list.size());

        List<Integer> list2=new MyArrayList<Integer>();
        list2.add(1);
        Assert.assertEquals(true,list.removeAll(list2));

        Assert.assertEquals(1,list.size());

    }


    @Test
    public void remove()
    {
        List<Integer> list=new MyArrayList<Integer>();
        Assert.assertEquals(null,list.remove(0));

        list.add(1);
        Assert.assertEquals(new Integer(1),list.remove(0));

        Assert.assertEquals(null,list.remove(0));

    }

    @Test
    public  void indexOf() {
        List<Integer> list=new MyArrayList<Integer>();
        list.add(1);
        list.add(null);
        Assert.assertEquals(0,list.indexOf(new Integer(1)));
        Assert.assertEquals(1,list.indexOf(null));
    }

    @Test
    public void lastIndexOf(){
        List<Integer> list=new MyArrayList<Integer>();
        list.add(1);
        list.add(1);
        Assert.assertEquals(-1,list.lastIndexOf(new Integer(2)));
        Assert.assertEquals(2,list.size());
        Assert.assertEquals(1,list.lastIndexOf(1));

    }



}
