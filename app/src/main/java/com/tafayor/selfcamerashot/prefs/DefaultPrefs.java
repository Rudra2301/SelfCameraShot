package com.tafayor.selfcamerashot.prefs;

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


import com.tafayor.selfcamerashot.taflib.types.Size;






    public interface DefaultPrefs
    {

        public static int CAMERA_VIEW = 0;
        public static String FLASH_MODE = FlashModeValues.OFF;
        public static boolean ENABLE_SHUTTER_SOUND = true;
        public static String WHITE_BALANCE = WhiteBalanceValues.AUTO;
        public static String SHOT_COUNTDOWN = ShotCountdownValues.OFF;
        public static Size PICTURE_SIZE = new Size(0,0);
        public static Size PREVIEW_SIZE = new Size(0,0);
        public static Size PREVIEW_SIZE_UNSET = new Size(0,0);
        public static String REMOTE_MODE = RemoteModeValues.WHISTLE;
        public static int REMOTE_MODE_DELAY = 1;
        public static int WHISTLE_SENSITIVITY = 70;
        public static int  CLAPPING_SENSITIVITY = 95;
        public static String FOCUS_MODE = FocusModeValues.UNSET;
        public static float EXPOSURE_COMPENSATION = 0;
        public static String SCENE_MODE = SceneModeValues.UNSET;
        public static String ISO = IsoValues.UNSET;
        public static boolean ENABLE_TOUCH_TO_fOCUS = true;
        public static boolean ENABLE_SCREEN_FLASH = false;
        public static String ACTIVATOR_VOLUME_BUTTONS = VolumeButtonsActivatorValues.DISABLED;
        public static boolean SHOW_ZOOM_UI = true;
        public static boolean  ENABLE_LOCATION_RECORDING = false;
        public static int CAM_FIRST_ID = 0;
        public static boolean KEEP_MENU_OPEN = true;
        public static boolean  ENABLE_CUSTOM_STORAGE = false;
        public static boolean  ENABLE_FULLSCREEN = false;
        public static boolean  FLIP_H = false;
        public static boolean  FLIP_V = false;


    }


