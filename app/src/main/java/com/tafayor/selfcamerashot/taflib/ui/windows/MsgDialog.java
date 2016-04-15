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
import android.os.Parcel;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tafayor.selfcamerashot.R;
import com.tafayor.selfcamerashot.taflib.helpers.ResHelper;



public class MsgDialog extends TafDefaultDialog
{
    public static String TAG = MsgDialog.class.getSimpleName();


    public MsgDialog(Context ctx) {
        super(ctx);
    }

    public MsgDialog (Context ctx, String title, String message, int iconRes )
    {
         super(ctx);
         setTitle(title);
         setMessage(message);
         setSmallIcon(iconRes);
    }


    public MsgDialog (Context ctx, String title, String message )
    {
        super(ctx);
        setTitle(title);
        setMessage(message);
    }





    @Override
    protected void onInitialize(final Dialog dialog)
    {

        setPositiveButton(ResHelper.getResString(getContext(), R.string.verb_close),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
    }












}

