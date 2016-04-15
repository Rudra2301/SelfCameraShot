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


package com.tafayor.selfcamerashot.camera;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Camera;
import android.os.Build;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;


import com.tafayor.selfcamerashot.taflib.helpers.LangHelper;
import com.tafayor.selfcamerashot.taflib.helpers.LogHelper;


import java.util.ArrayList;
import java.util.List;


public class FocusManager implements Camera.AutoFocusCallback
{

    public static String TAG = FocusManager.class.getSimpleName();


    //Constants

    private static float FOCUS_AREA_MULTIPLE = 1.5f;

    private static int STATE_IDLE =0;
    private static int STATE_FOCUSING =1;
    private static int STATE_SNAPSHOT =2;
    private static int STATE_FOCUSING_FINISHED =3;



    //genral
    private Context mContext;
    private List<Camera.Area> mFocusArea; // focus area in driver format


    // State
    private int mState = STATE_IDLE;


    // Views
    private FocusOverlay mFocusOverlay;
    WindowManager.LayoutParams mOverlayLParams;
    WindowManager mWinManager;


    //Flags
    private boolean mIsUiDisplayed;
    private boolean mIsStarted;
    private boolean mIsTouchToFocusEnabled;


    //Async
    private Object mUiMutex = new Object();
    private Object mStartMutex = new Object();

    private Object mFocusMutex = new Object();
    private Handler mHandler;

    //Managers
    private GestureDetector mGestureDetector;
    private CameraParameters mCamParams;

    //Listeners
    private Listener mListener;

    //
    private boolean mMirror;
    private int mDisplayOrientation;
    private Matrix mMatrix;






    //==============================================================================================
    // Init
    //==============================================================================================
    public FocusManager(Context context, CameraParameters camParams, Listener listener)
    {
        mContext = context;
        mCamParams = camParams;
        mListener = listener;
        init();
    }



    private void init()
    {
        //initWindowManager();
        mHandler = new Handler();
        mMatrix = new Matrix();


        mGestureDetector = new GestureDetector(mContext, new ViewContainerGesture());
        loadDefaults();
    }

    private void loadDefaults()
    {
        mIsUiDisplayed = false;
        mIsStarted = false;
        mIsTouchToFocusEnabled = false;

        mMirror = false;
        mDisplayOrientation = 0;
        mFocusArea = new ArrayList<Camera.Area>();

    }



    //==============================================================================================
    // Interface
    //==============================================================================================

    public void setup( )
    {
        if(mCamParams.getFocusMode().equals(CameraParameters.FOCUS_MODE_CONTINUOUS_PICTURE))
        {
            // mCamParams.getCamera().setAutoFocusMoveCallback(this);
        }
    }


    public void setMirror(boolean mirror)
    {
        mMirror = mirror;
        setMatrix();
    }


    public void setDisplayOrientation(int displayOrientation)
    {
        mDisplayOrientation = displayOrientation;
        setMatrix();
    }




    //----------------------------------------------------------------------------------------------
    // autoFocusAndCapture
    //----------------------------------------------------------------------------------------------
    public synchronized void autoFocusAndCapture()
    {

        if (mState == STATE_FOCUSING_FINISHED)
        {

            //mCamParams.lockAeAndWb();
            capture();

        }
        else
        {


            if(mCamParams.getCamera().isOpen())
            {
                resetAutoFocus();
                startFocusSearch();
                mState = STATE_SNAPSHOT;

                // initializeFocusAreas(mListener.getDetectedFaces());

                try
                {
                    LogHelper.log("mCamParams.getCamera().autoFocus");
                    mCamParams.getCamera().autoFocus(this);
                }
                catch(Exception ex)
                {
                    LogHelper.logx(ex);
                    onAutoFocus(false, null);
                }
            }
        }
    }







    public synchronized void resetAutoFocus()
    {

        mFocusArea.clear();
        mState = STATE_IDLE;
        endFocusSearch();
        mFocusOverlay.setLocation(-1, -1);


    }


    public synchronized void cancelAutoFocus()
    {
        if( mCamParams.getCamera().isOpen())
        {
            if(mIsTouchToFocusEnabled)
            {
               // mCamParams.setFocusAreas(null);
               // mCamParams.setMetringAreas(null);
            }

            mCamParams.getCamera().cancelAutoFocus();
        }

    }


    //----------------------------------------------------------------------------------------------
    // setFocusIndicatorContainer
    //----------------------------------------------------------------------------------------------
    public void setFocusOverlay(FocusOverlay overlay)
    {
        mFocusOverlay = overlay;
        mFocusOverlay.setFocusManager(this);

    }




    //----------------------------------------------------------------------------------------------
    // onTouchEvent
    //----------------------------------------------------------------------------------------------
    public void enableTouchToFocus(boolean state)
    {

        mIsTouchToFocusEnabled = state;
        if(mIsStarted && !state)
        {
            //resetAutoFocus();
        }
    }


