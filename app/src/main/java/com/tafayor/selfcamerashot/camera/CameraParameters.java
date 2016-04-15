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
import android.app.Activity;
import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.Surface;
import android.view.WindowManager;


import com.tafayor.selfcamerashot.App;
import com.tafayor.selfcamerashot.device.DeviceProfile;
import com.tafayor.selfcamerashot.prefs.FlashModeValues;
import com.tafayor.selfcamerashot.prefs.FocusModeValues;
import com.tafayor.selfcamerashot.prefs.IsoValues;
import com.tafayor.selfcamerashot.prefs.SceneModeValues;
import com.tafayor.selfcamerashot.prefs.WhiteBalanceValues;
import com.tafayor.selfcamerashot.taflib.helpers.CameraHelper;
import com.tafayor.selfcamerashot.taflib.helpers.LangHelper;
import com.tafayor.selfcamerashot.taflib.helpers.LogHelper;
import com.tafayor.selfcamerashot.taflib.types.Size;
import com.tafayor.selfcamerashot.taflib.types.WeakArrayList;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CameraParameters
{

    public static String TAG = CameraParameters.class.getSimpleName();


    //Param map
    public static String KEY_PARAM = "param";
    public static String KEY_VALS = "vals";
    public static String KEY_MAX= "max";
    public static String KEY_MIN= "min";
    public static String KEY_STEP= "step";



    public static String KEY_EXPOSURE = "contrast";



    public static String KEY_ZSL = "zsl";
    public static String KEY_ZSD_MODE = "zsd-mode";


    //Flash
    public static String FOCUS_MODE_AUTO = Camera.Parameters.FOCUS_MODE_AUTO;
    public static String FOCUS_MODE_CONTINUOUS_PICTURE = Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE;
    public static String FOCUS_MODE_FIXED = Camera.Parameters.FOCUS_MODE_FIXED;
    public static String FOCUS_MODE_INFINITY = Camera.Parameters.FOCUS_MODE_INFINITY;
    public static String FOCUS_MODE_MACRO = Camera.Parameters.FOCUS_MODE_MACRO;
    public static String FOCUS_MODE_EDOF = Camera.Parameters.FOCUS_MODE_EDOF;



    //Flash
    public static String FLASH_MODE_AUTO = Camera.Parameters.FLASH_MODE_AUTO;
    public static String FLASH_MODE_ON = Camera.Parameters.FLASH_MODE_ON;
    public static String FLASH_MODE_OFF = Camera.Parameters.FLASH_MODE_OFF;
    public static String FLASH_MODE_TORCH = Camera.Parameters.FLASH_MODE_TORCH;

    //White Balance
    public static String WHITE_BALANCE_AUTO = Camera.Parameters.WHITE_BALANCE_AUTO;
    public static String WHITE_BALANCE_CLOUDY_DAYLIGHT = Camera.Parameters.WHITE_BALANCE_CLOUDY_DAYLIGHT;
    public static String WHITE_BALANCE_DAYLIGHT = Camera.Parameters.WHITE_BALANCE_DAYLIGHT;
    public static String WHITE_BALANCE_FLUORESCENT = Camera.Parameters.WHITE_BALANCE_FLUORESCENT;
    public static String WHITE_BALANCE_INCANDESCENT = Camera.Parameters.WHITE_BALANCE_INCANDESCENT;

    // Contrast
    public static String KEY_CONTRAST = "contrast";
    public static String KEY_MAX_CONTRAST = "max-contrast";
    public static String KEY_MIN_CONTRAST = "min-contrast";

    // Saturation
    public static String KEY_SATURATION = "saturation";
    public static String KEY_MAX_SATURATION = "max-saturation";
    public static String KEY_MIN_SATURATION = "min-saturation";


    // SHARPNESS
    public static String KEY_SHARPNESS = "sharpness";
    public static String KEY_MAX_SHARPNESS = "max-sharpness";
    public static String KEY_MIN_SHARPNESS = "min-sharpness";


    //BRIGHTNESS
    public static String KEY_MAX_BRIGHTNESS = "max-brightness";
    public static String KEY_MIN_BRIGHTNESS = "min-brightness";
    public static String KEY_BRIGHTNESS_STEP = "brightness-step";
    public static String KEY_BRIGHTNESS = "luma-adaptation";

    //DENOISE
    public static String KEY_DENOISE = "denoise";
    public static String KEY_DENOISE_VALUES = "denoise-values";
    public static String DENOISE_OFF = "denoise-off";
    public static String DENOISE_ON = "denoise-on";

    //AE_BRACKET_HDR
    public static String KEY_AE_BRACKET_HDR = "ae-bracket-hdr";
    public static String KEY_AE_BRACKET_HDR_VALUES = "ae-bracket-hdr-values";
    public static String AE_BRACKET_HDR_OFF = "Off";
    public static String AE_BRACKET_HDR_AE_BRACKET = "AE-Bracket";

    // ISO
    public static String KEY_ISO = "iso";
    public static String KEY_ISO_VALUES = "iso-values";
    public static String ISO_AUTO = "auto";
    public static String ISO_HJR = "ISO_HJR";
    public static String ISO_100 = "ISO100,100";
    public static String ISO_200 = "ISO200,200";
    public static String ISO_400 = "ISO400,400";
    public static String ISO_800 = "ISO800,800";
    public static String ISO_1600 = "ISO1600,1600";



    private CameraWrapper mCamera = null;
    private Handler mHander;
    private int mCameraId;
    private String mFlashMode = "";
    private boolean mEnableFlash;
    private int mZoom;
    private boolean mEnableShutterSound;
    private String mWhiteBalance = "";
    private Size mPictureSize;
    private Size mPreviewSize;
    private String mFocusMode = "";
    private float mExposureFactor;
    private String mSceneMode = "";
    private String mIso = "";
    Map<String,List<String>> mFlatParams;
    private String mDenoise;
    private float mBrightnessFactor;
    private String mAeBracketHdr;
    private float mSharpnessFactor;
    private float mContrastFactor;
    private float mSaturationFactor;


    //private api
    private Map<String,String> mBrightnessKeys;
    private Map<String,String> mSharpnessKeys;
    private Map<String,String> mSaturationKeys;
    private Map<String,String> mContrastKeys;

    //Managers
    private DeviceProfile mDeviceProfile;
    private LocationManager mLocationManager;



    // Listeners$
    private WeakArrayList<CameraParametersListener> mListeners;


    private Camera.Parameters mDefaultParams;
    private boolean mDefaultParamsUsed = false;
    private boolean mUseDefaultParams = false;












    //==============================================================================================
    // Init
    //==============================================================================================

    public CameraParameters()
    {

        init();
    }




    void init()
    {
        mHander = new Handler();
        mDeviceProfile = new DeviceProfile();
        mListeners = new WeakArrayList<>();
        mFlatParams = new HashMap<>();



        mPictureSize = new Size();
        mPreviewSize = new Size();

        mBrightnessKeys = new HashMap<>();
        mBrightnessKeys.put(KEY_PARAM, KEY_BRIGHTNESS);
        mBrightnessKeys.put(KEY_MAX, KEY_MAX_BRIGHTNESS);
        mBrightnessKeys.put(KEY_MIN, KEY_MIN_BRIGHTNESS);
        mBrightnessKeys.put(KEY_STEP, KEY_BRIGHTNESS_STEP);

        mSharpnessKeys = new HashMap<>();
        mSharpnessKeys.put(KEY_PARAM, KEY_SHARPNESS);
        mSharpnessKeys.put(KEY_MAX, KEY_MAX_SHARPNESS);
        mSharpnessKeys.put(KEY_MIN, KEY_MIN_SHARPNESS);

        mSaturationKeys = new HashMap<>();
        mSaturationKeys.put(KEY_PARAM, KEY_SATURATION);
        mSaturationKeys.put(KEY_MAX, KEY_MAX_SATURATION);
        mSaturationKeys.put(KEY_MIN, KEY_MIN_SATURATION);

        mContrastKeys = new HashMap<>();
        mContrastKeys.put(KEY_PARAM, KEY_CONTRAST);
        mContrastKeys.put(KEY_MAX, KEY_MAX_CONTRAST);
        mContrastKeys.put(KEY_MIN, KEY_MIN_CONTRAST);



    }






    //==============================================================================================
    // Interface
    //==============================================================================================




    //----------------------------------------------------------------------------------------------
    // getDeviceProfile
    //----------------------------------------------------------------------------------------------
    public DeviceProfile getDeviceProfile()
    {
        return mDeviceProfile;
    }



    //----------------------------------------------------------------------------------------------
    // release
    //----------------------------------------------------------------------------------------------
    public void release()
    {
        mCamera = null;
    }





    //----------------------------------------------------------------------------------------------
    // getCameraOrientation
    //----------------------------------------------------------------------------------------------
    public int getCameraOrientation()
    {
        if(!isCameraAvailable()) return 0;

        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(mCameraId, cameraInfo);
        return cameraInfo.orientation;
    }



    //----------------------------------------------------------------------------------------------
    // getDisplayOrientation
    //----------------------------------------------------------------------------------------------
    public  int getDisplayOrientation(Context ctx)
    {
        return getDisplayOrientation(ctx, mCameraId);
    }


    //----------------------------------------------------------------------------------------------
    // getDisplayOrientation
    //----------------------------------------------------------------------------------------------
    public static int getDisplayOrientation(Context ctx, int camId)
    {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(camId, cameraInfo);

        WindowManager windowManager =  (WindowManager) ctx.getSystemService(Activity.WINDOW_SERVICE);
        int rotation = windowManager.getDefaultDisplay().getRotation();

        int degree = 0;

        switch (rotation)
        {
            case Surface.ROTATION_0:
                degree = 0;
                break;

            case Surface.ROTATION_90:
                degree = 90;
                break;

            case Surface.ROTATION_180:
                degree = 180;
                break;

            case Surface.ROTATION_270:
                degree = 270;
                break;
        }


        int displayOrientation;


        if(CameraHelper.isFrontCamera(camId))
        {
            displayOrientation = (cameraInfo.orientation + degree) % 360;
            displayOrientation = (360 - displayOrientation) % 360; // compensate the mirror
        }
        else
        {
            displayOrientation = (cameraInfo.orientation - degree + 360) % 360;
        }


        displayOrientation = (360 + displayOrientation) % 360;

        return displayOrientation;


    }

    // ===================




    //===========================================
    // Various
    //===========================================

    //----------------------------------------------------------------------------------------------
    // getPictureFormat
    //----------------------------------------------------------------------------------------------
    public int getPictureFormat()
    {
        int format = ImageFormat.JPEG;
        if(!isCameraAvailable()) return format;
        format = camParams().getPictureFormat();
        return format;
    }




    //===========================================
    // Gps
    //===========================================

    public  void setGpsParameters( Location loc)
    {



        Camera.Parameters parameters = camParams();

        // Clear previous GPS location from the parameters.
        parameters.removeGpsData();
        // We always encode GpsTimeStamp
        parameters.setGpsTimestamp(System.currentTimeMillis() / 1000);
        // Set GPS location.
        if (loc != null)
        {
            double lat = loc.getLatitude();
            double lon = loc.getLongitude();
            boolean hasLatLon = (lat != 0.0d) || (lon != 0.0d);
            if (hasLatLon)
            {
                Log.d(TAG, "Set gps location");
                parameters.setGpsLatitude(lat);
                parameters.setGpsLongitude(lon);
                parameters.setGpsProcessingMethod(loc.getProvider().toUpperCase());
                if (loc.hasAltitude()) {
                    parameters.setGpsAltitude(loc.getAltitude());
                } else {
                    // for NETWORK_PROVIDER location provider, we may have
                    // no altitude information, but the driver needs it, so
                    // we fake one.
                    parameters.setGpsAltitude(0);
                }
                if (loc.getTime() != 0) {
                    // Location.getTime() is UTC in milliseconds.
                    // gps-timestamp is UTC in seconds.
                    long utcTimeSeconds = loc.getTime() / 1000;
                    parameters.setGpsTimestamp(utcTimeSeconds);
                }


                setCamParams(parameters);

            }
            else
            {
                loc = null;
            }
        }
    }



    //===========================================
    // Lock
    //===========================================


    //----------------------------------------------------------------------------------------------
    // lockAeAndWb
    //----------------------------------------------------------------------------------------------
    public void lockAeAndWb()
    {
        if(Build.VERSION.SDK_INT >= 14)
        {
            Camera.Parameters params = camParams();
            if(params.isAutoExposureLockSupported())
            {
                params.setAutoExposureLock(true);
                setCamParams(params);
            }

            params = camParams();
            if(params.isAutoWhiteBalanceLockSupported())
            {
                params.setAutoWhiteBalanceLock(true);
                setCamParams(params);
            }


        }

    }

    //----------------------------------------------------------------------------------------------
    // lockAeAndWb
    //----------------------------------------------------------------------------------------------
    public void unlockAeAndWb()
    {
        if(Build.VERSION.SDK_INT >= 14)
        {
            Camera.Parameters params = camParams();
            if(params.isAutoExposureLockSupported())
            {
                params.setAutoExposureLock(false);
                setCamParams(params);
            }

            params = camParams();
            if(params.isAutoWhiteBalanceLockSupported())
            {
                params.setAutoWhiteBalanceLock(false);
                setCamParams(params);
            }

        }

    }



    //===========================================
    // Camera
    //===========================================



    //----------------------------------------------------------------------------------------------
    // setCamera
    //----------------------------------------------------------------------------------------------

    /**
     * The camera should be open before calling this method
     * @param camera
     */
    public void setCamera(int camId, CameraWrapper camera)
    {
        LogHelper.log(TAG , "setCamera : " + camera);
        mCamera = camera;
        setCameraId(camId);

        loadFlatParams();


    }


    //----------------------------------------------------------------------------------------------
    // isFrontCamera
    //----------------------------------------------------------------------------------------------
    public boolean isFrontCamera()
    {
        return CameraHelper.isFrontCamera(mCameraId);
    }


    //----------------------------------------------------------------------------------------------
    // toggleCameraView
    //----------------------------------------------------------------------------------------------
    public void toggleCameraView()
    {
        int cameraId;
        cameraId = mCameraId;

        cameraId ++;
        int max = Camera.getNumberOfCameras();
        if(max == 2 && cameraId >= max) cameraId = 0;
        else if (max == 1 && cameraId > max) cameraId = 0;
        setCameraId(cameraId);
    }


    //----------------------------------------------------------------------------------------------
    // setCameraId
    //----------------------------------------------------------------------------------------------
    public void setCameraId(int id)
    {
        mCameraId = LangHelper.clamp(id, 0, Camera.getNumberOfCameras());
        mDeviceProfile.queryCamera(mCameraId);
        useDefaultParams(false);
    }

    //----------------------------------------------------------------------------------------------
    // getCameraId
    //----------------------------------------------------------------------------------------------
    public int getCameraId()
    {
        return mCameraId;
    }


    //----------------------------------------------------------------------------------------------
    // getCamera
    //----------------------------------------------------------------------------------------------
    public CameraWrapper getCamera()
    {
        return mCamera;
    }

    //----------------------------------------------------------------------------------------------
    // getRawParams
    //----------------------------------------------------------------------------------------------
    public Camera.Parameters getRawParams()
    {
        return camParams();
    }

    //----------------------------------------------------------------------------------------------
    // canDisableShutterSound
    //----------------------------------------------------------------------------------------------
    public boolean canDisableShutterSound()
    {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(mCameraId, info);
        return info.canDisableShutterSound;
    }













    // ===============================
    // Contrast
    // ===============================


    //----------------------------------------------------------------------------------------------
    // hasContrast
    //----------------------------------------------------------------------------------------------
    public boolean hasContrast()
    {

        if (mFlatParams.containsKey(KEY_CONTRAST) &&
                isMinMaxValid(mContrastKeys))
        {
            return true;
        }

        return false;

    }



    //----------------------------------------------------------------------------------------------
    // setContrast
    //----------------------------------------------------------------------------------------------
    public void setContrast(float factor)
    {
        mContrastFactor = factor;
        applyParamFactor(mContrastKeys,factor);

    }

    //----------------------------------------------------------------------------------------------
    // getContrast
    //----------------------------------------------------------------------------------------------
    public float getContrast()
    {
        float contrast = 0;

        if(isCameraAvailable())
        {
            return readParamFactor(mContrastKeys);
        }

        return contrast;

    }






    // ===============================
    // Saturation
    // ===============================


    //----------------------------------------------------------------------------------------------
    // hasSaturation
    //----------------------------------------------------------------------------------------------
    public boolean hasSaturation()
    {

        if (mFlatParams.containsKey(KEY_SATURATION) &&
                isMinMaxValid(mSaturationKeys))
        {
            return true;
        }

        return false;

    }



    //----------------------------------------------------------------------------------------------
    // setSaturation
    //----------------------------------------------------------------------------------------------
    public void setSaturation(float factor)
    {
        mSaturationFactor = factor;
        applyParamFactor(mSaturationKeys,factor);
    }


    //----------------------------------------------------------------------------------------------
    // getSaturation
    //----------------------------------------------------------------------------------------------
    public float getSaturation()
    {
        float saturation = 0;

        if(isCameraAvailable() && hasSaturation())
        {
            return readParamFactor(mSaturationKeys);
        }

        return saturation;

    }







    // ===============================
    // Sharpness
    // ===============================


    //----------------------------------------------------------------------------------------------
    // hasSharpness
    //----------------------------------------------------------------------------------------------
    public boolean hasSharpness()
    {

        if (mFlatParams.containsKey(KEY_SHARPNESS) &&
                isMinMaxValid(mSharpnessKeys))
        {
            return true;
        }

        return false;

    }



    //----------------------------------------------------------------------------------------------
    // setSharpness
    //----------------------------------------------------------------------------------------------
    public void setSharpness(float factor)
    {
        mSharpnessFactor = factor;
        applyParamFactor(mSharpnessKeys,factor);
    }

    //----------------------------------------------------------------------------------------------
    // getSharpness
    //----------------------------------------------------------------------------------------------
    public float getSharpness()
    {
        float value = 0;

        if(isCameraAvailable() && hasSharpness())
        {
            return readParamFactor(mSharpnessKeys);
        }

        return value;

    }






    // =========================
    // Brightness
    // =========================

    //----------------------------------------------------------------------------------------------
    // hasBrightness
    //----------------------------------------------------------------------------------------------
    public boolean hasBrightness()
    {

        if (mFlatParams.containsKey(KEY_BRIGHTNESS) &&
                isMinMaxValid(mBrightnessKeys))
        {
            return true;
        }

        return false;

    }




    //----------------------------------------------------------------------------------------------
    // setBrightness
    //----------------------------------------------------------------------------------------------
    public void setBrightness(float factor)
    {
        mBrightnessFactor = factor;
        applyParamFactor(mBrightnessKeys,factor);

    }

    //----------------------------------------------------------------------------------------------
    // getBrightness
    //----------------------------------------------------------------------------------------------
    public float getBrightness()
    {
        float value = 0;

        if(isCameraAvailable() && hasBrightness())
        {
            return readParamFactor(mBrightnessKeys);
        }

        return value;

    }



    // =========================
    // AE Bracket hdr
    // =========================

    //----------------------------------------------------------------------------------------------
    // hasAeBracketHdrValue
    //----------------------------------------------------------------------------------------------
    public boolean hasAeBracketHdrValue(String value)
    {
        return hasParamValue(KEY_AE_BRACKET_HDR, KEY_AE_BRACKET_HDR_VALUES, value);
    }


    //----------------------------------------------------------------------------------------------
    // setAeBracketHdr
    //----------------------------------------------------------------------------------------------
    public void setAeBracketHdr(String value)
    {
        if(!hasAeBracketHdrValue(value)) return;
        mAeBracketHdr = value;
        applyParam(KEY_AE_BRACKET_HDR, KEY_AE_BRACKET_HDR_VALUES, value);
    }


    //----------------------------------------------------------------------------------------------
    // getAeBracketHdr
    //----------------------------------------------------------------------------------------------
    public String getAeBracketHdr()
    {

        String value = "";

        if(isCameraAvailable() && hasAeBracketHdr())
        {
            return mFlatParams.get(KEY_AE_BRACKET_HDR).get(0);
        }

        return value;
    }


    //----------------------------------------------------------------------------------------------
    // hasAeBracketHdr
    //----------------------------------------------------------------------------------------------
    public boolean hasAeBracketHdr()
    {
        return hasParam(KEY_AE_BRACKET_HDR, KEY_AE_BRACKET_HDR_VALUES);
    }


    // =========================
    // Denoise
    // =========================

    //----------------------------------------------------------------------------------------------
    // hasDenoiseValue
    //----------------------------------------------------------------------------------------------
    public boolean hasDenoiseValue(String value)
    {
        return hasParamValue(KEY_DENOISE, KEY_DENOISE_VALUES, value);
    }


    //----------------------------------------------------------------------------------------------
    // setDenoise
    //----------------------------------------------------------------------------------------------
    public void setDenoise(String value)
    {
        mDenoise = value;
        applyParam(KEY_DENOISE, KEY_DENOISE_VALUES, value);
    }


    //----------------------------------------------------------------------------------------------
    // getDenoise
    //----------------------------------------------------------------------------------------------
    public String getDenoise()
    {
        String value = "";

        if(isCameraAvailable() && hasDenoise())
        {
            return mFlatParams.get(KEY_DENOISE).get(0);
        }

        return value;
    }


    //----------------------------------------------------------------------------------------------
    // hasDenoise
    //----------------------------------------------------------------------------------------------
    public boolean hasDenoise()
    {
        return hasParam(KEY_DENOISE, KEY_DENOISE_VALUES);

    }





    // =========================
    // Iso
    // =========================

    //----------------------------------------------------------------------------------------------
    // hasDenoiseValue
    //----------------------------------------------------------------------------------------------
    public boolean hasIsoValue(String value)
    {
        return hasParamValue(KEY_ISO, KEY_ISO_VALUES, value);
    }


    //----------------------------------------------------------------------------------------------
    // setIso
    //----------------------------------------------------------------------------------------------
    public void setIso(String value)
    {
        mIso = value;
        applyParam(KEY_ISO, KEY_ISO_VALUES, value);
    }


    //----------------------------------------------------------------------------------------------
    // getIso
    //----------------------------------------------------------------------------------------------
    public String getIso()
    {
        String value = "";

        if(isCameraAvailable() && hasIso())
        {
            return mFlatParams.get(KEY_ISO).get(0);
        }

        return value;
    }


    //----------------------------------------------------------------------------------------------
    // hasIso
    //----------------------------------------------------------------------------------------------
    public boolean hasIso()
    {
        return hasParam(KEY_ISO, KEY_ISO_VALUES);
    }







    //==================================
    // Preview Size
    //==================================



    //----------------------------------------------------------------------------------------------
    // hasPreviewSizes
    //----------------------------------------------------------------------------------------------
    public boolean hasPreviewSize(Size size)
    {
        if(!isCameraAvailable()) return false;
        if(!hasPreviewSizes() || size ==null) return false;

        List<Camera.Size> sizes = camParams().getSupportedPreviewSizes();

        for(Camera.Size _size: sizes)
        {
            if(size.width == _size.width && size.height == _size.height) return true;
        }

        return false;
    }


    //----------------------------------------------------------------------------------------------
    // hasPreviewSizes
    //----------------------------------------------------------------------------------------------
    public boolean hasPreviewSizes()
    {
        if(!isCameraAvailable()) return false;

        List<Camera.Size> sizes = camParams().getSupportedPreviewSizes();

        if(sizes != null && sizes.size()>0)
        {
            return true;
        }
        else return false;

    }


    //----------------------------------------------------------------------------------------------
    // selectPreviewSize
    //----------------------------------------------------------------------------------------------
    public void selectPreviewSize(final Size size)
    {
        mPreviewSize = size;
        applyPreviewSize(size);
    }


    // --------------------------------------------------------------------------------------
    // getPreviewSize
    //----------------------------------------------------------------------------------------------
    public Size getPreviewSize()
    {
        Size value = new Size();

        if(!isCameraAvailable() || !hasPreviewSizes()) return value;

        Camera.Size camSize = camParams().getPreviewSize();
        value.width = camSize.width;
        value.height = camSize.height;


        return value;
    }

    //----------------------------------------------------------------------------------------------
    // getOptimalPreviewSize
    //----------------------------------------------------------------------------------------------
    public Size getOptimalPreviewSize(Size viewSize, boolean swapSize)
    {
        Size optimalSize = new Size();
        if(!isCameraAvailable() || !hasPreviewSizes()) return optimalSize;


        List<Camera.Size> sizes = camParams().getSupportedPreviewSizes();


        Camera.Size resultSize = getOptimalPreviewSize(sizes, viewSize,  swapSize);

        optimalSize.from(resultSize);
        return optimalSize;
    }


    //----------------------------------------------------------------------------------------------
    // getOptimalPreviewSize
    //----------------------------------------------------------------------------------------------
    private  Camera.Size getOptimalPreviewSize( List<Camera.Size> sizes,Size viewSize,  boolean swapSize)
    {


        // Use a very small tolerance because we want an exact match.
        final double ASPECT_TOLERANCE = 0.1;//0.01
        if (sizes == null) return null;
        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int w,h;

        double targetRatio ;
        if(swapSize )
        {
            w = viewSize.height;
            h = viewSize.width;
        }
        else
        {
            w = viewSize.width;
            h = viewSize.height;
        }

        targetRatio = (double) viewSize.width / viewSize.height;
        int targetHeight = h;










        if (optimalSize == null)
        {
            LogHelper.log("Try optimal size 1");

            for (Camera.Size size : sizes)
            {
                double ratio = (double) size.width / size.height;
                if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
                if (Math.abs(size.height - targetHeight) < minDiff)
                {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }


        // Cannot find the one match the aspect ratio. This should not happen.
        // Ignore the requirement.
        if (optimalSize == null)
        {
            LogHelper.log("Try optimal size 2");
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes)
            {
                if (Math.abs(size.height - targetHeight) < minDiff)
                {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }



    //----------------------------------------------------------------------------------------------
    // getMaxPreviewSize
    //----------------------------------------------------------------------------------------------
    public Size getMaxPreviewSize()
    {
        Size max = new Size();
        if(!isCameraAvailable() || !hasPreviewSizes()) return max;


        List<Size> sizes = getSortedPreviewSizes();

        if(sizes.size()>0)
        {
            max = sizes.get(0);
        }

        return max;
    }


    //----------------------------------------------------------------------------------------------
    // getSortedPreviewSizes
    //----------------------------------------------------------------------------------------------
    public  List<Size> getSortedPreviewSizes()
    {
        List<Camera.Size> sizes ;
        List<Size> sortedSizes = new ArrayList<>();

        if(!isCameraAvailable() || !hasPreviewSizes()) return sortedSizes;

        int max = 0;
        int index = 0;

        sizes = camParams().getSupportedPreviewSizes();
        for(Camera.Size size: sizes) sortedSizes.add(new Size(size.width, size.height));

        Collections.sort(sortedSizes, new SizeComparator());

        return sortedSizes;

    }














    // ===============================
    // Scene Mode
    // ===============================


    //----------------------------------------------------------------------------------------------
    // setSceneMode
    //----------------------------------------------------------------------------------------------
    public void setSceneMode(String mode)
    {
        mSceneMode = mode;
        applyScene(mode);
    }


    //----------------------------------------------------------------------------------------------
    // getSceneMode
    //----------------------------------------------------------------------------------------------
    public String getSceneMode()
    {
        String value = "";

        if(!isCameraAvailable() || !hasScene()) return value;

        value = camParams().getSceneMode();
        return value;
    }


    //----------------------------------------------------------------------------------------------
    // hasScene
    //----------------------------------------------------------------------------------------------
    public boolean hasScene()
    {
        if(!isCameraAvailable()) return false;
        List<String> sceneModes = camParams().getSupportedSceneModes();

        if(sceneModes != null && sceneModes.size()>1)
        {
            return true;
        }
        else return false;

    }

    //----------------------------------------------------------------------------------------------
    // hasSteadyPhotoScene
    //----------------------------------------------------------------------------------------------
    public boolean hasSteadyPhotoScene()
    {
        if(hasScene())
        {
            List<String> sceneModes = camParams().getSupportedSceneModes();
            if(sceneModes.contains(Camera.Parameters.SCENE_MODE_STEADYPHOTO))
            {
                return true;
            }
        }

        return false;


    }

    //----------------------------------------------------------------------------------------------
    // hasSceneMode
    //----------------------------------------------------------------------------------------------
    public boolean hasSceneMode(String mode)
    {
        if(!hasScene()) return false;

        List<String> sceneModes = camParams().getSupportedSceneModes();

        return sceneModes.contains(mode);

    }



    //================================
    // Exposure
    //================================

    //----------------------------------------------------------------------------------------------
    // hasExposureCompensation
    //----------------------------------------------------------------------------------------------
    public boolean hasExposure()
    {
        if(!isCameraAvailable()) return false;

        int maxEC =  camParams().getMaxExposureCompensation();
        int minEC =  camParams().getMinExposureCompensation();

        if(maxEC != minEC)
        {
            return true;
        }
        else
        {
            return false;
        }

    }




    //----------------------------------------------------------------------------------------------
    // setExposure
    //----------------------------------------------------------------------------------------------
    public void setExposure(float factor)
    {
        mExposureFactor = factor;
        applyExposure(factor);

    }


    //----------------------------------------------------------------------------------------------
    // getExposure
    //----------------------------------------------------------------------------------------------
    public float getExposure()
    {
        float factor = 0;

        if(!isCameraAvailable() || !hasExposure()) return factor;

        factor = convertValueToFactor(camParams().getExposureCompensation(),
                camParams().getMaxExposureCompensation(),
                camParams().getMinExposureCompensation());

        return factor;

    }



    //================================
    // Metring Areas
    //================================


    //----------------------------------------------------------------------------------------------
    // hasMetringArea
    //----------------------------------------------------------------------------------------------
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public boolean hasMetringAreas()
    {
        if(Build.VERSION.SDK_INT < 14) return false;
        if(!isCameraAvailable()) return false;

        int max = camParams().getMaxNumMeteringAreas();

        if(max > 0)
        {
            return true;
        }
        else return false;
    }

    //----------------------------------------------------------------------------------------------
    // setMetringAreas
    //----------------------------------------------------------------------------------------------
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void setMetringAreas(List<Camera.Area> areas)
    {
        if(!hasMetringAreas()) return ;

        Camera.Parameters params = camParams();
        int max = params.getMaxNumMeteringAreas();
        if(areas != null)
        {
        }
        List<Camera.Area> okAreas = areas;
        if(areas != null && areas.size() > max)
        {
            okAreas = areas.subList(0, max);
        }



        params.setMeteringAreas(okAreas);
        setCamParams(params);


    }


    //================================
    // Focus Areas
    //================================

    //----------------------------------------------------------------------------------------------
    // setFocusMode
    //----------------------------------------------------------------------------------------------
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public boolean hasFocusAreas()
    {
        if(Build.VERSION.SDK_INT < 14) return false;
        if(!hasFocus()) return false;

        int maxFocusAreas = camParams().getMaxNumFocusAreas();

        if(maxFocusAreas > 0)
        {
            return true;
        }
        else return false;
    }

    //----------------------------------------------------------------------------------------------
    // setFocusAreas
    //----------------------------------------------------------------------------------------------
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void setFocusAreas(List<Camera.Area> areas)
    {
        if(!hasFocusAreas()) return ;

        Camera.Parameters params = camParams();
        int max = params.getMaxNumFocusAreas();


        List<Camera.Area> okAreas = areas;
        if(areas != null && areas.size() > max)
        {
            okAreas = areas.subList(0, max);
        }


        params.setFocusAreas(okAreas);
        setCamParams(params);


    }





    //================================
    // Focus mode
    //================================


    //----------------------------------------------------------------------------------------------
    // needsAutoFocusCapture
    //----------------------------------------------------------------------------------------------
    public boolean needsAutoFocusCapture()
    {
        if (hasFocus() &&
                (getFocusMode().equals(CameraParameters.FOCUS_MODE_AUTO) ||
                        getFocusMode().equals(CameraParameters.FOCUS_MODE_CONTINUOUS_PICTURE) ||
                        getFocusMode().equals(CameraParameters.FOCUS_MODE_MACRO)  )
                )
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    //----------------------------------------------------------------------------------------------
    // setFocusMode
    //----------------------------------------------------------------------------------------------
    public void setFocusMode(String mode)
    {
        mFocusMode = mode;
        applyFocus(mode);
    }


    //----------------------------------------------------------------------------------------------
    // getFocusMode
    //----------------------------------------------------------------------------------------------
    public String getFocusMode()
    {
        String mode = "";

        if(!isCameraAvailable() || !hasFocus()) return mode;

        mode = camParams().getFocusMode();

        return mode;
    }


    //----------------------------------------------------------------------------------------------
    // hasFocusMode
    //----------------------------------------------------------------------------------------------
    public boolean hasFocusMode(String mode)
    {
        if(!hasFocus()) return false;

        List<String> focusModes = camParams().getSupportedFocusModes();

        return focusModes.contains(mode);

    }

    //----------------------------------------------------------------------------------------------
    // hasFocus
    //----------------------------------------------------------------------------------------------
    public boolean hasFocus()
    {
        if(!isCameraAvailable()) return false;

        List<String> focusModes = camParams().getSupportedFocusModes();

        if(focusModes != null && focusModes.size()>1)
        {
            return true;
        }
        else return false;

    }



    //===========================================
    // Picture Size
    //===========================================

    //----------------------------------------------------------------------------------------------
    // hasPictureSize
    //----------------------------------------------------------------------------------------------
    public boolean hasPictureSize(Size size)
    {
        if(!isCameraAvailable()) return false;
        if(!hasPictureSizes() || size == null) return false;

        List<Camera.Size> sizes = camParams().getSupportedPictureSizes();

        for(Camera.Size _size: sizes)
        {
            if(size.width == _size.width && size.height == _size.height) return true;
        }

        return false;
    }

    //----------------------------------------------------------------------------------------------
    // hasPictureSizes
    //----------------------------------------------------------------------------------------------
    public boolean hasPictureSizes()
    {
        if(!isCameraAvailable()) return false;
        List<Camera.Size> sizes = camParams().getSupportedPictureSizes();

        if(sizes != null && sizes.size()>0)
        {
            return true;
        }
        else return false;

    }


    //----------------------------------------------------------------------------------------------
    // getMaxPreviewSize
    //----------------------------------------------------------------------------------------------
    public Size getMaxPictureSize()
    {
        Size max = new Size();
        if(!isCameraAvailable() || !hasPictureSizes()) return max;


        List<Size> sizes = getSortedPictureSizes();

        if(sizes.size()>0)
        {
            max = sizes.get(0);
        }

        return max;
    }

    //----------------------------------------------------------------------------------------------
    // getSupportedPictureSizes
    //----------------------------------------------------------------------------------------------
    public  List<Size> getSortedPictureSizes()
    {
        List<Camera.Size> sizes ;
        List<Size> sortedSizes = new ArrayList<>();
        List<String> excludedSizes = new ArrayList<>();

        if(!hasPictureSizes()) return sortedSizes;

        sizes = camParams().getSupportedPictureSizes();
        if(mDeviceProfile.hasConstraints()) excludedSizes = mDeviceProfile.getExcludedPictureSizes();

        for(Camera.Size size: sizes)
        {
            Size newSize = new Size(size.width, size.height);
            if(!excludedSizes.contains(newSize.toString()))
            {
                sortedSizes.add(newSize);
            }
        }

        Collections.sort(sortedSizes, new SizeComparator());


        return sortedSizes;

    }

    //----------------------------------------------------------------------------------------------
    // selectPictureSize
    //----------------------------------------------------------------------------------------------
    public void selectPictureSize(final Size size)
    {
        mPictureSize = size;
        applyPictureSize(size);
    }


    //----------------------------------------------------------------------------------------------
    // getPictureSize
    //----------------------------------------------------------------------------------------------
    public Size getPictureSize()
    {
        Size size = new Size();

        if(!isCameraAvailable() || !hasPictureSizes()) return size;

        size.from(camParams().getPictureSize());

        return size;
    }




    //----------------------------------------------------------------------------------------------
    // getOptimalPictureSize
    //----------------------------------------------------------------------------------------------
    public  Size getOptimalPictureSize( )
    {
        Size optimalSize = new Size();
        if(!isCameraAvailable() || !hasPreviewSizes()) return optimalSize;


        List<Camera.Size> sizes = camParams().getSupportedPictureSizes();
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(mCameraId, cameraInfo);

        Size resultSize = getOptimalPictureSize(sizes);


        return resultSize;
    }

    //----------------------------------------------------------------------------------------------
    // getOptimalPictureSize
    //----------------------------------------------------------------------------------------------
    public  Size getOptimalPictureSize( List<Camera.Size> sizes)
    {


        if (sizes == null) return null;
        Size optimalSize = null;

        optimalSize = getMaxPictureSize();


        return optimalSize;
    }



    //===========================================
    // White Balance Mode
    //===========================================


    //----------------------------------------------------------------------------------------------
    // hasWhiteBalanceMode
    //----------------------------------------------------------------------------------------------
    public boolean hasWhiteBalanceMode(String mode)
    {
        if(!hasWhiteBalance()) return false;

        List<String> wbList = camParams().getSupportedWhiteBalance();
        return wbList.contains(mode);

    }


    //----------------------------------------------------------------------------------------------
    // hasWhiteBalance
    //----------------------------------------------------------------------------------------------
    public boolean hasWhiteBalance()
    {
        if(!isCameraAvailable()) return false;

        List<String> wbList = camParams().getSupportedWhiteBalance();

        if(wbList != null && wbList.size()>1)
        {
            return true;
        }
        else return false;

    }

    //----------------------------------------------------------------------------------------------
    // setWhiteBalance
    //----------------------------------------------------------------------------------------------
    public void setWhiteBalance(final String wb)
    {
        mWhiteBalance = wb;
        applyWhiteBalance(mWhiteBalance);
    }



    //----------------------------------------------------------------------------------------------
    // getWhiteBalance
    //----------------------------------------------------------------------------------------------
    public String getWhiteBalance()
    {
        String mode = "";

        if(!isCameraAvailable() || !hasWhiteBalance()) return mode;

        mode = camParams().getWhiteBalance();

        return mode;
    }




    //===========================================
    // Shutter
    //===========================================


    //----------------------------------------------------------------------------------------------
    // enableShutterSound
    //----------------------------------------------------------------------------------------------
    public void enableShutterSound(final boolean option)
    {
        mEnableShutterSound = option;
        applyShutterSound(option);
    }




    //===========================================
    // Zoom
    //===========================================

    //----------------------------------------------------------------------------------------------
    // hasZoom
    //----------------------------------------------------------------------------------------------
    public boolean hasZoom()
    {
        if(!isCameraAvailable()) return false;

        return camParams().isZoomSupported();
    }


    //----------------------------------------------------------------------------------------------
    // setZoom (1 - 100)
    //----------------------------------------------------------------------------------------------
    public void setZoom(int zoom)
    {
        try
        {
            mZoom = LangHelper.clamp(zoom, 0, 100);
            applyZoom(mZoom);
        }
        catch(Exception ex)
        {
            LogHelper.logx(ex);
        }
    }




    // ===============================
    // Flash
    // ===============================


    //----------------------------------------------------------------------------------------------
    // hasFlash
    //----------------------------------------------------------------------------------------------
    public boolean hasFlashMode(String mode)
    {
        if(!hasFlash()) return false;

        List<String> flashModes = camParams().getSupportedFlashModes();

        if(flashModes.contains(mode))
        {
            return true;
        }
        else return false;

    }

    //----------------------------------------------------------------------------------------------
    // hasFlash
    //----------------------------------------------------------------------------------------------
    public boolean hasFlash()
    {
        if(!isCameraAvailable()) return false;

        List<String> flashModes = camParams().getSupportedFlashModes();

        if(flashModes != null && flashModes.size()>1)
        {
            return true;
        }
        else return false;

    }


    //----------------------------------------------------------------------------------------------
    // setFlashMode
    //----------------------------------------------------------------------------------------------
    public void setFlashMode(final String mode)
    {
        mFlashMode = mode;
        applyFlashMode(mode);

    }


    //----------------------------------------------------------------------------------------------
    // getFlashMode
    //----------------------------------------------------------------------------------------------
    public String getFlashMode()
    {
        String mode = "";

        if(!isCameraAvailable() || !hasFlash()) return mode;

        mode = camParams().getFlashMode();

        return mode;
    }






    // ===============================
    // Params
    // ===============================



    public void restoreDefaultParams()
    {
        setCamParams(mDefaultParams);
    }

    public boolean isDefaultParamsUsed()
    {
        return mDefaultParamsUsed;
    }




    public boolean isUseDefaultParams()
    {
        return mUseDefaultParams;
    }

    public void useDefaultParams(boolean option)
    {
        mUseDefaultParams = option;

        if(mUseDefaultParams && mCamera.isOpen() && mDefaultParams!=null)
        {
            setCamParams(mDefaultParams);
        }
    }


    // ===============================
    // Camera Preparation
    // ===============================



    //----------------------------------------------------------------------------------------------
    // setupCamera
    //----------------------------------------------------------------------------------------------
    public void setupCamera()
    {
        Camera.Parameters params;

        mDefaultParams = camParams();



        // init



        if(hasPictureSizes()) {applyPictureSize(mPictureSize);}
        if(hasPreviewSizes()) {applyPreviewSize(mPreviewSize);}
        if(hasScene() && !mSceneMode.equals(getSceneMode())) {applyScene(mSceneMode);}
        if(hasFlash() && !mFlashMode.equals(getFlashMode())) {applyFlashMode(mFlashMode);}
        applyShutterSound(mEnableShutterSound);
        if(hasWhiteBalance() && !mWhiteBalance.equals(getWhiteBalance())) {applyWhiteBalance(mWhiteBalance);}

        if(hasFocus() && !mFocusMode.equals(getFlashMode())) {applyFocus(mFocusMode);}
        if(hasExposure() && mExposureFactor != getExposure())
        {
            applyExposure(mExposureFactor);
        }








        // Avoid NV16 format
        params  = camParams();
        List<Integer> supportedFormats = params.getSupportedPictureFormats();
        if(supportedFormats.contains(ImageFormat.JPEG))
        {
            params.setPictureFormat(ImageFormat.JPEG);
            mCamera.setParameters(params);
        }
        else if(supportedFormats.contains(ImageFormat.NV21))
        {
            params.setPictureFormat(ImageFormat.NV21);
            mCamera.setParameters(params);
        }


        /*for(int format : params.getSupportedPictureFormats())
        {
            LogHelper.log("pic format : " + strPicFormat(format));
        }*/


        setupImageQuality();





    }




    //----------------------------------------------------------------------------------------------
    // loadAppSettings
    //----------------------------------------------------------------------------------------------

    /**
     * camera should be opened before calling this method
     */
    public void loadAppSettings()
    {
        LogHelper.log("loadAppSettings");
        LogHelper.log(TAG , "loadAppSettings camera  : " + mCamera);
        mZoom = 0;


        if (hasPictureSizes())
        {
            if(!App.getCameraPrefHelper().isCamPictureSizeSet(mCameraId))
            {
                App.getCameraPrefHelper().loadDefaultPictureSizePref(this);

            }
            mPictureSize = App.getCameraPrefHelper().getXCamPictureSize(mCameraId);
        }


        if (hasPreviewSizes())
        {
            if(!App.getAdvancedCameraPrefHelper().isPreviewSizeSet(mCameraId))
            {
                App.getAdvancedCameraPrefHelper().loadDefaultPreviewSizePref(this);
            }
            mPreviewSize = App.getAdvancedCameraPrefHelper().getPreviewSize(mCameraId);


        }



        if(hasFocus())
        {

            if(!App.getCameraPrefHelper().isFocusModeSet(mCameraId))
            {
                App.getCameraPrefHelper().loadDefaultFocusModePref(this);

            }

            String focusModePref = App.getCameraPrefHelper().getFocusMode(mCameraId);
            if(focusModePref.equals(FocusModeValues.INIFINITY))
            {
                mFocusMode = Camera.Parameters.FOCUS_MODE_INFINITY;
            }
            else if(focusModePref.equals(FocusModeValues.MACRO))
            {
                mFocusMode = Camera.Parameters.FOCUS_MODE_MACRO;
            }
            else if(focusModePref.equals(FocusModeValues.FIXED))
            {
                mFocusMode = Camera.Parameters.FOCUS_MODE_FIXED;
            }
            else if(focusModePref.equals(FocusModeValues.AUTO))
            {
                mFocusMode = Camera.Parameters.FOCUS_MODE_AUTO;
            }
            else if(focusModePref.equals(FocusModeValues.CONTINUOUS))
            {
                mFocusMode = Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE;
            }
            else if(focusModePref.equals(FocusModeValues.EDOF))
            {
                mFocusMode = Camera.Parameters.FOCUS_MODE_EDOF;
            }
            else
            {
                mFocusMode = camParams().getSupportedFocusModes().get(0);
            }


        }


        if(hasScene())
        {
            if(!App.getCameraPrefHelper().isSceneModeSet(mCameraId)) App.getCameraPrefHelper().loadDefaultSceneModePref(this);

            String sceneModePref = App.getCameraPrefHelper().getSceneMode(mCameraId);
            if(sceneModePref.equals(SceneModeValues.AUTO)) mSceneMode = Camera.Parameters.SCENE_MODE_AUTO;
            else if(sceneModePref.equals(SceneModeValues.ACTION)) mSceneMode = Camera.Parameters.SCENE_MODE_ACTION;
            else if(sceneModePref.equals(SceneModeValues.BARCODE)) mSceneMode = Camera.Parameters.SCENE_MODE_BARCODE;
            else if(sceneModePref.equals(SceneModeValues.BEACH)) mSceneMode = Camera.Parameters.SCENE_MODE_BEACH;
            else if(sceneModePref.equals(SceneModeValues.CANDLE_LIGHT)) mSceneMode = Camera.Parameters.SCENE_MODE_CANDLELIGHT;
            else if(sceneModePref.equals(SceneModeValues.FIREWORKS)) mSceneMode = Camera.Parameters.SCENE_MODE_FIREWORKS;
            else if(sceneModePref.equals(SceneModeValues.HDR)) mSceneMode = Camera.Parameters.SCENE_MODE_HDR;
            else if(sceneModePref.equals(SceneModeValues.LANDSCAPE)) mSceneMode = Camera.Parameters.SCENE_MODE_LANDSCAPE;
            else if(sceneModePref.equals(SceneModeValues.NIGHT)) mSceneMode = Camera.Parameters.SCENE_MODE_NIGHT;
            else if(sceneModePref.equals(SceneModeValues.PARTY)) mSceneMode = Camera.Parameters.SCENE_MODE_PARTY;
            else if(sceneModePref.equals(SceneModeValues.PORTRAIT)) mSceneMode = Camera.Parameters.SCENE_MODE_PORTRAIT;
            else if(sceneModePref.equals(SceneModeValues.SNOW)) mSceneMode = Camera.Parameters.SCENE_MODE_SNOW;
            else if(sceneModePref.equals(SceneModeValues.SPORTS)) mSceneMode = Camera.Parameters.SCENE_MODE_SPORTS;
            else if(sceneModePref.equals(SceneModeValues.STEADY_PHOTO)) mSceneMode = Camera.Parameters.SCENE_MODE_STEADYPHOTO;
            else if(sceneModePref.equals(SceneModeValues.SUNSET)) mSceneMode = Camera.Parameters.SCENE_MODE_SUNSET;
            else if(sceneModePref.equals(SceneModeValues.THEATRE)) mSceneMode = Camera.Parameters.SCENE_MODE_THEATRE;
        }



        mEnableShutterSound = App.getCameraPrefHelper().getEnableShutterSound();



        //-----
        if(hasFlash())
        {
            String flashModePref = App.getCameraPrefHelper().getFlashMode();
            if(flashModePref.equals(FlashModeValues.OFF))
            {
                mFlashMode = Camera.Parameters.FLASH_MODE_OFF;
            }
            else if(flashModePref.equals(FlashModeValues.ON))
            {
                mFlashMode = Camera.Parameters.FLASH_MODE_ON;
            }
            else if(flashModePref.equals(FlashModeValues.AUTO))
            {
                mFlashMode = Camera.Parameters.FLASH_MODE_AUTO;
            }
            else if(flashModePref.equals(FlashModeValues.TORCH))
            {
                mFlashMode = Camera.Parameters.FLASH_MODE_TORCH;
            }
        }


        //-----

        if (hasWhiteBalance())
        {
            String whiteBalancePref = App.getCameraPrefHelper().getWhiteBalance();
            if(whiteBalancePref.equals(WhiteBalanceValues.AUTO))
            {
                mWhiteBalance = Camera.Parameters.WHITE_BALANCE_AUTO;
            }
            else if(whiteBalancePref.equals(WhiteBalanceValues.CLOUDY))
            {
                mWhiteBalance = Camera.Parameters.WHITE_BALANCE_CLOUDY_DAYLIGHT;
            }
            else if(whiteBalancePref.equals(WhiteBalanceValues.DAYLIGHT))
            {
                mWhiteBalance = Camera.Parameters.WHITE_BALANCE_DAYLIGHT;
            }
            else if(whiteBalancePref.equals(WhiteBalanceValues.FLUORESCENT))
            {
                mWhiteBalance = Camera.Parameters.WHITE_BALANCE_FLUORESCENT;
            }
            else if(whiteBalancePref.equals(WhiteBalanceValues.INCANDESCENT))
            {
                mWhiteBalance = Camera.Parameters.WHITE_BALANCE_INCANDESCENT;
            }
        }



        //------




        //-----

        if(hasExposure())
        {
            mExposureFactor = App.getCameraPrefHelper().getExposure(mCameraId);
        }









    }

    //==============================================================================================
    // Internals
    //==============================================================================================




    //----------------------------------------------------------------------------------------------
    // setupImageQuality
    //----------------------------------------------------------------------------------------------
    public void setupImageQuality() {
        LogHelper.log("setupImageQuality");




        Camera.Parameters params;

        // Jpeg quality
        params = camParams();

        params.setJpegQuality(100);
        setCamParams(params);




    }





    //----------------------------------------------------------------------------------------------
    // applyFlashMode
    //----------------------------------------------------------------------------------------------
    private void applyFlashMode(String mode)
    {
        if(!isCameraAvailable() || !hasFlashMode(mode)) return;

        Camera.Parameters params = camParams();
        params.setFlashMode(mode);
        setCamParams(params);
    }


    //----------------------------------------------------------------------------------------------
    // applyWhiteBalance
    //----------------------------------------------------------------------------------------------
    private void applyWhiteBalance(String mode)
    {
        if(!isCameraAvailable() || !hasWhiteBalanceMode(mode)) return;

        Camera.Parameters params = camParams();
        params.setWhiteBalance(mode);
        setCamParams(params);
    }


    //----------------------------------------------------------------------------------------------
    // applyPictureSize
    //----------------------------------------------------------------------------------------------
    public void applyPictureSize(Size size)
    {

        if(!isCameraAvailable() || !hasPictureSize(size)) return;

        Camera.Parameters params = camParams();
        params.setPictureSize(size.width, size.height);
        setCamParams(params);


    }


    //----------------------------------------------------------------------------------------------
    // applyPreviewSize
    //----------------------------------------------------------------------------------------------
    public void applyPreviewSize(Size size)
    {

        if(!isCameraAvailable() || !hasPreviewSize(size)) return;

        Camera.Parameters params = camParams();
        params.setPreviewSize(size.width, size.height);
        setCamParams(params);


    }


    //----------------------------------------------------------------------------------------------
    // applyShutterSound
    //----------------------------------------------------------------------------------------------
    public void applyShutterSound(boolean option)
    {
        if(!isCameraAvailable()) return;
        if(Build.VERSION.SDK_INT >= 17)
        {
            mCamera.enableShutterSound(option);
        }

    }


    //----------------------------------------------------------------------------------------------
    // applyZoom
    //----------------------------------------------------------------------------------------------
    public void applyZoom(int zoom)
    {
        if(!isCameraAvailable() || !hasZoom()) return;

        Camera.Parameters params = camParams();
        float step = params.getMaxZoom()/100f;
        int newZoom = (int) (step * zoom);
        newZoom = LangHelper.clamp(newZoom, 0, params.getMaxZoom());


        params.setZoom(newZoom);
        setCamParams(params);
    }


    //----------------------------------------------------------------------------------------------
    // applyFocus
    //----------------------------------------------------------------------------------------------
    public void applyFocus(String mode)
    {
        if(!isCameraAvailable() || !hasFocusMode(mode)) return;

        Camera.Parameters params = camParams();
        params.setFocusMode(mode);
        setCamParams(params);
    }



    //----------------------------------------------------------------------------------------------
    // applyExposure
    //----------------------------------------------------------------------------------------------
    public void applyExposure(float factor)
    {
        if(!isCameraAvailable() || !hasExposure()) return;

        Camera.Parameters params = camParams();
        int exposure = 0;

        int maxEC =  camParams().getMaxExposureCompensation();
        int minEC =  camParams().getMinExposureCompensation();


        factor = LangHelper.clamp(factor, -1f, 1f);

        if (factor > 0)
        {
            exposure = Math.round(maxEC * factor);

        }
        else if (factor < 0)
        {
            exposure = Math.round(minEC * Math.abs(factor));
        }
        else if(factor == 0)
        {
            exposure = 0;
        }


        params.setExposureCompensation(exposure);


        setCamParams(params);
    }



    //----------------------------------------------------------------------------------------------
    // applyScene
    //----------------------------------------------------------------------------------------------
    public void applyScene(String mode)
    {
        if(!isCameraAvailable() || !hasSceneMode(mode)) return;

        Camera.Parameters params = camParams();
        params.setSceneMode(mode);
        setCamParams(params);
    }























    //=========================
    // Helpers
    //=========================

    //----------------------------------------------------------------------------------------------
    // getCamParams
    //----------------------------------------------------------------------------------------------
    private Camera.Parameters camParams()
    {
        return mCamera.getParameters();
    }


    //----------------------------------------------------------------------------------------------
    // isCameraAvailable
    //----------------------------------------------------------------------------------------------
    private boolean isCameraAvailable()
    {
        if(mCamera == null || !mCamera.isOpen())
        {
            try
            {
                Camera.Parameters params = camParams();
                return true;
            }
            catch(Exception ex){LogHelper.logx(ex);return false;}
        }
        else
        {
            return true;
        }
    }


    //----------------------------------------------------------------------------------------------
    // setCamParams
    //----------------------------------------------------------------------------------------------
    public void setCamParams(Camera.Parameters params)
    {
        if(mCamera == null || !mCamera.isOpen()) return;

        mCamera.setParameters(params);
        mDefaultParamsUsed = (params == mDefaultParams);


    }




    //----------------------------------------------------------------------------------------------
    // strPicFormat
    //----------------------------------------------------------------------------------------------
    String strPicFormat(int format)
    {
        if(format == ImageFormat.JPEG) return  "JPEG";
        else if(format == ImageFormat.JPEG) return  "JPEG";
        else if(format == ImageFormat.NV16) return  "NV16";
        else if(format == ImageFormat.NV21) return  "NV21";
        else if(format == ImageFormat.RAW10) return  "RAW10";
        else if(format == ImageFormat.RGB_565) return  "RGB_565";
        else if(format == ImageFormat.YUV_420_888) return  "YUV_420_888";
        else if(format == ImageFormat.YUY2) return  "YUY2";
        else if(format == ImageFormat.YV12) return  "YV12";
        else return  "unknown";


    }











    //----------------------------------------------------------------------------------------------
    // loadFlatParams
    //----------------------------------------------------------------------------------------------
    private void loadFlatParams()
    {

        if(!isCameraAvailable()) return;

        mFlatParams.clear();

        try
        {
            Camera.Parameters params = camParams();
            String flat = params.flatten();
            String[] entries = flat.split(";");
            if(entries.length>0)
            {
                for(String entry:entries)
                {
                    String[] keyval = entry.split("=");
                    if(keyval.length>1)
                    {
                        String key = keyval[0];
                        ArrayList<String> valList = new ArrayList<String>();
                        if(keyval[1].contains(","))
                        {
                            String[] vals = keyval[1].split(",");
                            for(int i=0;i<vals.length;i++)
                            {
                                String val = vals[i].trim();
                                if(!val.isEmpty()) valList.add(val);
                            }
                            //valList.addAll(Arrays.asList(vals));
                        }
                        else
                        {
                            valList.add(keyval[1]);
                        }

                        mFlatParams.put(key,valList);
                    }
                }
            }

        }
        catch (Exception ex)
        {
            LogHelper.logx(ex);
        }

    }


    //----------------------------------------------------------------------------------------------
    // hasParamValue
    //----------------------------------------------------------------------------------------------
    public boolean hasParamValue(String paramKey, String valsKey, String value)
    {
        if(!isCameraAvailable()) return false;
        if(!hasParam(paramKey, valsKey)) return false;


        List<String> vals = mFlatParams.get(valsKey);

        if(value.contains(","))
        {
            String[] values = value.split(",");
            for(String v : values)
            {
                if(vals.contains(v)) return true;
            }
        }
        else if(vals.contains(value)) return true;


        return false;

    }


    //----------------------------------------------------------------------------------------------
    // hasParam
    //----------------------------------------------------------------------------------------------
    public boolean hasParam(String paramKey, String valsKey)
    {
        if(!isCameraAvailable()) return false;
        if(mFlatParams.containsKey(paramKey) && mFlatParams.containsKey(valsKey) )
        {
            List<String> vals = mFlatParams.get(valsKey);
            if(vals.size() > 1) return true;

        }

        return false;

    }

    //----------------------------------------------------------------------------------------------
    // applyParam
    //----------------------------------------------------------------------------------------------
    public void applyParam(String paramKey, String valsKey, String value)
    {
        if(!isCameraAvailable() || value==null || value.isEmpty()) return;

        Camera.Parameters params = camParams();
        List<String> vals = mFlatParams.get(valsKey);


        boolean ok = false;
        if(value.contains(","))
        {
            String[] values = value.split(",");
            for(String v : values)
            {
                if(vals.contains(v)) {ok = true; break;}
            }
        }
        else if(vals.contains(value)) ok = true;

        if(ok)
        {
            params.set(paramKey, value);
            setCamParams(params);
        }

    }


    //----------------------------------------------------------------------------------------------
    // isMinMaxValid
    //----------------------------------------------------------------------------------------------
    public boolean isMinMaxValid(Map<String,String> keys)
    {
        Camera.Parameters params = camParams();

        if(!keys.containsKey(KEY_MIN) || !keys.containsKey(KEY_MAX) )
        {
            return false;
        }

        if(params.get(keys.get(KEY_MIN)) == null ||
                params.get(keys.get(KEY_MAX)) == null  )
        {
            return false;
        }

        int min =  LangHelper.toInt(params.get(keys.get(KEY_MIN)));
        int max =  LangHelper.toInt(params.get(keys.get(KEY_MAX)));

        return (min != max);
    }



    //----------------------------------------------------------------------------------------------
    // applyParamFactor (0 - 1)
    //----------------------------------------------------------------------------------------------
    public void applyParamFactor(Map<String,String> keys ,float factor)
    {
        if(!isCameraAvailable()) return;


        if(!keys.containsKey(KEY_PARAM)  ||
                !keys.containsKey(KEY_MAX) )
        {
            return;
        }



        Camera.Parameters params = camParams();
        int value = 0;

        int max =  LangHelper.toInt(params.get(keys.get(KEY_MAX)));

        int min = 0;
        if(keys.containsKey(KEY_MIN) && params.get(keys.get(KEY_MIN)) != null)
        {
            min =  LangHelper.toInt(params.get(keys.get(KEY_MIN)));
        }

        int step = 1;
        if(keys.containsKey(KEY_STEP) && params.get(keys.get(KEY_STEP)) != null)
        {
            step =  LangHelper.toInt(params.get(keys.get(KEY_STEP)));
        }


        factor = LangHelper.clamp(factor, 0f, 1f);


        int interval = max - min;
        value = Math.round(min + (interval * factor));
        value = LangHelper.clamp(value, min, max);

        params.set(keys.get(KEY_PARAM), value);
        setCamParams(params);


    }



    //----------------------------------------------------------------------------------------------
    // readParamFactor
    //----------------------------------------------------------------------------------------------
    public float readParamFactor(Map<String,String> keys)
    {
        float factor = 0;

        if(!isCameraAvailable()) return factor;



        if(!keys.containsKey(KEY_PARAM) ||
                !keys.containsKey(KEY_MAX) )
        {
            return factor;
        }

        Camera.Parameters params = camParams();

        int max =  LangHelper.toInt(params.get(keys.get(KEY_MAX)));
        int value = LangHelper.toInt(params.get(keys.get(KEY_PARAM)));
        int min = 0;
        if(keys.containsKey(KEY_MIN) && params.get(keys.get(KEY_MIN)) != null)
        {
            min =  LangHelper.toInt(params.get(keys.get(KEY_MIN)));
        }


        int span = max - min;

        factor = (float) value /(float) span;

        factor = LangHelper.clamp(factor, 0f, 1f);
        return factor;
    }


    //----------------------------------------------------------------------------------------------
    // convertValueToFactor
    //----------------------------------------------------------------------------------------------
    private float convertValueToFactor(int value, int max, int min)
    {
        float factor = 0;



        if(value >= 0)
        {
            factor = (float) value /(float) max;
        }
        else
        {
            factor = (float) value /(float) Math.abs(min);
        }

        return factor;
    }



    //----------------------------------------------------------------------------------------------
    // SizeComparator
    //----------------------------------------------------------------------------------------------

    static  class SizeComparator implements Comparator<Size>
    {

        @Override
        public int compare(Size size1, Size size2)
        {
            int area1,area2;
            area1 = size1.width*size1.height;
            area2 = size1.width*size1.height;
            return (area1-area2);
        }
    }


    //==============================================================================================
    // Listeners
    //==============================================================================================


    public void addListener(CameraParametersListener listener)
    {
        mListeners.addUnique(listener);
    }

    public void removeListener(CameraParametersListener listener)
    {
        mListeners.remove(listener);
    }

    public void notifyListeners(String param, Object value )
    {

        for(CameraParametersListener listener : mListeners)
        {
            listener.onCameraParameterChanged(param, value);
        }


    }





    //==============================================================================================
    // Types
    //==============================================================================================

    public interface CameraParametersListener
    {
        public void onCameraParameterChanged(String param, Object value);
    }
}
