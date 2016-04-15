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
import com.tafayor.selfcamerashot.taflib.helpers.BasePrefsHelper;
import com.tafayor.selfcamerashot.taflib.helpers.ViewHelper;
import com.tafayor.selfcamerashot.taflib.ui.custom.CustomListPreference;


import java.util.ArrayList;
import java.util.List;



@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class RemoteControlSettingsFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener,
        SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceChangeListener
{
    public static String TAG = RemoteControlSettingsFragment.class.getSimpleName();







    private ArrayList<String> mAutoSummaryPrefs;
    private Context mContext;






    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mContext = getActivity().getApplicationContext();
        mAutoSummaryPrefs = new ArrayList<>();



        AppHelper.setLocale(mContext, App.getGeneralPrefHelper().getLanguage());

        getPreferenceManager().setSharedPreferencesName(RemoteControlPrefHelper.SHARED_PREFERENCES_NAME);
        addPreferencesFromResource(R.xml.pref_remote_control);




        mAutoSummaryPrefs.add(RemoteControlPrefHelper.KEY_PREF_REMOTE_MODE);



        if(!App.isPro())
        {

            CustomListPreference captureCmdsPref = (CustomListPreference)findPreference(
                    RemoteControlPrefHelper.KEY_PREF_REMOTE_MODE);
            String[] labels = getResources().getStringArray(R.array.prefRemoteModeLabels);
            String[] values = getResources().getStringArray(R.array.prefRemoteModeValues);
            List<String> proItems = new ArrayList<>();
            proItems.add(RemoteModeValues.CLAPPING);


            BasePrefsHelper.markItemsAsPro(captureCmdsPref, labels, values, proItems, " (Pro)");


        }



        for(String key : mAutoSummaryPrefs)
        {
            onPrefChanged(key);
        }

        onPrefChanged(RemoteControlPrefHelper.KEY_PREF_REMOTE_MODE_DELAY);
        onPrefChanged(RemoteControlPrefHelper.KEY_PREF_WHISLTE_SENSITIVITY);
        onPrefChanged(RemoteControlPrefHelper.KEY_PREF_CLAPPING_SENSITIVITY);
    }











    //==============================================================================================
    // Callbacks
    //==============================================================================================


    @Override
    public void onResume()
    {
        super.onResume();
        App.getRemoteControlPrefHelper().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);


    }


    @Override
    public void onPause()
    {
        super.onPause();
        App.getRemoteControlPrefHelper().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }








    //----------------------------------------------------------------------------------------------
    // onPrefChanged
    //----------------------------------------------------------------------------------------------
    public void onPrefChanged(String key)
    {
        Preference pref;

        if(mAutoSummaryPrefs.contains(key))
        {

            pref = getPreferenceScreen().findPreference(key);
            App.getRemoteControlPrefHelper().updatePreferenceSummary(mContext,pref, key);
        }


        if (key.equals(RemoteControlPrefHelper.KEY_PREF_REMOTE_MODE_DELAY))
        {
            pref = findPreference(RemoteControlPrefHelper.KEY_PREF_REMOTE_MODE_DELAY);
            String summary ;
            int delay = App.getRemoteControlPrefHelper().getRemoteModeDelay();
            summary = getResources().getString(R.string.pref_remoteModeDelay_formattedValue, delay);
            pref.setSummary(summary);
        }
        else if (key.equals(RemoteControlPrefHelper.KEY_PREF_WHISLTE_SENSITIVITY))
        {
            pref = findPreference(key);
            String summary = "/100" ;
            int value = App.getRemoteControlPrefHelper().getWhistleSensitivity();
            summary = value + summary;
            pref.setSummary(summary);
        }
        else if (key.equals(RemoteControlPrefHelper.KEY_PREF_CLAPPING_SENSITIVITY))
        {
            pref = findPreference(key);
            String summary = "/100" ;
            int value = App.getRemoteControlPrefHelper().getClappingSensitivity();
            summary = value + summary;
            pref.setSummary(summary);
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
