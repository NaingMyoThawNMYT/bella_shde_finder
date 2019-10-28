package com.example.bellashdefinder.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapUtil {

    public static Bitmap byteArrayToBitmap(byte[] bytes) {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
