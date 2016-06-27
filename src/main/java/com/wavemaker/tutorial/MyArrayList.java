package com.wavemaker.tutorial;

/**
  ArrayList Custom Implimentaion.
 */

import java.lang.System;
import java.util.*;
import java.lang.*;




public class MyArrayList<E> implements List<E> {

    private int size = 0;
    private int capacity;
    private Object[] backingArray;
    private int current_index = 0;

    MyArrayList() {
        this(10);
    }

    MyArrayList(int capacity) {
        this.capacity = capacity;
        backingArray = new Object[this.capacity];
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return (size == 0);
    }

    public boolean contains(Object o) {

        if (indexOf(o) != -1) {
            return true;
        }
        return false;
    }


    public Iterator<E> iterator() {

        class TempIterator implements Iterator {

            private int indexPosition = 0;
            private boolean flag = false;

            public boolean hasNext() {
                if ( size==0 || indexPosition==size) {
                    return false;
                } else {
                    return true;
                }
            }

            public Object next() {
                flag = true;
                try {
                    if (indexPosition >= size)
                        throw new NoSuchElementException();
                    else {
                        indexPosition+=1;
                        return backingArray[indexPosition-1];
                    }
                } catch (NoSuchElementException i) {
                    System.out.println("NoSuchElementException");
                }
                return null;
            }

            public void remove() {
                try {
                    if (flag) {
                        flag = false;
                        for (int i = indexPosition - 1; i < size - 1; i++) {
                            backingArray[i] = backingArray[i + 1];
                        }
                        size -= 1;
                        indexPosition-=1;
                    } else {
                        throw new IllegalStateException();
                    }
                } catch (IllegalStateException i) {
                    System.out.println("IllegalStateException");
                }

            }

        }
        return new TempIterator();
    }

    @Override
    public Object[] toArray() {

        Object[] tempBackingArray = new Object[size];
        System.arraycopy(backingArray, 0, tempBackingArray, 0, size);
        return tempBackingArray;
    }

    @Override
    public <E> E[] toArray(E[] a) {

        if(a.length >= size)
        {
            System.arraycopy(backingArray, 0, a, 0, size);
            return a;
        } else {
            E[] temp =  (E[]) Arrays.copyOf(backingArray, size, a.getClass());
            return temp;
        }
    }

    //method to append object to backingArray
    @Override
    public boolean add(E x) {
        updateCapacity();
        backingArray[size] = x;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);
        if (index != -1) {
            remove(index);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        /** is this valid??
         Object[] x =  c.toArray(new Object[c.size()]);
         **/
        Iterator it = c.iterator();
        while (it.hasNext()) {
            if (!contains(it.next())) {
                return false;
            }
        }
        return true;
    }


    public boolean addAll(Collection<? extends E> c) {
        Iterator it = c.iterator();
        while (it.hasNext()) {
            updateCapacity();
            backingArray[size++] = it.next();
        }
        return true;
    }

    public boolean addAll(int index, Collection<? extends E> c) {
        int collectionSize = c.size();
        int newSize;
        int lastElementIndex;
        int shiftLength = size - index;

        Iterator it = c.iterator();

        while (size + collectionSize >= capacity) {
            updateCapacity();
        }

        newSize = size + collectionSize - 1;
        lastElementIndex = size - 1;

        while (lastElementIndex <= index) {
            backingArray[newSize--] = backingArray[lastElementIndex--];

        }

        while (it.hasNext()) {
            backingArray[index++] = it.next();
        }
        size += collectionSize;
        return true;
    }

    public void clear() {
        size = 0;
    }

    public E get(int index) {
        try {
            if (index >= size)
                throw new IndexOutOfBoundsException();
            else {
                return (E) backingArray[index];
            }
        } catch (IndexOutOfBoundsException ie) {
            System.out.println("IndexOutOfBoundsException");
        }
        return null;
    }

    public E set(int index, E element) {

        Object previousObject = null;
        try {
            if (index >= size) {
                throw new IndexOutOfBoundsException();
            } else {
                previousObject = backingArray[index];
                backingArray[index] = element;
            }
        } catch (IndexOutOfBoundsException ie) {
            System.out.println("IndexOutOfBoundsException");
        }
        return (E) previousObject;
    }

    public boolean retainAll(Collection<?> c) {


         boolean[] flagArray=new boolean[size];
         boolean changed=false;
         boolean flag=false;
         int collectionSize=c.size();
         int i=0;
         int currentIndex=0;


         Object[] collectionArray=new Object[collectionSize];

         Iterator it = c.iterator();

         while (i<collectionSize) {
             collectionArray[i++] = it.next();
         }

        for(int backingArrayIndex=0;backingArrayIndex<size;backingArrayIndex++) {
            for(int collectionIndex=0;collectionIndex<collectionSize;collectionIndex++) {
                if(backingArray[backingArrayIndex].equals(collectionArray[collectionIndex])){
                     flag=true;
                     break;
                }
            }

            if(flag) {
                flagArray[backingArrayIndex]=flag;
                flag=false;
            }
            else {
                changed=true;
                flagArray[backingArrayIndex]=flag;
            }
         }

         for(int j=0;j<size;j++) {
                if(flagArray[j]){
                    backingArray[currentIndex++]=backingArray[i];
                }
         }

        size=currentIndex;
        return changed;
    }

