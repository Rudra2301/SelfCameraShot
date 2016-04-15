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


package com.tafayor.selfcamerashot.taflib.ruic.pref.preferenceActivity;



import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;

import com.tafayor.selfcamerashot.R;


public class HeaderPreference extends Preference
{





    public HeaderPreference(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
        init();
    }

    public HeaderPreference(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }


    private void init()
    {
        setLayoutResource(R.layout.ruic_preference_header);
    }


}