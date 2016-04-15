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

import android.graphics.drawable.Drawable;


import com.tafayor.selfcamerashot.taflib.types.IndexedHashMap;

import java.util.ArrayList;


public class Action
{

    public static String TAG = Action.class.getSimpleName();


    protected IndexedHashMap<Integer, Action> mSubActions;
    private int mId;
    private Drawable mIcon;
    private int mExpandDirection;
    private int mOrientation;
    private Action mParent = null;
    private MenuManager mMenuManager;
    private int mSelectedAction;
    private int mDisplayMode;
    private int mMode;
    private Object mValue;







    public Action(int id, Drawable icon )
    {
        mId = id;
        mIcon = icon;

        init();
    }




    private void init()
    {
        mSubActions = new IndexedHashMap<>();
        loadDefaults();

    }


    void loadDefaults()
    {
        mSelectedAction = -1;
        mDisplayMode = MenuManager.ActionDisplayModes.LIST;
        mMode = MenuManager.ActionModes.TASK;
    }

    //==============================================================================================
    // Interface
    //==============================================================================================

    public void setMenuManager(MenuManager manager)
    {
        mMenuManager = manager;
    }



    //----------------------------------------------------------------------------------------------
    // release
    //----------------------------------------------------------------------------------------------
    public void release()
    {
        if(mIcon != null)
        {
            mIcon.setCallback(null);
            mIcon = null;
        }

        mParent = null;
        mValue = null;


        if(hasSubActions())
        {
            for(Action action : mSubActions.values())
            {
                action.release();
            }

            mSubActions.clear();
        }



    }


    //----------------------------------------------------------------------------------------------
    // isSelected
    //----------------------------------------------------------------------------------------------
    public boolean isSelected()
    {
        return (hasParent() &&
                getParent().getSelectedAction() != null &&
                getParent().getSelectedAction().getId() == getId());

    }



    //----------------------------------------------------------------------------------------------
    // hasParent
    //----------------------------------------------------------------------------------------------
    public boolean hasParent()
    {
        return (mParent != null);
    }


    //----------------------------------------------------------------------------------------------
    // setParent
    //----------------------------------------------------------------------------------------------
    public void setParent(Action parent)
    {
        mParent = parent;
    }


    //----------------------------------------------------------------------------------------------
    // getParent
    //----------------------------------------------------------------------------------------------
    public Action getParent()
    {
        return mParent;
    }


    //----------------------------------------------------------------------------------------------
    // isRoot
    //----------------------------------------------------------------------------------------------
    public boolean isRoot()
    {
        return (mParent == null);
    }


    //----------------------------------------------------------------------------------------------
    // getId
    //----------------------------------------------------------------------------------------------
    public int getId()
    {
        return mId;
    }


    //----------------------------------------------------------------------------------------------
    // getIcon
    //----------------------------------------------------------------------------------------------
    public Drawable getIcon()
    {
        return mIcon;
    }


    //----------------------------------------------------------------------------------------------
    // getDisplayedIcon
    //----------------------------------------------------------------------------------------------
    public Drawable getDisplayedIcon()
    {

        if(hasSubActions() && getMode() == MenuManager.ActionModes.SELECTION)
        {
            return getSelectedAction().getIcon();
        }
        else
        {
            return mIcon;
        }
    }


    //----------------------------------------------------------------------------------------------
    // setIcon
    //----------------------------------------------------------------------------------------------
    public void setIcon(Drawable drawable)
    {
       mIcon = drawable;
    }





    //----------------------------------------------------------------------------------------------
    // addAction
    //----------------------------------------------------------------------------------------------
    public void addAction(Action action)
    {
        action.setParent(this);
        action.setMenuManager(mMenuManager);

        mSubActions.put(action.getId(), action);

    }


    //----------------------------------------------------------------------------------------------
    // getAction
    //----------------------------------------------------------------------------------------------
    public Action getAction(int id)
    {
        return mSubActions.get(id);
    }


    //----------------------------------------------------------------------------------------------
    // removeAction
    //----------------------------------------------------------------------------------------------
    public Action removeAction(int id)
    {
        mSubActions.get(id).setParent(null);
        return mSubActions.remove(id);
    }


    //----------------------------------------------------------------------------------------------
    // removeAllAcions
    //----------------------------------------------------------------------------------------------
    public void removeAllAcions()
    {
         mSubActions.clear();
    }


