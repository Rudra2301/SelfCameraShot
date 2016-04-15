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
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import com.tafayor.selfcamerashot.App;
import com.tafayor.selfcamerashot.R;
import com.tafayor.selfcamerashot.pro.ProHelper;
import com.tafayor.selfcamerashot.taflib.helpers.AppHelper;
import com.tafayor.selfcamerashot.taflib.helpers.LogHelper;
import com.tafayor.selfcamerashot.taflib.helpers.MarketHelper;
import com.tafayor.selfcamerashot.taflib.helpers.MsgHelper;
import com.tafayor.selfcamerashot.taflib.helpers.ResHelper;
import com.tafayor.selfcamerashot.taflib.helpers.ThemeHelper;
import com.tafayor.selfcamerashot.taflib.iab.UpgradeManager;
import com.tafayor.selfcamerashot.taflib.ruic.pref.preferenceActivity.HeaderInfo;
import com.tafayor.selfcamerashot.taflib.ruic.pref.preferenceActivity.MyPreferenceActivity;

import org.apmem.tools.layouts.FlowLayout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;




public class SettingsActivity extends MyPreferenceActivity
{

    private static String TAG = SettingsActivity.class.getSimpleName();


    public static String KEY_HEADER_GENERAL = "headerGeneral";
    public static String KEY_HEADER_CAMERA = "headerCamera";
    public static String KEY_HEADER_ADVANCED_CAMERA = "headerAdvancedCamera";
    public static String KEY_HEADER_REMOTE_CONTROL = "headerRemoteControl";
    public static String KEY_HEADER_UI = "headerUI";
    public static String KEY_HEADER_ACTIVATORS = "headerActivators";




    Context mContext;

    private Toolbar mToolBar;
    public static String KEY_CAMERA_ID = "keyCameraId";
    private int mCameraId = -1;



    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        mContext = getApplicationContext();

        AppHelper.setLocale(mContext, App.getGeneralPrefHelper().getLanguage());
        setContentView(R.layout.activity_settings);




        Intent intent = getIntent();
        mCameraId = intent.getIntExtra(KEY_CAMERA_ID, -1);


        setupActionBar();





    }

    void setupActionBar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //toolbar.setLogo(R.mipmap.ic_launcher);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setTitle(R.string.pref_windowTitle);




        SystemBarTintManager tintManager;
        int tintColor = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
        {
            tintColor = ThemeHelper.getColor(this, R.attr.colorPrimaryDark);
            tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);

            tintManager.setStatusBarTintColor(tintColor);
        }
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            tintColor = ThemeHelper.getColor(this, android.R.attr.colorPrimaryDark);//colorPrimaryDark
            getWindow().setStatusBarColor(tintColor);
        }


    }



    //==============================================================================================
    // Interface
    //==============================================================================================


    // called from fragments
    public int getCameraId()
    {

        return mCameraId;
    }





    //==============================================================================================
    // Callbacks
    //==============================================================================================


    @Override
    public List<HeaderInfo> onCreateHeaders()
    {
        List<HeaderInfo> list = new ArrayList<>();

        list.add(new HeaderInfo(mContext, KEY_HEADER_GENERAL,
                GeneralSettingsFragment.class,
                getResources().getString(R.string.pref_headerTitle_general),
                null));

        list.add(new HeaderInfo(mContext,KEY_HEADER_CAMERA,
                CameraSettingsFragment.class,
                getResources().getString(R.string.pref_headerTitle_mainCameraSettings),
                null ));

        list.add(new HeaderInfo(mContext,KEY_HEADER_ADVANCED_CAMERA,
                AdvancedCameraSettingsFragment.class,
                getResources().getString(R.string.pref_headerTitle_advancedCameraSettings),
                null ));

        list.add(new HeaderInfo(mContext,KEY_HEADER_REMOTE_CONTROL,
                RemoteControlSettingsFragment.class,
                getResources().getString(R.string.pref_headerTitle_remoteControl),
                null));

        list.add(new HeaderInfo(mContext,KEY_HEADER_UI,
                UiSettingsFragment.class,
                getResources().getString(R.string.pref_headerTitle_ui),
                null));

        list.add(new HeaderInfo(mContext,KEY_HEADER_ACTIVATORS,
                ActivatorsSettingsFragment.class,
                getResources().getString(R.string.pref_headerTitle_activators),
                null));

        return list;
    }




    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        // Allow super to try and create a view first
        final View result = super.onCreateView(name, context, attrs);
        if (result != null) {
            return result;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            // If we're running pre-L, we need to 'inject' our tint aware Views in place of the
            // standard framework versions
            switch (name) {
                case "EditText":
                    return new AppCompatEditText(this, attrs);
                case "Spinner":
                    return new AppCompatSpinner(this, attrs);
                case "CheckBox":
                    return new AppCompatCheckBox(this, attrs);
                case "RadioButton":
                    return new AppCompatRadioButton(this, attrs);
                case "CheckedTextView":
                    return new AppCompatCheckedTextView(this, attrs);
            }
        }

        return null;
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_settings, menu);


        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

      int id = item.getItemId();

        if (id == R.id.home)
        {
            goToBackScreen();
            return true;
        }

        else if (id == R.id.action_moved) {
            MarketHelper.showProductPage(mContext, "com.tafayor.displaced");
            return true;
        }
        else if (id == R.id.action_screenshield)
        {
            MarketHelper.showProductPage(mContext, "com.tafayor.screenshield");
            return true;
        }
        else if (id == R.id.action_rapidos)
        {
            MarketHelper.showProductPage(mContext, "com.tafayor.rapidos");
            return true;
        }
        else if (id == R.id.action_tiltscroll)
        {
            MarketHelper.showProductPage(mContext, "com.tafayor.tiltscroll.free");
            return true;
        }

        return super.onOptionsItemSelected(item);


    }



    private void goToBackScreen()
    {
        onBackPressed();
    }







    @Override
    protected void onResume()
    {
         super.onResume();

    }




    @Override
    protected void onPause()
    {
        LogHelper.log(TAG, "onPause");

        super.onPause();
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();


    }






    //==============================================================================================
    // __Listeners
    //==============================================================================================


    //----------------------------------------------------------------------------------------------

    //==============================================================================================
    // __Implemetation
    //==============================================================================================







    //==============================================================================================
    //  Implementation
    //==============================================================================================



}