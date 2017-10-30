package com.coreconcepts.threads;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by adity on 10/30/2017.
 */

    /*
    *   Two thread should adding values to list
    *   each thread takes 1000 milliseconds to add 1000 values
    *   two thread should 1000 milliseconds to add 1000 values
    *
    * */
public class MultipleLocksSynchronized {

    private Random random = new Random();

    private List<Integer> list_1 = new ArrayList<Integer>();
    private List<Integer> list_2 = new ArrayList<Integer>();

    private Object lock_1 = new Object();
    private Object lock_2 = new Object();


    public void stageOne(){

        synchronized (lock_1){
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            list_1.add(random.nextInt(100));
        }

    }

    public void stageTwo(){

        synchronized (lock_2){

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            list_2.add(random.nextInt(100));
        }

    }

    public void process(){

        for (int i =0; i < 1000; i++){
            stageOne();
            stageTwo();
        }

    }

    public void multiThreading(){

        long startTime = System.currentTimeMillis();

        Thread thread_1 = new Thread(new Runnable() {
            @Override
            public void run() {
                process();
            }
        });

        Thread thread_2 = new Thread(new Runnable() {
            @Override
            public void run() {
                process();
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

        long endTime = System.currentTimeMillis();

        System.out.println("Execution Time is : "+(endTime - startTime));
        System.out.println("List_1 size : "+list_2.size()+" : List_2 size : "+list_2.size());

    }
    public static void main(String args[]){

        new MultipleLocksSynchronized().multiThreading();

    }
}
