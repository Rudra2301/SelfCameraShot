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


package com.tafayor.selfcamerashot.gallery;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;

import android.location.Location;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;


import com.nononsenseapps.filepicker.FilePickerActivity;
import com.tafayor.selfcamerashot.App;
import com.tafayor.selfcamerashot.R;
import com.tafayor.selfcamerashot.taflib.helpers.AppHelper;
import com.tafayor.selfcamerashot.taflib.helpers.IOHelper;
import com.tafayor.selfcamerashot.taflib.helpers.IntentHelper;
import com.tafayor.selfcamerashot.taflib.helpers.LogHelper;
import com.tafayor.selfcamerashot.taflib.helpers.MsgHelper;


import java.io.File;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class GalleryManager  implements MediaScannerConnection.MediaScannerConnectionClient
{
    public static String TAG = GalleryManager.class.getSimpleName();



    private static final int PICTURE_QUALITY = 100;
    private WeakReference<Activity> mActivityPtr;
    private static final String INTENT_TYPE_IMAGE ="image/jpeg";
    private static final String GENERIC_IMAGE_TYPE="image/*";

    private MediaScannerConnection mMediaScanner;
    private Context mContext;
    private boolean mUseMediaScannerPath;



    public GalleryManager(Activity activity)
    {
        mActivityPtr = new WeakReference<Activity>(activity);
        mContext = activity.getApplicationContext();
    }

    //==============================================================================================
    // Interface
    //==============================================================================================


    //----------------------------------------------------------------------------------------------
    // getLastPicPath
    //----------------------------------------------------------------------------------------------
    public  String  getLastPicPath()
    {
        File file = null;
        try {
            file = IOHelper.getLatestFilefromDir(getPicsDirectory().getPath());
        } catch (Exception e) {
            LogHelper.logx(TAG, e);
        }
        return file.getPath();
    }



    //----------------------------------------------------------------------------------------------
    // showLatestImage
    //----------------------------------------------------------------------------------------------
    public void showLatestImage()
    {
        Activity activity = mActivityPtr.get();
        if(activity == null) return;

        if(Build.VERSION.SDK_INT < 14)
        {
            if(AppHelper.getLanguage().equals("ar")) mUseMediaScannerPath = true;
            else mUseMediaScannerPath = false;
            showLastImageUsingMediaScanner();

            return;
        }

        Cursor cursor = getLatestImageCursor();
        if(cursor != null)
        {
            int imageID = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media._ID));
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            Uri uri;
            if(AppHelper.getLanguage().equals("ar"))
            {
                 uri = Uri.fromFile(new File(path));
            }
            else
            {
                uri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, Integer.toString(imageID));
            }


            if (uri != null)
            {
                IntentHelper.openImageUri(activity, uri);
            }
        }



    }


    //----------------------------------------------------------------------------------------------
    // showGallery
    //----------------------------------------------------------------------------------------------
    public void showGallery()
    {
        Activity activity = mActivityPtr.get();
        if(activity == null) return;


        if(Build.VERSION.SDK_INT < 14)
        {
            mUseMediaScannerPath = false;
            showLastImageUsingMediaScanner();
            return;
        }

        Cursor cursor = getLatestImageCursor();
        if(cursor != null)
        {
            int imageID = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media._ID));
            Uri uri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, Integer.toString(imageID));

            if (uri != null)
            {
                IntentHelper.openImageUri(activity, uri);
            }
        }




    }


    //----------------------------------------------------------------------------------------------
    // getLatestImageUri
    //----------------------------------------------------------------------------------------------
    private Cursor getLatestImageCursor()
    {
        String[] projection = new String[]{
                MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATE_TAKEN,
                MediaStore.Images.ImageColumns.MIME_TYPE};

        Cursor cursor = mContext.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME + "=?",
                new String[]{getAlbumName()},
                MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");
        if(cursor != null && cursor.getCount()>0)
        {
            cursor.moveToFirst();

            return cursor;
        }
        else
        {
            MsgHelper.toastSlow(mContext, mContext.getResources().getString(R.string.gallery_alert_empty));
            return null;
        }
    }

    //----------------------------------------------------------------------------------------------
    // showLastImageUsingMediaScanner
    //----------------------------------------------------------------------------------------------
    public void showLastImageUsingMediaScanner ()
    {
        Activity activity = mActivityPtr.get();
        if (activity == null) return;



        if(mMediaScanner!=null)
        {
            mMediaScanner.disconnect();
        }
        mMediaScanner = new MediaScannerConnection(activity,this);
        mMediaScanner.connect();


    }

    //----------------------------------------------------------------------------------------------
    // getAlbumName
    //----------------------------------------------------------------------------------------------
    public String getAlbumName()
    {
        return mContext.getString(R.string.app_name).replace(" ", "");
    }



    //----------------------------------------------------------------------------------------------
    // getDefaultPicsDirectory
    //----------------------------------------------------------------------------------------------
    public  File getDefaultPicsDirectory() throws Exception
    {

        File globalPicsDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM);

        File appPicsDir = new File(globalPicsDir, getAlbumName());


        if (!appPicsDir.exists())
        {
            if (!appPicsDir.mkdirs())
            {
                throw new Exception("Failed to create pictures directory!");
            }
        }


        return appPicsDir;
    }


    //----------------------------------------------------------------------------------------------
    // getCustomPicsDirectory
    //----------------------------------------------------------------------------------------------
    public  File getCustomPicsDirectory() throws Exception
    {

       String path = App.getAdvancedCameraPrefHelper().getCustomStorage();

        if(!path.isEmpty())
        {
            File appPicsDir = new File(path, getAlbumName());

            if (!appPicsDir.exists())
            {
                if (!appPicsDir.mkdirs())
                {
                    throw new Exception("Failed to create pictures directory!");
                }
            }

            return  appPicsDir;
        }
        else return null;

    }


    //----------------------------------------------------------------------------------------------
    // getPicsDirectory
    //----------------------------------------------------------------------------------------------
    public  File getPicsDirectory() throws Exception
    {

        if(App.getAdvancedCameraPrefHelper().getEnableCustomStorage())
        {
            return getCustomPicsDirectory();

        }
        else
        {
            return getDefaultPicsDirectory();
        }

    }




    public static void writeFile(String path, byte[] data) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(path);
            out.write(data);
        } catch (Exception e) {
            Log.e(TAG, "Failed to write data", e);
        } finally {
            try {
                out.close();
            } catch (Exception e) {
            }
        }
    }

    //----------------------------------------------------------------------------------------------
    // savePitcture
    //----------------------------------------------------------------------------------------------
    public  String  savePicture(byte[] data, int w, int h, int orientation,
                                boolean hflip, boolean vflip, Location location)
    {
        String imagePath = null;
        boolean ignoreExif = false;

        try
        {
           // orientation = 90;

            Activity activity = mActivityPtr.get();
            if(activity == null) return null;

            File picsDirectory = getPicsDirectory();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File imageFile = new File(picsDirectory.getPath() + File.separator + "PHOTO_"+ timeStamp + ".jpg"
            );

            if(imageFile.exists()) imageFile.delete();
            imagePath = imageFile.getAbsolutePath();



            writeFile(imagePath, data);

            if(hflip || vflip)
            {
                 if(hflip != vflip && (orientation % 180 != 0))
                {
                    hflip = !hflip;
                    vflip = !vflip;
                }
                boolean ret = ImageUtils.flip(imagePath, hflip, vflip);
                if(!ret) writeFile(imagePath, data);
            }

            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, imageFile.getName());
            values.put(MediaStore.Images.Media.DESCRIPTION, "");
            values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis ());
            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis ());
            values.put(MediaStore.Images.Media.DATA, imageFile.getAbsolutePath());
            values.put(MediaStore.Images.Media.WIDTH, w);
            values.put(MediaStore.Images.Media.HEIGHT, h);
            values.put(MediaStore.Images.Media.ORIENTATION, orientation);

            if (location != null)
            {
                values.put(MediaStore.Images.Media.LATITUDE, location.getLatitude());
                values.put(MediaStore.Images.Media.LONGITUDE, location.getLongitude());
            }

            File parent = imageFile.getParentFile();
            String path = parent.toString().toLowerCase(Locale.US);
            String name = parent.getName().toLowerCase(Locale.US);
            values.put(MediaStore.Images.ImageColumns.BUCKET_ID, path.hashCode());
            values.put(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME, name);



            try
            {
                ContentResolver cr = mContext.getContentResolver();
                Uri uri = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            }
            catch(Exception e)
            {
                LogHelper.logx(e);
            }





           /* FileOutputStream os;
            os = (FileOutputStream) mContext.getContentResolver().openOutputStream(uri);
            os.write(data);
            os.flush();
            os.close();*/

            LogHelper.log("Picture was saved as " + imageFile.getPath());


        }
        catch(Exception ex)
        {
            LogHelper.logx(TAG, ex);
            MsgHelper.toastLong(mContext, mContext.getResources().getString(R.string.alert_error_photoSaveFailed));
            return null;
        }






        return imagePath;



    }





    //----------------------------------------------------------------------------------------------
    // savePitcture
    //----------------------------------------------------------------------------------------------
    public  String savePicture(Bitmap bitmap, int orientation)
    {
        String path = null;
        try
        {


            Activity activity = mActivityPtr.get();
            if(activity == null) return null;

            File picsDirectory = getPicsDirectory();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File imageFile = new File(picsDirectory.getPath() + File.separator + "PHOTO_"+ timeStamp + ".jpg"
            );

            if(imageFile.exists()) imageFile.delete();

            FileOutputStream stream = new FileOutputStream(imageFile);

            bitmap.compress(Bitmap.CompressFormat.JPEG, PICTURE_QUALITY, stream);


            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, imageFile.getName());
            values.put(MediaStore.Images.Media.DESCRIPTION, "");
            values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis ());
            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis ());
            values.put(MediaStore.Images.Media.DATA, imageFile.getAbsolutePath());
            values.put(MediaStore.Images.Media.ORIENTATION, orientation);

            File parent = imageFile.getParentFile();
            String parentPath = parent.toString().toLowerCase(Locale.US);
            String name = parent.getName().toLowerCase(Locale.US);
            values.put(MediaStore.Images.ImageColumns.BUCKET_ID, parentPath.hashCode());
            values.put(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME, name);


            ContentResolver cr = mContext.getContentResolver();
            cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);


            LogHelper.log("Picture was saved as " + imageFile.getPath());
            path = imageFile.getPath();

        }
        catch(Exception ex)
        {
            LogHelper.logx(TAG, ex);
        }
        finally
        {
            bitmap.recycle();
        }


        return path;
    }





    //==============================================================================================
    // Internals
    //==============================================================================================






    //==============================================================================================
    // Implementation
    //==============================================================================================




    //=====================================================
    // MediaScannerConnection.MediaScannerConnectionClient
    //=====================================================

    @Override
    public void onMediaScannerConnected()
    {

        try
        {

            String picsDirPath = getPicsDirectory().getPath() ;
            File scanFile = IOHelper.getLatestFilefromDir(picsDirPath);

            if(scanFile != null)
            {
                mMediaScanner.scanFile(scanFile.getPath(), INTENT_TYPE_IMAGE);

            }
            else
            {
               MsgHelper.toastSlow(mContext, mContext.getResources().getString(R.string.gallery_alert_empty));
            }


        }
        catch (Exception ex)
        {
            LogHelper.logx(TAG, ex);
        }
    }

    @Override
    public void onScanCompleted(String path, Uri uri)
    {
        Activity activity = mActivityPtr.get();
        if(activity == null) return;
        try
        {

            if (uri != null)
            {
                if(mUseMediaScannerPath)
                {
                    uri = Uri.fromFile(new File(path));
                }

                IntentHelper.openImageUri(activity, uri);
            }
        } finally
        {
            mMediaScanner.disconnect();
            mMediaScanner = null;
        }
    }




    //==============================================================================================
    // Types
    //==============================================================================================



}
