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


package com.tafayor.selfcamerashot.taflib.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;


import com.tafayor.selfcamerashot.R;
import com.tafayor.selfcamerashot.taflib.types.WeakArrayList;
import com.tafayor.selfcamerashot.taflib.ui.components.SeekBarPreference;
import com.tafayor.selfcamerashot.taflib.ui.custom.CustomListPreference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;




abstract public class BasePrefsHelper implements  SharedPreferences.OnSharedPreferenceChangeListener
{
    public static String TAG = BasePrefsHelper.class.getSimpleName() ;










    protected Context mContext;
    protected SharedPreferences mSharedPrefs;
    protected SharedPreferences.Editor mPrefsEditor;
    private WeakArrayList<PrefsListener> mPrefsListeners;





    protected BasePrefsHelper()
    {

    }

    public BasePrefsHelper(Context context)
    {
        mContext = context;
        init();
    }


    protected void init()
    {

        mSharedPrefs = mContext.getSharedPreferences(getSharedPreferencesName(),Context.MODE_PRIVATE);

        //mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        mPrefsEditor = mSharedPrefs.edit();
        mPrefsListeners = new WeakArrayList<PrefsListener>();
        mSharedPrefs.registerOnSharedPreferenceChangeListener(this);
    }


    protected Context getContext(){return mContext;}

    //----------------------------------------------------------------------------------------------
    // markItemsAsPro
    //----------------------------------------------------------------------------------------------
    public static void markItemsAsPro(ListPreference listPref, String[] labels,
                                    String[] values , List<String> proItems, String proWord)
    {

        List<String> labelsList = new ArrayList<String>(Arrays.asList(labels));
        List<String> valuesList = new ArrayList<String>(Arrays.asList(values));

        Iterator<String> it = valuesList.iterator();
        for(int i = 0; i<valuesList.size(); i++)
        {
            String value = valuesList.get(i);
            String label = labelsList.get(i);
            if(proItems.contains(value))
            {
                labelsList.set(i, label + " " + proWord);
            }
        }

        listPref.setEntries(labelsList.toArray(new String[1]));
        listPref.setEntryValues(valuesList.toArray(new String[1]));

    }



    //----------------------------------------------------------------------------------------------
    // removeListPrefItems
    //----------------------------------------------------------------------------------------------
    public static void removeListPrefItems(ListPreference listPref, String[] labels,
                                    String[] values , List<String> items)
    {

        List<String> labelsList = new ArrayList<String>(Arrays.asList(labels));
        List<String> valuesList = new ArrayList<String>(Arrays.asList(values));

        Iterator<String> it = valuesList.iterator();
        while(it.hasNext())
        {
            String value = it.next();
            if(items.contains(value))
            {
                labelsList.remove(valuesList.indexOf(value));
                it.remove();
            }
        }

        listPref.setEntries(labelsList.toArray(new String[1]));
        listPref.setEntryValues(valuesList.toArray(new String[1]));

    }




    abstract protected String getSharedPreferencesName() ;

    public SharedPreferences getSharedPreferences()
    {
        return mSharedPrefs;
    }
    public SharedPreferences.Editor getEditor() {
        return mPrefsEditor;
    }


    public boolean contains(String tag, String subTag)
    {
        return mSharedPrefs.contains(tag+":"+subTag);
    }


    public boolean contains(String tag)
    {
        return mSharedPrefs.contains(tag);
    }


    public void commit()
    {
        mPrefsEditor.commit();
    }


    public int getInt(String tag , int defVal)
    {
        return mSharedPrefs.getInt(tag, defVal);
    }

    public void putInt(String tag , int val)
    {
        mPrefsEditor.putInt(tag, val);
        mPrefsEditor.commit();
    }

    public int getInt(String tag, String subTag, int defVal)
    {
        return mSharedPrefs.getInt(tag+":"+subTag, defVal);
    }

    public void putInt(String tag, String subTag, int val)
    {
        mPrefsEditor.putInt(tag+":"+subTag, val);
        mPrefsEditor.commit();
    }


    public long getLong(String tag, long defVal)
    {
        return mSharedPrefs.getLong(tag, defVal);
    }

    public void putLong(String tag, long val)
    {
        mPrefsEditor.putLong(tag, val);
        mPrefsEditor.commit();
    }


