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



package com.tafayor.selfcamerashot.camera;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.support.v4.graphics.ColorUtils;


import com.tafayor.selfcamerashot.App;
import com.tafayor.selfcamerashot.R;
import com.tafayor.selfcamerashot.prefs.RemoteModeValues;
import com.tafayor.selfcamerashot.prefs.ShotCountdownValues;
import com.tafayor.selfcamerashot.tafQuickMenu.Action;
import com.tafayor.selfcamerashot.tafQuickMenu.Menu;
import com.tafayor.selfcamerashot.tafQuickMenu.MenuManager;
import com.tafayor.selfcamerashot.taflib.helpers.CameraHelper;
import com.tafayor.selfcamerashot.taflib.helpers.GraphicsHelper;
import com.tafayor.selfcamerashot.taflib.helpers.LogHelper;
import com.tafayor.selfcamerashot.utils.CamUtils;
import com.tafayor.selfcamerashot.utils.Util;

import java.util.Random;


public class CameraMenu extends Menu
{

    public static String TAG = CameraMenu.class.getSimpleName();


    enum QMenu
    {
        ACTION_CAMERA_VIEW(1),
        ACTION_CAMERA_FRONT,
        ACTION_CAMERA_REAR,

        ACTION_FLASH_MODE,
        ACTION_FLASH_AUTO,
        ACTION_FLASH_ON,
        ACTION_FLASH_OFF,
        ACTION_FLASH_TORCH,

        ACTION_WHITE_BALANCE,
        ACTION_WB_AUTO,
        ACTION_WB_CLOUDY,
        ACTION_WB_DAYLIGHT,
        ACTION_WB_FLUORESCENT,
        ACTION_WB_INCANDESCENT,

        ACTION_SHOT_COUNTDOWN,
        ACTION_SHOT_COUNTDOWN_OFF,
        ACTION_SHOT_COUNTDOWN_3S,
        ACTION_SHOT_COUNTDOWN_10S,

        ACTION_MORE,
        ACTION_SETTINGS,
        ACTION_ABOUT,

        ACTION_EXPOSURE_COMPENSATION,
        ACTION_EXPOSURE_PLUS_2,
        ACTION_EXPOSURE_PLUS_1,
        ACTION_EXPOSURE_ZERO,
        ACTION_EXPOSURE_MINUS_1,
        ACTION_EXPOSURE_MINUS_2,


        ACTION_REMOTEMODE,
        ACTION_REMOTEMODE_DISABLED,
        ACTION_REMOTEMODE_WHISTLE,
        ACTION_REMOTEMODE_CLAPPING,

        ACTION_QUALITY_CAMERA,
        ACTION_APPS;

        //------------------

        public int val;
        QMenu(){this(Counter.nextValue);}
        QMenu(int value){this.val = value;
            Counter.nextValue=value+1;}
        private static class Counter{ private static int nextValue = 0; }


    };
    
    
    Context mContext;
    CameraParameters mCamParams;
    
    
    
    
    public CameraMenu(Activity activity)
    {
        super(activity);

        mContext = activity.getApplicationContext();
    }



    //----------------------------------------------------------------------------------------------
    // setupMenu
    //----------------------------------------------------------------------------------------------
    public void setup(CameraParameters camParams)
    {
        mCamParams = camParams;

        setOrientation(MenuManager.Orientations.VERTICAL);
        setExpandDirection(MenuManager.ExpandDirections.DOWN);
        setActionWidth((int)mContext.getResources().getDimension(R.dimen.camera_qmenu_size));
        setActionHeight((int) mContext.getResources().getDimension(R.dimen.camera_qmenu_size));
        Drawable normalBg = mContext.getResources().getDrawable(R.drawable.camera_btn_selector);
        Drawable selectedBg = mContext.getResources().getDrawable(R.drawable.camera_btn_selected_selector);
        setActionNormalBackground(normalBg);
        setActionSelectedBackground(selectedBg);




        setupFlash();
        setupWhiteBalance();
        setupCountdownTimer();
        setupExposure();
        setupRemodeMode();


        Action qualityCamAction = buildAdsAction(QMenu.ACTION_QUALITY_CAMERA.val, R.drawable.ic_action_qualitycamera);
        qualityCamAction.setMode(MenuManager.ActionModes.TASK);
        addAction(qualityCamAction);


        // About
        Action aboutAction = buildAction(QMenu.ACTION_ABOUT.val, R.drawable.ic_action_about);
        aboutAction.setMode(MenuManager.ActionModes.TASK);
        addAction(aboutAction);




        // Apps
        if(!App.isPro())
        {
            Action appsAction = buildAdsAction(QMenu.ACTION_APPS.val, R.drawable.ic_action_apps);
            appsAction.setMode(MenuManager.ActionModes.TASK);
            addAction(appsAction);
        }



    }


