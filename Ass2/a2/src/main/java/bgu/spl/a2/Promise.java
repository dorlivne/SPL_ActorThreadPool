package bgu.spl.a2;


import java.util.LinkedList;

/**
 * this class represents a deferred result i.e., an object that eventually will
 * be resolved to hold a result of some operation, the class allows for getting
 * the result once it is available and registering a callback that will be
 * called once the result is available.
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add can only be
 * private, protected or package protected - in other words, no new public
 * methods
 *
 * @param <T>
 *            the result type, <boolean> resolved - initialized ;
 */
public class Promise<T>{
private T result = null;
private boolean resolved = false;
private LinkedList<callback> callBacks = new LinkedList<>();

	/**
	 *
	 * @return the resolved value if such exists (i.e., if this object has been
	 *         {@link #resolve(java.lang.Object)}ed 
	 * @throws IllegalStateException
	 *             in the case where this method is called and this object is
	 *             not yet resolved
	 */
	public  T get() {//we presume that two threads can't access at the same time to the same Promise (the same "this") - no sync needed
		if (this.isResolved())
			return this.result;
		else
			throw new IllegalStateException("Object is not resolved");
	}

	/**
	 *
	 * @return true if this object has been resolved - i.e., if the method
	 *         {@link #resolve(java.lang.Object)} has been called on this object
	 *         before.
	 */
	public boolean isResolved() {
		return resolved;

	}


	/**
	 * resolve this promise object - from now on, any call to the method
	 * {@link #get()} should return the given value
	 *
	 * Any callbacks that were registered to be notified when this object is
	 * resolved via the {@link #subscribe(callback)} method should
	 * be executed before this method returns
	 *
     * @throws IllegalStateException
     * 			in the case where this object is already resolved
	 * @param value
	 *            - the value to resolve this promise object with
	 */
	public void resolve(T value){
		result =  value;
		this.resolved = true;
		int index = 0;//current index in the list
		while( this.callBacks.size() != 0 && index< this.callBacks.size()) {
			this.callBacks.get(index).call();
			//this.callBacks.remove(index);
			index++;
		}
		//callBacks = null;//possible error - how to re-define it? / done because of possible memory leak
	}

	/**
	 * add a callback to be called when this object is resolved. If while
	 * calling this method the object is already resolved - the callback should
	 * be called immediately
	 *
	 *TODO- Note that in any case, the given callback should never get called more
	 * TODO than once, in addition, in order to avoid memory leaks - once the
	 * TODO callback got called, this object should not hold its reference any
	 *TODO longer.
	 *
	 * @param callback
	 *            the callback to be called when the promise object is resolved
	 */
	public void subscribe(callback callback) {
		if(!this.isResolved())
			this.callBacks.add(callback);
		else
			callback.call();


	}
}
