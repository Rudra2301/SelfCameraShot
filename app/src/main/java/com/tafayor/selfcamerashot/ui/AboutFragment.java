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
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tafayor.selfcamerashot.App;
import com.tafayor.selfcamerashot.R;
import com.tafayor.selfcamerashot.taflib.helpers.AppHelper;
import com.tafayor.selfcamerashot.taflib.helpers.FeedbackHelper;
import com.tafayor.selfcamerashot.taflib.helpers.IntentHelper;
import com.tafayor.selfcamerashot.taflib.helpers.MarketHelper;
import com.tafayor.selfcamerashot.taflib.helpers.ResHelper;
import com.tafayor.selfcamerashot.taflib.helpers.ViewHelper;
import com.tafayor.selfcamerashot.taflib.ui.windows.WebDialog;


import java.lang.ref.WeakReference;


/**
 * Created by youssef on 12/12/13.
 */
public class AboutFragment extends Fragment
{
    String TAG = AboutFragment.class.getSimpleName();





    private Context mContext;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity().getApplicationContext();
        init();

    }



    private void init()
    {

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {

        setupActionBar();

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View view;
        view = inflater.inflate(R.layout.fragment_about, container, false);
        initView(view);
        ViewHelper.fixViewGroupRtl(mContext, view);
        return view;
    }

    private void initView(View view)
    {

        TextView appTitleView = (TextView)view.findViewById(R.id.app_title);
        Button upgradeBtn = (Button)view.findViewById(R.id.btn_upgrade);
        Button rateBtn = (Button)view.findViewById(R.id.btn_rate);
        Button sendEmailBtn = (Button)view.findViewById(R.id.btn_sendEmail);
        Button legalNoticesBtn = (Button)view.findViewById(R.id.btn_legalNotices);
        TextView versionView = (TextView)view.findViewById(R.id.tv_version);
        Button contributionsBtn = (Button)view.findViewById(R.id.btn_contributions);
        Button otherAppsBtn = (Button)view.findViewById(R.id.btn_otherApps);
        ImageView facebookBtn = (ImageView)view.findViewById(R.id.social_facebook);
        ImageView gplusBtn = (ImageView)view.findViewById(R.id.social_gplus);
        ImageView twitterBtn = (ImageView)view.findViewById(R.id.social_twitter);

        versionView.setText("v"+ AppHelper.getVersionName(mContext));

        if(App.isPro())
        {
            upgradeBtn.setVisibility(View.GONE);
            appTitleView.setText(getResources().getString(R.string.app_name_pro));
        }
        else
        {
            upgradeBtn.setVisibility(View.VISIBLE);
            appTitleView.setText(getResources().getString(R.string.app_name));
        }


        upgradeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FragmentWrapperActivity)getActivity()).getUpgrader().upgrade();
            }
        });


        rateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               MarketHelper.showProductPage(mContext);
            }
        });


        sendEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FeedbackHelper.showSendEmailView(mContext, App.VENDOR_EMAIL, App.getAppTitle());
            }
        });

        legalNoticesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLegalNotices();
            }
        });

        contributionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContributions();
            }
        });

        otherAppsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               MarketHelper.showVendorPage(mContext);
            }
        });


        facebookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentHelper.openURL(mContext, "www.facebook.com/tafayor");
            }
        });


        gplusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentHelper.openURL(mContext, "plus.google.com/114057134380222341582");
            }
        });


        twitterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentHelper.openURL(mContext, "twitter.com/tafayor");
            }
        });
    }


    //==============================================================================================
    // Interface
    //==============================================================================================






    //==============================================================================================
    // Callbacks
    //==============================================================================================




    //==============================================================================================
    // Internals
    //==============================================================================================

    private void showContributions()
    {
        String title = ResHelper.getResString(mContext, R.string.about_contributions_windowTitle);
        String path = "contributions.html";
        WebDialog dialog = new WebDialog(mContext, title, path, WebDialog.SRC_TYPE_ASSET);

        FragmentActivity activity = getActivity();
        if(activity != null) dialog.show(activity.getSupportFragmentManager());

    }


    private void showLegalNotices()
    {
        String title = ResHelper.getResString(mContext, R.string.about_legalNotices_windowTitle);
        String path = "licenses.html";
        WebDialog dialog = new WebDialog(mContext, title, path, WebDialog.SRC_TYPE_ASSET);

        FragmentActivity activity = getActivity();
        if(activity != null) dialog.show(activity.getSupportFragmentManager());

    }



    private void setupActionBar()
    {
        ActionBar actionBar = ((ActionBarActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.about_pageTitle));
        actionBar.setLogo(null);

    }





    //==============================================================================================
    // Implementation
    //==============================================================================================






}