    //----------------------------------------------------------------------------------------------
    // setupRemodeMode
    //----------------------------------------------------------------------------------------------
    private void setupRemodeMode()
    {

        Action remoteModeAction = buildAction(QMenu.ACTION_REMOTEMODE.val, R.drawable.ic_action_remotemode_whistle);
        remoteModeAction.setOrientation(MenuManager.Orientations.HORIZONTAL);
        remoteModeAction.setExpandDirection(MenuManager.ExpandDirections.RIGHT);
        remoteModeAction.setMode(MenuManager.ActionModes.SELECTION);

        Action remoteModeDisabledAction = buildAction(QMenu.ACTION_REMOTEMODE_DISABLED.val, R.drawable.ic_action_remotemode_disabled);
        remoteModeDisabledAction.setValue(RemoteModeValues.DISABLED);

        Action remoteModeWhistleAction = buildAction(QMenu.ACTION_REMOTEMODE_WHISTLE.val, R.drawable.ic_action_remotemode_whistle);
        remoteModeWhistleAction.setValue(RemoteModeValues.WHISTLE);

        Action remoteModeClappingAction = buildAction(QMenu.ACTION_REMOTEMODE_CLAPPING.val, R.drawable.ic_action_remotemode_clapping);
        remoteModeClappingAction.setValue(RemoteModeValues.CLAPPING);

        String modePref = App.getRemoteControlPrefHelper().getRemoteMode();
        if(modePref.equals(RemoteModeValues.DISABLED))
        {
            remoteModeAction.setSelectedAction(remoteModeDisabledAction);
        }
        else if(modePref.equals(RemoteModeValues.WHISTLE))
        {
            remoteModeAction.setSelectedAction(remoteModeWhistleAction);
        }
        else if(modePref.equals(RemoteModeValues.CLAPPING))
        {
            remoteModeAction.setSelectedAction(remoteModeClappingAction);
        }


        remoteModeAction.addAction(remoteModeDisabledAction);
        remoteModeAction.addAction(remoteModeWhistleAction);
        remoteModeAction.addAction(remoteModeClappingAction);
        addAction(remoteModeAction);

    }


    //----------------------------------------------------------------------------------------------
    // setupCameraView
    //----------------------------------------------------------------------------------------------
    private void setupCameraView()
    {
        // Camera view
        if(CameraHelper.areBothCamerasSupported())
        {
            Action cameraViewAction = buildAction(QMenu.ACTION_CAMERA_VIEW.val, R.drawable.ic_action_switch_camera);

            addAction(cameraViewAction);
        }
    }



