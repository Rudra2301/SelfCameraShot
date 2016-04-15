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

import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;

import java.util.List;


public class SystemHelper
{

    public static String TAG = SystemHelper.class.getSimpleName();









    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public  static boolean isAccessibilityEnabled(Context ctx, String id)
    {

        AccessibilityManager am = (AccessibilityManager) ctx
                .getSystemService(Context.ACCESSIBILITY_SERVICE);

        List<AccessibilityServiceInfo> runningServices = am
                .getEnabledAccessibilityServiceList(AccessibilityEvent.TYPES_ALL_MASK);
        for (AccessibilityServiceInfo service : runningServices)
        {
            if (id.equals(service.getId()))
            {
                return true;
            }
        }

        return false;
    }


    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static void logInstalledAccessiblityServices(Context ctx )
    {

        AccessibilityManager am = (AccessibilityManager) ctx
                .getSystemService(Context.ACCESSIBILITY_SERVICE);

        List<AccessibilityServiceInfo> runningServices = am
                .getInstalledAccessibilityServiceList();
        for (AccessibilityServiceInfo service : runningServices) {
            LogHelper.log(TAG, service.getId());
        }
    }






}
