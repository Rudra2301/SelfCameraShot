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



package com.tafayor.selfcamerashot.taflib.ui.windows;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;


import com.tafayor.selfcamerashot.R;
import com.tafayor.selfcamerashot.taflib.helpers.ResHelper;
import com.tafayor.selfcamerashot.taflib.helpers.ViewHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;




public class WebDialog extends TafDefaultDialog
{
    public static String TAG = WebDialog.class.getSimpleName();

    public static int SRC_TYPE_ASSET = 0;

    private String mPath;
    private int mSrcType;
    private WebView mWebViewer;






    public WebDialog(Context ctx, String title, String src, int srcType)
    {
        super(ctx);
        setTitle(title);
        mPath= src;
        mSrcType = srcType;

    }




        @Override
        public View getView(final Dialog dialog)
        {
            Context context = dialog.getContext();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.web_dialog, null);
            mWebViewer = (WebView) view.findViewById(R.id.wvWebViewer);

            selectDialog(DefaultDialogs.ALERT_INFO);

            setPositiveButton(ResHelper.getResString(getContext(), R.string.verb_close), new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    dialog.dismiss();
                }
            });


            return view;
        }



        @Override
        public void onDialogReady(Dialog dialog)
        {

            LoadUrlAsync loader = new LoadUrlAsync(dialog);
            loader.execute(mPath);
        }




        @Override
        public void onFreeMemory()
        {
            super.onFreeMemory();
            ViewHelper.freeWebView(mWebViewer);
            mWebViewer = null;
        }




    private  static class LoadUrlAsync extends AsyncTask<String, Void, String>
    {
        WeakReference<Dialog> mDialogPtr;
        WeakReference<ProgressBar> mSpinnerPtr;
        WeakReference<WebView> mWebViewerPtr;

        LoadUrlAsync(Dialog dialog)
        {
            mDialogPtr = new WeakReference<Dialog>(dialog);
            mSpinnerPtr = new WeakReference<ProgressBar>((ProgressBar) dialog.findViewById(R.id.pbWaitSpinner));
            mWebViewerPtr = new WeakReference<WebView>((WebView) dialog.findViewById(R.id.wvWebViewer));
        }


        @Override
        protected void onPreExecute()
        {

        }

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

                Dialog dialog = mDialogPtr.get();
                if(dialog == null) return "";

                in = dialog.getContext().getAssets().open(url);
                reader = new BufferedReader(new InputStreamReader(in));
                while((line = reader.readLine()) != null)
                {

                    content.append(line);
                    content.append("\n");
                }


            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            return content.toString();
        }


        @Override
        protected void onPostExecute(String webContent)
        {

            Dialog dialog = mDialogPtr.get();
            if(dialog == null) return ;
            WebView webViewer = mWebViewerPtr.get();
            webViewer.setWebViewClient(new WebViewClient()
            {
                @Override
                public void onPageFinished(WebView view, String url)
                {
                    ProgressBar spinner = mSpinnerPtr.get();
                    spinner.setVisibility(View.INVISIBLE);
                }
            });

            webViewer.loadDataWithBaseURL(null, webContent, "text/html", "UTF-8", null);


        }
    }



}

