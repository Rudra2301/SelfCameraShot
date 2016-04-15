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
import android.util.Log;

import com.tafayor.selfcamerashot.App;

import java.io.File;
import java.util.Vector;


public class LogHelper
{
    static String TAG = LogHelper.class.getSimpleName();

    static String EXTORAGE_LOG_FOLDER = "logs";


    private static LogCallback mLogCallback = null;
    private static LogCallback mLogCallback3 = null;
    private static LogExceptionCallback mLogExceptionCallback = null;





    //----------------------------------------------------------------------------------------------
    // setLogCallback
    //----------------------------------------------------------------------------------------------
    public static void setLogCallback(LogCallback logCallback)
    {

        mLogCallback = logCallback;

    }


    //----------------------------------------------------------------------------------------------
    // setLogCallback3
    //----------------------------------------------------------------------------------------------
    public static void setLogCallback3(LogCallback logCallback)
    {
        mLogCallback3 = logCallback;

    }


    //----------------------------------------------------------------------------------------------
    // setLogCallback
    //----------------------------------------------------------------------------------------------
    public static void setLogExceptionCallback(LogExceptionCallback logCallback)
    {

        mLogExceptionCallback = logCallback;

    }


    //----------------------------------------------------------------------------------------------
    // log
    //----------------------------------------------------------------------------------------------
    public static void doLog(String msg)
    {
        if(mLogCallback != null) mLogCallback.onLog(msg);
        System.out.println(msg);

    }

    //----------------------------------------------------------------------------------------------
    // log
    //----------------------------------------------------------------------------------------------
    public static void doLog(String tag, String msg)
    {
        doLog(tag + " : " + msg);
    }

    //----------------------------------------------------------------------------------------------
    // log
    //----------------------------------------------------------------------------------------------
    public static void doLog(String mainTag, String subTag , String msg)
    {
        doLog(mainTag + " ->  " + subTag + " : " + msg);
    }




    //----------------------------------------------------------------------------------------------
    // print
    //----------------------------------------------------------------------------------------------
    // see  http://stackoverflow.com/questions/8888654/android-set-max-length-of-logcat-messages
    public static void print(String tag, String text)
    {
        if (text.length() > 4000)
        {
            Log.v(tag, "sb.length = " + text.length());
            int chunkCount = text.length() / 4000;     // integer division
            for (int i = 0; i <= chunkCount; i++) {
                int max = 4000 * (i + 1);
                if (max >= text.length()) {
                    Log.v(tag, "chunk " + i + " of " + chunkCount + ": " + text.substring(4000 * i));
                } else {
                    Log.v(tag, "chunk " + i + " of " + chunkCount + ": " + text.substring(4000 * i, max));
                }
            }
        } else {
            Log.v(tag, text);
        }

    }


    //----------------------------------------------------------------------------------------------
    // log
    //----------------------------------------------------------------------------------------------
    public static void log(String msg)
    {
        log("", msg);

    }

    //----------------------------------------------------------------------------------------------
    // log
    //----------------------------------------------------------------------------------------------
    public static void log(String tag, String msg)
    {
        if(mLogCallback != null) mLogCallback.onLog(tag+ " : " + msg);

        if(App.isDebug())   print(tag, msg);

    }




    //----------------------------------------------------------------------------------------------
    // log
    //----------------------------------------------------------------------------------------------
    public static void log(String mainTag, String subTag , String msg)
    {
        if(mLogCallback3 != null) mLogCallback3.onLog(msg);
        log(mainTag + " ->  " + subTag + " : " + msg);
    }







    //----------------------------------------------------------------------------------------------
    // logx
    //----------------------------------------------------------------------------------------------
    public static void logx(Exception e)
    {
        if(mLogExceptionCallback != null) mLogExceptionCallback.onLogException(e);
        logStack(e);

    }


    //----------------------------------------------------------------------------------------------
    // logx
    //----------------------------------------------------------------------------------------------
    public static void logx(String tag, Exception e)
    {
        log("Exception: " + tag + " : " +  e.getMessage());
        logStack(e);

    }


    //----------------------------------------------------------------------------------------------
    // logx
    //----------------------------------------------------------------------------------------------
    public static void logx(String tag, String subtag, Exception e)
    {
        log("Exception: " + tag ,subtag, e.getMessage());
        logStack(e);
    }

