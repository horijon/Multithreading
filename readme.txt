# Multitasking
-> Time sharing (time slots)
-> Tasks sharing a common resource (like 1 CPU).
# Multiprogramming
-> Context switching
-> Logical extension of multitasking
eg; A computer with 1 CPU running more than one program at a time (like running Excel and Firefox simultaneously)
# Multiprocessing
-> multiple CPUs/processors
-> Parallel processing.
-> Multiple processor does multiple tasks at the same instant of time.
# Multithreading
-> a thread is a single unit of a process.
-> All the threads of a process have their own(different) stacks inside the memory of the same process.
-> It is used to achieve multitasking.
-> It is an extension of multitasking.
Note: Note: Modern OS Schedulers puts the different threads even of the same process on different available cores if any.

# Concurrency vs Parallelism
Concurrency:
-> Concurrency means multiple tasks which start, run, and complete in overlapping time periods , in no specific order.
-> Concurrency is the mechanism of executing simultaneously by time sharing/context switching but not at the same time.
Parallelism:
-> Parallelism is when multiple tasks OR several part of a unique task literally run at the same time,
     e.g. on a multi-core processor.
-> Parallelism is the mechanism of executing simultaneously at the same time.
Note: Anyway, we always need to be careful while parallelism, we must maintain consistency on dependent executions
 and that can be achieved by applying concurrency.

1) For multitasking we went for multithreading.
    -> Thread class gave that functionality but lacked multiple inheritance feature so, Runnable came into picture.
    -> But still it had concurrency problems.
2) Then for achieving concurrency in addition to multithreading, we went for separate member instance of each member variables
    per job/task.
    -> It brought memory problems as each task/job have separate instance of each member variables of the Thread class.
3) Then to achieve multithreading, concurrency and also avoid memory problems, we went for another approach.
    -> Having separate member instance per thread but not per task/job/Runnable. ThreadLocal provided that functionality.
    -> This relatively increased performance and avoided memory problems.

Note:
1) Multiple cores CPU may work threads on multiple cores, and operations on the same object happens
    on that particular core's local cache per core.
    volatile keyword just pushes the object from the virtual/local cache to the shared memory in either single
    or multi-core processors/CPU. This resolves only the visibility issue. The key is understanding that the
    volatile variables on the CPU cache or registers will be written back to main memory with an IMMEDIATE write policy,
     so as to make any subsequent reads of the same variable reads the latest updated value
     but volatile only increases the chance of having synced threads - but it's not guaranteed.
2) For synchronization, but to resolve the problem of deadlock, we go with the Reentrant lock
    instead of synchronized keyword, this also solves the problem of synchronization while using synchronized keyword
    that was only able to get method level lock but it now gets lock to a series of methods execution.
    We can also tryLock and if available execute normal operation or go for alternative operation.
4) Multiple tasks with same runnable/callable instance creates member variables of that runnable/callable class 
	per task,
    so we need to reduce this memory issue; also static variables change is visible to per task of that same runnable/callable
    and the change by one thread is seen by the other thread, this is also an issue.
    So, we go with ThreadLocal to reduce memory issue and it is usually for static variables that makes a thread
    not see changes by another thread i.e. it creates the object instances per thread but not per task.
    The child thread cannot access parents thread variable values as ThreadLocal class is used,
    so InheritableThreadLocal came into picture. Internal implementation of the threadLocal -
    each threadLocal is kept in a map, so that we can set/get by each threadLocal instance.
5) Threadlocal is to do operations on separate tasks and have separate variables per thread, but if we need to 	
	share a variable among multiple threads and also achieve concurrency, eg; incrementing an integer value inside a loop by multiple threads and also achieving consistency, in such case, we need to use Atomic wrapper classes like AtomicInteger, AtomicBoolean.
