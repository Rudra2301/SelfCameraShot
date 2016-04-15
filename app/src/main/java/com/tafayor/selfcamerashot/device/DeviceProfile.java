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


package com.tafayor.selfcamerashot.device;

import android.content.Context;
import android.os.Build;

import com.tafayor.selfcamerashot.camera.CameraParameters;
import com.tafayor.selfcamerashot.taflib.helpers.LogHelper;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DeviceProfile
{

    private static String MODEL_GALAXY_S3_NEO = "GT-I9301I";
    private static String MODEL_NEXUS_4 = "Nexus 4";
    private static String MODEL_GALAXY_ACE_4 = "SM-G357FZ";

    private static String MODEL_BLADE_G_LTE = "Blade G LTE";
    private static String MODEL_SPLENDOR = "LG-US730";
    private static String MODEL_XPERIA_S = "LT26i";
    private static String MODEL_Z740 = "Z740";
    private static String MODEL_GALAXY_S3_M0 = "GT-I9300";
    private static String MODEL_XPERIA_L_C2104 = "C2104";
    private static String MODEL_XPERIA_L_C2105 = "C2105";
    private static String MODEL_Xperia_M_C1905 = "C1905";


    Context mContext;
    HashMap<Integer, Constraint> mConstraints;
    private Integer mCameraId;




    class Constraint
    {
        List<String> excludedPictureSizes = new ArrayList<>();
        List<String> excludedParams= new ArrayList<>();

    }




    public DeviceProfile()
    {

        init();
    }


    void init()
    {
        mConstraints = new HashMap<>();
        LogHelper.log("Build.MODEL : " + Build.MODEL);
        loadConstraints(Build.MODEL);

    }





    public void queryCamera(int camId)
    {
        mCameraId = camId;
    }



    public boolean hasConstraints()
    {


        boolean ret = (mConstraints.size()>0 );
        return ret;
    }



    public List<String> getExcludedPictureSizes()
    {
        List<String> sizes = new ArrayList<>();
        if(hasConstraints()) sizes = mConstraints.get(0).excludedPictureSizes;
        return sizes;
    }

    public boolean excludesParam(String param)
    {
        return mConstraints.get(0).excludedParams.contains(param);
    }










    void loadConstraints(String deviceModel)
    {

        Constraint ct = new Constraint();
        Integer cameraId ;

        if(deviceModel.equalsIgnoreCase(MODEL_NEXUS_4))
        {
            ct.excludedPictureSizes.add("1280x960");
            mConstraints.put(0, ct);

        }
        else if(deviceModel.equalsIgnoreCase(MODEL_GALAXY_ACE_4))
        {

            ct.excludedParams.add(CameraParameters.KEY_ZSL);
            mConstraints.put(0, ct);

        }
        else if(deviceModel.equalsIgnoreCase(MODEL_Xperia_M_C1905))
        {

            ct.excludedParams.add(CameraParameters.KEY_DENOISE);
            mConstraints.put(0, ct);

        }
        else if(deviceModel.equalsIgnoreCase(MODEL_GALAXY_S3_M0))
        {

            ct.excludedParams.add(CameraParameters.KEY_DENOISE);
            mConstraints.put(0, ct);

        }
        else if(deviceModel.equalsIgnoreCase(MODEL_Z740))
        {
            ct.excludedParams.add(CameraParameters.KEY_DENOISE);
            mConstraints.put(0, ct);

        }
        else if(deviceModel.equalsIgnoreCase(MODEL_XPERIA_L_C2104)
                || deviceModel.equalsIgnoreCase(MODEL_XPERIA_L_C2105) )
        {

            ct.excludedParams.add(CameraParameters.KEY_DENOISE);
            mConstraints.put(0, ct);

        }
        else if(deviceModel.equalsIgnoreCase(MODEL_XPERIA_S))
        {

            ct.excludedParams.add(CameraParameters.KEY_DENOISE);
            mConstraints.put(0, ct);

        }
        else if(deviceModel.equalsIgnoreCase(MODEL_SPLENDOR))
        {

            ct.excludedParams.add(CameraParameters.KEY_DENOISE);
            mConstraints.put(0, ct);

        }
        else if(deviceModel.equalsIgnoreCase(MODEL_BLADE_G_LTE))
        {

            ct.excludedParams.add(CameraParameters.KEY_DENOISE);
            mConstraints.put(0, ct);

        }








    }

}
