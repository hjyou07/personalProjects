## A10: Multithreading
- HeeJun You
- In this assignment, a multi-threaded version of DirectoryParser.c from a9 is explored.  
The DirectoryParser.c is modified into DirectoryParser_MT.c, and allow for multiple threads to run in parsing and indexing the files.  
A source data is given in a compressed file format due to its size, and Benchmarker.c is run with the given source data to compare the runtime between executions with different number of threads.  
### Part 2: Getting More Data and Testing it
The execution of the benchmarker with data/ took too long, and made it inconvenient for repeatability.  
Hence the results from data_tiny/ and data_small/ is also included.
- 1st try:  
  
| time taken | no thread | 5 thread  | 10 thread | number of files            |  
| ---------- | --------- | --------- | --------- | -------------------------- |  
| data_tiny/ | 0.000651  | 0.002222  | 0.001307  | 204 entries in the index   |  
| data_small/| 0.121983  | 0.134101  | 0.149209  | 20062 entries in the index |  
| data/      | 589.76052 | 767.59644 | **        | 639209 entries in the index|  
  
** This gives an Assert007(list->num_elements > 0) failed in file LinkedList.c, line 201.
running benchmarker with data/ itself takes very long, and my computer can't take the load of running this with valgrind.  
So I couldn't get far with debugging, but I observed the same assertion error is randomly thrown for data_small/ as well.
It would sometimes give the error and sometimes not, so I couldn't track what was causing it since it seemed "random".
When I try running valgrind to look more into the error, it actually finishes running without the assertion error,  
and have no errors and no memory leaks - which leaves me even more confused. 
To whomever will be test running this, if you run in to a segfault or an assertion error, try running with valgrind and observe the behavior.  
    
- 2nd try:  
  
| time taken | no thread | 5 thread  | 10 thread | number of files            |  
| ---------- | --------- | --------- | --------- | -------------------------- |  
| data_tiny/ | 0.000657  | 0.001407  | 0.001299  | 204 entries in the index   |  
| data_small/| 0.123938  | 0.126188  | 0.143258  | 20062 entries in the index |  
| data/      | N/A       | N/A       | N/A       | 639209 entries in the index|  
  
- 3rd try:  
  
| time taken | no thread | 5 thread  | 10 thread | number of files            |  
| ---------- | --------- | --------- | --------- | -------------------------- |  
| data_tiny/ | 0.000678  | 0.001260  | 0.001895  | 204 entries in the index   |  
| data_small/| 0.124056  | 0.126868  | 0.147156  | 20062 entries in the index |  
| data/      | N/A       | N/A       | N/A       | 639209 entries in the index|  
### Part 3: Experiment
The benchmarker was executed again after makeing the virtual machine operate on quad-core.  

- quad-core:  

| time taken | no thread | 1 thread  | 5 thread  | 10 thread | number of files            |
| ---------- | --------- | --------- | --------- | --------- | -------------------------- |
| data_tiny/ | 0.000462  | 0.000926  | 0.001545  | 0.001981  | 204 entries in the index   |
| data_small/| 0.111938  | 0.117791  | 0.157200  | 0.163258  | 20062 entries in the index |
| data/      | N/A       | N/A       | N/A       | N/A       | 639209 entries in the index|

- ```Is there a difference between running DirectoryParser_MT with one thread, versus the original single-threaded DirectoryParser?```  
There is no significant difference between them, if anything, running the Parser with one thread takes longer than the original single-theaded Parser. I believe this small difference is coming from the overhead from creating a thread and joining it.
- ```What may explain what you observe?```  
What I observed in this multi-threading experiment is that threading doesn't help at all in this particular situation. This inefficiency seems to come from the fact that we're placing both mutexes inside the while loop. The overhead introduced by locking and unlocking mutexes for how many times the iteration takes place, is larger than the reduced overhead by using multipler threads. A bigger lag is observed with a larger amount of data and higher number of threads, which does imply the effect of the while loop on this poor performance of threading. Since we can't take advantage of threading due to having to place the mutexes in a loop, having it run on a quad-core doesn't helpt either. In this particular situation, it is hard to avoid placing mutexes inside the loop, as there are two mutexes and for them to be exclusive and not interfere with another, they can't be taken out of each respecitve loop it resides in.   
