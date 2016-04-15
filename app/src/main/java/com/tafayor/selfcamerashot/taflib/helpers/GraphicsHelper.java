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

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.widget.ImageView;

import com.tafayor.selfcamerashot.App;


public class GraphicsHelper
{

    public static String TAG = GraphicsHelper.class.getSimpleName();





    
    public static int  GRADIENT_UP = 0;
    public static int  GRADIENT_DOWN = 1;
    public static int  GRADIENT_RIGHT = 2;
    public static int  GRADIENT_LEFT = 3;

    public static int  SHAPE_RECT = 1;
    public static int  SHAPE_OVAL = 2;




    //----------------------------------------------------------------------------------------------
    // tintDrawable
    //----------------------------------------------------------------------------------------------
    public static Drawable tintDrawable(Drawable img, int color)
    {

        Drawable tinted = DrawableCompat.wrap(img);
        DrawableCompat.setTint(tinted, color);

        return tinted;
    }


    //----------------------------------------------------------------------------------------------
    // tintView
    //----------------------------------------------------------------------------------------------
    public static  void tintView (ImageView view, int color)
    {
        Drawable  bg = view.getBackground();
        if(bg!=null)
        {
            bg = DrawableCompat.wrap(bg);
            DrawableCompat.setTint(bg, color);
            view.setBackground(bg);
        }

        Drawable img = view.getDrawable();
        if(img != null)
        {
            img = DrawableCompat.wrap(img);
            DrawableCompat.setTint(img, color);
            view.setImageDrawable(img);
        }
    }


    //----------------------------------------------------------------------------------------------
    // getEnableColorStateList
    //----------------------------------------------------------------------------------------------
    public static  ColorStateList getColorStateList (int color)
    {
        ColorStateList myColorStateList = new ColorStateList(
                new int[][]{
                        new int[]{}
                },
                new int[] {
                        color,
                }
        );

        return myColorStateList;

    }


