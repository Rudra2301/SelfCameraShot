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
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;

import com.tafayor.selfcamerashot.R;
import com.tafayor.selfcamerashot.taflib.helpers.LangHelper;
import com.tafayor.selfcamerashot.taflib.helpers.ViewHelper;
import com.tafayor.selfcamerashot.taflib.types.Size;


public class FocusOverlay extends FrameLayout
{

    public static final String TAG = FocusOverlay.class.getSimpleName();


    private static float FOCUS_SIZE_FACTOR = 0.3f;

    //General
    private Point mFocusLocation;
    private FocusManager mFocusManager;

    //Views
    private FocusView mFocusView;

    //animations
    private ScaleAnimation mFocusAnim;

    //Params
    private int mFocusSize ;




    //==============================================================================================
    // Init
    //==============================================================================================


    public FocusOverlay(Context context) {
        super(context);
        init();
    }

    public FocusOverlay(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FocusOverlay(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    void init()
    {
        mFocusView = new FocusView(getContext());

        mFocusLocation = new Point();
        loadDefaults();
        setupAnimations();

        LayoutParams lprams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        addView(mFocusView, lprams);
        mFocusView.setVisibility(INVISIBLE);
    }




    private void loadDefaults()
    {
        mFocusLocation.x = -1;
        mFocusLocation.y = -1;
    }




    //==============================================================================================
    // Interface
    //==============================================================================================

    //------------------------------------------------------------------------------------------
    // setFocusManager
    //------------------------------------------------------------------------------------------
    public void setFocusManager(FocusManager manager)
    {
        mFocusManager = manager;
    }


    //------------------------------------------------------------------------------------------
    // getFocusWidth
    //------------------------------------------------------------------------------------------
    public int getFocusWidth()
    {
        return mFocusView.getWidth();
    }

    //------------------------------------------------------------------------------------------
    // getFocusHeight
    //------------------------------------------------------------------------------------------
    public int getFocusHeight()
    {
        return mFocusView.getHeight();
    }

    //------------------------------------------------------------------------------------------
    // setLocation
    //------------------------------------------------------------------------------------------
    public void setLocation(int x, int y)
    {




        if(x>0 && y>0)
        {
            mFocusLocation.x = x-mFocusSize/2;
            mFocusLocation.y = y-mFocusSize/2;
            ViewHelper.relocateViewInFrameLayout(mFocusView, mFocusLocation.x, mFocusLocation.y);
        }
        else if(getWidth() >0 && getHeight()>0)
        {

            LayoutParams lparams = (LayoutParams)mFocusView.getLayoutParams();
            lparams.gravity = Gravity.CENTER;
            lparams.setMargins(0,0,0,0);
            updateViewLayout(mFocusView, lparams);
            // x = getWidth()/2 - mFocusSize/2;
            // y = getHeight()/2 - mFocusSize/2;
            // ViewHelper.relocateViewInFrameLayout(mFocusView,x , y);

        }





    }




    //------------------------------------------------------------------------------------------
    // clearModes
    //------------------------------------------------------------------------------------------
    public void clearModes()
    {
        mFocusView.setVisibility(INVISIBLE);
        mFocusView.clearAnimation();
    }


    //------------------------------------------------------------------------------------------
    // selectFocusSearchMode
    //------------------------------------------------------------------------------------------
    public void selectFocusSearchMode()
    {
        mFocusView.setVisibility(VISIBLE);
        mFocusView.startAnimation(mFocusAnim);
        mFocusView.selectFocusSearchMode();
    }


    //------------------------------------------------------------------------------------------
    // selectFocusSearchMode
    //------------------------------------------------------------------------------------------
    public void selectFocusSuccessMode()
    {
        mFocusView.setVisibility(VISIBLE);
        mFocusView.selectFocusSuccessMode();
        mFocusView.clearAnimation();
    }


    //------------------------------------------------------------------------------------------
    // selectFocusSearchMode
    //------------------------------------------------------------------------------------------
    public void selectFocusFailureMode()
    {
        mFocusView.setVisibility(VISIBLE);
        mFocusView.selectFocusFailureMode();
        mFocusView.clearAnimation();
    }


    //------------------------------------------------------------------------------------------
    // startAnimation
    //------------------------------------------------------------------------------------------

    public void startAnimation(Animation animation)
    {
        mFocusView.startAnimation(animation);
    }

    //------------------------------------------------------------------------------------------
    // clearAnimation
    //------------------------------------------------------------------------------------------

    public void clearAnimation()
    {
        mFocusView.clearAnimation();
    }

    //==========================================================================================
    // Calbacks
    //==========================================================================================


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        mFocusSize = (int) (LangHelper.min(w, h) * FOCUS_SIZE_FACTOR);
        ViewHelper.resizeView(mFocusView, mFocusSize, mFocusSize);


        if(mFocusLocation.x == -1)
        {
            //setLocation(w/2,h/2);
            setLocation(-1,-1);
            mFocusLocation.x= -1;
        }


        if(mFocusManager!=null) mFocusManager.onPreviewSizeChanged(w, h);
    }







    //==========================================================================================
    // Internals
    //==========================================================================================

    //----------------------------------------------------------------------------------------------
    // setupAnimations
    //----------------------------------------------------------------------------------------------
    private void setupAnimations()
    {
        mFocusAnim = new ScaleAnimation(1f,0.8f,1f,0.8f,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.45f);
        mFocusAnim.setDuration(700);
        mFocusAnim.setRepeatCount(-1);
        mFocusAnim.setRepeatMode(Animation.REVERSE);




    }







    //**********************************************************************************************
    // FocusView
    //**********************************************************************************************

    static class FocusView extends View
    {

        public static final String TAG = FocusView.class.getSimpleName();


        private static float FRAME_SIZE_FACTOR = 0.35f;
        private static float FRAME_CORNER_FACTOR = FRAME_SIZE_FACTOR * 0.3f;


        // properties
        private int mFocusSearchColor;
        private int mFocusSuccessColor;
        private int mFocusFailureColor;


        //Graphics
        private int mWidth = -1;
        private int mHeight = -1;
        private int mDiameter;
        private Paint mPaint;
        private Rect mFrameRect;
        private Bitmap mBackground;
        private Paint mBackgroundPaint;
        private int mPrimaryColor;
        private Paint mShadowPaint;
        private int mShadowColor;



        //==============================================================================================
        // Init
        //==============================================================================================

        public FocusView(Context context)
        {
            super(context);

            loadDefaults();
            initGraphicTools();
            ViewHelper.enableSoftwareLayer(this);

        }




        private void loadDefaults()
        {
            mFocusSearchColor = Color.WHITE;
            mFocusSuccessColor = Color.GREEN;
            mFocusFailureColor = Color.RED;
            mShadowColor = getResources().getColor(R.color.camera_btn_shadow);

            mPrimaryColor = mFocusSearchColor;
        }

        //==============================================================================================
        // Interface
        //==============================================================================================

        public void selectFocusSearchMode()
        {
            mPrimaryColor = mFocusSearchColor;
            invalidate();
        }


        public void selectFocusSuccessMode()
        {
            mPrimaryColor = mFocusSuccessColor;
            invalidate();
        }

        public void selectFocusFailureMode()
        {
            mPrimaryColor = mFocusFailureColor;
            invalidate();
        }





        //==========================================================================================
        // Calbacks
        //==========================================================================================


        //------------------------------------------------------------------------------------------
        // onDraw
        //------------------------------------------------------------------------------------------
        @Override
        protected void onDraw(Canvas canvas)
        {
            //canvas.drawColor(Color.BLUE);
            drawFocusUi(canvas);

        }


        //------------------------------------------------------------------------------------------
        // onSizeChanged
        //------------------------------------------------------------------------------------------
        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);

            initSizes(new Size(w,h));
        }


