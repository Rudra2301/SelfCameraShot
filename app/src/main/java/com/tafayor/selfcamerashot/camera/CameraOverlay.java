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
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.tafayor.selfcamerashot.R;
import com.tafayor.selfcamerashot.taflib.helpers.LangHelper;
import com.tafayor.selfcamerashot.taflib.helpers.LogHelper;
import com.tafayor.selfcamerashot.taflib.helpers.ViewHelper;



public class CameraOverlay extends RelativeLayout {

    public static String TAG = CameraOverlay.class.getSimpleName();


    private Context mContext;


    private int mWidth = -1;
    private int mHeight = -1;
    private int mDiameter;


    private TextView mTimerView;
    private CountDownTimer mShotTimer;
    private Animation mCountdownAnimation;


    public CameraOverlay(Context context) {
        super(context);
        init(context);
    }

    public CameraOverlay(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CameraOverlay(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        mContext = context;
        loadDefaults();
        initGraphicTools();
        setWillNotDraw(false);

        setFocusable(false);
        setClickable(false);

        mTimerView = new TextView(mContext);
        mTimerView.setId(ViewHelper.generateViewId());
        mTimerView.setTextColor(mContext.getResources().getColor(R.color.camera_overlay_timerTextColor));
        mTimerView.setTextSize(mContext.getResources().getDimension(R.dimen.camera_overlay_timerTextSize));

        float shadowRadius = mContext.getResources().getDimension(R.dimen.camera_overlay_shadowRadius);
        mTimerView.setShadowLayer(shadowRadius, 0, 0, Color.BLACK);
        LayoutParams timerLParams = new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        timerLParams.addRule(CENTER_IN_PARENT, mTimerView.getId());
        addView(mTimerView, timerLParams);


        mCountdownAnimation = AnimationUtils.loadAnimation(mContext, R.anim.shot_countdown_scale_up);


    }


    private void loadDefaults() {


    }

    //==============================================================================================
    // Interface
    //==============================================================================================


    //----------------------------------------------------------------------------------------------
    // flashOn
    //----------------------------------------------------------------------------------------------
    public void flashOn() {

        setBackgroundColor(Color.WHITE);

    }


    //----------------------------------------------------------------------------------------------
    // flashOff
    //----------------------------------------------------------------------------------------------
    public void flashOff() {

        setBackgroundColor(Color.TRANSPARENT);
    }


    //----------------------------------------------------------------------------------------------
    // release
    //----------------------------------------------------------------------------------------------
    public void release() {

        stopShotCountdown();
    }


    //----------------------------------------------------------------------------------------------
    // startShotAnimation
    //----------------------------------------------------------------------------------------------
    public void startShotCountdown(final int seconds, final Runnable cb) {

        if (mShotTimer != null) mShotTimer.cancel();


        mShotTimer = new CountDownTimer((seconds) * 1000, 1000) {
            int lastSecondsLeft = 0;

            public void onTick(long ms) {

                int secondsLeft = Math.round(ms / 1000);
                mTimerView.startAnimation(mCountdownAnimation);
                mTimerView.setText("" + secondsLeft);


            }

            public void onFinish() {

                mTimerView.setText("");
                cb.run();

            }
        };


        mTimerView.startAnimation(mCountdownAnimation);
        mTimerView.setText("" + seconds);


        Runnable task = new Runnable() {
            @Override
            public void run() {
                LangHelper.sleep(1000);
                if (mShotTimer != null) mShotTimer.start();
            }
        };

        new Thread(task).start();

    }


    //----------------------------------------------------------------------------------------------
    // stopCountdownTimer
    //----------------------------------------------------------------------------------------------
    public synchronized void stopShotCountdown() {

        if (mShotTimer != null) {
            mShotTimer.cancel();
            mShotTimer = null;
            mTimerView.setText("");
        }

    }


    //==============================================================================================
    // Callbacks
    //==============================================================================================


    //---------------------------------------------------------------------------------------------
    // onDraw
    //---------------------------------------------------------------------------------------------
    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

    }

    //---------------------------------------------------------------------------------------------
    // onSizeChanged
    //---------------------------------------------------------------------------------------------
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        initSizes();

    }


    //==============================================================================================
    // Internals
    //==============================================================================================


    //----------------------------------------------------------------------------------------------
    // initGraphicTools
    //----------------------------------------------------------------------------------------------
    private void initGraphicTools() {


    }


    //---------------------------------------------------------------------------------------------
    // initSizes
    //---------------------------------------------------------------------------------------------
    protected void initSizes() {
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mDiameter = LangHelper.min(mWidth, mHeight);


    }


}
