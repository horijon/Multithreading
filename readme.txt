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
4) Multiple tasks with same runnable/callable instance creates member variables of that runnable/callable class per task,
    so we need to reduce this memory issue; also static variables change is visible to per task of that same runnable/callable
    and the change by one thread is seen by the other thread, this is also an issue.
    So, we go with ThreadLocal to reduce memory issue and it is usually for static variables that makes a thread
    not see changes by another thread i.e. it creates the object instances per thread but not per task.
    The child thread cannot access parents thread variable values as ThreadLocal class is used,\
    so InheritableThreadLocal came into picture. Internal implementation of the threadLocal -
    each threadLocal is kept in a map, so that we can set/get by each threadLocal instance.
5) While iterating inside the task and getting and setting on the loop, this may arise inconsistency, so
    in such case we need to use Atomic wrapper classes like AtomicInteger, AtomicBoolean.
6) To reduce the expensive operations of creating thread each time for each task/job i.e. Runnable/Callable, etc,
    we used threadpool. But while doing so, it brought concurrency problem when using ThreadLocal class only,
    so we need to call remove method on that threadLocal instance inside that task(runnable/callable) class to remove
    ThreadLocal value of that thread to its initial value, after every task completes.
eg; of Threadpool -> ExecutorService service = Executors.newFixedThreadPool(3);
						service.submit(task); // task/job/runnble/callable
7) If multiple threads need to work on the same object/collection, and the change needs to be visible by the other threads
    in addition to relatively increased performance then we need to go for Concurrent collections.
8) Runnable is used when the return type of the thread execution is void. We can use Callable that returns an object
    after execution of the call method. We can use Future(like a promise in javascript) to hold the callable return object.
    We can get the returned value by using future.get().
	eg; Future future = service.submit(task);
		future.get();

	Note: ThreadPool can be shutdown by using
		service.shutdown();
9) future.get() blocks the execution of the thread further that is the thread executing this line future.get() .
    If we have many future.get() calls, every other future or future.get() calls are delayed even if the other calls are
    independent to the prior calls, this arises performance issue, so we use CompletableFuture.
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
2) Iterator (Universal iterator) :-
-> To iterate on any collection classes + read/remove while iterating
forEach or for in loop internally uses Iterator, so we can implement Iterator on custom classes
eg; Employee class; loop Employee objects just by using for in or for each loop.
https://www.journaldev.com/13460/java-iterator
but why bother, as it can be done rather, just by iterating the list.
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