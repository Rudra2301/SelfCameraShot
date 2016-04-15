package com.tafayor.selfcamerashot.pro;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;


import com.tafayor.selfcamerashot.App;
import com.tafayor.selfcamerashot.BuildConfig;
import com.tafayor.selfcamerashot.R;



/**
 * Created by youssef on 03/12/13.
 */
public class ProHelper
{
    static String TAG = ProHelper.class.getSimpleName();







    public static AppCompatDialog getProFeatureMsgDialog(Activity ctx, int msgResId, Runnable upgradeCallback )
    {
        String msg = ctx.getResources().getString(msgResId);
        return setupDialog(ctx, msg, upgradeCallback);
    }



    public static void showProMessage(Activity ctx, int msgResId, Runnable upgradeCallback)
    {
        String msg = ctx.getResources().getString(msgResId);
        AppCompatDialog dialog = setupDialog(ctx, msg, upgradeCallback);
        dialog.show();

    }



    public static void applyProState(boolean isPro)
    {


        if(App.FORCE_PRO && BuildConfig.DEBUG) isPro = true;
        App.setIsPro(isPro);
    }


    public static  void updateProState(boolean isPro)
    {

        App.getPrefHelper().setIsAppUpgraded(isPro);

        applyProState(isPro);



    }








    //==============================================================================================
    // Internals
    //==============================================================================================

    private static AppCompatDialog setupDialog(final Activity activity, String message, final Runnable upgradeCallback)
    {

        String title = activity.getString(R.string.pro_proFeatureDialog_title);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setIcon(R.drawable.ic_launcher);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.pro_proFeatureDialog_okBtn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                upgradeCallback.run();
            }
        });
        builder.setNegativeButton(R.string.pro_proFeatureDialog_cancelBtn,null);


        return (AppCompatDialog) builder.create();
    }
}


