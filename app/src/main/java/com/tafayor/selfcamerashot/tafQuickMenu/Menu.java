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


package com.tafayor.selfcamerashot.tafQuickMenu;

import android.app.Activity;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;



import java.util.List;


public class Menu extends Action
{
    public static String TAG = Menu.class.getSimpleName();

    MenuManager mManager;


    private Menu(int id, Drawable drawable)
    {
        super(MenuManager.ROOT_ID, null);
    }


    public Menu(Activity activity)
    {
        super(MenuManager.ROOT_ID, null);
        mManager = new MenuManager(activity);
        setMode(MenuManager.ActionModes.TASK);
    }






    //----------------------------------------------------------------------------------------------
    //setActionNormalBackground
    //----------------------------------------------------------------------------------------------
    public void setActionNormalBackground(Drawable bg)
    {
        mManager.setActionNormalBackground(bg);
    }



    //----------------------------------------------------------------------------------------------
    //setActionSelectedBackground
    //----------------------------------------------------------------------------------------------
    public void setActionSelectedBackground(Drawable bg)
    {
        mManager.setActionSelectedBackground(bg);
    }


    //----------------------------------------------------------------------------------------------
    // isOpen
    //----------------------------------------------------------------------------------------------
    public boolean isOpen()
    {
        return mManager.isActionDialogShown(this);
    }




    //----------------------------------------------------------------------------------------------
    // getDisplayedViews
    //----------------------------------------------------------------------------------------------
    public List<View> getDisplayedViews()
    {
        return mManager.getDisplayedViews();
    }



    //----------------------------------------------------------------------------------------------
    // close
    //----------------------------------------------------------------------------------------------
    public void close()
    {
        mManager.closeAll();
    }


    //----------------------------------------------------------------------------------------------
    // release
    //----------------------------------------------------------------------------------------------
    public void release()
    {
        mManager.release();
        loadDefaults();
        super.release();

    }



    //----------------------------------------------------------------------------------------------
    // showAtView
    //----------------------------------------------------------------------------------------------
    public void showAtView(View view)
    {

        mManager.showMenu(this, view);

    }


    //----------------------------------------------------------------------------------------------
    // setActionWidth
    //----------------------------------------------------------------------------------------------
    public void setActionWidth(int width)
    {
        mManager.setActionWidth(width);
    }


    //----------------------------------------------------------------------------------------------
    // setActionHeight
    //----------------------------------------------------------------------------------------------
    public void setActionHeight(int height)
    {
        mManager.setActionHeight(height);
    }


    //----------------------------------------------------------------------------------------------
    // setActionsSpacing
    //----------------------------------------------------------------------------------------------
    public void setActionsSpacing(int spacing)
    {
        mManager.setActionsSpacing(spacing);
    }


    //----------------------------------------------------------------------------------------------
    // setBackground
    //----------------------------------------------------------------------------------------------
    public void setBackground(Drawable bg)
    {
        mManager.setMenuBackground(bg);
    }


    //----------------------------------------------------------------------------------------------
    // setOnActionClickListener
    //----------------------------------------------------------------------------------------------
    public void setActionListener(MenuManager.ActionListener listener)
    {
        mManager.setActionListener(listener);
    }


    //----------------------------------------------------------------------------------------------
    // findAction
    //----------------------------------------------------------------------------------------------
    public Action findAction(int id)
    {

        for(Action action : getSubActions())
        {
            if(action.getId() == id) return action;
            else return findAction(id);
        }

        return null;
    }


    //----------------------------------------------------------------------------------------------
    // setRootShift
    //----------------------------------------------------------------------------------------------
    void setRootShift(int shift)
    {
        mManager.setRootShift(shift);
    }


    //----------------------------------------------------------------------------------------------
    // setBranchShift
    //----------------------------------------------------------------------------------------------
    void setBranchShift(int shift)
    {
        mManager.setRootShift(shift);
    }
}
