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



package com.tafayor.selfcamerashot;

import android.app.Application;
import android.content.Context;


import com.crashlytics.android.Crashlytics;
import com.tafayor.selfcamerashot.prefs.ActivatorsPrefHelper;
import com.tafayor.selfcamerashot.prefs.AdvancedCameraPrefHelper;
import com.tafayor.selfcamerashot.prefs.CameraPrefHelper;
import com.tafayor.selfcamerashot.prefs.GeneralPrefHelper;
import com.tafayor.selfcamerashot.prefs.PrefHelper;
import com.tafayor.selfcamerashot.prefs.RemoteControlPrefHelper;
import com.tafayor.selfcamerashot.prefs.UiPrefHelper;
import com.tafayor.selfcamerashot.pro.ProConfig;
import com.tafayor.selfcamerashot.taflib.helpers.AppHelper;
import com.tafayor.selfcamerashot.taflib.helpers.DisplayHelper;
import com.tafayor.selfcamerashot.taflib.helpers.LogHelper;
import com.tafayor.selfcamerashot.taflib.iab.UpgradeManager;
import com.tafayor.selfcamerashot.taflib.types.Size;
import com.tafayor.selfcamerashot.utils.LogReporter;
import com.tafayor.selfcamerashot.utils.UpdatesUtil;
import io.fabric.sdk.android.Fabric;




public class App extends Application {

    public static String TAG = App.class.getSimpleName();

    public static boolean FORCE_PRO = false;


    private static boolean IS_REPORT_MODE = false;

    public static String VENDOR_NAME = "Ouadban+Youssef";
    public static String VENDOR_EMAIL = "contact@tafayor.com";


    private static boolean sIsUpdated = false;
    private static boolean sIsPro = false;
    static Context sContext;
    private static PrefHelper sPrefHelper;
    private static ActivatorsPrefHelper sActivatorsPrefHelper;
    private static AdvancedCameraPrefHelper sAdvancedCameraPrefHelper;
    private static CameraPrefHelper sCameraPrefHelper;
    private static GeneralPrefHelper sGeneralPrefHelper;
    private static RemoteControlPrefHelper sRemoteControlPrefHelper;
    private static UiPrefHelper sUiPrefHelper;
    private static String sAppTitle;
    private static UpgradeManager sUpgradeManager;


    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());


        sContext = getApplicationContext();




        initializeApp();

        sAppTitle = sContext.getResources().getString(R.string.app_name);











        if(isReportMode())
        {
            LogHelper.setLogCallback(LogReporter.getLogCallback());
        }


    }

    public static Context getContext() {
        return sContext;
    }

    public static String getAppTitle() {
        if (App.isPro()) return sContext.getResources().getString(R.string.app_name_pro);
        else return sContext.getResources().getString(R.string.app_name_free);
    }


    public static PrefHelper getPrefHelper() {
        return PrefHelper.i(sContext);
    }

    public static ActivatorsPrefHelper getActivatorsPrefHelper() {
        return ActivatorsPrefHelper.i(sContext);
    }

    public static AdvancedCameraPrefHelper getAdvancedCameraPrefHelper() {
        return AdvancedCameraPrefHelper.i(sContext);
    }

    public static CameraPrefHelper getCameraPrefHelper() {
        return CameraPrefHelper.i(sContext);
    }

    public static GeneralPrefHelper getGeneralPrefHelper() {
        return GeneralPrefHelper.i(sContext);
    }

    public static RemoteControlPrefHelper getRemoteControlPrefHelper() {
        return RemoteControlPrefHelper.i(sContext);
    }

    public static UiPrefHelper getUiPrefHelper() {
        return UiPrefHelper.i(sContext);
    }



    public static boolean isPro() {
        return sIsPro;
    }

    public static void setIsPro(boolean state) {
        sIsPro = state;
    }


    public static void initializeApp() {
        int versionCode = AppHelper.getVersionCode(sContext);

        if (App.getPrefHelper().getFirstTime()) {
            App.getPrefHelper().loadDefaultPrefs();
            App.getPrefHelper().setFirstTime(false);
        }
        else if (getPrefHelper().getVersionCode() != versionCode) {
            UpdatesUtil.checkUpdates();
            getPrefHelper().setVersionCode(versionCode);
        }




    }


    public static int getScreenHeight(Context ctx) {
        return DisplayHelper.getScreenSize(ctx).height;
    }

    public static int getScreenWidth(Context ctx) {
        return DisplayHelper.getScreenSize(ctx).width;
    }

    public static int getScreenRawHeight(Context ctx) {
        return DisplayHelper.getScreenRawSize(ctx).height;
    }

    public static int getScreenRawWidth(Context ctx) {
        return DisplayHelper.getScreenRawSize(ctx).width;
    }


    public static int getScreenNativeHeight(Context ctx) {
        return DisplayHelper.getScreenNativeSize(ctx).height;
    }

    public static int getScreenNativeWidth(Context ctx) {
        return DisplayHelper.getScreenNativeSize(ctx).width;
    }


    public static int getScreenDiameter(Context ctx) {
        Size size = DisplayHelper.getScreenRawSize(ctx);
        return Math.min(size.width, size.height);
    }


    public static boolean isDebug()
    {
        boolean ret = AppHelper.isDebug(sContext);
        return ret;
    }

    public static boolean isReportMode()
    {
        return  IS_REPORT_MODE;
    }

}