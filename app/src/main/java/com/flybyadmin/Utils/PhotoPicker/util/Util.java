/*
 * Copyright (c) 2016. Ted Park. All Rights Reserved
 */

package com.flybyadmin.Utils.PhotoPicker.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

/**
 * Created by TedPark on 16. 1. 7..
 */
public class Util {


    public static int dpToPx(Context c, int dp) {
        return (int) (dp * c.getResources().getSystem().getDisplayMetrics().density);
    }


    public static void toast(Fragment fragment, String message) {
        toast(fragment.getActivity(), message);
    }

    public static void toast(Context context, String message) {
        if (context == null)
            return;


        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void toast(Context context, String message, int time) {
        if (context == null)
            return;


        Toast.makeText(context, message, time).show();
    }





    /**
     * EXIF정보를 회전각도로 변환하는 메서드
     *
     * @param exifOrientation EXIF 회전각
     * @return 실제 각도
     */
    public static int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    /**
     * 이미지를 회전시킵니다.
     *
     * @param bitmap  비트맵 이미지
     * @param degrees 회전 각도
     * @return 회전된 이미지
     */
    public static Bitmap rotate(Bitmap bitmap, int degrees) {
        if (degrees != 0 && bitmap != null) {
            Matrix m = new Matrix();
            m.setRotate(degrees, (float) bitmap.getWidth() / 2,
                    (float) bitmap.getHeight() / 2);

            try {
                Bitmap converted = Bitmap.createBitmap(bitmap, 0, 0,
                        bitmap.getWidth(), bitmap.getHeight(), m, true);
                if (bitmap != converted) {
                    bitmap.recycle();
                    bitmap = converted;
                }
            } catch (Exception ex) {
            }
        }
        return bitmap;
    }


}
