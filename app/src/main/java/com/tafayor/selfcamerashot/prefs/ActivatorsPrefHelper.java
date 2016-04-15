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




public class ActivatorsPrefHelper extends BasePrefsHelper
{
    public static String TAG = ActivatorsPrefHelper.class.getSimpleName() ;


    public static String SHARED_PREFERENCES_NAME = TAG;




    //Activators
    public static String KEY_PREF_ACTIVATOR_VOLUME_BUTTONS = "prefActivatorVolumeButtons";
    public static String KEY_PREF_DEFAULTS_LOADED = "prefDefaultsLoaded";




    private static ActivatorsPrefHelper sInstance;
    public static synchronized  ActivatorsPrefHelper i(Context ctx)
    {
        if(sInstance == null) sInstance = new ActivatorsPrefHelper(ctx);
        return sInstance;
    }

    public ActivatorsPrefHelper(Context context)
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

            setVolumeButtonsActivator(getVolumeButtonsActivator());



    }






    //==================================================================================================


//***********

    public String getVolumeButtonsActivator()
    {
        return mSharedPrefs.getString(KEY_PREF_ACTIVATOR_VOLUME_BUTTONS, DefaultPrefs.ACTIVATOR_VOLUME_BUTTONS);
    }

    public void setVolumeButtonsActivator(String value)
    {
        mPrefsEditor.putString(KEY_PREF_ACTIVATOR_VOLUME_BUTTONS, value);
        mPrefsEditor.commit();
    }








    //==============================================================================================
    // Implementation
    //==============================================================================================






}
