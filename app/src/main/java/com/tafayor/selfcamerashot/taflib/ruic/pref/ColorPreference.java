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


package com.tafayor.selfcamerashot.taflib.ruic.pref;

import android.content.Context;
import android.graphics.Color;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tafayor.selfcamerashot.R;



public class ColorPreference extends Preference
{

    public static String TAG = ColorPreference.class.getSimpleName();


    private static int DEFAULT_COLOR = Color.BLACK;





    public ColorPreference(Context context) {
        super(context);
        init();
    }

    public ColorPreference(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        init();
    }

    public ColorPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }


    private void init()
    {
        setLayoutResource(R.layout.ruic_preference_color);
    }



    //==============================================================================================
    // Interface
    //==============================================================================================

    public void setColor(int color)
    {
        persistInt(color);
        notifyChanged();
    }


    public int getColor()
    {
        return getPersistedInt(DEFAULT_COLOR);
    }




    //==============================================================================================
    // Callbacks
    //==============================================================================================

    @Override
    protected void onBindView(View view)
    {
        super.onBindView(view);
        ImageView colorIcon = (ImageView) view.findViewById(R.id.color_icon);
        colorIcon.setBackgroundColor(getColor());


    }

    @Override
    protected View onCreateView(ViewGroup parent)
    {

        return super.onCreateView(parent);
    }

//==============================================================================================
    // Internals
    //==============================================================================================


}
