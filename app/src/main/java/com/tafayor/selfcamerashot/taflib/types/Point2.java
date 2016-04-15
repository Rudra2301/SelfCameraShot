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


import com.tafayor.selfcamerashot.taflib.helpers.LangHelper;


public class Point2
{
    public static String PARSE_SEPARATOR = "x";
    public int x;
    public int y;

    public Point2()
    {
        x = 0;
        y = 0;
    }

    public Point2(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public Point2(Point2 other)
    {
        this.x = other.x;
        this.y = other.y;
    }

    public Point2(android.graphics.Point other)
    {
        this.x = other.x;
        this.y = other.y;
    }



    public boolean equals(Point2 other)
    {
        return ((this.x == other.x) && (this.y == other.y));
    }


    public boolean isZero()
    {
        return (x==0 && y ==0);
    }


    // string format  "xXy" , where X is seperator
    public static Point2 parse(String value, String separator)
    {
        String[] parts = value.split(separator);
        int w = LangHelper.toInt(parts[0]);
        int h = LangHelper.toInt(parts[1]);
        return new Point2(w,h);
    }

    public static Point2 fromString(String value)
    {
        return parse(value, PARSE_SEPARATOR);
    }

    public String  toString()
    {
        return x+"x"+y;
    }

    public void from(android.graphics.Point point)
    {
        x = point.x;
        y = point.y;
    }



    public android.graphics.Point toPoint()
    {
        return new android.graphics.Point(x, y);
    }
}
