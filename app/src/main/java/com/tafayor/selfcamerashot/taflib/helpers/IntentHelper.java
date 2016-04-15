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
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.provider.Settings;

import java.util.List;


public class IntentHelper
{

    public static String TAG = IntentHelper.class.getSimpleName();









    //----------------------------------------------------------------------------------------------
    // OpenImageUri
    //----------------------------------------------------------------------------------------------
    public static boolean openImageUri(Context ctx, Uri uri)
    {
        String MIME_IMAGE ="image/jpeg";
        try
        {

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, MIME_IMAGE);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            ctx.startActivity(intent);

            return true;
        } catch (Exception e)
        {
            LogHelper.logx(e);
            openGallery(ctx);
            return false;
        }
    }



    //----------------------------------------------------------------------------------------------
    // openGallery
    //----------------------------------------------------------------------------------------------
    public static boolean openGallery(Context ctx)
    {
        try
        {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ctx.startActivity(intent);

            return true;
        } catch (Exception e)
        {
            LogHelper.logx(e);
            return false;
        }
    }



    //----------------------------------------------------------------------------------------------
    // openYoutubeVideo
    //----------------------------------------------------------------------------------------------
    public static void openYoutubeVideo(Context ctx, String id)
    {
        try{
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ctx.startActivity(intent);
        }
        catch (Exception ex)
        {
            Intent intent=new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v="+id));
            ctx.startActivity(intent);
        }
    }


    //----------------------------------------------------------------------------------------------
    // openAccessibilitySettings
    //----------------------------------------------------------------------------------------------
    static public void  openAccessibilitySettings(Context ctx)
    {
        Intent goToSettings = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        goToSettings.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
        ctx.startActivity(goToSettings);
    }



    //----------------------------------------------------------------------------------------------
    // openURL
    //----------------------------------------------------------------------------------------------
    static public void openURL(Context ctx, String url)
    {
        try
        {
            if (!url.contains("://"))
            {
                url = "http://" + url;
            }

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse(url));
            ctx.startActivity(intent);
        }
        catch(Exception e)
        {
            LogHelper.logx(TAG, "openURL", e);
        }

    }


    //----------------------------------------------------------------------------------------------
    // isIntentSupported
    //----------------------------------------------------------------------------------------------
    public static boolean isIntentSupported(Context ctx, Intent intent)
    {
        PackageManager pm = ctx.getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(intent, 0);
        return (activities.size() != 0) ;

    }



    //----------------------------------------------------------------------------------------------
    // goHomeScreen
    //----------------------------------------------------------------------------------------------
    public static void goHomeScreen(Context ctx)
    {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(startMain);

    }




    //----------------------------------------------------------------------------------------------
    // openApp
    //----------------------------------------------------------------------------------------------
    public static boolean openApp(Context ctx, String packageName)
    {
        PackageManager manager = ctx.getPackageManager();
        try
        {
            Intent i = manager.getLaunchIntentForPackage(packageName);
            i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            if (i == null) return false;
            ctx.startActivity(i);
            return true;
        } catch (Exception e)
        {
            LogHelper.logx(e);
            return false;
        }
    }


}
