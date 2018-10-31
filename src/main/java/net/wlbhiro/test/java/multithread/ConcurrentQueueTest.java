package net.wlbhiro.test.java.multithread;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

public class ConcurrentQueueTest {
    public static Queue<HashMap<String, String>> queue = new LinkedList<HashMap<String, String>>();
    public static BlockingQueue<HashMap<String, String>>  blocking_queue = new LinkedBlockingQueue<HashMap<String, String>>();
    public static HashMap<String, BlockingQueue<HashMap<String, String>>> blocking_queue_hashmap = new HashMap<String, BlockingQueue<HashMap<String, String>>>();

    public static void main(String args[]) {
        int threads = Integer.parseInt(System.getProperty("threads", "20"));
        int max_count = Integer.parseInt(System.getProperty("max_count", "50000"));

        System.out.println("QUEUE TEST : ");
        for (int i = 0; i < threads; i++) {
            new QueueThreadTest(max_count).start();
        }
        long previous_queue_size = -1L;
        while (queue.size() != previous_queue_size) {
            previous_queue_size = queue.size();
            System.out.println("queue.size : "+queue.size());
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("RECORD_TIME(ns) : " + (QueueThreadTest.total_time.get())/previous_queue_size);
        QueueThreadTest.resetTotalTime();

        System.out.println("BLOCKING QUEUE TEST : ");

        for (int i = 0; i < threads; i++) {
            new BlockingQueueThreadTest(max_count).start();
        }
        long queue_size = 0;
        previous_queue_size = -1L;
        while (blocking_queue.size() != previous_queue_size) {
            previous_queue_size = blocking_queue.size();
            System.out.println("blocking_queue.size : "+blocking_queue.size());
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("RECORD_TIME(ns) : " + (BlockingQueueThreadTest.total_time.get())/previous_queue_size);
        BlockingQueueThreadTest.resetTotalTime();

        System.out.println("BLOCKING QUEUE HASHMAP TEST : ");
        for (int i = 0; i < threads; i++) {
            new BlockingQueueHashMapThreadTest(max_count).start();
        }
        queue_size = -1;
        previous_queue_size = 0L;
        while (queue_size != previous_queue_size) {
            previous_queue_size = queue_size;
            queue_size = 0;
            for(String key : blocking_queue_hashmap.keySet()) {
                if(blocking_queue_hashmap.get(key) != null) {
                    queue_size += blocking_queue_hashmap.get(key).size();
                }
            }
            System.out.println("blocking_queue_hashmap.get.size : "+queue_size);
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("RECORD_TIME(ns) : " + (BlockingQueueHashMapThreadTest.total_time.get())/previous_queue_size);
        BlockingQueueHashMapThreadTest.resetTotalTime();
    }
}

class QueueThreadTest extends TreadTest {
    public QueueThreadTest(int max_count) {
        super(max_count);
    }
    protected void exec(HashMap<String, String> map) {
        ConcurrentQueueTest.queue.add(map);
    }
}

class BlockingQueueThreadTest extends TreadTest {
    public BlockingQueueThreadTest(int max_count) {
        super(max_count);
    }
    protected void exec(HashMap<String, String> map) {
        ConcurrentQueueTest.blocking_queue.add(map);
    }
}

class BlockingQueueHashMapThreadTest extends TreadTest {
    BlockingQueue<HashMap<String, String>> blocking_queue_entry = new LinkedBlockingQueue<HashMap<String, String>>();
    public BlockingQueueHashMapThreadTest(int max_count) {
        super(max_count);
        ConcurrentQueueTest.blocking_queue_hashmap.put(super.thread_num_string, this.blocking_queue_entry);
    }
    protected void exec(HashMap<String, String> map) {
        this.blocking_queue_entry.add(map);
    }
}

abstract class TreadTest extends Thread {
    int max_count = 0;
    static int current_thread_num = 0;
    String thread_num_string = "";
    static AtomicLong total_time = new AtomicLong();
    public TreadTest() {
        current_thread_num++;
        thread_num_string = current_thread_num +"";
    }
    public TreadTest(int max_count) {
        this();
        this.max_count = max_count;
    }
    public static void resetTotalTime() {
        total_time = new AtomicLong();
    }
    @Override
    public void run() {
        for(int i=0; i<max_count; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("v", i+"");
            long start_time = System.nanoTime();
            exec(map);
            long end_time = System.nanoTime();
            total_time.addAndGet((end_time - start_time));
        }
    }
    protected abstract void exec(HashMap<String, String> map);
}