6) To reduce the expensive operations of creating thread each time for each task/job i.e. Runnable/Callable, etc,
    we used threadpool. But while doing so, it brought concurrency problem when using ThreadLocal class only,
    so we need to call remove method on that threadLocal instance inside that task(runnable/callable) class to remove
    ThreadLocal value of that thread to its initial value, after every task completes.
eg; of Threadpool -> ExecutorService service = Executors.newFixedThreadPool(3);
						service.submit(task); // task/job/runnble/callable
7) If multiple threads need to work on the same object/collection, and the change needs to be visible by the other 
	threads in addition to relatively increased performance then we need to go for Concurrent collections.
8) Runnable is used when the return type of the thread execution is void. We can use Callable that returns an object
    after execution of the call method. We can use Future(like a promise in javascript) to hold the callable return object.
    We can get the returned value by using future.get().
	eg; Future future = service.submit(task);
		future.get();

	Note: ThreadPool can be shutdown by using
		service.shutdown();
9) future.get() blocks the execution of the thread further that is the thread executing this line future.get() .
    If we have many future.get() calls, every other future or future.get() calls are delayed even if the other calls are independent to the prior calls, this arises performance issue, so we use CompletableFuture.
	eg; CompletableFuture.runAsync(task, executorService) in a loop where runnableTasks are iterated making each task runAsync.

Conclusion:
1) Do not use volatile or synchronized keywords. Go with Reentrant lock
    (mainly came into the picture to replace synchronized keyword and avoid deadlock situations) in case when
    we want to lock a series of method executions so as to achieve consistency and also in case
    when any alternative operation can be done when no lock is achieved
    and also it can be used when we want to lock and unlock programmatically.
2) Use ThreadPool for preventing "expensive thread creation and destroying each time" issue.
3) Use ThreadLocal to prevent "one thread to have access on an object changed by another thread"
i.e. create member instances of a task per thread but not per task. In addition to ThreadPool,
    use it and also call remove method on that threadLocal variable on the finally block inside the task's run method.
    ThreadLocal can be serialized and passed over the network as well that will maintain threadLocal behavior there as well.
4) Use concurrent collections when the change on some collection object needs to be visible to some other threads.
    eg; while iterating a collection by a thread, if another thread is updating, then the thread that is iterating
    needs to get the updated value if the iteration has not passed iterating that block/segment/index
    Concurrent collections:
        1) ConcurrentHashMap
        2) CopyOnWriteArrayList -> can read/modify but not remove
        3) CopyOnWriteArraySet -> can read/modify but not remove
        4) ConcurrentSkipListMap
        Use ConcurrentHashMap where a lot of concurrent addition happens followed by concurrent reads later on.
        It is not atomic, so while iterating, reading and writing is goind at the same time, then we better take help of
        atomic wrapper classes
5) Use Runnable if no return type expected.
	Use Callable and future.get() if return type expected.
	Use CompletableFuture if need to run independent tasks simultaneously and use completableFuture.ofAll(futures).join()
	    to block the following/next "lines of codes" so as to make next LOC run only when all tasks are completed.
	We can use CompletableFuture even when we need to run dependent as well as some independent tasks
	sequentially and simultaneously in a hybrid manner to improve performance.

Note 2: Study other multithreading concepts from the text books "multithreading 1", "multithreading 2" and "multithreading 3".

Note 3: Study more on -> concurrent collections, count down latches, spring boot way of multithreading, multithreading use case scenarios
    in real life, mechanisms/technologies that use multithreading like scheduling, tomcat web server, etc

# List (preserves insertion order, allows duplicated):-
-> Use ArrayList(Growable or Resizable data structure) if retrieval operations are frequent.
    (allows null values). If cannot decide between linkedList and arraylist we better go with arraylist.
-> Use LinkedList(Doubly linked list data structure) only if addition/removal are frequent operations,
    otherwise if retrieval operations are more, we better go with ArrayList.
