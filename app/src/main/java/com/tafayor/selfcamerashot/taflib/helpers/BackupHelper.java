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
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.channels.FileChannel;
import java.util.Map;



public class BackupHelper
{


    private static final String  EXTENSION_PREF = "pref";
    private static final String  EXTENSION_DB = "db";








    //==============================================================================================
    // Interface
    //==============================================================================================



    //----------------------------------------------------------------------------------------------
    // checkImportDbExists
    //----------------------------------------------------------------------------------------------
    public static  boolean checkImportPrefsExist(Context ctx, String folder, String... prefNames)
    {
        boolean ret ;


        try
        {
            File srcDir = new File(StorageHelper.getStorageDir(AppHelper.getAppName(ctx)),
                    folder);
            ret =  checkImportPrefsExist(srcDir, prefNames);
        }
        catch(Exception ex)
        {
            LogHelper.logx(ex);
            ret = false;
        }

        return ret;
    }


    //----------------------------------------------------------------------------------------------
    // checkImportDbExists
    //----------------------------------------------------------------------------------------------
    public static  boolean checkImportPrefsExist(File srcDir, String... prefNames)
    {

        for(String prefName : prefNames)
        {
            File srcFile = new File(srcDir, prefName+"."+EXTENSION_PREF);
            if(!srcFile.exists()) return false;
        }

        return true;
    }


    //----------------------------------------------------------------------------------------------
    // exportSharedPreferences
    //----------------------------------------------------------------------------------------------
    /**
     * export to sdcar/app_name/folder/prefName.EXTENTION_PREF
     */
    public static   boolean exportSharedPreferences(Context ctx, String folder, String... prefNames)
    {

        boolean ret = false;


        try
        {
            if(!StorageHelper.hasStorage(true)) throw new Exception("Storage not found");

            File dstDir = new File(StorageHelper.getWritableStorageDir(AppHelper.getAppName(ctx)),
                    folder);
            if(!dstDir.exists()) dstDir.mkdir();

            for(String prefName : prefNames)
            {
                File dstFile = new File(dstDir, prefName+"."+EXTENSION_PREF);
                if(!dstFile.exists()) dstFile.createNewFile();

                ret = exportSharedPreferences(ctx, dstFile, prefName);
                if(!ret) break;
            }


        }
        catch(Exception ex)
        {
            LogHelper.logx(ex);
            ret = false;
        }

        return ret;
    }

    //----------------------------------------------------------------------------------------------
    // exportSharedPreferences
    //----------------------------------------------------------------------------------------------
    public static   boolean exportSharedPreferences(Context ctx, File dst, String prefName)
    {

        boolean ret = false;
        ObjectOutputStream output = null;

        try
        {
            output = new ObjectOutputStream(new FileOutputStream(dst));
            SharedPreferences prefs =  ctx.getSharedPreferences(prefName, Context.MODE_PRIVATE);
            output.writeObject(prefs.getAll());

            ret = true;
        }
        catch(Exception ex)
        {
            LogHelper.logx(ex);
            ret = false;
        }

        return ret;
    }


    //----------------------------------------------------------------------------------------------
    // importSharedPreferences
    //----------------------------------------------------------------------------------------------
    /**
     * import from sdcar/app_name/folder/prefName.EXTENTION_PREF
     */
    public static  boolean importSharedPreferences(Context ctx, String folder,String... prefNames)
    {
        boolean ret = false;
        ObjectInputStream input = null;
        try
        {

            if(!StorageHelper.hasStorage(false)) throw new Exception("Storage not found");

            File srcDir = new File(StorageHelper.getStorageDir(AppHelper.getAppName(ctx)),
                    folder);
            for(String prefName : prefNames)
            {
                File srcFile = new File(srcDir, prefName+"."+EXTENSION_PREF);
                if(!srcFile.exists()) throw new Exception("File not found");
                ret = importSharedPreferences(ctx, srcFile, prefName);
                if(!ret) break;
            }


        }
        catch(Exception ex)
        {
            LogHelper.logx(ex);
            ret = false;
        }


        return ret;
    }



    //----------------------------------------------------------------------------------------------
    // importSharedPreferences
    //----------------------------------------------------------------------------------------------
    public static boolean importSharedPreferences(Context ctx, File src, String prefName)
    {
        boolean ret;
        ObjectInputStream input = null;
        try
        {
            input = new ObjectInputStream(new FileInputStream(src));
            SharedPreferences.Editor prefEdit = ctx.getSharedPreferences(prefName, Context.MODE_PRIVATE).edit();
            prefEdit.clear();

            Map<String, ?> entries = (Map<String, ?>) input.readObject();
            for (Map.Entry<String, ?> entry : entries.entrySet())
            {
                Object val = entry.getValue();
                String key = entry.getKey();

                if (val instanceof Boolean) prefEdit.putBoolean(key, ((Boolean) val).booleanValue());
                else if (val instanceof Float) prefEdit.putFloat(key, ((Float) val).floatValue());
                else if (val instanceof Integer) prefEdit.putInt(key, ((Integer) val).intValue());
                else if (val instanceof Long) prefEdit.putLong(key, ((Long) val).longValue());
                else if (val instanceof String) prefEdit.putString(key, ((String) val));
            }

            prefEdit.commit();

            ret = true;
        }
            catch(Exception ex)
            {
                LogHelper.logx(ex);
                ret = false;
            }


        return ret;
    }


