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

import android.os.Environment;

import java.io.File;


public class StorageHelper
{

    public static String TAG = StorageHelper.class.getSimpleName();






        //----------------------------------------------------------------------------------------------
    // getStorageDir
    //----------------------------------------------------------------------------------------------
    public static  File getStorageDir(String folder) throws Exception {

        File storagePathFile = null;

        try
        {
            if(!hasStorage(false))throw new Exception("External storage not found");
            storagePathFile = new File(Environment.getExternalStorageDirectory(),
                    folder);
        }
        catch(Exception e)
        {
            LogHelper.logx(e);
            throw e;
        }

        return storagePathFile;
    }


    //----------------------------------------------------------------------------------------------
    // getWritableStorageDir
    //----------------------------------------------------------------------------------------------
    public static  File getWritableStorageDir(String folder) throws Exception {

        File storagePathFile = null;

        try
        {
            if(!hasStorage(true))throw new Exception("Writable external storage not found");
            storagePathFile = new File(Environment.getExternalStorageDirectory(),
                    folder);
            if(!storagePathFile.exists()) storagePathFile.mkdirs();

        }
        catch(Exception e)
        {
            LogHelper.logx(e);
            throw e;
        }

        return storagePathFile;
    }




    //----------------------------------------------------------------------------------------------
    // hasStorage
    //----------------------------------------------------------------------------------------------
    public static boolean hasStorage(boolean requireWriteAcess)
    {

        boolean ret;
        boolean writable,available;
        try
        {
            String state = Environment.getExternalStorageState();

            if(state.equals(Environment.MEDIA_MOUNTED))
            {
                writable = available = true;
            }
            else if(state.equals(Environment.MEDIA_MOUNTED_READ_ONLY))
            {
                available = true;
                writable = false;
            }
            else
            {
                writable = available = false;
            }



            ret = (requireWriteAcess)? (available && writable) : available;

        }
        catch (Exception ex)
        {
            LogHelper.logx(ex);
            ret = false;
        }

        return ret;
    }








}
