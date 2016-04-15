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


package com.tafayor.selfcamerashot.utils;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.tafayor.selfcamerashot.taflib.helpers.LogHelper;



public class OnGestureTouchListener implements View.OnTouchListener
{


    private static final int TRACK_THRESHOLD = 3;


    private final GestureDetector gestureDetector;
    private boolean mIsDown = false;
    private int mDownStartX, mDownStartY ;
    private int mDownLastX, mDownLastY ;

    public OnGestureTouchListener(Context ctx)
    {
        gestureDetector = new GestureDetector(ctx, new GestureListener());
    }


    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        if(event.getAction() == MotionEvent.ACTION_DOWN)
        {
            mDownLastX = (int) event.getX();
            mDownLastY = (int) event.getY();

            mIsDown = true;

        }
        else if(event.getAction() == MotionEvent.ACTION_CANCEL ||
                event.getAction() == MotionEvent.ACTION_UP)
        {
            mIsDown = false;
        }
        else if(event.getAction() == MotionEvent.ACTION_MOVE)
        {
            if(mIsDown)
            {
                int deltaY = (int) event.getY() - mDownLastY;
                int deltaX = (int)event.getX() - mDownLastX;

                int absDeltaX = Math.abs(deltaX);
                int absDeltaY = Math.abs(deltaY);

                if(absDeltaX > absDeltaY && absDeltaX > TRACK_THRESHOLD)
                {
                    if(deltaX > 0 ) onTrackRight(absDeltaX);
                    else onTrackLeft(absDeltaX);
                }
                else if(absDeltaY > absDeltaX && absDeltaY > TRACK_THRESHOLD)
                {
                    if(deltaY > 0 ) onTrackBottom(absDeltaY);
                    else onTrackTop(absDeltaY);
                }

                mDownLastX = (int)event.getX();
                mDownLastY = (int) event.getY();
            }

        }

        return gestureDetector.onTouchEvent(event);
    }



    private final class GestureListener extends GestureDetector.SimpleOnGestureListener
    {

        private static final int SWIPE_THRESHOLD = 70;
        private static final int SWIPE_VELOCITY_THRESHOLD = 70;




        @Override
        public boolean onSingleTapUp(MotionEvent e)
        {
            return OnGestureTouchListener.this.onSingleTapUp(e);
        }


        @Override
        public boolean onDown(MotionEvent e)
        {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
        {
            boolean result = false;
            try
            {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                LogHelper.log("diffX : " + diffX);
                LogHelper.log("diffY : " + diffY);

                LogHelper.log("velocityX : " + velocityX);

                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD)
                    {
                        int distance = (int) Math.abs(diffX);
                        if (diffX > 0)
                        {
                            onSwipeRight(distance);
                        }
                        else
                        {
                            onSwipeLeft(distance);
                        }
                    }
                    //result = fa;
                }
                else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD)
                {
                    int distance = (int) Math.abs(diffY);
                    if (diffY > 0) {
                        onSwipeBottom(distance);
                    } else {
                        onSwipeTop(distance);
                    }
                }
               // result = true;

            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }



    }

    public boolean onSingleTapUp(MotionEvent e)
    {
        return false;
    }

    public void onSwipeRight(int distance)
    {

    }

    public void onSwipeLeft(int distance)
    {

    }

    public void onSwipeTop(int distance)
    {

    }

    public void onSwipeBottom(int distance)
    {

    }


    public void onTrackTop(int distance)
    {

    }

    public void onTrackBottom(int distance)
    {

    }

    public void onTrackRight(int distance)
    {

    }

    public void onTrackLeft(int distance)
    {

    }
}