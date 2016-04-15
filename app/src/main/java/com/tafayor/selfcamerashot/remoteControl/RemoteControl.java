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

package com.tafayor.selfcamerashot.remoteControl;

import android.content.Context;


import com.tafayor.selfcamerashot.remoteControl.sound.SoundControlListener;
import com.tafayor.selfcamerashot.remoteControl.sound.SoundManager;
import com.tafayor.selfcamerashot.taflib.types.WeakArrayList;


public class RemoteControl implements SoundControlListener
{

	 public static String TAG = RemoteControl.class.getSimpleName();




    // general
    private Context mContext;
    private WeakArrayList<SoundControlListener> mSoundListeners;



    //flags
    private boolean mIsDetecting;

    //Utils
    private SoundManager mSoundManager;




	public RemoteControl(Context ctx)
    {
        mContext = ctx;
        mIsDetecting = false;
        mSoundListeners = new WeakArrayList<>();
        mSoundManager = new SoundManager();
        mSoundManager.setListener(this);


	}


    //==============================================================================================
    // Interface
    //==============================================================================================

    //----------------------------------------------------------------------------------------------
    // setSensitivity (0 - 1)
    //----------------------------------------------------------------------------------------------
    public  void setSensitivity(float sensitivity)
    {

        mSoundManager.setSensitivity(sensitivity);

    }





    //----------------------------------------------------------------------------------------------
    // release
    //----------------------------------------------------------------------------------------------
    public synchronized void release()
    {

        mSoundListeners.clear();
        if(mSoundManager.isDetecting())
        {
            mSoundManager.stopDetection();
        }

    }








    //----------------------------------------------------------------------------------------------
    // startWhistleDetection
    //----------------------------------------------------------------------------------------------
    public synchronized void startWhistleDetection()
    {
        if(!mSoundManager.isDetecting()) mSoundManager.startDetection();
        mSoundManager.enableWhistleDetection(true);
    }


    //----------------------------------------------------------------------------------------------
    // stopWhistleDetection
    //----------------------------------------------------------------------------------------------
    public synchronized void stopWhistleDetection()
    {
        mSoundManager.enableWhistleDetection(false);
        if(!mSoundManager.isDetectingClapping()) mSoundManager.stopDetection();
    }





    //----------------------------------------------------------------------------------------------
    // startClappingDetection
    //----------------------------------------------------------------------------------------------
    public synchronized void startClappingDetection()
    {
        if(!mSoundManager.isDetecting()) mSoundManager.startDetection();
        mSoundManager.enableClappingDetection(true);
    }


    //----------------------------------------------------------------------------------------------
    // stopClappingDetection
    //----------------------------------------------------------------------------------------------
    public synchronized void stopClappingDetection()
    {
        mSoundManager.enableClappingDetection(false);
        if(!mSoundManager.isDetectingClapping()) mSoundManager.stopDetection();
    }






    //==============================================================================================
    // Callbacks
    //==============================================================================================



    //==============================================================================================
    // Listeners
    //==============================================================================================


    //----------------------------------------------------------------------------------------------
    // addSoundListener
    //----------------------------------------------------------------------------------------------
    public void addSoundListener(SoundControlListener listener)
    {
        mSoundListeners.addUnique(listener);
    }

    //----------------------------------------------------------------------------------------------
    // removeSoundListener
    //----------------------------------------------------------------------------------------------
    public void removeSoundListener(SoundControlListener listener)
    {
        mSoundListeners.remove(listener);
    }



    //----------------------------------------------------------------------------------------------
    // notifyWhistleListeners
    //----------------------------------------------------------------------------------------------
    public void notifyWhistleListeners()
    {


        for(SoundControlListener listener : mSoundListeners)
        {
            listener.onWhistleDetected();
        }
    }



    //----------------------------------------------------------------------------------------------
    // notifyWhistleListeners
    //----------------------------------------------------------------------------------------------
    public void notifyClappingListeners()
    {

        for(SoundControlListener listener : mSoundListeners)
        {
            listener.onClappingDetected();
        }
    }



    //----------------------------------------------------------------------------------------------
    // notifySoundExceptionListeners
    //----------------------------------------------------------------------------------------------
    public void notifySoundExceptionListeners()
    {
        for(SoundControlListener listener : mSoundListeners)
        {
            listener.onException();
        }
    }











    //==============================================================================================
    // Internals
    //==============================================================================================






    //==============================================================================================
    // Implementation
    //==============================================================================================


    //==============================
    // OnSignalsDetectedListener
    //==============================
    @Override
    public void onWhistleDetected()
    {

        notifyWhistleListeners();
    }


    @Override
    public void onClappingDetected()
    {
        notifyClappingListeners();
    }



    //==============================
    // RecorderThreadListener
    //==============================
    @Override
    public void onException()
    {
        notifySoundExceptionListeners();
    }




    //==============================================================================================
    // Types
    //==============================================================================================
}
