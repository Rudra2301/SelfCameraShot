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


package com.tafayor.selfcamerashot.taflib.helpers;

import android.graphics.Bitmap;
import android.os.Build;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;


public class AnimHelper
{

    public static String TAG = AnimHelper.class.getSimpleName();


 
 

    //==============================================================================================
    // Interface
    //==============================================================================================


    //----------------------------------------------------------------------------------------------
    // rotateView
    //----------------------------------------------------------------------------------------------
    public  static void rotateView(final View view,final float angle)
    {


        RotateAnimation an = new RotateAnimation(0, angle, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        an.setDuration(0);
        an.setFillAfter(true);
        an.setFillEnabled(true);
        view.startAnimation(an);

    }

    //----------------------------------------------------------------------------------------------
    // setRotation
    //----------------------------------------------------------------------------------------------
    public  static void rotateView(final View view,final float angle, int durationMs)
    {


        RotateAnimation an = new RotateAnimation(0, angle, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        // Set the animation's parameters
        an.setDuration(durationMs);               // duration in ms
        if(Build.VERSION.SDK_INT >= 14) an.setFillEnabled(true);
        else an.setFillAfter(true);

        an.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public  void onAnimationStart(Animation animation) {

            }

            @Override
            public  void onAnimationEnd(Animation animation)
            {


                if(Build.VERSION.SDK_INT >= 14)
                {
                    float rot = (ViewHelper.getRotation(view) + angle) % 360;
                    ViewHelper.setRotation(view, rot);
                }




            }

            @Override
            public  void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(an);


    }





    //----------------------------------------------------------------------------------------------
    // switchViewsVisibilityByFade
    //----------------------------------------------------------------------------------------------
    public  static void switchViewsVisibilityByFade(final View oldView, final View newView)
    {

        final Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(700);

        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setDuration(700);

        fadeOut.setAnimationListener(new Animation.AnimationListener()
        {
            @Override public  void onAnimationStart(Animation animation) {}
            @Override public  void onAnimationRepeat(Animation animation) {}
            @Override public  void onAnimationEnd(Animation animation)
            {

                oldView.setVisibility(View.GONE);
                newView.setVisibility(View.VISIBLE);
                newView.startAnimation(fadeIn);
            }
        });
        oldView.startAnimation(fadeOut);

    }


    //----------------------------------------------------------------------------------------------
    // animateImageViewChangeByFade
    //----------------------------------------------------------------------------------------------
    public  static void animateImageViewChangeByFade(final ImageView v, final Bitmap new_image)
    {

        final Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(700);

        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setDuration(700);

        fadeOut.setAnimationListener(new Animation.AnimationListener()
        {
            @Override public  void onAnimationStart(Animation animation) {}
            @Override public  void onAnimationRepeat(Animation animation) {}
            @Override public  void onAnimationEnd(Animation animation)
            {
                ViewHelper.releaseDrawable(v);
                v.setImageBitmap(new_image);
                v.startAnimation(fadeIn);
            }
        });
        v.startAnimation(fadeOut);

    }


    //----------------------------------------------------------------------------------------------
    // blink
    //----------------------------------------------------------------------------------------------
    public  static void blink (View view, int durationMs)
    {
        blink(view, durationMs, Animation.INFINITE);
    }

    //----------------------------------------------------------------------------------------------
    // blink
    //----------------------------------------------------------------------------------------------
    public  static void blink(View view, int durationMs, int repeatCount)
    {
        Animation anim = new AlphaAnimation(1.0f, 0.0f);
        anim.setDuration(durationMs); //You can manage the time of the blink with this parameter
        anim.setStartOffset(0);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(repeatCount);
        view.startAnimation(anim);

    }



    //==============================================================================================
    // Internals
    //==============================================================================================



}
