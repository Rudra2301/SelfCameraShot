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
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;

import com.tafayor.selfcamerashot.App;
import com.tafayor.selfcamerashot.R;
import com.tafayor.selfcamerashot.camera.CameraParameters;

import com.tafayor.selfcamerashot.camera.CameraWrapper;
import com.tafayor.selfcamerashot.taflib.helpers.AppHelper;
import com.tafayor.selfcamerashot.taflib.helpers.LogHelper;
import com.tafayor.selfcamerashot.taflib.helpers.ResHelper;
import com.tafayor.selfcamerashot.taflib.types.Size;
import com.tafayor.selfcamerashot.taflib.ui.components.SeekBarPreference;
import com.tafayor.selfcamerashot.taflib.ui.custom.CustomListPreference;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class LegacyCameraSettingsActivity extends PreferenceActivity implements Preference.OnPreferenceClickListener,
        SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceChangeListener
{

    public static String  TAG  = LegacyCameraSettingsActivity.class.getSimpleName();



   String  KEY_PREF_CAT_ADVANCED_CAMERA_SETTINGS = "prefCatAdvancedCameraSettings";

    private ArrayList<String> mAutoSummaryPrefs;
    private int mCameraId;
    private CameraParameters mCamParams;
    private CameraWrapper mCamera;
    SharedPreferences mSharedPrefs;
    Context mContext;
    protected SharedPreferences.Editor mPrefsEditor;





    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        init();
    }


    /**
     * Shows the simplified settings UI if the device configuration if the
     * device configuration dictates that a simplified, single-pane UI should be
     * shown.
     */
    private void init()
    {
        mAutoSummaryPrefs = new ArrayList<>();

        App.getAdvancedCameraPrefHelper().loadDefaultPrefs();
        App.getCameraPrefHelper().loadDefaultPrefs();
        App.getUiPrefHelper().loadDefaultPrefs();
        App.getActivatorsPrefHelper().loadDefaultPrefs();
        App.getRemoteControlPrefHelper().loadDefaultPrefs();

        //LogHelper.doLog(TAG, "init", "local : " + App.getLegacyCameraPrefHelper().getLanguage());

        AppHelper.setLocale(mContext, App.getGeneralPrefHelper().getLanguage());
        getPreferenceManager().setSharedPreferencesName(TAG);

        addPreferencesFromResource(R.xml.pref_legacy);
        mSharedPrefs = getSharedPreferences(TAG, MODE_PRIVATE);
        mPrefsEditor = mSharedPrefs.edit();


        Preference shutterSound = findPreference(CameraPrefHelper.KEY_PREF_ENABLE_SHUTTER_SOUND);
        getPreferenceScreen().removePreference(shutterSound);







        mAutoSummaryPrefs.add(CameraPrefHelper.KEY_PREF_ENABLE_SHUTTER_SOUND);
        mAutoSummaryPrefs.add(CameraPrefHelper.KEY_PREF_PICTURE_SIZE);
        mAutoSummaryPrefs.add(RemoteControlPrefHelper.KEY_PREF_REMOTE_MODE);
        mAutoSummaryPrefs.add(RemoteControlPrefHelper.KEY_PREF_WHISLTE_SENSITIVITY);
        mAutoSummaryPrefs.add(RemoteControlPrefHelper.KEY_PREF_CLAPPING_SENSITIVITY);
        mAutoSummaryPrefs.add(GeneralPrefHelper.KEY_PREF_LANGUAGE);
        mAutoSummaryPrefs.add(CameraPrefHelper.KEY_PREF_FOCUS_MODE);
        mAutoSummaryPrefs.add(CameraPrefHelper.KEY_PREF_SCENE_MODE);
        mAutoSummaryPrefs.add(AdvancedCameraPrefHelper.KEY_PREF_PREVIEW_SIZE);
        mAutoSummaryPrefs.add(AdvancedCameraPrefHelper.KEY_PREF_ISO);


        try
        {
            mCamera = CameraWrapper.i();
            Intent intent = getIntent();
            mCameraId = intent.getIntExtra(SettingsActivity.KEY_CAMERA_ID, 0);
            LogHelper.log(TAG , "mCameraId : " + mCameraId);
            mCamera.open(mCameraId);
            mCamParams = new CameraParameters();
            mCamParams.setCamera(mCameraId, mCamera);



            mPrefsEditor.putString(GeneralPrefHelper.KEY_PREF_LANGUAGE,
                    App.getGeneralPrefHelper().getLanguage());
            mPrefsEditor.commit();

            mPrefsEditor.putString(RemoteControlPrefHelper.KEY_PREF_REMOTE_MODE,
                    App.getRemoteControlPrefHelper().getRemoteMode());
            mPrefsEditor.commit();

            mPrefsEditor.putInt(RemoteControlPrefHelper.KEY_PREF_REMOTE_MODE_DELAY,
                    App.getRemoteControlPrefHelper().getRemoteModeDelay());
            mPrefsEditor.commit();

            mPrefsEditor.putInt(RemoteControlPrefHelper.KEY_PREF_WHISLTE_SENSITIVITY,
                    App.getRemoteControlPrefHelper().getWhistleSensitivity());
            mPrefsEditor.commit();

            mPrefsEditor.putInt(RemoteControlPrefHelper.KEY_PREF_CLAPPING_SENSITIVITY,
                    App.getRemoteControlPrefHelper().getClappingSensitivity());
            mPrefsEditor.commit();



            setupPreviewSizePreference();
            setupPictureSizePreference();
            setupFocusModePreference();
            setupSceneModePreference();

        }
        catch(Exception ex)
        {
            LogHelper.logx(ex);
        }






        for(String key : mAutoSummaryPrefs)
        {
            onPrefChanged(key);
        }

        onPrefChanged(RemoteControlPrefHelper.KEY_PREF_REMOTE_MODE_DELAY);





        mCamera.close();
        mCamera = null;
    }

    //==============================================================================================
    // Callbacks
    //==============================================================================================


    @Override
    protected void onResume()
    {
        super.onResume();
        mSharedPrefs.registerOnSharedPreferenceChangeListener(this);
        if(mCamera != null)  mCamera.close();
    }


    @Override
    protected void onPause()
    {
        super.onPause();
        mSharedPrefs.unregisterOnSharedPreferenceChangeListener(this);
    }

    //----------------------------------------------------------------------------------------------
    // onPrefChanged
    //----------------------------------------------------------------------------------------------
    public void onPrefChanged(String key)
    {

        if(mAutoSummaryPrefs.contains(key))
        {

            Preference pref = findPreference(key);
            updatePreferenceSummary(pref, key);
        }



        if(key.equals(GeneralPrefHelper.KEY_PREF_LANGUAGE))
        {
            App.getGeneralPrefHelper().setLanguage(mSharedPrefs.getString(GeneralPrefHelper.KEY_PREF_LANGUAGE,"en"));
        }
        if(key.equals(CameraPrefHelper.KEY_PREF_PICTURE_SIZE))
        {
            Size size = Size.fromString(mSharedPrefs.getString(CameraPrefHelper.KEY_PREF_PICTURE_SIZE,DefaultPrefs.PICTURE_SIZE.toString()));
            App.getCameraPrefHelper().setXCamPictureSize(mCameraId,size);

        }
        else if(key.equals(AdvancedCameraPrefHelper.KEY_PREF_PREVIEW_SIZE))
        {
            Size size = Size.fromString(mSharedPrefs.getString(AdvancedCameraPrefHelper.KEY_PREF_PREVIEW_SIZE,DefaultPrefs.PREVIEW_SIZE.toString()));
            App.getAdvancedCameraPrefHelper().setPreviewSize(mCameraId, size);
        }
        else if (key.equals(RemoteControlPrefHelper.KEY_PREF_REMOTE_MODE_DELAY))
        {
            Preference pref = findPreference(RemoteControlPrefHelper.KEY_PREF_REMOTE_MODE_DELAY);
            String summary ;
            int delay = mSharedPrefs.getInt(RemoteControlPrefHelper.KEY_PREF_REMOTE_MODE_DELAY,0);
            summary = getResources().getString(R.string.pref_remoteModeDelay_formattedValue, delay);
            pref.setSummary(summary);
        }
        if(key.equals(CameraPrefHelper.KEY_PREF_FOCUS_MODE))
        {

            App.getCameraPrefHelper().setFocusMode(mCameraId,
                    mSharedPrefs.getString(CameraPrefHelper.KEY_PREF_FOCUS_MODE,DefaultPrefs.FOCUS_MODE));
        }
        else if(key.equals(CameraPrefHelper.KEY_PREF_SCENE_MODE))
        {

            App.getCameraPrefHelper().setSceneMode(mCameraId,
                    mSharedPrefs.getString(CameraPrefHelper.KEY_PREF_SCENE_MODE,DefaultPrefs.SCENE_MODE));
        }
        else  if(key.equals(RemoteControlPrefHelper.KEY_PREF_REMOTE_MODE))
        {
            App.getRemoteControlPrefHelper().setRemoteMode(mSharedPrefs.getString(RemoteControlPrefHelper.KEY_PREF_REMOTE_MODE,""));
        }
        else  if(key.equals(RemoteControlPrefHelper.KEY_PREF_WHISLTE_SENSITIVITY))
        {
            App.getRemoteControlPrefHelper().setWhistleSensitivity(
                    mSharedPrefs.getInt(RemoteControlPrefHelper.KEY_PREF_WHISLTE_SENSITIVITY, 100));
        }
        else  if(key.equals(RemoteControlPrefHelper.KEY_PREF_CLAPPING_SENSITIVITY))
        {
            App.getRemoteControlPrefHelper().setClappingSensitivity(
                    mSharedPrefs.getInt(RemoteControlPrefHelper.KEY_PREF_CLAPPING_SENSITIVITY, 100));
        }
        else  if(key.equals(CameraPrefHelper.KEY_PREF_ENABLE_SHUTTER_SOUND))
        {
            App.getCameraPrefHelper().setEnableShutterSound(
                    mSharedPrefs.getBoolean(CameraPrefHelper.KEY_PREF_ENABLE_SHUTTER_SOUND,true));
        }
    }








    //==============================================================================================
    // Internals
    //==============================================================================================



    //----------------------------------------------------------------------------------------------
    // setupPictureSizePreference
    //----------------------------------------------------------------------------------------------
    private void setupPreviewSizePreference()
    {
        mPrefsEditor.putString(AdvancedCameraPrefHelper.KEY_PREF_PREVIEW_SIZE,
                App.getAdvancedCameraPrefHelper().getPreviewSize(mCameraId).toString());
        mPrefsEditor.commit();


        ListPreference pref = (ListPreference)findPreference(
                AdvancedCameraPrefHelper.KEY_PREF_PREVIEW_SIZE);

        List<Size> sizes = mCamParams.getSortedPreviewSizes();
        String[] entries = new String[sizes.size()];
        String[] entryValues = new String[sizes.size()];
        DecimalFormat  decimalFormat = new DecimalFormat("0.0");
        String unit = "M pixels";
        boolean swipeDimensions = false;
        if((mCamParams.getCameraOrientation() % 90) == 0) swipeDimensions = true;


        if(!mCamParams.hasPreviewSizes())
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
            int selectedIndex = pref.findIndexOfValue(App.getAdvancedCameraPrefHelper(). getPreviewSize(mCameraId).toString()) ;
            if(selectedIndex == -1)
            {

                App.getAdvancedCameraPrefHelper().loadDefaultPreviewSizePref(mCamParams);
            }

            try
            {


                selectedIndex = pref.findIndexOfValue(App.getAdvancedCameraPrefHelper().getPreviewSize(mCameraId).toString());
                 pref.setValueIndex(selectedIndex);
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


    //----------------------------------------------------------------------------------------------
    // setupSceneModePreference
    //----------------------------------------------------------------------------------------------
    private void setupSceneModePreference()
    {

        mPrefsEditor.putString(CameraPrefHelper.KEY_PREF_SCENE_MODE,
                App.getCameraPrefHelper().getSceneMode(mCameraId).toString());
        mPrefsEditor.commit();


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
        mPrefsEditor.putString(CameraPrefHelper.KEY_PREF_FOCUS_MODE,
                App.getCameraPrefHelper().getFocusMode(mCameraId));
        mPrefsEditor.commit();


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
        mPrefsEditor.putString(CameraPrefHelper.KEY_PREF_PICTURE_SIZE,
                App.getCameraPrefHelper().getXCamPictureSize(mCameraId).toString());
        mPrefsEditor.commit();


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






    public boolean updatePreferenceSummary(Preference preference, String key)
    {


        if (preference instanceof ListPreference || preference instanceof CustomListPreference)
        {

            String stringValue = mSharedPrefs.getString(key,"");
            ListPreference listPreference = (ListPreference) preference;
            int index = listPreference.findIndexOfValue(stringValue);


            String summary = (String) ((index >= 0) ? listPreference.getEntries()[index] : null);

            preference.setSummary(summary);

        }
        else if(preference instanceof SeekBarPreference)
        {
            int intValue = mSharedPrefs.getInt(key, 0);
            SeekBarPreference seekBarPreference =  (SeekBarPreference) preference;

            String summary = "/" + seekBarPreference.getMaxValue() ;
            // int progress = seekBarPreference.getProgress();
            summary = intValue + summary;
            seekBarPreference.setSummary(summary);
        }
        else if(preference instanceof CheckBoxPreference )
        {

            boolean boolValue = mSharedPrefs.getBoolean(key, false);
            String summary, choice;
            choice = "";
            if (boolValue)
                choice = ResHelper.getResString(mContext, R.string.yes);
            else
                choice = ResHelper.getResString(mContext,R.string.no);

            summary = choice ;
            preference.setSummary(summary);
        }
        return true;

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

        return false;
    }



    //=================================
    // SharedPreferences.OnSharedPreferenceChangeListener
    //=================================
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {


        if(key.equals(GeneralPrefHelper.KEY_PREF_LANGUAGE))
        {
            AppHelper.setLocale(mContext,App.getGeneralPrefHelper().getLanguage());
            AppHelper.restartActivity(this);
        }
        onPrefChanged(key);
    }
}
