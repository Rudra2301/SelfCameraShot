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


package com.tafayor.selfcamerashot.devApps;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.tafayor.selfcamerashot.R;
import com.tafayor.selfcamerashot.taflib.helpers.IntentHelper;
import com.tafayor.selfcamerashot.taflib.helpers.LogHelper;
import com.tafayor.selfcamerashot.taflib.helpers.MarketHelper;




public class DevAppDialog extends DialogFragment
{

    static String  ARG_APPINFO  = "argAppInfo";

    AppInfo mAppInfo;

    public static DevAppDialog getInstance(AppInfo info)
    {
        DevAppDialog dialog = new DevAppDialog();
        Bundle args = new Bundle();
        args.putParcelable(ARG_APPINFO, info);
        dialog.setArguments(args);
        return dialog;
    }




    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        final Context ctx = getActivity();

        Bundle args = getArguments();
        if(args != null  && args.containsKey(ARG_APPINFO))
        {
            mAppInfo = args.getParcelable(ARG_APPINFO);
        }


        if(mAppInfo == null)
        {
            LogHelper.logx(new Exception("DevAppDialog mAppInfo null"));
            Dialog dlg =  new  AlertDialog.Builder(ctx).create();
            return dlg;
        }

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View rootView = inflater.inflate(R.layout.dialog_dev_app, null, false);
        TextView tvDescription = (TextView) rootView.findViewById(R.id.description);
        ImageView imageView = (ImageView) rootView.findViewById(R.id.image);
        ImageButton playView = (ImageButton) rootView.findViewById(R.id.play);

        tvDescription.setText(mAppInfo.getDescription());

        imageView.setImageResource(mAppInfo.getImage());
        imageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                MarketHelper.showProductPage(ctx, mAppInfo.getPackageName());
            }
        });

        if(mAppInfo.getYoutubeId() == null) playView.setVisibility(View.GONE);
        playView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                IntentHelper.openYoutubeVideo(ctx, mAppInfo.getYoutubeId());
            }
        });




        AlertDialog.Builder builder = new  AlertDialog.Builder(ctx)
                .setTitle(mAppInfo.getTitle())
                .setIcon(mAppInfo.getIcon())
                .setView(rootView)
                .setPositiveButton(R.string.devApps_dialog_install, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        MarketHelper.showProductPage(ctx, mAppInfo.getPackageName());
                    }
                });
               // .setNegativeButton(android.R.string.cancel,null);

        AppCompatDialog dialog = (AppCompatDialog) builder.create();

        return dialog;
    }
}
