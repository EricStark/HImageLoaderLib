# HImageLoaderLib
# HImageLoader

这是一个图片加载、缓存及压缩库

## How to use in Activity：

```
        /**
         * 设置参数 得到config对象
         */
        RequestConfig config = new RequestConfig.Builder(this)
                .from("https://upload-images.jianshu.io/upload_images/2441545-6aeb5a737351879c.png?imageMogr2/auto-orient/strip|imageView2/2/w/1200/format/webp")  //设置图片地址
                .error(R.drawable.bu)  //设置加载失败时的占位图
                .placeHolder(R.drawable.bu)  //设置加载未完成前的占位图
                .compressionStrategy(LosslessCpmpress.getInstance()) //设置图片压缩策略
                .size(200, 200) //图片大小
                .cacheStrategy(DoubleCacheStrategy.getInstance())   //设置图片缓存策略
                .cacheDir("/sdcard/cache")  //设置缓存目录
                .quality(100)   //设置图片质量（1-100）
                .scaleType(ImageView.ScaleType.CENTER_CROP)
                .build();  //设置图片的scaleType.build();
        //图片展示的imageview
        imageView = findViewById(R.id.iv);
        //展示图片
        HImageLoader.displayImage(imageView,config);
```

## Disadvantage:

在没有设置缓存策略时也不能使用压缩策略

## How to update：

1.修改程序存在的Bug  
2.完善库功能  
3.解决库与Activity生命周期同步问题，当Activity异常退出时，能够检测到并取消相关工作  
## How to add dependencies
To get a Git project into your build:

### Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:
```

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
### Step 2. Add the dependency
```

	dependencies {
	        implementation 'com.github.EricStark:HImageLoaderLib:v1.0.0'
	}
```
Share this release:

TweetLink
That's it! The first time you request a project JitPack checks out the code, builds it and serves the build artifacts (jar, aar).

If the project doesn't have any GitHub Releases you can use the short commit hash or 'master-SNAPSHOT' as the version.
