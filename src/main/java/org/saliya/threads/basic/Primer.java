package org.saliya.threads.basic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Primer {
    public static void main(String[] args) {
        Thread.currentThread().getThreadGroup().list();
        Thread t = new Thread(() -> Thread.currentThread().getThreadGroup().list());
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ExecutorService pool = Executors.newFixedThreadPool(10, Executors.defaultThreadFactory());
    }
}
