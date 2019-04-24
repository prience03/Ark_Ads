package com.ark.adkit.polymers.polymer.utils;

import android.os.Handler;
import android.os.SystemClock;

public class ClockTicker {

    private boolean mTickerStopped;
    private Handler mHandler;
    private Runnable mTicker;
    private long endTime;

    private ClockListener mClockListener;


    public void setClockListener(ClockListener clockListener) {
        this.mClockListener = clockListener;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public void start() {
        if (mHandler != null && mTicker != null) {
            mHandler.removeCallbacks(mTicker);
        }
        mTickerStopped = false;
        mHandler = new Handler();
        mTicker = new Runnable() {
            public void run() {
                if (mTickerStopped) {
                    return;
                }
                long currentTime = System.currentTimeMillis();
                long distanceTime = endTime - currentTime;
                distanceTime /= 1000;
                if (distanceTime == 0) {
                    release();
                    if (null != mClockListener) {
                        mClockListener.onTick(0);
                        mClockListener.timeEnd();
                    }
                } else if (distanceTime < 0) {
                    if (null != mClockListener) {
                        mClockListener.onTick(0);
                        mClockListener.timeEnd();
                    }
                } else {
                    if (null != mClockListener) {
                        mClockListener.onTick(dealTime(distanceTime));
                    }
                }
                long now = SystemClock.uptimeMillis();
                long next = now + (1000 - now % 1000);// 够不够一秒,保证一秒更新一次
                mHandler.postAtTime(mTicker, next);
            }
        };
        mTicker.run();
    }

    public long dealTime(long time) {
        return ((time % (24 * 60 * 60)) % (60 * 60)) % 60;
    }

    public void release() {
        mTickerStopped = true;
        mHandler.removeCallbacks(mTicker);
    }

    public interface ClockListener {

        void timeEnd();

        void onTick(long tick);
    }
}