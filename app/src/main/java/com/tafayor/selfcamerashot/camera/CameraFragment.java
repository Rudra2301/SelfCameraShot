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
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.location.Location;
import android.media.ExifInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;



import com.tafayor.selfcamerashot.App;
import com.tafayor.selfcamerashot.R;
import com.tafayor.selfcamerashot.UiValues;
import com.tafayor.selfcamerashot.anroid.exif.Exif;

import com.tafayor.selfcamerashot.gallery.GalleryManager;
import com.tafayor.selfcamerashot.gallery.ImageUtils;

import com.tafayor.selfcamerashot.prefs.LegacyCameraSettingsActivity;
import com.tafayor.selfcamerashot.prefs.PrefValsProxy;
import com.tafayor.selfcamerashot.prefs.RemoteModeValues;
import com.tafayor.selfcamerashot.prefs.SettingsActivity;
import com.tafayor.selfcamerashot.prefs.ShotCountdownValues;
import com.tafayor.selfcamerashot.prefs.VolumeButtonsActivatorValues;
import com.tafayor.selfcamerashot.pro.ProHelper;
import com.tafayor.selfcamerashot.pro.Upgrader;
import com.tafayor.selfcamerashot.remoteControl.RemoteControl;
import com.tafayor.selfcamerashot.remoteControl.sound.SoundControlListener;

import com.tafayor.selfcamerashot.tafQuickMenu.Action;
import com.tafayor.selfcamerashot.tafQuickMenu.MenuManager;
import com.tafayor.selfcamerashot.taflib.helpers.AnimHelper;
import com.tafayor.selfcamerashot.taflib.helpers.DisplayHelper;
import com.tafayor.selfcamerashot.taflib.helpers.GraphicsHelper;
import com.tafayor.selfcamerashot.taflib.helpers.LangHelper;
import com.tafayor.selfcamerashot.taflib.helpers.LogHelper;
import com.tafayor.selfcamerashot.taflib.helpers.MarketHelper;
import com.tafayor.selfcamerashot.taflib.helpers.MsgHelper;
import com.tafayor.selfcamerashot.taflib.helpers.SoundHelper;
import com.tafayor.selfcamerashot.taflib.helpers.ViewHelper;
import com.tafayor.selfcamerashot.taflib.types.Size;
import com.tafayor.selfcamerashot.taflib.ui.custom.CustomVerticalSeekBar;
import com.tafayor.selfcamerashot.ui.FragmentWrapperActivity;
import com.tafayor.selfcamerashot.utils.CamUtils;
import com.tafayor.selfcamerashot.utils.OnGestureTouchListener;
import com.tafayor.selfcamerashot.taflib.managers.OrientationManager;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;



/**
 * Created by weber on 02/02/2015.
 */
public class CameraFragment extends Fragment implements CameraPreview.CameraPreviewListener,
        SoundControlListener, MenuManager.ActionListener,
        View.OnKeyListener,
        LocationManager.Listener

