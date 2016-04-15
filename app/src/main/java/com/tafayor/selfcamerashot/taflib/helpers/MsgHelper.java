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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Looper;
import android.widget.Toast;


public class MsgHelper
{
    static String TAG = MsgHelper.class.getSimpleName();




    public static void alert(Context ctx, String message)
    {
        AlertDialog.Builder bld = new AlertDialog.Builder(ctx);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        bld.create().show();


    }

    public static void alert(Context ctx, String message, final Runnable callback)
    {
        AlertDialog.Builder bld = new AlertDialog.Builder(ctx);
        bld.setMessage(message);
        bld.setNeutralButton("OK", new DialogInterface.OnClickListener()
        {
            @Override
            public  void onClick(DialogInterface dialogInterface, int i)
            {
                android.os.Handler handler = new android.os.Handler();
                handler.post(callback);
            }
        });
        bld.create().show();

    }



    public static void alertOnUi(Context ctx, String message, final Runnable callback)
    {
        AlertDialog.Builder bld = new AlertDialog.Builder(ctx);
        bld.setMessage(message);
        bld.setNeutralButton("OK", new DialogInterface.OnClickListener()
        {
            @Override
            public  void onClick(DialogInterface dialogInterface, int i)
            {
                android.os.Handler handler = new android.os.Handler(Looper.getMainLooper());
                handler.post(callback);
            }
        });
        bld.create().show();

    }


    public static void toastLong(final Context ctx, final String msg)
    {

        AppHelper.postOnUi(ctx, new Runnable()
        {
            @Override
            public void run() {
                Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
            }
        });

    }


    public static void toast(Context ctx, int resId)
    {
        toast(ctx, ctx.getResources().getString(resId));
    }

    public static void toast(final Context ctx, final String msg)
    {
        AppHelper.postOnUi(ctx, new Runnable()
        {
            @Override
            public  void run() {
                Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
            }
        });

    }



    public static void toastSlow(final Context ctx, final String msg)
    {
        AppHelper.postOnUi(ctx, new Runnable()
        {
            @Override
            public  void run() {
                Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
            }
        });

    }

    public static void toastSlow(final Context ctx, int resId)
    {
        final String msg = ResHelper.getResString(ctx, resId);
        AppHelper.postOnUi(ctx, new Runnable()
        {
            @Override
            public  void run() {
                Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
            }
        });

    }




    public static void toastFast(final Context ctx, final String msg)
    {
        AppHelper.postOnUi(ctx, new Runnable()
        {
            @Override
            public  void run()
            {
                Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
            }
        });

    }


    public static void toastFast(final Context ctx, int resId)
    {
        final String msg = ResHelper.getResString(ctx, resId);
        AppHelper.postOnUi(ctx, new Runnable()
        {
            @Override
            public  void run()
            {
                Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
            }
        });

    }


}


