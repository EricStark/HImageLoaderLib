package com.example.himageloaderlibrary.cache;

import android.graphics.Bitmap;

import com.example.himageloaderlibrary.compress.HCompressStrategy;

public interface HCacheStrategy {
    void restore(String addr, Bitmap bitmap,
                 HCompressStrategy compressStrategy, HCompressStrategy.CompressOptions options);
    Bitmap fetch(String addr, HCompressStrategy.CompressOptions options);
}
