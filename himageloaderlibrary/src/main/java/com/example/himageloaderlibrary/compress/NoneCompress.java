package com.example.himageloaderlibrary.compress;

import android.graphics.Bitmap;

public class NoneCompress extends HCompressStrategy{

    private static NoneCompress noneCompressInstance;
    private NoneCompress(){}
    public static NoneCompress getInstance(){
        if (noneCompressInstance==null){
            return new NoneCompress();
        }else {
            return noneCompressInstance;
        }
    }
    @Override
    public Bitmap compress(Bitmap bitmap, CompressOptions options) {
        return bitmap;
    }
}
