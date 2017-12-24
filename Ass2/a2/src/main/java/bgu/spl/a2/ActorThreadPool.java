package bgu.spl.a2;



import sun.misc.Queue;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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
	private ConcurrentHashMap<String,Queue<Action>> _Actors = new ConcurrentHashMap<>();
	private Vector<String>  _ActorsId = new Vector<>();
	private ConcurrentHashMap<String,Boolean> _ActorsOccupied = new ConcurrentHashMap<>();
	private HashMap<String,PrivateState> _ActorsPrivateState = new HashMap<>();
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
	/*
	the reason for the sync is that i want to avoid a situation where a thread enters this function searches for the actor not finding it
	and creating a new actor , there can be a situation where two threads enter this func and two of the create the same actor
	 */
	public void submit(Action<?> action, String actorId, PrivateState actorState) {
		synchronized (_Actors) {
			boolean Submitted = false;
			for (int i = 0; i < _ActorsId.size() & !Submitted; i++) {
				if (_ActorsId.get(i).equals(actorId)) {
					_Actors.get(_ActorsId.get(i)).enqueue(action);
					Submitted = true;
				}
			}//Actor Not found
			if (!Submitted) {
				Queue<Action> x = new Queue<>();
				x.enqueue(action);
				_ActorsId.add(actorId);//now we need to add the name and private state to the appropriate queue
				_Actors.put(actorId,x);
				_ActorsOccupied.put(actorId, false);
				_ActorsPrivateState.put(actorId, actorState);
			}
			_version.inc();//updates pool's version
			_Actors.notifyAll();
		}
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
		shutdown = true;
		this._version.shutdown = true;
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
	/**
	 * getter for actors
	 * @return actors
	 */
	public Map<String, PrivateState> getActors(){
		return _ActorsPrivateState;
	}
	/**
	 * getter for actor's private state
	 * @param actorId actor's id
	 * @return actor's private state
	 */
	public PrivateState getPrivateStates(String actorId){
		return _ActorsPrivateState.get(actorId);
		//Should be able to find the actor id if not found return null
	}

	/**
	 * this function operates the pool, it basiclly times the threads so no two threads go over the actors
	 * this prevents chaos and allows for a thread to pick up an action and lock in to an actor safely
	 *
	 */
	////Added Functions
	private void ThreadFunction() {
		while(!shutdown) {
			boolean Executing = false;
			String WorkingActor = null;
			PrivateState WorkingPrivateState = null;
			Action Act = null;
			int currentversion;
			synchronized (_Actors) {//Looking for available queue only one thread can use this field at any moment
				Iterator<String> actorIdIterator = _ActorsId.iterator();
				while (actorIdIterator.hasNext() && !Executing) {
					String ActorId = actorIdIterator.next();
					Queue<Action> actor = _Actors.get(ActorId);
					if (!_ActorsOccupied.get(ActorId) && !actor.isEmpty()) {//Available queue with actions in it
						_ActorsOccupied.put(ActorId,true);//this queue is occupied
						try {
							Act = actor.dequeue();
						}catch(InterruptedException e){System.out.println("WTF");}
						Executing = true;
						WorkingActor = ActorId;
						WorkingPrivateState = _ActorsPrivateState.get(ActorId);
					}
				}
				currentversion = this._version.getVersion();
				_Actors.notifyAll();//Wake the threads waiting on this vector of queue
			}//only one thread goes through the vector the reason is an exception thrown by the iterator
			if (!Executing) {//No available Actors
				try {
					this._version.await(currentversion);
				} catch (InterruptedException e) {//A thread has just Completed a Turn
				}
			} else {//Executing = true means we need to exe an action
				Act.handle(this, WorkingActor, WorkingPrivateState);
				_ActorsOccupied.put(WorkingActor,false);//the thread finished with this queue can be use by another thread
				_version.inc();
			}
			Thread.currentThread().interrupt();

		}
	}

}