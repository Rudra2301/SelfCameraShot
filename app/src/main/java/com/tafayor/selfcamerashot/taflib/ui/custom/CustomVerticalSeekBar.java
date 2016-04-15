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



package com.tafayor.selfcamerashot.taflib.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;



public class CustomVerticalSeekBar extends SeekBar {


    public static String  TAG = CustomVerticalSeekBar.class.getSimpleName();




    OnSeekBarChangeListener mOnSeekBarChangeListener = null;


    public CustomVerticalSeekBar(Context context) {
        super(context);

    }

    public CustomVerticalSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public CustomVerticalSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);


        init(attrs);
    }

    void init(AttributeSet attrs)
    {



        int [] attributes = new int [] {android.R.attr.paddingLeft,
                android.R.attr.paddingTop,
                android.R.attr.paddingRight,
                android.R.attr.paddingBottom
                };


        TypedArray arr = getContext().obtainStyledAttributes(attrs, attributes);

        int leftPadding = getPaddingLeft() ;
        int topPadding= getPaddingTop();
        int rightPadding = getPaddingRight() ;
        int bottomPadding = getPaddingBottom();

         leftPadding = arr.hasValue(0) ? arr.getDimensionPixelOffset(0, leftPadding) : leftPadding;
         topPadding = arr.hasValue(1) ? arr.getDimensionPixelOffset(1, topPadding) : topPadding;
         rightPadding = arr.hasValue(2) ? arr.getDimensionPixelOffset(2, rightPadding) : rightPadding;
         bottomPadding = arr.hasValue(3) ? arr.getDimensionPixelOffset(3, bottomPadding) : bottomPadding;



        setPadding(topPadding,rightPadding,bottomPadding,leftPadding);

    }


    //==============================================================================================
    // Interface
    //==============================================================================================




    public void refresh()
    {
        onSizeChanged(getWidth(), getHeight() , 0, 0);
    }



    //==============================================================================================
    // Callbaks
    //==============================================================================================
    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh)
    {

        super.onSizeChanged(h, w, oldh, oldw);


    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());

    }

    protected void onDraw(Canvas c)
    {

        c.rotate(-90);
        c.translate(-getHeight(), 0);

        super.onDraw(c);


    }





    public synchronized void setProgressAndMoveThumb(int progress)
    {
        super.setProgress(progress);
        onSizeChanged(getWidth(), getHeight() , 0, 0);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                int i= getMax() - (int) (getMax() * event.getY() / getHeight());
                super.setProgress(i);
                onSizeChanged(getWidth(), getHeight(), 0, 0);
                break;

            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }








    //==============================================================================================
    // Internals
    //==============================================================================================





}