    //----------------------------------------------------------------------------------------------
    // setupFlash
    //----------------------------------------------------------------------------------------------
    private void setupFlash()
    {
        // Flash
        if(mCamParams.hasFlash())
        {
            Action flashModeAction = buildAction(QMenu.ACTION_FLASH_MODE.val, R.drawable.ic_action_flash_auto);
            flashModeAction.setOrientation(MenuManager.Orientations.HORIZONTAL);
            flashModeAction.setExpandDirection(MenuManager.ExpandDirections.RIGHT);
            flashModeAction.setMode(MenuManager.ActionModes.SELECTION);

            Action flashAutoAction = buildAction(QMenu.ACTION_FLASH_AUTO.val, R.drawable.ic_action_flash_auto);
            flashAutoAction.setValue(Camera.Parameters.FLASH_MODE_AUTO);
            Action flashOnAction = buildAction(QMenu.ACTION_FLASH_ON.val, R.drawable.ic_action_flash_on);
            flashOnAction.setValue(Camera.Parameters.FLASH_MODE_ON);
            Action flashTorchAction = buildAction(QMenu.ACTION_FLASH_TORCH.val, R.drawable.ic_action_flash_torch);
            flashTorchAction.setValue(Camera.Parameters.FLASH_MODE_TORCH);
            Action flashOffAction = buildAction(QMenu.ACTION_FLASH_OFF.val, R.drawable.ic_action_flash_off);
            flashOffAction.setValue(Camera.Parameters.FLASH_MODE_OFF);



            flashModeAction.addAction(flashOffAction);
            if (mCamParams.getFlashMode().equals(Camera.Parameters.FLASH_MODE_OFF)) {

                flashModeAction.setSelectedAction(flashOffAction);
            }

            if (mCamParams.hasFlashMode(Camera.Parameters.FLASH_MODE_AUTO)) {
                flashModeAction.addAction(flashAutoAction);
                if (mCamParams.getFlashMode().equals(Camera.Parameters.FLASH_MODE_AUTO)) {

                    flashModeAction.setSelectedAction(flashAutoAction);
                }
            }
            if (mCamParams.hasFlashMode(Camera.Parameters.FLASH_MODE_ON)) {
                flashModeAction.addAction(flashOnAction);
                if (mCamParams.getFlashMode().equals(Camera.Parameters.FLASH_MODE_ON)) {

                    flashModeAction.setSelectedAction(flashOnAction);
                }
            }
            if (mCamParams.hasFlashMode(Camera.Parameters.FLASH_MODE_TORCH)) {
                flashModeAction.addAction(flashTorchAction);
                if (mCamParams.getFlashMode().equals(Camera.Parameters.FLASH_MODE_TORCH)) {

                    flashModeAction.setSelectedAction(flashTorchAction);
                }
            }

            addAction(flashModeAction);

        } // hasFlash
    }


    //----------------------------------------------------------------------------------------------
    // setupWhiteBalance
    //----------------------------------------------------------------------------------------------
    private void setupWhiteBalance()
    {


        if(mCamParams.hasWhiteBalance())
        {
            Action whiteBalanceAction = buildAction(QMenu.ACTION_WHITE_BALANCE.val, R.drawable.ic_action_wb_auto);
            whiteBalanceAction.setOrientation(MenuManager.Orientations.HORIZONTAL);
            whiteBalanceAction.setExpandDirection(MenuManager.ExpandDirections.RIGHT);
            whiteBalanceAction.setMode(MenuManager.ActionModes.SELECTION);

            Action wbAutoAction = buildAction(QMenu.ACTION_WB_AUTO.val, R.drawable.ic_action_wb_auto);
            wbAutoAction.setValue(Camera.Parameters.WHITE_BALANCE_AUTO);
            Action wbCloudyAction = buildAction(QMenu.ACTION_WB_CLOUDY.val, R.drawable.ic_action_wb_cloudy);
            wbCloudyAction.setValue(Camera.Parameters.WHITE_BALANCE_CLOUDY_DAYLIGHT);
            Action wbDaylightAction = buildAction(QMenu.ACTION_WB_DAYLIGHT.val, R.drawable.ic_action_wb_daylight);
            wbDaylightAction.setValue(Camera.Parameters.WHITE_BALANCE_DAYLIGHT);
            Action wbFluorescentAction = buildAction(QMenu.ACTION_WB_FLUORESCENT.val, R.drawable.ic_action_wb_fluorescent);
            wbFluorescentAction.setValue(Camera.Parameters.WHITE_BALANCE_FLUORESCENT);
            Action wbIncandescentAction = buildAction(QMenu.ACTION_WB_INCANDESCENT.val, R.drawable.ic_action_wb_incandescent);
            wbIncandescentAction.setValue(Camera.Parameters.WHITE_BALANCE_INCANDESCENT);




            if (mCamParams.hasWhiteBalanceMode(Camera.Parameters.WHITE_BALANCE_AUTO)) {
                whiteBalanceAction.addAction(wbAutoAction);
                if (mCamParams.getWhiteBalance().equals(Camera.Parameters.WHITE_BALANCE_AUTO)) {
                    whiteBalanceAction.setSelectedAction(wbAutoAction);
                }
            }

            if (mCamParams.hasWhiteBalanceMode(Camera.Parameters.WHITE_BALANCE_CLOUDY_DAYLIGHT)) {
                whiteBalanceAction.addAction(wbCloudyAction);
                if (mCamParams.getWhiteBalance().equals(Camera.Parameters.WHITE_BALANCE_CLOUDY_DAYLIGHT)) {
                    whiteBalanceAction.setSelectedAction(wbCloudyAction);
                }
            }

            if (mCamParams.hasWhiteBalanceMode(Camera.Parameters.WHITE_BALANCE_DAYLIGHT)) {
                whiteBalanceAction.addAction(wbDaylightAction);
                if (mCamParams.getWhiteBalance().equals(Camera.Parameters.WHITE_BALANCE_DAYLIGHT)) {
                    whiteBalanceAction.setSelectedAction(wbDaylightAction);
                }
            }

            if (mCamParams.hasWhiteBalanceMode(Camera.Parameters.WHITE_BALANCE_FLUORESCENT)) {
                whiteBalanceAction.addAction(wbFluorescentAction);
                if (mCamParams.getWhiteBalance().equals(Camera.Parameters.WHITE_BALANCE_FLUORESCENT)) {
                    whiteBalanceAction.setSelectedAction(wbFluorescentAction);
                }
            }

            if (mCamParams.hasWhiteBalanceMode(Camera.Parameters.WHITE_BALANCE_INCANDESCENT)) {
                whiteBalanceAction.addAction(wbIncandescentAction);
                if (mCamParams.getWhiteBalance().equals(Camera.Parameters.WHITE_BALANCE_INCANDESCENT)) {
                    whiteBalanceAction.setSelectedAction(wbIncandescentAction);
                }
            }

            addAction(whiteBalanceAction);

        }//hasWhiteBalance
    }


