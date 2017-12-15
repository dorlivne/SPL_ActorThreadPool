package bgu.spl.a2;



import sun.misc.Queue;

import java.util.*;

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
	private Vector<Queue<Action>> _Actors = new Vector<>();
	private Vector<String>   _ActorsId = new Vector<>();
	private HashMap<String,Boolean>  _ActorsOccupied = new HashMap<>();
	private HashMap<String,PrivateState> _ActorsPrivateState = new HashMap<>();
	private VersionMonitor _version;
	private Object Lock = new Object();



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
	the reason for the Lock-sync is that i want to avoid a situation where a thread enters this function searches for the actor not finding it
	and creating a new actor , there can be a situation where two threads enter this func and two of the create the same actor
	 */
	public void submit(Action<?> action, String actorId, PrivateState actorState) {
		boolean Submitted = false;
		for (int i = 0; i < _Actors.size() & !Submitted; i++) {
			if (_ActorsId.get(i).equals(actorId)) {
				_Actors.get(i).enqueue(action);
				Submitted = true;
			}
		}//Actor Not found
		if (!Submitted) {
			synchronized (Lock){//TODO Is this needed??
				if(_ActorsPrivateState.containsKey(actorId)){//means some other thread already added this actor
					submit(action,actorId,actorState);
					Lock.notifyAll();
					return;
				}
				Queue<Action> x = new Queue<>();
				x.enqueue(action);
				_Actors.add(x);//Add the Queue of Actions to The vector
				_ActorsId.add(actorId);//now we need to add the name and private state to the appropriate queue
				_ActorsOccupied.put(actorId,false);
				_ActorsPrivateState.put(actorId,actorState);
				Lock.notifyAll();
			}
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

	////Added Functions
	private void ThreadFunction() {
		while(!shutdown) {
			boolean Executing = false;
			Queue<Action> WorkingQueue = null;
			String WorkingActor = null;
			PrivateState WorkingPrivateState = null;
			synchronized (_Actors) {//Looking for available queue
				Iterator<Queue<Action>> actorIterator = _Actors.iterator();
				Iterator<String> actorIdIterator = _ActorsId.iterator();
				while (actorIterator.hasNext() && !Executing) {
					Queue<Action> actor = actorIterator.next();
					String ActorId = actorIdIterator.next();
					if (!_ActorsOccupied.get(ActorId) & !actor.isEmpty()) {//Available queue with actions in it
						_ActorsOccupied.put(ActorId,true);
						Executing = true;
						WorkingQueue = actor;
						WorkingActor = ActorId;
						WorkingPrivateState = _ActorsPrivateState.get(ActorId);
						_Actors.notifyAll();//Wake the threads waiting on this vector of queue
					}
				}
			}//only one thread goes through the vector the reason is an exception thrown by the iterator
			if (!Executing) {//No available Actors
				try {
					this._version.await(_version.getVersion());
				} catch (InterruptedException e) {//A thread has just Completed a Turn
				}
			} else {//Executing = true means we need to exe an action
				try {//because of Interrupt exception throw fromm queue
					Action Act = WorkingQueue.dequeue();
					WorkingPrivateState.addRecord(Act.getActionName());
					Act.handle(this, WorkingActor, WorkingPrivateState);
				} catch (InterruptedException e) {}
				_ActorsOccupied.put(WorkingActor,false);//the thread finished with this queue can be use by another thread
				_version.inc();
			}
		}
	}

	/*protected Vector<ActorQueue<Action>> get_Actors() {
		return _Actors;
	}*/
}



