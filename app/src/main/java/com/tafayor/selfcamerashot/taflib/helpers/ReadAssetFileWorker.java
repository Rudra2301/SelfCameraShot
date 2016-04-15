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
import android.os.AsyncTask;


import com.tafayor.selfcamerashot.taflib.interfaces.IAsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;



public class ReadAssetFileWorker  extends AsyncTask<String, String, String>
{
    public static String TAG = ReadAssetFileWorker.class.getSimpleName();

    WeakReference<IAsyncTask> mListenerPtr;
    Context mContext;
    String mContent;


    public ReadAssetFileWorker(Context context, IAsyncTask listener)
    {
        mListenerPtr = new WeakReference<IAsyncTask>(listener);
        mContext = context;
    }

    public String getContent()
    {
        return mContent;
    }

    public boolean isContentAvailable(){return (mContent != null);}

    @Override
    protected String doInBackground(String... params)
    {
        String url = params[0];
        InputStream in;
        BufferedReader reader;
        StringBuilder content = new StringBuilder();
        String line ;

        try
        {
            in = mContext.getAssets().open(url);
            reader = new BufferedReader(new InputStreamReader(in));
            while((line = reader.readLine()) != null)
            {
                content.append(line);
                content.append("\n");
            }
        }
        catch (IOException e)
        {
            LogHelper.log(TAG, "doInBackground", e.getMessage());
        }

        return content.toString();
    }


    @Override
    protected void onProgressUpdate(String... line)
    {

    }


    @Override
    protected void onPostExecute(String content)
    {
        mContent = content;
        IAsyncTask listener;
        listener = mListenerPtr.get();
        if(listener != null) listener.onTaskCompleted();
    }

}
