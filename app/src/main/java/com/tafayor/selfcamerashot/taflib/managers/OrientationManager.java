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



package com.tafayor.selfcamerashot.taflib.managers;

import android.content.Context;
import android.hardware.SensorManager;
import android.view.OrientationEventListener;



;import com.tafayor.selfcamerashot.App;
import com.tafayor.selfcamerashot.taflib.types.WeakArrayList;


public class OrientationManager
{
    public static String TAG = OrientationManager.class.getSimpleName();




    //==============================================================================================
    // Data
    //==============================================================================================

    private Context mContext;

    private WeakArrayList<OrientationListener> mListeners;
    private OrientationEventListenerImpl mOrientationListener;
    private int mLastX90Orientation;
    boolean mIsEnabled;


    private static class Loader {  private static final OrientationManager INSTANCE = new OrientationManager(); }



    //==============================================================================================
    // Init
    //==============================================================================================

    //----------------------------------------------------------------------------------------------
    // RunningAppsManager
    //----------------------------------------------------------------------------------------------
    private OrientationManager()
    {
        mContext = App.getContext();
        mListeners = new WeakArrayList<OrientationListener>();
        mOrientationListener = new OrientationEventListenerImpl(mContext,
                SensorManager.SENSOR_DELAY_NORMAL);
        mLastX90Orientation = 0;
        mIsEnabled = false;
    }

    //----------------------------------------------------------------------------------------------
    // getInstance
    //----------------------------------------------------------------------------------------------
    public static OrientationManager getInstance()
    {
        return Loader.INSTANCE;

    }



    //==============================================================================================
    // Interface
    //==============================================================================================


    //----------------------------------------------------------------------------------------------
    // getOrientation
    //----------------------------------------------------------------------------------------------
    public int getOrientation()
    {
        return mOrientationListener.getOrientation();
    }




    //----------------------------------------------------------------------------------------------
    // getX90Orientation
    //----------------------------------------------------------------------------------------------
    public int getX90Orientation()
    {
        return mOrientationListener.getX90Orientation();
    }



    //----------------------------------------------------------------------------------------------
    // release
    //----------------------------------------------------------------------------------------------
    public synchronized void release( )
    {
        mListeners.clear();
        mOrientationListener.disable();
        mIsEnabled = false;
    }

    //==============================================================================================
    // Callbacks
    //==============================================================================================

    //----------------------------------------------------------------------------------------------
    // onOrientationChanged
    //----------------------------------------------------------------------------------------------
    public void onOrientationChanged(int orientation)
    {
        notifyOrientationListeners(orientation);
        int newX90Orientation = mOrientationListener.getX90Orientation();
        if(mLastX90Orientation != newX90Orientation)
        {

            notifyX90OrientationListeners(newX90Orientation);
            notifyX90OrientationListeners(mLastX90Orientation, newX90Orientation);
            mLastX90Orientation = newX90Orientation;
        }
    }




    //==============================================================================================
    // Listeners
    //==============================================================================================


    //----------------------------------------------------------------------------------------------
    // addListener
    //----------------------------------------------------------------------------------------------
    public synchronized void   addListener(OrientationListener listener)
    {
        mListeners.addUnique(listener);

        Runnable task = new Runnable() {
            @Override
            public void run() {
                mOrientationListener.enable();
            }
        };


        if(!mIsEnabled)
        {
            (new Thread(task)).start();
        }


    }

    //----------------------------------------------------------------------------------------------
    // removeListener
    //----------------------------------------------------------------------------------------------
    public synchronized void removeListener(OrientationListener listener)
    {
        mListeners.remove(listener);
        if(mListeners.size()<= 0)
        {
            mOrientationListener.disable();
            mIsEnabled = false;
        }
    }


    //----------------------------------------------------------------------------------------------
    // notifyOrientationListeners
    //----------------------------------------------------------------------------------------------
    public void notifyOrientationListeners(int orientation)
    {

        for(OrientationListener listener : mListeners)
        {
            listener.onOrientationChanged(orientation);
        }
    }


    //----------------------------------------------------------------------------------------------
    // notifyX90OrientationListeners
    //----------------------------------------------------------------------------------------------
    public void notifyX90OrientationListeners(int orientation)
    {
        for(OrientationListener listener : mListeners)
        {
            listener.onX90OrientationChanged(orientation);
        }
    }


    //----------------------------------------------------------------------------------------------
    // notifyX90OrientationListeners
    //----------------------------------------------------------------------------------------------
    public void notifyX90OrientationListeners(int from, int to)
    {
        for(OrientationListener listener : mListeners)
        {
            listener.onX90OrientationChanged(from, to);
        }
    }
    //==============================================================================================
    // Internals
    //==============================================================================================






    //==============================================================================================
    // Implementation
    //==============================================================================================


    //==========================
    // OrientationListener
    //==========================
    class OrientationEventListenerImpl extends OrientationEventListener
    {
        private int mOrientation = 0;
        public OrientationEventListenerImpl(Context context) {
            super(context);
        }

        public OrientationEventListenerImpl(Context context, int rate) {
            super(context, rate);
        }

        @Override
        public void onOrientationChanged(int orientation)
        {
            mOrientation = orientation;
            OrientationManager.this.onOrientationChanged(orientation);
        }


        public int getOrientation()
        {
            return mOrientation;
        }


        public  int getX90Orientation()
        {
            return getX90Orientation(mOrientation);
        }


        public  int getX90Orientation(int angle)
        {
            if (angle > 315 || angle <= 45)
            {
                return 0;
            }
            else if (angle > 45 && angle <= 135)
            {
                return 90;
            }
            else  if (angle > 135 && angle <= 225)
            {
                return 180;
            }
            else if (angle > 225 && angle <= 315)
            {
                return 270;
            }
            else
            {
                return 0;
            }
        }
    }



    //==============================================================================================
    // Types
    //==============================================================================================

    //----------------------------------------------------------------------------------------------
    // OrientationListener
    //----------------------------------------------------------------------------------------------
    public static class  OrientationListener
    {
        public void onOrientationChanged(int orientation){}
        public void onX90OrientationChanged(int orientation){}
        public void onX90OrientationChanged(int from, int to){}
    }




}


