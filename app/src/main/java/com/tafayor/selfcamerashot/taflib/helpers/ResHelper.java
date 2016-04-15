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

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;


public class ResHelper
{
    static String TAG = ResHelper.class.getSimpleName();












    //----------------------------------------------------------------------------------------------
    // getViewId
    //----------------------------------------------------------------------------------------------
    public static  int  getViewId(Context ctx, String name)
    {
        int resID = ctx.getResources().getIdentifier(name , "view", ctx.getPackageName());
        return resID;
    }

    //----------------------------------------------------------------------------------------------
    // getDrawableId
    //----------------------------------------------------------------------------------------------
    public static  int  getDrawableId(Context ctx, String name)
    {
        int resID = ctx.getResources().getIdentifier(name , "drawable", ctx.getPackageName());
        return resID;
    }

    //----------------------------------------------------------------------------------------------
    // getDrawableId
    //----------------------------------------------------------------------------------------------
    public static  int  getStringId(Context ctx, String name)
    {
        int resID = ctx.getResources().getIdentifier(name , "string", ctx.getPackageName());
        return resID;
    }



    //----------------------------------------------------------------------------------------------
    // getResString
    //----------------------------------------------------------------------------------------------
    public static  String  getResString(Context ctx, int id)
    {
        String string = "";
        Resources res = ctx.getResources();
        if(res != null) string = res.getString(id);
        return string;
    }



    //----------------------------------------------------------------------------------------------
    // getResString
    //----------------------------------------------------------------------------------------------
    public static  String  getResString(Context ctx, int id, String... args)
    {
        String string = "";
        Resources res = ctx.getResources();
        if(res != null) string = res.getString(id, (Object[]) args);
        return string;
    }



    //----------------------------------------------------------------------------------------------
    // getResStringArray
    //----------------------------------------------------------------------------------------------
    public static  String[] getResStringArray(Context ctx, int id)
    {
        String[] string = {} ;
        Resources res = ctx.getResources();
        if(res != null) string = res.getStringArray(id);
        return string;
    }


    //----------------------------------------------------------------------------------------------
    // getResColor
    //----------------------------------------------------------------------------------------------
    public static  int  getResColor(Context ctx, int id)
    {
        int color = 0;
        Resources res = ctx.getResources();
        if(res != null) color = res.getColor(id);
        return color;
    }

    //----------------------------------------------------------------------------------------------
    // getResColorStateList
    //----------------------------------------------------------------------------------------------
    public static  ColorStateList getResColorStateList(Context ctx, int id)
    {
        ColorStateList colorList=  null;
        Resources res = ctx.getResources();
        if(res != null) colorList = res.getColorStateList(id);
        return colorList;
    }

    //----------------------------------------------------------------------------------------------
    // getResDrawable
    //----------------------------------------------------------------------------------------------
    public static  Drawable  getResDrawable(Context ctx, int id)
    {
        Drawable drawable = null;
        Resources res = ctx.getResources();
        if(res != null) drawable = res.getDrawable(id);
        return drawable;
    }

    //----------------------------------------------------------------------------------------------
    // getResDimension
    //----------------------------------------------------------------------------------------------
    public static  float  getResDimension(Context ctx, int id)
    {
        float dimension = 0;
        Resources res = ctx.getResources();
        if(res != null) dimension = res.getDimension(id);
        return dimension;
    }







}


