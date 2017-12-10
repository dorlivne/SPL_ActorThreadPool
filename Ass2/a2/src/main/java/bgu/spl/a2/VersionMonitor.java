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


    public int getVersion() {
            return version.get();
        }


    public  void inc() {
        this.version.incrementAndGet();
    }

    /**synchronized is needed because this method is where threads go to wait and wait has to have a lock
     * @param version
     * if current version is diffrent from version throws execption
     * @throws InterruptedException
     */
    public synchronized void await(int version) throws InterruptedException {
        while(this.version.get() == version) {//we wait until the version of our objects equals to the needed version
                this.wait();
        }
   //TODO: this.notifyAll();-why isn't needed?
       throw new InterruptedException();

    }
}
