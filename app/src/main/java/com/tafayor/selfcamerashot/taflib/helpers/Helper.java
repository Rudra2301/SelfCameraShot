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
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;

import com.tafayor.selfcamerashot.R;


/**
 * Created by youssef on 03/12/13.
 */
public class Helper {
    static String TAG = "Helper";





    public int clamp(int val , int min, int max)
    {
        if (val< min) val = min;
        else if(val > max) val = max;

        return val;
    }

    public float clamp(float val , float min, float max)
    {
        if (val< min) val = min;
        else if(val > max) val = max;

        return val;
    }

    public void beep(Context ctx) {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(ctx, notification);
            r.play();

        } catch (Exception e) {
            logError(e.getMessage());
            e.printStackTrace();
        }
    }

    public static void logError(String error) {
        System.out.println("Error: " + error);
    }

    public static void msg(String msg) {
        System.out.println(msg);
    }

    public static void msg(String tag, String msg) {
        msg(tag + " : " + msg);
    }

    public static void msg(String mainTag, String subTag , String msg)
    {
        msg(mainTag + " ->  " + subTag + " : " + msg);
    }

    static double roundOff(double x, int position) {
        double a = x;
        double temp = Math.pow(10.0, position);
        a *= temp;
        a = Math.round(a);
        return (a / (double) temp);
    }



    public void vibrate(Context ctx) {
        Vibrator vibrator = (Vibrator) ctx.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(300);

    }


    public void showSendEmailView(Context ctx, String email, String subject, String message)
    {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);

        try
        {
            AppHelper.startActivity(ctx, Intent.createChooser(intent,
                    ResHelper.getResString(ctx, R.string.chooser_email_title)));
        }
        catch (android.content.ActivityNotFoundException ex)
        {
            LogHelper.log(TAG, "showSendEmailView", "There is no email client installed.");
        }
    }




}


