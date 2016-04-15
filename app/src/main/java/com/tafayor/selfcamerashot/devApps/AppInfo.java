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

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.style.ParagraphStyle;


public class AppInfo implements Parcelable
{

    private String mTitle;
    private String mDescription;
    private String mPackageName;
    private int mIconRes;
    private int mImageRes;
    private String mYoutubeId;





    public AppInfo(String packageName, int iconRes, String title, String description)
    {
        mPackageName = packageName;
        mIconRes = iconRes;
        mTitle = title;
        mDescription = description;
        mImageRes = 0;
        mYoutubeId = null;
    }


    public void setPackageName(String value) { mPackageName = value; }
    public String getPackageName() { return mPackageName; }

    public void setIcon(int iconRes) { mIconRes = iconRes; }
    public int getIcon() { return mIconRes; }

    public void setTitle(String value) { mTitle = value; }
    public String getTitle() { return mTitle; }

    public void setDescription(String value) { mDescription = value; }
    public String getDescription() { return mDescription; }

    public void setImage(int imageRes) { mImageRes = imageRes; }
    public int getImage() { return mImageRes; }

    public void setYoutubeId(String id) { mYoutubeId = id; }
    public String getYoutubeId() { return mYoutubeId; }











    public AppInfo(Parcel in)
    {
        mTitle = in.readString();
        mDescription = in.readString();
        mPackageName = in.readString();
        mIconRes = in.readInt();
        mImageRes = in.readInt();
        mYoutubeId = in.readString();

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(mTitle);
        dest.writeString(mDescription);
        dest.writeString(mPackageName);
        dest.writeInt(mIconRes);
        dest.writeInt(mImageRes);
        dest.writeString(mDescription);
    }



    public static  Creator<AppInfo> CREATOR = new Creator<AppInfo>() {

        @Override
        public AppInfo createFromParcel(Parcel source)
        {
            return new AppInfo(source);
        }

        @Override
        public AppInfo[] newArray(int size)
        {
            return new AppInfo[size];
        }

    };

}
