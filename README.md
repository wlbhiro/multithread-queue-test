# multithread-queue-test
マルチスレッドでのキューの動作確認

# Output例
```
Queue<HashMap<String, String>> TEST : 
 DATA_TOTAL : 1209
 RECORD_TIME(ns) : 8040867
  RECORD_TIME_FACT(ns)(9721408377/12000000) : 810
BlockingQueue<HashMap<String, String>> TEST : 
 DATA_TOTAL : 12000000
 RECORD_TIME(ns) : 12802
  RECORD_TIME_FACT(ns)(153630489743/12000000) : 12802
HashMap<String, BlockingQueue<HashMap<String, String>>> TEST : 
 DATA_TOTAL : 12000000
 RECORD_TIME(ns) : 9266
  RECORD_TIME_FACT(ns)(111192190353/12000000) : 9266

Process finished with exit code 0
```
