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

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.WindowManager;


import com.tafayor.selfcamerashot.taflib.helpers.LogHelper;
import com.tafayor.selfcamerashot.taflib.helpers.ViewHelper;
import com.tafayor.selfcamerashot.taflib.interfaces.IClosingUi;

import java.lang.ref.WeakReference;




public class TafBaseDialog
{
    public static String TAG = TafBaseDialog.class.getSimpleName();

    private DialogSF mDialogSF = null;
    private DialogF mDialogF = null;
    private DialogC mDialogC = null; // for Contexts (Activity and Service)
    private BaseDialogBuilder mFactory;
    private int mTheme = 0;
    private Context mContext;



    //==============================================================================================
    // __Init
    //==============================================================================================


    //----------------------------------------------------------------------------------------------
    //  UniversalDialog
    //----------------------------------------------------------------------------------------------
    public TafBaseDialog(Context ctx)
    {
        mContext = ctx;
    }


    //----------------------------------------------------------------------------------------------
    //  UniversalDialog
    //----------------------------------------------------------------------------------------------
    public TafBaseDialog( BaseDialogBuilder factory)
    {

        mFactory = factory;

    }



    protected Context getContext(){return mContext;}

    //==============================================================================================
    // __Interface
    //==============================================================================================

    //----------------------------------------------------------------------------------------------
    //  setFactory
    //----------------------------------------------------------------------------------------------
    public void setFactory(BaseDialogBuilder factory)
    {
        mFactory = factory;

    }

    //----------------------------------------------------------------------------------------------
    //  setTheme
    //----------------------------------------------------------------------------------------------
    public void setTheme(int theme)
    {
        mTheme = theme;

    }



    //----------------------------------------------------------------------------------------------
    //  isShowing
    //----------------------------------------------------------------------------------------------

    public boolean isShowing()
    {
        boolean _isShowing = false;
        if(mDialogSF != null) _isShowing |= mDialogSF.isShowing();
        if(mDialogF != null) _isShowing |= mDialogF.isShowing();
        if(mDialogC != null) _isShowing |= mDialogC.isShowing();

        return _isShowing;
    }


    //----------------------------------------------------------------------------------------------
    // dismiss
    //----------------------------------------------------------------------------------------------

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void dismiss()
    {
        if(mDialogSF != null && mDialogSF.isShowing() )  {mDialogSF.dismiss(); mDialogSF = null;}

        if( mDialogF != null && mDialogF.isShowing() )
        {
            mDialogF.dismiss(); mDialogF = null;
        }

        if(mDialogC != null && mDialogC.isShowing()) { mDialogC.dismiss(); mDialogC = null;}


    }


    //----------------------------------------------------------------------------------------------
    // show
    //----------------------------------------------------------------------------------------------
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void show (android.app.FragmentManager fm)
    {
        if(isShowing())
        {
            LogHelper.log(TAG, "Dialog is already showing");
            return;
        }

        mDialogF =  DialogF.newInstance(mFactory);
        mDialogF.show(fm, "");
    }






    //----------------------------------------------------------------------------------------------
    // show
    //----------------------------------------------------------------------------------------------
    public void show (Activity activity)
    {
        if(isShowing())
        {
            LogHelper.log(TAG, "Dialog is already showing");
            return;
        }

        mDialogC = new DialogC(activity, mFactory, mTheme);
        mDialogC.show();
    }

    //----------------------------------------------------------------------------------------------
    // show
    //----------------------------------------------------------------------------------------------
    public void show (Context appContext)
    {
        if(isShowing())
        {
            LogHelper.log(TAG, "Dialog is already showing");
            return;
        }

        mDialogC = new DialogC(appContext, mFactory, mTheme);
        mDialogC.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        mDialogC.show();
    }



    //----------------------------------------------------------------------------------------------
    // show
    //----------------------------------------------------------------------------------------------
    public void show (final FragmentManager fm)
    {
        if(isShowing())
        {
            LogHelper.log(TAG, "Dialog is already showing");
            return;
        }

        mDialogSF =  DialogSF.newInstance(mFactory);
        mDialogSF.show(fm, null);
    }

    //==============================================================================================
    // Internals
    //==============================================================================================




    //==============================================================================================
    // UniversalDialogFactory
    //==============================================================================================

    public static  class BaseDialogBuilder implements Parcelable
    {


        
        public BaseDialogBuilder()
        {

        }

        public BaseDialogBuilder(Parcel in)
        {

        }

        public void init(DialogFragment dialog )
        {

        }

        public Dialog createDialog(Context context){return null;}
        public View getView(DialogFragment dialog){return null;}

        public void setupDialog(Dialog dialog){}

        public void onDialogReady(Dialog dialog){}
        public void onDialogReady(DialogFragment dialog){}

        public void onFreeMemory(){}








        @Override
        public int describeContents()
        {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i)
        {

        }


        static final Creator<BaseDialogBuilder> CREATOR
                = new Creator<BaseDialogBuilder>()
        {

           public BaseDialogBuilder createFromParcel(Parcel in)
            {
                return new BaseDialogBuilder(in);
            }

            public BaseDialogBuilder[] newArray(int size)
            {
                return new BaseDialogBuilder[size];
            }
        };
    }




    //==============================================================================================
    // DialogSF
    //==============================================================================================
    public  static class DialogSF extends DialogFragment
    {
        public static String TAG = DialogSF.class.getSimpleName();


        private static String KEY_FACTORY_TAG = "keyFactoryTag";