-> Avoid Vector and Stack as far as possible. Use listIterator to iterate in either direction so as to achieve both FIFO
    as well as LIFO
-> Use CopyOnWriteArrayList for thread-safety and if better performance is required, but as it updates
    on a separate copy/clone of the list, once the listIterator object on any list is get by using
    list.listIterator(), we cannot access the modified list but only access the original list.
    It just provides the facility to simultaneouly and concurrently update. The clones are synced after updation,
    and only another call to list.listIterator() can now access the updated list.
    We cannot remove an element from the list, if this or some other threads are iterating over it, throws exception.

# Set (duplicates not allowed):-
-> Use HashSet(Hashtable data structure) when insertion order doesn't matter.
-> Use LinkedHashSet("Hashtable + Linked List" data structure) when insertion order needs to be preserved.
-> Use TreeSet (balanced tree data structure) if sorting required, sorted either by Comparable(natural default sorting) or
    by Comparator(customized sorting).
-> Also, Collections.synchronizedSet(set) is there to convert a set into thread-safe, but better to avoid it, as
       better concurrent option is available.
-> Use CopyOnWriteArraySet when similar cases as that of CopyOnWriteArrayList arises and duplicates not allowed.
-> Use EnumSet that will contain enum type of same type (faster than HashSet).
    Order is natural i.e. the order in which the enum constants are declared inside enum type.
    values are of type enum of same type, null values are not allowed, not synchronized, doesn't throw
    ConcurrentModificationException(including concurrentMap) but other set throws
    while modifying and iterating at the same time.


# Queue :-
-> Use queue when we need to store data prior to processing.
-> Use PriorityQueue if we want ordered queue
-> Use Deque when we want insertion and removal of elements at both head and tail of the queue.
-> Use ArrayDeque for using dequeue and resizable array.
Note:  BlockingQueue provides insert and remove operations that block or keep waiting until they are performed.
    The blocking queues are usually used in Producer-Consumer frameworks.
-> Use PriorityBlockingQueue to achieve BlockingQueue(blocks the queue until completion of an operation on that queue)
    and achieve Comparator(sorting).
    Note: There are many queue interfaces and implementations, go through this link
    https://blog.e-zest.com/queues-and-blocking-queues/

# Map:-
Note: Hashtable(Hashtable data structure, doesn't allow null keys and values) is synchronized;
    but HashMap(allow null key and values) is not
-> Use HashMap(
    "items in hash bucket > TREEIFY_THRESHOLD i.e. 8" then content on the bucket
    switches from using a linked list of Entry objects to a balanced tree in java 8; improves from O(n) to O(logn)
    ) in non-threaded areas for better performance.
    Also, Collections.synchronizedMap(map) uses decorator design pattern to convert a map into thread-safe,
    but better to avoid it, as better concurrent Collections are available.
-> Use LinkedHashMap(subclass of HashMap) when insertion order is needed to be preserved.
-> Use TreeMap(Red Black Tree data structure) when default/customized sorting is needed.
-> Use WeakHashMap to destroy/(garbage collect) its element when any element's key points to null, when the System.gc() or
    daemon GC thread runs, then the element gets garbage collected,
    use in caches/lookup storage; use when cache entries is determined by external references to the key
    not the value. Keys inserted gets wrapped in java.lang.ref.WeakReference
-> Use IdentityHashMap in case of deep copying or when our key is "Class" object or interned strings - memory
    footprint comparitively smaller than a hashmap as there are no Entry/Node created.
    System.identityHashCode(key) is invoked rather than key.hashCode().
    It checks by == rather than by equals method
-> Use ConcurrentHashMap if thread-safe/concurrency required..
    Avoid using ConcurrentSkipListMap and only use when order needed to be preserved (in addition to thread-safe/concurrency)
    as it is slower than ConcurrentHashMap.
-> Use Properties when we need to load "key,value pairs of Strings" from a properties file to Properties object
    or save "key,value pairs of Strings" to a properties file.
-> Use EnumMap that will only contain enum type of same type (faster than HashMap).
    Keys are of type enum of same type, null keys are not allowed, not synchronized, doesn't throw
    Order is natural order of their keys( i.e. the order on which enum constant are declared inside enum type )
    ConcurrentModificationException(including concurrentMap) but other maps throws
    while modifying and iterating at the same time.
    Faster than HashMap


Cursors in Java
1) Enumeration :-
-> To iterate and read elements in objects of legacy classes (Stack, Vector, Hashtable); only in forward direction.
2) Iterator (Universal iterator for any collection classes) :-
-> To iterate on any collection classes + read/remove while iterating
->forEach or for in loop internally uses Iterator, so we can implement Iterator on custom classes
    eg; Employee class; loop Employee objects just by using for in or for each loop.
        https://www.journaldev.com/13460/java-iterator
    but why bother, as it can be done rather, just by iterating the list.
