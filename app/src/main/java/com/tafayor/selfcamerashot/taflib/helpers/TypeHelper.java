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

import android.graphics.Point;

import com.tafayor.selfcamerashot.taflib.types.Size;



public class TypeHelper
{

    public static String TAG = TypeHelper.class.getSimpleName();


    private static String PARSING_SEPARATOR = "x";





    public static Point parsePoint(String value)
    {
        if(value.contains(PARSING_SEPARATOR))
        {
            String[] parts = value.split(PARSING_SEPARATOR);
            int x = Integer.parseInt("0"+parts[0]);
            int y = Integer.parseInt("0"+parts[1]);
            return new Point(x,y);
        }
        else return new Point(0,0);
    }

    public static String  strPoint(Point p)
    {
        return p.x + PARSING_SEPARATOR + p.y;
    }




    public static Size parseTafSize(String value)
    {
        if(value.contains(PARSING_SEPARATOR))
        {
            String[] parts = value.split(PARSING_SEPARATOR);
            int w = Integer.parseInt("0"+parts[0]);
            int h = Integer.parseInt("0"+parts[1]);
            return new Size(w,h);
        }
        else return new Size(0,0);
    }

    public static String  strSize(Size s)
    {
        return s.width + PARSING_SEPARATOR + s.height;
    }


}
