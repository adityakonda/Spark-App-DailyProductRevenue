package com.coreconcepts.threads;

/**
 * Created by adity on 10/29/2017.
 */

    /*
    *   Assume 2 threads are processing same data:  COUNTER Example
    *
    *       thread_1.join()
    *       thread_2.join()
    *
    *   AtomicVariable
    *
    *   https://examples.javacodegeeks.com/core-java/util/concurrent/atomic/atomicinteger/java-atomicinteger-example/
    *
    * */
public class SynchronizedKeyword{

    private int count;

    public synchronized void increament(){
        count ++;
    }

    public void doWork(){

        Thread thread_1 = new Thread(new Runnable() {
            @Override
            public void run() {

                for (int i =0; i<=10000;i++){
                    increament();
                }
            }
        });

        Thread thread_2 = new Thread(new Runnable() {
            @Override
            public void run() {

                for (int i =0; i<=10000;i++){
                    increament();
                }
            }
        });

        thread_1.start();
        thread_2.start();

        try {
            thread_1.join();
            thread_2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Counter value is : "+count);
    }

    public static void main(String args[]){

        new SynchronizedKeyword().doWork();
    }
}
