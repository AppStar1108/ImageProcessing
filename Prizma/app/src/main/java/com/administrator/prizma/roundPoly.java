package com.administrator.prizma;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;

/**
 * Created by xyzxqs(xyzxqs@gmail.com) on 9/21/sixteen_cat.
 */

public class roundPoly {
    private static final String TAG = "LowPoly";

    private roundPoly() {
        throw new IllegalStateException("no instance!");
    }

    /**
     * generate lowpoly picture
     *
     * @param input             src Bitmap
     * @param alphaOrPointCount (0.0, 1) or [1, max)
     * @param fill              fill ? Paint.Style.FILL : Paint.Style.STROKE
     * @return out Bitmap
     */
    public static Bitmap roundPoly(Bitmap input, float alphaOrPointCount, boolean fill) {
        return generate(input, 50, alphaOrPointCount, true, fill);
    }

    /**
     * generate sandpainting
     *
     * @param input             src Bitmap
     * @param threshold         recommend（30, 90）
     * @param alphaOrPointCount (0.0, 1) or [1, max)
     * @return out Bitmap
     */
    public static Bitmap sandPainting(Bitmap input, int threshold, float alphaOrPointCount) {
        return generate(input, threshold, alphaOrPointCount, false, false);
    }


    public static Bitmap generate(Bitmap input, int threshold, float alphaOrPointCount, boolean lowPoly, boolean fill) {
        int width = input.getWidth();
        int height = input.getHeight();

        Bitmap newImage = Bitmap.createBitmap(input.getWidth(), input.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newImage);
        Paint paint = new Paint();
        paint.setAntiAlias(false);
        paint.setStyle(fill ? Paint.Style.FILL : Paint.Style.STROKE);
        int x1, x2, x3, y1, y2, y3;
        int pixels[] = new int[width * height];
        input.getPixels(pixels, 0, width, 0, 0, width, height);
        if (!lowPoly) {
            Log.w(TAG, "when lowPoly = false, ignore fill boolean.");
        }


        int[] triangles = getTriangles(pixels, width, height, threshold, alphaOrPointCount, lowPoly);

        if (lowPoly) {
            Path path = new Path();
            for (int i = 0; i + 5 < triangles.length; i = i + 6) {
                x1 = triangles[i];
                y1 = triangles[i + 1];
                x2 = triangles[i + 2];
                y2 = triangles[i + 3];
                x3 = triangles[i + 4];
                y3 = triangles[i + 5];

                int color = input.getPixel((x1 + x2 + x3) / 3, (y1 + y2 + y3) / 3);
                path.rewind();
                path.moveTo(x1, y1);
                path.lineTo(x2, y2);
                path.lineTo(x3, y3);
                path.close();
                paint.setColor(color);
                canvas.drawPath(path, paint);
            }
        } else {
            for (int i = 0, n = triangles.length; i + 1 < n; i += 2) {
                x1 = triangles[i];
                y1 = triangles[i + 1];
                int color = input.getPixel(x1, y1);
                paint.setColor(color);
                canvas.drawCircle(x1, y1, 1, paint);
            }
        }
        return newImage;
    }

    static {
        System.loadLibrary("lowpoly-lib");
    }


    private static native int[] getTriangles(int[] pixels, int width, int height, int threshold, float alpha_count, boolean lowPoly);
}
