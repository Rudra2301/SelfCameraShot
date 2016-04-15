/*
 * Copyright (C) 2011 Jacquet Wong
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
 */

package com.tafayor.selfcamerashot.remoteControl.sound;


import android.os.Build;

import com.musicg.wave.WaveHeader;

/**
 * Api for detecting whistle
 * 
 * @author Jacquet Wong
 * 
 */
public class WhistleApi extends DetectionApi {


    ByFreqParams mLowFreqParams;
    ByFreqParams mHighFreqParams;


	public WhistleApi(WaveHeader waveHeader) {
		super(waveHeader);
	}

	protected void init()
    {
		// settings for detecting a whistle
        minFrequency = 1000.f;
        maxFrequency = Double.MAX_VALUE;

        minIntensity = 100.0f;
        maxIntensity = 100000.0f;

        minStandardDeviation = 0.1f;//0.1
        maxStandardDeviation = 1.0f;

        highPass = 100;//100
        lowPass = 10000;//10000

        minNumZeroCross = 50; //50
        maxNumZeroCross = 200;//200

        numRobust = 10;//10




        mHighFreqParams = new ByFreqParams();
        mHighFreqParams.minFrequency = 5000;
        mHighFreqParams.maxFrequency = 50000; //Kitkat ...
        mHighFreqParams.numRobust = 40;
        mHighFreqParams.minStandardDeviation = 0.08f;
        mHighFreqParams.maxStandardDeviation = 1.0f;
        mHighFreqParams.minNumZeroCross = 50;
        mHighFreqParams.maxNumZeroCross = 200;//100


        mLowFreqParams = new ByFreqParams();
        mLowFreqParams.minFrequency = minFrequency;
        mLowFreqParams.maxFrequency = 5000; //gingerbread lollipop ...
        mLowFreqParams.numRobust = 7;
        mLowFreqParams.minStandardDeviation = 0.1f;
        mLowFreqParams.maxStandardDeviation = 1.0f;
        mLowFreqParams.minNumZeroCross = 50;
        mLowFreqParams.maxNumZeroCross = 200;//150

        addByFreqParams(mHighFreqParams);
        addByFreqParams(mLowFreqParams);
    }
		
	public boolean isWhistle(byte[] audioBytes){
		return isSpecificSound2(audioBytes);
	}
}