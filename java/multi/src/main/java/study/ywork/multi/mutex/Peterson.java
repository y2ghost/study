package study.ywork.multi.mutex;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.Condition;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

// Peterson互斥算法-无饥饿、无死锁
class Peterson implements Lock {
    private boolean[] flag = new boolean[2];
    private int victim;

    public void lock() {
        int i = ThreadID.get();
        int j = 1 - i;
        flag[i] = true;
        victim = i;
        while (flag[j] && victim == i) {
        }
        ; // 自旋
    }

    public void unlock() {
        int i = ThreadID.get();
        flag[i] = false;
    }

    @Override
    public Condition newCondition() {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public boolean tryLock() {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return "Peterson [flag=" + Arrays.toString(flag) + ", victim=" + victim + "]";
    }
}
