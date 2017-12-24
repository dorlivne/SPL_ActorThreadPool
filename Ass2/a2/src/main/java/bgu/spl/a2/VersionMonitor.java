package bgu.spl.a2;

import java.util.concurrent.atomic.AtomicInteger;


/**
 * Describes a monitor that supports the concept of versioning - its idea is
 * simple, the monitor has a version number which you can receive via the method
 * {@link #getVersion()} once you have a version number, you can call
 * {@link #await(int)} with this version number in order to wait until this
 * version number changes.
 *
 * you can also increment the version number by one using the {@link #inc()}
 * method.
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add can only be
 * private, protected or package protected - in other words, no new public
 * methods
 */
public class VersionMonitor {

    private AtomicInteger version = new AtomicInteger();//The initial version equals to zero
    boolean shutdown = false;

    public int getVersion() {
        return version.get();
    }


    /**
     * Increment the version Monitor and notifying threads waiting on him
     * the reason for the sync is to making sure that each threads who wants to increment the VM
     * will increment the VM
     */
    public synchronized void inc() {
        this.version.incrementAndGet();
        this.notifyAll();
    }

    /**synchronized is needed because this method is where threads go to wait and wait has to have a lock
     * @param version
     * if current version is diffrent from version throws execption
     * @throws InterruptedException
     */
    public synchronized void await(int version) throws InterruptedException {
        while(!shutdown && version == this.getVersion() ) {//we wait until the version of our objects equals to the needed version
            this.wait();
        }
        this.notifyAll();
        throw new InterruptedException();

    }
}