    //----------------------------------------------------------------------------------------------
    // logx
    //----------------------------------------------------------------------------------------------
    public static void logStack( Exception e)
    {
        log("Exception : " + e.getMessage());
        if(App.isDebug())
        {
            e.printStackTrace();
            if(App.isReportMode())
            {
                StackTraceElement[] stack = e.getStackTrace();
                for(StackTraceElement item : stack)
                {
                    log(item.toString() + "\n");
                }
            }

            SoundHelper.beep(App.getContext());
        }
    }


    //----------------------------------------------------------------------------------------------
    // logx
    //----------------------------------------------------------------------------------------------
    public static void doLogx(Exception e)
    {
        if(mLogExceptionCallback != null) mLogExceptionCallback.onLogException(e);
        doLogStack(e);

    }

    //----------------------------------------------------------------------------------------------
    // doLogStack
    //----------------------------------------------------------------------------------------------
    public static void doLogStack( Exception e)
    {
        doLog("Exception : " + e.getMessage());

        e.printStackTrace();
        StackTraceElement[] stack = e.getStackTrace();
        for(StackTraceElement item : stack)
        {
            doLog(item.toString() + "\n");
        }

    }



    //----------------------------------------------------------------------------------------------
    // logw
    //----------------------------------------------------------------------------------------------
    public static void logw(String msg)
    {
        log("Warning: " +msg);
    }


    //----------------------------------------------------------------------------------------------
    // logw
    //----------------------------------------------------------------------------------------------
    public static void logw(String tag, String msg)
    {
        logw(tag + " : " + msg);
    }



    //----------------------------------------------------------------------------------------------
    // logw
    //----------------------------------------------------------------------------------------------
    public static void logw(String mainTag, String subTag , String msg)
    {
        logw(mainTag + " ->  " + subTag + " : " + msg);
    }




    //----------------------------------------------------------------------------------------------
    // loge
    //----------------------------------------------------------------------------------------------
    public static void loge(String msg)
    {
        log("Error: " + msg);
    }

    //----------------------------------------------------------------------------------------------
    // loge
    //----------------------------------------------------------------------------------------------
    public static void loge(String tag, String msg)
    {
        loge(tag + " : " + msg);
    }


    //----------------------------------------------------------------------------------------------
    // loge
    //----------------------------------------------------------------------------------------------
    public static void loge(String mainTag, String subTag , String msg)
    {
        loge(mainTag + " ->  " + subTag + " : " + msg);
    }





    //----------------------------------------------------------------------------------------------
    // getLogFilePaths
    //----------------------------------------------------------------------------------------------
    public Vector<String> getLogFilePaths(Context ctx)
    {
        Vector<String> files = new Vector<String>();

        File logDirFile = new File(PackageHelper.getTmpDir(ctx));
        File[] entries = logDirFile.listFiles();

        for(File entry : entries)
        {
            if(entry.isFile() && entry.getName().contains(".log"))
            {
                files.add(entry.getAbsolutePath());
            }
        }


        return files;
    }

    //----------------------------------------------------------------------------------------------
    // exportLogFiles
    //----------------------------------------------------------------------------------------------
    /*public  boolean exportLogFiles() throws MsgException
    {
        boolean result = true;
        boolean ret ;
        String extorageLogDir ;
        Vector<String> logFilePaths;

         try
        {
            extorageLogDir = getExtorageLogDir();
            if(extorageLogDir == null) throw new StrException("Failed to get log directory on external storage");

            logFilePaths = getLogFilePaths();
            if(logFilePaths.size()>0)
            {
                for(String file : logFilePaths)
                {
                    ret = App.getIOHelper().copyFileToDir(file, extorageLogDir);
                    if(!ret)  throw new StrException("Failed to copy log file to the external storage : " + file);

                }
            }
            else
            {
                throw new StrException("Log files not found",
                        App.getAppHelper().getResString(R.string.prefMsgExportLogNotFound));
            }

            throw new MsgException(App.getAppHelper().getResString(R.string.prefMsgExportLogSuccess) +  extorageLogDir);

        }
        catch(StrException e)
        {
            LogHelper.log(TAG, "exportLogFiles", e.getMessage());
            result = false;
            if(e.isUserMsg()) throw new MsgException(e);
        }
        return result;
    }*/



    public interface LogCallback
    {
        public void onLog(String msg);
    }
    public interface LogExceptionCallback
    {
        public void onLogException(Exception ex);
    }
}