    //----------------------------------------------------------------------------------------------
    // setupCountdownTimer
    //----------------------------------------------------------------------------------------------
    private void setupCountdownTimer()
    {

        Action shotCountdownAction = buildAction(QMenu.ACTION_SHOT_COUNTDOWN.val, R.drawable.ic_action_timer_off);
        shotCountdownAction.setOrientation(MenuManager.Orientations.HORIZONTAL);
        shotCountdownAction.setExpandDirection(MenuManager.ExpandDirections.RIGHT);
        shotCountdownAction.setMode(MenuManager.ActionModes.SELECTION);

        Action shotCountdownOffAction = buildAction(QMenu.ACTION_SHOT_COUNTDOWN_OFF.val, R.drawable.ic_action_timer_off);
        shotCountdownOffAction.setValue(ShotCountdownValues.OFF);

        Action shotCountdown3SAction = buildAction(QMenu.ACTION_SHOT_COUNTDOWN_3S.val, R.drawable.ic_action_timer_3);
        shotCountdown3SAction.setValue(ShotCountdownValues._3S);

        Action shotCountdown10SAction = buildAction(QMenu.ACTION_SHOT_COUNTDOWN_10S.val, R.drawable.ic_action_timer_10);
        shotCountdown10SAction.setValue(ShotCountdownValues._10S);

        String shotCountdownPref = App.getCameraPrefHelper().getShotCountdown();
        if(shotCountdownPref.equals(ShotCountdownValues.OFF))
        {
            shotCountdownAction.setSelectedAction(shotCountdownOffAction);
        }
        else if(shotCountdownPref.equals(ShotCountdownValues._3S))
        {
            shotCountdownAction.setSelectedAction(shotCountdown3SAction);
        }
        else if(shotCountdownPref.equals(ShotCountdownValues._10S))
        {
            shotCountdownAction.setSelectedAction(shotCountdown10SAction);
        }
        shotCountdownAction.addAction(shotCountdownOffAction);
        shotCountdownAction.addAction(shotCountdown3SAction);
        shotCountdownAction.addAction(shotCountdown10SAction);
        addAction(shotCountdownAction);
    }


