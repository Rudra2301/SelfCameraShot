package com.tafayor.selfcamerashot.pro;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;


import com.tafayor.selfcamerashot.App;
import com.tafayor.selfcamerashot.R;
import com.tafayor.selfcamerashot.taflib.helpers.AppHelper;
import com.tafayor.selfcamerashot.taflib.helpers.LogHelper;
import com.tafayor.selfcamerashot.taflib.helpers.MsgHelper;
import com.tafayor.selfcamerashot.taflib.helpers.ResHelper;
import com.tafayor.selfcamerashot.taflib.iab.UpgradeManager;

import java.lang.ref.WeakReference;


public class Upgrader
{
    static String TAG = Upgrader.class.getSimpleName();


    private WeakReference<Activity> mActivityPtr;
    private UpgradeListenerImpl mUpgradeListener;
    UpgradeManager mUpgradeManager;
    private Context mContext;
    UpgradeCallback mUpgradeCallback;





    public Upgrader(Activity activity)
    {
        mActivityPtr = new WeakReference<Activity>(activity);
        mContext = activity.getApplicationContext();

        mUpgradeCallback = new UpgradeCallback(this);
        mUpgradeManager = new UpgradeManager(mContext, ProConfig.GOOGLE_PLAY_KEY, ProConfig.SKU_PRO);
        mUpgradeListener = new UpgradeListenerImpl(this);


    }




    
    //==============================================================================================
    // Interface
    //==============================================================================================




    //----------------------------------------------------------------------------------------------
    // upgrade
    //----------------------------------------------------------------------------------------------
    public synchronized void upgrade()
    {
        LogHelper.log(TAG, "upgrade");
        Activity activity = mActivityPtr.get(); if(activity == null) return;

        mUpgradeManager.upgrade(activity);

    }



    //----------------------------------------------------------------------------------------------
    // release
    //----------------------------------------------------------------------------------------------
    public synchronized void setup()
    {
        if(!mUpgradeManager.isSetup())
        {
            mUpgradeManager.addListener(mUpgradeListener);
            mUpgradeManager.setup();
        }
    }


    //----------------------------------------------------------------------------------------------
    // release
    //----------------------------------------------------------------------------------------------
    public synchronized void release()
    {
        if(mUpgradeManager.isSetup()) mUpgradeManager.release();
    }


    //----------------------------------------------------------------------------------------------
    // upgrade
    //----------------------------------------------------------------------------------------------
    public boolean handleActivityResult(int requestCode, int resultCode, Intent data)
    {
        return mUpgradeManager.handleActivityResult(requestCode, resultCode, data);

    }



    //----------------------------------------------------------------------------------------------
    // getUpgradeCallback
    //----------------------------------------------------------------------------------------------
    public UpgradeCallback getUpgradeCallback()
    {
        return mUpgradeCallback;

    }




    //==============================================================================================
    // Internals
    //==============================================================================================

    //----------------------------------------------------------------------------------------------
    // restartActivity
    //----------------------------------------------------------------------------------------------
    private void restartActivity()
    {
        Activity activity = mActivityPtr.get(); if(activity == null) return;
        AppHelper.restartActivity(activity);
    }




    //==============================================================================================
    // Implementation
    //==============================================================================================



    //========================================
    // UpgradeCallback
    //========================================
    static class UpgradeCallback implements  Runnable
    {
        WeakReference<Upgrader> outerPtr;

        public UpgradeCallback(Upgrader o)
        {
            outerPtr = new WeakReference<Upgrader>(o);
        }


        @Override
        public void run()
        {
            Upgrader outer = outerPtr.get();
            if(outer == null) return ;
            outer.upgrade();
        }
    }




    //===================================
    // UpgradeListenerImpl
    //===================================
    private static class UpgradeListenerImpl extends UpgradeManager.UpgradeListener
    {
        WeakReference<Upgrader> outerPtr;
        public UpgradeListenerImpl(Upgrader o)
        {
            outerPtr = new WeakReference<Upgrader>(o);
        }

        @Override
        public void onPurchaseFinished(boolean result)
        {

            LogHelper.log("onPurchaseFinished : " + result);
            final Upgrader outer = outerPtr.get();
            if(outer == null ) return;



            if(result && !App.isPro())
            {
                ProHelper.updateProState(result);

                Activity activity = outer.mActivityPtr.get(); if(activity == null) return;
                String msg = ResHelper.getResString(outer.mContext, R.string.pro_upgradeMessage);
                MsgHelper.alert(activity, msg, new Runnable() {
                    @Override
                    public void run() {
                        outer.restartActivity();
                    }
                });
            }


        }

        @Override
        public void onProStateReceived(boolean isPro)
        {

            LogHelper.log("onProStateReceived : ");
            LogHelper.log("isPro : " + isPro);
            final Upgrader outer = outerPtr.get();
            if(outer == null ) return;


            if(App.isPro() != isPro)
            {
                ProHelper.updateProState(isPro);


                Handler handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        outer.restartActivity();
                    }
                });
            }

        }


        @Override
        public void onPurchaseAgain()
        {
            final Upgrader outer = outerPtr.get();
            if(outer == null ) return;



            if(!App.isPro())
            {
                ProHelper.updateProState(true);

                Activity activity = outer.mActivityPtr.get(); if(activity == null) return;
                String msg = ResHelper.getResString(outer.mContext, R.string.pro_upgradeAgainMessage);
                MsgHelper.alert(activity, msg, new Runnable() {
                    @Override
                    public void run()
                    {
                        outer.restartActivity();
                    }
                });
            }
        }




    };



}

