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
        boolean is_debug = Boolean.parseBoolean(System.getProperty("is_debug", "false"));
        int threads = Integer.parseInt(System.getProperty("threads", "20"));
        int max_count = Integer.parseInt(System.getProperty("max_count", "600000"));

        ArrayList<ThreadTest> threadTests = new ArrayList<ThreadTest>();

        System.out.println("Queue<HashMap<String, String>> TEST : ");

        // データの欠損確認
        for (int i = 0; i < threads; i++) {
            ThreadTest threadTest = new QueueThreadTest(max_count);
            threadTest.start();
            threadTests.add(threadTest);
        }
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long previous_queue_size = -1L;
        long queue_value_total = 0L;
        while (queue_value_total != previous_queue_size) {
            previous_queue_size = queue_value_total;
            if(is_debug) {
                System.out.println(" queue_value_total : " + queue_value_total);
            }
            for(HashMap<String, String> data = queue.poll(); data != null; data = queue.poll()) {
                queue_value_total += Long.parseLong(data.get("v"));
            }
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(" DATA_TOTAL : " + queue_value_total);
        System.out.println(" RECORD_TIME(ns) : " + (QueueThreadTest.total_time.get())/queue_value_total);
        System.out.println("  RECORD_TIME_FACT(ns)("+QueueThreadTest.total_time.get()+"/"+(threads * max_count)+") : " + (QueueThreadTest.total_time.get())/(threads * max_count));
        QueueThreadTest.resetTotalTime();

        // スレッドを終了
        for (ThreadTest threadTest : threadTests) {
            threadTest.stop();
        }
        threadTests = new ArrayList<ThreadTest>();

        System.out.println("BlockingQueue<HashMap<String, String>> TEST : ");
        for (int i = 0; i < threads; i++) {
            ThreadTest threadTest = new BlockingQueueThreadTest(max_count);
            threadTest.start();
            threadTests.add(threadTest);
        }
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        previous_queue_size = -1L;
        queue_value_total = 0L;
        while (queue_value_total != previous_queue_size) {
            previous_queue_size = queue_value_total;
            for(HashMap<String, String> data = blocking_queue.poll(); data != null; data = blocking_queue.poll()) {
                queue_value_total += Long.parseLong(data.get("v"));
            }
            if(is_debug) {
                System.out.println("queue_value_total : " + queue_value_total);
            }
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(" DATA_TOTAL : " + queue_value_total);
        System.out.println(" RECORD_TIME(ns) : " + (BlockingQueueThreadTest.total_time.get())/queue_value_total);
        System.out.println("  RECORD_TIME_FACT(ns)("+BlockingQueueThreadTest.total_time.get()+"/"+(threads * max_count)
                + ") : " + (BlockingQueueThreadTest.total_time.get())/(threads * max_count));
        BlockingQueueThreadTest.resetTotalTime();

        // スレッドを終了
        for (ThreadTest threadTest : threadTests) {
            threadTest.stop();
        }
        threadTests = new ArrayList<ThreadTest>();

        System.out.println("HashMap<String, BlockingQueue<HashMap<String, String>>> TEST : ");
        for (int i = 0; i < threads; i++) {
            ThreadTest threadTest = new BlockingQueueHashMapThreadTest(max_count);
            threadTest.start();
            threadTests.add(threadTest);
        }
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        previous_queue_size = -1L;
        queue_value_total = 0L;
        while (queue_value_total != previous_queue_size) {
            previous_queue_size = queue_value_total;
            for(String key : blocking_queue_hashmap.keySet()) {
                if(blocking_queue_hashmap.get(key) != null) {
                    for(HashMap<String, String> data = blocking_queue_hashmap.get(key).poll();
                        data != null;
                        data = blocking_queue_hashmap.get(key).poll()) {
                        queue_value_total += Long.parseLong(data.get("v"));
                    }
                }
            }
            if(is_debug) {
                System.out.println("queue_value_total : " + queue_value_total);
            }
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(" DATA_TOTAL : " + queue_value_total);
        System.out.println(" RECORD_TIME(ns) : " + (BlockingQueueHashMapThreadTest.total_time.get())/queue_value_total);
        System.out.println("  RECORD_TIME_FACT(ns)("+BlockingQueueHashMapThreadTest.total_time.get()+"/"+(threads * max_count)
                + ") : " + (BlockingQueueHashMapThreadTest.total_time.get())/(threads * max_count));
        BlockingQueueHashMapThreadTest.resetTotalTime();

        // スレッドを終了
        for (ThreadTest threadTest : threadTests) {
            threadTest.stop();
        }
        threadTests = new ArrayList<ThreadTest>();
    }
}

class QueueThreadTest extends ThreadTest {
    public QueueThreadTest(int max_count) {
        super(max_count);
    }
    protected void exec(HashMap<String, String> map) {
        ConcurrentQueueTest.queue.add(map);
    }
}

class BlockingQueueThreadTest extends ThreadTest {
    public BlockingQueueThreadTest(int max_count) {
        super(max_count);
    }
    protected void exec(HashMap<String, String> map) {
        ConcurrentQueueTest.blocking_queue.add(map);
    }
}

class BlockingQueueHashMapThreadTest extends ThreadTest {
    BlockingQueue<HashMap<String, String>> blocking_queue_entry = new LinkedBlockingQueue<HashMap<String, String>>();
    public BlockingQueueHashMapThreadTest(int max_count) {
        super(max_count);
        ConcurrentQueueTest.blocking_queue_hashmap.put(super.thread_num_string, this.blocking_queue_entry);
    }
    protected void exec(HashMap<String, String> map) {
        this.blocking_queue_entry.add(map);
    }
}

abstract class ThreadTest extends Thread {
    int max_count = 0;
    static int current_thread_num = 0;
    String thread_num_string = "";
    static AtomicLong total_time = new AtomicLong();
    public ThreadTest() {
        current_thread_num++;
        thread_num_string = current_thread_num +"";
    }
    public ThreadTest(int max_count) {
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
            map.put("v", 1L+"");
            long start_time = System.nanoTime();
            exec(map);
            long end_time = System.nanoTime();
            total_time.addAndGet((end_time - start_time));
        }
    }
    protected abstract void exec(HashMap<String, String> map);
}
