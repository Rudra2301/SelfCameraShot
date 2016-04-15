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
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;



public class DBHelper
{


    private static final String  DB_PATH_SUFFIX  = "/databases/";









    //==============================================================================================
    // Interface
    //==============================================================================================




    public static boolean checkDbExists(Context ctx, String dbName)
    {


        String path = getDatabasePath(ctx, dbName);

        File dbFile = new File(path);
        return dbFile.exists();
    }



    public static void copyDataBaseFromAsset(Context ctx, String folder, String dbFileName) throws IOException
    {

        String srcDbPath = (folder!=null)? folder+File.separator+dbFileName : dbFileName;

        InputStream myInput = ctx.getAssets().open(srcDbPath);

        String dbName = dbFileName;
        if(dbFileName.contains("."))
        {
            dbName = dbFileName.substring(0, dbFileName.lastIndexOf("."));
        }

        // Path to the just created empty db
        String outFileName = getDatabasePath(ctx, dbName);

        // if the path doesn't exist first, create it
        File f = new File(getDatabaseDir(ctx));
        if (!f.exists()) f.mkdir();


        // Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        // transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        // Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }



    private static String getDatabasePath(Context ctx, String dbName)
    {
        return ctx.getApplicationInfo().dataDir + DB_PATH_SUFFIX
                + dbName;
    }

    private static String getDatabaseDir(Context ctx)
    {
        return ctx.getApplicationInfo().dataDir + DB_PATH_SUFFIX;
    }





    //----------------------------------------------------------------------------------------------
    // checkDbIsValid
    //----------------------------------------------------------------------------------------------
    public static boolean checkDbIsValid(File dbFile, TableSchema[] tableSchemas)
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
