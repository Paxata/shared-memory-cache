package util;


/**
 * Created by shachar on 2/4/16.
 */


import sun.misc.Unsafe;


/**
 * Spin Lock
 * <p>This is a lock designed to protect VERY short sections of
 * critical code.  Threads attempting to take the lock will spin
 * forever until the lock is available, thus it is important that
 * the code protected by this lock is extremely simple and non
 * blocking. The reason for this lock is that it prevents a thread
 * from giving up a CPU core when contending for the lock.</p>
 * <pre>
 * try(SpinLock.Lock lock = spinlock.lock())
 * {
 *   // something very quick and non blocking
 * }
 * </pre>
 */

public class AtomicLock {
    private static final Unsafe unsafe = JavaInternals.getUnsafe();
    long address_;

    public AtomicLock(long address) {
        address_ = address;
    }

    public void dump(int offset) {
        System.out.println(String.format("%02X", unsafe.getInt(address_ + offset)));
    }

    public void lock() {

        while (true) {
            if (!isLocked()) {
                if (unsafe.compareAndSwapInt(null,address_, 0, 1)) {
                    break;
                }
            }
            assert true;
        }
    }


    public boolean isLocked() {
        return unsafe.getInt(address_) != 0;
    }


    public void unlock() {
        unsafe.putOrderedInt(null,address_, 0);
    }
}
