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


package com.tafayor.selfcamerashot.prefs;

import com.tafayor.selfcamerashot.camera.CameraParameters;



public class PrefValsProxy
{




    public static String wrapWhiteBalance(String wbParam)
    {
        String pref = "";

        if(wbParam.equals(CameraParameters.WHITE_BALANCE_AUTO))
        {
            pref = WhiteBalanceValues.AUTO;
        }
        else if(wbParam.equals(CameraParameters.WHITE_BALANCE_CLOUDY_DAYLIGHT))
        {
            pref = WhiteBalanceValues.CLOUDY;
        }
        else if(wbParam.equals(CameraParameters.WHITE_BALANCE_DAYLIGHT))
        {
            pref = WhiteBalanceValues.DAYLIGHT;
        }
        else if(wbParam.equals(CameraParameters.WHITE_BALANCE_FLUORESCENT))
        {
            pref = WhiteBalanceValues.FLUORESCENT;
        }
        else if(wbParam.equals(CameraParameters.WHITE_BALANCE_INCANDESCENT))
        {
            pref = WhiteBalanceValues.INCANDESCENT;
        }

        return pref;
    }


    //-------------------




    public static String wrapFlashMode(String flashParam)
    {
        String pref = "";

        if(flashParam.equals(CameraParameters.FLASH_MODE_AUTO))
        {
            pref = FlashModeValues.AUTO;
        }
        else if(flashParam.equals(CameraParameters.FLASH_MODE_ON))
        {
            pref = FlashModeValues.ON;
        }
        else if(flashParam.equals(CameraParameters.FLASH_MODE_OFF))
        {
            pref = FlashModeValues.OFF;
        }
        else if(flashParam.equals(CameraParameters.FLASH_MODE_TORCH))
        {
            pref = FlashModeValues.TORCH;
        }

        return pref;
    }






   
}
