package bgu.spl.a2;

import com.sun.xml.internal.ws.api.pipe.FiberContextSwitchInterceptor;

import java.util.Iterator;
import java.util.Queue;
import java.util.Vector;

/**
 * represents an actor thread pool - to understand what this class does please
 * refer to your assignment.
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add can only be
 * private, protected or package protected - in other words, no new public
 * methods
 */
public class ActorThreadPool {
	private Vector<Thread> _Threads = new Vector<>();
	private boolean shutdown = false;
	private Vector<ActorQueue<Action>> _Actors = new Vector<>();
	private VersionMonitor _version;


	/**
	 * creates a {@link ActorThreadPool} which has nthreads. Note, threads
	 * should not get started until calling to the {@link #start()} method.
	 * <p>
	 * Implementors note: you may not add other constructors to this class nor
	 * you allowed to add any other parameter to this constructor - changing
	 * this may cause automatic tests to fail..
	 *
	 * @param nthreads the number of threads that should be started by this thread
	 *                 pool
	 */
	public ActorThreadPool(int nthreads) {
		int i = 0;
		while (i < nthreads) {
			_Threads.add(new Thread(() -> ThreadFunction()));
			i++;
		}
		_version = new VersionMonitor();
	}

	/**
	 * submits an action into an actor to be executed by a thread belongs to
	 * this thread pool
	 *
	 * @param action     the action to execute
	 * @param actorId    corresponding actor's id
	 * @param actorState actor's private state (actor's information)
	 */
	public void submit(Action<?> action, String actorId, PrivateState actorState) {
		boolean Submitted = false;
		for (int i = 0; i < _Actors.size() & !Submitted; i++) {
			if (_Actors.get(i).get_Actorid().equals(actorId)) {
				_Actors.get(i).enqueue(action);
				Submitted = true;
			}
		}//Actor Not found
		if (!Submitted) {
			ActorQueue<Action> x = new ActorQueue<>(actorId, actorState);
			x.enqueue(action);
			_Actors.add(x);
		}
		_version.inc();//updates pool's version
	}

	/**
	 * closes the thread pool - this method interrupts all the threads and waits
	 * for them to stop - it is returns *only* when there are no live threads in
	 * the queue.
	 * <p>
	 * after calling this method - one should not use the queue anymore.
	 *
	 * @throws InterruptedException if the thread that shut down the threads is interrupted
	 */
	public void shutdown() throws InterruptedException {
		for (Thread thread : _Threads)
			thread.interrupt();//All threads listen in
		for (Thread thread : _Threads)
			thread.join();   //Kills the threads
	}

	/**
	 * start the threads belongs to this thread pool
	 */
	public void start() {
		for (Thread thread : _Threads)
			thread.start();


	}

	////Added Functions
	private void ThreadFunction() {
		while(!shutdown) {
			boolean Executing = false;
			ActorQueue<Action> WorkingQueue = null;
			synchronized (_Actors) {//Looking for available queue
				Iterator<ActorQueue<Action>> actorIterator = _Actors.iterator();
				while (actorIterator.hasNext() && !Executing) {
					ActorQueue<Action> actor = actorIterator.next();
					if (!actor.is_Occupied() & !actor.isEmpty()) {//Available actorqueue with actions in it
						actor.set_Occupied(true);
						Executing = true;
						WorkingQueue = actor;
						_Actors.notifyAll();//Wake the threads waiting on this vector of queue
					}
				}
			}//only one thread goes through the vector the reason is an exception thrown by the iterator
			if (!Executing) {//No available Actors or actions
				try {
					this._version.await(_version.getVersion());
				} catch (InterruptedException e) {//A thread has just Completed a Turn
				}
			} else {//Executing = true means we need to exe an action
				//TODO by using the boolean Occupied we may be able to get past that problem/TODO by using the boolean Occupied we may be able to get past that problem
				//TODO maybe need a sync on this queue problem is the iterator is going through the queue so if some queue is locked an Exception is being thrown
				try {
				    Action Act = ((Action)WorkingQueue.dequeue());
				    Act.handle(this, WorkingQueue.get_Actorid(), WorkingQueue.get_PrivateState());
				} catch (InterruptedException e) {
				}
				WorkingQueue.set_Occupied(false);//the thread finished with this queue can be use by another thread
				_version.inc();
			}
		}
	}
}



