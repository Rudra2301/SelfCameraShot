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
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;


import com.nononsenseapps.filepicker.FilePickerActivity;
import com.tafayor.selfcamerashot.App;

import com.tafayor.selfcamerashot.R;
import com.tafayor.selfcamerashot.camera.CameraParameters;
import com.tafayor.selfcamerashot.camera.CameraWrapper;
import com.tafayor.selfcamerashot.taflib.helpers.AppHelper;
import com.tafayor.selfcamerashot.taflib.helpers.LogHelper;
import com.tafayor.selfcamerashot.taflib.helpers.MsgHelper;
import com.tafayor.selfcamerashot.taflib.helpers.ViewHelper;
import com.tafayor.selfcamerashot.taflib.types.Size;


import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class AdvancedCameraSettingsFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener,
        SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceChangeListener
{
    public static String TAG = AdvancedCameraSettingsFragment.class.getSimpleName();




    int FILE_CODE  = 1;


    private ArrayList<String> mAutoSummaryPrefs;
    private int mCameraId;
    private CameraParameters mCamParams;
    private CameraWrapper mCamera;
    private Context mContext;
    CheckBoxPreference mEnableCustomStorageView;






    public static AdvancedCameraSettingsFragment newInstance(int cameraId)
    {
        AdvancedCameraSettingsFragment f = new AdvancedCameraSettingsFragment();

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

        getPreferenceManager().setSharedPreferencesName(AdvancedCameraPrefHelper.SHARED_PREFERENCES_NAME);
        addPreferencesFromResource(R.xml.pref_camera_advanced);


        mEnableCustomStorageView = (CheckBoxPreference) getPreferenceScreen().findPreference(AdvancedCameraPrefHelper.KEY_PREF_ENABLE_CUSTOM_STORAGE);
        mEnableCustomStorageView.setOnPreferenceClickListener(this);


        mAutoSummaryPrefs.add(AdvancedCameraPrefHelper.KEY_PREF_PREVIEW_SIZE);

        mAutoSummaryPrefs.add(AdvancedCameraPrefHelper.KEY_PREF_ENABLE_TOUCH_TO_fOCUS);
        mAutoSummaryPrefs.add(AdvancedCameraPrefHelper.KEY_PREF_CUSTOM_STORAGE);
        mAutoSummaryPrefs.add(AdvancedCameraPrefHelper.KEY_PREF_FLIP_H);
        mAutoSummaryPrefs.add(AdvancedCameraPrefHelper.KEY_PREF_FLIP_V);


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
        App.getAdvancedCameraPrefHelper().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

        try
        {
            mCamera = CameraWrapper.i();
            mCameraId = ((SettingsActivity) getActivity()).getCameraId();
            LogHelper.log(TAG, "mCameraId : " + mCameraId);
            mCamera.open(mCameraId);
            mCamParams = new CameraParameters();
            mCamParams.setCamera(mCameraId, mCamera);

            setupPreviewSizePreference();
            setupFlipPreference();


            if(!mCamParams.needsAutoFocusCapture())
            {
                Preference pref = findPreference(
                        AdvancedCameraPrefHelper.KEY_PREF_ENABLE_TOUCH_TO_fOCUS);
                if(pref!=null) getPreferenceScreen().removePreference(pref);
            }


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
        App.getAdvancedCameraPrefHelper().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        if(mCamera != null)  mCamera.close();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FILE_CODE && resultCode == Activity.RESULT_OK)
        {
            Uri uri = data.getData();
            File dir = new File(uri.getPath());
            if(dir.canWrite())
            {
                App.getAdvancedCameraPrefHelper().setCustomStorage(uri.getPath());
                onPrefChanged(AdvancedCameraPrefHelper.KEY_PREF_CUSTOM_STORAGE);
            }
            else
            {
                mEnableCustomStorageView.setChecked(false);
                MsgHelper.toast(getActivity(), R.string.alert_error_selectedFolderNotWritable);
            }
        }
        else
        {
            mEnableCustomStorageView.setChecked(false);
        }

    }

    //----------------------------------------------------------------------------------------------
    // onPrefChanged
    //----------------------------------------------------------------------------------------------
    public void onPrefChanged(String key)
    {

        if(mAutoSummaryPrefs.contains(key))
        {

            Preference pref = findPreference(key);
            App.getAdvancedCameraPrefHelper().updatePreferenceSummary(mContext, pref, key);
        }


        if(key.equals(AdvancedCameraPrefHelper.KEY_PREF_PREVIEW_SIZE))
        {
            App.getAdvancedCameraPrefHelper().setPreviewSize(mCameraId, App.getAdvancedCameraPrefHelper().getPreviewSize());
        }
        /*/else if(key.equals(AdvancedCameraPrefHelper.KEY_PREF_ISO))
        {
            App.getAdvancedCameraPrefHelper().setIso(mCameraId, App.getAdvancedCameraPrefHelper().getIso());
        }*/
        else if(key.equals(AdvancedCameraPrefHelper.KEY_PREF_CUSTOM_STORAGE))
        {
            this.mEnableCustomStorageView.setSummary(App.getAdvancedCameraPrefHelper().getCustomStorage());
        }
        else if(key.equals(AdvancedCameraPrefHelper.KEY_PREF_FLIP_H))
        {
            App.getAdvancedCameraPrefHelper().setFlipH(mCameraId, App.getAdvancedCameraPrefHelper().getFlipH());
        }
        else if(key.equals(AdvancedCameraPrefHelper.KEY_PREF_FLIP_V))
        {
            App.getAdvancedCameraPrefHelper().setFlipV(mCameraId, App.getAdvancedCameraPrefHelper().getFlipV());
        }

    }










    //==============================================================================================
    // Internals
    //==============================================================================================


    //----------------------------------------------------------------------------------------------
    // setupFlipPreference
    //----------------------------------------------------------------------------------------------
    private void setupFlipPreference()
    {

        CheckBoxPreference hflipPref = (CheckBoxPreference)findPreference(AdvancedCameraPrefHelper.KEY_PREF_FLIP_H);
        hflipPref.setChecked(App.getAdvancedCameraPrefHelper().getFlipH(mCameraId));


        CheckBoxPreference vflipPref = (CheckBoxPreference)findPreference(AdvancedCameraPrefHelper.KEY_PREF_FLIP_V);
        vflipPref.setChecked(App.getAdvancedCameraPrefHelper().getFlipV(mCameraId));


    }

    //----------------------------------------------------------------------------------------------
    // setupPictureSizePreference
    //----------------------------------------------------------------------------------------------
    private void setupPreviewSizePreference()
    {
        ListPreference pref = (ListPreference)findPreference(
                AdvancedCameraPrefHelper.KEY_PREF_PREVIEW_SIZE);

        List<Size> sizes = mCamParams.getSortedPreviewSizes();
        String[] entries = new String[sizes.size()];
        String[] entryValues = new String[sizes.size()];
        DecimalFormat  decimalFormat = new DecimalFormat("0.0");
        String unit = "M pixels";
        boolean swipeDimensions = false;
        if((mCamParams.getCameraOrientation() % 180) != 0) swipeDimensions = true;


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
            int selectedIndex = pref.findIndexOfValue(App.getAdvancedCameraPrefHelper().getPreviewSize(mCameraId).toString()) ;
            if(selectedIndex == -1)
            {
                App.getAdvancedCameraPrefHelper(). setPreviewSize(mCameraId, DefaultPrefs.PREVIEW_SIZE_UNSET);
                App.getAdvancedCameraPrefHelper().loadDefaultPreviewSizePref(mCamParams);
            }

            try
            {
                pref.setValueIndex(pref.findIndexOfValue(App.getAdvancedCameraPrefHelper().getPreviewSize(mCameraId).toString()));
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



    private void selectFodler()
    {
        Intent i = new Intent(getActivity(), FilePickerActivity.class);
        // This works if you defined the intent filter
        // Intent i = new Intent(Intent.ACTION_GET_CONTENT);

        // Set these depending on your use case. These are the defaults.
        i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
        i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, true);
        i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_DIR);

        // Configure initial directory by specifying a String.
        // You could specify a String like "/storage/emulated/0/", but that can
        // dangerous. Always use Android's API calls to get paths to the SD-card or
        // internal memory.
        String initialPath = App.getAdvancedCameraPrefHelper().getCustomStorage();

        if( !(new File(initialPath).exists()) )
        {
            initialPath = Environment.getExternalStorageDirectory().getPath();
        }
        i.putExtra(FilePickerActivity.EXTRA_START_PATH, initialPath);

        startActivityForResult(i, FILE_CODE);
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
        String key = preference.getKey();

        if(key.equals(AdvancedCameraPrefHelper.KEY_PREF_ENABLE_CUSTOM_STORAGE))
        {

            if(mEnableCustomStorageView.isChecked()) selectFodler();

        }

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

        onPrefChanged(key);

    }

}