-> for in loop vs iterator usage (when to use which):-
    If we have to modify collection, we can use Iterator because can't modify in the for in loop.
    While using nested for loops it is better to use for-each loop, consider the below code for better understanding.
        https://www.geeksforgeeks.org/java-implementing-iterator-and-iterable-interface/
    "Develop Custom Class Iterator" (to use for in loop in case of custom classes, i.e. for custom iterator)
        https://www.journaldev.com/13460/java-iterator
3) ListIterator (only for list implemented classes) :-
-> iterator + bi-directional iteration + read/remove/modify/add while iterating + many other useful methods
4) Spliterator :-
-> It is an Iterator for both Collection and Stream API, except Map implemented classes.
-> supports both Sequential and Parallel processing of data
-> We can use it for both Collection API and Stream API classes (provides additional methods).

Sorting:-
1) Comparable
-> default natural sorting
-> implemented by the class that will be sorted inside a list later.
-> needs to override compareTo(logic for sorting) method inside the class that implements it.

2) Comparator
-> customized sorting
-> implemented by the LOC that uses the list and is not satisfied by the default natural sorting; but requires its
    own sorting strategy/logic.
-> needs to override compare(logic for sorting) method; overriding equals method is optional
    that can be used inside compare method. implementation inside it looks like eg; object1.name.compareTo(object2.name)
    we usually call compareTo method inside compare method.

Note:- TreeMap is available for sorting a map and TreeSet for sorting a set but in case of list, we use Collections.sort(list).

3) Collections.sort(list);
-> it sorts any list in ascending natural sorting order.
-> to reverse, Collections.sort(list, Collections.reverseOrder()); can be used
Note:  Collections.binarySearch(list, "Kshitij"); returns the position of an object(i.e. String "Kshitij") in a sorted list.
    The method throws ClassCastException if elements of list are not comparable using the specified comparator,
       or the search key is not omparable with the elements.

4) Arrays.sort()
-> it sorts arrays which can be of primitive data type also along with the collection type.
Note:  Arrays.binarySearch(array, "Kshitij"); returns the position of an object(i.e. String "Kshitij") in a sorted array.
Note: For sort and binarySearch in the list, Collections methods are preferable over Arrays performance wise.
Note: -> Arrays.asList(array) will only behave as a list but internal data structure is an array, so any modification to the
underlying data structure using list methods is not supported, will only throw exception.
Note: collectionObject.toArray() will convert the collectionObject to an array of type Object or T (generics).
    public Object[] toArray()
    public<T> T[] toArray(T[] a)

Note: All collection classes are serializable and cloneable as all implements Cloneable, java.io.Serializable interfaces.

Note: ArrayList, Stack, Vector, CopyOnWriteArrayList implements java.util.RandomAccess for faster access to the element.

Note: Market interfaces like Cloneable, java.io.Serializable
https://www.geeksforgeeks.org/marker-interface-java/

