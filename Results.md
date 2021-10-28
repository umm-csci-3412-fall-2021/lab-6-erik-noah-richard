# Experimental results

_Briefly (you don't need to write a lot) document the results of your
experiments with throwing a bunch of clients at your server, as described
in the lab write-up. You should probably delete or incorporate this text
into your write-up._

_You should indicate here what variations you tried (every connection gets
a new thread, using a threadpool of size X, etc., etc.), and what the
results were like when you spun up a bunch of threads that send
decent-sized files to the server._

We tried using both approaches with multithreading. Our initial approach was 
just to handle any incoming connection and assign it a new thread, and once
we got that working we implemented a thread pool using and ExecutorService.
We noticed that by changing the size of the NUM_THREADS variable, which
determined how many threads the server could make, the runtime of the 
testing script changed. When we assigned 1 the script took noticeably 
longer than when we assigned 4, and using Task Manager to view the work
done by each core we could see a difference (about a 1-5 second variation).