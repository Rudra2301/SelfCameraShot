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



package com.tafayor.selfcamerashot.taflib.ruic.pref.preferenceActivity;


import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;


import com.tafayor.selfcamerashot.R;
import com.tafayor.selfcamerashot.taflib.helpers.AppHelper;
import com.tafayor.selfcamerashot.taflib.helpers.LogHelper;
import com.tafayor.selfcamerashot.taflib.helpers.ViewHelper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MyPreferenceActivity extends AppCompatActivity implements Preference.OnPreferenceClickListener
{

    private static String TAG = MyPreferenceActivity.class.getSimpleName();


    private static String TAG__MENU_FRAGMENT = TAG+"menuFragment";
    public static String KEY_ACTIVE_FRAGMENT = "keyActiveFragment";
    public static String KEY_RESTART_REQUESTED = "keyRestartRequested";




    Context mContext;
    private String mTitle;
    private String mActiveHeader;
    private Map<String, HeaderInfo> mHeaders;
    private Handler mUiHandler;
    private boolean mRestartRequested = false;








    @Override
    public void onCreate(Bundle savedInstanceState)
    {


        super.onCreate(savedInstanceState);

        mContext = getApplicationContext();
        mHeaders = new HashMap<>();
        mUiHandler = new Handler();


        try
        {
            List<HeaderInfo> headers = onCreateHeaders();
            for(HeaderInfo header : headers)
            {
                mHeaders.put(header.key, header);
            }

            mRestartRequested = false;
            if(savedInstanceState != null && savedInstanceState.containsKey(KEY_RESTART_REQUESTED))
            {
                mRestartRequested = true;
            }
            if(savedInstanceState != null )
            {
                if(savedInstanceState.containsKey(KEY_ACTIVE_FRAGMENT))
                {
                    // restore header fragment
                    //--------
                    mActiveHeader = savedInstanceState.getString(KEY_ACTIVE_FRAGMENT);
                    if(mActiveHeader!=null) loadHeader(mActiveHeader, false);
                }


                // Update menu fragment
                //-------
                MenuPreferenceFragment menuFrag;
                menuFrag = (MenuPreferenceFragment) getFragmentManager().findFragmentByTag(TAG__MENU_FRAGMENT);
                if(menuFrag!=null) menuFrag.setListener(this);
            }
            else loadMenu();
        }
        catch (Exception e)
        {
            LogHelper.logx(e);
        }



    }




    //==============================================================================================
    // Protected
    //==============================================================================================

    public void requestRestart()
    {
        mRestartRequested = true;
        AppHelper.restartActivity(this);
    }

    public void setTitle(int resId)
    {
        mTitle = getResources().getString(resId);
    }


    protected void setTitle(String title)
    {
        mTitle = title;
    }




    //==============================================================================================
    // Callbacks
    //==============================================================================================







    @Override
    protected void onResume()
    {
        super.onResume();

        updateTitle();
    }

    @Override
    protected void onPause()
    {
        super.onPause();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        LogHelper.log(TAG, "onSaveInstanceState");
        if(mRestartRequested) outState.putString(KEY_RESTART_REQUESTED, "");

        Fragment f = getFragmentManager().findFragmentById(R.id.content);
        if(!(f instanceof  MenuPreferenceFragment))
        {
            outState.putString(KEY_ACTIVE_FRAGMENT, mActiveHeader);
        }
        super.onSaveInstanceState(outState);
    }




    //----------------------------------------------------------------------------------------------
    // onCreateHeaders
    //----------------------------------------------------------------------------------------------
    public List<HeaderInfo> onCreateHeaders()
    {
        return null;
    }


    //----------------------------------------------------------------------------------------------
    // onHeaderSelected
    //----------------------------------------------------------------------------------------------
    protected void onHeaderSelected(String key)
    {

    }


    //----------------------------------------------------------------------------------------------
    // onMenuVisible
    //----------------------------------------------------------------------------------------------
    protected void onMenuVisible()
    {

    }




    @Override
    public void onBackPressed()
    {
        try
        {
            // initialize variables
            FragmentManager fm = getFragmentManager();

            FragmentTransaction ft = fm.beginTransaction();


            //We use stackSize since getBackStackEntryCount is not updated immediately
            // I think ft.commit is asynchronous
            int stackSize = fm.getBackStackEntryCount();
            if (stackSize > 0)
            {
                Fragment frag = fm.findFragmentById(R.id.content);
                ft.remove(frag);
                fm.popBackStack();
                ft.commit();
                stackSize--;
                LogHelper.log("mRestartRequested : " + mRestartRequested);
                if(mRestartRequested)
                {

                    mUiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            LogHelper.log("restarting");
                            AppHelper.restartClearActivityOutside(MyPreferenceActivity.this);
                        }
                    });
                }
            }
            else
            {
                mRestartRequested = false;
                super.onBackPressed();
            }


            if(stackSize==0)
            {
                mActiveHeader = null;
                mUiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        onMenuVisible();
                    }
                });
            }

            updateTitle();
        }
        catch (Exception e)
        {
            LogHelper.logx(e);
        }



        //
    }



    //==============================================================================================
    // Internals
    //==============================================================================================






    //----------------------------------------------------------------------------------------------
    // updateTitle
    //----------------------------------------------------------------------------------------------
    private void updateTitle()
    {
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
        {
            if(mActiveHeader == null) actionBar.setTitle(mTitle);
            else
            {
                HeaderInfo header = mHeaders.get(mActiveHeader);
                actionBar.setTitle(header.title);
            }
        }


    }



    //----------------------------------------------------------------------------------------------
    // loadMenu
    //----------------------------------------------------------------------------------------------
    private  void loadMenu()
    {

        try
        {
            MenuPreferenceFragment menuFrag;
            menuFrag = (MenuPreferenceFragment) getFragmentManager().findFragmentByTag(TAG__MENU_FRAGMENT);
            if (menuFrag == null)
            {

                ArrayList<HeaderInfo> headers = (ArrayList) onCreateHeaders();
                menuFrag =  MenuPreferenceFragment.newInstance(headers);

            }
            menuFrag.setListener(this);





            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.content, menuFrag, TAG__MENU_FRAGMENT);
            ft.commit();


            mUiHandler.post(new Runnable() {
                @Override
                public void run() {
                    onMenuVisible();
                }
            });
        }
        catch (Exception e)
        {
            LogHelper.logx(e);
        }

    }





    //----------------------------------------------------------------------------------------------
    // loadHeader
    //----------------------------------------------------------------------------------------------

    private void loadHeader(final String key, boolean backstack)
    {
        Fragment fragment = null;

        HeaderInfo header = mHeaders.get(key);

        try
        {

            fragment = getFragmentManager().findFragmentByTag(key);
            if(fragment == null)
            {
                fragment = (Fragment) header.clazz.newInstance();
            }
            else
            {
                //if (getFragmentManager().getBackStackEntryCount() > 0) getFragmentManager().popBackStack();
            }

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.content, fragment, key);
            if(backstack) ft.addToBackStack(null);
            ft.commit();
            mActiveHeader = key;

            updateTitle();

            mUiHandler.post(new Runnable() {
                @Override
                public void run() {
                    onHeaderSelected(key);
                }
            });

        } catch (Exception e)
        {
            LogHelper.logx(e);
        }


    }


    //==============================================================================================
    // __Implemetation
    //==============================================================================================



    //=======================================
    // Preference.OnPreferenceClickListener
    //=======================================

    @Override
    public boolean onPreferenceClick(Preference preference)
    {
        String key = preference.getKey();
        loadHeader(key, true);
        return false;
    }



    //=======================================
    // ListPreferenceFragment
    //=======================================

    public static class  MenuPreferenceFragment extends PreferenceFragment implements  Preference.OnPreferenceClickListener
    {

        public static String KEY_HEADERS  = "keyHeaders";

        ArrayList<HeaderInfo> mHeaders;
        WeakReference<Preference.OnPreferenceClickListener> mClickListener;


        public static MenuPreferenceFragment newInstance(ArrayList<HeaderInfo> headers)
        {

            Bundle args = new Bundle();
            args.putParcelableArrayList(KEY_HEADERS, headers);
            MenuPreferenceFragment fragment = new MenuPreferenceFragment();
            fragment.setArguments(args);
            return fragment;
        }


        public MenuPreferenceFragment()
        {

        }



        public void setListener(Preference.OnPreferenceClickListener listener)
        {

            mClickListener = new WeakReference<Preference.OnPreferenceClickListener>(listener);
        }



        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);

            Bundle args = getArguments();
            if(args != null  && args.containsKey(KEY_HEADERS))
            {
                LogHelper.log("restoring mHeaders");
                mHeaders = args.getParcelableArrayList(KEY_HEADERS);
            }

            //setRetainInstance(true);
            PreferenceScreen prefScreen = getPreferenceManager().createPreferenceScreen(getActivity());
            setPreferenceScreen(prefScreen);

            if(mHeaders != null)
            {
                for(HeaderInfo header : mHeaders)
                {
                    Preference pref = new Preference(getActivity(), null);
                    pref.setKey(header.key);
                    pref.setTitle(header.title);
                    pref.setIcon(header.icon);
                    pref.setOnPreferenceClickListener(this);
                    getPreferenceScreen().addPreference(pref);
                }
            }
            else LogHelper.logx(new Exception("mHeaders is null"));

        }

        @Override
        public boolean onPreferenceClick(Preference preference)
        {
            Preference.OnPreferenceClickListener listener;
            if(mClickListener!=null)
            {
                listener = mClickListener.get();
                if(listener != null) return listener.onPreferenceClick(preference);
            }
            else LogHelper.logx(new Exception("mClickListener is null"));



            return false;
        }




        @Override
        public void onActivityCreated(Bundle savedInstanceState)
        {
            super.onActivityCreated(savedInstanceState);
            ViewHelper.fixViewGroupRtl(getActivity(), getView());



        }

    }












}