# Is multithreading useful even on a single processor?
-> Trivially yes, as a single processor can have multiple physical cores,
and a single physical core can have multiple logical cores due to hyperthreading/SMT.
# HOWEVER, the answer to the question “is multithreading useful even on a single-threaded processor?”
-> Yes. Parallel processing isn’t really the original driving force for threaded programming.
Asynchronous processing (even with a single-threaded CPU) is originally the largest driver for threaded programming.
IO is often ludicrously slow. A single transaction to an HDD can stall for milliseconds as we wait for the
    appropriate sector of disk to come swinging around. Likewise a network transaction can take an arbitrary
    amount of time to complete as you upload or download an object. Blocking further processing upon completion
    of such IO can be a tremendous waste of CPU time.
Put simply, if our application is running even on only a single-threaded CPU, our application.
can benefit tremendously from threading techniques that separate IO bottlenecks from application logic that is CPU bound.
However, if we’re using multiple threads to number crunch then we’re gaining nothing but the overhead of additional
    threading and context switching without the benefit of actual parallel processing.

Note: Defog Tech: youtube tutorials
Note: Best number of threads in a thread pool =
1) If the task type is CPU intensive, then the ideal pool size = CPU core count
    also, considering how many other applications (or other executors/threads) are running on the same CPU.
2) If the task type is I/O intensive, then the ideal pool size = Higher than the CPU core count,
    exact number will depend on rate of task submissions and average task wait time.
    Too many threads will increase memory consumption too.


Types of Thread pools :-
1) Executors.newFixedThreadPool(fixedPoolSize); -> (LinkedBlockingQueue) creates a pool of size = fixedPoolSize
2) Executors.newCachedThreadPool(); -> (SynchronousQueue that holds only 1 task) creates a pool with dynamic number of size, used when the number of tasks
    is unknown. When a task comes to the queue, then it searches for any thread
    is that is free, if it is, then it uses it otherwise it creates a new thread for that 1 task.
    Also, if any thread remains idle for more than around 60 seconds then the thread will be killed.
3) Executors.newScheduledThreadPool(poolSize); -> (DelayedWorkQueue) creates a pool with size = poolSize.
    It will create threads more than the poolSize if needed to execute more tasks at the same time.
    1) service.schedule(task, delayTime, TimeUnit); -> task to run only once after delayTime delay
    2) service.scheduleAtFixedRate(task, initialDelay, period, TimeUnit); -> task to run repeatedly after every period time,
        at the first time, it delays for initialDelay time.
    3) service.scheduleAtFixedDelay(task, initialDelay, delay, TimeUnit); -> task to run repeatedly after
    "completion of previous task" + "delay time". at the first time, it delays for initialDelay time.
4) Executors.newSingleThreadExecutor(); (LinkedBlockingQueue)
    It ensures that the tasks are run sequentially after completion of the previous task.
    If the thread is killed and another task is required to be executed then again another thread is created,
    i.e. at a time only 1 thread will exist.
Other Custom types -> (ArrayBlockingQueue)
Note: Internally all the pool types methods returns an instance of either ThreadPoolExecutor
    or the wrapper of the ThreadPoolExecutor.  We can create thread pool by directly creating an instance
    of ThreadPoolExecutor passing required parameters to the constructor of the ThreadPoolExecutor.
-> Special threadPoll - ForkJoinPool.
It sub-tasks any task and maintain its own local queue i.e. for a thread, there will be two queues, one for the task
and another for its local sub-tasks. If any other threads are idle and any other thread has sub-tasks, then the idle thread
will steal the sub-task from the tail of the local queue of another thread and execute it.
While using ForkJoinPool, we need to be careful that all the sub-tasks will be independent of each other.
It is usually used in case of ...

# Java Memory Model
-> "happens before" style.
-> JVM compiler will do performance driven shifts of the LOCs up and down.
eg;
int a = 1; // (1) load a from main memory (2) set to 1 (3) store a
int b = 5; // (1) load b from main memory (2) set to 5 (3) store b
a++; // (1) load a from main memory (2) set to 2 (3) store a

