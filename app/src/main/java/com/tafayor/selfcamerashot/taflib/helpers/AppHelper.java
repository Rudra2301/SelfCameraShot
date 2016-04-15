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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;

import java.util.Locale;


public class AppHelper
{
    static String TAG = AppHelper.class.getSimpleName();






    public static  String getAppName(Context ctx)
    {
        String name = "";
        try
        {
            int stringId = ctx.getApplicationInfo().labelRes;
            name =  ctx.getString(stringId);
        }
        catch(Exception ex)
        {
            LogHelper.logx(ex);
        }

        return name;
    }


    //----------------------------------------------------------------------------------------------
    // postOnUi
    //----------------------------------------------------------------------------------------------
    public static void postOnUi(Context ctx, Runnable task)
    {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(task);
    }


    //----------------------------------------------------------------------------------------------
    // postOnUi
    //----------------------------------------------------------------------------------------------
    public static void postOnUi(Context ctx, Runnable task, long delayMs)
    {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(task, delayMs);
    }


    //----------------------------------------------------------------------------------------------
    // isPackageExisted
    //----------------------------------------------------------------------------------------------
    public static boolean isPackageInstalled(Context ctx, String targetPackage)
    {
        PackageManager pm = ctx.getPackageManager();
        try
        {
            PackageInfo info = pm.getPackageInfo(targetPackage,PackageManager.GET_META_DATA);
        }
        catch (PackageManager.NameNotFoundException e)
        {
            return false;
        }

        return true;
    }





    //----------------------------------------------------------------------------------------------
    // getLocaleLanguage
    //----------------------------------------------------------------------------------------------
    public static String getLanguage()
    {
        String locale =  Locale.getDefault().getLanguage();
        LogHelper.log("getLanguage : " + locale);
        if(locale.equals("")) locale = "en";
        return locale;
    }



    //----------------------------------------------------------------------------------------------
    // restartClearActivityOutside
    //----------------------------------------------------------------------------------------------
    public static  void restartClearActivityOutside(Activity activity)
    {
        if(activity == null)  return;
        Context ctx = activity.getApplicationContext();
        Intent intent;
        intent = activity.getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_NO_ANIMATION);

        activity.finish();
        activity.overridePendingTransition(0,0);

        ctx.startActivity(intent);
        activity.overridePendingTransition(0,0);

    }

    //----------------------------------------------------------------------------------------------
    // restartClearActivity
    //----------------------------------------------------------------------------------------------
    public static  void restartClearActivity(Activity activity)
    {
        if(activity == null)  return;
        Context ctx = activity.getApplicationContext();
        Intent intent;
        intent = activity.getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP  |
                Intent.FLAG_ACTIVITY_NO_ANIMATION);

        activity.finish();
        activity.overridePendingTransition(0,0);

        ctx.startActivity(intent);
        activity.overridePendingTransition(0,0);

    }



    //----------------------------------------------------------------------------------------------
    // restartActivity
    //----------------------------------------------------------------------------------------------
    public static  void restartActivity(Activity activity)
    {

        if(activity == null)    return;

        if (Build.VERSION.SDK_INT >= 11)
        {
            activity.recreate();
        }
        else
        {
            Intent intent;
            intent = activity.getIntent();
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP  | Intent.FLAG_ACTIVITY_NO_ANIMATION |
            Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.finish();
            activity.overridePendingTransition(0, 0);
            activity.startActivity(intent);
            activity.overridePendingTransition(0, 0);
        }

    }


    //----------------------------------------------------------------------------------------------
    // restartActivityOutside
    //----------------------------------------------------------------------------------------------
    public static  void restartActivityOutside(Activity activity)
    {

        if(activity == null)  return;

        if (Build.VERSION.SDK_INT >= 11)
        {
            activity.recreate();
        }
        else
        {
            restartClearActivityOutside(activity);
        }

    }

    //----------------------------------------------------------------------------------------------
    // startActivity
    //----------------------------------------------------------------------------------------------
    public static  void startActivity(Context ctx, Intent intent)
    {
        intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
    }





    //----------------------------------------------------------------------------------------------
    // setLocale
    //----------------------------------------------------------------------------------------------
    public static void setLocale(Context ctx, String lang)
    {
        Locale locale;

       /* if(lang.equals("zh") || lang.equals("zh-HK") || lang.equals("zh-")) locale = Locale.TRADITIONAL_CHINESE;
        else if(lang.equals("zh-CN")) locale = Locale.SIMPLIFIED_CHINESE;
        else    locale = new Locale(lang);*/

        locale = new Locale(lang);
        Resources res = ctx.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = locale;
        res.updateConfiguration(conf, dm);
    }


    //----------------------------------------------------------------------------------------------
    // isDebug
    //----------------------------------------------------------------------------------------------
    public static boolean isDebug(Context ctx)
    {

        boolean  isDebuggable;
        isDebuggable =  ( 0 != ( ctx.getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE ) );
       // System.out.println( "isDebug : " + isDebuggable);
        return isDebuggable;
    }




    //----------------------------------------------------------------------------------------------
    // getVersionName
    //----------------------------------------------------------------------------------------------
    public static String getVersionName(Context ctx)
    {
        PackageInfo pInfo = null;
        String versionName = "";
        try
        {
            pInfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
            versionName = pInfo.versionName;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            LogHelper.logx(e);
        }

        return versionName;
    }



    //----------------------------------------------------------------------------------------------
    // getVersionCode
    //----------------------------------------------------------------------------------------------
    public static int getVersionCode(Context ctx)
    {
        PackageInfo pInfo = null;
        int versionCode = 1;
        try
        {
            pInfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
            versionCode = pInfo.versionCode;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            LogHelper.logx(e);
        }

        return versionCode;
    }

}


