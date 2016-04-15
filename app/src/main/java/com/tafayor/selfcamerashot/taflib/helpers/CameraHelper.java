
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

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Build;


public class CameraHelper
{

    public static String TAG = CameraHelper.class.getSimpleName();


 


    private static int sCameraFirstIndex = -1;



    //==============================================================================================
    // Interface
    //==============================================================================================







    //----------------------------------------------------------------------------------------------
    // getCamerasCount
    //----------------------------------------------------------------------------------------------
    @SuppressLint("NewApi")
    public static  int getCamerasCount(Context ctx)
    {
        int count = 0;

        if(Build.VERSION.SDK_INT >= 21)
        {
            CameraManager manager = (CameraManager) ctx.getSystemService(Context.CAMERA_SERVICE);
            try
            {
                count = manager.getCameraIdList().length;

            } catch ( CameraAccessException e)
            {
                LogHelper.logx(e);
            }

        }
        else
        {
            count = Camera.getNumberOfCameras();
        }


        return count;
    }


    //----------------------------------------------------------------------------------------------
    // isFrontCamera
    //----------------------------------------------------------------------------------------------
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static  boolean isFrontCamera(Context ctx, String cameraId)
    {

        boolean isFront = false;

        CameraManager manager = (CameraManager) ctx.getSystemService(Context.CAMERA_SERVICE);
        try
        {
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);

            if (characteristics.get(CameraCharacteristics.LENS_FACING)
                    == CameraCharacteristics.LENS_FACING_FRONT)
            {
                isFront = true;
            }

        } catch (CameraAccessException e)
        {
            LogHelper.logx(e);
        }

        return isFront;
    }


    //----------------------------------------------------------------------------------------------
    // isFrontCamera
    //----------------------------------------------------------------------------------------------
    public static  boolean isFrontCamera(int cameraId)
    {

        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, cameraInfo);

        return (cameraInfo.facing==Camera.CameraInfo.CAMERA_FACING_FRONT);

    }


    //----------------------------------------------------------------------------------------------
    // isBackCamera
    //----------------------------------------------------------------------------------------------
    public static  boolean isBackCamera(int cameraId)
    {
        return !isFrontCamera(cameraId);
    }

    //----------------------------------------------------------------------------------------------
    // getCameraFirstId
    //----------------------------------------------------------------------------------------------
    public static   int getCameraFirstId(Context ctx)
    {

        if(sCameraFirstIndex != -1) return sCameraFirstIndex;

        Camera camera = null;


        try
        {
            camera = Camera.open(0);
            sCameraFirstIndex = 0;
            camera.release();
            camera = null;

        }
        catch(Exception ex)
        {
            sCameraFirstIndex = 1;
        }


        return sCameraFirstIndex;
    }







    //----------------------------------------------------------------------------------------------
    // areBothCamerasSupported
    //----------------------------------------------------------------------------------------------
    public static  boolean areBothCamerasSupported()
    {

        return (Camera.getNumberOfCameras()>1);

    }



    //----------------------------------------------------------------------------------------------
    // isBackCameraSupported
    //----------------------------------------------------------------------------------------------
    public static  boolean isBackCameraSupported(Context ctx)
    {
        PackageManager pm = ctx.getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    //----------------------------------------------------------------------------------------------
    // isFrontCameraSupported
    //----------------------------------------------------------------------------------------------
    public static  boolean isFrontCameraSupported(Context ctx)
    {
        PackageManager pm = ctx.getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT);
    }



    //----------------------------------------------------------------------------------------------
    // isCameraSupported
    //----------------------------------------------------------------------------------------------
    public static  boolean isCameraSupported(int cameraId)
    {
        return (Camera.getNumberOfCameras() > cameraId);
    }



    //==============================================================================================
    // Internals
    //==============================================================================================



}
