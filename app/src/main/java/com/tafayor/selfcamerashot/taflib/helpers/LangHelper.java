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

import java.util.ArrayList;


public class LangHelper
{

    public static String TAG = LangHelper.class.getSimpleName();



    public static String removeLastChar(String str)
    {
        if(str==null || str.length()==0) return str;
        return str.substring(0,str.length()-1);
    }




    public static byte toByte(String val)
    {
        byte ret = 0;

        try
        {
            ret = Byte.parseByte(val);
        }
        catch(Exception ex)
        {
            LogHelper.logx(ex);
        }

        return ret;
    }


    public static int toInt(String val)
    {
        int ret = 0;

        try
        {
            ret = Integer.parseInt(val);
        }
        catch(Exception ex)
        {
            LogHelper.logx(ex);
        }

        return ret;
    }


    public static float toFloat(String val)
    {
        float ret = 0;

        try
        {
            ret = Float.parseFloat(val);
        }
        catch(Exception ex)
        {
            LogHelper.logx(ex);
        }

        return ret;
    }



    public static boolean intToBool(int val)
    {
        return ((val == 0)? false:true);
    }

    public static int boolToInt(boolean val)
    {
        return ((val == true)? 1:0);
    }

    public static<T extends Comparable<? super T>> T max(T... values)
    {

        T maxValue = values[0];

        for(int i=0;i<values.length;i++)
        {
            if(values[i].compareTo(maxValue)>0)
            {
                maxValue = values[i];
            }

        }

        return maxValue;
    }


    public static<T extends Comparable<? super T>> T min(T... values)
    {

        T minValue = values[0];

        for(int i=0;i<values.length;i++)
        {
            if(values[i].compareTo(minValue)<0)
            {
                minValue = values[i];
            }

        }

        return minValue;
    }



   /* public static float clamp(float val, float min, float max)
    {
        return Math.max(min, Math.min(max, val));
    }*/

    public static <T extends Comparable<T>> T clamp(T val, T min, T max) {
        if (val.compareTo(min) < 0) return min;
        else if(val.compareTo(max) > 0) return max;
        else return val;
    }



    public static  String strRepeat(String str, int count)
    {
        String ret = "";
        for(int i=0;i<count ;i++) ret += str;
        return ret;
    }


    public static ArrayList<String> lineRepeat(String line, int count)
    {
        ArrayList<String> list = new ArrayList<String>();
        for(int i=0;i<count ;i++) list.add(line);

        return list;
    }


    public static void sleep(long millis)
    {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {

        }
    }


}
