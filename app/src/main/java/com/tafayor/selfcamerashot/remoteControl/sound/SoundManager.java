/*
 * Copyright (C) 2012 Jacquet Wong
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * musicg api in Google Code: http://code.google.com/p/musicg/
 * Android Application in Google Play: https://play.google.com/store/apps/details?id=com.whistleapp
 * 
 */

package com.tafayor.selfcamerashot.remoteControl.sound;

import android.content.Context;

import com.tafayor.selfcamerashot.taflib.helpers.LogHelper;


public class SoundManager   implements SoundControlListener, RecorderThread.RecorderThreadListener
{

	 public static String TAG = SoundManager.class.getSimpleName();

	
	public static final int DETECT_NONE = 0;
	public static final int DETECT_WHISTLE = 1;
    public static final int DETECT_CLAP_HANDS = 2;
	public static int selectedDetection = DETECT_NONE;

    // general
    private Context mContext;
    private SoundControlListener mListener;


	// detection parameters
	private DetectorThread detectorThread;
	private RecorderThread recorderThread;


    //flags
    private boolean mIsDetecting;

    private float mSensitivity;



	public  SoundManager()
    {

        mIsDetecting = false;
        mListener = null;
        mSensitivity = 0.9f;
	}


    //==============================================================================================
    // Interface
    //==============================================================================================


    //----------------------------------------------------------------------------------------------
    // setSensitivity (0 - 1)
    //----------------------------------------------------------------------------------------------
    public  void setSensitivity(float sensitivity)
    {
        mSensitivity = sensitivity;
        if(isDetecting())
        {
            detectorThread.setSensitivity(sensitivity);
            recorderThread.setSensitivity(sensitivity);
        }

    }




    //----------------------------------------------------------------------------------------------
    // isWhistleDetectionEnabled
    //----------------------------------------------------------------------------------------------
    public synchronized boolean isWhistleDetectionEnabled()
    {
        return detectorThread.isWhistleDetectionEnabled();
    }




    //----------------------------------------------------------------------------------------------
    // setListener
    //----------------------------------------------------------------------------------------------
    public synchronized void setListener(SoundControlListener listener)
    {
        mListener = listener;
    }

    //----------------------------------------------------------------------------------------------
    // startDetection
    //----------------------------------------------------------------------------------------------
    public synchronized void startDetection()
    {
        if(mIsDetecting)
        {
            LogHelper.log(TAG, "startDetection", "Detection already started");
            return;
        }
        recorderThread = new RecorderThread(this);
        recorderThread.start();
        recorderThread.setSensitivity(mSensitivity);

        detectorThread = new DetectorThread(recorderThread);
        detectorThread.setOnSignalsDetectedListener(this);
        detectorThread.start();
        detectorThread.setSensitivity(mSensitivity);

        mIsDetecting = true;
    }


    //----------------------------------------------------------------------------------------------
    // release
    //----------------------------------------------------------------------------------------------
    public synchronized  void stopDetection()
    {

        enableWhistleDetection(false);
        enableClappingDetection(false);

        if (recorderThread != null)
        {
            recorderThread.stopRecording();
            recorderThread = null;
        }
        if (detectorThread != null)
        {
            detectorThread.stopDetection();
            detectorThread = null;
        }

        mIsDetecting = false;
    }


    //----------------------------------------------------------------------------------------------
    // isDetecting
    //----------------------------------------------------------------------------------------------
    public synchronized boolean  isDetecting()
    {
        return mIsDetecting;
    }



    //----------------------------------------------------------------------------------------------
    // enableWhistleDetection
    //----------------------------------------------------------------------------------------------
    public synchronized  void enableWhistleDetection(boolean option)
    {
        if(!mIsDetecting)
        {
            LogHelper.log(TAG, "startDetection", "Detection is not started");
            return;
        }
        detectorThread.enableWhistleDetection(option);

    }

    //----------------------------------------------------------------------------------------------
    // v
    //----------------------------------------------------------------------------------------------
    public synchronized  void enableClappingDetection(boolean option)
    {
        if(!mIsDetecting)
        {
            LogHelper.log(TAG, "startDetection", "Detection is not started");
            return;
        }
        detectorThread.enableClappingDetection(option);
    }




    //----------------------------------------------------------------------------------------------
    // isDetectingWhistle
    //----------------------------------------------------------------------------------------------
    public synchronized  boolean isDetectingWhistle()
    {
        return (mIsDetecting && detectorThread.isWhistleDetectionEnabled());
    }


    //----------------------------------------------------------------------------------------------
    // isDetectingClapping
    //----------------------------------------------------------------------------------------------
    public synchronized  boolean isDetectingClapping()
    {
        return (mIsDetecting && detectorThread.isClappingDetectionEnabled());
    }



    //==============================================================================================
    // Callbacks
    //==============================================================================================





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

        if(mListener!=null) mListener.onWhistleDetected();
    }

    @Override
    public void onClappingDetected() {
        if(mListener!=null) mListener.onClappingDetected();
    }


    //==============================
    // RecorderThreadListener
    //==============================
    @Override
    public void onException()
    {
        stopDetection();
        if(mListener!=null) mListener.onException();
    }


    //==============================================================================================
    // Types
    //==============================================================================================
}