{

    public static String TAG = CameraFragment.class.getSimpleName();






    //Constants
    private static final int INITIAL_VIEWS_ORIENTATION = 0;
    private static final long SHOT_THROTTLE = 1000; //ms


    private static int STATE_READY = 0;
    private static int STATE_SNAPSHOT = 1;
    private static int STATE_TOUCH_TO_FOCUS = 2;
    private static int STATE_BUSY = 3;
    private static int STATE_STARTING = 4;
    private static int STATE_FINISHING = 5;
    private static int STATE_SWITCHING = 6;



    CameraPreview mCameraPreview;

    private int mCameraId;

    private CameraParameters mCamParams;
    private CameraMenu mQMenu;

    private Context mContext;

    private ViewGroup mRootView;
    private Handler mUiHandler;


    //Views
    private LinearLayout mZoomPanel;
    private GalleryManager mGalleryManager;
    private ImageView mCaptureBtn;
    private FrameLayout mPreviewContainer;
    private FrameLayout mOverlaysContainer;
    private FocusOverlay  mFocusOverlay;
    ImageView mMenuBtn;
    ImageView mGalleryBtn;
    ImageView mSettingsBtn;
    ImageView mCameraToggleBtn;
    private CameraOverlay mCameraOverlay;
    private ImageView mThumbnailView;
    private TextView mZoomMinusView;
    private CustomVerticalSeekBar mZoomSlider;
    private ScreenFlash mScreenFlash;

    private Button mBetaBtn;

    // Utils
    private RemoteControl mRemoteControl;


    //Flags


    //Implementation
    private OrientationListenerImpl mOrientationListener;
    private FocusListener mFocusListener;
    private PreviewTouchGestureListener mPreviewTouchGestureListener;


    //animation
    int mCurrentViewsOrientation = 0;
    List<View> mViewsToRotate;
    int mScreenOrientationAngle;


    //Async
    private static volatile HandlerThread mThread;
    private static volatile Handler mAsyncHandler;


    //Managers
    private FocusManager mFocusManager;
    private LocationManager mLocationManager;


    //time
    private long mLastShotTimeMs;

    //Params
    private int mState = STATE_READY;
    private Location mLastCaptureLocation;
    private Size mPictureSize;
    private int mPictureFormat;


    //alerts
    private AppCompatDialog mClappingProDialog;



    public CameraFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mContext = getActivity().getApplicationContext();
        init();
    }


    void init()
    {
        mUiHandler = new Handler();
        mQMenu = new CameraMenu(getActivity());
        mQMenu.setActionListener(this);
        mGalleryManager = new GalleryManager(getActivity());
        mLocationManager = new LocationManager(mContext, this);
        mRemoteControl = new RemoteControl(mContext);


        mOrientationListener = new OrientationListenerImpl();
        mViewsToRotate = new ArrayList<>();
        mCamParams = new CameraParameters();
        mFocusListener = new FocusListener();
        mFocusManager = new FocusManager(mContext, mCamParams, mFocusListener);
        mLastShotTimeMs = System.currentTimeMillis();
        mPreviewTouchGestureListener = new PreviewTouchGestureListener(mContext);




    }








    //==============================================================================================
    // Callbacks
    //==============================================================================================




    //----------------------------------------------------------------------------------------------
    // onVolumeButtonActivatorEvent
    //----------------------------------------------------------------------------------------------
    public void onVolumeButtonActivatorEvent(int keyCode , KeyEvent event)
    {
        String pref = App.getActivatorsPrefHelper().getVolumeButtonsActivator();
        if(pref.equals(VolumeButtonsActivatorValues.SHUTTER))
        {
            if(event.getAction() == KeyEvent.ACTION_UP)
            {
                takePicture();
            }
        }
        else if(pref.equals(VolumeButtonsActivatorValues.ZOOM))
        {
            int inc = 3 ;
            if(event.getAction() == KeyEvent.ACTION_MULTIPLE) inc = event.getRepeatCount();


            if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) inc *= -1;

            int progress = mZoomSlider.getProgress() + inc;
            progress = LangHelper.clamp(progress, 1, 100);
            mZoomSlider.setProgressAndMoveThumb(progress);

        }

    }








    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        int deviceOrientation = OrientationManager.getInstance().getX90Orientation();

        for(View view : mViewsToRotate)
        {
            CamUtils.rotateView(view, mScreenOrientationAngle, deviceOrientation);
        }


        Runnable cb = ((CameraActivity)(getActivity())).getUpgrader().getUpgradeCallback();
        mClappingProDialog = ProHelper.getProFeatureMsgDialog(getActivity(),
                R.string.pro_proFeatureDialog_clappingMessage,
                cb);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        LogHelper.log(TAG, "onCreateView start");
        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        initView(view);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(this);

        LogHelper.log(TAG, "onCreateView end");

        return view;
    }


    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);


        mThread = new HandlerThread("");
        mThread.start();
        mAsyncHandler = new Handler(mThread.getLooper());

    }



    @Override
    public void onDetach()
    {
        super.onDetach();

        mAsyncHandler.removeCallbacksAndMessages(null);
        mThread.quit();

    }




    @Override
    public  void onResume()
    {
        super.onResume();
        LogHelper.log(TAG, "onResume");


        mState = STATE_STARTING;
        start();


        LogHelper.log("end resume");

    }




    @Override
    public  void onPause()
    {
        super.onPause();
        LogHelper.log(TAG, "onPause");
        try
        {
            mState = STATE_FINISHING;
            release();
        }
        catch(Exception e)
        {
            LogHelper.logx(e);
        }


    }




    //==============================================================================================
    // Listeners
    //==============================================================================================





    //==============================================================================================
    // Internals
    //==============================================================================================

    //----------------------------------------------------------------------------------------------
    // onPictureTakenUiTask
    //----------------------------------------------------------------------------------------------
    private synchronized void onPictureTakenUiTask()
    {
        LogHelper.log(TAG, "onPictureTakenUiTask");
        mCaptureBtn.setEnabled(true);
        mFocusManager.endFocusSearch();
        mScreenFlash.flashOff();

        mState = STATE_READY;
    }

    //----------------------------------------------------------------------------------------------
    // setupMenu
    //----------------------------------------------------------------------------------------------
    private void setupMenu()
    {
        mQMenu.setup(mCamParams);

        if(Camera.getNumberOfCameras()>1)
        {
            mCameraToggleBtn.setVisibility(View.VISIBLE);
        }


    }
    //----------------------------------------------------------------------------------------------
    // toggleCamera
    //----------------------------------------------------------------------------------------------
    private synchronized void toggleCamera()
    {
        mState = STATE_SWITCHING;
        release();
        mCamParams.toggleCameraView();


        App.getCameraPrefHelper().setCameraView(mCamParams.getCameraId());

        start();
    }



    //----------------------------------------------------------------------------------------------
    // resumeView
    //----------------------------------------------------------------------------------------------
    private void resumeView()
    {



        int deviceOrientation = OrientationManager.getInstance().getX90Orientation();
        CamUtils.rotateView(mGalleryBtn, mScreenOrientationAngle, deviceOrientation);
        mThumbnailView.setVisibility(View.GONE);
        mGalleryBtn.setVisibility(View.VISIBLE);
    }


    //----------------------------------------------------------------------------------------------
    // resetLocks
    //----------------------------------------------------------------------------------------------
    private synchronized  void resetLocks()
    {

        mCaptureBtn.setEnabled(true);
        mState = STATE_READY;
    }


    //----------------------------------------------------------------------------------------------
    // initialize
    //----------------------------------------------------------------------------------------------
    private void start()
    {
        resumeView();
        mScreenOrientationAngle = DisplayHelper.getRelativeScreenOrientationAngle(mContext);

        OrientationManager.getInstance().addListener(mOrientationListener);
        mLocationManager.recordLocation(App.getAdvancedCameraPrefHelper().getEnableLocationRecording());



        setupCameraPreview();
        setupRemoteControl();
        openCameraAsync();


    }




    //----------------------------------------------------------------------------------------------
    // release
    //----------------------------------------------------------------------------------------------
    private void release()
    {



        mCameraPreview.closeCamera();

        mLocationManager.recordLocation(false);
        mScreenFlash.release();
        mFocusManager.end();
        OrientationManager.getInstance().removeListener(mOrientationListener);
        mRemoteControl.release();
        mCameraOverlay.release();
        mCaptureBtn.setEnabled(true);
        mCameraPreview.setListener(null);
        mCameraPreview.setOnTouchListener(null);
        mPreviewContainer.removeView(mCameraPreview);
        mQMenu.release();

        if(mClappingProDialog.isShowing()) mClappingProDialog.dismiss();

    }



    //----------------------------------------------------------------------------------------------
    //     private void setupFocusManager()

    //----------------------------------------------------------------------------------------------
    private void setupFocusManager()
    {


        mFocusManager.setMirror(mCamParams.isFrontCamera());
        mFocusManager.setDisplayOrientation(mCameraPreview.getDisplayOrientation());
        LogHelper.log(" focus mode : " + App.getCameraPrefHelper().getFocusMode());

        if(Build.VERSION.SDK_INT >= 14 &&
                (mCamParams.getFocusMode().equals(CameraParameters.FOCUS_MODE_AUTO) ||
                        mCamParams.getFocusMode().equals(CameraParameters.FOCUS_MODE_MACRO))
                )
        {

            mFocusManager.enableTouchToFocus(App.getAdvancedCameraPrefHelper().getEnableTouchToFocus());
        }
        else
        {
            mFocusManager.enableTouchToFocus(false);
        }

        mFocusManager.setup();
        mFocusManager.start();
    }

    //----------------------------------------------------------------------------------------------
    // isServiceAvailble
    //----------------------------------------------------------------------------------------------
    public boolean isServiceAvailble()
    {
        boolean result = isAdded() &&
                !getActivity().isFinishing() &&
                mCameraPreview != null &&
                mCameraPreview.isCameraOpen();
        if(!result) LogHelper.log(TAG, "service is not available");
        return result;
    }


    //----------------------------------------------------------------------------------------------
    // isUiAvailable
    //----------------------------------------------------------------------------------------------
    public boolean isUiAvailable()
    {
        boolean result = isAdded() &&
                !getActivity().isFinishing();

        if(!result) LogHelper.log(TAG, "Ui is not available");
        return result;
    }


    //----------------------------------------------------------------------------------------------
    // openCameraAsync
    //----------------------------------------------------------------------------------------------
    void openCameraAsync()
    {
        Runnable task = new Runnable()
        {
            @Override
            public void run()
            {
                if(isUiAvailable())
                {
                    int cameraId = 0;


                    cameraId = App.getCameraPrefHelper().getCameraView();
                    mCamParams.setCameraId(cameraId);
                    mCameraPreview.openCamera(cameraId);


                }



            }
        };

//        (new Thread(task)).start();
        mUiHandler.postDelayed(task, 100);
    }




    //----------------------------------------------------------------------------------------------
    // setupCameraParams
    //----------------------------------------------------------------------------------------------
    private void setupCameraParams()
    {
        mCamParams.loadAppSettings();

        mCamParams.setupCamera();

        if(mCamParams.hasZoom()) mCamParams.setZoom(mZoomSlider.getProgress());
    }


    //----------------------------------------------------------------------------------------------
    // setupRemoteControl
    //----------------------------------------------------------------------------------------------
    private void setupRemoteControl()
    {

        float sensitivity = 1 ;

        String remoteMode = App.getRemoteControlPrefHelper().getRemoteMode();



        mRemoteControl = new RemoteControl(mContext);
        sensitivity = App.getRemoteControlPrefHelper().getSoundControlSensitivity()/100.f;



        if(remoteMode.equals(RemoteModeValues.WHISTLE))
        {
            mRemoteControl.setSensitivity(App.getRemoteControlPrefHelper().getWhistleSensitivity() / 100.f);
            mRemoteControl.startWhistleDetection();
        }
        else if(remoteMode.equals(RemoteModeValues.CLAPPING))
        {
            mRemoteControl.setSensitivity(App.getRemoteControlPrefHelper().getClappingSensitivity() / 100.f);
            mRemoteControl.startClappingDetection();
        }


        mRemoteControl.addSoundListener(this);


    }


    //----------------------------------------------------------------------------------------------
    // setupCameraPreview
    //----------------------------------------------------------------------------------------------
    private void setupCameraPreview()
    {
        mCameraPreview = new CameraPreview(mContext);
        mCameraPreview.setListener(this);
        mCameraPreview.setOnTouchListener(mPreviewTouchGestureListener);

        Size size = DisplayHelper.getScreenSize(mContext);
        FrameLayout.LayoutParams lparams = new FrameLayout.LayoutParams(
                size.width, size.height);
        mPreviewContainer.removeAllViews();

        mPreviewContainer.addView(mCameraPreview, lparams);
    }


    //----------------------------------------------------------------------------------------------
    // resizeCameraPreview
    //----------------------------------------------------------------------------------------------
    private void resizeCameraPreview()
    {


        final double ASPECT_TOLERANCE = 0.1;//0.01
        int pw , ph;
        int w,h;
        Size size = new Size();
        Size mPreviewSize = mCamParams.getPreviewSize();


        if((mCamParams.getDisplayOrientation(mContext) % 180) != 0)
        {

            pw = mPreviewSize.height;
            ph = mPreviewSize.width;
        }
        else
        {
            pw = mPreviewSize.width;
            ph = mPreviewSize.height;
        }


        FrameLayout.LayoutParams lparams = null;

        w = getView().getWidth();
        h = getView().getHeight();
        size.width = w;
        size.width = h;

        if(ph >0  && pw >0 )
        {
            double previewRatio = (double)ph/pw;
            double targetRatio = (double)h/w;
            double ratioDiff = Math.abs(previewRatio - targetRatio);

            if(ratioDiff <= ASPECT_TOLERANCE)
            {
                size.height = h;
            }
            else
            {
                h = (int) (w * previewRatio);
            }

        }



        LogHelper.log("mCameraPreview  lparams  w x h " + w +"x"+h);

        // lparams = new FrameLayout.LayoutParams(w, h);





        //mPreviewContainer.removeAllViews();
        //mPreviewContainer.addView(mCameraPreview, lparams);

        ViewHelper.resizeView(mCameraPreview, w,h);



    }

    //----------------------------------------------------------------------------------------------
    // initView
    //----------------------------------------------------------------------------------------------
    private void initView(View view)
    {


        mCaptureBtn = (ImageView) view.findViewById(R.id.take_picture);
        mMenuBtn = (ImageView) view.findViewById(R.id.iv_menu);
        mGalleryBtn = (ImageView) view.findViewById(R.id.iv_gallery);
        mRootView =  (ViewGroup) view.findViewById(R.id.root);
        mOverlaysContainer =  (FrameLayout) view.findViewById(R.id.overlays_container);
        mCameraOverlay = (CameraOverlay) view.findViewById(R.id.camera_overlay);
        mThumbnailView = (ImageView) view.findViewById(R.id.iv_thumbnail);
        mPreviewContainer = (FrameLayout) view.findViewById(R.id.preview_container);
        mZoomMinusView = (TextView) view.findViewById(R.id.zoom_minus);

        // mPreviewOverlay = (PreviewOverlay) view.findViewById(R.id.preview_overlay);
        mBetaBtn = (Button) view.findViewById(R.id.btn_beta);
        mFocusOverlay = (FocusOverlay) view.findViewById(R.id.focus_overlay);
        mScreenFlash = (ScreenFlash) view.findViewById(R.id.screen_flash);
        mSettingsBtn = (ImageView) view.findViewById(R.id.iv_settings);
        mCameraToggleBtn = (ImageView) view.findViewById(R.id.iv_cameraToggle);



        mViewsToRotate.add(mCaptureBtn);
        mViewsToRotate.add(mMenuBtn);
        mViewsToRotate.add(mGalleryBtn);
        // mViewsToRotate.add(mCameraOverlay);
        mViewsToRotate.add(mThumbnailView);
        mViewsToRotate.add(mZoomMinusView);
        mViewsToRotate.add(mCameraToggleBtn);
        mViewsToRotate.add(mSettingsBtn);
        mCurrentViewsOrientation = INITIAL_VIEWS_ORIENTATION;






        mMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                if(!mQMenu.isOpen())
                {

                    mQMenu.showAtView(mMenuBtn);
                }
                else
                {

                    mQMenu.close();
                }

            }
        });


        int navbarInset = CamUtils.getNavbarInset(getActivity(), mThumbnailView);
        ViewHelper.setViewMarginInRLayout(mThumbnailView, navbarInset);


        ViewHelper.enableSoftwareLayer(mGalleryBtn);
        navbarInset = CamUtils.getNavbarInset(getActivity(), mGalleryBtn);
        ViewHelper.setViewMarginInRLayout(mGalleryBtn, navbarInset);
        mGalleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                mAsyncHandler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        mGalleryManager.showGallery();
                    }
                });

            }
        });


        mThumbnailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {



                mAsyncHandler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        mGalleryManager.showLatestImage();
                    }
                });

            }
        });

        mBetaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



        navbarInset = CamUtils.getNavbarInset(getActivity(), mSettingsBtn);
        ViewHelper.setViewMarginInRLayout(mSettingsBtn, navbarInset);
        mSettingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                Intent intent ;
                if(Build.VERSION.SDK_INT >= 14)
                {
                    intent = new Intent(getActivity(), SettingsActivity.class);
                }
                else
                {
                    intent = new Intent(getActivity(), LegacyCameraSettingsActivity.class);
                }


                intent.putExtra(SettingsActivity.KEY_CAMERA_ID, mCamParams.getCameraId());
                startActivity(intent);



            }
        });


        mCameraToggleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                toggleCamera();

            }
        });




        //----------------

        Drawable icon;




        icon = CamUtils.addShadow(mContext, R.drawable.ic_action_menu);
        ViewHelper.setBackground(mContext,mMenuBtn, R.drawable.camera_btn_selector);
        mMenuBtn.setImageDrawable(icon);

        icon = CamUtils.addShadow(mContext, R.drawable.ic_action_image);
        ViewHelper.setBackground(mContext,mGalleryBtn, R.drawable.camera_btn_selector);
        mGalleryBtn.setImageDrawable(icon);

        // Settings
        icon = CamUtils.addShadow(mContext, R.drawable.ic_action_app_settings);
        ViewHelper.setBackground(mContext,mSettingsBtn, R.drawable.camera_btn_selector);
        mSettingsBtn.setImageDrawable(icon);

        // Camera toggle
        icon = CamUtils.addShadow(mContext, R.drawable.ic_action_switch_camera);
        ViewHelper.setBackground(mContext,mCameraToggleBtn, R.drawable.camera_btn_selector);
        mCameraToggleBtn.setImageDrawable(icon);

        setupCaptureButton();

        setupZoomUi(view);


        mFocusManager.setFocusOverlay(mFocusOverlay);

    }



    void switchGalleryBtnAndThumbnailBtnVisibility()
    {
        int deviceOrientation = OrientationManager.getInstance().getX90Orientation();

        CamUtils.rotateView(mGalleryBtn, mScreenOrientationAngle, deviceOrientation);
        CamUtils.rotateView(mThumbnailView, mScreenOrientationAngle, deviceOrientation);
        AnimHelper.switchViewsVisibilityByFade(mThumbnailView, mGalleryBtn);
    }




    private void setupCaptureButton()
    {
        Drawable
                icon = CamUtils.addShadow(mContext, R.drawable.ic_action_take_picture);
        ViewHelper.setBackground(mContext,mMenuBtn, R.drawable.camera_btn_selector);
        mCaptureBtn.setImageDrawable(icon);

        ViewHelper.setViewAlpha(mCaptureBtn, UiValues.TRASPARENT_VIEW_ALPHA);


        int navbarInset = CamUtils.getNavbarInset(getActivity(), mCaptureBtn);
        ViewHelper.setViewMarginInLLayout(mCaptureBtn, navbarInset);
        mCaptureBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public synchronized void onClick(View v)
            {
                if(mState == STATE_READY)
                {
                    mCaptureBtn.setEnabled(false);
                    String shotTimerPref = App.getCameraPrefHelper().getShotCountdown();

                    if (!shotTimerPref.equals(ShotCountdownValues.OFF))
                    {
                        int seconds;
                        if (shotTimerPref.equals(ShotCountdownValues._3S)) seconds = 3;
                        else seconds = 10;
                        takePicture(seconds);

                    }
                    else
                    {
                        takePicture();
                    }
                }
                else
                {
                    LogHelper.log("sate not ready");
                }


            }
        });



        mCaptureBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ViewHelper.setViewAlpha(mCaptureBtn, 1f);
                } else if (event.getAction() == MotionEvent.ACTION_UP
                        || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    ViewHelper.setViewAlpha(mCaptureBtn, UiValues.TRASPARENT_VIEW_ALPHA);
                }

                return false;
            }
        });



    }



    //----------------------------------------------------------------------------------------------
    // setupZoom
    //----------------------------------------------------------------------------------------------
    private void setupZoomUi(final View view)
    {
        mZoomSlider = (CustomVerticalSeekBar) view.findViewById(R.id.zoom_slider);
        mZoomPanel  = (LinearLayout) view.findViewById(R.id.zoom_panel);

        mZoomSlider.setMax(100);
        mZoomSlider.setThumbOffset((int) getResources().getDimension(R.dimen.camera_zoom_thumbOffset));


        ViewHelper.addOnGlobalLayoutTask(view, new Runnable() {
            @Override
            public void run() {
                ViewHelper.resizeViewHeight(mZoomSlider, view.getMeasuredHeight() / 3);
            }
        });

        mZoomSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(!isServiceAvailble()) return;
                mCamParams.setZoom(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }



    //----------------------------------------------------------------------------------------------
    // releaseMenu
    //----------------------------------------------------------------------------------------------
    private void releaseMenu()
    {
        mQMenu.release();
    }










    //----------------------------------------------------------------------------------------------
    // takePictureOnUi
    //----------------------------------------------------------------------------------------------
    private void takePictureOnUi(final int seconds)
    {
        mUiHandler.post(new Runnable() {
            @Override
            public void run() {
                takePicture(seconds);
            }
        });

    }


    //----------------------------------------------------------------------------------------------
    // takePicture
    //----------------------------------------------------------------------------------------------
    private synchronized void takePicture(int seconds)
    {
        if(seconds == 0)
        {
            takePicture();
        }
        if(!checkShotThrottle())
        {
            LogHelper.log(TAG, "shot throttled");
            return;
        }
        if(mState != STATE_READY)
        {
            LogHelper.log(TAG, "shot not allowed");
            return;
        }
        mState = STATE_SNAPSHOT;

        int deviceOrientation = OrientationManager.getInstance().getX90Orientation();
        CamUtils.rotateView(mCameraOverlay, mScreenOrientationAngle, deviceOrientation);


        mCameraOverlay.startShotCountdown(seconds, new Runnable() {
            @Override
            public void run() {
                CaptureProxy();
            }
        });

    }


    private boolean checkShotThrottle()
    {
        long time = System.currentTimeMillis() - mLastShotTimeMs;
        if(time > SHOT_THROTTLE)
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    //----------------------------------------------------------------------------------------------
    // takePicture
    //----------------------------------------------------------------------------------------------
    private synchronized  void takePicture()
    {
        if(mState != STATE_READY)
        {
            LogHelper.log(TAG, "shot not allowed");
            return;
        }
        mState = STATE_SNAPSHOT;
        CaptureProxy();
    }


    //----------------------------------------------------------------------------------------------
    // CaptureProxy
    //----------------------------------------------------------------------------------------------
    private synchronized void  CaptureProxy()
    {

        if(!isServiceAvailble()) return;


        if(mCamParams.isFrontCamera() && !mCamParams.hasFlash() &&
                App.getAdvancedCameraPrefHelper().getEnableScreenFlash())
        {
            mScreenFlash.flashOn(new Runnable()
            {
                @Override
                public void run()
                {
                    captureOnUi();
                }
            });

            return;

        }

        capture();

    }




    //----------------------------------------------------------------------------------------------
    // savePictureTask
    //----------------------------------------------------------------------------------------------
    private void savePictureTask(byte[] data, int pictureRotation)
    {

        LogHelper.log("savePictureTask orientation : " + pictureRotation);

        byte[] outputData = data;


        if(!isServiceAvailble()) return;

        String path;

        int format = mPictureFormat;
        if (format == ImageFormat.NV21 || format == ImageFormat.YUY2)
        {
            LogHelper.log(TAG, "data is yuv format : " + format);
            int w = mPictureSize.width;
            int h = mPictureSize.height;
            // Get the YuV image
            YuvImage yuv_image = new YuvImage(data, format, w, h, null);
            // Convert YuV to Jpeg
            Rect rect = new Rect(0, 0, w, h);
            ByteArrayOutputStream output_stream = new ByteArrayOutputStream();
            yuv_image.compressToJpeg(rect, 100, output_stream);
            outputData = output_stream.toByteArray();

        }




        int orientation = Exif.getOrientation(data);
        int width, height;
        if ((mCameraPreview.getJpegRotation() + orientation) % 180 == 0)
        {
            width = mPictureSize.width;
            height = mPictureSize.height;
        } else {
            width = mPictureSize.height;
            height = mPictureSize.width;
        }


        boolean hflip = App.getAdvancedCameraPrefHelper().getFlipH(mCamParams.getCameraId());
        boolean vflip = App.getAdvancedCameraPrefHelper().getFlipV(mCamParams.getCameraId());

        path = mGalleryManager.savePicture(outputData, width,height,orientation,
                hflip,vflip, mLastCaptureLocation);



        if(path != null)
        {
            if(!isServiceAvailble()) return;
            int thumbsize =(int)  getResources().getDimension(R.dimen.camera_thumbnail_size);
            Bitmap thumbImage = ImageUtils.createThumbnailFromFile(path, thumbsize, thumbsize);
            if(thumbImage != null)
            {
                Bitmap rotatedThumbImage = null;


                rotatedThumbImage = ImageUtils.rotateBitmap(thumbImage, orientation);
                if(rotatedThumbImage!= thumbImage) thumbImage.recycle();

                final Bitmap decoratedThumb  = GraphicsHelper
                        .addShadow(rotatedThumbImage, 12, 0, 0, Color.WHITE);
                rotatedThumbImage.recycle();


                mUiHandler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {

                        int deviceOrientation = OrientationManager.getInstance().getX90Orientation();
                        CamUtils.rotateView(mThumbnailView, mScreenOrientationAngle, deviceOrientation);

                        if(mGalleryBtn.getVisibility() == View.VISIBLE)
                        {
                            mThumbnailView.setImageBitmap(decoratedThumb);
                            AnimHelper.switchViewsVisibilityByFade(mGalleryBtn, mThumbnailView);

                        }
                        else
                        {
                            AnimHelper.animateImageViewChangeByFade(mThumbnailView, decoratedThumb);
                        }


                    }
                });
            }


        }



    }


    //----------------------------------------------------------------------------------------------
    // capture
    //----------------------------------------------------------------------------------------------
    public void capture()
    {
        LogHelper.log("capture");
        if (mCamParams.needsAutoFocusCapture())
        {
            mFocusManager.autoFocusAndCapture();

        }
        else
        {

            mFocusManager.startFocusSearch();
            directCapture();


        }

    }


    //----------------------------------------------------------------------------------------------
    // directCapture
    //----------------------------------------------------------------------------------------------
    public synchronized void directCapture()
    {
        LogHelper.log("directCapture");
        if (!isServiceAvailble()) return;

        boolean muted = false;
        int ringerMode=0;
        if (!App.getCameraPrefHelper().getEnableShutterSound() && !mCamParams.canDisableShutterSound())
        {
            ringerMode = SoundHelper.getMode(mContext);
            SoundHelper.setSilentMode(mContext);
            muted = true;
        }

        try
        {

            if(App.getAdvancedCameraPrefHelper().getEnableLocationRecording())
            {
                mLastCaptureLocation = mLocationManager.getCurrentLocation();
                mCamParams.setGpsParameters(mLastCaptureLocation);
            }


            mCameraPreview.takePicture();


        }
        catch(Exception ex)
        {
            LogHelper.logx(ex);
            safeCapture();
        }
        finally
        {
            if (muted)
            {
                final int finalRingerMode = ringerMode;
                mUiHandler.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        SoundHelper.setMode(mContext, finalRingerMode);
                    }
                },1000);
            }
        }



    }


    //----------------------------------------------------------------------------------------------
    // safeCapture
    //----------------------------------------------------------------------------------------------
    public void safeCapture()
    {
        if (!isServiceAvailble()) return;

        try
        {

            mCamParams.restoreDefaultParams();
            mCameraPreview.restartPreview();
            mCameraPreview.takePicture();
        }
        catch(Exception ex)
        {
            LogHelper.logx(ex);
        }
    }





    //----------------------------------------------------------------------------------------------
    // captureOnUi
    //----------------------------------------------------------------------------------------------
    public void captureOnUi()
    {
        mUiHandler.post(new Runnable()
        {
            @Override
            public void run() {
                capture();
            }
        });
    }



    //==============================================================================================
    // Implementation
    //==============================================================================================


    //==========================
    // ONKeyListener
    //==========================
    @Override
    public  boolean onKey(View v, int keyCode, KeyEvent event)
    {
        LogHelper.log(TAG, "onKey");

        if( keyCode == KeyEvent.KEYCODE_VOLUME_UP ||
                keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)
        {
            String activatorPref = App.getActivatorsPrefHelper().getVolumeButtonsActivator();
            LogHelper.log("activatorPref : " + activatorPref) ;
            if(!activatorPref.equals(VolumeButtonsActivatorValues.DISABLED))
            {
                onVolumeButtonActivatorEvent(keyCode, event);
                return true;
            }

        }


        return false;
    }



    //======================================
    // CameraPreview.CameraPreviewListener
    //======================================


    @Override
    public void onCameraOpened()
    {



    }

    @Override
    public void onCameraError(Exception ex, int error)
    {

        if(error == CameraPreview.ERROR_OPEN_CAMERA_FAILED)
        {
            if(mCamParams.getCameraId() == 0 )
            {
                mUiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        toggleCamera();
                    }
                });
                return;
            } else {
                MsgHelper.toastSlow(mContext, R.string.alert_error_cameraConnectionFailed);
            }


        }
        else if(error == CameraPreview.ERROR_PREVIEW_FAILED)
        {
            if(!mCamParams.isDefaultParamsUsed())
            {
                mCamParams.restoreDefaultParams();
                mCameraPreview.restartPreview();
            }
        }

    }



    @Override
    public void onPictureTaken(final byte[] data , final int pictureRotation)
    {

        //mCamParams.unlockAeAndWb();
        mAsyncHandler.post(new Runnable()
        {
            @Override
            public void run()
            {
                savePictureTask(data, pictureRotation);
            }
        });

        mUiHandler.post(new Runnable()
        {
            @Override
            public  void run()
            {
                onPictureTakenUiTask();
            }
        });


        mLastShotTimeMs = System.currentTimeMillis();

    }


    @Override
    public void onPreviewFrame(byte[] data)
    {

    }




    @Override
    public void onShutter()
    {
        //mFocusManager.onFocusEnd();
    }

    @Override
    public void onPreviewReadyForStart()
    {

        LogHelper.log("onPreviewReadyForStart");
        if(!mCameraPreview.isPreviewRunning())
        {
            mCameraPreview.startPreview();
        }
    }


    @Override
    public void onPrePreviewStart()
    {
        if(!isServiceAvailble()) return;

        try
        {

            mCamParams.setCamera(mCameraPreview.getCameraId(), mCameraPreview.getCamera());
            setupCameraParams();
            mScreenFlash.setup(getActivity());
            setupMenu();
            resizeCameraPreview();
            if(!mQMenu.isOpen()) mQMenu.showAtView(mMenuBtn);

        }
        catch(Exception ex)
        {
            LogHelper.logx(ex);
        }


    }


    @Override
    public void onPreviewStarted()
    {
        if(mState != STATE_STARTING &&
                mState != STATE_SWITCHING)  return;


        setupFocusManager();

        if(App.getUiPrefHelper().getShowZoomUi())
        {
            if(mCamParams.hasZoom())
            {
                mZoomPanel.setVisibility(View.VISIBLE);
            }
            else
            {
                mZoomPanel.setVisibility(View.GONE);
            }
        }
        else
        {
            mZoomPanel.setVisibility(View.GONE);
        }


        mPictureSize = mCamParams.getPictureSize();
        mPictureFormat = mCamParams.getPictureFormat();


        mUiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {resetLocks();}
        }, 500);


    }



    @Override
    public void onFaceDetection(List<Rect> faces)
    {

    }


    //======================================
    // MenuManager.ActionListener
    //======================================
    @Override
    public void onActionClick(Action action, View view)
    {
        LogHelper.log("onActionClick : ");

        if(action.getId() == CameraMenu.QMenu.ACTION_CAMERA_VIEW.val)
        {
            toggleCamera();


        }
        else if(action.getId() == CameraMenu.QMenu.ACTION_SETTINGS.val)
        {
           /* Intent intent ;
            intent = new Intent(getActivity(), SettingsActivity.class);

            intent.putExtra(SettingsActivity.KEY_CAMERA_ID, mCamParams.getCameraId());
            startActivity(intent);*/
        }
        else if(action.getId() == CameraMenu.QMenu.ACTION_ABOUT.val)
        {
            Intent intent = new Intent(getActivity(), FragmentWrapperActivity.class);
            intent.putExtra(FragmentWrapperActivity.KEY_FRAGMENT, FragmentWrapperActivity.FRAGMENT_ABOUT);
            startActivity(intent);
        }
        else if(action.getId() == CameraMenu.QMenu.ACTION_APPS.val)
        {
            showDevApp();
        }
        else if(action.getId() == CameraMenu.QMenu.ACTION_QUALITY_CAMERA.val)
        {
            MarketHelper.showProductPage(mContext, "com.tafayor.qualitycamera");
        }



    }

    private void showDevApp()
    {
        try
        {
            List<String> apps = new ArrayList<>();

            apps.add("com.tafayor.screenshield");
            apps.add("com.tafayor.displaced");
            apps.add("com.tafayor.lockeye");
            apps.add("com.tafayor.rapidos");
            apps.add("com.tafayor.tiltscroll.free");
            apps.add("com.tafayor.volkeyscroll");
            apps.add("com.tafayor.opened");
            apps.add("com.tafayor.smartscrolltools");
            apps.add("com.tafayor.alcomra");
            apps.add("com.tafayor.digitcontrol");
            apps.add("com.tafayor.carpus");
            apps.add("com.tafayor.qualitycamera");

            boolean shown = false;

            for(String app: apps)
            {
                if(!App.getPrefHelper().getDevAppShown(app))
                {
                    MarketHelper.showProductPage(mContext, app);
                    App.getPrefHelper().setDevAppShown(app, true);
                    shown = true;
                    break;
                }
            }

            if(!shown)
            {
                for(String app: apps) App.getPrefHelper().setDevAppShown(app, false);
                showDevApp();
            }
        }
        catch(Exception e)
        {
            LogHelper.logx(e);
        }



    }

    @Override
    public void onActionSelected(Action parent, Action action)
    {
        LogHelper.log("onActionSelected");


        if(parent.getId() == CameraMenu.QMenu.ACTION_FLASH_MODE.val)
        {
            String flashMode = (String) action.getValue();
            LogHelper.log("flash mode changed : " + flashMode);
            mCamParams.setFlashMode(flashMode);
            App.getCameraPrefHelper().setFlashMode(PrefValsProxy.wrapFlashMode(flashMode));
        }
        else if(parent.getId() == CameraMenu.QMenu.ACTION_WHITE_BALANCE.val)
        {
            String wbMode = (String) action.getValue();
            LogHelper.log("white balance changed : " + wbMode);
            mCamParams.setWhiteBalance(wbMode);
            App.getCameraPrefHelper().setFlashMode(PrefValsProxy.wrapWhiteBalance(wbMode));
        }
        else if(parent.getId() == CameraMenu.QMenu.ACTION_SHOT_COUNTDOWN.val)
        {
            String timer = (String) action.getValue();
            LogHelper.log("countdown changed : " + timer);
            App.getCameraPrefHelper().setShotCountdown(timer);
        }
        else if(parent.getId() == CameraMenu.QMenu.ACTION_EXPOSURE_COMPENSATION.val)
        {
            float exposure = (float) action.getValue();
            LogHelper.log("exposure changed : " + exposure);

            mCamParams.setExposure(exposure);
            App.getCameraPrefHelper().setExposure(mCamParams.getCameraId(), exposure);
        }
        else if(parent.getId() == CameraMenu.QMenu.ACTION_REMOTEMODE.val)
        {
            String mode =  (String) action.getValue();
            App.getRemoteControlPrefHelper().setRemoteMode(mode);
            mRemoteControl.release();
            setupRemoteControl();
        }

    }

    @Override
    public void onActionViewCreated(Action action, View view)
    {
        LogHelper.log("onActionViewCreated : " + action.getId());

        int deviceOrientation = OrientationManager.getInstance().getX90Orientation();
        CamUtils.rotateView(view, mScreenOrientationAngle, deviceOrientation);
    }





    //======================================
    // SoundControlListener
    //======================================

    @Override
    public void onWhistleDetected()
    {
        LogHelper.log(TAG, "onWhistleDetected");
        int delay = App.getRemoteControlPrefHelper().getRemoteModeDelay();
        takePictureOnUi(delay);
    }

    @Override
    public  void onClappingDetected()
    {

        if(App.isPro())
        {
            int delay = App.getRemoteControlPrefHelper().getRemoteModeDelay();
            takePictureOnUi(delay);
        }
        else
        {

            if( mState != STATE_READY || mClappingProDialog.isShowing()) return;
            mState = STATE_BUSY;
            mUiHandler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    SoundHelper.beep(mContext);
                    mClappingProDialog.show();
                    mState = STATE_READY;
                }
            });

        }


    }


    @Override
    public void onException()
    {
        MsgHelper.toastLong(mContext,getResources().getString(R.string.alert_error_micFailed));
        mRemoteControl.release();
    }




    //==========================
    // OrientationListener
    //==========================
    class OrientationListenerImpl extends OrientationManager.OrientationListener
    {
        @Override
        public void onX90OrientationChanged(int from, int to)
        {

            LogHelper.log(TAG, "onX90OrientationChanged : " + from + " > " + to);

            List<View> menuViews =  mQMenu.getDisplayedViews();
            for(View view : menuViews)
            {

                CamUtils.animateViewRotation(view, mScreenOrientationAngle, to);

            }

            for(View view : mViewsToRotate)
            {
                if(view.getVisibility() == View.VISIBLE)
                {
                    CamUtils.animateViewRotation(view, mScreenOrientationAngle, to);
                }

            }

        }
    }


    //======================================
    // FocusManager.Listener
    //======================================
    class FocusListener implements FocusManager.Listener
    {
        public FocusListener()
        {
            super();
        }


        @Override
        public synchronized  boolean onTouchToFocusStarted()
        {
            if(mState == STATE_READY)
            {
                mState = STATE_TOUCH_TO_FOCUS;
                return true;
            }
            else return false;
        }

        @Override
        public synchronized void onTouchToFocusEnded()
        {
            if(mState == STATE_TOUCH_TO_FOCUS) mState = STATE_READY;
        }


        @Override
        public synchronized void onTouchToFocusCanceled()
        {
            if(mState == STATE_TOUCH_TO_FOCUS) mState = STATE_READY;
        }

        @Override
        public void capture()
        {
            directCapture();
        }


        @Override
        public void safeCapture()
        {
            if(!isServiceAvailble()) return;
            CameraFragment.this.safeCapture();

        }

        @Override
        public List<Rect> getDetectedFaces() {
            return null;
        }

    }


    //======================================
    // CameraPreview.TouchGestureListener
    //======================================
    class PreviewTouchGestureListener extends OnGestureTouchListener
    {


        public PreviewTouchGestureListener(Context ctx)
        {
            super(ctx);
        }


        @Override
        public void onTrackTop(int distance) {

        }

        @Override
        public void onTrackBottom(int distance) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e)
        {
            //LogHelper.log("onSingleTapUp");
            return mFocusManager.onSingleTapUp(e);
        }

        @Override
        public void onSwipeLeft(int distance)
        {
            // LogHelper.log("onSwipeLeft : " + distance);

            mGalleryManager.showLatestImage();
        }

        @Override
        public void onSwipeRight(int distance)
        {
            //LogHelper.log("onSwipeRight : " + distance);
            if(!mQMenu.isOpen()) mQMenu.showAtView(mMenuBtn);
        }


    }



    //==========================
    // LocationManager.Listener
    //=========================
    @Override
    public void showGpsOnScreenIndicator(boolean hasSignal)
    {

    }

    @Override
    public void hideGpsOnScreenIndicator()
    {

    }








    //==============================================================================================
    // Types
    //==============================================================================================




}
