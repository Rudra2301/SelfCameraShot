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


import com.tafayor.selfcamerashot.App;
import com.tafayor.selfcamerashot.taflib.helpers.AppHelper;
import com.tafayor.selfcamerashot.taflib.helpers.BasePrefsHelper;



public class PrefHelper extends BasePrefsHelper
{
    public static String TAG = PrefHelper.class.getSimpleName() ;


    public static String SHARED_PREFERENCES_NAME = TAG;



    public static String KEY_PREF_FIRST_TIME = "prefFirstTime";
    public static String KEY_PREF_APP_UPGRADED = "prefAppUpgraded";
    public static String KEY_PREF_UI_FIRST_TIME = "prefUiFirstTime";
    public static String KEY_PREF_VERSION_CODE = "prefVersionCode";
    public static String KEY_PREF_DEVAPP_PREV_SHOW_DATE = "prefDevappPevShowDate";
    public static String KEY_PREF_DEVAPP_SHOWN  = "prefDevappShown";





    private static PrefHelper sInstance;
    public static synchronized  PrefHelper i(Context ctx)
    {
        if(sInstance == null) sInstance = new PrefHelper(ctx);
        return sInstance;
    }




    public PrefHelper(Context context)
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


        setIsAppUpgraded(false);
        setVersionCode(AppHelper.getVersionCode(mContext));

        App.getActivatorsPrefHelper().loadDefaultPrefs();
        App.getAdvancedCameraPrefHelper().loadDefaultPrefs();
        App.getCameraPrefHelper().loadDefaultPrefs();
        App.getGeneralPrefHelper().loadDefaultPrefs();
        App.getRemoteControlPrefHelper().loadDefaultPrefs();
        App.getUiPrefHelper().loadDefaultPrefs();

    }










    //==================================================================================================



    //***********


    public long getDevAppPrevShowDate()
    {
        return getLong(KEY_PREF_DEVAPP_PREV_SHOW_DATE, 0);
    }

    public void setDevAppPrevShowDate(long value)
    {
        putLong(KEY_PREF_DEVAPP_PREV_SHOW_DATE, value);

    }


    //***********


    public boolean getDevAppShown(String pkg)
    {
        return getBoolean(KEY_PREF_DEVAPP_SHOWN, pkg, false);
    }

    public void setDevAppShown(String pkg, boolean value)
    {
        putBoolean(KEY_PREF_DEVAPP_SHOWN, pkg, value);

    }



    //***********

    public int getVersionCode()
    {
        return mSharedPrefs.getInt(KEY_PREF_VERSION_CODE, 0);
    }

    public void setVersionCode(int value)
    {
        mPrefsEditor.putInt(KEY_PREF_VERSION_CODE, value);
        mPrefsEditor.commit();
    }

    //***********

    public boolean getUiFirstTime()
    {
        return mSharedPrefs.getBoolean(KEY_PREF_UI_FIRST_TIME, true);
    }

    public void setUiFirstTime(boolean value)
    {
        mPrefsEditor.putBoolean(KEY_PREF_UI_FIRST_TIME, value);
        mPrefsEditor.commit();
    }

    //***********

    public boolean getIsAppUpgraded()
    {
        return mSharedPrefs.getBoolean(KEY_PREF_APP_UPGRADED, false);
    }

    public void setIsAppUpgraded(boolean value)
    {
        mPrefsEditor.putBoolean(KEY_PREF_APP_UPGRADED, value);
        mPrefsEditor.commit();
    }


    //***********

    public boolean getFirstTime()
    {
        return mSharedPrefs.getBoolean(KEY_PREF_FIRST_TIME, true);
    }

    public void setFirstTime(boolean value)
    {
        mPrefsEditor.putBoolean(KEY_PREF_FIRST_TIME, value);
        mPrefsEditor.commit();
    }


}
