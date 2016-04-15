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



public class UiPrefHelper extends BasePrefsHelper
{
    public static String TAG = UiPrefHelper.class.getSimpleName() ;


    public static String SHARED_PREFERENCES_NAME = TAG;



    public static String KEY_PREF_SHOW_ZOOM_UI = "prefShowZoomUi";
    public static String KEY_PREF_DEFAULTS_LOADED = "prefDefaultsLoaded";
    public static String KEY_PREF_ENABLE_FULLSCREEN = "prefEnableFullscreen";



    private static UiPrefHelper sInstance;
    public static synchronized  UiPrefHelper i(Context ctx)
    {
        if(sInstance == null) sInstance = new UiPrefHelper(ctx);
        return sInstance;
    }


    public UiPrefHelper(Context context)
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

        setShowZoomUi(getShowZoomUi());
        setEnableFullscreen(DefaultPrefs.ENABLE_FULLSCREEN);

    }









    //==================================================================================================

    public boolean getEnableFullscreen()
    {
        return getBoolean(KEY_PREF_ENABLE_FULLSCREEN, DefaultPrefs.ENABLE_FULLSCREEN);
    }

    public void setEnableFullscreen(boolean value)
    {
        putBoolean(KEY_PREF_ENABLE_FULLSCREEN, value);
        commit();
    }



    //***********

    //***********

    public boolean getShowZoomUi()
    {
        return mSharedPrefs.getBoolean(KEY_PREF_SHOW_ZOOM_UI, DefaultPrefs.SHOW_ZOOM_UI);
    }

    public void setShowZoomUi(boolean value)
    {
        mPrefsEditor.putBoolean(KEY_PREF_SHOW_ZOOM_UI, value);
        mPrefsEditor.commit();
    }

    //***********



}