    //----------------------------------------------------------------------------------------------
    // start
    //----------------------------------------------------------------------------------------------
    public void start()
    {
        synchronized (mStartMutex)
        {
            if(mIsStarted)
            {
                LogHelper.log(TAG, "Already started : ");
                return;
            }
            showUi();
            mIsStarted = true;
        }
    }




    //----------------------------------------------------------------------------------------------
    // end
    //----------------------------------------------------------------------------------------------
    public void end()
    {
        synchronized (mStartMutex)
        {
            if(!mIsStarted) return;

            resetAutoFocus();
            hideUi();

            mFocusOverlay.setFocusManager(null);
            mIsStarted = false;
        }
    }


    //----------------------------------------------------------------------------------------------
    // onFocusEnd
    //----------------------------------------------------------------------------------------------
    public  synchronized void endFocusSearch()
    {

        mFocusOverlay.clearModes();
    }

    //----------------------------------------------------------------------------------------------
    // onFocusEnd
    //----------------------------------------------------------------------------------------------
    public  synchronized void endFocusSearch(int delayMs)
    {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run()
            {
                endFocusSearch();
            }
        }, delayMs);

    }

    //----------------------------------------------------------------------------------------------
    // startFocusSearch
    //----------------------------------------------------------------------------------------------
    public  synchronized void startFocusSearch()
    {

        mFocusOverlay.selectFocusSearchMode();

    }








    //==============================================================================================
    // Callbacks
    //==============================================================================================

    public void onPreviewSizeChanged(int w, int h)
    {
        LogHelper.log("onPreviewSizeChanged");
        setMatrix();

    }


    //----------------------------------------------------------------------------------------------
    // onSingleTapUp
    //----------------------------------------------------------------------------------------------
    public boolean onSingleTapUp(MotionEvent e)
    {
        try
        {
            onTouchToFocus((int) e.getX(), (int) e.getY());
        }
        catch(Exception ex)
        {
            LogHelper.logx(ex);
        }

        return true;
    }



    private synchronized void onTouchToFocus(int x, int y)
    {
        LogHelper.log("onTouchToFocus : " + mIsTouchToFocusEnabled);


        if(mState == STATE_SNAPSHOT) return;
        if(mState == STATE_FOCUSING)
        {
            resetAutoFocus();
            cancelAutoFocus();
            mListener.onTouchToFocusCanceled();
        }

        if(mIsTouchToFocusEnabled)
        {
            initializeFocusAreas(x,y);


            boolean ret = mListener.onTouchToFocusStarted();

            // LogHelper.doLog("ret start : " + ret);
            if(ret)
            {
                mState = STATE_FOCUSING;
                mFocusOverlay.setLocation(x, y);
                startFocusSearch();
                if(mCamParams.getCamera().isOpen())
                {
                    mCamParams.setFocusAreas(mFocusArea);
                    mCamParams.setMetringAreas(mFocusArea);
                    try
                    {
                        mCamParams.getCamera().autoFocus(this);
                    }
                    catch(Exception ex)
                    {
                        LogHelper.logx(ex);
                        onAutoFocus(false, null);
                    }
                }
            }

        }




    }




    //==============================================================================================
    // Internals
    //==============================================================================================


    //----------------------------------------------------------------------------------------------
    // setMatrix
    //----------------------------------------------------------------------------------------------
    private void setMatrix()
    {
        if (mFocusOverlay.getWidth() > 0 && mFocusOverlay.getHeight()> 0)
        {
            Matrix matrix = new Matrix();
            prepareMatrix(matrix, mMirror, mDisplayOrientation,
                    mFocusOverlay.getWidth(), mFocusOverlay.getHeight());
            // In face detection, the matrix converts the driver coordinates to UI
            // coordinates. In tap focus, the inverted matrix converts the UI
            // coordinates to driver coordinates.
            matrix.invert(mMatrix);

        }
    }


    //----------------------------------------------------------------------------------------------
    // prepareMatrix
    //----------------------------------------------------------------------------------------------
    private void prepareMatrix(Matrix matrix, boolean mirror, int displayOrientation,
                               int viewWidth, int viewHeight) {
        // Need mirror for front camera.
        matrix.setScale(mirror ? -1 : 1, 1);
        // This is the value for android.hardware.Camera.setDisplayOrientation.
        matrix.postRotate(displayOrientation);
        // Camera driver coordinates range from (-1000, -1000) to (1000, 1000).
        // UI coordinates range from (0, 0) to (width, height).
        matrix.postScale(viewWidth / 2000f, viewHeight / 2000f);
        matrix.postTranslate(viewWidth / 2f, viewHeight / 2f);
    }


    //----------------------------------------------------------------------------------------------
    // initializeFocusAreas
    //----------------------------------------------------------------------------------------------
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void initializeFocusAreas(int x, int y)
    {

        mFocusArea.clear();
        mFocusArea.add(new Camera.Area(new Rect(), 1000));
        // Convert the coordinates to driver format.
        calculateFocusArea(x, y, mFocusArea.get(0).rect);

    }


    //----------------------------------------------------------------------------------------------
    // initializeFocusAreas
    //----------------------------------------------------------------------------------------------
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void initializeFocusAreas(List<Rect> rects)
    {

        mFocusArea.clear();
        // Convert the coordinates to driver format.
        for(Rect rect : rects)
        {
            mFocusArea.add(new Camera.Area(rect, 1000));
        }


    }

    //----------------------------------------------------------------------------------------------
    // calculateFocusArea
    //----------------------------------------------------------------------------------------------
    private void calculateFocusArea(int x, int y,  Rect rect)
    {

        int areaWidth = (int) (mFocusOverlay.getFocusWidth());
        int areaHeight = (int) (mFocusOverlay.getFocusHeight());

        int left = LangHelper.clamp(x - areaWidth / 2, 0, mFocusOverlay.getWidth() - areaWidth);
        int right = LangHelper.clamp(x + areaWidth / 2, 0, mFocusOverlay.getWidth());
        int top = LangHelper.clamp(y - areaHeight / 2, 0, mFocusOverlay.getHeight() - areaHeight);
        int bottom = LangHelper.clamp(y + areaHeight / 2, 0, mFocusOverlay.getHeight());
        RectF rectF = new RectF(left, top, right, bottom);
        setMatrix();
        mMatrix.mapRect(rectF);
        rect.left = (int) rectF.left;
        rect.top = (int) rectF.top;
        rect.right = (int) rectF.right;
        rect.bottom = (int) rectF.bottom;

    }

    //----------------------------------------------------------------------------------------------
    // attach
    //----------------------------------------------------------------------------------------------
    public void attach()
    {


    }


    //----------------------------------------------------------------------------------------------
    // detach
    //----------------------------------------------------------------------------------------------
    public void detach()
    {

    }



    //----------------------------------------------------------------------------------------------
    // showUi
    //----------------------------------------------------------------------------------------------
    private void showUi()
    {
        synchronized (mUiMutex)
        {
            if(mIsUiDisplayed) return;
            // mWinManager.addView(mFocusOverlay, mOverlayLParams);
            attach();
            mIsUiDisplayed = true;
        }
    }



    //----------------------------------------------------------------------------------------------
    // hideUi
    //----------------------------------------------------------------------------------------------
    private void hideUi()
    {
        synchronized (mUiMutex)
        {
            if(!mIsUiDisplayed) return;

            detach();
            mIsUiDisplayed = false;
        }
    }



    //==============================================================================================
    // Implementation
    //==============================================================================================








    //======================================
    // onAutoFocus
    //======================================
    @Override
    public synchronized  void onAutoFocus(boolean success, Camera camera)
    {

        LogHelper.log("onAutoFocus : " + success);

        if(mState == STATE_IDLE) return;



        if(mState == STATE_FOCUSING)
        {

            mListener.onTouchToFocusEnded();
            endFocusSearch(250);
            mState = STATE_FOCUSING_FINISHED;


        }
        else if(mState == STATE_SNAPSHOT)
        {

            //mCamParams.lockAeAndWb();
            if(mCamParams.needsAutoFocusCapture() )
            {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        capture();
                    }
                }, 300);
            }
            else
            {
                capture();
            }


        }


        if(success) mFocusOverlay.selectFocusSuccessMode();
        else mFocusOverlay.selectFocusFailureMode();


    }




    void capture()
    {
        try
        {
            LogHelper.log(TAG, "capture");
            mListener.capture();
            mState = STATE_IDLE;
            cancelAutoFocus();
            if(mCamParams.getFocusMode().equals(CameraParameters.FOCUS_MODE_CONTINUOUS_PICTURE))
            {
                mCamParams.setFocusMode(CameraParameters.FOCUS_MODE_CONTINUOUS_PICTURE);

            }
        }
        catch(Exception ex)
        {
            LogHelper.logx(ex);
        }

    }



    //======================================
    // ViewContainerGesture
    //======================================
    private class ViewContainerGesture extends GestureDetector.SimpleOnGestureListener
    {


        @Override
        public boolean onSingleTapUp(MotionEvent e)
        {
            try
            {
                onTouchToFocus((int) e.getX(), (int) e.getY());
            }
            catch(Exception ex)
            {
                LogHelper.logx(ex);
            }


            return true;
        }
    }


    //======================================
    // ViewContainerGesture
    //======================================
   /* private class ViewContainerGesture extends GestureDetector.SimpleOnGestureListener
    private class ViewContainerGesture extends GestureDetector.SimpleOnGestureListener
    {


        @Override
        public boolean onSingleTapUp(MotionEvent e)
        {
            onTouchToFocus((int) e.getX(), (int) e.getY());
            try
            {
                onTouchToFocus((int) e.getX(), (int) e.getY());
            }
            catch(Exception ex)
            {
                LogHelper.logx(ex);
            }


            return true;
        }
    }*/





//==============================================================================================
    // Types
    //==============================================================================================



    public interface Listener
    {
        public boolean onTouchToFocusStarted();
        public void onTouchToFocusCanceled();
        public void onTouchToFocusEnded();
        public void capture();
        public void safeCapture();
        public List<Rect> getDetectedFaces();
    }




























} // FocusManager