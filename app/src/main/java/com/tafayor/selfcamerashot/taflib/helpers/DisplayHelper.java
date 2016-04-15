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

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Surface;
import android.view.Window;
import android.view.WindowManager;

import com.tafayor.selfcamerashot.taflib.types.Size;



public class DisplayHelper
{
    public static String TAG = DisplayHelper.class.getSimpleName();




    public static int ORIENTATION_PORTRAIT = 0;
    public static int ORIENTATION_LANDSCAPE = 1;

    public static int DEVICE_TYPE_PORTRAIT = 0;
    public static int DEVICE_TYPE_LANDSCAPE= 1;
    public static int DEVICE_TYPE_SQUARE= 2;

    static final int DEFAULT_ACCELEROMETER_ROTATION = 0;

   

    public static  int sStatusBarHeight = 0;



  

    public static int getNavigationBarHeight(Context ctx, int orientation)
    {
        Resources resources = ctx.getResources();

        int id = resources.getIdentifier(
                orientation == Configuration.ORIENTATION_PORTRAIT ? "navigation_bar_height" : "navigation_bar_height_landscape",
                "dimen", "android");
        if (id > 0) {
            return resources.getDimensionPixelSize(id);
        }
        return 0;
    }



    @TargetApi(19)
    public static void setTranslucentStatus(Activity activity, Boolean state)
    {
        Window win = activity.getWindow();
        WindowManager winManager = win.getWindowManager();
        WindowManager.LayoutParams attributes = win.getAttributes();
        int flagBits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if(state)
        {
            attributes.flags |= flagBits;
        }
        else
        {
            attributes.flags &= ~flagBits;
        }
        win.setAttributes(attributes);
    }



    public static int getStatusBarHeight(Activity activity)
    {


        if(activity != null && sStatusBarHeight == 0)
        {
            Rect rectangle= new Rect();
            Window window= activity.getWindow();
            window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
            sStatusBarHeight = rectangle.top;
        }


        return sStatusBarHeight;
    }


    public static boolean isOrientationLocked(Context ctx)
    {
        int ret = Settings.System.getInt(ctx.getContentResolver(),
                   Settings.System.ACCELEROMETER_ROTATION, DEFAULT_ACCELEROMETER_ROTATION);

        return (ret == 1);
    }


    public static int getDeviceType(Context ctx)
    {
        WindowManager winManager = (WindowManager) ctx.getSystemService(Activity.WINDOW_SERVICE);
        int rotation = winManager.getDefaultDisplay().getRotation();
        DisplayMetrics dm = new DisplayMetrics();
        winManager.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        int deviceType;


        if ( ((rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) && height > width)
             || ((rotation == Surface.ROTATION_90|| rotation == Surface.ROTATION_270) && width > height)
           )
        {
            deviceType = DEVICE_TYPE_PORTRAIT;
        }
        else if(height == width)
        {
            deviceType = DEVICE_TYPE_SQUARE;
        }
        else
        {
            deviceType = DEVICE_TYPE_LANDSCAPE;
        }

        return deviceType;
    }



    public static int getDeviceDefaultOrientation(Context ctx)
    {

        WindowManager windowManager =  (WindowManager) ctx.getSystemService(Activity.WINDOW_SERVICE);

        Configuration config = ctx.getResources().getConfiguration();

        int rotation = windowManager.getDefaultDisplay().getRotation();

        if ( ((rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) &&
                config.orientation == Configuration.ORIENTATION_LANDSCAPE)
                || ((rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) &&
                config.orientation == Configuration.ORIENTATION_PORTRAIT)) {
            return Configuration.ORIENTATION_LANDSCAPE;
        } else {
            return Configuration.ORIENTATION_PORTRAIT;
        }
    }


