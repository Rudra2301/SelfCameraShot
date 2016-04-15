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

import java.util.LinkedList;



import com.musicg.wave.WaveHeader;
import com.tafayor.selfcamerashot.taflib.helpers.LangHelper;
import com.tafayor.selfcamerashot.taflib.helpers.LogHelper;


import android.media.AudioFormat;
import android.media.AudioRecord;

public class DetectorThread extends Thread{


    public static String TAG = DetectorThread.class.getSimpleName();
    private static boolean LOG = false;

	private RecorderThread recorder;
	private WaveHeader waveHeader;
	private WhistleApi whistleApi;
    private ClapApi clapApi;
	private volatile boolean mIsRunning;

	private LinkedList<Boolean> whistleResultList = new LinkedList<Boolean>();
	private int whistleCheckLength = 5;
	private int whistlePassScore = 3;



    private boolean isPreviousclap  ;


    //flags

    private boolean mEnableWhistleDetection;
    private boolean mEnableClappingDetection;
	
	private SoundControlListener onSignalsDetectedListener;


    //==============================================================================================
    // Init
    //==============================================================================================
	public DetectorThread(RecorderThread recorder){
		this.recorder = recorder;

		
		int bitsPerSample = 0;
		if (recorder.getAudioFormat() == AudioFormat.ENCODING_PCM_16BIT){
			bitsPerSample = 16;
		}
		else if (recorder.getAudioFormat() == AudioFormat.ENCODING_PCM_8BIT){
			bitsPerSample = 8;
		}
		
		int channel = 0;

        //CHANNEL_IN_MONO
		// whistle detection only supports mono channel
		if (recorder.getChannelConfiguration() == AudioFormat.CHANNEL_IN_MONO){
			channel = 1;
		}

		waveHeader = new WaveHeader();
		waveHeader.setChannels(channel);
		waveHeader.setBitsPerSample(bitsPerSample);
		waveHeader.setSampleRate(recorder.getSampleRate());

		whistleApi = new WhistleApi(waveHeader);
        clapApi = new ClapApi(waveHeader);



	}



    private void loadDefaults()
    {
        mEnableWhistleDetection = false;
        mEnableClappingDetection = false;
        mIsRunning = false;
        isPreviousclap = false;
    }


    //==============================================================================================
    // Interface
    //==============================================================================================


    //----------------------------------------------------------------------------------------------
    // setSensitivity (0 - 1)
    //----------------------------------------------------------------------------------------------
    public  void setSensitivity(float sensitivity)
    {
        int maxChecklength = 13;
        int checkLength =   Math.round(sensitivity * maxChecklength);
        int passScore =   Math.round((maxChecklength - checkLength) * 0.5f);


        checkLength = LangHelper.clamp(checkLength,5, maxChecklength);
        passScore = LangHelper.clamp(passScore, 1, checkLength);

        LogHelper.log(TAG, "setSensitivity " + sensitivity);

        whistleCheckLength = checkLength;
        whistlePassScore = passScore;
        initWhistleBuffer();


    }


    //----------------------------------------------------------------------------------------------
    // isWhistleDetectionEnabled
    //----------------------------------------------------------------------------------------------
    public  boolean isWhistleDetectionEnabled()
    {
        return mEnableWhistleDetection;
    }



    //----------------------------------------------------------------------------------------------
    // enableWhistleDetection
    //----------------------------------------------------------------------------------------------
    public void enableWhistleDetection(boolean option)
    {
        initWhistleBuffer();
        mEnableWhistleDetection = option;
    }




    //----------------------------------------------------------------------------------------------
    // isClappingDetectionEnabled
    //----------------------------------------------------------------------------------------------
    public  boolean isClappingDetectionEnabled()
    {
        return mEnableClappingDetection;
    }



    //----------------------------------------------------------------------------------------------
    // enableWhistleDetection
    //----------------------------------------------------------------------------------------------
    public void enableClappingDetection(boolean option)
    {
        isPreviousclap = false;
        mEnableClappingDetection = option;
    }