    //----------------------------------------------------------------------------------------------
    // getEnableColorStateList
    //----------------------------------------------------------------------------------------------
    public static  ColorStateList getEnableColorStateList (int enabledColor, int disabledColor)
    {
        ColorStateList myColorStateList = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_enabled},
                        new int[]{}
                },
                new int[] {
                        disabledColor,
                        enabledColor,
                }
        );

        return myColorStateList;

    }


    //----------------------------------------------------------------------------------------------
    // rotateBitmap
    //----------------------------------------------------------------------------------------------
    public static  Bitmap rotateBitmap (Bitmap bmp , int angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        int w = bmp.getWidth();
        int h = bmp.getHeight();
        if(angle%90==0 && angle%180 !=0)
        {
             w = bmp.getHeight();
             h = bmp.getWidth();
        }
        Bitmap rotatedBitmap = Bitmap.createBitmap(bmp, 0, 0, w,
                h, matrix, false);

        return rotatedBitmap;
    }




    //----------------------------------------------------------------------------------------------
    // drawableToBitmap
    //----------------------------------------------------------------------------------------------
    public static  Bitmap drawableToBitmap (int resId)
    {
        return drawableToBitmap(App.getContext().getResources().getDrawable(resId));
    }

    //----------------------------------------------------------------------------------------------
    // drawableToBitmap
    //----------------------------------------------------------------------------------------------
    public static  Bitmap drawableToBitmap (Drawable drawable)
    {
        return drawableToBitmap (drawable, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
    }


    //----------------------------------------------------------------------------------------------
    // drawableToBitmap
    //----------------------------------------------------------------------------------------------
    public static  Bitmap drawableToBitmap (int resId, int width, int height)
    {
        Drawable drawable = App.getContext().getResources().getDrawable(resId);
        return drawableToBitmap(drawable, width, height);
    }

    //----------------------------------------------------------------------------------------------
    // drawableToBitmap
    //----------------------------------------------------------------------------------------------
    public static  Bitmap drawableToBitmap (Drawable drawable, int width, int height)
    {
        Bitmap bitmap;






        bitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());

        /*
        The drawable may be composite of bitmap and drawable layers
         */
        if (drawable instanceof BitmapDrawable)
        {
            Bitmap bmp = ((BitmapDrawable)drawable).getBitmap();
            canvas.drawBitmap(bmp, (canvas.getWidth()-bmp.getWidth())/2,
                    (canvas.getHeight()-bmp.getHeight())/2 , null);
        }
        drawable.draw(canvas);

        return bitmap;
    }



    //----------------------------------------------------------------------------------------------
    // addGlow
    //----------------------------------------------------------------------------------------------
    public static Bitmap addGlow(Drawable drawable, int glowColor, float radiusFactor)
    {
        Bitmap src = drawableToBitmap(drawable);

        return addGlow(src, glowColor, radiusFactor);
    }

    //----------------------------------------------------------------------------------------------
    // addGlow
    //----------------------------------------------------------------------------------------------
    public static Bitmap addGlow(Drawable drawable, int glowColor, float radiusFactor, int w,int  h)
    {
        Bitmap src = drawableToBitmap(drawable,w,h);

        return addGlow(src, glowColor, radiusFactor);
    }




    //----------------------------------------------------------------------------------------------
    // addGlow
    //----------------------------------------------------------------------------------------------
    public static Bitmap addGlow(Drawable drawable, int glowColor)
    {
        Bitmap src = drawableToBitmap(drawable);

        return addGlow(src, glowColor, 0);
    }


    //----------------------------------------------------------------------------------------------
    // addGlow
    //----------------------------------------------------------------------------------------------
    public static Bitmap addGlow(Context ctx, int resId, int glowColor)
    {

        Bitmap src = BitmapFactory.decodeResource(ctx.getResources(), resId);

        return addGlow(src, glowColor, 0);
    }

    //----------------------------------------------------------------------------------------------
    // addGlow
    //----------------------------------------------------------------------------------------------
    public static Bitmap addGlow(Context ctx, int resId, int glowColor, float radiusFactor)
    {

        Bitmap src = BitmapFactory.decodeResource(ctx.getResources(), resId);

        return addGlow(src, glowColor, radiusFactor);
    }


    //----------------------------------------------------------------------------------------------
    // addGlow
    //----------------------------------------------------------------------------------------------
    public static Bitmap addGlow(Bitmap src, int glowColor)
    {
       return addGlow(src, glowColor, 0);
    }


    //----------------------------------------------------------------------------------------------
    // addGlow
    //----------------------------------------------------------------------------------------------
    public static Bitmap addGlow(Bitmap src, int glowColor, float radius)
    {
        float DEFAULT_GLOW_RADIUS_FACTOR = 0.1f;
        // An added margin to the initial image
        int margin ;
        int glowRadius ;



        int midSize = (src.getWidth() + src.getHeight())/2;

        if(radius > 0 && radius < 1) glowRadius = (int) (midSize * radius);
        else if(radius >= 1) glowRadius = (int) radius;
        else  glowRadius = (int) (midSize * DEFAULT_GLOW_RADIUS_FACTOR);


        margin = (int) (glowRadius);

        Bitmap alpha = src.extractAlpha();

        // The output bitmap (with the icon + glow)
        Bitmap bmp = Bitmap.createBitmap(src.getWidth() + margin*2,
                src.getHeight() + margin*2, Bitmap.Config.ARGB_8888);

        //LogHelper.log("glow new size : " + (src.getWidth() + margin*2));
        // The canvas to paint on the image
        Canvas canvas = new Canvas(bmp);

        Paint paint = new Paint();
        paint.setColor(glowColor);

        // outer glow
        paint.setMaskFilter(new BlurMaskFilter(glowRadius, BlurMaskFilter.Blur.OUTER));
        canvas.drawBitmap(alpha, margin, margin, paint);

        // original icon
        canvas.drawBitmap(src, margin, margin, null);

        return bmp;
    }


    //----------------------------------------------------------------------------------------------
    // addShadow
    //----------------------------------------------------------------------------------------------
    public static Bitmap addShadow(Bitmap src, float radius, float dx, float dy,  int shadowColor)
    {



        int margin = 0;
        if(radius <=0) radius = 1;
        if(dx <0) dx = 0;
        if(dy <0) dy = 0;

        margin = (int)radius;

        Bitmap alpha = src.extractAlpha();

        Bitmap bmp = Bitmap.createBitmap(src.getWidth() + margin*2,
                src.getHeight() + margin*2, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bmp);
        //LogHelper.log ("Canvas.isHardwareAccelerated() " + canvas.isHardwareAccelerated() );
        Paint paint = new Paint();
        paint.setColor(shadowColor);

        paint.setShadowLayer(radius, dx, dy, shadowColor);

        canvas.drawBitmap(alpha, margin, margin, paint);
        canvas.drawBitmap(src, margin, margin, null);

        return bmp;
    }



    //----------------------------------------------------------------------------------------------
    // converToGrayscale
    //----------------------------------------------------------------------------------------------
    public static  void converToGrayscale(Drawable drawable)
    {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);

        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);

        drawable.setColorFilter(filter);


    }


    //----------------------------------------------------------------------------------------------
    // setColorAlpha
    //----------------------------------------------------------------------------------------------
    public static  int setColorAlpha(int color, float alphaPercent)
    {
        int alpha = (int)(LangHelper.clamp(alphaPercent, 0f , 1f) * 255);
        int newColor = Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color));
        return newColor;
    }


    //----------------------------------------------------------------------------------------------
    // createSelector
    //----------------------------------------------------------------------------------------------
    public static  StateListDrawable createSelector(Drawable normal, Drawable pressed)
    {
        StateListDrawable selector = new StateListDrawable();


        selector.addState(new int[]{android.R.attr.state_pressed}, pressed.mutate());

        selector.addState(new int[0] , normal.mutate());

        return selector;
    }



    //----------------------------------------------------------------------------------------------
    // setCorners
    //----------------------------------------------------------------------------------------------
    public static  void setCorners(GradientDrawable drawable, float tl, float tr, float br, float bl)
    {

        float[] radii = null;

            radii = new float[]{tl,tl,
                    tr,tr,
                    br,br,
                    bl,bl};


        drawable.setCornerRadii(radii);

    }




    public static Drawable createRectGradient(final int direction, final int[] colors)
    {

        final ShapeDrawable shapeDrawable = new ShapeDrawable(new RectShape());
        final ShapeDrawable.ShaderFactory shaderFactory = new ShapeDrawable.ShaderFactory() {

            @Override
            public Shader resize(int width, int height)
            {
                int x0,y0,x1,y1;
                x0=y0=x1=y1=0;
                if(direction == GRADIENT_DOWN) {y1 = height;}
                else if(direction == GRADIENT_UP) {y0 = height;}
                else if(direction == GRADIENT_RIGHT) {x1 = width;}
                else if(direction == GRADIENT_LEFT) {x0 = width;}
                LinearGradient lg = new LinearGradient(x0, y0, x1, y1,
                        colors,
                        new float[]{
                                0, 0.05f, 0.1f, 1},
                        Shader.TileMode.REPEAT);
                return lg;
            }
        };
        shapeDrawable.setShaderFactory(shaderFactory);


        return shapeDrawable;
    }



    public static Drawable createRGradient(int shape, final int[] colors, final float[] positions) {

        ShapeDrawable shapeDrawable;
        if (shape == SHAPE_RECT) {
            shapeDrawable = new ShapeDrawable(new RectShape());
        } else {
            shapeDrawable = new ShapeDrawable(new OvalShape());
        }


        ShapeDrawable.ShaderFactory shaderFactory = new ShapeDrawable.ShaderFactory() {

            @Override
            public  Shader resize(int width, int height) {
                float gradRadius = Math.max(width, height) / 2;
                RadialGradient rg = new RadialGradient(width / 2, height / 2, gradRadius,
                        colors,
                        positions,
                        Shader.TileMode.CLAMP);


                return rg;
            }
        };
        shapeDrawable.setShaderFactory(shaderFactory);


        return shapeDrawable;
    }

    public static Drawable createLGradient(int shape, final int[] colors, final float[] positions) {

        ShapeDrawable shapeDrawable;
        if (shape == SHAPE_RECT) {
            shapeDrawable = new ShapeDrawable(new RectShape());
        } else {
            shapeDrawable = new ShapeDrawable(new OvalShape());
        }


        ShapeDrawable.ShaderFactory shaderFactory = new ShapeDrawable.ShaderFactory() {

            @Override
            public  Shader resize(int width, int height) {
                LinearGradient rg = new LinearGradient(0, 0, 0, height,
                        colors,
                        positions,
                        Shader.TileMode.CLAMP);


                return rg;
            }
        };
        shapeDrawable.setShaderFactory(shaderFactory);


        return shapeDrawable;
    }


    public static  Bitmap getRoundedCornerBitmap(Bitmap bitmap) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),

                bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(output);


        final int color = 0xff424242;

        final Paint paint = new Paint();

        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        final RectF rectF = new RectF(rect);

        final float roundPx = 12;


        paint.setAntiAlias(true);

        canvas.drawARGB(0, 0, 0, 0);

        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);


        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        canvas.drawBitmap(bitmap, rect, rect, paint);


        return output;
    }


}

