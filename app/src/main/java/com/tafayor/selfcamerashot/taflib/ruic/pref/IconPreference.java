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



package com.tafayor.selfcamerashot.taflib.ruic.pref;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.tafayor.selfcamerashot.R;
import com.tafayor.selfcamerashot.taflib.helpers.ViewHelper;
import com.tafayor.selfcamerashot.taflib.types.Size;


public class IconPreference extends Preference
{

    private Drawable mStartIcon;
    private Drawable mStartIconBackground;
    ImageView mStartIconView;
    int mStartIconWidth=-1, mStartIconHeight=-1;
    View.OnClickListener mStartIconClickListener;
    
    private Drawable mEndIcon;
    private Drawable mEndIconBackground;
    ImageView mEndIconView;
    int mEndIconWidth=-1, mEndIconHeight=-1;
    View.OnClickListener mEndIconClickListener;



    public IconPreference(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public IconPreference(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        //setLayoutResource(R.layout.ruic_preference_icon);
        setLayoutResource(R.layout.ruic_preference_icon);
        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.IconPreference, defStyle, 0);
        mStartIcon = a.getDrawable(R.styleable.IconPreference_startIcon);
        mEndIcon = a.getDrawable(R.styleable.IconPreference_endIcon);

    }



    @Override
    public void onBindView(View view)
    {
        super.onBindView(view);



        mStartIconView = (ImageView) view.findViewById(R.id.icon);
        mEndIconView = (ImageView) view.findViewById(R.id.endIcon);


        if(mStartIconWidth !=-1 && mStartIconHeight != -1)
        {
            ViewHelper.resizeView(mStartIconView, mStartIconWidth, mStartIconHeight);
        }

        if(mEndIconWidth !=-1 && mEndIconHeight != -1)
        {
            ViewHelper.resizeView(mEndIconView,mEndIconWidth,mEndIconHeight);
        }

        if (mStartIconView != null )
        {
            if(mStartIcon != null) mStartIconView.setImageDrawable(mStartIcon);
            if(mStartIconBackground != null) ViewHelper.setBackground(mStartIconView,mStartIconBackground);
        }

        if (mEndIconView != null )
        {
            if(mEndIcon != null) mEndIconView.setImageDrawable(mEndIcon);
            if(mEndIconBackground != null) ViewHelper.setBackground(mEndIconView,mEndIconBackground);
        }


        mStartIconView.setOnClickListener(mStartIconClickListener);
        if(mStartIconClickListener != null)
        {
             mStartIconView.setClickable(true);
        }
        else
        {
            mStartIconView.setClickable(false);
        }


        mEndIconView.setOnClickListener(mEndIconClickListener);
        if(mEndIconClickListener != null)
        {
            mEndIconView.setClickable(true);
        }
        else
        {
            mEndIconView.setClickable(false);
        }

    }

    public void setStartIcon(int resId)
    {
        Drawable  d = getContext().getResources().getDrawable(resId);
        setStartIcon(d);
    }

    public void setEndIcon(int resId)
    {
        Drawable  d = getContext().getResources().getDrawable(resId);
        setEndIcon(d);
    }


    public void setStartIcon(Bitmap icon)
    {
        setStartIcon(new BitmapDrawable(icon));
    }

    public void setEndIcon(Bitmap icon)
    {
        setEndIcon(new BitmapDrawable(icon));
    }

    public void setStartIcon(Drawable icon)
    {
        mStartIcon = icon;
        notifyChanged();
    }


    public void setEndIcon(Drawable icon)
    {
        mEndIcon = icon;
        notifyChanged();

    }


    public void setStartIconBackground(Drawable bg)
    {
        mStartIconBackground = bg;
        notifyChanged();
    }

    public void setEndIconBackground(Drawable bg)
    {
        mEndIconBackground = bg;
        notifyChanged();
    }


    public Drawable getCustomIcon()
    {
        return mStartIcon;
    }

    public Drawable getEndIcon()
    {
        return mEndIcon;
    }


    public Size getCustomIconSize()
    {
        return new Size(mStartIconView.getWidth(), mStartIconView.getHeight());
    }

    public Size getEndIconSize()
    {
        return new Size(mEndIconView.getWidth(), mEndIconView.getHeight());
    }


    public void resizeIconView(int w, int h)
    {
        mStartIconWidth = w;
        mStartIconHeight = h;
        notifyChanged();
    }

    public void resizeEndIconView(int w, int h)
    {
        mEndIconWidth = w;
        mEndIconHeight = h;
        notifyChanged();
    }




    public void setStartIconOnClickListener(View.OnClickListener listener)
    {
        mStartIconClickListener = listener;
        notifyChanged();

    }

    public void setEndIconOnClickListener(View.OnClickListener listener)
    {
        mEndIconClickListener = listener;
        notifyChanged();

    }




    public void releaseEndIcon()
    {
        mEndIconBackground = null;
        mEndIcon = null;
        mEndIconClickListener = null;
        notifyChanged();
    }











    static class SavedState extends BaseSavedState {
        int stateToSave;

        SavedState(Parcelable superState) {
            super(superState);

        }

        private SavedState(Parcel in) {
            super(in);
            this.stateToSave = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.stateToSave);
        }

        //required field that makes Parcelables from a Parcel
        public static final Creator<SavedState> CREATOR =
                new Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }
                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }
}