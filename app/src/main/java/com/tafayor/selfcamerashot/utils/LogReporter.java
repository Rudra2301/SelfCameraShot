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

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;


import com.tafayor.selfcamerashot.App;
import com.tafayor.selfcamerashot.taflib.helpers.LogHelper;

import java.io.PrintWriter;
import java.io.StringWriter;



public class LogReporter
{


    public static  String  mLogData = "";






    public static LogHelper.LogCallback getLogCallback()
    {
        LogHelper.LogCallback callback = new LogHelper.LogCallback()
        {
            @Override
            public void onLog(String msg)
            {
                addLogVal(msg);
            }
        };

        return callback;
    }



    public static  void resetLog()
    {
        mLogData = "";
    }

    public static  void addLogVal(String val)
    {
        mLogData += "> " + val + "\n";
    }











    public static  String getLogHeader()
    {
        String model = "";
        String header;

        PackageManager manager = App.getContext().getPackageManager();
        PackageInfo info = null;
        try
        {
            info = manager.getPackageInfo (App.getContext().getPackageName(), 0);
        }
        catch (PackageManager.NameNotFoundException e2)
        {
        }

        model = Build.MODEL;
        if (!model.startsWith(Build.MANUFACTURER)) model = Build.MANUFACTURER + " " + model;

        header = "Android version : " +  Build.VERSION.SDK_INT + "\n";
        header += "Device : " + model + "\n";
        header += "App version : " + (info == null ? "(null)" : info.versionCode) + "\n";




        return header;
    }



    public static void sendEmail(String email, String subject, String log)
    {
        String data;

        System.out.println("sending log...");
        System.out.println(email);
        System.out.println(subject);
        System.out.println( log);

        data = getLogHeader() + "\n\n" + "----------------" + "\n\n" + log;

        Intent intent = new Intent (Intent.ACTION_SEND) ; //(Intent.ACTION_SEND);
        intent.setType ("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        intent.putExtra (Intent.EXTRA_SUBJECT, subject);
        intent.putExtra (Intent.EXTRA_TEXT, data); // do this so some email clients don't complain about empty body.
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        App.getContext().startActivity(intent);

    }



    public static void sendEmail()
    {
        sendEmail("contact@tafayor.com", "Log", mLogData);
    }
}
