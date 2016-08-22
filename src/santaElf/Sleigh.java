package santaElf;

import java.util.concurrent.Semaphore;

public class Sleigh {
    private final Gift [] body;
    private int nextIn=0;
    private int nextOut=0;
    private int num = 0;
    
    private final int full;
    private final Semaphore mutex = new Semaphore(1);
    private final Semaphore numAvail = new Semaphore(0);
    private final Semaphore numFree;

    public Sleigh(int size){
        body = new Gift[size];
        numFree = new Semaphore(size);
        full = size;
    }

    public void insert(Gift item){
        try {
            numFree.acquire();
            mutex.acquire();
        } catch (InterruptedException ex) {
        }
        
        body[nextIn] = item;
        num++;
        
        try {
            Thread.sleep((int) (Math.random() * 10));
        } catch (InterruptedException ex) {
        }
        nextIn++;
        if(nextIn == body.length){
            nextIn = 0;
        }
        numAvail.release();
        mutex.release();
    }
    
    public Gift extract(){
        try {
            numAvail.acquire();
            mutex.acquire();
        } catch (InterruptedException ex) {
        }
        Gift res ;
        res = body[nextOut];
        num--;
        try {
            Thread.sleep((int) (Math.random() * 10));
        } catch (InterruptedException ex) {
        }

            
        nextOut++;
        if (nextOut==body.length)
            nextOut=0;
        mutex.release();
        numFree.release();
        return res;
    }
    
    public boolean isFull(){
        boolean bool = true;
        try {
            mutex.acquire();
            bool = num == full;
        } catch (InterruptedException ex){
        } finally {
            mutex.release();
        }
        return bool;
    }
    
    public boolean isEmpty(){
        boolean bool = true;
        try {
            mutex.acquire();
            bool = num == 0;
        } catch (InterruptedException ex){
        } finally {
            mutex.release();
        }
        return bool;
    }
    
    public int numofToys(){
        int i;
        try {
            mutex.acquire();
            i = num;
        } catch (InterruptedException ex){
        } finally {
            mutex.release();
        }
        return num;
    }
}