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

import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;


public class Util
{







    //----------------------------------------------------------------------------------------------
    // tintActionIcon
    //----------------------------------------------------------------------------------------------
    public static Drawable tint(Drawable icon, int color)
    {

        Drawable tintedIcon = DrawableCompat.wrap(icon);
        DrawableCompat.setTint(tintedIcon, color);

        return tintedIcon;
    }












    public static boolean isRooted()
    {

        // Method 1
        //--------------
        String[] paths = {
                "/sbin/su",
                "/system/bin/su",
                "/system/xbin/su",
                "/data/local/xbin/su",
                "/data/local/bin/su",
                "/system/sd/xbin/su",
                "/system/bin/failsafe/su",
                "/data/local/su",
                "/bin/su",
                "/xbin/su",
                "/sbin/su",};

        for (String path : paths)
        {
            if (new File(path).exists()) return true;
        }


        // Method 2
        //--------------

        Process process = null;
        try {
            process = Runtime.getRuntime().exec(new String[] { "which", "su" });
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            if (in.readLine() != null)
            {
                return true;
            }
            return false;
        } catch (Throwable t)
        {
            return false;
        } finally {
            if (process != null) process.destroy();
        }


    }





}
