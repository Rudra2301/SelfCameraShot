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
import android.net.Uri;


public class MarketHelper
{
    static String TAG = MarketHelper.class.getSimpleName();


    private static String WEB_URL_PRODUCT = "http://play.google.com/store/apps/details?id=";//+package
    private static String APP_URL_PRODUCT = "market://details?id=";
    private static String WEB_URL_VENDOR = "http://play.google.com/store/search?q=pub:";//+package
    private static String APP_URL_VENDOR = "market://search?q=pub:";

    
    private static String sVendorName;




 



    //----------------------------------------------------------------------------------------------
    // setVendorName
    //----------------------------------------------------------------------------------------------
    public static  void setVendorName(String name)
    {
         sVendorName = name;
    }




    //----------------------------------------------------------------------------------------------
    // getProductLink
    //----------------------------------------------------------------------------------------------
    public static  String getProductLink(String packageName)
    {
        return  WEB_URL_PRODUCT+packageName;
    }



    //----------------------------------------------------------------------------------------------
    // getProductLink
    //----------------------------------------------------------------------------------------------
    public static  String getProductLink(Context ctx)
    {
        return  getProductLink(PackageHelper.getPackageName(ctx));
    }



    //----------------------------------------------------------------------------------------------
    // getVendorLink
    //----------------------------------------------------------------------------------------------
    public static  String getVendorLink(String vendor)
    {
        return  WEB_URL_VENDOR + vendor;
    }



    //----------------------------------------------------------------------------------------------
    // getVendorLink
    //----------------------------------------------------------------------------------------------
    public static  String getVendorLink()
    {
        return  getVendorLink(sVendorName);
    }




    //----------------------------------------------------------------------------------------------
    // showProductPage
    //----------------------------------------------------------------------------------------------
    public static  void showProductPage(Context ctx, String packageName)
    {
        String uri;
        Intent intent ;


        try
        {
            uri = APP_URL_PRODUCT + packageName;
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ctx.startActivity(intent);
        }
        catch (android.content.ActivityNotFoundException e)
        {
            uri = WEB_URL_PRODUCT + packageName;
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ctx.startActivity(intent);
        }
    }

    //----------------------------------------------------------------------------------------------
    // showProductPage
    //----------------------------------------------------------------------------------------------
    public static  void showProductPage(Context ctx)
    {
        showProductPage(ctx, PackageHelper.getPackageName(ctx));
    }




    //----------------------------------------------------------------------------------------------
    // showProductPage
    //----------------------------------------------------------------------------------------------
    public static  void showVendorPage(Context ctx, String vendor)
    {
        String uri;
        Intent intent;

        try
        {
            uri = APP_URL_VENDOR+ vendor;
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ctx.startActivity(intent);
        }
        catch (android.content.ActivityNotFoundException e)
        {
            uri = WEB_URL_VENDOR + vendor;
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ctx.startActivity(intent);
        }
    }


    //----------------------------------------------------------------------------------------------
    // showVendorPage
    //----------------------------------------------------------------------------------------------
    public static  void showVendorPage(Context ctx)
    {
        showVendorPage(ctx, sVendorName);
    }

}


