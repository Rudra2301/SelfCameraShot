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
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Parcel;
import android.util.StateSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.tafayor.selfcamerashot.App;
import com.tafayor.selfcamerashot.R;
import com.tafayor.selfcamerashot.taflib.helpers.ResHelper;
import com.tafayor.selfcamerashot.taflib.helpers.ThemeHelper;
import com.tafayor.selfcamerashot.taflib.helpers.ViewHelper;
import com.tafayor.selfcamerashot.taflib.types.WeakArrayList;

import java.lang.ref.WeakReference;
import java.util.zip.Inflater;



public class TafDefaultDialog extends TafBaseDialog
{
    public static String TAG = TafDefaultDialog.class.getSimpleName();

    private DefaultDialogBuilder mFactory ;
    private WeakArrayList<TafDefaultDialogListener> mListeners;



    public TafDefaultDialog(Context ctx)
    {
        super(ctx);
        init();
    }






    private void init()
    {
        mFactory = new DefaultDialogBuilder(this);
        mListeners = new WeakArrayList<TafDefaultDialogListener>();
        super.setTheme(R.style.TafDefaultDialog_Light);
        mFactory.mDefaultTheme = R.style.TafDefaultDialog_Light;
        setFactory(mFactory);
    }


    public void setTheme (int theme)
    {
        mFactory.mTheme = theme;
    }

    public void selectButtons (int type)
    {
        mFactory.mButtonsType = type;
    }

    public void selectDialog(int type)
    {
        mFactory.mDialogType = type;
    }

    public void setTitle(String title)
    {
        mFactory.mTitle = title;
    }


    public void setMessage(String message)
    {
        mFactory.mMessage = message;
    }



    public void selectIcon(int type)
    {
        if(type == DefaultIcons.ALERT_ERROR)
        {
            setSmallIcon( R.drawable.ic_alert_error_small);
        }
        else if(type == DefaultIcons.ALERT_INFO)
        {
            setSmallIcon( R.drawable.ic_alert_info_small);
        }
        else if(type == DefaultIcons.ALERT_SUCCESS)
        {
            setSmallIcon( R.drawable.ic_alert_success_small);
        }
        else if(type == DefaultIcons.ALERT_WARNING)
        {
            setSmallIcon( R.drawable.ic_alert_warning_small);
        }

    }

    public void setSmallIcon(Drawable icon)
    {
        mFactory.mSmallIcon = icon;
    }

    public void setSmallIcon(int iconResId)
    {
        mFactory.mSmallIcon = ResHelper.getResDrawable(getContext(), iconResId);
    }


    public void setMessageIcon(Drawable icon)
    {
        mFactory.mMessageIcon = icon;
    }


    public void setMessageIcon(int iconResId)
    {
        mFactory.mMessageIcon  = ResHelper.getResDrawable(getContext(), iconResId);
    }


    public void setPositiveButton(String text, final View.OnClickListener listener)
    {
        mFactory.mPositiveButtonText = text;
        mFactory.mPositiveButtonListener = listener;
    }


    public void setNegativeButton(String text, final View.OnClickListener listener)
    {
        mFactory.mNegativeButtonText = text;
        mFactory.mNegativeButtonListener = listener;
    }


    public void setNeutralButton(String text, final View.OnClickListener listener)
    {
        mFactory.mNeutralButtonText = text;
        mFactory.mNeutralButtonListener = listener;
    }




    //==============================================================================================
    // Callbacks
    //==============================================================================================


    protected void onInitialize(Dialog dialog)
    {

    }


    protected View getView(Dialog dialog)
    {
        return null;
    }


    public void onDialogReady(Dialog dialog)
    {

    }

    public void onFreeMemory()
    {

    }



    //==============================================================================================
    // Internals
    //==============================================================================================

