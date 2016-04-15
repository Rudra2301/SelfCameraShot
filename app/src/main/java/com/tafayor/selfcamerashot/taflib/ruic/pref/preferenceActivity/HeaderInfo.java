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


package com.tafayor.selfcamerashot.taflib.ruic.pref.preferenceActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import com.tafayor.selfcamerashot.taflib.helpers.GraphicsHelper;



public class HeaderInfo implements Parcelable
{
    public String key;
    public String title;
    public Class clazz;
    public Drawable icon;
    public Context context;
    public HeaderInfo(Context ctx, String key, Class clazz, String title, Drawable icon)
    {
        this.key = key;
        this.title = title;
        this.clazz = clazz;
        this.icon = icon;
        context = ctx;
    }


    public HeaderInfo(Parcel in)
    {
        key = in.readString();
        title = in.readString();
        clazz = (Class) in.readSerializable();

        Bitmap bitmap = in.readParcelable(null);;
        icon = (bitmap==null)? null : new BitmapDrawable(context.getResources(), bitmap);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(key);
        dest.writeString(title);
        dest.writeSerializable(clazz);

        Bitmap bitmap = (icon==null) ? null : GraphicsHelper.drawableToBitmap(icon);
        dest.writeParcelable(bitmap, flags);

    }



    public static  Creator<HeaderInfo> CREATOR = new Creator<HeaderInfo>() {

        @Override
        public HeaderInfo createFromParcel(Parcel source)
        {
            return new HeaderInfo(source);
        }

        @Override
        public HeaderInfo[] newArray(int size)
        {
            return new HeaderInfo[size];
        }

    };

}