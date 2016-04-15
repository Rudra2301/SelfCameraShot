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


;import com.tafayor.selfcamerashot.App;
import com.tafayor.selfcamerashot.prefs.AdvancedCameraPrefHelper;
import com.tafayor.selfcamerashot.prefs.DefaultPrefs;



public class UpdatesUtil
{
    public static String TAG = UpdatesUtil.class.getSimpleName() ;










    //==================================================================================================

    public  static void checkUpdates()
    {
        int versionCode = App.getPrefHelper().getVersionCode();

        if(versionCode < 133) update133();;
        if(versionCode < 180) update180();;
        if(versionCode < 201) update201();
    }



    public static void update201()//1.8.21
    {
        if(!App.getUiPrefHelper().getEnableFullscreen())
        {
            App.getAdvancedCameraPrefHelper().setPreviewSize(0, DefaultPrefs.PREVIEW_SIZE);
            App.getAdvancedCameraPrefHelper().setPreviewSize(1, DefaultPrefs.PREVIEW_SIZE);
        }

    }


    public static void update180()//1.8.0
    {
        App.getRemoteControlPrefHelper().setWhistleSensitivity(App.getRemoteControlPrefHelper().getSoundControlSensitivity());
        App.getRemoteControlPrefHelper().setClappingSensitivity(DefaultPrefs.CLAPPING_SENSITIVITY);
    }


    public static void update133()//1.7.33
    {
        App.getUiPrefHelper().setEnableFullscreen(DefaultPrefs.ENABLE_FULLSCREEN);
    }

}