    @Override
    //----------------------------------------------------------------------------------------------
    // start
    //----------------------------------------------------------------------------------------------
	public void start()
    {
        mIsRunning = true;
        super.start();
    }



    //-----------------------------------------------------------------------------------------
    // stopDetection
    //----------------------------------------------------------------------------------------------
	public void stopDetection()
    {
        mIsRunning = false;

	}


    //----------------------------------------------------------------------------------------------
    // setOnSignalsDetectedListener
    //----------------------------------------------------------------------------------------------
    public void setOnSignalsDetectedListener(SoundControlListener listener)
    {
        onSignalsDetectedListener = listener;
    }





    //==============================================================================================
    // Callbacks
    //==============================================================================================

    //----------------------------------------------------------------------------------------------
    // run
    //----------------------------------------------------------------------------------------------
	public void run()
    {
		try
        {
			byte[] buffer;

			
			Thread thisThread = Thread.currentThread();

			while (mIsRunning)
            {
				if(recorder.isRecording())
                {
                    buffer = recorder.getFrameBytes();
                    if(!mIsRunning) break;
                    if(mEnableWhistleDetection) checkWhistle(buffer);
                    if (mEnableClappingDetection) checkClapping(buffer);


                }



                //LangHelper.sleep(10);
			}
		}
        catch (Exception e)
        {
			LogHelper.logx(e);
		}



	}



    //==============================================================================================
    // Internals
    //==============================================================================================


    //----------------------------------------------------------------------------------------------
    // startWhistleDetection
    //----------------------------------------------------------------------------------------------
    private void initWhistleBuffer()
    {
        whistleResultList.clear();

        // init the first frames
        for (int i = 0; i < whistleCheckLength; i++) {
            whistleResultList.add(false);
        }
        // end init the first frames
    }







    //----------------------------------------------------------------------------------------------
    // checkWhistle
    //----------------------------------------------------------------------------------------------
    private void checkWhistle(byte[] buffer)
    {



        // audio analyst
        if (buffer != null)
        {
            int numWhistles = 0;
            boolean isWhistle = whistleApi.isWhistle(buffer);
           // if(LOG) LogHelper.log("isWhistle : " + isWhistle);



            whistleResultList.removeFirst();
            whistleResultList.add(isWhistle);

            for(boolean whistle : whistleResultList)
            {
                if(whistle) numWhistles++;
            }


            //if(LOG) System.out.println("num:" + numWhistles);


            if (numWhistles >= whistlePassScore)
            {
                // clear buffer
                initWhistleBuffer();
                onWhistleDetected();
            }
            // end whistle detection
        }
        else
        {

            whistleResultList.removeFirst();
            whistleResultList.add(false);
        }
    }



    //----------------------------------------------------------------------------------------------
    // checkClapping
    //----------------------------------------------------------------------------------------------
    private void checkClapping(byte[] buffer)
    {


        // audio analyst
        if (buffer != null)
        {
            int numClaps = 0;
            boolean isClap = clapApi.isClap(buffer);
           // if(LOG) LogHelper.log("isClap : " + isClap);



            if(isClap)
            {
                if(!isPreviousclap) onClappingDetected();
                isPreviousclap = true;
            }
            else isPreviousclap = false;


        }

    }




    //----------------------------------------------------------------------------------------------
    // onWhistleDetected
    //----------------------------------------------------------------------------------------------
	private void onWhistleDetected()
    {

		if (onSignalsDetectedListener != null){
			onSignalsDetectedListener.onWhistleDetected();
		}
	}



    //----------------------------------------------------------------------------------------------
    // onClappingDetected
    //----------------------------------------------------------------------------------------------
    private void onClappingDetected()
    {

        if (onSignalsDetectedListener != null){
            onSignalsDetectedListener.onClappingDetected();
        }
    }

}