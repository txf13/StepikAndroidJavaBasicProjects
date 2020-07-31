package com.example.getloginpassapp.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class PhotosUtils {

    public static Bitmap getScaledBitmpap(String path, int destWidth, int destHeigth){

        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(path, options);

        float srcWidth=options.outWidth;
        float srcHeigth=options.outHeight;

        int inSampleSize=1;

        if(srcHeigth>destHeigth || srcWidth>destWidth) {
            if (srcWidth > srcHeigth) {
                inSampleSize = Math.round(srcHeigth / destHeigth);
            } else {
                inSampleSize = Math.round(srcWidth / destWidth);
            }
        }

        options=new BitmapFactory.Options();
        options.inSampleSize=inSampleSize;
        return BitmapFactory.decodeFile(path, options);
    }
}
