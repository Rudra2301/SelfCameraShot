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

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import com.tafayor.selfcamerashot.taflib.helpers.LogHelper;


public class RecorderThread extends Thread {

    public static String TAG = RecorderThread.class.getCanonicalName();


    private static int MIN_AVERAGE = 3;


	private AudioRecord audioRecord;
	private volatile  boolean isRecording;
	private int channelConfiguration = AudioFormat.CHANNEL_IN_MONO;
	private int audioEncoding = AudioFormat.ENCODING_PCM_16BIT ; //ENCODING_PCM_16BIT;
	private int sampleRate = 44100; // 44100
	private int frameByteSize = 2048; // for 1024 fft size (16bit sample size)
	byte[] buffer;
    volatile int minAverageValue;
    RecorderThreadListener mListener ;
	
	public RecorderThread(RecorderThreadListener listener){

        mListener = listener;
		buffer = new byte[frameByteSize];
        minAverageValue = MIN_AVERAGE;
	}
	
	public AudioRecord getAudioRecord(){
		return audioRecord;
	}

    public int getChannelConfiguration()
    {
        return channelConfiguration;
    }

    public int getSampleRate()
    {
        return sampleRate;
    }

    public int getAudioFormat()
    {
        return audioEncoding;
    }

	public synchronized boolean isRecording(){
		//return this.isAlive() && isRecording;

        return  isRecording;
	}
	
	public synchronized void startRecording()
    {


            int recBufSize = AudioRecord.getMinBufferSize(sampleRate, channelConfiguration, audioEncoding); // need to be larger than size of a frame
            //audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate, channelConfiguration, audioEncoding, recBufSize);
			audioRecord = findAudioRecord();

            if(audioRecord != null)
            {
                audioRecord.startRecording();
                isRecording = true;
            }
            else
            {
                mListener.onException();
            }


	}
	
	public synchronized  void stopRecording(){


		try
        {
            if(audioRecord != null)
            {

                audioRecord.stop();
                audioRecord.release();
            }

            audioRecord = null;
            isRecording = false;

		} catch (Exception e) {
            LogHelper.logx(e);
		}
	}
	
	public byte[] getFrameBytes(){

       // if(!isRecording) return null;

		int status = audioRecord.read(buffer, 0, frameByteSize);


		// analyze sound
		int totalAbsValue = 0;
        short sample = 0; 
        float averageAbsValue = 0.0f;
        
        for (int i = 0; i < frameByteSize; i += 2) {
            sample = (short)((buffer[i]) | buffer[i + 1] << 8);
            totalAbsValue += Math.abs(sample);
        }
        averageAbsValue = totalAbsValue / frameByteSize / 2;

       // LogHelper.log("averageAbsValue : " + averageAbsValue);


        // no input
        if (averageAbsValue < minAverageValue) //averageAbsValue < 30
        {
        	return null;
        }

       // System.out.println("averageAbsValue : " + averageAbsValue);
        
		return buffer;
	}
	
	public void run()
    {
		startRecording();
	}


    //----------------------------------------------------------------------------------------------
    // setSensitivity (0 - 1)
    //----------------------------------------------------------------------------------------------
    public  void setSensitivity(float sensitivity)
    {

        int value = (int)(100 * sensitivity) ;
        int minAverage = (int) ((100-value));

        minAverageValue = MIN_AVERAGE + minAverage;

    }






    private static int[] mSampleRates = new int[] { 44100,48000, 22050 , 11025, 16000, 8000  };
    public AudioRecord findAudioRecord()
    {
        for (int rate : mSampleRates)
        {
            for (short audioFormat : new short[] { AudioFormat.ENCODING_PCM_16BIT})
            {//,
                for (short channelConfig : new short[] { AudioFormat.CHANNEL_IN_MONO })
                {
                    for (short mic : new short[] {MediaRecorder.AudioSource.DEFAULT, MediaRecorder.AudioSource.MIC,
                            MediaRecorder.AudioSource.CAMCORDER})
                    {
                        try
                        {
                            LogHelper.log(TAG, "Attempting rate " + rate + "Hz, bits: " + audioFormat + ", channel: "
                                    + channelConfig);
                            int bufferSize = AudioRecord.getMinBufferSize(rate, channelConfig, audioFormat);

                            if (bufferSize != AudioRecord.ERROR_BAD_VALUE) {
                                // check if we can instantiate and have a success
                                AudioRecord recorder = new AudioRecord(mic, rate, channelConfig, audioFormat, bufferSize);

                                if (recorder.getState() == AudioRecord.STATE_INITIALIZED)
                                {
                                    audioEncoding = audioFormat;
                                    sampleRate = rate;
                                    LogHelper.log("Attemp succeeded");
                                    return recorder;
                                }

                            }
                        } catch (Exception e)
                        {
                            LogHelper.log("Attemp failed");
                            //
                        }
                    }

                }
            }
        }

        try
        {
            new RuntimeException("Failed to find an audio record");
        }
        catch (Exception e)
        {
            LogHelper.logx(e);
        }

        return null;
    }




    public interface RecorderThreadListener
    {
        public void onException();
    }
}