    protected LayoutInflater getInflater(Context context)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        return inflater;
    }




    //=======================================================
    // Listeners
    //=======================================================

    //----------------------------------------------------------------------------------------------
    // addListener
    //----------------------------------------------------------------------------------------------
    public void addListener(TafDefaultDialogListener listener)
    {
        mListeners.addUnique(listener);
    }

    //----------------------------------------------------------------------------------------------
    // removeListener
    //----------------------------------------------------------------------------------------------
    public void removeListener(TafDefaultDialogListener  listener)
    {
        mListeners.remove(listener);
    }



    //----------------------------------------------------------------------------------------------
    // notifyOnCloseButtonClick
    //----------------------------------------------------------------------------------------------
    public void notifyOnCloseButtonClick(Dialog dialog)
    {
        for(TafDefaultDialogListener listener : mListeners)
        {
            listener.onCloseButtonClick(dialog);
        }
    }


    //----------------------------------------------------------------------------------------------
    // notifyOnYesButtonClick
    //----------------------------------------------------------------------------------------------
    public void notifyOnYesButtonClick(Dialog dialog)
    {
        for(TafDefaultDialogListener listener : mListeners)
        {
            listener.onYesButtonClick(dialog);
        }
    }


    //----------------------------------------------------------------------------------------------
    // notifyOnNoButtonClick
    //----------------------------------------------------------------------------------------------
    public void notifyOnNoButtonClick(Dialog dialog)
    {
        for(TafDefaultDialogListener listener : mListeners)
        {
            listener.onNoButtonClick(dialog);
        }
    }




    //==============================================================================================
    // Implementation
    //==============================================================================================

    //----------------------------------------------------------------------------------------------
    // DefaultDialogFactory
    //----------------------------------------------------------------------------------------------
    private static class DefaultDialogBuilder extends BaseDialogBuilder
    {
         public static String TAG = DefaultDialogBuilder.class.getSimpleName();

         int mDefaultTheme = 0;
         int mTheme = 0;
         String mTitle = null;
         String mMessage = null;
         String mPositiveButtonText = null;
         String mNegativeButtonText = null;
         String mNeutralButtonText = null;
         View.OnClickListener mPositiveButtonListener = null;
         View.OnClickListener mNegativeButtonListener = null;
         View.OnClickListener mNeutralButtonListener = null;
         Drawable mSmallIcon = null;
         Drawable mMessageIcon = null;
        int mDialogType = DefaultDialogs.CUSTOM;
        int mButtonsType = DefaultButtons.CUSTOM;





        // Views
        LinearLayout mContentPanel;
        TextView mTitleView;
        View mTitleDividerView;
        TextView mMessageView;
        ImageView mIconView;
        ImageView mMessageIconView;
        View mButtonPanelDividerPanel;

        //Attributes
        int mTitleTextColor ;
        int mTitleDividerColor ;
        int mMessageTextColor ;
        int mButtonPanelDividerColor ;
        Drawable mDialogBg;
        int mButtonTextColor ;
        int mButtonDividerColor ;
        ColorDrawable mButtonColorDefault;
        ColorDrawable mButtonColorPressed;
        ColorDrawable mButtonColorFocused;
        float mMessageTextSize;


        Context mContext;




        private WeakReference<TafDefaultDialog> mOuterPtr;



        public DefaultDialogBuilder(TafDefaultDialog outer)
        {
            mOuterPtr = new WeakReference<TafDefaultDialog>(outer);
        }


        public DefaultDialogBuilder(Parcel in)
        {
            //this.mTitle = in.readString();
            //this.mMessage = in.readString();
            //this.mIconRes = in.readInt();

        }


        //==========================================================================================
        // Interface
        //==========================================================================================



         @Override
        public Dialog  createDialog( Context context )
        {

            Dialog dialog ;

            dialog = new Dialog(context, mDefaultTheme);


            setupDialog(dialog);

            return dialog;
        }


        @Override
        public void setupDialog(Dialog dialog)
        {
            mContext = dialog.getContext();

            if(mOuterPtr == null) return; // temporary fix for a bug (mOuterPtr shouldn't be null normally)
            TafDefaultDialog outer = mOuterPtr.get();
            if(outer != null)
            {
                outer.onInitialize(dialog);
            }

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            //dialog.setTitle(mTitle);
            dialog.setContentView(R.layout.taf_default_dialog);
            resizeWindow(dialog);
            initView(dialog);
        }





        void initView(final Dialog dialog)
        {
            TafDefaultDialog outer = mOuterPtr.get();
            if(outer == null) return;

            mContentPanel = (LinearLayout)dialog.findViewById(R.id.llContentPanel);
            mTitleView = (TextView) dialog.findViewById(R.id.tvTitle);
            mMessageView = (TextView) dialog.findViewById(R.id.tvMessage);
            mIconView = (ImageView) dialog.findViewById(R.id.ivIcon);
            mMessageIconView = (ImageView) dialog.findViewById(R.id.ivMessageIcon);
            mTitleDividerView = dialog.findViewById(R.id.vTitleDivider);
            mButtonPanelDividerPanel = dialog.findViewById(R.id.vButtonPanelDivider);


            View customView = outer.getView(dialog);



            if(mTitle != null)
            {
                mTitleView.setText(mTitle);
            }



            if(customView != null)
            {
                LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
                );

                mContentPanel.removeAllViews();
                mContentPanel.addView(customView, lparams);
            }
            else
            {
                if(mMessage != null)
                {
                    mMessageView.setText(mMessage);
                }

                if(mMessageIconView != null)
                {
                    mMessageIconView.setVisibility(View.VISIBLE);
                    mMessageIconView.setImageDrawable(mMessageIcon);
                }
                else
                {
                    mMessageIconView.setVisibility(View.GONE);
                }
            }



            final TypedArray defaultAttrs = ThemeHelper.getTypedArray(mContext,
                    mDefaultTheme, R.styleable.tafDialog);



            readAttributes(defaultAttrs);



            TypedArray customAttrs;
            if(mTheme != 0)
            {

                customAttrs = ThemeHelper.getTypedArray(mContext, mTheme, R.styleable.tafDialog);
                readAttributes(customAttrs);
            }
            else
            {
                customAttrs = defaultAttrs;
            }






            if(mMessage != null)
            {
                mMessageView.setTextColor(mMessageTextColor);
                mMessageView.setTextSize(mMessageTextSize);
            }

            dialog.getWindow().setBackgroundDrawable(mDialogBg);
            mButtonPanelDividerPanel.setBackgroundColor(mButtonPanelDividerColor);

            if(mDialogType == DefaultDialogs.CUSTOM)
            {
                mTitleView.setTextColor(mTitleTextColor);
                mTitleDividerView.setBackgroundColor(mTitleDividerColor);
            }
            else
            {
                setupPresetDialog(defaultAttrs);

            }



            if(mSmallIcon != null)
            {
                mIconView.setVisibility(View.VISIBLE);
                mIconView.setImageDrawable(mSmallIcon);
            }
            else
            {
                mIconView.setVisibility(View.GONE);
            }


            if(mButtonsType == DefaultButtons.CUSTOM)
            {
                addButtons(dialog, customAttrs);
            }
            else
            {
                setupPresetButtons(dialog, defaultAttrs);
            }




            if(customAttrs != defaultAttrs) customAttrs.recycle();
            defaultAttrs.recycle();

        }



        //=======================================================
        // Internals
        //=======================================================


        private void readAttributes(TypedArray attrs)
        {


            for(int i = 0 ; i< attrs.getIndexCount(); i++)
            {
                int attr = attrs.getIndex(i);
                if(attr == R.styleable.tafDialog_tafDialog_titleTextColor)
                {
                    mTitleTextColor = attrs.getColor(R.styleable.tafDialog_tafDialog_titleTextColor, 0);
                }
                else if(attr == R.styleable.tafDialog_tafDialog_titleDividerColor)
                {
                    mTitleDividerColor = attrs.getColor(R.styleable.tafDialog_tafDialog_titleDividerColor, 0);
                }
                else if(attr == R.styleable.tafDialog_tafDialog_buttonPanelDividerColor)
                {
                    mButtonPanelDividerColor = attrs.getColor(R.styleable.tafDialog_tafDialog_buttonPanelDividerColor, 0);
                }
                else if(attr == R.styleable.tafDialog_tafDialog_background)
                {

                    mDialogBg =  attrs.getDrawable(R.styleable.tafDialog_tafDialog_background);
                }
                else if(attr == R.styleable.tafDialog_tafDialog_buttonTextColor)
                {
                    mButtonTextColor = attrs.getColor(R.styleable.tafDialog_tafDialog_buttonTextColor, 0);
                }
                else if(attr == R.styleable.tafDialog_tafDialog_buttonDividerColor)
                {
                    mButtonDividerColor = attrs.getColor(R.styleable.tafDialog_tafDialog_buttonDividerColor, 0);
                }

                else if(attr == R.styleable.tafDialog_tafDialog_buttonBackgroundColorNormal)
                {
                    mButtonColorDefault = new ColorDrawable(
                            attrs.getColor(R.styleable.tafDialog_tafDialog_buttonBackgroundColorNormal, 0));
                }

                else if(attr == R.styleable.tafDialog_tafDialog_buttonBackgroundColorPressed)
                {
                    mButtonColorPressed = new ColorDrawable(
                            attrs.getColor(R.styleable.tafDialog_tafDialog_buttonBackgroundColorPressed, 0));
                }

                else if(attr == R.styleable.tafDialog_tafDialog_buttonBackgroundColorFocused)
                {
                    mButtonColorFocused = new ColorDrawable(
                            attrs.getColor(R.styleable.tafDialog_tafDialog_buttonBackgroundColorFocused, 0));
                }
                else if(attr == R.styleable.tafDialog_tafDialog_messageTextColor)
                {

                    mMessageTextColor = attrs.getColor(R.styleable.tafDialog_tafDialog_messageTextColor, 0);

                }
                else if(attr == R.styleable.tafDialog_tafDialog_messageTextSize)
                {

                    mMessageTextSize = attrs.getDimension(R.styleable.tafDialog_tafDialog_messageTextSize, 16);

                }




            }







        }
        private void setupPresetDialog(TypedArray attrs)
        {


            if(mDialogType == DefaultDialogs.ALERT_SUCCESS)
            {
                mTitleTextColor = ResHelper.getResColor(mContext, R.color.taf_dialogSuccess_title_tc);
                mTitleDividerColor = ResHelper.getResColor(mContext, R.color.taf_dialogSuccess_titleDivider);
                mSmallIcon = ResHelper.getResDrawable(mContext, R.drawable.ic_alert_success_small);
            }
            else if(mDialogType == DefaultDialogs.ALERT_ERROR)
            {
                mTitleTextColor = ResHelper.getResColor(mContext, R.color.taf_dialogError_title_tc);
                mTitleDividerColor = ResHelper.getResColor(mContext, R.color.taf_dialogError_titleDivider);
                mSmallIcon = ResHelper.getResDrawable(mContext, R.drawable.ic_alert_error_small);
            }
            else if(mDialogType == DefaultDialogs.ALERT_INFO)
            {
                mTitleTextColor = ResHelper.getResColor(mContext, R.color.taf_dialogInfo_title_tc);
                mTitleDividerColor = ResHelper.getResColor(mContext, R.color.taf_dialogInfo_titleDivider);
                mSmallIcon = ResHelper.getResDrawable(mContext, R.drawable.ic_alert_info_small);
            }
            else if(mDialogType == DefaultDialogs.ALERT_WARNING)
            {
                mTitleTextColor = ResHelper.getResColor(mContext, R.color.taf_dialogWarning_title_tc);
                mTitleDividerColor = ResHelper.getResColor(mContext, R.color.taf_dialogWarning_titleDivider);
                mSmallIcon = ResHelper.getResDrawable(mContext, R.drawable.ic_alert_warning_small);
            }

            mTitleView.setTextColor(mTitleTextColor);
            mTitleDividerView.setBackgroundColor(mTitleDividerColor);

        }


        private void setupPresetButtons(final Dialog dialog, TypedArray attrs)
        {

            final TafDefaultDialog outer = mOuterPtr.get();
            if(outer == null) return;

            Drawable buttonBg = getButtonBackground(attrs);
            Drawable buttonDivider = getButtonDivider(mButtonDividerColor);

            LinearLayout buttonPanelLayout = (LinearLayout) dialog.findViewById(R.id.llButtonPanel);
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(Build.VERSION.SDK_INT>=14)
            {
                buttonPanelLayout.setDividerDrawable(buttonDivider);
            }

            if(mButtonsType == DefaultButtons.BUTTONS_CLOSE)
            {
                Button okBtn =(Button) inflater.inflate(R.layout.taf_default_dialog_button_view, buttonPanelLayout, false);
                okBtn.setId(R.id.tafDefaultDialog_positiveButton);
                okBtn.setText(ResHelper.getResString(mContext, R.string.tafDefaultDialog_close));
                okBtn.setTextColor(mButtonTextColor);
                ViewHelper.setBackgroundOnUi(mContext, okBtn, getButtonBackground(attrs));
                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        outer.notifyOnCloseButtonClick(dialog);
                        dialog.dismiss();
                    }
                });
                buttonPanelLayout.addView(okBtn);
            }
            else if(mButtonsType == DefaultButtons.BUTTONS_YES_NO)
            {

                Button yesBtn =(Button) inflater.inflate(R.layout.taf_default_dialog_button_view, buttonPanelLayout, false);
                yesBtn.setId(R.id.tafDefaultDialog_positiveButton);
                yesBtn.setText(ResHelper.getResString(mContext, R.string.tafDefaultDialog_yes));
                yesBtn.setTextColor(mButtonTextColor);
                ViewHelper.setBackgroundOnUi(mContext, yesBtn, getButtonBackground(attrs));
                yesBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        outer.notifyOnYesButtonClick(dialog);
                    }
                });
                buttonPanelLayout.addView(yesBtn);



                Button noBtn =(Button) inflater.inflate(R.layout.taf_default_dialog_button_view, buttonPanelLayout, false);
                noBtn.setId(R.id.tafDefaultDialog_negativeButton);
                noBtn.setText(ResHelper.getResString(mContext, R.string.tafDefaultDialog_no));
                noBtn.setTextColor(mButtonTextColor);
                ViewHelper.setBackgroundOnUi(mContext, noBtn, getButtonBackground(attrs));
                noBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        outer.notifyOnNoButtonClick(dialog);
                        dialog.dismiss();
                    }
                });
                buttonPanelLayout.addView(noBtn);
            }


        }


        private void addButtons(Dialog dialog, TypedArray attrs)
        {

            Drawable buttonBg = getButtonBackground(attrs);
            Drawable buttonDivider = getButtonDivider(mButtonDividerColor);

            LinearLayout buttonPanelLayout = (LinearLayout) dialog.findViewById(R.id.llButtonPanel);
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(Build.VERSION.SDK_INT>=14)
            {
                buttonPanelLayout.setDividerDrawable(buttonDivider);
            }



            if(mNegativeButtonText != null)
            {
                Button negativeBtn =(Button) inflater.inflate(R.layout.taf_default_dialog_button_view, buttonPanelLayout, false);
                negativeBtn.setId(R.id.tafDefaultDialog_negativeButton);
                negativeBtn.setText(mNegativeButtonText);
                negativeBtn.setTextColor(mButtonTextColor);
               ViewHelper.setBackgroundOnUi(mContext, negativeBtn, getButtonBackground(attrs));
                negativeBtn.setOnClickListener(mNegativeButtonListener);
                buttonPanelLayout.addView(negativeBtn);
            }


            if(mPositiveButtonText != null)
            {
                Button positiveBtn =(Button) inflater.inflate(R.layout.taf_default_dialog_button_view, buttonPanelLayout, false);
                positiveBtn.setId(R.id.tafDefaultDialog_positiveButton);
                positiveBtn.setText(mPositiveButtonText);
                positiveBtn.setTextColor(mButtonTextColor);
                ViewHelper.setBackgroundOnUi( mContext, positiveBtn, getButtonBackground(attrs));
                positiveBtn.setOnClickListener(mPositiveButtonListener);
                buttonPanelLayout.addView(positiveBtn);
            }
        }


        private StateListDrawable getButtonBackground(TypedArray attrs)
        {
            StateListDrawable background = new StateListDrawable();
            background.addState(new int[]{android.R.attr.state_pressed}, mButtonColorPressed);
            background.addState(new int[]{android.R.attr.state_focused}, mButtonColorFocused);
            background.addState(StateSet.WILD_CARD, mButtonColorDefault);
            return background;
        }



            private Drawable getButtonDivider(int color)
        {
            GradientDrawable drawable = (GradientDrawable) mContext.getResources().getDrawable(R.drawable.taf_default_dialog_button_divider);
            drawable.setColor(color);
            return drawable;
        }


        void resizeWindow(Dialog dialog)
        {
            int width, height;
            width = (int) (App.getScreenWidth(mContext) * 0.85f);
            height = (int) (App.getScreenHeight(mContext) * 0.85f);
            dialog.getWindow().setLayout(width, height);
        }



        //=======================================================
        // Calbacks
        //=======================================================

        @Override
        public void onDialogReady(Dialog dialog)
        {
            if(mOuterPtr == null) return;
            TafDefaultDialog outer = mOuterPtr.get();
            if(outer == null) return;

            outer.onDialogReady(dialog);
        }


        @Override
        public void onFreeMemory()
        {
            if(mOuterPtr == null) return;
            TafDefaultDialog outer = mOuterPtr.get();
            if(outer == null) return;

            outer.onFreeMemory();
        }

        @Override
        public int describeContents()
        {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i)
        {
            //parcel.writeString(mTitle);
           // parcel.writeString(mMessage);
            //parcel.writeInt(mIconRes);

        }




        //=======================================================
        // Types
        //=======================================================



        public static final Creator<DefaultDialogBuilder> CREATOR = new Creator<DefaultDialogBuilder>()
        {
            @Override
            public DefaultDialogBuilder createFromParcel(Parcel source)
            {
                return new DefaultDialogBuilder(source);
            }

            @Override
            public DefaultDialogBuilder[] newArray(int size)
            {
                return new DefaultDialogBuilder[size];
            }
        };


    };




    //==============================================================================================
    // Types
    //==============================================================================================


    //----------------------------------------------------------------------------------------------
    // DialogIcons
    //----------------------------------------------------------------------------------------------
    public interface DefaultIcons
    {
        public static int ALERT_INFO = 1;
        public static int ALERT_WARNING = 2;
        public static int ALERT_SUCCESS = 3;
        public static int ALERT_ERROR = 4;

    }


    //----------------------------------------------------------------------------------------------
    // DialogTypes
    //----------------------------------------------------------------------------------------------
    public interface DefaultDialogs
    {
        public static int CUSTOM = 0;
        public static int ALERT_INFO = 1;
        public static int ALERT_WARNING = 2;
        public static int ALERT_SUCCESS = 3;
        public static int ALERT_ERROR = 4;

    }




    //----------------------------------------------------------------------------------------------
    // DialogTypes
    //----------------------------------------------------------------------------------------------
    public interface DefaultButtons
    {
        public static int CUSTOM = 0;
        public static int BUTTONS_CLOSE = 1;
        public static int BUTTONS_YES_NO = 2;
        public static int BUTTONS_YES_NO_CANCEL = 3;

    }




    //----------------------------------------------------------------------------------------------
    // DialogTypes
    //----------------------------------------------------------------------------------------------
    public static class TafDefaultDialogListener
    {
        public void onCloseButtonClick(Dialog dialog){};
        public void onYesButtonClick(Dialog dialog){};
        public void onNoButtonClick(Dialog dialog){};
        public void onCancelButtonClick(Dialog dialog){};

    }

}

