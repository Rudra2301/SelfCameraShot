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


package com.tafayor.selfcamerashot.taflib.helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;

import android.util.StateSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;


import com.tafayor.selfcamerashot.taflib.constants.LanguageValues;
import com.tafayor.selfcamerashot.taflib.types.Size;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public  class ViewHelper 
{
  


   
 





    //----------------------------------------------------------------------------------------------
    // relocateViewInFrameLayout
    //----------------------------------------------------------------------------------------------
    public static  void relocateViewInFrameLayout(View view, int x, int y)
    {
        FrameLayout.LayoutParams lparams = (FrameLayout.LayoutParams) view.getLayoutParams();
        lparams.gravity = Gravity.NO_GRAVITY;
        int rmargin = lparams.rightMargin;
        int bmargin = lparams.bottomMargin;
        lparams.setMargins(x,y,rmargin,bmargin);

        ((FrameLayout) view.getParent()).updateViewLayout(view, lparams);
    }



    //----------------------------------------------------------------------------------------------
    // setViewMarginInLLayout
    //----------------------------------------------------------------------------------------------
    public static  void setViewMarginInLLayout(View view, int margin)
    {
        LinearLayout.LayoutParams lparams = (LinearLayout.LayoutParams)view.getLayoutParams();

        lparams.topMargin = margin;
        lparams.rightMargin = margin;
        lparams.bottomMargin = margin;
        lparams.leftMargin = margin;

        ((LinearLayout)view.getParent()).updateViewLayout(view, lparams);
    }


    //----------------------------------------------------------------------------------------------
    // setViewMarginInLLayout
    //----------------------------------------------------------------------------------------------
    public static  void setViewMarginInRLayout(View view, int margin)
    {
        RelativeLayout.LayoutParams lparams = (RelativeLayout.LayoutParams)view.getLayoutParams();

        lparams.topMargin = margin;
        lparams.rightMargin = margin;
        lparams.bottomMargin = margin;
        lparams.leftMargin = margin;

        ((RelativeLayout)view.getParent()).updateViewLayout(view, lparams);
    }


    //----------------------------------------------------------------------------------------------
    // releaseDrawable
    //----------------------------------------------------------------------------------------------
    public static  void releaseDrawable (ImageView imageView)
    {

        Drawable drawable = imageView.getDrawable();
        if(drawable != null)
        {
            drawable.setCallback(null);
            if (drawable instanceof BitmapDrawable) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                Bitmap bitmap = bitmapDrawable.getBitmap();
                bitmap.recycle();
            }
        }

    }




    //----------------------------------------------------------------------------------------------
    // setRotation
    //----------------------------------------------------------------------------------------------
    public static  void setRotation (View view, int angle)
    {
        if(Build.VERSION.SDK_INT >= 11)
        {
            view.setRotation(angle);
        }

    }


    //----------------------------------------------------------------------------------------------
    // getRotation
    //----------------------------------------------------------------------------------------------
    public static  float getRotation(View view)
    {
        if(Build.VERSION.SDK_INT >= 11)
        {
            return view.getRotation();
        }
        else
        {
            return 0;
        }

    }

    //----------------------------------------------------------------------------------------------
    // setRotation
    //----------------------------------------------------------------------------------------------
    public static  void setRotation(View view, float angle)
    {
        if(Build.VERSION.SDK_INT >= 11)
        {
            view.setRotation(angle);
        }

    }


    //----------------------------------------------------------------------------------------------
    // enableSoftwareLayer
    //----------------------------------------------------------------------------------------------
    public static  void enableSoftwareLayer (View view)
    {
        if(Build.VERSION.SDK_INT >= 11)
        {
            view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

    }



    //----------------------------------------------------------------------------------------------
    // scaleDrawable
    //----------------------------------------------------------------------------------------------
    public static  Drawable scaleDrawable (Context ctx, Drawable image, float scaleFactor)
    {

        if ((image == null) || !(image instanceof BitmapDrawable)) {
            return image;
        }

        Bitmap b = ((BitmapDrawable)image).getBitmap();

        int sizeX = Math.round(image.getIntrinsicWidth() * scaleFactor);
        int sizeY = Math.round(image.getIntrinsicHeight() * scaleFactor);

        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, sizeX, sizeY, false);

        image = new BitmapDrawable(ctx.getResources(), bitmapResized);

        return image;

    }


    //----------------------------------------------------------------------------------------------
    // createSelector
    //----------------------------------------------------------------------------------------------
    public static  StateListDrawable createSelector(Drawable normalDrawable, Drawable pressedDrawable)
    {
        StateListDrawable selector = new StateListDrawable();
        selector.addState(new  int[]{android.R.attr.state_pressed}, pressedDrawable);
        selector.addState(StateSet.WILD_CARD, normalDrawable);

        return selector;
    }


    //----------------------------------------------------------------------------------------------
    // mergeLayers
    //----------------------------------------------------------------------------------------------
    public static  LayerDrawable mergeLayers(Drawable d1, Drawable d2)
    {
        LayerDrawable layerDrawable;
        layerDrawable = new LayerDrawable(new Drawable[]{d1, d2 });

        return layerDrawable;
    }



    //----------------------------------------------------------------------------------------------
    // measure
    //----------------------------------------------------------------------------------------------
    public static Size measure(View view)
    {
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(widthMeasureSpec, heightMeasureSpec);

        return new Size(view.getMeasuredWidth(), view.getMeasuredHeight());
    }




    //----------------------------------------------------------------------------------------------
    // getBoundsOnScreen
    //----------------------------------------------------------------------------------------------
    public static Rect getBoundsOnScreen(View v)
    {
        int[] loc_int = new int[2];
        if (v == null) return null;
        try
        {
            v.getLocationOnScreen(loc_int);

        } catch (NullPointerException npe)
        {
            //Happens when the view doesn't exist on screen anymore.
            return null;
        }
        Rect bounds = new Rect();
        float rotation = ViewHelper.getRotation(v);

        bounds.left = loc_int[0];
        bounds.top = loc_int[1];


        if(rotation == 90)
        {
            bounds.left = loc_int[0] - v.getHeight();
        }
        else if(rotation == 180)
        {
            bounds.left = loc_int[0] - v.getHeight();
            bounds.top = loc_int[1] - v.getWidth();
        }
        else if(rotation == 270)
        {
            bounds.top = loc_int[1] - v.getWidth();
        }


        bounds.right = bounds.left + v.getWidth();
        bounds.bottom = bounds.top + v.getHeight();




        return bounds;
    }


    //----------------------------------------------------------------------------------------------
    // getBoundsOnWindow
    //----------------------------------------------------------------------------------------------
    public static Rect getBoundsOnWindow(View v)
    {
        int[] loc_int = new int[2];
        if (v == null) return null;
        try
        {
            v.getLocationInWindow(loc_int);
        } catch (NullPointerException npe)
        {
            //Happens when the view doesn't exist on screen anymore.
            return null;
        }
        Rect bounds = new Rect();
        bounds.left = loc_int[0];
        bounds.top = loc_int[1];
        bounds.right = bounds.left + v.getWidth();
        bounds.bottom = bounds.top + v.getHeight();
        return bounds;
    }

    //----------------------------------------------------------------------------------------------
    // setCheckedOnUI
    //----------------------------------------------------------------------------------------------
    public static  void setCheckedOnUI(Context ctx, final CompoundButton view, final boolean checked)
    {
        AppHelper.postOnUi(ctx, new Runnable() {
            @Override
            public  void run()
            {
                view.setChecked(checked);
            }
        });

    }


    //--------------------------------------------------------------------------------------------------
    // getLocationOnScreen
    //--------------------------------------------------------------------------------------------------
    public static  Point getLocationInWindow(View view)
    {
        int[] location = new int[2];
        view.getLocationInWindow(location);
        Point point = new Point(location[0], location[1]);

        return point;
    }



    //--------------------------------------------------------------------------------------------------
    // getLocationInScreen
    //--------------------------------------------------------------------------------------------------
    public static  Point getLocationInScreen(View view)
    {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        Point point = new Point(location[0], location[1]);

        return point;
    }




    //--------------------------------------------------------------------------------------------------
    // getViewId
    //--------------------------------------------------------------------------------------------------
    public static  int getViewId(Context ctx, String viewName)
    {
        int id = -1;

        Resources  resources = ctx.getResources();
        id = resources.getIdentifier(viewName, "id", ctx.getPackageName());

        return id;
    }


    //--------------------------------------------------------------------------------------------------
    // findViewByName
    //--------------------------------------------------------------------------------------------------
    public static  View findViewByName(Context ctx, View parent, String name)
    {
        View view = null;
        Resources  resources = parent.getContext().getResources();
        int id = resources.getIdentifier(name, "id", ctx.getPackageName());
        view = parent.findViewById(id);

        return view;
    }


    public static void setRecursiveAlpha(ViewGroup root, float alphaFactor)
    {
        for(int i=0;i<root.getChildCount(); i++)
        {
            ViewHelper.setViewAlpha(root, alphaFactor);
            View child = root.getChildAt(i);
            if(child != null)
            {
                if(child instanceof  ViewGroup) setRecursiveAlpha((ViewGroup)child, alphaFactor);
                else
                {
                    ViewHelper.setViewAlpha(child, alphaFactor);
                }
            }
        }
    }


    //--------------------------------------------------------------------------------------------------
    // setViewAlpha
    //--------------------------------------------------------------------------------------------------
    // alpha : 0 - 1
    public static  void setViewAlpha(View view, float alpha)
    {
        setViewAlpha(view, (int) (alpha * 255));
    }

    //--------------------------------------------------------------------------------------------------
    // setViewAlpha
    //--------------------------------------------------------------------------------------------------
    // alpha 0 - 255
    public static  void setViewAlpha(View view, int alpha)
    {
        if(view instanceof ImageView)
        {
            ImageView imageView = ((ImageView)view);
            if(Build.VERSION.SDK_INT >= 16)
            {
                imageView.setImageAlpha(alpha);

            }
            else
            {
                    imageView.setAlpha(alpha);

            }
        }

            Drawable bg =  view.getBackground();
            if(bg != null) bg.setAlpha(alpha);

    }


    public static  Size getViewSize(View view)
    {
        ViewGroup.LayoutParams lparams = view.getLayoutParams();
        return new Size(lparams.width, lparams.height);
    }


    public static  Point getViewPositionInsideParent( View child, ViewGroup parent)
    {
        Point childPos = getLocationInScreen(child);
        Point parentPos = getLocationInScreen(parent);
        Point pos = new Point();
        pos.x = childPos.x - parentPos.x;
        pos.y = childPos.y - parentPos.y;
        return pos;
    }


    public static  void resizeView(View view, int width, int height)
    {
        ViewGroup.LayoutParams lparams = view.getLayoutParams();

        lparams.width = width;
        lparams.height = height;

        ViewGroup parent = ((ViewGroup)view.getParent());
        if(parent!=null)
        {
            ((ViewGroup)view.getParent()).updateViewLayout(view, lparams);
        }
    }

    public static  void resizeViewWidth(View view, int width )
    {
        ViewGroup.LayoutParams lparams = view.getLayoutParams();

        lparams.width = width;
        ((ViewGroup)view.getParent()).updateViewLayout(view, lparams);
    }


    public static  void resizeViewHeight(View view, int height )
    {
        ViewGroup.LayoutParams lparams = view.getLayoutParams();

        lparams.height = height;
        ((ViewGroup)view.getParent()).updateViewLayout(view, lparams);
    }

    public static  void updateViewSize(View view, int dx, int dy)
    {
        ViewGroup.LayoutParams lparams = view.getLayoutParams();

        lparams.width += dx;
        lparams.height += dy;
        ((ViewGroup)view.getParent()).updateViewLayout(view, lparams);
    }


    public static  void updateViewSize(View view, int dx, int dy, int min)
    {
        ViewGroup.LayoutParams lparams = view.getLayoutParams();

        lparams.width += dx;
        lparams.height += dy;
        if(lparams.width < min) lparams.width = min;
        if(lparams.height < min) lparams.height = min;
        ((ViewGroup)view.getParent()).updateViewLayout(view, lparams);
    }


    public static  void fixViewRtl(Context ctx, View view)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
        {
            Resources res = ctx.getResources();
            if(res == null ) return;

            String lang = res.getConfiguration().locale.getLanguage();
            if(lang.equals(LanguageValues.ARABIC))
            {
                view.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
        }
    }



    public static  void fixViewGroupRtl(Context ctx, View view)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
        {
            Resources res = ctx.getResources();
            if(res == null ) return;
            String lang = res.getConfiguration().locale.getLanguage();
            if(lang.equals(LanguageValues.ARABIC))
            {
                view.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

                if(view instanceof ViewGroup)
                {
                    ViewGroup parent = (ViewGroup) view;
                    int count = parent.getChildCount();

                    for(int i=0;i<count;i++)
                    {
                        View child =  parent.getChildAt(i);

                        if(child instanceof ViewGroup)
                        {
                            fixViewGroupRtl(ctx, (ViewGroup) child);
                        }
                        else child.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                    }
                }

            }
        }

    }


    /**
     * Android pre Honeycomb versions allocate bitmap memory natively so we need a manual recycling
     * @param bitmap
     */
    @SuppressLint("NewApi")
    public static   void recycleBitmap(Bitmap bitmap)
    {

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB )
        {
            if(bitmap != null) bitmap.recycle();
        }
    }


    public static  void recycleDrawable(Drawable drawable)
    {
        if (drawable instanceof BitmapDrawable)
        {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            recycleBitmap(bitmap);
        }
    }




    public static  void unbindDrawables(View view)
    {
        if (view == null)
        {
            return;
        }

        if (view.getBackground() != null)
        {
            view.getBackground().setCallback(null);
        }

        if (view instanceof ImageView && ((ImageView) view).getDrawable() != null)
        {
            ((ImageView) view).getDrawable().setCallback(null);

        }

        if (view instanceof ViewGroup)
        {
            if (view instanceof AbsListView) {
                // Clean out list view
                List<View> cells = new LinkedList<View>();
                ((AbsListView) view).reclaimViews(cells);
                if (cells != null) {
                    for (View c : cells) {
                        unbindDrawables(c);
                    }
                }
            } else {
                for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                    unbindDrawables(((ViewGroup) view).getChildAt(i));
                }
            }
        }


    }



    public static  void freeWebView(WebView webView)
    {
        webView.clearHistory();
        webView.clearCache(true);
        webView.loadUrl("about:blank");
    }


    public static   void setBackgroundOnUi(Context ctx, final View view , final Drawable drawable)
    {
        Runnable task = new Runnable() {
            @Override
            public  void run() {
                setBackground(view, drawable);
            }
        };
        AppHelper.postOnUi(ctx, task);
    }




    @SuppressLint("NewApi")
    public static   void setBackground(final View view , final Bitmap bmp)
    {
        setBackground(view, new BitmapDrawable(view.getContext().getResources(), bmp));
    }

    @SuppressLint("NewApi")
    public static   void setBackground(final View view , final Drawable drawable)
    {

        int sdk = Build.VERSION.SDK_INT;


        if (view.getBackground() != null)
        {
            //view.getBackground().setCallback(null);
            //recycleDrawable(view.getBackground() );
        }


        if(sdk < Build.VERSION_CODES.JELLY_BEAN)
        {
            view.setBackgroundDrawable(drawable);
        }
        else
        {
            view.setBackground(drawable);
        }


    }



    @SuppressLint("NewApi")
    public static   void setBackground(Context ctx, final View view , int drawableResID)
    {
        setBackground(view, ResHelper.getResDrawable(ctx, drawableResID));
    }


    public static   void setBackgroundOnUi(final Context ctx, final View view , final int drawableResID)
    {
        Runnable task = new Runnable() {
            @Override
            public  void run()
            {
                setBackground(view, ResHelper.getResDrawable(ctx, drawableResID));
            }
        };
        AppHelper.postOnUi(ctx, task);
    }


    public static   void removeOnGlobalLayoutListener(View v, ViewTreeObserver.OnGlobalLayoutListener listener)
    {
        if (Build.VERSION.SDK_INT < 16)
        {
            v.getViewTreeObserver().removeGlobalOnLayoutListener(listener);
        }
        else
        {
            v.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
        }
    }


    public static   void addOnPreDrawTask(final View view, final Runnable task)
    {
        final ViewTreeObserver vto = view.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                task.run();
                view.getViewTreeObserver().removeOnPreDrawListener(this);
                return false;
            }
        });
    }


    public static   void addOnGlobalLayoutTask(final View view, final Runnable task)
    {
        final ViewTreeObserver vto = view.getViewTreeObserver();

        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                removeOnGlobalLayoutListener(view, this);
                task.run();

            }
        });

    }




    public static   void enableListViewInsideScrollView(ListView listView)
    {
        listView.setOnTouchListener(new View.OnTouchListener()
        {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public  boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }

    public static   void setListViewHeightBasedOnChildren(ListView listView)
    {
        setListViewHeightBasedOnChildren(listView, null);
    }

    /**** Method for Setting the Height of the ListView dynamically.
     **** Hack to fix the issue of not showing all the items of the ListView
     **** when placed inside a ScrollView  ****/
    public static   void setListViewHeightBasedOnChildren(final ListView listView, final Runnable postTask)
    {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) return;


        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++)
        {
            view = listAdapter.getView(i, view, listView);
            view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() ));
        params.height += (view.getMeasuredHeight()*8); // fix to show a hidden part of listview
        listView.setLayoutParams(params);


        if(postTask != null)
        {
            ViewTreeObserver vto = listView.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public  void onGlobalLayout()
                {
                    removeOnGlobalLayoutListener(listView, this);
                    if(postTask != null) listView.post(postTask);
                }
            });
        }


        listView.invalidate();
        //listView.requestLayout();
    }



    //----------------------------------------------------------------------------------------------
    // generateViewId
    //----------------------------------------------------------------------------------------------
    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);
    public static int generateViewId()
    {

        if (Build.VERSION.SDK_INT >= 17)
        {

            return View.generateViewId();

        }
        else
        {
            int result = 1;
            for (;;)
            {
                result = sNextGeneratedId.get();
                // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
                int newValue = result + 1;
                if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
                if (sNextGeneratedId.compareAndSet(result, newValue))
                {
                    return result;
                }
            }

        }


    }




}