The JVM compiler will change it to:-
int a = 1; // (1) load a from main memory (2) set to 1
a++; // (3) set to 2 (4) store a
int b = 5; // (1) load b from main memory (2) set to 5 (3) store b

Refer defog tech concurrency vs parallelism videos in youtube:-
# ReentrantReadWriteLock vs ReentrantLock vs Guava's StripedLock vs SpinLock
# ReentrantLock
-> ReentrantLock simply locks for any operation.
    Can lock and unlock multiple times by the same thread.
-> The tryLock() method will try to acquire a lock if available, and if it is then despite the fairness policy,
    it will steal the lock.
# ReentrantReadWriteLock
-> ReentrantReadWriteLock can be used to divide executions into two categories one for read and another for write.
    The read lock allows multiple threads access at the same time.
    The write lock only allows access to only 1 thread at a time.
    Also, if there is no write operation waiting thread, then all the threads trying to read can read simultaneously.
        But if there is any write operation thread waiting in the queue, then it will get the chance after the threads
        that have already acquired the lock beforehand; and other read locks will wait for that write thread to update.
# Guava's StripedLock
-> When there is a situation that there are many objects of a group and many threads then, we need lock for each object
    that increases throughput but creates memory issue. And if there is only 1 lock for that group then we loose
    throughput/performance. To resolve this, we can go with locking sub-groups of that group of object so that we can get
    the middle ground of achieving throughput and also avoiding memory issue.
    For this reason, Guava's StripedLock came into the picture
# SpinLock
-> It keeps trying to acquire the lock without going into wait state.
-> Suitable/Assumption: Most locks are used only for short period of time.
-> Case I: lock available quickly
        improves efficiency by avoiding thread switch (because in other locking mechanism, if the thread goes
        to the waiting state, it would had cost thread switches, but here no waiting scenario)
   Case II: lock takes more time
        can cause thread starvation (but in other locking mechanism, improves CPU utilization)
   Note: So, we need to use spinlock only when we have case I and avoid it when we have case II.

# Adaptive spinning concept
-> Try spinning for some time, if not available still, go into wait state.
-> JVM profiles the code and decides how much time to spin for.

# Java Interrupts
-> Java doesn't provide any way to forcefully stop the thread. Also, using Thread.stop() method is not good,
    if we stopped the thread in the middle then there can be a case of open resources that needed to be closed like
    jdbc connections.
    There is a method called cancel in future, but it only cancels if the thread haven't started the execution yet,
        if it already has, then there is no effect.
    So, we need to have a mechanism such that inside the thread/runnable implementation, whenever there is a call from
    outside to stop the thread, the thread/runnable implementation clean the resources before and notify that it
    has stopped before stopping. So Java interrupts came into picture.
    The thread making another thread execute a task, calls interrupt method then, the task's run/call etc method
    should be constantly polling/listening to any interrupts, this polling is done by using infinite while loop that
    terminates only on either interruption or after task completion. Then inside the while loop, we need to reset the
    interrupted status and throw InterruptedException to notify the thread that is interrupting it.
    So, there seems we need have a "co-operation/not by force" between the thread interrupting and the task executing.

# Semaphore
-> If we need a fixed number of threads to execute a task at a time, then we need to permit only that number of threads
    to execute a task simultaneously. So, semaphore came into picture.

# CountDownLatch
-> It keeps a count of tasks that needs to be executed. And every time a task completes, it decreases the count inside the task.
    When the count becomes 0, then the thread submitting the tasks will come out of the blocking state to the
    runnable state if it was blocked by calling countDownLatch.await() . The value/number of count and tasks can vary
    but in real scenario we usually have to complete all the tasks before further execution while we use await method.
    So, the same can be achieved by using CompletableFuture as well.

