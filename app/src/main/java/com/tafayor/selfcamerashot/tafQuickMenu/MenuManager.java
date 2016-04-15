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
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;


import com.tafayor.selfcamerashot.R;
import com.tafayor.selfcamerashot.taflib.helpers.DisplayHelper;
import com.tafayor.selfcamerashot.taflib.helpers.LogHelper;
import com.tafayor.selfcamerashot.taflib.helpers.ViewHelper;
import com.tafayor.selfcamerashot.taflib.types.Size;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class MenuManager
{

    public static String TAG  = MenuManager.class.getSimpleName();


    private ConcurrentHashMap<Integer, ActionInfo> mActionMap;
    private WeakReference<Activity>  mActivityPtr;
    private Context mContext;
    private  int mActionWidth, mActionHeight;
    private int mActionsSpacing;
    private Drawable mMenuBackground;
    private WeakReference<ActionListener> mActionListenerPtr = null;
    private int mRootShift;
    private int mBranchShift;
    private long mLastPopupDismissEventTime;
    private long mLastDismissedPopupId;
    private boolean mIgnoreActionClick;
    private Drawable mActionNormalBackground;
    private Drawable mActionSelectedBackground;






    public MenuManager(Activity activity)
    {
        mActivityPtr = new WeakReference<Activity>(activity);
        mContext = activity.getApplicationContext();
        mActionMap = new ConcurrentHashMap<>();

        loadDefaults();
    }


    private void loadDefaults()
    {
        mActionWidth = (int) (50 * DisplayHelper.getDensity(mContext));
        mActionHeight = (int) (50 * DisplayHelper.getDensity(mContext));
        mActionsSpacing = (int) (7 * DisplayHelper.getDensity(mContext));
        mMenuBackground = new ColorDrawable(Color.TRANSPARENT);
        mRootShift= (int) (14 * DisplayHelper.getDensity(mContext));
        mBranchShift = (int) (5 * DisplayHelper.getDensity(mContext));

    }





    //==============================================================================================
    // Interface
    //==============================================================================================


    //----------------------------------------------------------------------------------------------
    //release
    //----------------------------------------------------------------------------------------------
    public void release()
    {
        closeAll();
        if(mActionNormalBackground != null)
        {
            mActionNormalBackground.setCallback(null);
            mActionNormalBackground = null;
        }
        if(mActionSelectedBackground != null)
        {
            mActionSelectedBackground.setCallback(null);
            mActionSelectedBackground = null;
        }


        mActionMap.clear();
        loadDefaults();

    }




    //----------------------------------------------------------------------------------------------
    //setActionNormalBackground
    //----------------------------------------------------------------------------------------------
    public void setActionNormalBackground(Drawable bg)
    {
        mActionNormalBackground = bg;
    }



    //----------------------------------------------------------------------------------------------
    //setActionNormalBackground
    //----------------------------------------------------------------------------------------------
    public void setActionSelectedBackground(Drawable bg)
    {
        mActionSelectedBackground = bg;
    }



    //----------------------------------------------------------------------------------------------
    //getDisplayedViews
    //----------------------------------------------------------------------------------------------
    public synchronized List<View> getDisplayedViews()
    {
        List<View> views = new ArrayList<>();
        Map.Entry<Integer, ActionInfo>   entry;
        Iterator<Map.Entry<Integer,ActionInfo>> it;

        it = mActionMap.entrySet().iterator();
        while (it.hasNext())
        {
            entry = it.next();
            int id = entry.getKey();
            if(id != ROOT_ID)
            {
                View view = entry.getValue().view;
                views.add(view);
            }


        }

        return views;
    }


    //----------------------------------------------------------------------------------------------
    //closeAll
    //----------------------------------------------------------------------------------------------
    public  void closeAll()
    {
        closeAllActions();
    }

    //----------------------------------------------------------------------------------------------
    //closeAllActions
    //----------------------------------------------------------------------------------------------
    public synchronized void closeAllActions()
    {

        Map.Entry<Integer, ActionInfo>   entry;
        Iterator<Map.Entry<Integer,ActionInfo>> it;

        it = mActionMap.entrySet().iterator();
        while (it.hasNext())
        {
            entry = it.next();
            int id = entry.getKey();
            PopupWindow popup = entry.getValue().popup;
            if(popup != null && popup.isShowing())  popup.dismiss();
            it.remove();

        }

    }



    //----------------------------------------------------------------------------------------------
    // closeSiblings
    //----------------------------------------------------------------------------------------------
    public void showMenu(Action action, View view)
    {
        LogHelper.log(TAG, "showMenu");
        Point pos = new Point();

        Rect bounds = ViewHelper.getBoundsOnScreen(view);



        pos.x = bounds.left;
        pos.y =  bounds.bottom;

        if(action.getExpandDirection() == ExpandDirections.LEFT)
        {
            pos.x =  bounds.left;
            pos.y =  bounds.top+bounds.height()/2;
        }
        else if(action.getExpandDirection() == ExpandDirections.UP)
        {
            pos.x =  bounds.left+ bounds.width()/2;
            pos.y =  bounds.top;

        }
        else if(action.getExpandDirection() == ExpandDirections.RIGHT)
        {
            pos.x =  bounds.right;
            pos.y =  bounds.top+bounds.height()/2;

        }
        else if(action.getExpandDirection() == ExpandDirections.DOWN)
        {
            pos.x =  bounds.left + bounds.width()/2;
            pos.y =  bounds.bottom;
        }

        LogHelper.log(TAG, "showMenu" + "pos : " + pos.x + " x " + pos.y);

        showMenu(action, pos);

    }

    //----------------------------------------------------------------------------------------------
    // showMenu
    //----------------------------------------------------------------------------------------------
    public void showMenu(Action action, Point pos)
    {
        showActionDialog(action, pos);
    }


    //----------------------------------------------------------------------------------------------
    // setActionWidth
    //----------------------------------------------------------------------------------------------
    void setActionWidth(int width)
    {
        mActionWidth = width;
    }

    //----------------------------------------------------------------------------------------------
    // setActionHeight
    //----------------------------------------------------------------------------------------------
    void setActionHeight(int height)
    {
        mActionHeight = height;
    }

    //----------------------------------------------------------------------------------------------
    // setActionsSpacing
    //----------------------------------------------------------------------------------------------
    void setActionsSpacing(int spacing)
    {
        mActionsSpacing = spacing;
    }

    //----------------------------------------------------------------------------------------------
    // setMenuBackground
    //----------------------------------------------------------------------------------------------
    void setMenuBackground(Drawable bg)
    {
        mMenuBackground = bg;
    }

    //----------------------------------------------------------------------------------------------
    // setOnActionClickListener
    //----------------------------------------------------------------------------------------------
    void setActionListener(ActionListener listener)
    {
        mActionListenerPtr = new WeakReference<>(listener);
    }


    //----------------------------------------------------------------------------------------------
    // setRootShift
    //----------------------------------------------------------------------------------------------
    void setRootShift(int shift)
    {
        mRootShift = shift;
    }


    //----------------------------------------------------------------------------------------------
    // setBranchShift
    //----------------------------------------------------------------------------------------------
    void setBranchShift(int shift)
    {
        mBranchShift = shift;
    }



    //==============================================================================================
    // Callbacks
    //==============================================================================================


    //----------------------------------------------------------------------------------------------
    //onActionClickListener
    //----------------------------------------------------------------------------------------------
    private void onActionClickListener(Action action, View view)
    {
        ActionListener listener = mActionListenerPtr.get();
        Action parent = action.getParent();




        if(action.hasSubActions())
        {

            if(isActionDialogShown(action))
            {

                closeAction(action);
            }
            else
            {
                if(action.getDisplayMode() == ActionDisplayModes.LIST ||
                        action.getDisplayMode() == ActionDisplayModes.LIST_SINGLE_SELECTION)
                {
                    showMenu(action,view);
                }
                else if(action.getDisplayMode() == ActionDisplayModes.SINGLE)
                {
                    switchSelectedAction(action);
                    if(listener != null) listener.onActionSelected(action, action.getSelectedAction());
                }

            }


        }
        else
        {



            closeAction(parent);



            if(parent.getMode() == ActionModes.SELECTION)
            {

                selectAction(action);
                if(listener != null) listener.onActionSelected(parent, action);
            }


        }

        if(listener != null)
        {
            listener.onActionClick(action, view);
        }


    }



    //----------------------------------------------------------------------------------------------
    //onPopupDismissed
    //----------------------------------------------------------------------------------------------
    private void onPopupDismissed(int id)
    {

        closeAction(id);
    }


    //==============================================================================================
    // Internals
    //==============================================================================================


    //----------------------------------------------------------------------------------------------
    // selectNextAction
    //----------------------------------------------------------------------------------------------
    private void switchSelectedAction(Action parent)
    {


        parent.selectNextAction();
        if(parent.getMode() == ActionModes.SELECTION)
        {
            setupActionView(parent);
        }

    }

    //----------------------------------------------------------------------------------------------
    // selectAction
    //----------------------------------------------------------------------------------------------
    private void selectAction(Action subAction)
    {

        Action parent = subAction.getParent();
        parent.setSelectedAction(subAction);


        if(parent.getDisplayMode() == ActionDisplayModes.LIST_SINGLE_SELECTION)
        {
            deselectSubActions(parent);
            if(mActionMap.containsKey(subAction.getId()))
            {
                ActionInfo info = mActionMap.get(subAction.getId());
                ViewHelper.setBackground(info.view, mActionSelectedBackground);

            }

        }
        else
        {
            setupActionView(parent);
        }

    }


    //----------------------------------------------------------------------------------------------
    // deselectSubActions
    //----------------------------------------------------------------------------------------------
    private void deselectSubActions(Action parent)
    {

        for(Action child : parent.getSubActions())
        {
            if(mActionMap.containsKey(child.getId()))
            {
                ActionInfo info = mActionMap.get(child.getId());
                ViewHelper.setBackground(info.view,
                        mActionNormalBackground.getConstantState().newDrawable());
            }
        }

    }

    //----------------------------------------------------------------------------------------------
    //showActionDialog
    //----------------------------------------------------------------------------------------------
    private  synchronized void showActionDialog (Action action, Point pos)
    {
        int animationStyle;
        Point newPos = new Point(pos);
        View view;



        try
        {
            if(isActionDialogShown(action))
            {
                LogHelper.log(TAG, "popup for this id is already shown");
            }

            view = buildActionView(action);

            Size viewSize = ViewHelper.measure(view);

            if(action.getExpandDirection() == ExpandDirections.UP)
            {
                newPos.y = pos.y - viewSize.height;
            }
            else if(action.getExpandDirection() == ExpandDirections.LEFT)
            {
                newPos.x = pos.x - viewSize.width;

            }

            animationStyle= R.style.TqmUpwardPopupAnimation;

            if(action.getExpandDirection() == ExpandDirections.LEFT)
            {
                animationStyle= R.style.TqmLeftwardPopupAnimation;
            }
            else if(action.getExpandDirection() == ExpandDirections.UP)
            {
                animationStyle= R.style.TqmUpwardPopupAnimation;
            }
            else if(action.getExpandDirection() == ExpandDirections.RIGHT)
            {
                animationStyle= R.style.TqmRightwardPopupAnimation;
            }
            else if(action.getExpandDirection() == ExpandDirections.DOWN)
            {
                animationStyle= R.style.TqmDownwardPopupAnimation;
            }



            if(action.getOrientation() == Orientations.VERTICAL)
            {
                newPos.x -= viewSize.width/2;
            }
            else if(action.getOrientation() == Orientations.HORIZONTAL)
            {
                newPos.y-= viewSize.height/2;
            }



            showPopup(action.getId(), view, newPos, animationStyle);

        }
        catch(Exception ex)
        {
            LogHelper.logx(TAG, ex);
            return;
        }



    }





    //----------------------------------------------------------------------------------------------
    // isActionDialogShown
    //----------------------------------------------------------------------------------------------
    public synchronized boolean isActionDialogShown(Action action)
    {
        ActionInfo info = mActionMap.get(action.getId());
        if(info != null && info.popup != null)
        {
            return true;
        }
        else
        {
            return false;
        }
    }




    //----------------------------------------------------------------------------------------------
    // closeSiblings
    //----------------------------------------------------------------------------------------------
    private void closeSiblings(Action action)
    {
        List<Action> siblings = action.getParent().getSubActions();
        for(Action sibling : siblings)
        {
            closeAction(sibling);
        }
    }




    //----------------------------------------------------------------------------------------------
    // closeAction
    //----------------------------------------------------------------------------------------------
    private  void closeAction(int id)
    {
        closeAction(mActionMap.get(id).action);
    }


    //----------------------------------------------------------------------------------------------
    // closeAction
    //----------------------------------------------------------------------------------------------
    private synchronized void closeAction(Action action)
    {

        if(mActionMap.containsKey(action.getId()))
        {
            PopupWindow popup = mActionMap.get(action.getId()).popup;
            if(popup != null)
            {
                if(action.hasSubActions())
                {
                    for(Action subAction : action.getSubActions())
                    {
                        closeAction(subAction);
                        mActionMap.remove(subAction.getId());
                    }
                }

                if(popup.isShowing()) popup.dismiss();

                mActionMap.get(action.getId()).popup = null;
            }
        }

    }


    //----------------------------------------------------------------------------------------------
    // closeAncestors
    //----------------------------------------------------------------------------------------------
    private synchronized void closeNonAncestors(Action action)
    {

        Map.Entry<Integer, ActionInfo>   entry;
        Iterator<Map.Entry<Integer,ActionInfo>> it;
        List<Action> ancestors = getAncestors(action);

        it = mActionMap.entrySet().iterator();
        while (it.hasNext())
        {
            entry = it.next();
            int id = entry.getKey();
            ActionInfo info = entry.getValue();
            PopupWindow popup = entry.getValue().popup;
            if(isActionDialogShown(info.action) && !ancestors.contains(id))
            {
                popup.dismiss();
                it.remove();
            }
        }



    }


    //----------------------------------------------------------------------------------------------
    // getAncestors
    //----------------------------------------------------------------------------------------------
    private List<Action> getAncestors(Action action)
    {

        List<Action> ancestors = new ArrayList<>();
        Action ancestor = action.getParent();
        while(ancestor != null)
        {
            ancestors.add(ancestor);
            ancestor = ancestor.getParent();
        }

        return ancestors;
    }






    //----------------------------------------------------------------------------------------------
    // updateActionView
    //----------------------------------------------------------------------------------------------
    private synchronized void setupActionView(Action action)
    {

        ActionInfo info = mActionMap.get(action.getId());
        Drawable icon, background;



        background = mActionNormalBackground;
        icon = action.getIcon();



        if(action.getMode() == ActionModes.SELECTION )
        {
            if  (action.getDisplayMode() == ActionDisplayModes.SINGLE ||
                    action.getDisplayMode() == ActionDisplayModes.LIST )
            {
                icon = action.getDisplayedIcon();

            }
        }
        else if(action.getMode() == ActionModes.TASK )
        {
            if(action.isSelected() &&
                    action.getParent().getDisplayMode() == ActionDisplayModes.LIST_SINGLE_SELECTION)
            {
                background = mActionSelectedBackground;
            }
        }









        if(icon != null) info.view.setImageDrawable(icon);
        info.view.setScaleType(ImageView.ScaleType.CENTER_INSIDE);


            ViewHelper.setBackground(info.view,
                    background.getConstantState().newDrawable());


        info.view.invalidate();

        ActionListener listener = mActionListenerPtr.get();
        if(listener!=null)
        {
            listener.onActionViewCreated(action, info.view);
        }

    }


    void dumpActionMap()
    {
        LogHelper.log("mActionMap size : " + mActionMap.size());
        for(int id : mActionMap.keySet())
        {
            LogHelper.log("mActionMap id : " + id);

        }
    }


    //----------------------------------------------------------------------------------------------
    // closeSiblings
    //----------------------------------------------------------------------------------------------
    public synchronized View buildActionView(final Action action)
    {

        if(action.isRoot())
        {
            ActionInfo info = new ActionInfo(action, null);
            mActionMap.put(action.getId(), info);
        }
        LinearLayout layout = new LinearLayout(mContext);
        LinearLayout wrapper = new LinearLayout(mContext);
        int layoutLeftMargin,layoutTopMargin,layoutRightMargin,layoutBottomMargin;
        layoutLeftMargin=layoutTopMargin=layoutRightMargin=layoutBottomMargin = 0;
        int startSpacer ;
        int entryLeftMargin,entryTopMargin,entryRightMargin,entryBottomMargin;
        entryLeftMargin=entryTopMargin=entryRightMargin=entryBottomMargin =0;

        LinearLayout.LayoutParams layoutLParams = new LinearLayout.LayoutParams(mActionWidth,
                mActionHeight);

        LinearLayout.LayoutParams wrapperLParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        if(action.isRoot()) startSpacer = mRootShift;
        else startSpacer = mBranchShift;

        ViewHelper.setBackground(layout, mMenuBackground);
        if(action.getOrientation() == Orientations.VERTICAL)
        {
            layout.setOrientation(LinearLayout.VERTICAL);
        }
        else
        {
            layout.setOrientation(LinearLayout.HORIZONTAL);
        }


        if(action.getExpandDirection() == ExpandDirections.LEFT)
        {
            layoutRightMargin = startSpacer;
            entryLeftMargin = mActionsSpacing;
        }
        else if(action.getExpandDirection() == ExpandDirections.UP)
        {
            layoutBottomMargin = startSpacer;
            entryTopMargin = mActionsSpacing;

        }
        else if(action.getExpandDirection() == ExpandDirections.RIGHT)
        {
            layoutLeftMargin = startSpacer;
            entryRightMargin = mActionsSpacing;

        }
        else if(action.getExpandDirection() == ExpandDirections.DOWN)
        {
            layoutTopMargin = startSpacer;
            entryBottomMargin = mActionsSpacing;
        }


        layoutLParams.setMargins(entryLeftMargin,entryTopMargin,entryRightMargin,entryBottomMargin);


        List<Action> subActions = action.getSubActions();
        for(final Action subAction : subActions)
        {
            ImageView actionView;
            actionView = new ImageView(mContext);

            layout.addView(actionView, layoutLParams);

            actionView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event)
                {
                    LogHelper.log("actionView event.getEventTime() " + event.getEventTime());
                    if(event.getEventTime() == mLastPopupDismissEventTime &&
                            subAction.getId() == mLastDismissedPopupId )
                    {
                        mIgnoreActionClick = true;
                    }
                    return false;
                }
            });
            actionView.setOnClickListener(new ActionOnClickListener(subAction));

            ActionInfo info = new ActionInfo(subAction, actionView);
            mActionMap.put(subAction.getId(), info);
            setupActionView(subAction);

        }

        wrapperLParams.setMargins(layoutLeftMargin,layoutTopMargin,layoutRightMargin,layoutBottomMargin);
        wrapper.addView(layout, wrapperLParams);
        return wrapper;
    }


    class ActionOnClickListener implements View.OnClickListener
    {
        Action mAction;
        ActionOnClickListener(Action action)
        {
            mAction = action;
        }

        @Override
        public void onClick(View v)
        {
            if(mIgnoreActionClick)
            {
                mIgnoreActionClick = false;
                return ;
            }
            onActionClickListener(mAction, v);

        }
    };


    //----------------------------------------------------------------------------------------------
    // showPopup
    //----------------------------------------------------------------------------------------------
    private synchronized void showPopup(final int id, View view, Point location, int animationStyle)
    {



        Activity activity = mActivityPtr.get();
        if(activity == null) return;

        final ActionInfo info = mActionMap.get(id);

        final PopupWindow popup = new PopupWindow(activity);

        popup.setContentView(view);

        popup.setFocusable(false);
        popup.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if(animationStyle>0) popup.setAnimationStyle(animationStyle);


        if(Build.VERSION.SDK_INT >=14)
        {
            popup.setWindowLayoutMode(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        }
        else
        {
            Size size = ViewHelper.measure(view);
            popup.setHeight(size.height);
            popup.setWidth(size.width);
        }

        popup.setOutsideTouchable(true);
        popup.setTouchInterceptor(new View.OnTouchListener()
        {

            @Override
            public boolean onTouch(View v, MotionEvent event)
            {

                if(event.getAction() == MotionEvent.ACTION_OUTSIDE)
                {
                    LogHelper.log("popup event.getEventTime() " + event.getEventTime());
                    mLastPopupDismissEventTime = event.getEventTime();
                    mLastDismissedPopupId = id;

                    if(id == ROOT_ID)
                    {
                        // disable auto close
                        return true;
                    }


                    return false;
                }
                return false;
            }
        });



        info.popup = popup;

        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss()
            {
                //LogHelper.log("start onDismiss : "+id);

                onPopupDismissed(id);


               // LogHelper.log("end onDismiss : "+id);

            }
        });





        popup.showAtLocation(view, Gravity.NO_GRAVITY, location.x, location.y );


    }






    //==============================================================================================
    // Implementation
    //==============================================================================================


    class  MenuFactory implements ViewFactory<PopupWindow>
    {
        Action mAction;

        public MenuFactory(Action action)
        {
            mAction = action;
        }

        @Override
        public View buildView(PopupWindow param) {
            return null;
        }
    }


    //==============================================================================================
    // Types
    //==============================================================================================


    public static int ROOT_ID = 0;
    class ActionInfo
    {
        ActionInfo(Action action, ImageView view)
        {
            this.action = action;
            this.view = view;
        }
        public Action action;
        public ImageView view;
        public PopupWindow popup;

    }


    public interface ActionModes
    {
        public int SELECTION = 0 ;
        public int TASK = 1 ;

    }

    public interface ActionDisplayModes
    {
        public int LIST = 0 ;
        public int SINGLE = 1 ;
        public int LIST_SINGLE_SELECTION = 2 ;

    }


    public interface Orientations
    {
        public int VERTICAL = 0 ;
        public int HORIZONTAL = 1 ;
    }

    public interface ExpandDirections
    {
        public int LEFT = 0 ;
        public int UP = 1 ;
        public int RIGHT = 2 ;
        public int DOWN = 3 ;
    }


    public interface ActionListener
    {
        public void onActionClick(Action action, View view);
        public void onActionSelected(Action parent, Action action);
        public void onActionViewCreated(Action action, View view);
    }
}
