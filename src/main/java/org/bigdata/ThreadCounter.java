package org.bigdata;

public class ThreadCounter {
    public static void main(String[] args) {
        Runtime runtime = Runtime.getRuntime();
        int numberOfThreads = runtime.availableProcessors();
        System.out.println("number of available threads: " + numberOfThreads);
    }
}
