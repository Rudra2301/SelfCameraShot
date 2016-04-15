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
import android.graphics.Paint;
import android.graphics.Rect;

import com.tafayor.selfcamerashot.App;
import com.tafayor.selfcamerashot.taflib.types.Dimension;



public class DrawHelper {
    public static String TAG = DrawHelper.class.getSimpleName();


    private Context mContext;
    private static DrawHelper mInstance;


    private static class Loader {
        private static final DrawHelper INSTANCE = new DrawHelper(App.getContext());
    }


    public static DrawHelper getInstance() {
        return Loader.INSTANCE;
    }


    private DrawHelper(Context context) {
        mContext = context;

    }


    public static Dimension getTextDimension(String text, Paint paint) {
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        int width = bounds.left + bounds.width();
        int height = bounds.bottom + bounds.height();
        return new Dimension(width, height);

    }



    public static float  getTextHeightFactor(String text)
    {
        Paint paint = new Paint();
        float factor;
        paint.setTextSize(100);
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        float h = Math.abs((Math.abs(bounds.bottom) - Math.abs(bounds.top)));
        factor  = ((1f/h)*100f);

        return factor;
    }

}