package bgu.spl.a2;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * an abstract class that represents an action that may be executed using the
 * {@link ActorThreadPool}
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add to this class can
 * only be private!!!
 *
 * @param <R> the action result type
 */
public abstract class Action<R> {
    private Promise<R> promise = new Promise<>();
    private boolean Contiunation = false;
    private callback Tag;
    private ActorThreadPool pool;
    private String ActorId;
    private String ActionName;
    private PrivateState ActorState;

	/**
     * start handling the action - note that this method is protected, a thread
     * cannot call it directly.
     */
    protected abstract void start();
    

    /**
    *
    * start/continue handling the action
    *
    * this method should be called in order to start this action
    * or continue its execution in the case where it has been already started.
    *
    * IMPORTANT: this method is package protected, i.e., only classes inside
    * the same package can access it - you should *not* change it to
    * public/private/protected
    *
    */
   /*package*/ final void handle(ActorThreadPool pool, String actorId, PrivateState actorState) {
       this.ActorId = actorId;
       this.pool = pool;
       this.ActorState = actorState;
       if(this.Contiunation) {
           this.Tag.call();
           Contiunation = false;
       }else
           start();
   }
    
    
    /**
     * add a callback to be executed once *all* the given actions results are
     * resolved
     * 
     * Implementors note: make sure that the callback is running only once when
     * all the given actions completed.
     *
     * @param actions
     * @param callback the callback to execute once all the results are resolved
     */
    protected final void then(Collection<? extends Action<?>> actions, callback callback) {
        this.Tag = callback;
        this.Contiunation = true;
        Iterator< ? extends  Action<?> > NeedToComplete = actions.iterator();
        if(actions.size() == 0)
            callback.call();
        AtomicInteger ActionLeft =new AtomicInteger(actions.size());//amount of action to be fulfilled
        while (NeedToComplete.hasNext()) {
          //  if (!CurrentMission.getResult().isResolved())//check if current mission is resolved
            NeedToComplete.next().getResult().subscribe(() -> {
                                        ActionLeft.decrementAndGet();
                                        if(ActionLeft.intValue() == 0)///Completed all the actions
                                           sendMessage(this,ActorId,ActorState);//submit the continuation to the pool
                                         });
            }
    }

    /**
     * resolve the internal result - should be called by the action derivative
     * once it is done.
     *
     * @param result - the action calculated result
     */
    protected final void complete(R result) {
        if(!promise.isResolved())
            promise.resolve(result);

    }
    
    /**
     * @return action's promise (result)
     */
    public final Promise<R> getResult() {
        return promise;
    }
    
    /**
     * send an action to an other actor
     * 
     * @param action
     * 				the action
     * @param actorId
     * 				actor's id
     * @param actorState
	 * 				actor's private state (actor's information)
	 *    
     * @return promise that will hold the result of the sent action
     */
	public Promise<?> sendMessage(Action<?> action, String actorId, PrivateState actorState){
        this.pool.submit(action,actorId,actorState);
        return action.getResult();
	}
	
	/**
	 * set action's name
	 * @param actionName
	 */
	public void setActionName(String actionName){
        this.ActionName = actionName;
	}
	
	/**
	 * @return action's name
	 */
	public String getActionName(){
       return ActionName;
	}
}