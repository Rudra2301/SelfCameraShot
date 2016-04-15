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


package com.tafayor.selfcamerashot.taflib.types;

import android.graphics.Point;
import android.hardware.Camera;

import com.tafayor.selfcamerashot.taflib.helpers.LangHelper;



public class Size
{
    public static String PARSE_SEPARATOR = "x";
    public int width;
    public int height;

    public Size()
    {
        width = 0;
        height = 0;
    }

    public Size(int width, int height)
    {
        this.width = width;
        this.height = height;
    }

    public Size(Size other)
    {
        this.width = other.width;
        this.height = other.height;
    }

    public Size(Point point)
    {
        width = point.x;
        height = point.y;
    }

    public boolean equals(Size other)
    {
        return ((this.width == other.width) && (this.height == other.height));
    }


    public boolean isZero()
    {
        return (width==0 && height ==0);
    }


    // string format  "widthXheight" , where X is seperator
    public static Size parse(String value, String separator)
    {
        String[] parts = value.split(separator);
        if(parts.length == 2)
        {
            int w = LangHelper.toInt(parts[0]);
            int h = LangHelper.toInt( parts[1]);
            return new Size(w,h);
        }
        else
        {
            return new Size(0,0);
        }

    }

    public static Size fromString(String value)
    {
        return parse(value, PARSE_SEPARATOR);
    }

    public String  toString()
    {
        return width+"x"+height;
    }

    public void from(Point point)
    {
        width = point.x;
        height = point.y;
    }

    public void from(Camera.Size size)
    {
        width = size.width;
        height = size.height;
    }

    public Point toPoint()
    {
        return new Point(width, height);
    }
}
