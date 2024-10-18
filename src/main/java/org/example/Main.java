package org.example;

import java.util.Collections;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    private static final Map<Integer, Integer> sizeToFreq = new TreeMap<>();

    public static void main(String[] args) {

        int threadsNumber = 1000;
        ExecutorService threadPool = Executors.newFixedThreadPool(threadsNumber);
        for (int i = 0; i < threadsNumber; i++) {
            threadPool.submit(Main::process);
        }
        threadPool.shutdown();
        printMap();
    }

    private static void process() {
        String route = generateRoute("RLRFR", 100);
        int rOccurrencesCount = Math.toIntExact(route.chars().filter(currentChar -> currentChar == 'R').count());
        incrementFreq(rOccurrencesCount);
    }

    private static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    private static void incrementFreq(int rOccurrencesCount) {
        synchronized (sizeToFreq) {
            sizeToFreq.put(rOccurrencesCount, sizeToFreq.getOrDefault(rOccurrencesCount, 0) + 1);
        }
    }

    private static void printMap() {
        int rMaxOccurrenceKey = Collections.max(sizeToFreq.entrySet(), Map.Entry.comparingByValue()).getKey();
        System.out.println("Самое частое количество повторений " + rMaxOccurrenceKey + " встретилось " +  sizeToFreq.get(rMaxOccurrenceKey) + " раз");

        sizeToFreq.keySet().forEach(key -> System.out.println(key + " встретилось " + sizeToFreq.get(key) + " раз"));
    }
}