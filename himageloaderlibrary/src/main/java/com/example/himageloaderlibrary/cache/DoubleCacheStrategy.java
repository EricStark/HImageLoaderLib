package com.example.himageloaderlibrary.cache;

import android.graphics.Bitmap;

import com.example.himageloaderlibrary.compress.HCompressStrategy;
import com.example.himageloaderlibrary.compress.NoneCompress;

public class DoubleCacheStrategy implements HCacheStrategy{

    private MemCacheStrategy memCacheStrategy = MemCacheStrategy.getInstance();
    private DiskCacheStrategy diskCacheStrategy = DiskCacheStrategy.getInstance();
    private static DoubleCacheStrategy doubleCacheInstance;
    private DoubleCacheStrategy(){}

    /**
     * 单例获取实例
     * @return
     */
    public static DoubleCacheStrategy getInstance(){
        if (doubleCacheInstance==null){
            return new DoubleCacheStrategy();
        }else {
            return doubleCacheInstance;
        }
    }
    public void setCacheDir(String dir){
        diskCacheStrategy.setmCacheDir(dir);
    }
    @Override
    public void restore(String addr, Bitmap bitmap, HCompressStrategy compressStrategy, HCompressStrategy.CompressOptions options) {
        memCacheStrategy.restore(addr,bitmap,compressStrategy,options);
        diskCacheStrategy.restore(addr,bitmap,compressStrategy,options);
    }

    @Override
    public Bitmap fetch(String addr, HCompressStrategy.CompressOptions options) {
        Bitmap bitmap = memCacheStrategy.fetch(addr,options);
        if (bitmap==null){
            bitmap = diskCacheStrategy.fetch(addr,options);
            if (bitmap!=null){
                memCacheStrategy.restore(addr,bitmap, NoneCompress.getInstance(),options);
            }
        }
        return bitmap;
    }
}
