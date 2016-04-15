/*
 * Copyright (C) 2015 Ouadban Youssef(tafayor.dev@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */


package com.tafayor.selfcamerashot.taflib.helpers;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


public class AsyncHelper
{

    static String TAG = AsyncHelper.class.getSimpleName();



    public static long   TIMEOUT_FAST = 1500;
    public static long   TIMEOUT_NORMAL = 3000;
    public static long   TIMEOUT_SLOW = 9000;
    public static long   TIMEOUT_VERY_SLOW = 15000;
    public static long   TIMEOUT_Extra_SLOW = 30000;
    public static long   TIMEOUT_SLOWEST = 60000;









    public static class TimeoutRunnable  implements Runnable
    {
        public static String TAG = TimeoutRunnable.class.getSimpleName();


        private long mTimeout = 10; // ms
        private Runnable mTask = null;
        private Thread mThread;
        ReentrantLock mMutex;
        Condition mCond;
        private volatile boolean mIsAlive;



        public TimeoutRunnable()
        {
            mIsAlive = false;
            mMutex = new ReentrantLock();
            mCond = mMutex.newCondition();
            mThread = new Thread(new InnerRunnable(this));

        }

        public void setTimeout(long timeoutMs){mTimeout = timeoutMs;}

        public void run()
        {
            mIsAlive = true;
            mThread.start();
            try
            {
                mMutex.lock();
                if(mIsAlive){ if(!mCond.await(mTimeout, TimeUnit.MILLISECONDS)) onTimeouted();}

            }
            catch (InterruptedException e)
            {
                onInterrupted();
            }
            finally
            {
                mMutex.unlock();
                mThread.interrupt();
            }

        }



        public void terminate()
        {
            mThread.interrupt();
        }



        protected void timeoutRun()
        {
            if(mTask != null) mTask.run();
        }



        protected void onTimeouted(){}
        protected void onInterrupted(){}


        private static class InnerRunnable implements Runnable
        {
            WeakReference<TimeoutRunnable> outerPtr;
            public InnerRunnable(TimeoutRunnable o)
            {
                outerPtr = new WeakReference<TimeoutRunnable>(o);
            }

            @Override
            public void run()
            {
                TimeoutRunnable outer = outerPtr.get();
                if(outer == null) return;

                outer.timeoutRun();

                outer.mMutex.lock();
                outer.mIsAlive = false;
                try
                {
                    outer.mCond.signal();
                }
                catch(Exception e)
                {
                    outer.mMutex.unlock();
                }


            }


        };

    }




    }