    /**
     * Orientation relative to device type
     * @return
     */
    public static int getScreenOrientation(Context ctx)
    {
        WindowManager winManager = (WindowManager) ctx.getSystemService(Activity.WINDOW_SERVICE);
        int rotation = winManager.getDefaultDisplay().getRotation();
        int orientation;
        int deviceType;

        orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        deviceType = getDeviceType(ctx);
        int confOrientation = ctx.getResources().getConfiguration().orientation;

        if( (deviceType == DEVICE_TYPE_PORTRAIT && confOrientation == Configuration.ORIENTATION_PORTRAIT)
            || (deviceType == DEVICE_TYPE_LANDSCAPE && confOrientation == Configuration.ORIENTATION_PORTRAIT)
                )
        {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
                orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
            } else {

                if (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_180) {
                    orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                } else {
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                }
            }

        }
        else  if( (deviceType == DEVICE_TYPE_PORTRAIT && confOrientation == Configuration.ORIENTATION_LANDSCAPE)
                || (deviceType == DEVICE_TYPE_LANDSCAPE && confOrientation == Configuration.ORIENTATION_LANDSCAPE)
                )
        {
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO)
            {
                orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
            }
            else
            {

                if(rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_90)
                {
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                }
                else
                {
                    orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                }
            }
        }




        return orientation;
    }



    public static int getRelativeScreenOrientation(Context ctx)
    {
        WindowManager winManager = (WindowManager) ctx.getSystemService(Activity.WINDOW_SERVICE);
        int rotation = winManager.getDefaultDisplay().getRotation();
        int orientation;

        switch(rotation) {
            case Surface.ROTATION_0:
                orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

                break;
            case Surface.ROTATION_90:
                orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;

                break;
            case Surface.ROTATION_180:
                orientation =  ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;

                break;
            case Surface.ROTATION_270:
                orientation =  ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;

                break;
            default:
                LogHelper.log(TAG, "Unknown screen orientation");
                orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                break;
        }


        return orientation;
    }





    public static int getScreenOrientationAngle(Context ctx)
    {
        int orientation = getScreenOrientation(ctx);
        int angle = 0 ;

        if(orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) angle = 90;
        else if(orientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT) angle = 180;
        else if(orientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE) angle = 270;

        return angle;
    }


    public static int getRelativeScreenOrientationAngle(Context ctx)
    {
        int orientation = getRelativeScreenOrientation(ctx);
        int angle = 0 ;

        if(orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) angle = 90;
        else if(orientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT) angle = 180;
        else if(orientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE) angle = 270;

        return angle;
    }



    public static boolean isPortrait(Context ctx)
    {
        int orientation = getScreenOrientation(ctx);
        return ( orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT ||
                orientation ==  ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
    }

    public static boolean isAbsPortrait(Context ctx)
    {
        int orientation = getRelativeScreenOrientation(ctx);
        return ( orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT ||
                orientation ==  ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
    }

    public static boolean isLandscape(Context ctx)
    {
        int orientation = getScreenOrientation(ctx);
        return ( orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE ||
                orientation ==  ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
    }

    public static boolean isAbsLandscape(Context ctx)
    {
        int orientation = getRelativeScreenOrientation(ctx);
        return ( orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE ||
                orientation ==  ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
    }

    public static boolean isDeviceTypePortrait(Context ctx)
    {
        int deviceType = getDeviceType(ctx);
        return (deviceType == DEVICE_TYPE_PORTRAIT);
    }

    public static boolean isDeviceTypeLanscape(Context ctx)
    {
        int deviceType = getDeviceType(ctx);
        return (deviceType == DEVICE_TYPE_LANDSCAPE);
    }





    public static void lockCurrentOrientation(Activity activity)
    {
        int orientation = getScreenOrientation(activity.getApplicationContext());

        if(orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        else if(orientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT)
        {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
        }
        else if(orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
        {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        else if(orientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE)
        {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
        }


    }


    /**
     * The returned size may
     * be adjusted to exclude certain system decoration elements that are always visible.
     * It may also be scaled to provide compatibility with older applications that
     * were originally designed for smaller displays.
     */
    public static Size getScreenSize(Context ctx)
    {
        Size size = new Size();

        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager winManager = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        Display display = winManager.getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2)
        {
            Point  _size = new Point();
            display.getSize(_size);
            size.width = _size.x;
            size.height = _size.y;
        }
        else
        {
            size.width = display.getWidth();
            size.height = display.getHeight();
        }

        return size;
    }



    /**
     *  Gets the real size of the display without subtracting any window decor or
     * applying any compatibility scale factors.
     * The size is adjusted based on the current rotation of the display.
     */
    @SuppressWarnings("deprecation")
    public static Size getScreenRawSize(Context ctx)
    {

        Size size = new Size();

        WindowManager winManager = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        Display display = winManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();


        if (Build.VERSION.SDK_INT < 14)
        {
            display.getMetrics(displayMetrics);
            size.width = displayMetrics.widthPixels;
            size.height = displayMetrics.heightPixels;
        }
        else if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17)
        {
            try
            {
                size.width = (Integer) Display.class.getMethod("getRawWidth").invoke(display);
                size.height = (Integer) Display.class.getMethod("getRawHeight").invoke(display);
            } catch (Exception e) {}

        }
        else if (Build.VERSION.SDK_INT >= 17)
        {
            Point _size = new Point();
            display.getRealSize(_size);
            size.width = _size.x;
            size.height = _size.y;
        }

        return size;
    }


    public static Size getScreenNativeSize(Context ctx)
    {
        Size  rawSize = getScreenRawSize(ctx);
        int angle = getScreenOrientationAngle(ctx);
        Size nativeSize = new Size(rawSize);

        if(angle == 90 || angle == 270)
        {
            nativeSize.width = rawSize.height;
            nativeSize.height = rawSize.width;
        }


        return nativeSize;
    }




    public static Point fromNativePos(Context ctx, Point rawPos)
    {
        Point pos = new Point(rawPos);
        int angle = getScreenOrientationAngle(ctx);
        Size size = getScreenNativeSize(ctx);


        if(angle == 90)
        {
            pos.x = rawPos.y;
            pos.y = size.width - rawPos.x;
        }
        else if(angle == 180)
        {
            pos.x = size.width - rawPos.x;
            pos.y =  size.height - rawPos.y;
        }
        else if(angle == 270)
        {
            pos.x = size.height - rawPos.y;
            pos.y = rawPos.x;
        }

        return pos;
    }


    public static Point toNativePos(Context ctx, Point position)
    {
        Point rawPos = new Point(position);
        int angle = getScreenOrientationAngle(ctx);
        Size size = getScreenRawSize(ctx);


        if(angle == 90)
        {
            rawPos.x = size.height - position.y;
            rawPos.y = position.x;
        }
        else if(angle == 180)
        {
            rawPos.x = size.width - position.x;
            rawPos.y =  size.height - position.y;
        }
        else if(angle == 270)
        {
            rawPos.x = position.y;
            rawPos.y = size.width - position.x;
        }

        return rawPos;

    }




    public static void fromNativePos(Context ctx, Point rawPos, Size size)
    {
        Point pos = new Point();
        pos.x = rawPos.x;
        pos.y = rawPos.y;
        int angle = getScreenOrientationAngle(ctx);
        Size screenSize = getScreenNativeSize(ctx);
        Size newSize = new Size(size);


        if(angle == 90)
        {
            pos.x = rawPos.y;
            pos.y = screenSize.width - (rawPos.x+size.width);

            newSize.width = size.height;
            newSize.height = size.width;
        }
        else if(angle == 180)
        {
            pos.x = screenSize.width - (rawPos.x+size.width);
            pos.y =  screenSize.height - (rawPos.y+size.height);
        }
        else if(angle == 270)
        {
            pos.x = screenSize.height - (rawPos.y+size.height);
            pos.y = rawPos.x;
            newSize.width = size.height;
            newSize.height = size.width;
        }


        rawPos.x = pos.x;
        rawPos.y = pos.y;
        size.width = newSize.width;
        size.height = newSize.height;

    }


    public static void toNativePos(Context ctx, Point position, Size size)
    {
        Point rawPos = new Point(position);
        Size newSize = new Size(size);

        int angle = getScreenOrientationAngle(ctx);
        Size screenSize = getScreenRawSize(ctx);


        if(angle == 90)
        {
            rawPos.x = screenSize.height - (rawPos.y+size.height);
            rawPos.y = position.x;

            newSize.width = size.height;
            newSize.height = size.width;
        }
        else if(angle == 180)
        {
            rawPos.x = screenSize.width - (rawPos.x+size.width);
            rawPos.y =  screenSize.height - (rawPos.y+size.height);
        }
        else if(angle == 270)
        {
            rawPos.x = position.y;
            rawPos.y = screenSize.width - (rawPos.x+size.width);
            newSize.width = size.height;
            newSize.height = size.width;
        }

       position.x = rawPos.x;
       position.y = rawPos.y;
       size.width = newSize.width;
       size.height = newSize.height;

    }




//------------------



    public static float dpToPx(Context ctx, float dp)
    {
        DisplayMetrics metrics = getDisplayMetrics(ctx);
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
        return px;
    }

    public static int dpToPx(Context ctx, int dp) {return (int)dpToPx(ctx, (float) dp);}


    public static float pxToDp(Context ctx, float px)
    {
        DisplayMetrics metrics = getDisplayMetrics(ctx);
        float dp =  (int) (px / metrics.density);

        return dp;
    }

    public static int pxToDp(Context ctx, int px) { return (int)pxToDp(ctx, (float) px);}


    public static float getDensity(Context ctx)
    {
        DisplayMetrics metrics = getDisplayMetrics(ctx);
        return metrics.density;

    }

    public static double getScreenInches(Context ctx)
    {
        WindowManager winManager = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        winManager.getDefaultDisplay().getMetrics(dm);
        int width=dm.widthPixels;
        int height=dm.heightPixels;
        int dens=dm.densityDpi;
        double wi=(double)width/(double)dens;
        double hi=(double)height/(double)dens;
        double x = Math.pow(wi,2);
        double y = Math.pow(hi,2);
        double screenInches = Math.sqrt(x+y);

        return screenInches;
    }



    public static boolean isScreenOn(Context ctx)
    {
        PowerManager pm = (PowerManager)ctx.getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();
        return isScreenOn;
    }

    public static boolean isScreenLocked(Context ctx)
    {
        KeyguardManager myKM = (KeyguardManager) ctx.getSystemService(Context.KEYGUARD_SERVICE);

        //LogHelper.log("inKeyguardRestrictedInputMode() : " + myKM.inKeyguardRestrictedInputMode());
        if( myKM.inKeyguardRestrictedInputMode()) {
            return true;
        } else {
            return false;
        }
    }


    public static DisplayMetrics getDisplayMetrics(Context ctx)
    {
        DisplayMetrics metrics = new DisplayMetrics();
        Resources res = ctx.getResources();
        if(res != null)
        {
            metrics = res.getDisplayMetrics();
        }


        return metrics;
    }




    public static String strOrientation(int orientation)
    {
        String text = "Unknown screen orientation";

        switch(orientation)
        {
            case ActivityInfo.SCREEN_ORIENTATION_PORTRAIT:
                text = "SCREEN_ORIENTATION_PORTRAIT";

                break;
            case ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE:
                text = "SCREEN_ORIENTATION_LANDSCAPE";

                break;
            case ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT:
                text = "SCREEN_ORIENTATION_REVERSE_PORTRAIT";

                break;
            case ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE:
                text = "SCREEN_ORIENTATION_REVERSE_LANDSCAPE";

                break;

        }


        return text;

    }


}
