package com.ark.adkit.basics.handler;

import java.util.Queue;

final class ActionSyncTask implements Action, Task {

    private final Action mAction;
    private boolean mDone = false;
    private Queue<Task> mPool = null;

    ActionSyncTask(Action action) {
        this.mAction = action;
    }

    /**
     * In this we call cal the {@link Func} and check should run it
     */
    @Override
    public void call() {
        // Cleanup reference the pool
        mPool = null;
        // Doing
        mAction.call();
    }

    /**
     * Run to doing something
     */
    @Override
    public void run() {
        if (!mDone) {
            synchronized (this) {
                if (!mDone) {
                    call();
                    mDone = true;
                    try {
                        this.notifyAll();
                    } catch (Exception ignored) {
                    }
                }
            }
        }
    }

    /**
     * Wait to run end
     */
    void waitRun() {
        if (!mDone) {
            synchronized (this) {
                while (!mDone) {
                    try {
                        this.wait();
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        }
    }

    /**
     * Wait for a period of time to run end
     *
     * @param waitMillis      wait milliseconds time
     * @param waitNanos       wait nanoseconds time
     * @param cancelOnTimeOut when wait end cancel the runner
     */
    void waitRun(long waitMillis, int waitNanos, boolean cancelOnTimeOut) {
        if (!mDone) {
            synchronized (this) {
                if (!mDone) {
                    try {
                        this.wait(waitMillis, waitNanos);
                    } catch (InterruptedException ignored) {
                    } finally {
                        if (!mDone && cancelOnTimeOut) {
                            mDone = true;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void setPool(Queue<Task> pool) {
        mPool = pool;
    }

    @Override
    public boolean isDone() {
        return mDone;
    }

    @Override
    public void cancel() {
        if (!mDone) {
            synchronized (this) {
                mDone = true;

                // clear the task form pool
                if (mPool != null) {
                    //noinspection SynchronizeOnNonFinalField
                    synchronized (mPool) {
                        if (mPool != null) {
                            try {
                                mPool.remove(this);
                            } catch (Exception e) {
                                e.getStackTrace();
                            } finally {
                                mPool = null;
                            }
                        }
                    }
                }
            }
        }
    }
}