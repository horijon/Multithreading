1) For multitasking we went for multithreading.
    -> Thread class gave that functionality but lacked multiple inheritance feature so, Runnable came into picture.
    -> But still it had concurrency problems.
2) Then for achieving concurrency in addition to multithreading, we went for separate member instance of each member variables
    per job/task.
    -> It brought memory problems as each task/job have separate instance of each member variables of the Thread class.
3) Then to achieve multithreading, concurrency and also avoid memory problems, we went for another approach.
    -> Having separate member instance per thread but not per task/job/Runnable. com.kk.ThreadLocal provided that functionality.
    -> This relatively increased performance and avoided memory problems.

Note:
1) Threads operate on separate cache memory/main memory but volatile keyword pushes to work on the same shared memory.
    But still it doesn't solve concurrency as at the same time 2 threads can read the same value but may update on the same value
    as well i.e. if initial value of an int variable is 0 and 2 threads each increments int value by 1 then instead of 2,
    we may get 1 as the final value of that variable; so to solve this problem, synchronized keyword came into picture
    for that increment method. But it may arise deadlock situations using synchronized keyword, and performance also decreases,
    so instead of using synchronized keyword in addition to volatile keyword we went for similar locking like approach that
    combines both; several wrapper classes are provided for achieving this like AtomicInteger, AtomicBoolean etc.
2) It has same effects for both static and non-static case with either com.kk.ThreadLocal or other simple object in multithreading.
So, com.kk.ThreadLocal is the only way for achieving member instance per thread to achieve concurrency.
3) But as different threads were required for different job/tasks i.e. Runnable instances, it again had memory issues.
 So, ThreadPool came into the picture that uses same threads for different tasks to improve memory problems.
 But while doing so, it brought concurrency problem when using com.kk.ThreadLocal class only, so we went for locking approach,
 that uses Reentrant lock that have remove method used to remove com.kk.ThreadLocal value to its initial value, after every task completes.
4) Also, the child thread cannot access parents thread variable values as com.kk.ThreadLocal class is used, so
    InheritableThread came into picture.
5) Internal implementation of the threadLocal - each threadlocal is kept in a map, so that we can set/get by each threadlocal
instance.

Note: Study other multithreading concepts from the text books "multithreading 1", "multithreading 2" and "multithreading 3".

Note: Study more on -> concurrent collections, latches, spring boot way of multithreading, multithreading use case scenarios
    in real life, mechanisms/technologies that use multithreading like scheduling, tomcat web server, etc