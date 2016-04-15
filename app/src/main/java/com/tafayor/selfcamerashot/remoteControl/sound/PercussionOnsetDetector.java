/**
 * * Implementation based on "https://github.com/JorenSix/TarsosDSP/blob/master/src/core/be/tarsos/dsp/onsets/PercussionOnsetDetector.java"
 * and  "http://vamp-plugins.org/code-doc/PercussionOnsetDetector_8cpp-source.html"
 * <pre>
 *  Centre for Digital Music, Queen Mary, University of London.
 *  Copyright 2006 Chris Cannam.
 *
 *  Permission is hereby granted, free of charge, to any person
 *  obtaining a copy of this software and associated documentation
 *  files (the "Software"), to deal in the Software without
 *  restriction, including without limitation the rights to use, copy,
 *  modify, merge, publish, distribute, sublicense, and/or sell copies
 *  of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *  MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR
 *  ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 *  CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 *  WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 *  Except as contained in this notice, the names of the Centre for
 *  Digital Music; Queen Mary, University of London; and Chris Cannam
 *  shall not be used in advertising or otherwise to promote the sale,
 *  use or other dealings in this Software without prior written
 *  authorization.
 * </pre>
 *
 * </p>
 *
 * @author Ouadban Youssef
 * @author Joren Six
 * @author Chris Cannam
 *
 */


package com.tafayor.selfcamerashot.remoteControl.sound;


import com.tafayor.selfcamerashot.taflib.helpers.LangHelper;
import org.jtransforms.fft.DoubleFFT_1D;



public class PercussionOnsetDetector
{
    int bufferSize = 2048;
    private  float[] priorMagnitudes = new float[bufferSize];
    private  float[] currentMagnitudes = new float[bufferSize];
    private float dfMinus1, dfMinus2;
    private  double sensitivity = 15;
    private double threshold = 10;

    float minDfMinus;


    public PercussionOnsetDetector()
    {
        minDfMinus = (float) (((100 - sensitivity) * bufferSize) / 200);//180
    }


    boolean process(byte[] data)
    {
        boolean detected = false;

        float[] samples = getFloatBuffer(data);

        currentMagnitudes = getMagnitudes(samples);
        int binsOverThreshold = 0;
        for (int i = 0; i < currentMagnitudes.length; i++)
        {
            if (priorMagnitudes[i] > 0.f)
            {
                double diff = 10 * Math.log10(currentMagnitudes[i] / priorMagnitudes[i]);
                if (diff >= threshold)
                {
                    binsOverThreshold++;
                }
            }
            priorMagnitudes[i] = currentMagnitudes[i];
        }

           /* LogHelper.log("binsOverThreshold : " + binsOverThreshold);
            LogHelper.log("dfMinus1 : " + dfMinus1);
            LogHelper.log("dfMinus2 : " + dfMinus2);
            LogHelper.log("minDfMinus : " +minDfMinus);*/

        if (dfMinus2 < dfMinus1
                && dfMinus1 >= binsOverThreshold
                && dfMinus1 > minDfMinus  )
        {
            detected = true;
        }

        dfMinus2 = dfMinus1;
        dfMinus1 = binsOverThreshold;

        return detected;
    }





    float[] getFloatBuffer(byte[] bytes)
    {
        float[] floats = new float[bytes.length/2];
        for(int i =  0 ; i<bytes.length ; i+=2)
        {
            short sample = (short)(bytes[i] | (bytes[i+1]<<8));
            floats[i/2] =  sample/32768.0f;
            floats[i/2] = LangHelper.clamp(floats[i / 2], -1f, 1f);
        }

        return floats;
    }


    float[] getMagnitudes(float[] samples)
    {
        int samplesSize = samples.length;
        DoubleFFT_1D fft1d = new DoubleFFT_1D(samplesSize);
        double[] fftBuffer = new double[samplesSize*2];
        float[] magnitudes = new float[samplesSize];

        for (int i = 0; i < samplesSize; i++)
        {
            // copying audio data to the fft data buffer, imaginary part is 0
            fftBuffer[2 * i] = samples[i];
            fftBuffer[2 * i + 1] = 0;
        }


        fft1d.complexForward(fftBuffer);

        for(int i = 0; i < samplesSize; ++i)
        {
            double real =  fftBuffer[2*i];
            double imaginary = fftBuffer[2*i+1];
            magnitudes[i] = (float) Math.sqrt( real*real + imaginary*imaginary );


        }

        return magnitudes;

    }


}