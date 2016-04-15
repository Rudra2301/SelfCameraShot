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


package com.tafayor.selfcamerashot.taflib.iab;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import com.tafayor.selfcamerashot.App;
import com.tafayor.selfcamerashot.R;
import com.tafayor.selfcamerashot.taflib.helpers.LogHelper;
import com.tafayor.selfcamerashot.taflib.helpers.MsgHelper;
import com.tafayor.selfcamerashot.taflib.helpers.ResHelper;
import com.tafayor.selfcamerashot.taflib.iab.util.IabHelper;
import com.tafayor.selfcamerashot.taflib.iab.util.IabResult;
import com.tafayor.selfcamerashot.taflib.iab.util.Inventory;
import com.tafayor.selfcamerashot.taflib.iab.util.Purchase;
import com.tafayor.selfcamerashot.taflib.types.WeakArrayList;



public class UpgradeManager
{
    static String TAG = UpgradeManager.class.getSimpleName();


    static int RC_REQUEST  =  1001;
    static int RESPONSE_ITEM_ALREADY_OWNED = 7;


    public Context mContext;
    protected WeakArrayList<UpgradeListener> mListeners;
    private IabHelper mIabHelper;
    private boolean mIsReady = false;
    private boolean mIsSetup = false;

    private boolean mIsInventoryReady =false;
    private boolean mIsInventoryRunning =false;
    private boolean mIsPurchaseRunning =false;
    private Object mMainMutex;
    String mBase64EncodedPublicKey;
    String mProSku = "";
    Handler mUiHandler;




    private static UpgradeManager sInstance;
    public static synchronized  UpgradeManager i(Context ctx, String key, String sku)
    {
        if(sInstance == null) sInstance = new UpgradeManager(ctx,key,sku);
        return sInstance;
    }


    public UpgradeManager(Context context, String key, String sku)
    {
        mContext = context;
        mListeners =new WeakArrayList<UpgradeListener>();
        mMainMutex = new Object();
        mBase64EncodedPublicKey = key;
        mProSku = sku;
        mUiHandler = new Handler(Looper.getMainLooper());
    }








    //==============================================================================================
    // Interface
    //==============================================================================================









    public void setup()
    {

        Runnable task = new Runnable()
        {
            @Override
            public void run()
            {
                synchronized (mMainMutex)
                {
                    if(mIsSetup)
                    {
                        LogHelper.log("Iab already setup");
                        return;
                    }




                    try
                    {
                        mIabHelper = new IabHelper(mContext, mBase64EncodedPublicKey);
                        mIabHelper.enableDebugLogging(App.isDebug());

                        mIabHelper.startSetup(mIabSetupListener);
                        mIsSetup = true;
                    }
                    catch(Exception ex)
                    {
                        LogHelper.logx(ex);
                    }

                }

            }
        };


        (new Thread(task)).start();

    }



    public boolean isSetup()
    {
        return mIsSetup;
    }

    public boolean isReady()
    {
        return mIsReady;
    }


    public boolean isProStateReady()
    {
        return mIsInventoryReady;
    }



    public void release()
    {
        synchronized (mMainMutex)
        {
            try
            {
                if(mIsSetup)
                {
                    mListeners.clear();
                    mIsReady = false;
                    mIsInventoryReady = false;
                    mIsInventoryRunning = false;
                    mIsPurchaseRunning = false;


                    if (mIabHelper != null) mIabHelper.dispose();
                    mIabHelper = null;

                    mUiHandler.removeCallbacksAndMessages(null);
                    mIsSetup = false;

                }

            }
            catch(Exception ex)
            {
                LogHelper.logx(ex);
            }
        }



    }



    public boolean handleActivityResult(int requestCode, int resultCode, Intent data)
    {
        synchronized (mMainMutex)
        {
            if(mIsSetup)  return mIabHelper.handleActivityResult(requestCode, resultCode, data);
            else return false;
        }


    }


    public void reloadProState()
    {
        if(mIsReady)
        {
            if(checkBeforeRunningTask()) return;
            mIsInventoryReady = false;
            mIsInventoryRunning = true;

            try
            {
                mIabHelper.queryInventoryAsync(mGotInventoryListener);
            }
            catch(Exception ex)
            {
                LogHelper.logx(ex);
                mIsInventoryRunning = false;
            }

        }


    }




    public void upgrade(Activity activity)
    {
        String payload = "";

        if(checkBeforeRunningTask()) return;
        mIsPurchaseRunning = true;

        try
        {
            mIabHelper.launchPurchaseFlow(activity, mProSku, RC_REQUEST, mPurchaseFinishedListener,
                    payload);

        }
        catch(Exception ex)
        {
            LogHelper.logx(ex);
            mIsPurchaseRunning = false;
        }

    }



    //==============================================================================================
    // Internals
    //==============================================================================================

    private boolean verifyDeveloperPayload(Purchase purchase)
    {

        return true;
    }



    private boolean checkBeforeRunningTask()
    {
        if(mIsReady == false)
        {
            LogHelper.log(TAG, "checkBeforeRunningTask", "Iab is not ready");
            MsgHelper.toastSlow(mContext, ResHelper.getResString(mContext, R.string.pro_appstoreNeededMessage));
            return true;
        }


        if(mIsInventoryRunning == true)
        {
            LogHelper.log(TAG, "checkBeforeRunningTask", "inventory is running");
            return true;
        }

        if(mIsPurchaseRunning == true)
        {
            LogHelper.log(TAG, "checkBeforeRunningTask", "purchase is running");
            return true;
        }

        return false;
    }




