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


package com.tafayor.selfcamerashot.prefs;

import android.content.Context;
import android.hardware.Camera;


import com.tafayor.selfcamerashot.camera.CameraParameters;
import com.tafayor.selfcamerashot.taflib.helpers.BasePrefsHelper;
import com.tafayor.selfcamerashot.taflib.helpers.LogHelper;
import com.tafayor.selfcamerashot.taflib.types.Size;


/**
 *
 * Created by youssef on 20/12/13.
 */

public class CameraPrefHelper extends BasePrefsHelper
{
    public static String TAG = CameraPrefHelper.class.getSimpleName() ;


    public static String SHARED_PREFERENCES_NAME = TAG;



    public static String KEY_PREF_CAMERA_VIEW = "prefCameraViewInt";
    public static String KEY_PREF_FLASH_MODE = "prefFlashMode";
    public static String KEY_PREF_ENABLE_SHUTTER_SOUND = "prefEnableShutterSound";
    public static String KEY_PREF_WHITE_BALANCE = "prefWhiteBalance";
    public static String KEY_PREF_SHOT_COUNTDOWN = "prefShotCountdown";
    public static String KEY_PREF_PICTURE_SIZE= "prefPictureSize";
    public static String KEY_PREF_X_CAM_PICTURE_SIZE= "prefXCamPictureSize";
    public static String KEY_PREF_EXPOSURE_COMPENSATION_FACTOR = "prefExposureCompensationFactor";
    public static String KEY_PREF_FOCUS_MODE = "prefFocusMode";
    public static String KEY_PREF_ID_FOCUS_MODE = "prefIdFocusMode";
    public static String KEY_PREF_SCENE_MODE = "prefSceneMode";
    public static String KEY_PREF_ID_SCENE_MODE = "prefIdSceneMode";
    public static String KEY_PREF_CAM_FIRST_ID = "prefCamFirstId";
    public static String KEY_PREF_DEFAULTS_LOADED = "prefDefaultsLoaded";





    private static CameraPrefHelper sInstance;
    public static synchronized  CameraPrefHelper i(Context ctx)
    {
        if(sInstance == null) sInstance = new CameraPrefHelper(ctx);
        return sInstance;
    }

    public CameraPrefHelper(Context context)
    {
        super(context);
    }


    @Override
    protected String getSharedPreferencesName()
    {
        return SHARED_PREFERENCES_NAME;
    }


    //==================================================================================================

    public void loadDefaultPrefs()
    {
        if(!getBoolean(KEY_PREF_DEFAULTS_LOADED,false))
        {
            setCameraView(getCameraView());
            setFlashMode(getFlashMode());
            setEnableShutterSound(getEnableShutterSound());
            setWhiteBalance(getWhiteBalance());
            setShotCountdown(getShotCountdown());
            putBoolean(KEY_PREF_DEFAULTS_LOADED,true);
        }

    }





    public void loadDefaultPictureSizePref(CameraParameters camParams)
    {
        Size size = camParams.getOptimalPictureSize();
        LogHelper.log("getOptimalPictureSize : " + size.toString());
        setXCamPictureSize(camParams.getCameraId(), size);
    }



    public void loadDefaultFocusModePref(CameraParameters camParams)
    {
        Camera.Parameters params = camParams.getRawParams();
        String mode = CameraParameters.FOCUS_MODE_AUTO ;

        if(camParams.hasFocusMode(CameraParameters.FOCUS_MODE_AUTO)) mode = FocusModeValues.AUTO;
        else if(camParams.hasFocusMode(CameraParameters.FOCUS_MODE_MACRO)) mode = FocusModeValues.MACRO;
        else if(camParams.hasFocusMode(CameraParameters.FOCUS_MODE_CONTINUOUS_PICTURE)) mode = FocusModeValues.CONTINUOUS;
        else if(camParams.hasFocusMode(CameraParameters.FOCUS_MODE_INFINITY)) mode = FocusModeValues.INIFINITY;


        setFocusMode(camParams.getCameraId(), mode);
    }


    public void loadDefaultSceneModePref(CameraParameters camParams)
    {
        String mode = SceneModeValues.AUTO;
        /*if(camParams.hasSceneMode(Camera.Parameters.SCENE_MODE_STEADYPHOTO))
        {
            mode = SceneModeValues.STEADY_PHOTO;
        }*/
        setSceneMode(camParams.getCameraId(), mode);
    }




    //==================================================================================================






//***********

    public int getCamFirstId()
    {
        return mSharedPrefs.getInt(KEY_PREF_CAM_FIRST_ID, DefaultPrefs.CAM_FIRST_ID);
    }

    public void setCamFirstId(int value)
    {
        mPrefsEditor.putInt(KEY_PREF_CAM_FIRST_ID, value);
        mPrefsEditor.commit();
    }
    public String getSceneMode()
    {
        return getString(KEY_PREF_SCENE_MODE, DefaultPrefs.SCENE_MODE);
    }

