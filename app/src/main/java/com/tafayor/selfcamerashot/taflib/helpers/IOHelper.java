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
import android.os.Environment;


import com.tafayor.selfcamerashot.taflib.types.StrException;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by youssef on 03/12/13.
 */
public class IOHelper
{
    static String TAG = "IOHelper";


 
    //----------------------------------------------------------------------------------------------
    //getLatestFilefromDir
    //----------------------------------------------------------------------------------------------
    public static  File getLatestFilefromDir(String dirPath)
    {
        File dir = new File(dirPath);

        File[] files = dir.listFiles(new FileFilter()
        {
            public   boolean accept(File file) {
                return file.isFile();
            }
        });

        if (files == null || files.length == 0) {
            return null;
        }

        long lastMod = Long.MIN_VALUE;
        File lastModifiedFile = null;
        for (File file : files)
        {
            if (file.lastModified() > lastMod)
            {
                lastModifiedFile = file;
                lastMod = file.lastModified();
            }
        }
        return lastModifiedFile;
    }




    public static  void deletePathRecursive(File path)
    {
        if (path.isDirectory())
        {
            for (File child : path.listFiles())
            {
                deletePathRecursive(child);
            }
        }
        path.delete();
    }



    public static  String readFile(String path)
    {
        File file = new File(path);
        return readFile(file);
    }


    public static  String readFile(File file)
    {
        FileInputStream fis = null;
        String content = null;
        StringBuffer strBuffer =  new StringBuffer("");
        try
        {
            fis = new FileInputStream(file);
            //byte[] data = new byte[(int)file.length()];
            //fis.read(data);

            int chr;
            while ((chr = fis.read()) != -1)
            {
                strBuffer.append((char) chr);
            }

            fis.close();

           if(strBuffer.length()>0) content = strBuffer.toString();

        }
        catch (Exception e)
        {
            LogHelper.log(TAG, "readFile", e.getMessage());
        }
        finally
        {
            if(fis != null)
            {
                try
                {
                    fis.close();
                }
                catch (IOException e){}
            }

        }

        return content;

    }



    public static  void writeToFile(String path, String content)
    {
        File file = new File(path);
        writeToFile(file, content);
    }


    public static  void writeToFile(File file, String content)
    {
        FileWriter fileWriter = null;
        try
        {
            fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.close();
        }
        catch (IOException ex)
        {
            LogHelper.logx(ex);
        }
        finally
        {
            try
            {
                fileWriter.close();
            } catch (IOException ex)
            {
                LogHelper.logx(ex);
            }
        }
    }


    public static  boolean fileExists(String path)
    {
        File file = new File(path);
        return file.exists();
    }


    public static  boolean folderExists(String path)
    {
        File folder = new File(path);
        return (folder.exists() && folder.isDirectory());
    }


    public static  boolean isFile(String path)
    {
        File file = new File(path);
        return file.isFile();
    }

    public static  boolean isFolder(String path)
    {
        File file = new File(path);
        return file.isDirectory();
    }

    public static  boolean deletePath(String path)
    {
        boolean result;
        try
        {

            File serverFile = new File(path);
            if(!serverFile.exists()) throw new StrException("Path not found", true);
            if(!serverFile.delete()) throw new StrException("Failed to remove the path");
            result = true;
        }
        catch(StrException e)
        {
            LogHelper.log(TAG, "deletePath", e.getMessage());
            result = e.ret();
        }

        return result;
    }


    public static  String getSystemPath()
    {
        return "/system/";

    }


    public static  String getWritableExtoragePath()
    {
        String path = null;

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {
            path =  Environment.getExternalStorageDirectory().getAbsolutePath()+"/";
        }

        return path;
    }


    public static  String getExtoragePath()
    {
        String path = null;

        String extorageState = Environment.getExternalStorageState();
        if (extorageState.equals(Environment.MEDIA_MOUNTED_READ_ONLY) || extorageState.equals(Environment.MEDIA_MOUNTED_READ_ONLY))
        {
            path =  Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
        }

        return path;
    }




    public static  String getExtorageAppDir(Context ctx)
    {
        String result = null;
        String extoragePath ;
        File extorageAppPath ;

        try
        {
            extoragePath = getWritableExtoragePath();
            if(extoragePath == null) throw new StrException("Writable external storage not found");
            extorageAppPath = new File(extoragePath + AppHelper.getAppName(ctx) );

            if(!extorageAppPath.exists())
            {
                if(!extorageAppPath.mkdir()) throw new StrException("Failed to create app folder on external storage");
            }

            result = extorageAppPath.getAbsolutePath() + "/";
        }
        catch(StrException e)
        {
            LogHelper.logx(e);
        }


        return result;
    }










    public static  boolean createFolder(String folderName , String path)
    {
        File folder = new File(path + folderName);
        boolean result ;
        boolean ret = true;

        try
        {
            if (!folder.exists())
            {
                ret = folder.mkdir();
                if(!ret) throw new StrException("Failed to create folder " + folderName);
            }

            result = true;
        }
        catch(StrException e)
        {
            LogHelper.logx(e);
            result = false;
        }


        return result;
    }




    public static  String getFilename(String path)
    {
        return (new File(path)).getName();
    }


    public static  boolean copyFileToDir(String srcPath, String desDirPath)
    {
        return copyFile(srcPath, desDirPath + getFilename(srcPath));
    }


    public static  boolean copyFile(String srcPath, String desPath)
    {
        LogHelper.log(TAG, "copyFile", "src: "+srcPath + " , des : "+desPath);
        InputStream is = null;
        OutputStream os = null;
        File srcFile;
        File desFile;
        File desDir;
        String path="";
        boolean result = false;
        String fileName;


        try
        {
            srcFile = new File(srcPath);
            if (!srcFile.exists()) throw new StrException("Source file not found");
            fileName = srcFile.getName();

            desFile = new File(desPath);
            desDir = desFile.getParentFile();
            if (!desDir.exists())
            {
                if(!desDir.mkdirs()) throw new StrException("Failed to create destination folder");
            }

            if (desFile.exists())
            {
                if(!desFile.delete()) throw new StrException("Failed to replace target file");
            }

            if(!desFile.createNewFile()) throw new StrException("Failed to create target file");

            is = new FileInputStream(srcFile);
            os = new FileOutputStream(desFile)  ;

            copyStream(is, os);

            result = true;
        }
        catch(StrException e)
        {
            LogHelper.log(TAG, "copyFile", e.getMessage());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                is.close();
                is = null;
                os.flush();
                os.close();
                os = null;
            }
            catch(Exception e){}
        }

        return result;
    }


    public static   void copyStream(InputStream in, OutputStream out) throws IOException
    {
        byte[] buffer = new byte[16 * 1024];
        int read;
        while((read = in.read(buffer)) != -1)
        {
            out.write(buffer, 0, read);
        }
    }

}


