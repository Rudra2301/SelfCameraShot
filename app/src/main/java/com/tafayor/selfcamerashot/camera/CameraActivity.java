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




package com.tafayor.selfcamerashot.camera;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

import com.tafayor.selfcamerashot.App;
import com.tafayor.selfcamerashot.R;


import com.tafayor.selfcamerashot.devApps.AppInfo;
import com.tafayor.selfcamerashot.devApps.DevAppDialog;
import com.tafayor.selfcamerashot.interfaces.IWindowEvent;


import com.tafayor.selfcamerashot.pro.ProHelper;

import com.tafayor.selfcamerashot.pro.Upgrader;
import com.tafayor.selfcamerashot.taflib.helpers.AppHelper;
import com.tafayor.selfcamerashot.taflib.helpers.LogHelper;
import com.tafayor.selfcamerashot.taflib.helpers.MsgHelper;
import com.tafayor.selfcamerashot.taflib.helpers.ResHelper;
import com.tafayor.selfcamerashot.taflib.iab.UpgradeManager;
import com.tafayor.selfcamerashot.taflib.types.WeakArrayList;

import com.tafayor.selfcamerashot.ui.MsgDialog;
import com.tafayor.selfcamerashot.utils.Util;
 ;

import java.lang.ref.WeakReference;
import java.util.ArrayList;


public class CameraActivity extends AppCompatActivity implements IWindowEvent.Server
{


    public static String TAG = CameraActivity.class.getSimpleName();




    //Views
    Button startCameraBtn;
    private Context mContext;
    CameraFragment mCameraFragment;
    Upgrader mUpgrader;




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setTheme(R.style.AppTheme_Light_CameraPreview);

        super.onCreate(savedInstanceState);



        mContext = getApplicationContext();

       init();

    }


    void init()
    {

        setContentView(R.layout.activity_camera);

        mUpgrader = new Upgrader(this);
        ProHelper.applyProState(App.getPrefHelper().getIsAppUpgraded());

        initActionbar();



        if(App.getPrefHelper().getUiFirstTime())
        {
            runUiFirstTimeTasks();
            App.getPrefHelper().setUiFirstTime(false);
        }


    }







    //==============================================================================================
    // Interface
    //==============================================================================================



    public Upgrader getUpgrader()
    {
        return mUpgrader;
    }


    //==============================================================================================
    // Callbacks
    //==============================================================================================




    @Override
    protected void onResume()
    {
        super.onResume();
        mUpgrader.setup();



    }


    @Override
    protected void onStop() {
        super.onStop();
        mUpgrader.release();
    }

    @Override
    protected void onResumeFragments()
    {
        super.onResumeFragments();


    }

    @Override
    protected void onPause()
    {
        super.onPause();

        App.getPrefHelper().setUiFirstTime(false);


    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev)
    {

        notifyWindowEventListeners(ev);

        return super.dispatchTouchEvent(ev);

    }






    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (!mUpgrader.handleActivityResult(requestCode, resultCode, data))
        {
            super.onActivityResult(requestCode, resultCode, data);
        }
        else LogHelper.log(TAG, "IabHelper handleActivityResult");

    }


    //==============================================================================================
    // Listeners
    //==============================================================================================

    //----------------------------------------------------------------------------------------------
    // notifyRunningAppStateListeners
    //----------------------------------------------------------------------------------------------
    public void notifyWindowEventListeners(MotionEvent ev)
    {
        for(IWindowEvent.Client listener : mWindowEventListeners)
        {
            listener.onWindowTouchEvent(ev);
        }
    }




    //==============================================================================================
    // Internals
    //==============================================================================================

    private void initActionbar() {

       /* int navColor = GraphicsHelper.setColorAlpha(getResources().
                getColor(R.color.camera_navigationBar_color), UiValues.TRASPARENT_VIEW_ALPHA);*/



    }

    private void runUiFirstTimeTasks()
    {

       MsgDialog dialog = MsgDialog.getInstance(getResources().getString(R.string.alert_welcomeDialog_title),
                getResources().getString(R.string.alert_welcomeDialog_Message));
        dialog.show(getSupportFragmentManager(), null);

    }



    //----------------------------------------------------------------------------------------------
    // hasLightSensor
    //----------------------------------------------------------------------------------------------
    public  boolean  hasLightSensor()
    {
        boolean ret = false;
        try
        {
            SensorManager sensormanager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            ret = (sensormanager.getDefaultSensor(Sensor.TYPE_LIGHT) != null);
        }
        catch (Exception e)
        {
            LogHelper.logx( e);
        }

        return ret;
    }




    //==============================================================================================
    // Implementation
    //==============================================================================================


    //==================================
    // IWindowEvent.Server
    //==================================

    private WeakArrayList<IWindowEvent.Client> mWindowEventListeners = new WeakArrayList<>();

    @Override
    public void addWindowEventListener(IWindowEvent.Client listener)
    {
        mWindowEventListeners.addUnique(listener);
    }

    @Override
    public void removeWindowEventListener(IWindowEvent.Client listener)
    {
        mWindowEventListeners.remove(listener);
    }









}
