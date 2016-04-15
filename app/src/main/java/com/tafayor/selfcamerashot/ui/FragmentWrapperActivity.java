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


package com.tafayor.selfcamerashot.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.tafayor.selfcamerashot.R;
import com.tafayor.selfcamerashot.pro.Upgrader;
import com.tafayor.selfcamerashot.taflib.helpers.LogHelper;
import com.tafayor.selfcamerashot.taflib.helpers.MarketHelper;
import com.tafayor.selfcamerashot.taflib.helpers.ThemeHelper;


public class FragmentWrapperActivity extends ActionBarActivity
{
    public static String TAG = FragmentWrapperActivity.class.getSimpleName();



    public static String KEY_FRAGMENT = "keyFragment";

    public static Integer FRAGMENT_ABOUT = 1;



    private Context mContext ;
    Upgrader mUpgrader;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();

        setContentView(R.layout.activity_fragment_wrapper);


        mUpgrader = new Upgrader(this);
        Intent intent = getIntent();
        int fragId = 0;

        fragId = intent.getIntExtra(KEY_FRAGMENT, 0);
        if(fragId >0)
        {
            setupActionbar();
            loadFragment(fragId);
        }
        else
        {
            finish();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (!mUpgrader.handleActivityResult(requestCode, resultCode, data))
        {
            super.onActivityResult(requestCode, resultCode, data);
        }
        else LogHelper.log(TAG, "IabHelper handleActivityResult");

    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }






    //==============================================================================================
    // Internals
    //=============================================================================================



    private void setupActionbar()
    {


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        actionBar.setLogo(R.drawable.ic_launcher);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle(getResources().getString(R.string.app_name));


        // mToolBar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



        int tintColor = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
        {
            tintColor = ThemeHelper.getColor(this, R.attr.colorPrimary);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);

            tintManager.setStatusBarTintColor(tintColor);
        }
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            tintColor = ThemeHelper.getColor(this, R.attr.colorPrimaryDark);
            getWindow().setStatusBarColor(tintColor);
        }

    }

    private void loadFragment(int id )
    {

        Fragment fragment = new Fragment();
        if(id == FRAGMENT_ABOUT)
        {

            fragment = new AboutFragment();
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_container, fragment).commit();
    }









    //==============================================================================================
    // Implementation
    //==============================================================================================








    //==============================================================================================
    // Types
    //==============================================================================================










}
