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


import com.tafayor.selfcamerashot.R;
import com.tafayor.selfcamerashot.taflib.helpers.AppHelper;
import com.tafayor.selfcamerashot.taflib.helpers.BasePrefsHelper;


import java.util.Arrays;




public class GeneralPrefHelper extends BasePrefsHelper
{
    public static String TAG = GeneralPrefHelper.class.getSimpleName() ;


    public static String SHARED_PREFERENCES_NAME = TAG;



    public static String KEY_PREF_LANGUAGE = "prefLanguage";
    public static String KEY_PREF_UPGRADE = "prefUpgrade";
    public static String KEY_PREF_DEFAULTS_LOADED = "prefDefaultsLoaded";






    private static GeneralPrefHelper sInstance;
    public static synchronized  GeneralPrefHelper i(Context ctx)
    {
        if(sInstance == null) sInstance = new GeneralPrefHelper(ctx);
        return sInstance;
    }

    public GeneralPrefHelper(Context context)
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

        setLanguage(getLanguage());

    }


    public String getDefaultLanguage()
    {
        String lang = AppHelper.getLanguage() ;
        String[] supportedLangs = mContext.getResources().getStringArray(R.array.prefLanguageValues);
        if(!Arrays.asList(supportedLangs).contains(lang)) lang = "en";
        return lang;
    }







    //==================================================================================================


    //***********

    public String getLanguage()
    {
        return mSharedPrefs.getString(KEY_PREF_LANGUAGE, getDefaultLanguage());
    }

    public void setLanguage(String value)
    {
        mPrefsEditor.putString(KEY_PREF_LANGUAGE, value);
        mPrefsEditor.commit();
    }

    //***********



}