    //----------------------------------------------------------------------------------------------
    // getSubActions
    //----------------------------------------------------------------------------------------------
    public ArrayList<Action> getSubActions()
    {
        return new ArrayList<Action>(mSubActions.values());
    }



    //----------------------------------------------------------------------------------------------
    // hasChild
    //----------------------------------------------------------------------------------------------
    public boolean containsSubAction(int id)
    {
        return  mSubActions.containsKey(id);
    }

    //----------------------------------------------------------------------------------------------
    // hasSubActions
    //----------------------------------------------------------------------------------------------
    public boolean hasSubActions()
    {
        return mSubActions.size()>0;
    }


    //----------------------------------------------------------------------------------------------
    // setOrientation
    //----------------------------------------------------------------------------------------------
    public void setOrientation(int orientation)
    {
        mOrientation = orientation;
    }


    //----------------------------------------------------------------------------------------------
    // getOrientation
    //----------------------------------------------------------------------------------------------
    public int getOrientation( )
    {
        return mOrientation;
    }


    //----------------------------------------------------------------------------------------------
    // setExpandDirection
    //----------------------------------------------------------------------------------------------
    public void setExpandDirection(int direction)
    {
        mExpandDirection = direction;
    }


    //----------------------------------------------------------------------------------------------
    // getExpandDirection
    //----------------------------------------------------------------------------------------------
    public int getExpandDirection( )
    {
        return mExpandDirection;
    }


    //----------------------------------------------------------------------------------------------
    // setSelectedAction
    //----------------------------------------------------------------------------------------------
    public void setSelectedAction(Action action)
    {
        setSelectedAction(action.getId());
    }


    //----------------------------------------------------------------------------------------------
    // setSelectedAction
    //----------------------------------------------------------------------------------------------
    public void setSelectedAction(int id)
    {
        mSelectedAction = id;
    }


    //----------------------------------------------------------------------------------------------
    // getSelectedAction
    //----------------------------------------------------------------------------------------------
    public Action getSelectedAction()
    {
        if(hasSubActions())
        {
            if(mSelectedAction == -1) mSelectedAction = mSubActions.getKey(0);
            return mSubActions.getByKey(mSelectedAction);
        }

        return null;
    }


    //----------------------------------------------------------------------------------------------
    // setDisplayMode
    //----------------------------------------------------------------------------------------------
    public void setDisplayMode(int mode)
    {
        mDisplayMode = mode;
    }

    //----------------------------------------------------------------------------------------------
    // getDisplayMode
    //----------------------------------------------------------------------------------------------
    public int getDisplayMode()
    {
        return mDisplayMode;
    }


    //----------------------------------------------------------------------------------------------
    // setMode
    //----------------------------------------------------------------------------------------------
    public void setMode(int mode)
    {
        mMode = mode;
    }

    //----------------------------------------------------------------------------------------------
    // getMode
    //----------------------------------------------------------------------------------------------
    public int getMode()
    {
        return mMode;
    }



    //----------------------------------------------------------------------------------------------
    // selectNextAction
    //----------------------------------------------------------------------------------------------
    public void selectNextAction()
    {

        int curIndex = mSubActions.getKeyIndex(mSelectedAction);
        int nextIndex = curIndex + 1;
        if(nextIndex  >= mSubActions.size())  nextIndex = 0;

        mSelectedAction = mSubActions.getKey(nextIndex);


    }


    //----------------------------------------------------------------------------------------------
    // selectPreviousAction
    //----------------------------------------------------------------------------------------------
    public void selectPreviousAction()
    {
        int curIndex = mSubActions.getKeyIndex(mSelectedAction);
        int prevIndex = curIndex - 1;
        if(prevIndex  < 0)  prevIndex = mSubActions.size()-1;

        mSelectedAction =  mSubActions.getKey(prevIndex);
    }



    //----------------------------------------------------------------------------------------------
    // setValue
    //----------------------------------------------------------------------------------------------
    public void setValue(Object value)
    {
        mValue = value;
    }

    //----------------------------------------------------------------------------------------------
    // getValue
    //----------------------------------------------------------------------------------------------
    public Object getValue()
    {
        return mValue;
    }


    //==============================================================================================
    // Internals
    //==============================================================================================


    protected MenuManager menuManager()
    {
        return mMenuManager;
    }




}
