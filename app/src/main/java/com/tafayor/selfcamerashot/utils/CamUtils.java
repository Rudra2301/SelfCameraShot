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

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.os.Build;
import android.view.OrientationEventListener;
import android.view.View;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.tafayor.selfcamerashot.R;
import com.tafayor.selfcamerashot.camera.CameraWrapper;
import com.tafayor.selfcamerashot.taflib.helpers.AnimHelper;
import com.tafayor.selfcamerashot.taflib.helpers.GraphicsHelper;
import com.tafayor.selfcamerashot.taflib.helpers.LangHelper;
import com.tafayor.selfcamerashot.taflib.helpers.ViewHelper;



public class CamUtils
{



    //----------------------------------------------------------------------------------------------
    // findCamFirstId
    //----------------------------------------------------------------------------------------------
    public static int findCamFirstId()
    {
        int id = 0;
        CameraWrapper cam = CameraWrapper.i();
        try
        {
            cam.open(0);
            id = 0;
        }
        catch(Exception ex)
        {
            id = 1;
        }

        return id;
    }


    //----------------------------------------------------------------------------------------------
    // getMaxInset
    //----------------------------------------------------------------------------------------------
    public static int getNavbarInset(Activity context, View view)
    {
        Rect insets = getInsets(context, view);

        return LangHelper.max(insets.right, insets.bottom);

    }


    //----------------------------------------------------------------------------------------------
    // getInsets
    //----------------------------------------------------------------------------------------------
    public static Rect getInsets(Activity context, View view)
    {
        Rect insets = new Rect();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return insets;
        SystemBarTintManager tintManager = new SystemBarTintManager(context);
        SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();

        insets.left = 0;
        insets.right = config.getPixelInsetRight();
        insets.top = config.getPixelInsetTop(true);
        insets.bottom = config.getPixelInsetBottom();


        return insets;
    }



    //----------------------------------------------------------------------------------------------
    // rotateView
    //----------------------------------------------------------------------------------------------
    public static void rotateView(View view,float screenOrientation,  float deviceOrientation)
    {

        final float rotAngle = getRotAngle(view, screenOrientation, deviceOrientation);

        if(Build.VERSION.SDK_INT>=11)
        {
            float angle = (ViewHelper.getRotation(view) + rotAngle) % 360;
            ViewHelper.setRotation(view, angle);
        }
        else
        {
            AnimHelper.rotateView(view, rotAngle);
        }
    }


    //----------------------------------------------------------------------------------------------
    // animateRotation
    //----------------------------------------------------------------------------------------------
    public static void animateViewRotation(View view,float screenOrientation,  float deviceOrientation)
    {
        animateViewRotation(view, screenOrientation, deviceOrientation, 700);
    }


    //----------------------------------------------------------------------------------------------
    // rotateView
    //----------------------------------------------------------------------------------------------
    public static void animateViewRotation(View view, float screenOrientation, float deviceOrientation, int durationMs)
    {
        final float rotAngle = getRotAngle(view, screenOrientation, deviceOrientation);
        AnimHelper.rotateView(view, rotAngle, durationMs);

    }


    //----------------------------------------------------------------------------------------------
    // getRotAngle
    //----------------------------------------------------------------------------------------------
    private static float getRotAngle(View view, float screenOrientation, float deviceOrientation)
    {


        float newOrientation = (360  - deviceOrientation - screenOrientation) % 360;
        final float rotAngle = newOrientation - ViewHelper.getRotation(view);
        return rotAngle;
    }


        //----------------------------------------------------------------------------------------------
    // addShadow
    //----------------------------------------------------------------------------------------------
    public static Drawable addShadow(Context ctx, int resId)
    {
        Bitmap bmp;
        final int glowColor = ctx.getResources().getColor(R.color.camera_btn_glowColor);

        bmp = GraphicsHelper.addGlow(ctx, resId, glowColor);
        Drawable drawable = new BitmapDrawable(ctx.getResources(), bmp);


        return drawable;
    }
}
