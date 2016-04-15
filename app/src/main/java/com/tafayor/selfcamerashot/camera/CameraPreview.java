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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.AttributeSet;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;


import com.tafayor.selfcamerashot.taflib.helpers.CameraHelper;
import com.tafayor.selfcamerashot.taflib.helpers.DisplayHelper;
import com.tafayor.selfcamerashot.taflib.helpers.LogHelper;
import com.tafayor.selfcamerashot.taflib.managers.OrientationManager;


import java.util.ArrayList;
import java.util.List;


public class CameraPreview extends SurfaceView implements
        SurfaceHolder.Callback,Camera.PictureCallback,
        Camera.ShutterCallback,Camera.PreviewCallback


{

    public static String TAG = CameraPreview.class.getSimpleName();

    public static final int ERROR_OTHER = 0;
    public static final int ERROR_OPEN_CAMERA_FAILED = 1;
    public static final int ERROR_PREVIEW_FAILED     = 2;



    private SurfaceHolder mHolder;
    private CameraWrapper mCamera = null;
    private Camera.Parameters mParameters;
    private byte[] mBuffer;
    private int mCameraId;
    private Context mContext;
    private CameraPreviewListener mListener = null;
    //private boolean mIsCameraOpen = false;

    private int mDisplayOrientation;
    private OrientationListenerImpl mOrientationListener;
    private int mDeviceOrienationAtShot;
    private int mLayoutOrientation;

    private Camera.Size mPreviewSize;
    private static volatile HandlerThread mThread;
    private static volatile Handler mAsyncHandler;
    private volatile boolean mIsSurfaceReady = false;
    private Handler mUiHandler;
    private int mJpegRotation;




    Camera.FaceDetectionListener mFaceDetectionListener;
    List<Rect> mFaceRects;


    //==============================================================================================
    // Init
    //==============================================================================================
    public CameraPreview(Context context)
    {
        super(context);
        mContext = context.getApplicationContext();
        init();
    }



    public CameraPreview(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mContext = context.getApplicationContext();
        init();


    }


    private void init()
    {
        mCamera = CameraWrapper.i();
        mUiHandler = new Handler();

        mHolder = getHolder();
        mHolder.addCallback(this);

        if(Build.VERSION.SDK_INT<=14)
        {
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        mCameraId = 0;
        mOrientationListener = new OrientationListenerImpl();


        mFaceRects = new ArrayList<>();

    }




    //==============================================================================================
    // Interface
    //==============================================================================================



    //----------------------------------------------------------------------------------------------
    // getDetectedFaces
    //----------------------------------------------------------------------------------------------
    public List<Rect> getDetectedFaces()
    {
        return mFaceRects;
    }


    //----------------------------------------------------------------------------------------------
    // isPreviewReadyForStart
    //----------------------------------------------------------------------------------------------
    public synchronized boolean isPreviewReadyForStart()
    {
        return mIsSurfaceReady;
    }


    //----------------------------------------------------------------------------------------------
    // getCameraId
    //----------------------------------------------------------------------------------------------
    public int getCameraId()
    {
        return mCameraId ;

    }


    //----------------------------------------------------------------------------------------------
    // getCamera
    //----------------------------------------------------------------------------------------------
    public synchronized CameraWrapper getCamera()
    {
        return mCamera ;

    }


    //----------------------------------------------------------------------------------------------
    // isReady
    //----------------------------------------------------------------------------------------------
    public synchronized boolean isReady()
    {
        return mCamera.isOpen();

    }


    //----------------------------------------------------------------------------------------------
    // setCamParams
    //----------------------------------------------------------------------------------------------
    public void setCamParams(Camera.Parameters params)
    {
        if(mCamera == null) return ;
        mCamera.setParameters(params);

    }

    //----------------------------------------------------------------------------------------------
    // getCameraParameters
    //----------------------------------------------------------------------------------------------
    public Camera.Parameters getCamParams()
    {
        if(mCamera == null) return null;

        return mCamera.getParameters();


    }

    //----------------------------------------------------------------------------------------------
    // getJpegRotation
    //----------------------------------------------------------------------------------------------
    public  int getJpegRotation()
    {
        return mJpegRotation;
    }



    //----------------------------------------------------------------------------------------------
    // takePicture
    //----------------------------------------------------------------------------------------------
    public  void takePicture()
    {
        if(!isCameraOpen()) return;

        LogHelper.log(TAG, "takePicture");


        updateJpegRotation();
        mCamera.takePicture(this, null, this);



    }


    //----------------------------------------------------------------------------------------------
    // openCamera
    //----------------------------------------------------------------------------------------------
    public synchronized void openCamera(int id)
    {
        LogHelper.log(TAG, "openCamera : " + id);
        mCameraId = id;
        try
        {

            mCamera.open(id);

            OrientationManager.getInstance().addListener(mOrientationListener);
            if(mListener!=null) mListener.onCameraOpened();
            if(mIsSurfaceReady) startPreview();

        }
        catch(Exception ex)
        {

            LogHelper.logx(ex);
            if(mListener != null)
            {
                mListener.onCameraError(ex, ERROR_OPEN_CAMERA_FAILED);
            }
        }

    }



    //----------------------------------------------------------------------------------------------
    // closeCamera
    //----------------------------------------------------------------------------------------------
    public synchronized void closeCamera()
    {
        LogHelper.log(TAG, "closeCamera : ");


        getHolder().removeCallback(this);
        mCamera.close();

        OrientationManager.getInstance().removeListener(mOrientationListener);
        //mIsCameraOpen = false;

    }


    //----------------------------------------------------------------------------------------------
    // isCameraOpen
    //----------------------------------------------------------------------------------------------
    public synchronized boolean isCameraOpen()
    {
        return (mCamera != null  && mCamera.isOpen());

    }



    //----------------------------------------------------------------------------------------------
    // restartPreview
    //----------------------------------------------------------------------------------------------
    public synchronized void restartPreview()
    {
        try
        {
            if(!mCamera.isOpen()) return;
            mCamera.stopPreview();
            mCamera.startPreview();
            if(mListener!=null) mListener.onPreviewStarted();
        }
        catch(Exception ex)
        {
            LogHelper.logx(ex);
        }

    }

    //----------------------------------------------------------------------------------------------
    // startCameraPreview
    //----------------------------------------------------------------------------------------------
    public synchronized void startPreview()
    {
        LogHelper.log(TAG, "startPreview");
        mUiHandler.post(new Runnable() {
            @Override
            public void run()
            {
                startPreviewTask();
            }
        });
    }



    //----------------------------------------------------------------------------------------------
    // startCameraPreview
    //----------------------------------------------------------------------------------------------
    private synchronized void startPreviewTask()
    {
        LogHelper.log(TAG, "startCameraPreviewTask start");
        if(!mCamera.isOpen()) return;
        if(mCamera.isPreviewRunning()) return;


        determineDisplayOrientation();


        try
        {
            if(mListener!=null) mListener.onPrePreviewStart();
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

            if(mListener!=null) mListener.onPreviewStarted();
        }
        catch(Exception ex)
        {
            LogHelper.logx(TAG, ex);
            if(mListener != null) mListener.onCameraError(ex, ERROR_PREVIEW_FAILED);
        }



        LogHelper.log(TAG, "startCameraPreviewTask end");
    }


    //----------------------------------------------------------------------------------------------
    // stopCameraPreview
    //----------------------------------------------------------------------------------------------
    public synchronized void stopCameraPreview()
    {
        try
        {
            //stopFaceDetection();
            mCamera.stopPreview();

        }
        catch (Exception ex)
        {
            LogHelper.logx(TAG, ex);
            if(mListener != null) mListener.onCameraError(ex, ERROR_OTHER);
        }
    }



    //----------------------------------------------------------------------------------------------
    // stopCameraPreview
    //----------------------------------------------------------------------------------------------
    public synchronized boolean isPreviewRunning()
    {

        return mCamera.isPreviewRunning();
    }



    //==============================================================================================
    // Callbacks
    //==============================================================================================


    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();

        mThread = new HandlerThread("");
        mThread.start();
        mAsyncHandler = new Handler(mThread.getLooper());
    }

    @Override
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();

        mAsyncHandler.removeCallbacksAndMessages(null);
        mThread.quit();
    }


    //----------------------------------------------------------------------------------------------
    // onSurfaceCreated
    //----------------------------------------------------------------------------------------------
    void onSurfaceCreated()
    {
        LogHelper.log("onSurfaceCreated");
        LogHelper.log("isCameraOpen() : " + isCameraOpen());
        if(isCameraOpen())
        {
            if(mListener!= null) mListener.onPreviewReadyForStart();
        }
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {

        final int width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int height = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        setMeasuredDimension(width, height);


    }


    //==============================================================================================
    // Listeners
    //==============================================================================================


    public void setListener(CameraPreviewListener listener)
    {
        mListener = listener;
    }


    //==============================================================================================
    // Internals
    //==============================================================================================

/*
    //----------------------------------------------------------------------------------------------
    // startFaceDetection
    //----------------------------------------------------------------------------------------------
    public void startFaceDetection()
    {
        mCamera.setFaceDetectionListener(mFaceDetectionListener);
        mCamera.startFaceDetection();
    }

    //----------------------------------------------------------------------------------------------
    // stopFaceDetection
    //----------------------------------------------------------------------------------------------
    public void stopFaceDetection()
    {
        mCamera.stopFaceDetection();

    }*/


    //----------------------------------------------------------------------------------------------
    // setupFaceDetection
    //----------------------------------------------------------------------------------------------
    @SuppressLint("NewApi")
    public void setupFaceDetection()
    {
        if(Build.VERSION.SDK_INT < 14)  return;

        mFaceDetectionListener = new Camera.FaceDetectionListener()
        {
            @Override
            public void onFaceDetection(Camera.Face[] faces, Camera camera)
            {
                List<Rect> faceRects = new ArrayList<>();
                LogHelper.log("onFaceDetection : " + faces.length);
                for (int i=0; i<faces.length; i++)
                {
                    int left = faces[i].rect.left;
                    int right = faces[i].rect.right;
                    int top = faces[i].rect.top;
                    int bottom = faces[i].rect.bottom;
                    Rect rect = new Rect(left, top, right, bottom);
                    faceRects.add(rect);
                }
                mFaceRects.clear();
                mFaceRects.addAll(faceRects);
                if(mListener != null) mListener.onFaceDetection(mFaceRects);
            }
        } ;
    }


    //----------------------------------------------------------------------------------------------
    // updateJpegRotation
    //----------------------------------------------------------------------------------------------
    public void updateJpegRotation()
    {
        if(!isCameraOpen()) return;

        int deviceOrientation = OrientationManager.getInstance().getX90Orientation();
        int rotation = 0;
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(mCameraId, info);
        boolean isFront = (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT);


        if (deviceOrientation != OrientationEventListener.ORIENTATION_UNKNOWN)
        {
            deviceOrientation = (deviceOrientation + 45) / 90 * 90;


            if (isFront)
            {
                rotation = (info.orientation - deviceOrientation + 360) % 360;
            } else {  // back-facing camera
                rotation = (info.orientation + deviceOrientation) % 360;
            }
        }




        if (rotation == 0 || rotation == 90 || rotation == 180
                || rotation == 270)
        {
            mJpegRotation = rotation;
            Camera.Parameters params = getCamParams();

            params.setRotation(mJpegRotation);
            setCamParams(params);
        }


    }


    public int getDisplayOrientation()
    {
        return mDisplayOrientation;
    }

    //----------------------------------------------------------------------------------------------
    // determineDisplayOrientation
    //----------------------------------------------------------------------------------------------
    public void determineDisplayOrientation()
    {
        mDisplayOrientation  = CameraParameters.getDisplayOrientation(mContext, mCameraId);
        mCamera.setDisplayOrientation(mDisplayOrientation);


    }


















    //==============================================================================================
    // Implementation
    //==============================================================================================

    //==========================
    // SurfaceHolder.Callback
    //==========================




    //==========================
    // SurfaceHolder.Callback
    //==========================

    @Override
    public synchronized void surfaceCreated(SurfaceHolder holder)
    {
        LogHelper.log(TAG, "surfaceCreated");

        mIsSurfaceReady = true;

        mAsyncHandler.post(new Runnable()
        {
            @Override
            public void run() {
                onSurfaceCreated();
            }
        });



    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {

    }

    @Override
    public synchronized void surfaceDestroyed(SurfaceHolder holder)
    {
        LogHelper.log(TAG, "surfaceDestroyed");
        mIsSurfaceReady = false;
    }




    //==========================
    // Camera.PictureCallback
    //==========================

    @Override
    public  void onPictureTaken(byte[] data, Camera camera)
    {

        LogHelper.log(TAG, "onPictureTaken");

        if(!isCameraOpen())
        {
            LogHelper.log(TAG, "onPictureTaken", "camera is not open");
            return;
        }



        // Camera HAL of some devices have a bug. Starting preview
        // immediately after taking a picture will fail. Wait some
        // time before starting the preview.
        mUiHandler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    mCamera.forceStartPreview();
                }
                catch(Exception ex){ LogHelper.logx(ex); }
            }
        },300);




        if(mListener != null)
        {


            int pictureRotation ;


            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(mCameraId, cameraInfo);


            if(CameraHelper.isFrontCamera(mCameraId))
            {
                pictureRotation =  360 + cameraInfo.orientation - mDeviceOrienationAtShot ;
            }
            else
            {
                pictureRotation =  cameraInfo.orientation  + mDeviceOrienationAtShot ;
            }

            pictureRotation = pictureRotation% 360;



            mListener.onPictureTaken(data, pictureRotation);
        }



    }





    //==========================
    // Camera.ShutterCallback
    //==========================
    @Override
    public void onShutter()
    {
        LogHelper.log(TAG, "onShutter start ");
        mDeviceOrienationAtShot = OrientationManager.getInstance().getX90Orientation();
        if(mListener != null) mListener.onShutter();

        LogHelper.log(TAG, "onShutter end ");
    }


    //==========================
    // Camera.PreviewCallback
    //==========================
    @Override
    public void onPreviewFrame(byte[] data, Camera camera)
    {
        if(mListener != null) mListener.onPreviewFrame(data);
    }








    //==========================
    // OrientationListener
    //==========================
    class OrientationListenerImpl extends OrientationManager.OrientationListener
    {

    }

    //==============================================================================================
    // Types
    //==============================================================================================




    public interface CameraPreviewListener
    {

        public void onCameraOpened();
        public void onCameraError(Exception ex, int error);
        public void onPictureTaken(byte[] data, int pictureRotation);
        public void onPreviewFrame(byte[] data);
        public void onShutter();
        public void onPreviewReadyForStart();
        public void onPreviewStarted();
        public void onPrePreviewStart();
        public void onFaceDetection(List<Rect> faces);
    }



}// end CameraView
