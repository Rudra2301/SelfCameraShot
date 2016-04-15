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


package com.tafayor.selfcamerashot.taflib.ui.components;


import android.app.AlertDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.tafayor.selfcamerashot.R;
import com.tafayor.selfcamerashot.taflib.helpers.GraphicsHelper;


public class SeekBarPreference extends DialogPreference implements SeekBar.OnSeekBarChangeListener
{
    public static String TAG = SeekBarPreference.class.getSimpleName();

    static final int DEFAULT_MIN_VALUE = 1;
    static final int DEFAULT_MAX_VALUE = 100;
    static final int DEFAULT_DEFAULT_VALUE = 50;


    private int mCurrentValue;
    private int mMinValue;
    private int mMaxValue;
    private int mDefaultValue;
    private SeekBar mSeekBar;
    private TextView tvCurrentValue;
    ColorStateList mTextColor ;//= new ColorStateList(new int[])


    public SeekBarPreference(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);

    }

    public SeekBarPreference(Context context, AttributeSet attrs) {

        super(context, attrs);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);

        setDialogIcon(null);

        int customTheme = 0;

        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.SeekBarPreference);

        loadDefaults();

        for(int i = 0; i< array.getIndexCount(); i++)
        {
            int attr = array.getIndex(i);
            if(attr == R.styleable.SeekBarPreference_minValue)
            {

                mMinValue = array.getInt(R.styleable.SeekBarPreference_minValue, DEFAULT_MIN_VALUE);
            }
            else if(attr == R.styleable.SeekBarPreference_maxValue)
            {

                mMaxValue = array.getInt(R.styleable.SeekBarPreference_maxValue, DEFAULT_MAX_VALUE);
            }
            else if(attr == R.styleable.SeekBarPreference_defaultValue)
            {

                mDefaultValue = array.getInt(R.styleable.SeekBarPreference_defaultValue, DEFAULT_DEFAULT_VALUE);
            }
            else if(attr == R.styleable.SeekBarPreference_seekBarPref_textColor)
            {

                mTextColor = array.getColorStateList(R.styleable.SeekBarPreference_seekBarPref_textColor);

            }

        }
        array.recycle();




    }

    void loadDefaults()
    {
        mTextColor = GraphicsHelper.getColorStateList(Color.GRAY);
    }


    public int getMaxValue()
    {
        return mMaxValue;
    }

    public int getProgress()
    {
        return getPersistedInt(mDefaultValue);
    }

    private void readAttributes(TypedArray attrs)
    {

        for(int i = 0 ; i< attrs.getIndexCount(); i++) {
            int attr = attrs.getIndex(i);
            if (attr == R.styleable.SeekBarPreference_seekBarPref_textColor)
            {

                mTextColor = attrs.getColorStateList(R.styleable.SeekBarPreference_seekBarPref_textColor);
            }
        }
    }




        @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {

        super.onPrepareDialogBuilder(builder);
        // builder.setNegativeButton(null,null);
        // builder.setTitle(null);

    }

    @Override
    protected View onCreateDialogView() {


        mCurrentValue = getPersistedInt(mDefaultValue);
      
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.pref_seek_slider, null);

        tvCurrentValue = (TextView) view.findViewById(R.id.tvCurrentValue);
        TextView tvMaxValue = (TextView) view.findViewById(R.id.tvMaxValue);
        TextView tvMinValue = (TextView) view.findViewById(R.id.tvMinValue);
        mSeekBar = (SeekBar) view.findViewById(R.id.seekBar);


        tvCurrentValue.setTextColor(mTextColor);
        tvMaxValue.setTextColor(mTextColor);
        tvMinValue.setTextColor(mTextColor);

        tvMaxValue.setText(String.valueOf(mMaxValue));
        tvMinValue.setText(String.valueOf(mMinValue));

        mSeekBar.setMax(mMaxValue - mMinValue);
        mSeekBar.setProgress(mCurrentValue - mMinValue);
        mSeekBar.setOnSeekBarChangeListener(this);

        tvCurrentValue.setText("" + mCurrentValue);

        return view;
    }


    @Override
    protected void onDialogClosed(boolean positiveResult) {
        // When the user selects "OK", persist the new value
        if (!positiveResult) {
            return;
        }

        if (shouldPersist()) {
            persistInt(mCurrentValue);
        }

        notifyChanged();

       /* if (positiveResult) {
            Editor editor = getEditor();
            editor.putString(myKey1, myView.getValue1());
            editor.putString(myKey2, myView.getValue2());
            editor.commit();
        }*/
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int value, boolean b) {
        mCurrentValue = mMinValue + value;
        tvCurrentValue.setText("" + mCurrentValue);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}