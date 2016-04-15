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

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tafayor.selfcamerashot.R;
import com.tafayor.selfcamerashot.taflib.helpers.LogHelper;
import com.tafayor.selfcamerashot.taflib.helpers.ViewHelper;


import java.lang.ref.WeakReference;
import java.util.Objects;


public class ScreenFlash extends RelativeLayout
{

    public static String TAG = ScreenFlash.class.getSimpleName();







    private Context mContext;


    ImageView mIcon;
    WeakReference<Activity> mActivityPtr;
    Object mLock = new Object();
    boolean mIsShowing ;
    private static volatile HandlerThread mThread;
    private static volatile Handler mAsyncHandler;









    public ScreenFlash(Context context)
    {
        super(context);
        init(context);
    }

    public ScreenFlash(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ScreenFlash(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context)
    {
        mContext = context;
        loadDefaults();
        setWillNotDraw(false);
        setBackgroundColor(Color.WHITE);
        setVisibility(GONE);


        mIcon = new ImageView(mContext);
        mIcon.setScaleType(ImageView.ScaleType.FIT_CENTER);
        mIcon.setImageResource(R.drawable.ic_screen_flash);
        ViewHelper.setViewAlpha(mIcon, 0.2f);
        RelativeLayout.LayoutParams timerLParams = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        timerLParams.addRule(CENTER_IN_PARENT, mIcon.getId());
        addView(mIcon, timerLParams);



        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                flashOff();
            }
        });
    }


    private void loadDefaults()
    {
        mIsShowing = false;

    }

    //==============================================================================================
    // Interface
    //==============================================================================================

    //----------------------------------------------------------------------------------------------
    // setup
    //----------------------------------------------------------------------------------------------
    public void setup(Activity activity)
    {
        mActivityPtr = new WeakReference<Activity>(activity);

    }


    //----------------------------------------------------------------------------------------------
    // setup
    //----------------------------------------------------------------------------------------------
    public void release()
    {
        if(mActivityPtr == null) return;
        flashOff();
        mActivityPtr = null;


    }


    //----------------------------------------------------------------------------------------------
    // flashOn
    //----------------------------------------------------------------------------------------------
    public void flashOn(final Runnable cb)
    {
        if(mActivityPtr == null) return;
        Activity activity = mActivityPtr.get();
        if (activity == null) return;

        ViewHelper.addOnGlobalLayoutTask(this, new Runnable() {
            @Override
            public void run()
            {
                if(mActivityPtr==null) return;

                mAsyncHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(mActivityPtr==null) return;
                        if(cb != null) cb.run();
                    }
                },1000);

            }
        });

        setVisibility(VISIBLE);
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.screenBrightness =1.f;
        activity.getWindow().setAttributes(lp);

        activity = null;

    }




    //----------------------------------------------------------------------------------------------
    // flashOff
    //----------------------------------------------------------------------------------------------
    public void flashOff()
    {
        setVisibility(GONE);
        if(mActivityPtr == null) return;
        Activity activity = mActivityPtr.get();
        if (activity == null) return;

        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.screenBrightness = -1.f;
        activity.getWindow().setAttributes(lp);


    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        mThread = new HandlerThread("");
        mThread.start();
        mAsyncHandler = new Handler(mThread.getLooper());
    }

    @Override
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();

        try
        {
            if(mAsyncHandler!=null) mAsyncHandler.removeCallbacksAndMessages(null);
            if(mThread!=null) mThread.quit();
        }
        catch(Exception ex)
        {
            LogHelper.logx(ex);
        }

    }



    //==============================================================================================
    //
    //==============================================================================================





}
