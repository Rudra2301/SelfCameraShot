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
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.tafayor.selfcamerashot.App;

import com.tafayor.selfcamerashot.R;
import com.tafayor.selfcamerashot.taflib.helpers.AppHelper;
import com.tafayor.selfcamerashot.taflib.helpers.LogHelper;
import com.tafayor.selfcamerashot.taflib.helpers.ViewHelper;


import java.util.ArrayList;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ActivatorsSettingsFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener,
        SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceChangeListener
{
    public static String TAG = ActivatorsSettingsFragment.class.getSimpleName();







    private ArrayList<String> mAutoSummaryPrefs;
    private Context mContext;


    public static ActivatorsSettingsFragment newInstance(int cameraId)
    {
        ActivatorsSettingsFragment f = new ActivatorsSettingsFragment();

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

        getPreferenceManager().setSharedPreferencesName(ActivatorsPrefHelper.SHARED_PREFERENCES_NAME);
        addPreferencesFromResource(R.xml.pref_activators);





        mAutoSummaryPrefs.add(ActivatorsPrefHelper.KEY_PREF_ACTIVATOR_VOLUME_BUTTONS);







        for(String key : mAutoSummaryPrefs)
        {
            onPrefChanged(key);
        }


    }











    //==============================================================================================
    // Callbacks
    //==============================================================================================


    @Override
    public void onResume()
    {
        super.onResume();
        App.getActivatorsPrefHelper().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);


    }


    @Override
    public void onPause()
    {
        super.onPause();
        App.getActivatorsPrefHelper().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }








    //----------------------------------------------------------------------------------------------
    // onPrefChanged
    //----------------------------------------------------------------------------------------------
    public void onPrefChanged(String key)
    {

        if(mAutoSummaryPrefs.contains(key))
        {

            Preference pref = findPreference(key);
            App.getActivatorsPrefHelper().updatePreferenceSummary(mContext,pref, key);
        }



    }







    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ViewHelper.fixViewGroupRtl(mContext, getView());

    }



    //==============================================================================================
    // Internals
    //==============================================================================================






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

        onPrefChanged(key);

    }

}