    //----------------------------------------------------------------------------------------------
    // setupExposure
    //----------------------------------------------------------------------------------------------
    private void setupExposure()
    {
        if(mCamParams.hasExposure())
        {
            Action exposureCompensationAction = buildAction(QMenu.ACTION_EXPOSURE_COMPENSATION.val, R.drawable.ic_action_exposure);
            exposureCompensationAction.setOrientation(MenuManager.Orientations.HORIZONTAL);
            exposureCompensationAction.setExpandDirection(MenuManager.ExpandDirections.RIGHT);
            exposureCompensationAction.setMode(MenuManager.ActionModes.SELECTION);
            exposureCompensationAction.setDisplayMode(MenuManager.ActionDisplayModes.LIST_SINGLE_SELECTION);

            Action exposureMinus2Action = buildAction(QMenu.ACTION_EXPOSURE_MINUS_2.val, R.drawable.ic_action_exposure_minus_2);
            exposureMinus2Action.setValue(-1.f);

            Action exposureMinus1Action = buildAction(QMenu.ACTION_EXPOSURE_MINUS_1.val, R.drawable.ic_action_exposure_minus_1);
            exposureMinus1Action.setValue(-0.5f);

            Action exposureZeroAction = buildAction(QMenu.ACTION_EXPOSURE_ZERO.val, R.drawable.ic_action_exposure_zero);
            exposureZeroAction.setValue(0.f);

            Action exposurePlus1Action = buildAction(QMenu.ACTION_EXPOSURE_PLUS_1.val, R.drawable.ic_action_exposure_plus_1);
            exposurePlus1Action.setValue(0.5f);

            Action exposurePlus2Action = buildAction(QMenu.ACTION_EXPOSURE_PLUS_2.val, R.drawable.ic_action_exposure_plus_2);
            exposurePlus2Action.setValue(1.f);



            float exposurePref = App.getCameraPrefHelper().getExposure(mCamParams.getCameraId());

            if(exposurePref == -1)
            {
                exposureCompensationAction.setSelectedAction(exposureMinus2Action);
            }
            else if(exposurePref == -0.5f)
            {
                exposureCompensationAction.setSelectedAction(exposureMinus1Action);
            }
            else if(exposurePref == 0)
            {
                exposureCompensationAction.setSelectedAction(exposureZeroAction);
            }
            else if(exposurePref == 0.5f)
            {
                exposureCompensationAction.setSelectedAction(exposurePlus1Action);
            }
            else if(exposurePref == 1f)
            {
                exposureCompensationAction.setSelectedAction(exposurePlus2Action);
            }

            exposureCompensationAction.addAction(exposureMinus2Action);
            exposureCompensationAction.addAction(exposureMinus1Action);
            exposureCompensationAction.addAction(exposureZeroAction);
            exposureCompensationAction.addAction(exposurePlus1Action);
            exposureCompensationAction.addAction(exposurePlus2Action);
            addAction(exposureCompensationAction);
        }

    }




    //----------------------------------------------------------------------------------------------
    // buildAction
    //----------------------------------------------------------------------------------------------
    private Action buildAction(int id, int resId)
    {

        Drawable icon;

        icon = CamUtils.addShadow(mContext, resId);

        return new Action(id, icon);
    }


    //----------------------------------------------------------------------------------------------
    // buildAction
    //----------------------------------------------------------------------------------------------
    private Action buildAdsAction(int id, int resId)
    {




        int tintColor ;
        Random r = new Random();
        float h = r.nextFloat() * 365;
        float s = 0.9f;
        float l = 0.8f;
        tintColor = ColorUtils.HSLToColor(new float[]{h, s, l});
        final int glowColor = mContext.getResources().getColor(R.color.camera_btn_glowColor);
        Bitmap bmp ;
        Drawable icon;


         bmp = BitmapFactory.decodeResource(mContext.getResources(), resId);
        Drawable drawable = new BitmapDrawable(mContext.getResources(), bmp);
         icon = Util.tint(drawable, tintColor);

        bmp = GraphicsHelper.drawableToBitmap(icon);
        bmp = GraphicsHelper.addGlow(bmp, glowColor);
        icon = new BitmapDrawable(mContext.getResources(), bmp);





        return new Action(id, icon);
    }

}
