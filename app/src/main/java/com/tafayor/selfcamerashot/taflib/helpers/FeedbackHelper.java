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
import android.content.Intent;

import com.tafayor.selfcamerashot.R;



public class FeedbackHelper
{

    public static String TAG = FeedbackHelper.class.getSimpleName();









    //----------------------------------------------------------------------------------------------
    // showSendEmailView
    //----------------------------------------------------------------------------------------------
    public static void showSendEmailView(Context ctx, String email, String subject)
    {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);

        try
        {
            intent = Intent.createChooser(intent, ResHelper.getResString(ctx, R.string.chooser_email_title));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ctx.startActivity(intent);

        }
        catch (android.content.ActivityNotFoundException ex)
        {
            LogHelper.log(TAG, "showSendEmailView", "There is no email client installed.");
        }
    }








}
