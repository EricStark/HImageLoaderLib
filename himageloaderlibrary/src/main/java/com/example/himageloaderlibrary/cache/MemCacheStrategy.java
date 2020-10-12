package com.example.himageloaderlibrary.cache;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.example.himageloaderlibrary.compress.HCompressStrategy;

public class MemCacheStrategy implements HCacheStrategy{

    //使用Lru缓存策略
    private LruCache<String,Bitmap> lruCache;
    private static MemCacheStrategy memCacheInstance;
    private MemCacheStrategy(){
        //获取最大内存
        int maxMem = (int) (Runtime.getRuntime().maxMemory()/1024);
        //计算缓存区大小，一般为最大内存的1/8
        int cacheSize = maxMem/8;
        lruCache = new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes()*value.getHeight()/1024;
            }
        };
    }

    /**
     * 单例模式获取对象
     * @return
     */
    public static MemCacheStrategy getInstance(){
        if (memCacheInstance==null){
            return new MemCacheStrategy();
        }else {
            return memCacheInstance;
        }
    }
    @Override
    public void restore(String addr, Bitmap bitmap, HCompressStrategy compressStrategy, HCompressStrategy.CompressOptions options) {
        lruCache.put(addr,compressStrategy.compress(bitmap,options));
    }

    @Override
    public Bitmap fetch(String addr, HCompressStrategy.CompressOptions options) {
        return lruCache.get(addr);
    }
}
