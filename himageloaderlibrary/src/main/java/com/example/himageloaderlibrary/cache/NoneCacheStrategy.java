package com.example.himageloaderlibrary.cache;

import android.graphics.Bitmap;

import com.example.himageloaderlibrary.compress.HCompressStrategy;

public class NoneCacheStrategy implements HCacheStrategy{

    private static NoneCacheStrategy noneCacheInstance;
    private NoneCacheStrategy(){}

    /**
     * 单例模式获取对象
     * @return
     */
    public static NoneCacheStrategy getInstance(){
        if (noneCacheInstance==null){
            return new NoneCacheStrategy();
        }else {
            return noneCacheInstance;
        }
    }
    @Override
    public void restore(String addr, Bitmap bitmap, HCompressStrategy compressStrategy, HCompressStrategy.CompressOptions options) {

    }

    @Override
    public Bitmap fetch(String addr, HCompressStrategy.CompressOptions options) {
        return null;
    }
}
