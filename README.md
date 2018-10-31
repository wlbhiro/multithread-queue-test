# multithread-queue-test
マルチスレッドでのキューの動作確認

# Output例
```
Queue<HashMap<String, String>> TEST : 
DATA_TOTAL : 1298
RECORD_TIME(ns) : 1691009
RECORD_TIME_FACT(ns)(2194929873/1000000) : 2194
BlockingQueue<HashMap<String, String>> TEST : 
DATA_TOTAL : 1000000
RECORD_TIME(ns) : 13923
RECORD_TIME_FACT(ns)(13923651376/1000000) : 13923
HashMap<String, BlockingQueue<HashMap<String, String>>> TEST : 
DATA_TOTAL : 1000000
RECORD_TIME(ns) : 320
RECORD_TIME_FACT(ns)(320259055/1000000) : 320

Process finished with exit code 0
```