        BaseDialogBuilder mFactory ;



        public DialogSF()
        {

        }


        public static final DialogSF newInstance(BaseDialogBuilder factory)
        {
            DialogSF frag = new DialogSF();
            Bundle args = new Bundle();
            args.putParcelable(KEY_FACTORY_TAG, factory);
            frag.setArguments(args);

            return frag;
        }


        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
        }


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState)
        {
            LogHelper.log(TAG, "onCreateDialog");
            mFactory =  getArguments().getParcelable(KEY_FACTORY_TAG);
            if(mFactory == null)
            {
                dismiss();
                return null;
            }

            Dialog dialog = mFactory.createDialog(getActivity());
            dialog.show();
            return dialog;
        }


        @Override
        public void onActivityCreated(Bundle savedInstanceState)
        {
            LogHelper.log(TAG, "onActivityCreated");
            mFactory.onDialogReady(getDialog());
            super.onActivityCreated(savedInstanceState);

        }



        @Override
        public void onDestroyView()
        {
            LogHelper.log(TAG, "onDestroyView");
            if (getDialog() != null && getRetainInstance())
            {
                getDialog().setDismissMessage(null); // fix bug : dialog disapears after config change
            }

            super.onDestroyView();
        }


        @Override
        public void onDestroy()
        {
            LogHelper.log(TAG, "onDestroy");
            super.onDestroy();
            mFactory.onFreeMemory();
            ViewHelper.unbindDrawables(getView());
            mFactory = null;
            System.gc();
        }


        //Fixes a bug
        //Bug : isVisible doesn't works when the dialog is created withAlertDialog.Builder.create()
        //Fix : using getDialog().isShowing()
        public boolean isShowing()
        {
            return (isVisible() ||  (getDialog()!=null && getDialog().isShowing()));
        }

    }




    //==============================================================================================
    // DialogF
    //==============================================================================================

    @SuppressLint("NewApi")
    static public class DialogF extends android.app.DialogFragment
    {
        public static String TAG = DialogF.class.getSimpleName();

        private static String KEY_FACTORY_TAG = "keyFactoryTag";

        BaseDialogBuilder mFactory ;



        public DialogF()
        {

        }



        public static final DialogF newInstance(BaseDialogBuilder factory)
        {
            DialogF frag = new DialogF();
            Bundle args = new Bundle();
            args.putParcelable(KEY_FACTORY_TAG, factory);
            frag.setArguments(args);

            return frag;
        }

        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState)
        {
            mFactory = (BaseDialogBuilder)  getArguments().getParcelable(KEY_FACTORY_TAG);
            if(mFactory == null)
            {
                dismiss();
                return null;
            }

            Dialog dialog = mFactory.createDialog(getActivity());
            dialog.show();
            return dialog;
        }


        @Override
        public void onActivityCreated(Bundle savedInstanceState)
        {

            mFactory.onDialogReady(getDialog());
            super.onActivityCreated(savedInstanceState);
        }


        @Override
        public void onDestroyView()
        {
            LogHelper.log(TAG, "onDestroyView");
            if (getDialog() != null && getRetainInstance())
            {
                getDialog().setDismissMessage(null); // fix bug : dialog disapears after config change
            }

            super.onDestroyView();
        }


        @Override
        public void onDestroy()
        {
            super.onDestroy();
            LogHelper.log(TAG, "onDestroy");
            mFactory.onFreeMemory();
            ViewHelper.unbindDrawables(getView());
            System.gc();
        }


        //Fixingg a bug
        //Bug : isVisible doesn't works when the dialog is created withAlertDialog.Builder.create()
        //Fix : getDialog().isShowing()
        public boolean isShowing()
        {
            return (isVisible() || (getDialog()!=null && getDialog().isShowing()));
        }
    }


    //==============================================================================================
    // DialogC (called from Contexts (activities and services)
    //==============================================================================================
    static public  class DialogC extends Dialog
    {
        public static String TAG = DialogC.class.getSimpleName();


        BaseDialogBuilder mFactory ;



        public DialogC(final Activity activity, BaseDialogBuilder factory, int theme)
        {

            super(activity, theme);
            LogHelper.log(TAG , "DialogC for activity");
            mFactory = factory;




        }

        public DialogC(final Context appContext, BaseDialogBuilder factory, int theme)
        {

            super(appContext.getApplicationContext() , theme);
            LogHelper.log(TAG , "DialogC for service");
            mFactory = factory;



        }




        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            LogHelper.log(TAG , "onCreate");
            if(mFactory == null)
            {
                dismiss();
                return;
            }
            mFactory.setupDialog(this);
        }


        @Override
        protected void onStart()
        {
            LogHelper.log(TAG , "onStart");
            mFactory.onDialogReady(this);
            super.onStart();
        }


        @Override
        protected void onStop()
        {
            super.onStop();
            LogHelper.log(TAG , "onStop");
            mFactory.onFreeMemory();
        }


        private static class ClosingUiListenerImpl implements IClosingUi.ClosingUiListener
        {
            WeakReference<DialogC> outerPtr;
            public ClosingUiListenerImpl(DialogC o)
            {
                outerPtr = new WeakReference<DialogC>(o);
            }

            @Override
            public void onCloseQuery()
            {
                DialogC outer = outerPtr.get();
                if(outer == null) return ;

                outer.dismiss();
            }
        }

    }







    //==============================================================================================
    // Types
    //==============================================================================================






}