        //==========================================================================================
        // Internals
        //==========================================================================================


        //------------------------------------------------------------------------------------------
        // drawFocusUi
        //------------------------------------------------------------------------------------------
        private void drawFocusUi(Canvas canvas)
        {

            mPaint.setColor(mPrimaryColor);

            final Path path = new Path();


            int cornerSize = (int) (mDiameter * FRAME_CORNER_FACTOR);

            //top left
            path.moveTo(mFrameRect.left, mFrameRect.top+cornerSize);
            path.lineTo(mFrameRect.left, mFrameRect.top);
            path.lineTo(mFrameRect.left+cornerSize , mFrameRect.top);

            //top right
            path.moveTo(mFrameRect.right, mFrameRect.top+cornerSize);
            path.lineTo(mFrameRect.right, mFrameRect.top);
            path.lineTo(mFrameRect.right-cornerSize , mFrameRect.top);

            //bottom right
            path.moveTo(mFrameRect.right, mFrameRect.bottom-cornerSize);
            path.lineTo(mFrameRect.right, mFrameRect.bottom);
            path.lineTo(mFrameRect.right-cornerSize , mFrameRect.bottom);

            //bottom left
            path.moveTo(mFrameRect.left, mFrameRect.bottom-cornerSize);
            path.lineTo(mFrameRect.left, mFrameRect.bottom);
            path.lineTo(mFrameRect.left+cornerSize , mFrameRect.bottom);


            mShadowPaint.setColor(mShadowColor);
            mShadowPaint.setStrokeWidth(mPaint.getStrokeWidth() * 2);
            mShadowPaint.setMaskFilter(new BlurMaskFilter(mPaint.getStrokeWidth(), BlurMaskFilter.Blur.NORMAL));
            canvas.drawPath(path, mShadowPaint);
            canvas.drawPath(path, mPaint);

        }


        //---------------------------------------------------------------------------------------------
        // initSizes
        //---------------------------------------------------------------------------------------------
        protected void initSizes(Size size)
        {
            mWidth = size.width;
            mHeight = size.height;
            mDiameter = LangHelper.min(mWidth, mHeight);


            int frameSize = mDiameter;
            mFrameRect.left = (mWidth-frameSize)/2;
            mFrameRect.top =  (mHeight-frameSize) /2;
            mFrameRect.right = mFrameRect.left + frameSize;
            mFrameRect.bottom = mFrameRect.top + frameSize;

        }


        //----------------------------------------------------------------------------------------------
        // initGraphicTools
        //----------------------------------------------------------------------------------------------
        private void initGraphicTools()
        {

            mBackgroundPaint = new Paint();


            mFrameRect = new Rect();
            mPaint = new Paint();
            mPaint.setColor(mPrimaryColor);
            mPaint.setStrokeWidth(getResources().getDimension(R.dimen.camera_focus_frameStrokeSize));
            mPaint.setAntiAlias(true);
            mPaint.setDither(true);
            mPaint.setStrokeJoin(Paint.Join.ROUND);
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            mPaint.setStyle(Paint.Style.STROKE);



            mShadowPaint = new Paint();
            mShadowPaint.set(mPaint);
        }





    } // FocusOverlay


} // FocusOverlay

