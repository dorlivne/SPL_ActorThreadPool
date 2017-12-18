package bgu.spl.a2.sim;
import bgu.spl.a2.Promise;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;

/**
 *
 * this class is related to {@link Computer}
 * it indicates if a computer is free or not
 *
 * Note: this class can be implemented without any synchronization.
 * However, using synchronization will be accepted as long as the implementation is blocking free.
 *
 */
public class SuspendingMutex {
	private AtomicBoolean Locked = new AtomicBoolean();
	private Computer computer;
	private Promise<Boolean> Key;


	/**
	 * Constructor
	 * @param computer
	 */
	public SuspendingMutex(Computer computer){
		this.computer = computer;
		Locked.set(false);//set the Lock to false at initialize

	}
	/**
	 * Computer acquisition procedure
	 * Note that this procedure is non-blocking and should return immediatly
	 *
	 * @param computerType
	 * 					computer's type
	 *
	 * @return a promise for the requested computer
	 */
	public Promise<Computer> down() {
		/*
			What we need to understand is that the first thread ever to try and acquire this computer will pass,
			other that will come after him will try to lock this computer but wont be able to because of locked
			they all subscribe(Waiting List) to the same key which we create for each thread who acquire this computer
	       	once up is called the key callbacks the waiting list the first one to enter initialize the same process with a new key
	       	but also with a new promise which is bad....that is why we created TryLock where the ones who tried to acquire to
	       	would not need to initialize a new promise
		 */
		Promise<Computer> promise = new Promise<>();//a promise for each request --we have to beacuse we can only resolve once
		TryLock(promise);//try to lock to a computer
		return promise;
	}

	/**
	 * Computer return procedure
	 * releases a computer which becomes available in the warehouse upon completion
	 *
	 * @param computer
	 */
	public void up(){
		Locked.set(false);//we release a computer from use locked = false
		Key.resolve(true);//callback the waiting list the first one who answer will get the computer
	}
	//Added Functions
	private void TryLock(Promise<Computer> promise){
		boolean Owner = false;
		synchronized (Locked) {
			if (Locked.compareAndSet(false, true)) {//if false then set to true and aquire computer
				Key = new Promise<>();//a new key for a new tenant
				Owner = true;
				//promise.resolve(computer);
			}
			Locked.notifyAll();
		}
			if(!Owner)
				Key.subscribe(() -> TryLock(promise));//try again once the key is freed
			else
				promise.resolve(computer);
		}

	}

