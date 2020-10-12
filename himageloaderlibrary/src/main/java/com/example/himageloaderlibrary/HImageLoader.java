package com.example.himageloaderlibrary;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.widget.ImageView;

import com.example.himageloaderlibrary.cache.HCacheStrategy;
import com.example.himageloaderlibrary.compress.HCompressStrategy;
import com.example.himageloaderlibrary.config.RequestConfig;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 图片加载工具
 */
public class HImageLoader{
    private static final Handler UI_HANDLER = new Handler();
    private static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors();
    //初始化线程池
    private static ExecutorService mFixedThreadPool = new ThreadPoolExecutor(
            THREAD_COUNT,THREAD_COUNT,0L, TimeUnit.MILLISECONDS,new MyLinkedBlockingQueue<Runnable>());

    /**
     * 展示图片
     * @param imageView 图片展示的ImageView
     * @param requestConfig 图片的配置信息
     */
    public static void displayImage(final ImageView imageView, final RequestConfig requestConfig){
        HCompressStrategy.CompressOptions compressOptions = requestConfig.getmCompressOptions();
        //展示前的展位图
        imageView.setImageDrawable(requestConfig.getmPlaceHoder());
        //缩放类型
        imageView.setScaleType(compressOptions.scaleType);
        if (compressOptions.width<=0||compressOptions.heigth<=0){
            compressOptions.width = imageView.getWidth();
            compressOptions.heigth = imageView.getHeight();
        }
        imageView.setTag(requestConfig.getmAddr());
        //回调
        submitLoadRequest(requestConfig, new CallBack() {
            @Override
            public void success(Bitmap bitmap) {
                imageView.setImageBitmap(bitmap);
            }

            @Override
            public void failed(Exception e) {
                imageView.setImageDrawable(requestConfig.getmErrorHolder());
            }
        });
    }

    /**
     * 提交加载结果
     * @param config
     * @param callBack
     */
    public static void submitLoadRequest(final RequestConfig config, final CallBack callBack){
        mFixedThreadPool.submit(new Runnable() {
            @Override
            public void run() {
                //获得缓存策略
                HCacheStrategy cacheStrategy = config.getmCacheStrategy();
                //读取缓存
                Bitmap bitmap = cacheStrategy.fetch(config.getmAddr(), config.getmCompressOptions());
                if (bitmap!=null){
                    //返回UI线程
                    return2UIThread(callBack,bitmap,null);
                    return;
                }else {
                    //加载
                    try {
                        loadImage(config,callBack);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    private static void loadImage(RequestConfig config, CallBack callBack) throws IOException {
        //判断请求类型
        String addr = config.getmAddr();
        final Bitmap bitmap = addr.startsWith("https")? fromHttp(addr):fromFile(addr);
        if (bitmap==null){
            //返回UI线程
            return2UIThread(callBack,bitmap,new Exception("failed"));
        }else {
            HCompressStrategy compressStrategy = config.getmCompressStrategy();
            HCompressStrategy.CompressOptions compressOptions = config.getmCompressOptions();
            //回到UI线程
            return2UIThread(callBack,compressStrategy.compress(bitmap,compressOptions),null);
            HCacheStrategy cacheStrategy = config.getmCacheStrategy();
            cacheStrategy.restore(addr,bitmap,compressStrategy,compressOptions);
        }
    }

    /**
     * 从文件中获取
     * @param addr
     * @return
     */
    private static Bitmap fromFile(String addr) {
        Bitmap bitmap = BitmapFactory.decodeFile(addr);
        return bitmap;
    }

    /**
     * 从网络中获取
     * @param addr
     * @return
     * @throws IOException
     */
    private static Bitmap fromHttp(String addr) throws IOException {
        URL url = new URL(addr);
        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        final Bitmap bitmap = BitmapFactory.decodeStream(url.openStream());
        connection.disconnect();
        return bitmap;
    }

    /**
     * 返回到UI线程中
     * @param callBack
     * @param bitmap
     * @param e
     */
    private static void return2UIThread(final CallBack callBack, final Bitmap bitmap, final Exception e){
        UI_HANDLER.post(new Runnable() {
            @Override
            public void run() {
                if (e==null)
                    callBack.success(bitmap);
                else
                    callBack.failed(e);
            }
        });
    }
    /**
     * 自定义LinkedBlockingDeque 重写take和poll方法
     * @param <T>
     */
    private static class MyLinkedBlockingQueue<T> extends LinkedBlockingDeque{
        @Override
        public Object take() throws InterruptedException {
            return takeLast();
        }

        @Override
        public Object poll() {
            return pollLast();
        }
    }
}

