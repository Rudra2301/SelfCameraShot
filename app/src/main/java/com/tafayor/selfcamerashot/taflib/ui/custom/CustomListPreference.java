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


package com.tafayor.selfcamerashot.taflib.ui.custom;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.ListPreference;
import android.util.AttributeSet;
import android.widget.Button;

import java.util.ArrayList;


public class CustomListPreference extends ListPreference
{

    public static String TAG = CustomListPreference.class.getSimpleName();


    private int mSelectedIndex;
    private ArrayList<String> mDisabledEntries;
    String mValue;



    public CustomListPreference(Context context)
    {
        super(context);
        init();
    }


    public CustomListPreference(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();

    }


    private void init()
    {
        mDisabledEntries = new ArrayList<String>();
    }


    public void addDisabledEntry(String val)
    {
        mDisabledEntries.add(val);
    }




    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder)
    {
        CharSequence[] values = this.getEntries();

        mSelectedIndex = this.findIndexOfValue(this.getValue());

        builder.setSingleChoiceItems(values, mSelectedIndex, mClickListener)
                .setPositiveButton(android.R.string.ok, mClickListener)
                .setNegativeButton(android.R.string.cancel, mClickListener);
    };


    protected void onChoiceClick(String clickedValue)
    {

    }


    DialogInterface.OnClickListener mClickListener = new DialogInterface.OnClickListener()
    {
        public void onClick(DialogInterface dialog, int which)
        {
            if (which >= 0)
            {
                String clickedValue = (String) CustomListPreference.this
                        .getEntryValues()[which];
                mValue = clickedValue;

                onChoiceClick(clickedValue);

                Boolean isEnabled;


                isEnabled = !(mDisabledEntries.contains(clickedValue));



                AlertDialog alertDialog = (AlertDialog) dialog;

                Button positiveButton = alertDialog
                        .getButton(AlertDialog.BUTTON_POSITIVE);

                positiveButton.setEnabled(isEnabled);

                mSelectedIndex = which;
            }
            else
            {
                if (which == DialogInterface.BUTTON_POSITIVE)
                {

                     if(mSelectedIndex !=  -1) CustomListPreference.this.setValueIndex(mSelectedIndex);


                    CustomListPreference.this.onClick(dialog, DialogInterface.BUTTON_POSITIVE);

                }

                dialog.dismiss();
            }
        }
    };

    @Override
    protected void onDialogClosed(boolean positiveResult) {}
}