    public void setSceneMode(String value)
    {
        putString(KEY_PREF_SCENE_MODE, value);
        mPrefsEditor.commit();
    }


    public String getSceneMode(int id)
    {
        return getString(KEY_PREF_ID_SCENE_MODE,""+id, DefaultPrefs.SCENE_MODE);
    }

    public void setSceneMode(int id, String value)
    {
        putString(KEY_PREF_ID_SCENE_MODE, ""+id, value);
        mPrefsEditor.commit();
    }

    public boolean isSceneModeSet(int id)
    {
        String mode = getSceneMode(id);
        return  (!mode.equals(SceneModeValues.UNSET));
    }


    //***********

    public String getFocusMode()
    {
        return getString(KEY_PREF_FOCUS_MODE, DefaultPrefs.FOCUS_MODE);
    }

    public void setFocusMode(String value)
    {
        putString(KEY_PREF_FOCUS_MODE, value);
        mPrefsEditor.commit();
    }


    public String getFocusMode(int id)
    {
        return getString(KEY_PREF_ID_FOCUS_MODE,""+id, DefaultPrefs.FOCUS_MODE);
    }

    public void setFocusMode(int id, String value)
    {
        putString(KEY_PREF_ID_FOCUS_MODE, ""+id, value);
        mPrefsEditor.commit();
    }

    public boolean isFocusModeSet(int id)
    {
        String mode = getFocusMode(id);
        return  (!mode.equals(FocusModeValues.UNSET));
    }


    //***********

    public float getExposure(int id)
    {
        return getFloat(KEY_PREF_EXPOSURE_COMPENSATION_FACTOR, ""+id, DefaultPrefs.EXPOSURE_COMPENSATION);
    }

    public void setExposure(int id, float value)
    {
        putFloat(KEY_PREF_EXPOSURE_COMPENSATION_FACTOR, "" + id, value);

    }




    //***********

    public Size getXCamPictureSize(int id)
    {
        String strSize = getString(KEY_PREF_X_CAM_PICTURE_SIZE, ""+id, DefaultPrefs.PICTURE_SIZE.toString());

        return Size.fromString(strSize);
    }

    public void setXCamPictureSize(int id, Size value)
    {
        String strVal = value.toString();
        putString(KEY_PREF_X_CAM_PICTURE_SIZE, ""+id, strVal);

    }

    public boolean isCamPictureSizeSet(int camId)
    {
        Size size = getXCamPictureSize(camId);
        return !size.isZero();
    }

    //***********

    public Size getPictureSize()
    {

        String strSize = mSharedPrefs.getString(KEY_PREF_PICTURE_SIZE, DefaultPrefs.PICTURE_SIZE.toString());
        return Size.fromString(strSize);
    }

    public void setPictureSize(Size value)
    {
        String strVal = value.toString();
        mPrefsEditor.putString(KEY_PREF_PICTURE_SIZE, strVal);
        mPrefsEditor.commit();
    }

    //***********


    public String getShotCountdown()
    {
        return mSharedPrefs.getString(KEY_PREF_SHOT_COUNTDOWN, DefaultPrefs.SHOT_COUNTDOWN);
    }

    public void setShotCountdown(String value)
    {
        mPrefsEditor.putString(KEY_PREF_SHOT_COUNTDOWN, value);
        mPrefsEditor.commit();
    }

    //***********

    public String getWhiteBalance()
    {
        return mSharedPrefs.getString(KEY_PREF_WHITE_BALANCE, DefaultPrefs.WHITE_BALANCE);
    }

    public void setWhiteBalance(String value)
    {
        mPrefsEditor.putString(KEY_PREF_WHITE_BALANCE, value);
        mPrefsEditor.commit();
    }

    //***********

    public boolean getEnableShutterSound()
    {
        return mSharedPrefs.getBoolean(KEY_PREF_ENABLE_SHUTTER_SOUND, DefaultPrefs.ENABLE_SHUTTER_SOUND);
    }

    public void setEnableShutterSound(boolean value)
    {
        mPrefsEditor.putBoolean(KEY_PREF_ENABLE_SHUTTER_SOUND, value);
        mPrefsEditor.commit();
    }

    //***********

    public int getCameraView()
    {
        return mSharedPrefs.getInt(KEY_PREF_CAMERA_VIEW, 0);
    }

    public void setCameraView(int value)
    {

        mPrefsEditor.putInt(KEY_PREF_CAMERA_VIEW, value);
        mPrefsEditor.commit();
    }

    //***********

    public String getFlashMode()
    {
        return mSharedPrefs.getString(KEY_PREF_FLASH_MODE, FlashModeValues.OFF);
    }

    public void setFlashMode(String value)
    {
        mPrefsEditor.putString(KEY_PREF_FLASH_MODE, value);
        mPrefsEditor.commit();
    }











    //==============================================================================================
    // Implementation
    //==============================================================================================






}
