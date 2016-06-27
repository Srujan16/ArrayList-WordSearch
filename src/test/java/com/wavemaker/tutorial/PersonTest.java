package com.wavemaker.tutorial;

import java.util.*;

import org.junit.Assert;
import org.junit.Test;



/**
 * Created by srujant on 13/6/16.
 */
public class PersonTest {

    List<Person> list=new ArrayList<Person>();
    Set<Person> set=new HashSet<Person>();
//    Set<Person> treeSet=new TreeSet<Person>();

    @Test

    public void test() {

        Person obj1 = new Person("lee", "bruce", 22);
        Person obj2 = new Person("lee", "bruce", 22);


        /*System.out.println(list.add(obj1));
        System.out.println(list.add(obj2));

        System.out.println(set.add(obj1));
        System.out.println(set.add(obj1));

        System.out.println(set.add(obj2));*/

        Assert.assertEquals(obj1.hashCode(), obj2.hashCode());





    }




}