# CyclicBarrier
-> If we need certain number of tasks of same type, to be executed until certain point inside run/call method, and
    only then do further execution inside run/call method then, we need to have a barrier inside the run/call method.
    That's why CyclicBarrier came into picture.

# Phaser
-> Ability of CountDownLatch + Ability of CyclicBarrier + dynamically register/"bulk register"
    and deregister parties(a parameter).
Note: For coding, we can see simply google/docs.

# SynchronousQueue
-> When there is a scenario, where producer needs to provide 1 task to a consumer (assuming a situation where
    the consumer can only hold 1 task at a time), so we need a queue of size only 1. This can be replaced by directly
    giving/handing the task from the producer to the consumer rather than "the consumer pick the task from the queue".
    Here the producer will only send 1 task,
    and after its work completed by the consumer then only the producer will send another task.

# Exchanger
-> Similar as SynchronousQueue but with hand-off in both directions.
    i.e. both ends at the same time acting as a producer as well as a consumer.
-> For the exchange to happen, both ends should produce and consume at the same time.
    i.e. one end produces and other end consumes and vice versa simultaneously.
    eg; of use case scenario -> buffer
     where, Producer and Consumer exchanging full and empty buffer.


# Adder (eg; LongAdder)
-> In case of AtomicLong, a thread "t1" in core "c1" increments a count value in its local cache.
    Whenever another thread "t2" in core "c2" tries to get the increment, it needs updated value,
    for this the value updated by the previous thread "t1" in previous core "c1" local cache is flushed to the
    shared cache/memory, so the value needs to be refreshed in the local cache of core "c2". This flushing and
    refreshing decreases the throughput/performance. So, we need a mechanism that the keep count in one core
    "c1" local cache in a separate variable, also keep count in another core "c2" local cache, at update independently,
    and at last when we need the total count, we can sum it by using counter.sum() . For this, LongAdder came into picture.
    We need to be careful that we should not use the value of the LongAdder variable before doing sum.

# Accumulator (eg; LongAccumulator)
-> It is a generic implementation that gives functionality like Adder, but with extra capability.
    eg; LongAccumulator counter = new LongAccumulator((x,y) -> x+ y, 0);
    Here, the counter is initialized to 0, then on every execution of the run/call method,
    counter.accumulate(1); where counter is the same counter passed into the constructor of the Task.
    Here, we achieved the functionality of LongAdder. We can use it to do any operation and do a counter.get()
    at last, it simply blocks, until "counter counting"/"tasks" is completed.
    The counter passing way is similar to the Adder as well.

#Detect and resolve deadlocks
Detecting:-
-> killing the process shows the information of the threads that are deadlocked in the console itself.
-> jStack is a tool that dumps the information about the threads that are deadlocked in a file.
-> ManagementFactory.getThreadMXBean.findDeadLockedThreads() can be used to find the dead locked threads.
Preventing:-
-> Include timeouts while acquiring locks
-> Maintain order of locks, i.e. if in one place it is trying to acquire lock A and then lock B, then in another place
    as well first it needs to try for Lock A and then only lock B.
-> We can maintain global ordering of the locking.
    eg; public void doSomething(Account account1, Account account2) {
            // extra logic to tweak to prevent from deadlock
            // swap account variables if account1.getAccountNumber() is less than account2.getAccountNumber()
            synchronized(account1) {
                synchronized(account2) {
                    // do something
                }
            }
        }
        In this case as well, there might be a problem, as one thread can call doSomething(kshitijAccount, ritaAccount)
        while another thread calls doSomething(ritaAccount, kshitijAccount).
        So, we need to make use extra logic to tweak(eg; swap) it to work without deadlock.

