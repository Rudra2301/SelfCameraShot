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
import android.annotation.TargetApi;
import android.hardware.Camera;
import android.os.Build;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


import com.tafayor.selfcamerashot.taflib.helpers.LogHelper;


import java.io.IOException;

/**
 * Created by weber on 06/03/2015.
 */
public class CameraWrapper
{
    public static String TAG = CameraWrapper.class.getSimpleName();

    private Camera mCamera;
    private boolean mIsPreviewRunning = false;




    private static class Loader {  private static final CameraWrapper INSTANCE = new CameraWrapper(); }



    public static CameraWrapper i()
    {
        return Loader.INSTANCE;
    }


    private  CameraWrapper()
    {

    }







    //----------------------------------------------------------------------------------------------
    // setFaceDetectionListener
    //----------------------------------------------------------------------------------------------
    @SuppressLint("NewApi")
    public  void setFaceDetectionListener(Camera.FaceDetectionListener listener)
    {
        if(mCamera == null) return;
        if(Build.VERSION.SDK_INT < 14)  return;

        mCamera.setFaceDetectionListener(listener);

    }


    //----------------------------------------------------------------------------------------------
    // startFaceDetection
    //----------------------------------------------------------------------------------------------
    @SuppressLint("NewApi")
    public  void startFaceDetection()
    {
        if(mCamera == null) return;
        if(Build.VERSION.SDK_INT < 14)  return;
        mCamera.startFaceDetection();
    }


    //----------------------------------------------------------------------------------------------
    // stopFaceDetection
    //----------------------------------------------------------------------------------------------
    @SuppressLint("NewApi")
    public  void stopFaceDetection()
    {
        if(mCamera == null) return;
        if(Build.VERSION.SDK_INT < 14)  return;
        mCamera.stopFaceDetection();
    }


    //----------------------------------------------------------------------------------------------
    // setDisplayOrientation
    //----------------------------------------------------------------------------------------------
    public synchronized void setDisplayOrientation(int degrees)
    {
        if(mCamera == null) return;

        mCamera.setDisplayOrientation(degrees);
    }





    //----------------------------------------------------------------------------------------------
    // takePicture
    //----------------------------------------------------------------------------------------------
    public synchronized void takePicture(Camera.ShutterCallback shutter, Camera.PictureCallback raw,
                                         Camera.PictureCallback jpeg)
    {
        if(mCamera == null) return;


        mCamera.takePicture(shutter, raw, jpeg);


    }

    //----------------------------------------------------------------------------------------------
    // setPreviewDisplay
    //----------------------------------------------------------------------------------------------
    public synchronized void startPreview()
    {
        if(mCamera == null) return;
        if(mIsPreviewRunning) return;

        mCamera.startPreview();
        mIsPreviewRunning = true;

    }


    //----------------------------------------------------------------------------------------------
    // forceStartPreview
    //----------------------------------------------------------------------------------------------
    public synchronized void forceStartPreview()
    {
        if (mCamera == null) return;
        mCamera.startPreview();
        mIsPreviewRunning = true;
    }


    //----------------------------------------------------------------------------------------------
    // stopPreview
    //----------------------------------------------------------------------------------------------
    public synchronized void stopPreview()
    {
        try
        {
            if(mCamera == null) return;
            if(!mIsPreviewRunning) return;
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mIsPreviewRunning = false;
        }
        catch(Exception ex)
        {
            LogHelper.logx(ex);
        }

    }


    //----------------------------------------------------------------------------------------------
    // isPreviewRunning
    //----------------------------------------------------------------------------------------------
    public synchronized boolean isPreviewRunning()
    {
        if(mCamera == null) return false;

        return mIsPreviewRunning;
    }

    //----------------------------------------------------------------------------------------------
    // setPreviewDisplay
    //----------------------------------------------------------------------------------------------
    public synchronized void setPreviewDisplay(SurfaceHolder holder ) throws IOException
    {
        if(mCamera == null) return;

        mCamera.setPreviewDisplay(holder);
    }


    //----------------------------------------------------------------------------------------------
    // getRawCamera
    //----------------------------------------------------------------------------------------------
    public  Camera getRawCamera()
    {
        return mCamera;

    }


    //----------------------------------------------------------------------------------------------
    // setCamParams
    //----------------------------------------------------------------------------------------------
    public synchronized void setParameters(Camera.Parameters params)
    {
        if(mCamera == null) return;

        Camera.Parameters oldParams = null;
        try
        {
            oldParams = mCamera.getParameters();
            mCamera.setParameters(params);
        }
        catch(Exception ex)
        {
            LogHelper.logx(ex);
            if(oldParams != null)
            {
                try{mCamera.setParameters(oldParams);}catch(Exception ex2){}
            }
        }
    }


    //----------------------------------------------------------------------------------------------
    // getParameters
    //----------------------------------------------------------------------------------------------
    public synchronized Camera.Parameters getParameters()
    {
        if(mCamera == null) return null;

        return mCamera.getParameters();
    }


    //----------------------------------------------------------------------------------------------
    // enableShutterSound
    //----------------------------------------------------------------------------------------------
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public synchronized void enableShutterSound(boolean enabled)
    {
        if(mCamera == null) return;

        if(Build.VERSION.SDK_INT >= 17)
        {
            mCamera.enableShutterSound(enabled);
        }

    }

    //----------------------------------------------------------------------------------------------
    // isCameraOpen
    //----------------------------------------------------------------------------------------------
    public synchronized boolean isOpen()
    {
        return (mCamera!=null);

    }


    //----------------------------------------------------------------------------------------------
    // openCamera
    //----------------------------------------------------------------------------------------------
    public synchronized void open(int id)
    {
        if(isOpen()) close();



        mCamera = mCamera.open(id);

    }


    //----------------------------------------------------------------------------------------------
    // closeCamera
    //----------------------------------------------------------------------------------------------
    public synchronized void close()
    {
        if(mCamera == null) return;

        try
        {
            if(isPreviewRunning()) stopPreview();
        }
        finally
        {

            mCamera.release();
            mCamera = null;
        }



    }



    //----------------------------------------------------------------------------------------------
    // autoFocus
    //----------------------------------------------------------------------------------------------
    public synchronized void autoFocus(Camera.AutoFocusCallback cb)
    {
        LogHelper.log("(autoFocus");
        LogHelper.log("(mCamera : " + mCamera);
        if(mCamera == null) return;


        mCamera.autoFocus(cb);


    }


    //----------------------------------------------------------------------------------------------
    // cancelAutoFocus
    //----------------------------------------------------------------------------------------------
    public synchronized void cancelAutoFocus()
    {
        if(mCamera == null) return;



        try
        {
            LogHelper.log(TAG, "cancelAutoFocus");
            mCamera.cancelAutoFocus();
        }
        catch(Exception ex)
        {
            LogHelper.logx(ex);
        }
    }


    //----------------------------------------------------------------------------------------------
    // setAutoFocusMoveCallback
    //----------------------------------------------------------------------------------------------
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public synchronized void setAutoFocusMoveCallback(Camera.AutoFocusMoveCallback cb)
    {
        if(mCamera == null) return;

        mCamera.setAutoFocusMoveCallback(cb);
    }
}