    //==============================================================================================
    // Listeners
    //==============================================================================================



    //----------------------------------------------------------------------------------------------
    // addListener
    //----------------------------------------------------------------------------------------------
    public void addListener(UpgradeListener listener)
    {
        mListeners.addUnique(listener);
    }


    //----------------------------------------------------------------------------------------------
    // removeListener
    //----------------------------------------------------------------------------------------------
    public void removeListener(UpgradeListener listener)
    {
        mListeners.remove(listener);
    }



    //----------------------------------------------------------------------------------------------
    // notifyPurchaseListener
    //----------------------------------------------------------------------------------------------
    protected void notifyPurchaseListener(final boolean result)
    {
        mUiHandler.post(new Runnable() {
            @Override
            public void run() {
                for(UpgradeListener listener : mListeners)
                {
                    listener.onPurchaseFinished(result);
                }
            }
        });

    }


    //----------------------------------------------------------------------------------------------
    // notifyPurchaseAgainListeners
    //----------------------------------------------------------------------------------------------
    protected void notifyPurchaseAgainListeners( )
    {

        mUiHandler.post(new Runnable() {
            @Override
            public void run() {
                for(UpgradeListener listener : mListeners)
                {
                    listener.onPurchaseAgain();
                }
            }
        });
    }


    //----------------------------------------------------------------------------------------------
    // notifyProStateListener
    //----------------------------------------------------------------------------------------------
    protected void notifyProStateListener(final boolean isPro)
    {
        mUiHandler.post(new Runnable() {
            @Override
            public void run() {
                for(UpgradeListener listener : mListeners)
                {
                    listener.onProStateReceived(isPro);
                }
            }
        });

    }




    //==============================================================================================
    // Implementation
    //==============================================================================================


    //========================================
    // OnIabSetupFinishedListener
    //========================================
    private  IabHelper.OnIabSetupFinishedListener mIabSetupListener = new IabHelper.OnIabSetupFinishedListener()
    {
        @Override
        public void onIabSetupFinished(IabResult result)
        {
            LogHelper.log(TAG, "onIabSetupFinished");

            if(result.isFailure())
            {
                LogHelper.log(TAG, "Iab setup failed ; " + result.getMessage());
                return;
            }

            if(mIabHelper == null)
            {
                LogHelper.log(TAG, "mIabHelper null");
                return;
            }

            mIsReady = true;
            LogHelper.log(TAG, "Iab setup was successful, querying inventory");
            reloadProState();

        }
    };




    //========================================
    // QueryInventoryFinishedListener
    //========================================

    private IabHelper.QueryInventoryFinishedListener mGotInventoryListener  = new IabHelper.QueryInventoryFinishedListener()
    {
        @Override
        public void onQueryInventoryFinished(IabResult result, Inventory inventory)
        {
            LogHelper.log(TAG, "onQueryInventoryFinished");

            mIsInventoryRunning = false;

            boolean upgraded = false;
            if(result.isFailure())
            {
                LogHelper.log(TAG, "Query inventoryfailed ; " + result.getMessage());
                return;
            }

            if(mIabHelper == null)
            {
                LogHelper.log(TAG, "mIabHelper is null");
                return;
            }


            LogHelper.log(TAG, "Query inventory was successful");

            Purchase proPurchase = null;

            try
            {
                proPurchase = inventory.getPurchase(mProSku);
            }
            catch(Exception ex)
            {
                LogHelper.logx(ex);
            }




            if(proPurchase != null)
            {
                LogHelper.log(TAG, "getPurchaseState : " + proPurchase.getPurchaseState());
                upgraded  = verifyDeveloperPayload(proPurchase);
                mIsInventoryReady = true;
                notifyProStateListener(upgraded);
            }



        }
    };


    //========================================
    // QueryInventoryFinishedListener
    //========================================

    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener()
    {

        @Override
        public void onIabPurchaseFinished(IabResult result, Purchase purchase)
        {
            LogHelper.log(TAG, "onIabPurchaseFinished");
            boolean upgraded = false;


            mIsPurchaseRunning = false;

            if(result.isFailure())
            {
                LogHelper.log(TAG, "Purchase failed : " + result.getMessage());
                if(result.getResponse() == RESPONSE_ITEM_ALREADY_OWNED)
                {
                    notifyPurchaseAgainListeners();
                }
                return;
            }

            if(!verifyDeveloperPayload(purchase))
            {
                LogHelper.log(TAG, "Purchase authenticity verification failed");
                return;
            }

            if(mIabHelper == null)
            {
                LogHelper.log(TAG, "mIabHelper is null");
                return;
            }


            LogHelper.log(TAG, "Purchase was successful");

            if(purchase.getSku().equals(mProSku))
            {
                LogHelper.log(TAG, "Purchase is Pro upgrade");
                upgraded = true;
                notifyPurchaseListener(upgraded);
            }
        }
    };





    public static class UpgradeListener
    {
        public void onPurchaseFinished(boolean result){}
        public void onProStateReceived(boolean isPro){}
        public void onPurchaseAgain(){}
    }


}


