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


package com.tafayor.selfcamerashot.ui;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;




public class MsgDialog extends DialogFragment
{

    String mTitle;
    String mMessage;

    public static MsgDialog getInstance(String title, String message)
    {
        MsgDialog dialog = new MsgDialog();
        dialog.setTitle(title);
        dialog.setMessage(message);
        return dialog;
    }



    public void setTitle(String title)
    {
        mTitle = title;
    }

    public void setMessage(String message)
    {
        mMessage = message;
    }


    @Override

    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        final Context ctx = getActivity();


        AlertDialog.Builder builder = new  AlertDialog.Builder(ctx)
                .setTitle(mTitle)
                .setMessage(mMessage)
                .setNegativeButton(android.R.string.ok,null);

        AppCompatDialog dialog = (AppCompatDialog) builder.create();

        return dialog;
    }
}
