package com.coreconcepts.threads;

/**
 * Created by adity on 10/29/2017.
 */

    /*
    *    Method - 1
    *
    *       Class extends Thread
    *       run()
    *       obj.start();
    *
    * */
public class StartingThreads_1 extends Thread{

    public void run(){

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
        StartingThreads_1 obj = new StartingThreads_1();
        obj.start();
    }


}