# Data Races
-> If multiple threads access shared variable without synchronization and at the same time at least one thread is writing
    to the variable then data race condition can occur. It is the case where
    a portion of the bits of the object is changed by a thread and at the same time,
    another thread reads the object value then it will get a portion updated and remaining portion
    old value, i.e. it neither gets stale nor updated value, but gets corrupt value. This situation is called
    data race. eg; long count = 5; Here the long data type can be split internally/implicitly also called "data tearing",
    then 32 bits of the long value may be updated while other 32 bits are still old value and another thread reads it.
    This can be avoided by performing atomic operations. Java Specification says, it is atomic in case of int
    values but for others like long is not atomic

# Race Condition
-> If multiple threads access shared variable, value of the variable depends on execution order
    eg; "first thread reads then second threads reads then first thread updates then second thread updates"
        Here two threads are operating on same Java object then there may be a chance of data inconsistency problem.
    This is called Race Condition. We can overcome this problem by using synchronization.
        that makes execution like "first thread reads then updates then second thread reads then updates"

# Singleton and Double Checked Locking
-> We can achieve singleton design pattern on an object of a class, by following.
eg;
    public class Resource {
        /**
         * volatile for telling compiler to not change the order of execution, as internally while preparing Resource object
         * at the first time, it may initialize some fields but not all, this makes another thread to get
         * instance != null
         * so volatile takes care of it.
         */
        private static volatile Resource instance = null; // step 4
        private Resource() {
        }
        public static Resource getInstance() {
            if (instance
                    == null) { // check for performance enhancement to synchronized keyword, step 3
                synchronized (Resource.class) { // check for thread-safe step 2
                    if (instance
                            == null) { // check for singleton step 1 (eager initialization)
                        instance = new Resource();
                    }
                }
            }
            return instance;
        }
    }
    Note: The above way of achieving singleton is verbose (boilerplate codes), so we can go with another approach.
eg;
    class Resource {
        private static class Holder {
            static final Resource INSTANCE = new Resource();
        }
        public static Resource getInstance() {
            return Holder.INSTANCE;
        }
        private Resource() {
        }
    }
    Note: For enums
eg;
    public enum Resource {
        INSTANCE;
        Resource() {
        }
    }

Note: For all above styles, the instance is not created until first time it is invoked.


# CompletableFuture
-> As future.get() blocks the current thread executing it until it finishes, CompletableFuture comes into picture.
-> It assigns callbacks to each tasks.
-> It doesn't block the thread executing it, on wish we can block after all tasks completes.
-> The tasks in the CompletableFuture can be run in async/multi-threads for independent tasks or sequential
for dependent tasks according to our wish inside the CompletableFuture.
-> Also, the tasks in CompletableFuture run by a thread doesn't block the tasks in CompletableFuture for another thread.
eg; for futures -> fetchEmployeeTask followed by fetchTaxRateTask followed by calculateTaxTask followed by sendEmailTask
    if the order tasks is dependent, i.e. on previous task and if thread "t1" is executing CompletableFuture and is currently
    executing calculateTaxTask then for any other thread "t2" executing the CompletableFuture can run simultaneously its own
    calculateTaxTask or sendEmailTask, etc.
    This increases throughput/performance of the application.
-> One CompletableFuture can be combined with another CompletableFuture.

# Java Fibers (co-routines)
-> Fibers are lightweight, user-mode threads, scheduled by the Java virtual machine, not the operating system.
-> Fibers are low footprint and have negligible task-switching overhead.
Note: Number of Java Threads = Number of OS Threads.
        but, Number of Java Fibers is not equal to Number of OS Threads.
      Better to go with Java Fibers whenever possible. It is a type of reactive programming.

# Spring Flux/ Rx Java
-> huge methods and syntax, so hard/"takes time" for developers to learn and use Spring Flux.
-> So, should go for Java Fibers.

# Lock's Condition class
-> alternative to "wait/notify/notifyAll (that must always be in synchronized block)"
-> provides several other useful methods

Note: There is a NIO package available in java to support non-blocking I/O operations.
