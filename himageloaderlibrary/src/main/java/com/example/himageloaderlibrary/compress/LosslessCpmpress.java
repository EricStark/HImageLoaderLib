package com.example.himageloaderlibrary.compress;

import android.graphics.Bitmap;

public class LosslessCpmpress extends HCompressStrategy{

    private static LosslessCpmpress losslessCpmpressInstance;
    private LosslessCpmpress(){}

    /**
     * 单例模式获取对象
     * @return
     */
    public static LosslessCpmpress getInstance(){
        if (losslessCpmpressInstance==null){
            return new LosslessCpmpress();
        }else {
            return losslessCpmpressInstance;
        }
    }
    @Override
    public Bitmap compress(Bitmap bitmap, CompressOptions options) {
        return scale(bitmap,options.width,options.heigth);
    }
    private Bitmap scale(Bitmap bitmap,int outWidth,int outHeigth){
        //获得图片的原始宽高
        int srcWidth = bitmap.getWidth();
        int srcHeigth = bitmap.getHeight();
        //检查参数有效性
        if ((outWidth>srcWidth && outHeigth>srcHeigth)||(outHeigth<0 && outWidth<0)){
            return bitmap;
        }
        //使得压缩后图片比例不变
        float srcRatio = srcWidth/srcHeigth;
        float outRatio = outWidth/outHeigth;
        if (outRatio<srcRatio){
            outHeigth = (int) (outWidth / srcRatio);
        }else if (outRatio > srcRatio){
            outWidth = (int) (outHeigth * srcRatio);
        }
        return Bitmap.createScaledBitmap(bitmap,outWidth,outHeigth,true);
    }
}
