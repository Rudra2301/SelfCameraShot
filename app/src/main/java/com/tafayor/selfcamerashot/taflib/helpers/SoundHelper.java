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
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;


public class SoundHelper
{

    public static String TAG = SoundHelper.class.getSimpleName();


 
    //==============================================================================================
    // Interface
    //==============================================================================================


    //----------------------------------------------------------------------------------------------
    // setMuteSystem
    //----------------------------------------------------------------------------------------------
    public static void setMuteSystem(Context ctx, boolean mute)
    {
        AudioManager am = (AudioManager)ctx.getSystemService(Context.AUDIO_SERVICE);

        if(mute) am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        else am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);

        am.setStreamMute(AudioManager.STREAM_SYSTEM, mute);
        am.setStreamMute(AudioManager.STREAM_NOTIFICATION, mute);
        am.setStreamMute(AudioManager.STREAM_ALARM, mute);
        am.setStreamMute(AudioManager.STREAM_RING, mute);
        am.setStreamMute(AudioManager.STREAM_MUSIC, mute);
    }


    //----------------------------------------------------------------------------------------------
    // beep
    //----------------------------------------------------------------------------------------------
    public static void beep(Context ctx)
    {

        try
        {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(ctx, notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    public static void mute(Context ctx)
    {
        AudioManager mgr = (AudioManager)ctx.getSystemService(Context.AUDIO_SERVICE);


    }


    public static void unmute(Context ctx)
    {
        AudioManager mgr = (AudioManager) ctx.getSystemService(Context.AUDIO_SERVICE);


    }


    public static void setMode(Context ctx, int mode)
    {
        AudioManager mgr = (AudioManager) ctx.getSystemService(Context.AUDIO_SERVICE);
        mgr.setRingerMode(mode);
    }


    public static int getMode(Context ctx)
    {
        AudioManager mgr = (AudioManager)ctx.getSystemService(Context.AUDIO_SERVICE);
        return mgr.getRingerMode();
    }

    public static void setSilentMode(Context ctx)
    {
        AudioManager mgr = (AudioManager)ctx.getSystemService(Context.AUDIO_SERVICE);
        mgr.setRingerMode(AudioManager.RINGER_MODE_SILENT);
    }




    //==============================================================================================
    // Internals
    //==============================================================================================



}
