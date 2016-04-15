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
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;




public class ThemeHelper
{

    public static String TAG = ThemeHelper.class.getSimpleName();







    //----------------------------------------------------------------------------------------------
    // getTypedArray
    //----------------------------------------------------------------------------------------------
    public static  TypedArray getTypedArray(Context context,int[] attrs)
    {

        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs);

        return  attributes;
    }


    //----------------------------------------------------------------------------------------------
    // getTypedArray
    //----------------------------------------------------------------------------------------------
    public static  TypedArray getTypedArray(Context context, int theme, int[] attrs)
    {

        TypedArray attributes = context.getTheme().obtainStyledAttributes(theme, attrs);

        return  attributes;
    }


    //----------------------------------------------------------------------------------------------
    // getColor
    //----------------------------------------------------------------------------------------------
    public static int getColor(Context context, int attr)
    {
        TypedArray atts = context.obtainStyledAttributes(new int[]{ attr});
        int color = atts.getColor(0, 0);
        atts.recycle();
        return color;
    }



    //----------------------------------------------------------------------------------------------
    // getDrawable
    //----------------------------------------------------------------------------------------------
    public static Drawable getDrawable(Context context, int attr)
    {
        TypedArray atts = context.obtainStyledAttributes(new int[]{attr});
        Drawable drawable = atts.getDrawable(0);
        atts.recycle();
        return drawable;
    }

    //----------------------------------------------------------------------------------------------
    // getDimension
    //----------------------------------------------------------------------------------------------
    public static  float getDimension(Context context, int attr)
    {
        TypedArray atts = context.obtainStyledAttributes(new int[]{ attr});
        float color = atts.getDimension(0, 0);
        atts.recycle();
        return color;
    }


    //----------------------------------------------------------------------------------------------
    // getResourceId
    //----------------------------------------------------------------------------------------------
    public static  int getResourceId(Context context, int attr)
    {
        TypedArray atts = context.obtainStyledAttributes(new int[]{ attr});
        int id = atts.getResourceId(0, 0);
        atts.recycle();
        return id;
    }
}
