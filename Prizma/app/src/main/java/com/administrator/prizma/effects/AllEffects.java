package com.administrator.prizma.effects;

import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.Log;

import java.nio.IntBuffer;
import java.util.Random;

/**
 * Created by admin on 23-12-2016.
 */
public class AllEffects
{
    public static final double FULL_CIRCLE_DEGREE = 360.0D;
    public static final double HALF_CIRCLE_DEGREE = 180.0D;
    public static final double PI = 3.14159D;
    public static final double RANGE = 256.0D;
    private static final int COLOR_MAX = 0xff;
    private static final int COLOR_MIN = 0x00;

    public Bitmap applyFleaEffect(Bitmap source) {
        int width = source.getWidth();
        int height = source.getHeight();
        int[] pixels = new int[width * height];
        source.getPixels(pixels, 0, width, 0, 0, width, height);
        Random random = new Random();
        boolean index = false;

        for(int bmOut = 0; bmOut < height; ++bmOut) {
            for(int x = 0; x < width; ++x) {
                int var10 = bmOut * width + x;
                int randColor = Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255));
                pixels[var10] |= randColor;
            }
        }

        Bitmap var11 = Bitmap.createBitmap(width, height, source.getConfig());
        var11.setPixels(pixels, 0, width, 0, 0, width, height);
        return var11;
    }

    public Bitmap applyEngraveEffect(Bitmap src) {
        ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);
        convMatrix.setAll(0.0D);
        convMatrix.Matrix[0][0] = -2.0D;
        convMatrix.Matrix[1][1] = 2.0D;
        convMatrix.Factor = 10.0D;
        convMatrix.Offset = 100.0D;
        return ConvolutionMatrix.computeConvolution3x3(src, convMatrix);
    }

    public Bitmap applyColorFilterEffect(Bitmap src, double red, double green, double blue) {
        int width = src.getWidth();
        int height = src.getHeight();
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());

        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                int pixel = src.getPixel(x, y);
                int A = Color.alpha(pixel);
                int R = (int)((double) Color.red(pixel) * red);
                int G = (int)((double) Color.green(pixel) * green);
                int B = (int)((double) Color.blue(pixel) * blue);
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }

        return bmOut;
    }



    public Bitmap applyHueFilter(Bitmap source, int level) {
        int width = source.getWidth();
        int height = source.getHeight();
        int[] pixels = new int[width * height];
        float[] HSV = new float[3];
        source.getPixels(pixels, 0, width, 0, 0, width, height);
        boolean index = false;

        for(int bmOut = 0; bmOut < height; ++bmOut) {
            for(int x = 0; x < width; ++x) {
                int var10 = bmOut * width + x;
                Color.colorToHSV(pixels[var10], HSV);
                HSV[0] *= (float)level;
                HSV[0] = (float) Math.max(0.0D, Math.min((double)HSV[0], 360.0D));
                pixels[var10] |= Color.HSVToColor(HSV);
            }
        }

        Bitmap var11 = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        var11.setPixels(pixels, 0, width, 0, 0, width, height);
        return var11;
    }

    public Bitmap applyBoostEffect(Bitmap src, int type, float percent) {
        int width = src.getWidth();
        int height = src.getHeight();
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());

        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                int pixel = src.getPixel(x, y);
                int A = Color.alpha(pixel);
                int R = Color.red(pixel);
                int G = Color.green(pixel);
                int B = Color.blue(pixel);
                if(type == 1) {
                    R = (int)((float)R * (1.0F + percent));
                    if(R > 255) {
                        R = 255;
                    }
                } else if(type == 2) {
                    G = (int)((float)G * (1.0F + percent));
                    if(G > 255) {
                        G = 255;
                    }
                } else if(type == 3) {
                    B = (int)((float)B * (1.0F + percent));
                    if(B > 255) {
                        B = 255;
                    }
                }

                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }

        return bmOut;
    }

    public static Bitmap applyBlackFilter(Bitmap source) {
        // get image size
        int width = source.getWidth();
        int height = source.getHeight();
        int[] pixels = new int[width * height];
        // get pixel array from source
        source.getPixels(pixels, 0, width, 0, 0, width, height);
        // random object
        Random random = new Random();

        int R, G, B, index = 0, thresHold = 0;
        // iteration through pixels
        for(int y = 0; y < height; ++y) {
            for(int x = 0; x < width; ++x) {
                // get current index in 2D-matrix
                index = y * width + x;
                // get color
                R = Color.red(pixels[index]);
                G = Color.green(pixels[index]);
                B = Color.blue(pixels[index]);
                // generate threshold
                thresHold = random.nextInt(COLOR_MAX);
                if(R < thresHold && G < thresHold && B < thresHold) {
                    pixels[index] = Color.rgb(20, 50, 150);
                }
            }
        }
        // output bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bmOut.setPixels(pixels, 0, width, 0, 0, width, height);
        return bmOut;
    }

    public Bitmap applySepiaToningEffect(Bitmap src, int depth, double red, double green, double blue) {
        int width = src.getWidth();
        int height = src.getHeight();
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
        double GS_RED = 0.3D;
        double GS_GREEN = 0.59D;
        double GS_BLUE = 0.11D;

        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                int pixel = src.getPixel(x, y);
                int A = Color.alpha(pixel);
                int R = Color.red(pixel);
                int G = Color.green(pixel);
                int B = Color.blue(pixel);
                B = G = R = (int)(0.3D * (double)R + 0.59D * (double)G + 0.11D * (double)B);
                R = (int)((double)R + (double)depth * red);
                if(R > 255) {
                    R = 255;
                }

                G = (int)((double)G + (double)depth * green);
                if(G > 255) {
                    G = 255;
                }

                B = (int)((double)B + (double)depth * blue);
                if(B > 255) {
                    B = 255;
                }

                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }
        return bmOut;
    }

    public Bitmap applySaturationFilter(Bitmap source, int level) {
        int width = source.getWidth();
        int height = source.getHeight();
        int[] pixels = new int[width * height];
        float[] HSV = new float[3];
        source.getPixels(pixels, 0, width, 0, 0, width, height);
        boolean index = false;

        for(int bmOut = 0; bmOut < height; ++bmOut) {
            for(int x = 0; x < width; ++x) {
                int var10 = bmOut * width + x;
                Color.colorToHSV(pixels[var10], HSV);
                HSV[1] *= (float)level;
                HSV[1] = (float) Math.max(0.0D, Math.min((double)HSV[1], 1.0D));
                pixels[var10] |= Color.HSVToColor(HSV);
            }
        }

        Bitmap var11 = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        var11.setPixels(pixels, 0, width, 0, 0, width, height);
        return var11;
    }

    public static Bitmap applyMeanRemoval(Bitmap paramBitmap)
    {
        double[] arrayOfDouble1 = { -1.0D, -1.0D, -1.0D };
        double[] arrayOfDouble2 = { -1.0D, 9.0D, -1.0D };
        double[] arrayOfDouble3 = { -1.0D, -1.0D, -1.0D };
        ConvolutionMatrix localConvolutionMatrix = new ConvolutionMatrix(50);
        localConvolutionMatrix.applyConfig(new double[][] { arrayOfDouble1, arrayOfDouble2, arrayOfDouble3 });
        localConvolutionMatrix.Factor = 1.0D;
        localConvolutionMatrix.Offset = 0.0D;
        return ConvolutionMatrix.computeConvolution3x3(paramBitmap, localConvolutionMatrix);
    }

    public static Bitmap applyShadingFilter(Bitmap source, int shadingColor) {
        // get image size
        int width = source.getWidth();
        int height = source.getHeight();
        int[] pixels = new int[width * height];
        // get pixel array from source
        source.getPixels(pixels, 0, width, 0, 0, width, height);

        int index = 0;
        // iteration through pixels
        for(int y = 0; y < height; ++y) {
            for(int x = 0; x < width; ++x) {
                // get current index in 2D-matrix
                index = y * width + x;
                // AND
                pixels[index] &= shadingColor;
            }
        }
        // output bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bmOut.setPixels(pixels, 0, width, 0, 0, width, height);
        return bmOut;
    }

    public static Bitmap createContrast(Bitmap src, double value)
    {
        int width = src.getWidth();
        int height = src.getHeight();
        // create output bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
        // color information
        int A, R, G, B;
        int pixel;
        // get contrast value
        double contrast = Math.pow((100 + value) / 100, 2);

        // scan through all pixels
        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                // get pixel color
                pixel = src.getPixel(x, y);
                A = Color.alpha(pixel);
                // apply filter contrast for every channel R, G, B
                R = Color.red(pixel);
                R = (int)(((((R / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if(R < 0) { R = 0; }
                else if(R > 255) { R = 255; }

                G = Color.green(pixel);
                G = (int)(((((G / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if(G < 0) { G = 0; }
                else if(G > 255) { G = 255; }

                B = Color.blue(pixel);
                B = (int)(((((B / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if(B < 0) { B = 0; }
                else if(B > 255) { B = 255; }

                // set new pixel color to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }

        // return final image
        return bmOut;
    }


    public static Bitmap decreaseColorDepth(Bitmap src, int bitOffset) {
        // get image size
        System.out.println("Testtttttttttt : "+ bitOffset);
        int width = src.getWidth();
        int height = src.getHeight();
        // create output bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
        // color information
        int A, R, G, B;
        int pixel;

        // scan through all pixels
        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                // get pixel color
                pixel = src.getPixel(x, y);
                A = Color.alpha(pixel);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);

                // round-off color offset
                R = ((R + (bitOffset / 2)) - ((R + (bitOffset / 2)) % bitOffset) - 1);
                if(R < 0) { R = 0; }
                G = ((G + (bitOffset / 2)) - ((G + (bitOffset / 2)) % bitOffset) - 1);
                if(G < 0) { G = 0; }
                B = ((B + (bitOffset / 2)) - ((B + (bitOffset / 2)) % bitOffset) - 1);
                if(B < 0) { B = 0; }

                // set pixel color to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }

        // return final image
        return bmOut;
    }

    public static Bitmap doHighlightImage(Bitmap paramBitmap)
    {
        Bitmap localBitmap = Bitmap.createBitmap(paramBitmap.getWidth() + 96, paramBitmap.getHeight() + 96, Bitmap.Config.ARGB_8888);
        Canvas localCanvas = new Canvas(localBitmap);
        localCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        Object localObject = new Paint();
        ((Paint)localObject).setMaskFilter(new BlurMaskFilter(15.0F, BlurMaskFilter.Blur.NORMAL));
        int[] arrayOfInt = new int[2];
        localObject = paramBitmap.extractAlpha((Paint)localObject, arrayOfInt);
        Paint localPaint = new Paint();
        localPaint.setColor(-1);
        localCanvas.drawBitmap((Bitmap)localObject, arrayOfInt[0], arrayOfInt[1], localPaint);
        ((Bitmap)localObject).recycle();
        localCanvas.drawBitmap(paramBitmap, 0.0F, 0.0F, null);
        return localBitmap;
    }

    public static Bitmap tintImage(Bitmap src, int degree)
    {
        int width = src.getWidth();
        int height = src.getHeight();

        int[] pix = new int[width * height];
        src.getPixels(pix, 0, width, 0, 0, width, height);

        int RY, GY, BY, RYY, GYY, BYY, R, G, B, Y;
        double angle = (PI * (double)degree) / HALF_CIRCLE_DEGREE;

        int S = (int)(RANGE * Math.sin(angle));
        int C = (int)(RANGE * Math.cos(angle));

        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++) {
                int index = y * width + x;
                int r = ( pix[index] >> 16 ) & 0xff;
                int g = ( pix[index] >> 8 ) & 0xff;
                int b = pix[index] & 0xff;
                RY = ( 70 * r - 59 * g - 11 * b ) / 100;
                GY = (-30 * r + 41 * g - 11 * b ) / 100;
                BY = (-30 * r - 59 * g + 89 * b ) / 100;
                Y  = ( 30 * r + 59 * g + 11 * b ) / 100;
                RYY = ( S * BY + C * RY ) / 256;
                BYY = ( C * BY - S * RY ) / 256;
                GYY = (-51 * RYY - 19 * BYY ) / 100;
                R = Y + RYY;
                R = ( R < 0 ) ? 0 : (( R > 255 ) ? 255 : R );
                G = Y + GYY;
                G = ( G < 0 ) ? 0 : (( G > 255 ) ? 255 : G );
                B = Y + BYY;
                B = ( B < 0 ) ? 0 : (( B > 255 ) ? 255 : B );
                pix[index] = 0xff000000 | (R << 16) | (G << 8 ) | B;
            }

        Bitmap outBitmap = Bitmap.createBitmap(width, height, src.getConfig());
        outBitmap.setPixels(pix, 0, width, 0, 0, width, height);

        pix = null;

        return outBitmap;
    }

    public static Bitmap applySnowEffect(Bitmap source) {
        // get image size
        int width = source.getWidth();
        int height = source.getHeight();
        int[] pixels = new int[width * height];
        // get pixel array from source
        source.getPixels(pixels, 0, width, 0, 0, width, height);
        // random object
        Random random = new Random();

        int R, G, B, index = 0, thresHold = 50;
        // iteration through pixels
        for(int y = 0; y < height; ++y) {
            for(int x = 0; x < width; ++x) {
                // get current index in 2D-matrix
                index = y * width + x;
                // get color
                R = Color.red(pixels[index]);
                G = Color.green(pixels[index]);
                B = Color.blue(pixels[index]);
                // generate threshold
                thresHold = random.nextInt(COLOR_MAX);
                if(R > thresHold && G > thresHold && B > thresHold) {
                    pixels[index] = Color.rgb(COLOR_MAX, COLOR_MAX, COLOR_MAX);
                }
            }
        }
        // output bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        bmOut.setPixels(pixels, 0, width, 0, 0, width, height);
        return bmOut;
    }

    public Bitmap fastblur(Bitmap sentBitmap, int radius) {

        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16)
                        | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }

    public static Bitmap doGreyscale(Bitmap src)
    {
        int width, height;
        height = src.getHeight();
        width = src.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(src, 0, 0, paint);
        return bmpGrayscale;
    }

    public Bitmap createInvertedBitmap(Bitmap src)
    {
        ColorMatrix colorMatrix_Inverted =
                new ColorMatrix(new float[] {
                        -1,  0,  0,  0, 255,
                        0, -1,  0,  0, 255,
                        0,  0, -1,  0, 255,
                        0,  0,  0,  1,   0});

        ColorFilter ColorFilter_Sepia = new ColorMatrixColorFilter(
                colorMatrix_Inverted);

        Bitmap bitmap = Bitmap.createBitmap(src.getWidth(), src.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint();

        paint.setColorFilter(ColorFilter_Sepia);
        canvas.drawBitmap(src, 0, 0, paint);

        return bitmap;
    }

    public static Bitmap applyGaussianBlur(Bitmap src) {
        double[][] GaussianBlurConfig = new double[][] {
                { 1, 2, 1 },
                { 2, 4, 2 },
                { 1, 2, 1 }
        };
        ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);
        convMatrix.applyConfig(GaussianBlurConfig);
        convMatrix.Factor = 16;
        convMatrix.Offset = 0;
        return ConvolutionMatrix.computeConvolution3x3(src, convMatrix);
    }

    public Bitmap ColorDodgeBlend(Bitmap source, Bitmap layer)
    {
        Bitmap base = source.copy(Bitmap.Config.ARGB_8888, true);
        Bitmap blend = layer.copy(Bitmap.Config.ARGB_8888, false);

        IntBuffer buffBase = IntBuffer.allocate(base.getWidth() * base.getHeight());
        base.copyPixelsToBuffer(buffBase);
        buffBase.rewind();

        IntBuffer buffBlend = IntBuffer.allocate(blend.getWidth() * blend.getHeight());
        blend.copyPixelsToBuffer(buffBlend);
        buffBlend.rewind();

        IntBuffer buffOut = IntBuffer.allocate(base.getWidth() * base.getHeight());
        buffOut.rewind();

        while (buffOut.position() < buffOut.limit()) {
            int filterInt = buffBlend.get();
            int srcInt = buffBase.get();

            int redValueFilter = Color.red(filterInt);
            int greenValueFilter = Color.green(filterInt);
            int blueValueFilter = Color.blue(filterInt);

            int redValueSrc = Color.red(srcInt);
            int greenValueSrc = Color.green(srcInt);
            int blueValueSrc = Color.blue(srcInt);

            int redValueFinal = colordodge(redValueFilter, redValueSrc);
            int greenValueFinal = colordodge(greenValueFilter, greenValueSrc);
            int blueValueFinal = colordodge(blueValueFilter, blueValueSrc);

            int pixel = Color.argb(255, redValueFinal, greenValueFinal, blueValueFinal);

            buffOut.put(pixel);
        }

        buffOut.rewind();

        base.copyPixelsFromBuffer(buffOut);
        blend.recycle();

        return base;
    }

    private int colordodge(int in1, int in2) {
        float image = (float)in2;
        float mask = (float)in1;
        return ((int) ((image == 255) ? image: Math.min(255, (((long)mask << 8 ) / (255 - image)))));

    }

    public Bitmap getCartoonizedBitmap(Bitmap realBitmap, Bitmap dodgeBlendBitmap, int hueIntervalSize, int saturationIntervalSize, int valueIntervalSize, int saturationPercent, int valuePercent) {
        // Bitmap bitmap = Bitmap.createBitmap(scaledBitmap);
        // //fastblur(scaledBitmap, 4);
        Bitmap base = fastblur(realBitmap, 3).copy(Bitmap.Config.ARGB_8888, true);
        Bitmap dodge = dodgeBlendBitmap.copy(Bitmap.Config.ARGB_8888, false);
        try {
            int realColor;
            int color;
            float top = 0.87f; //Between 0.0f .. 1.0f I use 0.87f
            IntBuffer templatePixels = IntBuffer.allocate(dodge.getWidth()
                    * dodge.getHeight());
            IntBuffer scaledPixels = IntBuffer.allocate(base.getWidth()
                    * base.getHeight());
            IntBuffer buffOut = IntBuffer.allocate(base.getWidth()
                    * base.getHeight());

            base.copyPixelsToBuffer(scaledPixels);
            dodge.copyPixelsToBuffer(templatePixels);

            templatePixels.rewind();
            scaledPixels.rewind();
            buffOut.rewind();

            while (buffOut.position() < buffOut.limit()) {
                color = (templatePixels.get());
                realColor = scaledPixels.get();

                float[] realHSV = new float[3];
                Color.colorToHSV(realColor, realHSV);

                realHSV[0] = getRoundedValue(realHSV[0], hueIntervalSize);

                realHSV[2] = (getRoundedValue(realHSV[2] * 100,
                        valueIntervalSize) / 100) * (valuePercent / 100);
                realHSV[2] = realHSV[2]<1.0?realHSV[2]:1.0f;

                realHSV[1] = realHSV[1] * (saturationPercent / 100);
                realHSV[1] = realHSV[1]<1.0?realHSV[1]:1.0f;

                float[] HSV = new float[3];
                Color.colorToHSV(color, HSV);

                boolean putBlackPixel = HSV[2] <= top;

                realColor = Color.HSVToColor(realHSV);

                if (putBlackPixel) {
                    buffOut.put(color);
                } else {
                    buffOut.put(realColor);
                }
            }// END WHILE
            dodge.recycle();
            buffOut.rewind();
            base.copyPixelsFromBuffer(buffOut);

        } catch (Exception e) {
            // TODO: handle exception
        }

        return base;
    }

    public static float getRoundedValue(float value, int intervalSize) {
        float result = Math.round(value);
        int mod = ((int) result) % intervalSize;
        result += mod < (intervalSize / 2) ? -mod : intervalSize - mod;
        return result;

    }


    public static final Bitmap changeToSketch(Bitmap src, int type, int threshold) {
        int width = src.getWidth();
        int height = src.getHeight();
        Bitmap result = Bitmap.createBitmap(width, height, src.getConfig());

        int A, R, G, B;
        int sumR, sumG, sumB;
        int[][] pixels = new int[3][3];
        for(int y = 0; y < height - 2; ++y) {
            for(int x = 0; x < width - 2; ++x) {
                //      get pixel matrix
                for(int i = 0; i < 3; ++i) {
                    for(int j = 0; j < 3; ++j) {
                        pixels[i][j] = src.getPixel(x + i, y + j);
                    }
                }
                // get alpha of center pixel
                A = Color.alpha(pixels[1][1]);
                // init color sum
                sumR = sumG = sumB = 0;
                sumR = (type* Color.red(pixels[1][1])) - Color.red(pixels[0][0]) - Color.red(pixels[0][2]) - Color.red(pixels[2][0]) - Color.red(pixels[2][2]);
                sumG = (type* Color.green(pixels[1][1])) - Color.green(pixels[0][0]) - Color.green(pixels[0][2]) - Color.green(pixels[2][0]) - Color.green(pixels[2][2]);
                sumB = (type* Color.blue(pixels[1][1])) - Color.blue(pixels[0][0]) - Color.blue(pixels[0][2]) - Color.blue(pixels[2][0]) - Color.blue(pixels[2][2]);
                // get final Red
                R = (int)(sumR  + threshold);
                if(R < 0) { R = 0; }
                else if(R > 255) { R = 255; }
                // get final Green
                G = (int)(sumG  + threshold);
                if(G < 0) { G = 0; }
                else if(G > 255) { G = 255; }
                // get final Blue
                B = (int)(sumB  + threshold);
                if(B < 0) { B = 0; }
                else if(B > 255) { B = 255; }

                result.setPixel(x + 1, y + 1, Color.argb(A, R, G, B));
            }
        }
        return result;
    }
}
