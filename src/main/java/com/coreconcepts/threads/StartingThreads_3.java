package com.coreconcepts.threads;

/**
 * Created by adity on 10/29/2017.
 */

    /*
    *   Method - 3
    *
    *       new Thread(new Runnable() { run() })
    *       .start();
    *
    *
    *
    * */
public class StartingThreads_3 {

    public static void main(String args[]){

        Thread thread = new Thread(new Runnable() {
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
        });

        thread.start();
    }
}
