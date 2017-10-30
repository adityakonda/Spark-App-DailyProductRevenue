package com.coreconcepts.threads;

import java.util.Scanner;

/**
 * Created by adity on 10/29/2017.
 */

    /*
    *   Volatile variable
    *        every read of a volatile variable will be read from the computer's main memory, and not from the CPU cache
    *
    *        http://tutorials.jenkov.com/java-concurrency/volatile.html
    *
    * */

class  Processor extends Thread{

    private volatile boolean running = true;

    @Override
    public void run() {
        int i = 0;
        while (running){
            try {
                Thread.sleep(100);
                System.out.println(i + " - Executing Thread ...!!!");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            i++;
        }
    }

    public void shutDown(){
        running = false;
    }
}
public class BasicThreadSynchronization {

    public static void main(String args[]){

        Processor processor = new Processor();
        processor.start();

        System.out.println("To abort the application please press Enter.");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();

        processor.shutDown();

    }
}


