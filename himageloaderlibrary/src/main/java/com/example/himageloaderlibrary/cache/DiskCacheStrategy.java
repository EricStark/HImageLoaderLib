package com.example.himageloaderlibrary.cache;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.himageloaderlibrary.compress.HCompressStrategy;
import com.example.himageloaderlibrary.utils.IEncode;
import com.example.himageloaderlibrary.utils.MD5;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class DiskCacheStrategy implements HCacheStrategy {

    //采用MD5加密
    private IEncode iEncode = new MD5();
    private String mCacheDir;
    private static DiskCacheStrategy diskCacheInstance;
    private DiskCacheStrategy(){}

    /**
     * 单例模式获得实例
     * @return
     */
    public static DiskCacheStrategy getInstance(){
        if (diskCacheInstance==null){
            diskCacheInstance = new DiskCacheStrategy();
            return diskCacheInstance;
        }else {
            return diskCacheInstance;
        }
    }
    @Override
    public void restore(String addr, Bitmap bitmap, HCompressStrategy compressStrategy, HCompressStrategy.CompressOptions options) {
        FileOutputStream fos = null;
        try {
            //获得输出流
            fos = new FileOutputStream(mCacheDir+iEncode.encode(addr));
            //对图片进行压缩
            compressStrategy.compress(bitmap,options)
                    .compress(Bitmap.CompressFormat.JPEG,100,fos);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Bitmap fetch(String addr, HCompressStrategy.CompressOptions options) {
        return BitmapFactory.decodeFile(mCacheDir+iEncode.encode(addr));
    }
    public void setmCacheDir(String dir){
        mCacheDir = dir.endsWith("/")? dir : dir + "/";
    }
}
