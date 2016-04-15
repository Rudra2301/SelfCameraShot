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

import com.tafayor.selfcamerashot.taflib.helpers.BasePrefsHelper;




public class RemoteControlPrefHelper extends BasePrefsHelper
{
    public static String TAG = RemoteControlPrefHelper.class.getSimpleName() ;


    public static String SHARED_PREFERENCES_NAME = TAG;





    //Remote
    public static String KEY_PREF_REMOTE_MODE = "prefRemoteMode";
    public static String KEY_PREF_REMOTE_MODE_DELAY = "prefRemoteModeDelay";

    public static String KEY_PREF_WHISLTE_SENSITIVITY = "preWhistleSensitivity";
    public static String KEY_PREF_DEFAULTS_LOADED = "prefDefaultsLoaded";
    public static String KEY_PREF_CLAPPING_SENSITIVITY = "preClappingSensitivity";
    public static String KEY_PREF_SOUND_CONTROL_SENSITIVITY = "preSoundControlSensitivity";






    private static RemoteControlPrefHelper sInstance;
    public static synchronized  RemoteControlPrefHelper i(Context ctx)
    {
        if(sInstance == null) sInstance = new RemoteControlPrefHelper(ctx);
        return sInstance;
    }

    public RemoteControlPrefHelper(Context context)
    {
        super(context);
    }

    @Override
    protected String getSharedPreferencesName()
    {
        return SHARED_PREFERENCES_NAME;
    }


    //==================================================================================================

    public void loadDefaultPrefs() {

        setRemoteMode(getRemoteMode());
        setRemoteModeDelay(getRemoteModeDelay());
        setWhistleSensitivity(getWhistleSensitivity());
        setClappingSensitivity(getClappingSensitivity());
    }





    //==================================================================================================



//***********

    public int getClappingSensitivity()
    {
        return mSharedPrefs.getInt(KEY_PREF_CLAPPING_SENSITIVITY, DefaultPrefs.CLAPPING_SENSITIVITY);
    }

    public void setClappingSensitivity(int value)
    {
        mPrefsEditor.putInt(KEY_PREF_CLAPPING_SENSITIVITY, value);
        mPrefsEditor.commit();
    }



    //***********

    public int getWhistleSensitivity()
    {
        return mSharedPrefs.getInt(KEY_PREF_WHISLTE_SENSITIVITY, DefaultPrefs.WHISTLE_SENSITIVITY);
    }

    public void setWhistleSensitivity(int value)
    {
        mPrefsEditor.putInt(KEY_PREF_WHISLTE_SENSITIVITY, value);
        mPrefsEditor.commit();
    }


    //***********
    public int getSoundControlSensitivity()
    {
        return mSharedPrefs.getInt(KEY_PREF_SOUND_CONTROL_SENSITIVITY, DefaultPrefs.WHISTLE_SENSITIVITY);
    }

    public void setSoundControlSensitivity(int value)
    {
        mPrefsEditor.putInt(KEY_PREF_SOUND_CONTROL_SENSITIVITY, value);
        mPrefsEditor.commit();
    }

    //***********

    public int getRemoteModeDelay()
    {
        return mSharedPrefs.getInt(KEY_PREF_REMOTE_MODE_DELAY, DefaultPrefs.REMOTE_MODE_DELAY);
    }

    public void setRemoteModeDelay(int seconds)
    {
        mPrefsEditor.putInt(KEY_PREF_REMOTE_MODE_DELAY, seconds);
        mPrefsEditor.commit();
    }

    //***********

    public String getRemoteMode()
    {
        return mSharedPrefs.getString(KEY_PREF_REMOTE_MODE, DefaultPrefs.REMOTE_MODE);
    }

    public void setRemoteMode(String value)
    {
        mPrefsEditor.putString(KEY_PREF_REMOTE_MODE, value);
        mPrefsEditor.commit();
    }

    //***********












    //==============================================================================================
    // Implementation
    //==============================================================================================






}
