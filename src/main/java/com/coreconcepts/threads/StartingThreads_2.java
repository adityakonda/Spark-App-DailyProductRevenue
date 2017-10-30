package com.coreconcepts.threads;

/**
 * Created by adity on 10/29/2017.
 */

    /*
    *   Method - 2
    *
    *       Class implements Runnable Interface
    *       run() --> Override
    *       new Thread( cls object ).start()
    *
    * */
public class StartingThreads_2 implements Runnable {

    @Override
    public void run() {

        for (int i = 1; i<=10; i++){

            System.out.println("Hello - "+i);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String args[]){

        Thread thread = new Thread(new StartingThreads_2());
        thread.start();

        Thread thread1 = new Thread(new StartingThreads_2());
        thread1.start();

    }
}
