package com.example.himageloaderlibrary.compress;

import android.graphics.Bitmap;
import android.widget.ImageView;

public abstract class HCompressStrategy {
    /**
     * 压缩图片方法，实现由其子类实现
     * @param bitmap 待压缩的图片
     * @param options 压缩参数
     * @return 压缩后的图片
     */
    public abstract Bitmap compress(Bitmap bitmap,CompressOptions options);

    /**
     * 压缩图参数类
     */
    public static class CompressOptions{
        //压缩后图片的宽高
        public int width = -1;
        public int heigth = -1;
        //压缩后图片的质量
        public int quality = 100;
        //图片的缩放类型
        public ImageView.ScaleType scaleType = ImageView.ScaleType.CENTER_CROP;
    }
}