    //----------------------------------------------------------------------------------------------
    // checkImportDbExists
    //----------------------------------------------------------------------------------------------
    public static  boolean checkImportDbExists(Context ctx, String folder, String dbName)
    {
        boolean ret ;


        try
        {
            File srcDir = new File(StorageHelper.getStorageDir(AppHelper.getAppName(ctx)),
                    folder);
            ret =  checkImportDbExists(srcDir, dbName);
        }
        catch(Exception ex)
        {
            LogHelper.logx(ex);
            ret = false;
        }

        return ret;
    }


    //----------------------------------------------------------------------------------------------
    // checkImportDbExists
    //----------------------------------------------------------------------------------------------
    public static  boolean checkImportDbExists(File srcDir, String dbName)
    {

        File srcFile = new File(srcDir, dbName+"."+EXTENSION_DB);
        return (srcFile.exists()) ;

    }

    //----------------------------------------------------------------------------------------------
    // exportDatabase
    //----------------------------------------------------------------------------------------------
    /**
     * export to sdcar/app_name/folder/dbName
     */
    public static  boolean exportDatabase(Context ctx, String folder, String dbName)
    {

        boolean ret;

        try
        {
            if(!StorageHelper.hasStorage(true)) throw new Exception("Storage not found");
            File dstDir = new File(StorageHelper.getWritableStorageDir(AppHelper.getAppName(ctx)),
                    folder);
            if(!dstDir.exists()) dstDir.mkdir();

            ret = exportDatabase(ctx, dstDir, dbName);

        }
        catch(Exception ex)
        {
            LogHelper.logx(ex);
            ret = false;
        }

        return ret;

    }


    //----------------------------------------------------------------------------------------------
    // exportDatabase
    //----------------------------------------------------------------------------------------------
    /**
     * export to sdcar/app_name/folder/dbName
     */
    public static  boolean exportDatabase(Context ctx, File dstDir, String dbName)
    {

        boolean ret;

        try
        {
            if(!StorageHelper.hasStorage(true)) throw new Exception("Storage not found");

            File srcFile = ctx.getDatabasePath(dbName);

            if(!dstDir.exists()) dstDir.mkdirs();
            File dstFile = new File(dstDir, srcFile.getName()+"."+EXTENSION_DB);
            if(dstFile.exists()) dstFile.delete();
            dstFile.createNewFile();
            copyFile(srcFile, dstFile);

            ret = true;
        }
        catch(Exception ex)
        {
            LogHelper.logx(ex);
            ret = false;
        }

        return ret;

    }


    //----------------------------------------------------------------------------------------------
    // importDb
    //----------------------------------------------------------------------------------------------
    public static boolean importDatabase(Context ctx, String folder, String dbName, TableSchema[] tableSchemas)
    {
        boolean ret;

        try
        {
            if(!StorageHelper.hasStorage(true)) throw new Exception("Storage not found");

            File srcDir = new File(StorageHelper.getWritableStorageDir(AppHelper.getAppName(ctx)),
                    folder);

            ret = importDatabase(ctx, srcDir, dbName, tableSchemas);
        }
        catch(Exception ex)
        {
            LogHelper.logx(ex);
            ret = false;
        }

        return ret;
    }



    //----------------------------------------------------------------------------------------------
    // importDb
    //----------------------------------------------------------------------------------------------
    public static boolean importDatabase(Context ctx, File srcDir, String dbName, TableSchema[] tableSchemas)
    {
        boolean ret;

        try
        {


            File srcFile = new File(srcDir, dbName+"."+EXTENSION_DB);
            if(!srcFile.exists()) throw new Exception("Import file not found");

            File dstFile = ctx.getDatabasePath(dbName);

            if(!checkDbIsValid(srcFile, tableSchemas))  throw new Exception("Database is not valid");

            if(dstFile.exists()) dstFile.delete();
            dstFile.createNewFile();
            copyFile(srcFile, dstFile);

            ret = true;
        }
        catch(Exception ex)
        {
            LogHelper.logx(ex);
            ret = false;
        }

        return ret;
    }



    //==============================================================================================
    // Internal
    //==============================================================================================

    //----------------------------------------------------------------------------------------------
    // checkDbIsValid
    //----------------------------------------------------------------------------------------------
    private static boolean checkDbIsValid(File dbFile, TableSchema[] tableSchemas)
    {
        boolean ret;
        Cursor cursor = null;
        SQLiteDatabase db = null;

        try
        {
            db = SQLiteDatabase.openDatabase(dbFile.getPath(),
                    null, SQLiteDatabase.OPEN_READONLY);

            for(TableSchema schema : tableSchemas)
            {
                cursor = db.query(true, schema.tableName,
                        null, null, null, null, null, null, null);

                for(String col : schema.columns)
                {
                    // throws exception if column is missing
                    cursor.getColumnIndex(col);
                }
                cursor.close();
            }


            db.close();


            ret = true;
        }
        catch(Exception ex)
        {
            LogHelper.logx(ex);
            ret = false;
        }
        finally
        {
            if(cursor != null) cursor.close();
            if(db != null) db.close();
        }

        return ret;
    }



    //----------------------------------------------------------------------------------------------
    // copyFile
    //----------------------------------------------------------------------------------------------
    private static void copyFile(File src, File dst) throws Exception
    {
        FileChannel inChannel = (new FileInputStream(src)).getChannel();
        FileChannel outChannel = (new FileOutputStream(dst)).getChannel();
        try
        {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        }
        finally
        {
            if(inChannel!=null) inChannel.close();
            if(outChannel!=null) outChannel.close();
        }
    }








    //==============================================================================================
    // Types
    //==============================================================================================


    public static class TableSchema
    {
        public String tableName;
        public String[] columns;

        public TableSchema(String tableName, String[] columns)
        {
            this.tableName = tableName;
            this.columns = columns;
        }

    }


}
