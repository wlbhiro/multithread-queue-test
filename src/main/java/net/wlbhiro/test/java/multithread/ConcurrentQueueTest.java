package net.wlbhiro.test.java.multithread;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ConcurrentQueueTest {
    public static Queue<HashMap<String, String>> queue = new LinkedList<HashMap<String, String>>();
    public static BlockingQueue<HashMap<String, String>> concurrent_queue = new LinkedBlockingQueue<HashMap<String, String>>();

    public static void main(String args[]) {
        for (int i = 0; i < 10; i++) {
            new ThreadTest().start();
        }

        while (true) {
            long queue_size=queue.size();
            long concurrent_queue_size=concurrent_queue.size();
            System.out.println("queue : "+queue_size);
            System.out.println("concurrent_queue : "+concurrent_queue_size);
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class ThreadTest extends Thread {
    public void run() {
        for(int i=0; i<100000; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("v", i+"");
            ConcurrentQueueTest.queue.add(map);
            ConcurrentQueueTest.concurrent_queue.add(map);
        }
    }
}