    public boolean removeAll(Collection<?> c) {

        boolean changed=false;
        boolean flag=false;
        int collectionSize=c.size();
        int[] flagArray=new int[collectionSize];
        int i=0;
        int currentIndex=0;
        int count=0;
        int removeCount=0;


        Object[] collectionArray=new Object[collectionSize];
        Iterator it = c.iterator();

        while (i<collectionSize) {
            collectionArray[i++] = it.next();
        }

        for(int collecionIndex=0;collecionIndex<collectionSize;collecionIndex++){
            for(int backingArrayIndex=0;backingArrayIndex<size;backingArrayIndex++){
                if(backingArray[backingArrayIndex].equals(collectionArray[collecionIndex]))
                {
                    count++;
                }
            }
            flagArray[collecionIndex]=count;
            count=0;
        }

        for(int j=0;j<collectionSize;j++) {
            count=flagArray[j];
            while(count>0) {
                removeCount++;
                remove(collectionArray[j]);
                count--;
                changed=true;
            }
        }

        return changed;
    }


    public void add(int index, E x) {

        try {
            if (index > size || index < 0) {
                throw new IndexOutOfBoundsException();
            } else {
                updateCapacity();
                if (index == size) {
                    backingArray[index] = x;
                    size += 1;
                } else {
                    for (int i = size; i > index; i--) {
                        backingArray[i] = backingArray[i - 1];

                    }
                    backingArray[index] = x;
                    size++;
                }
            }

        } catch (IndexOutOfBoundsException i) {
            System.out.println("Index out Of Bound");

        }
    }


    public E remove(int index) {
        try {
            if (index >= size || index<0 && size!=0) {
                throw new IndexOutOfBoundsException();
            } else {
                if (index == size - 1) {
                    size -= 1;
                    return (E) backingArray[index];
                } else {
                    Object x = backingArray[index];
                    for (int i = index; i < size - 1; i++) {
                        backingArray[i] = backingArray[i + 1];
                    }
                    size -= 1;

                    return (E) x;
                }
            }
        } catch (IndexOutOfBoundsException ie) {
            System.out.println("IndexOutOfBoudn Exception");
        }
        return null;
    }


    public int indexOf(Object o) {
        for (int i = 0; i < size; i++) {
            if (o == null) {
                if (backingArray[i] == null) {
                    return i;
                }
            } else if (backingArray[i] != null) {
                if (o.equals(backingArray[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    /*
            Better Solurtion would be, start scanning element from the end.
     */
    public int lastIndexOf(Object x) {
        int lastindex = -1;
        for (int i = 0; i < size; i++) {
            if (x.equals(backingArray[i])) {
                lastindex = i;
            }
        }
        return lastindex;
    }

    public ListIterator<E> listIterator() {

        class TempListIterator<E> implements ListIterator<E> {

            int cursorPosition = 0;
            boolean nextOrPreviousMethodFlag;
            boolean removeFlag;
            boolean addMethodFlag;
            int forwardPosition = 0;
            int backwardPostion = 0;

            public boolean hasNext() {
                if (cursorPosition < size - 1) {
                    return true;
                }
                return false;
            }

            public boolean hasPrevious() {
                if (cursorPosition == 0)
                    return false;
                return true;
            }

            public E next() {
                try {
                    if (cursorPosition == size ) {
                        throw new NoSuchElementException();
                    }
                    nextOrPreviousMethodFlag=true;
                    addMethodFlag=false;
                    removeFlag=false;
                    cursorPosition += 1;
                    return (E) backingArray[cursorPosition - 1];
                } catch (NoSuchElementException ie) {
                    System.out.println("NoSuchElementException");
                }
                return null;
            }

            public E previous() {
                try {
                    if (cursorPosition <= 0) {
                        throw new NoSuchElementException();
                    } else {
                        nextOrPreviousMethodFlag=true;
                        addMethodFlag=false;
                        removeFlag=false;
                        cursorPosition -= 1;
                        return (E) backingArray[cursorPosition + 1];
                    }
                } catch (NoSuchElementException ex) {
                    System.out.println("NoSuchElementException");
                }
                return null;
            }

            public int nextIndex() {
                return cursorPosition;
            }

            public int previousIndex() {
                return cursorPosition;
            }

            @Override
            public void remove() {
                try{
                        if(nextOrPreviousMethodFlag) {
                            nextOrPreviousMethodFlag=false;
                            removeFlag=true;
                            for(int i=cursorPosition;i<size-1;i++){
                                backingArray[i]=backingArray[i+1];
                            }
                              size-=1;
                        }else{
                            throw new IllegalStateException();
                        }
                    }
                catch (IllegalStateException ie){
                    System.out.println("IllegalStateExcception");
                }

            }

            @Override
            public void set(E e) {
                try{

                    if(nextOrPreviousMethodFlag){
                        backingArray[cursorPosition]=e;
                    }
                    else{
                        throw new IllegalStateException();
                    }
                }
                catch (IllegalStateException ie) {
                    System.out.println("IllegalStateException");

                }

            }

            @Override
            public void add(E e) {

                        if(size==0) {
                             backingArray[size]=e;
                            size++;
                        }
                        else  {

                            size++;
                            updateCapacity();

                            for(int i=size-1;i>cursorPosition;i--)
                            {
                                backingArray[i]=backingArray[i-1];
                            }
                            backingArray[cursorPosition]=e;
                            nextOrPreviousMethodFlag = false;
                            addMethodFlag = true;
                        }

            }

        }
        return new TempListIterator<E>();
    }

    public ListIterator listIterator(int index) {
        return null;
    }

    public List subList(int fromIndex, int toIndex) {
        return null;
    }

    /**
     * increases capacity of backingArray to support addition of new elements into the list
     */
    private void updateCapacity() {
        if (size >= capacity) {
            capacity *= 2;
            Object[] tempBackingArray = new Object[capacity];
            System.arraycopy(backingArray, 0, tempBackingArray, 0, size);
            backingArray = tempBackingArray;
        }
    }


}