    public float getFloat(String tag, float defVal)
    {
        return mSharedPrefs.getFloat(tag, defVal);
    }

    public void putFloat(String tag, float val)
    {
        mPrefsEditor.putFloat(tag, val);
        mPrefsEditor.commit();
    }

    public float getFloat(String tag, String subTag, float defVal)
    {
        return mSharedPrefs.getFloat(tag+":"+subTag, defVal);
    }

    public void putFloat(String tag, String subTag, float val)
    {
        mPrefsEditor.putFloat(tag+":"+subTag, val);
        mPrefsEditor.commit();
    }

    public boolean getBoolean(String key,  boolean defVal)
    {
        return mSharedPrefs.getBoolean(key , defVal);
    }

    public void putBoolean(String key, boolean val)
    {
        mPrefsEditor.putBoolean(key , val);
        mPrefsEditor.commit();
    }


    public boolean getBoolean(String tag, String subTag, boolean defVal)
    {
        return mSharedPrefs.getBoolean(tag + ":" + subTag, defVal);
    }


    public void putBoolean(String tag, String subTag, boolean val)
    {
        mPrefsEditor.putBoolean(tag + ":" + subTag, val);
        mPrefsEditor.commit();
    }

    public String getString(String tag, String defVal)
    {
        return mSharedPrefs.getString(tag, defVal);
    }

    public void putString(String tag, String val)
    {
        mPrefsEditor.putString(tag, val);
        mPrefsEditor.commit();
    }


    public String getString(String tag, String subTag, String defVal)
    {
        return mSharedPrefs.getString(tag + ":" + subTag, defVal);
    }

    public void putString(String tag, String subTag, String val)
    {
        mPrefsEditor.putString(tag + ":" + subTag, val);
        mPrefsEditor.commit();
    }


    public String toString()
    {
        String output = "Shared preferences (" + getSharedPreferencesName() +")" + "\n";
        Map<String, ?> allEntries = mSharedPrefs.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet())
        {
            output += ( entry.getKey() + " : " + entry.getValue().toString()) + "\n";
        }

        return output;
    }





    //==============================================================================================
    // Interface
    //==============================================================================================


    public void bindPreferenceToChangeListener(Preference preference, Preference.OnPreferenceChangeListener listener)
    {
        preference.setOnPreferenceChangeListener(listener);

        if (preference instanceof SeekBarPreference)
        {
            listener.onPreferenceChange(
                    preference,
                    PreferenceManager.getDefaultSharedPreferences(preference.getContext()).getInt(preference.getKey(), 0)
            );
        }
        else if (preference instanceof CheckBoxPreference)
        {

            listener.onPreferenceChange(
                    preference,
                    PreferenceManager.getDefaultSharedPreferences(preference.getContext()).getBoolean(preference.getKey(), true)
            );
        }
        else
        {
            listener.onPreferenceChange(
                    preference,
                    PreferenceManager.getDefaultSharedPreferences(preference.getContext()).getString(preference.getKey(), "")
            );
        }


    }


    public boolean updatePreferenceSummary(Context ctx, Preference preference, String key)
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
        else if(preference instanceof CheckBoxPreference || preference instanceof SwitchPreference)
        {

            boolean boolValue = mSharedPrefs.getBoolean(key, false);
            String summary, choice;
            choice = "";
            if (boolValue)
                choice = ResHelper.getResString(ctx, R.string.yes);
            else
                choice = ResHelper.getResString(ctx, R.string.no);

            summary = choice ;
            preference.setSummary(summary);
        }

        return true;

    }

    //==============================================================================================
    // Listeners
    //==============================================================================================


    public void addPrefsListener(PrefsListener listener)
    {
        mPrefsListeners.addUnique(listener);
    }

    public void removePrefsListener(PrefsListener listener)
    {
        mPrefsListeners.remove(listener);
    }

    public void notifyListeners(final String key )
    {
        for(PrefsListener listener : mPrefsListeners)
        {
            listener.onPrefChanged(key);
        }

    }


    //==============================================================================================
    // Implementation
    //==============================================================================================

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {

        notifyListeners(key);
    }





    //==============================================================================================
    // Types
    //==============================================================================================


    static public class PrefsListener
    {
        public void onPrefChanged(String key){}
    }
}
