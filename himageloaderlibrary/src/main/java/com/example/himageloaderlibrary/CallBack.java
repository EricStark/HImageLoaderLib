package com.example.himageloaderlibrary;

import android.graphics.Bitmap;

public interface CallBack {
    /**
     * 图片展示成功回调
     * @param bitmap
     */
    void success(Bitmap bitmap);

    /**
     * 图片展示失败回调
     * @param e
     */
    void failed(Exception e);
}
