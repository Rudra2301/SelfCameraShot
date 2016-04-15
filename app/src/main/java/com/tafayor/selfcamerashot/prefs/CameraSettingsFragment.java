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

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;



import com.tafayor.selfcamerashot.App;
import com.tafayor.selfcamerashot.R;
import com.tafayor.selfcamerashot.camera.CameraParameters;
import com.tafayor.selfcamerashot.camera.CameraWrapper;
import com.tafayor.selfcamerashot.taflib.helpers.AppHelper;
import com.tafayor.selfcamerashot.taflib.helpers.LogHelper;
import com.tafayor.selfcamerashot.taflib.helpers.ViewHelper;
import com.tafayor.selfcamerashot.taflib.types.Size;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;



@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class CameraSettingsFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener,
        SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceChangeListener
{
    public static String TAG = CameraSettingsFragment.class.getSimpleName();







    private ArrayList<String> mAutoSummaryPrefs;
    private int mCameraId;
    private CameraParameters mCamParams;
    private CameraWrapper mCamera;
    private Context mContext;




    public static CameraSettingsFragment newInstance(int cameraId)
    {
        CameraSettingsFragment f = new CameraSettingsFragment();

        Bundle args = new Bundle();
        args.putInt(SettingsActivity.KEY_CAMERA_ID, cameraId);
        f.setArguments(args);
        return f;
    }




    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mContext = getActivity().getApplicationContext();
        mAutoSummaryPrefs = new ArrayList<>();



        AppHelper.setLocale(mContext, App.getGeneralPrefHelper().getLanguage());

        getPreferenceManager().setSharedPreferencesName(CameraPrefHelper.SHARED_PREFERENCES_NAME);
        addPreferencesFromResource(R.xml.pref_camera_main);



        if(Build.VERSION.SDK_INT <17)
        {
            Preference shutterSound = findPreference(CameraPrefHelper.KEY_PREF_ENABLE_SHUTTER_SOUND);
            getPreferenceScreen().removePreference(shutterSound);
        }









        mAutoSummaryPrefs.add(CameraPrefHelper.KEY_PREF_ENABLE_SHUTTER_SOUND);
        mAutoSummaryPrefs.add(CameraPrefHelper.KEY_PREF_PICTURE_SIZE);
        mAutoSummaryPrefs.add(CameraPrefHelper.KEY_PREF_FOCUS_MODE);
        mAutoSummaryPrefs.add(CameraPrefHelper.KEY_PREF_SCENE_MODE);












    }











    //==============================================================================================
    // Callbacks
    //==============================================================================================


    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);


        ViewHelper.fixViewGroupRtl(mContext, getView());



    }



    @Override
    public void onResume()
    {

        super.onResume();

        App.getCameraPrefHelper().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        try
        {
            mCamera = CameraWrapper.i();
            mCameraId = getActivity().getIntent().getIntExtra(SettingsActivity.KEY_CAMERA_ID, -1);


            mCamera.open(mCameraId);
            mCamParams = new CameraParameters();
            mCamParams.setCamera(mCameraId, mCamera);

            setupPictureSizePreference();
            setupFocusModePreference();
            setupSceneModePreference();

            for(String key : mAutoSummaryPrefs)
            {
                onPrefChanged(key);
            }



        }
        catch(Exception ex)
        {

            LogHelper.logx(ex);
        }
        finally
        {
            if(mCamera != null)  mCamera.close();
        }

    }


    @Override
    public void onPause()
    {

        super.onPause();
        App.getCameraPrefHelper().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);

        if(mCamera != null)  mCamera.close();
    }








    //----------------------------------------------------------------------------------------------
    // onPrefChanged
    //----------------------------------------------------------------------------------------------
    public void onPrefChanged(String key)
    {

        if(mAutoSummaryPrefs.contains(key))
        {

            Preference pref = findPreference(key);
            App.getCameraPrefHelper().updatePreferenceSummary(mContext,pref, key);
        }


        if(key.equals(CameraPrefHelper.KEY_PREF_PICTURE_SIZE))
        {
            App.getCameraPrefHelper().setXCamPictureSize(mCameraId, App.getCameraPrefHelper().getPictureSize());
        }
        else if(key.equals(CameraPrefHelper.KEY_PREF_FOCUS_MODE))
        {

            App.getCameraPrefHelper().setFocusMode(mCameraId, App.getCameraPrefHelper().getFocusMode());
        }
        else if(key.equals(CameraPrefHelper.KEY_PREF_SCENE_MODE))
        {

            App.getCameraPrefHelper().setSceneMode(mCameraId, App.getCameraPrefHelper().getSceneMode());
        }


    }










    //==============================================================================================
    // Internals
    //==============================================================================================








    //----------------------------------------------------------------------------------------------
    // setupSceneModePreference
    //----------------------------------------------------------------------------------------------
    private void setupSceneModePreference()
    {
        ListPreference scenePref = (ListPreference)findPreference(
                CameraPrefHelper.KEY_PREF_SCENE_MODE);



        String[] labels = getResources().getStringArray(R.array.prefSceneModeLabels);
        String[] values = getResources().getStringArray(R.array.prefSceneModeValues);
        List<String> excludedEntries = new ArrayList<>();



        if(!mCamParams.hasScene())
        {
            scenePref.setEnabled(false);
            getPreferenceScreen().removePreference(scenePref);
        }
        else
        {
            if(!mCamParams.hasSceneMode(Camera.Parameters.SCENE_MODE_AUTO))  excludedEntries.add(SceneModeValues.AUTO);
            if(!mCamParams.hasSceneMode(Camera.Parameters.SCENE_MODE_ACTION))  excludedEntries.add(SceneModeValues.ACTION);
            if(!mCamParams.hasSceneMode(Camera.Parameters.SCENE_MODE_BARCODE))  excludedEntries.add(SceneModeValues.BARCODE);
            if(!mCamParams.hasSceneMode(Camera.Parameters.SCENE_MODE_BEACH))  excludedEntries.add(SceneModeValues.BEACH);
            if(!mCamParams.hasSceneMode(Camera.Parameters.SCENE_MODE_CANDLELIGHT))  excludedEntries.add(SceneModeValues.CANDLE_LIGHT);
            if(!mCamParams.hasSceneMode(Camera.Parameters.SCENE_MODE_FIREWORKS))  excludedEntries.add(SceneModeValues.FIREWORKS);
            if(!mCamParams.hasSceneMode(Camera.Parameters.SCENE_MODE_HDR))  excludedEntries.add(SceneModeValues.HDR);
            if(!mCamParams.hasSceneMode(Camera.Parameters.SCENE_MODE_LANDSCAPE))  excludedEntries.add(SceneModeValues.LANDSCAPE);
            if(!mCamParams.hasSceneMode(Camera.Parameters.SCENE_MODE_NIGHT))  excludedEntries.add(SceneModeValues.NIGHT);
            if(!mCamParams.hasSceneMode(Camera.Parameters.SCENE_MODE_PARTY))  excludedEntries.add(SceneModeValues.PARTY);
            if(!mCamParams.hasSceneMode(Camera.Parameters.SCENE_MODE_PORTRAIT))  excludedEntries.add(SceneModeValues.PORTRAIT);
            if(!mCamParams.hasSceneMode(Camera.Parameters.SCENE_MODE_SNOW))  excludedEntries.add(SceneModeValues.SNOW);
            if(!mCamParams.hasSceneMode(Camera.Parameters.SCENE_MODE_SPORTS))  excludedEntries.add(SceneModeValues.SPORTS);
            if(!mCamParams.hasSceneMode(Camera.Parameters.SCENE_MODE_STEADYPHOTO))  excludedEntries.add(SceneModeValues.STEADY_PHOTO);
            if(!mCamParams.hasSceneMode(Camera.Parameters.SCENE_MODE_SUNSET))  excludedEntries.add(SceneModeValues.SUNSET);
            if(!mCamParams.hasSceneMode(Camera.Parameters.SCENE_MODE_THEATRE))  excludedEntries.add(SceneModeValues.THEATRE);

            App.getCameraPrefHelper().removeListPrefItems(scenePref,labels,values,excludedEntries);


            int selectedIndex = scenePref.findIndexOfValue(App.getCameraPrefHelper(). getSceneMode(mCameraId)) ;
            if(selectedIndex == -1)
            {
                App.getCameraPrefHelper(). setSceneMode(mCameraId, SceneModeValues.UNSET);
                App.getCameraPrefHelper().loadDefaultSceneModePref(mCamParams);
            }

            try
            {
                scenePref.setValueIndex(scenePref.findIndexOfValue(App.getCameraPrefHelper(). getSceneMode(mCameraId)));
            }
            catch(Exception ex)
            {
                LogHelper.logx(ex);
            }

        }

    }


    //----------------------------------------------------------------------------------------------
    // setupFocusModePreference
    //----------------------------------------------------------------------------------------------
    private void setupFocusModePreference()
    {
        ListPreference focusPref = (ListPreference)findPreference(
                CameraPrefHelper.KEY_PREF_FOCUS_MODE);



        String[] labels = getResources().getStringArray(R.array.prefFocusModeLabels);
        String[] values = getResources().getStringArray(R.array.prefFocusModeValues);
        List<String> excludedEntries = new ArrayList<>();



        if(!mCamParams.hasFocus())
        {
            focusPref.setEnabled(false);
            getPreferenceScreen().removePreference(focusPref);
        }
        else
        {

            if(!mCamParams.hasFocusMode(Camera.Parameters.FOCUS_MODE_AUTO))  excludedEntries.add(FocusModeValues.AUTO);
            if(!mCamParams.hasFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE))  excludedEntries.add(FocusModeValues.CONTINUOUS);
            if(!mCamParams.hasFocusMode(Camera.Parameters.FOCUS_MODE_INFINITY))  excludedEntries.add(FocusModeValues.INIFINITY);
            if(!mCamParams.hasFocusMode(Camera.Parameters.FOCUS_MODE_MACRO))  excludedEntries.add(FocusModeValues.MACRO);
            if(!mCamParams.hasFocusMode(Camera.Parameters.FOCUS_MODE_FIXED))  excludedEntries.add(FocusModeValues.FIXED);


            App.getCameraPrefHelper().removeListPrefItems(focusPref,labels,values,excludedEntries);


            int selectedIndex = focusPref.findIndexOfValue(App.getCameraPrefHelper(). getFocusMode(mCameraId)) ;
            if(selectedIndex == -1)
            {
                App.getCameraPrefHelper(). setFocusMode(mCameraId, FocusModeValues.UNSET);
                App.getCameraPrefHelper().loadDefaultFocusModePref(mCamParams);
            }

            try
            {
                focusPref.setValueIndex(focusPref.findIndexOfValue(App.getCameraPrefHelper(). getFocusMode(mCameraId)));
            }
            catch(Exception ex)
            {
                LogHelper.logx(ex);
            }

        }

    }


    //----------------------------------------------------------------------------------------------
    // setupPictureSizePreference
    //----------------------------------------------------------------------------------------------
    private void setupPictureSizePreference()
    {
        ListPreference pref = (ListPreference)findPreference(
                CameraPrefHelper.KEY_PREF_PICTURE_SIZE);

        List<Size> sizes = mCamParams.getSortedPictureSizes();
        String[] entries = new String[sizes.size()];
        String[] entryValues = new String[sizes.size()];
        DecimalFormat  decimalFormat = new DecimalFormat("0.0");
        String unit = "M pixels";
        boolean swipeDimensions = false;
        if((mCamParams.getCameraOrientation() % 90) == 0) swipeDimensions = true;

        if(!mCamParams.hasPictureSizes())
        {
            pref.setEnabled(false);
            getPreferenceScreen().removePreference(pref);
            return;
        }


        for(int i=0;i<sizes.size();i++)
        {
            Size size =  sizes.get(i);
            float mpixSize = size.width * size.height / 1024000f;
            if(mpixSize>4) mpixSize = Math.round(mpixSize);
            entries[i] = decimalFormat.format(mpixSize) + unit ;
            if(swipeDimensions) entries[i] +=  " ("+size.height + "  x " + size.width+")";
            else entries[i] += " ("+size.width + "  x " + size.height+")";
            entryValues[i] = size.toString();


        }
        pref.setEntries(entries);
        pref.setEntryValues(entryValues);

        if(sizes.size() > 0)
        {
            try
            {
                LogHelper.log("getXCamPictureSize : " +  App.getCameraPrefHelper().getXCamPictureSize(mCameraId).toString());
                pref.setValueIndex(pref.findIndexOfValue(App.getCameraPrefHelper().getXCamPictureSize(mCameraId).toString()));
            }
            catch(Exception ex)
            {
                LogHelper.logx(ex);
            }

        }
        else
        {
            getPreferenceScreen().removePreference(pref);
        }


    }




    //==============================================================================================
    // Implementation
    //==============================================================================================

    //=================================
    // Preference.OnPreferenceClickListener,
    //=================================
    @Override
    public boolean onPreferenceClick(Preference preference)
    {

        return false;
    }



    //=================================
    // Preference.OnPreferenceChangeListener
    //=================================
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue)
    {
        LogHelper.log(TAG, "onPreferenceChange  : " + preference.getKey());
        return false;
    }



    //=================================
    // SharedPreferences.OnSharedPreferenceChangeListener
    //=================================
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {
        LogHelper.log(TAG, "onSharedPreferenceChanged  : " + key);


        onPrefChanged(key);

    }

}
