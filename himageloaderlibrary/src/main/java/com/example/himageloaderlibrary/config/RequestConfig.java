package com.example.himageloaderlibrary.config;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.example.himageloaderlibrary.cache.DiskCacheStrategy;
import com.example.himageloaderlibrary.cache.DoubleCacheStrategy;
import com.example.himageloaderlibrary.cache.HCacheStrategy;
import com.example.himageloaderlibrary.compress.HCompressStrategy;
import com.example.himageloaderlibrary.compress.LosslessCpmpress;

import java.io.File;
import java.net.URL;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;

public class RequestConfig {
    private Context mContext;
    private String mAddr;
    private HCacheStrategy mCacheStrategy;
    private HCompressStrategy mCompressStrategy;
    private HCompressStrategy.CompressOptions mCompressOptions;
    private Drawable mPlaceHoder;
    private Drawable mErrorHolder;

    public Context getmContext() {
        return mContext;
    }

    public String getmAddr() {
        return mAddr;
    }

    public HCacheStrategy getmCacheStrategy() {
        return mCacheStrategy;
    }

    public HCompressStrategy getmCompressStrategy() {
        return mCompressStrategy;
    }

    public HCompressStrategy.CompressOptions getmCompressOptions() {
        return mCompressOptions;
    }

    public Drawable getmPlaceHoder() {
        return mPlaceHoder;
    }

    public Drawable getmErrorHolder() {
        return mErrorHolder;
    }
    public static class Builder{
        private RequestConfig mRequestConfig = new RequestConfig();
        //缓存目录
        private String mCacheDir;
        public Builder(Context context){
            mRequestConfig.mContext = context;
            mRequestConfig.mCompressOptions = new HCompressStrategy.CompressOptions();
        }
        public RequestConfig build(){
            checkConfig();
            return mRequestConfig;
        }

        private void checkConfig() {
            HCacheStrategy cacheStrategy = mRequestConfig.getmCacheStrategy();
            if (cacheStrategy==null){
                mRequestConfig.mCacheStrategy = DoubleCacheStrategy.getInstance();
            }
            HCompressStrategy compressStrategy = mRequestConfig.getmCompressStrategy();
            if (compressStrategy==null){
                mRequestConfig.mCompressStrategy = LosslessCpmpress.getInstance();
            }
            if (cacheStrategy instanceof DiskCacheStrategy){
                if (mCacheDir==null){
                    mCacheDir = mRequestConfig.mContext.getExternalCacheDir().getPath();
                    ((DiskCacheStrategy) cacheStrategy).setmCacheDir(mCacheDir);
                }
            }else if (cacheStrategy instanceof DoubleCacheStrategy){
                if (mCacheDir==null){
                    mCacheDir = mRequestConfig.mContext.getExternalCacheDir().getPath();
                    ((DoubleCacheStrategy) cacheStrategy).setCacheDir(mCacheDir);
                }
            }
        }
        /**
         * 设置图片加载错误时的占位图
         *
         * @param drawable 图片加载错误时的占位图
         */
        public Builder error(Drawable drawable) {
            mRequestConfig.mErrorHolder = drawable;
            return this;
        }

        /**
         * 设置图片加载错误时的占位图
         *
         * @param id 图片加载错误时的占位图的资源ID
         */
        public Builder error(@DrawableRes int id) {
            error(mRequestConfig.mContext.getResources().getDrawable(id));
            return this;
        }

        /**
         * 设置图片未加载完成时的占位图
         *
         * @param drawable 图片未加载完成时的占位图
         */
        public Builder placeHolder(Drawable drawable) {
            mRequestConfig.mPlaceHoder = drawable;
            return this;
        }

        /**
         * 设置图片未加载完成时的占位图
         *
         * @param id 图片未加载完成时的占位图的资源ID
         */
        public Builder placeHolder(@DrawableRes int id) {
            placeHolder(mRequestConfig.mContext.getResources().getDrawable(id));
            return this;
        }

        /**
         * 设置图片的缓存策略
         * 提供了四种缓存策略，分别为：
         * NoneCacheStrategy（不使用缓存）；
         * MemoryCacheStrategy（内存缓存）；
         * DiskCacheStrategy（SD卡缓存）；
         * DoubleCacheStrategy（内存与SD卡双缓存）【默认】。
         * 如果以上需求不能满足需求，可通过实现JCacheStrategy接口自定义缓存策略
         *
         * @param cacheStrategy 缓存策略
         */
        public Builder cacheStrategy(@Nullable HCacheStrategy cacheStrategy) {
            mRequestConfig.mCacheStrategy = cacheStrategy;
            return this;
        }

        /**
         * 设置图片的位置
         *
         * @param url 图片的URL
         */
        public Builder from(URL url) {
            mRequestConfig.mAddr = url.toString();
            return this;
        }

        /**
         * 设置图片的位置
         *
         * @param address 描述图片位置的字符串
         */
        public Builder from(String address) {
            mRequestConfig.mAddr = address;
            return this;
        }

        /**
         * 设置图片的位置
         *
         * @param file 包含图片信息的File对象
         */
        public Builder from(File file) {
            from(file.getPath());
            return this;
        }

        /**
         * 设置图片的缓存目录(有bug暂时关闭)
         *
         * @param cacheDir 图片的缓存目录
         */
        public Builder cacheDir(String cacheDir) {
            mCacheDir = cacheDir;
            return this;
        }

        /**
         * 设置输出的图片的大小，是否生效取决于压缩策略的具体实现
         *
         * @param width  图片的输出宽度
         * @param height 图片的输出高度
         */
        public Builder size(int width, int height) {
            mRequestConfig.mCompressOptions.width = width;
            mRequestConfig.mCompressOptions.heigth = height;
            return this;
        }

        /**
         * 设置ImageView的scaleType属性
         *
         * @param scaleType ImageView的scaleType属性
         */
        public Builder scaleType(ImageView.ScaleType scaleType) {
            mRequestConfig.mCompressOptions.scaleType = scaleType;
            return this;
        }

        /**
         * 设置输出的图片的质量，是否生效取决于压缩策略的具体实现
         *
         * @param quality 图片质量（1-100）
         */
        public Builder quality(int quality) {
            mRequestConfig.mCompressOptions.quality = quality;
            return this;
        }

        /**
         * 设置图片的压缩策略
         * 提供了两种压缩策略，分别为：
         * LosslessCompression（无损压缩策略，即仅参照size（）方法设置的参数按原比例缩放图片）【默认】；
         * NoneCompression（不压缩）。
         * 如果以上策略无法满足需求，可通过继承JCompressStrategy类自定义压缩策略
         *
         * @param compressStrategy 压缩策略
         */
        public Builder compressionStrategy(@Nullable HCompressStrategy compressStrategy) {
            mRequestConfig.mCompressStrategy = compressStrategy;
            return this;
        }
    }
}
