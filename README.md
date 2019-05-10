# Ark_Ads
聚合广告sdk
## 使用方法
### Gradle   version 1.x (GDTUnionSDK<4.20,ZhaoCai_Ad_SDK<3.0)
```groovy
maven { url 'http://nexus.xiaoc.cn/repository/maven-releases/'}

implementation 'com.ark.ads:basics:1.2.3'(必须,广告基础库)
implementation 'com.ark.ads:core:1.2.3'(必须，广告聚合处理)
implementation 'com.ark.ads:iflytek:1.2.3'(科大讯飞广告)
implementation 'com.ark.ads:longyun:1.2.3'(龙云聚合广告，包含GDTUnionSDK.4.19.574.min.jar)
implementation 'com.ark.ads:zhaocai:1.2.3'(无双科技广告,已去除广点通依赖)
implementation 'com.ark.ads:gdt:1.2.3'(广点通单独依赖时需要拷贝GDTUnionSDK.4.19.574.min.jar)
implementation 'com.ark.ads:ttad:1.2.3'(今日头条穿山甲广告)
```

### Gradle   version 2.x (GDTUnionSDK>=4.20,ZhaoCai_Ad_SDK>=3.0)
```groovy
implementation 'com.ark.ads:basics:2.0.0'(必须,广告基础库)
implementation 'com.ark.ads:core:2.0.0'(必须，广告聚合处理)
implementation 'com.ark.ads:iflytek:2.0.0'(科大讯飞广告)
implementation 'com.ark.ads:longyun:2.0.0'(龙云聚合广告，包含GDTUnionSDK.4.24.894.min.jar)
implementation 'com.ark.ads:zhaocai:2.0.0'(无双科技广告,已去除广点通依赖)
implementation 'com.ark.ads:gdt:2.0.0'(广点通单独依赖时需要拷贝GDTUnionSDK.4.24.894.min.jar)
implementation 'com.ark.ads:ttad:2.0.0'(今日头条穿山甲广告)
```

### 初始化SDK，非必须
```java
ADTool.initialize(new ADTool.Builder()
        //.setStrategy(Strategy.cycle)//按sort轮流排序策略
        //.setLoadOtherWhenVideoDisable(true)//视频广告失败后尝试其他原生横图广告填充
        //.setLocalConfig(JsonUtils.getJson(this,"localconfig.json"))//使用本地json字符串配置
        .setDebugMode(BuildConfig.DEBUG)//调试模式（日志打印，平台标识）
        .build());
```
#### 广告使用
##### 1.开屏
```java
/**
 * 加载开屏广告
 * @param adContainer  广告展示容器
 * @param rootView     跳过按钮所在父容器(根容器，最好和广告容器分开(不要使用LinearLayout，否则跳过按钮可能不可见)
 * @param onSplashImpl 开屏回调
 */
 public void loadSplash(ViewGroup adContainer, ViewGroup rootView, OnSplashImpl onSplashImpl) {
    ADTool.getADTool().getManager()
            .getSplashWrapper()
            .needPermissions(true)//是否由SDK申请必要权限
            .setPermissions(list)//自定义权限列表
            .loadSplash(activity, adContainer, skipViewGroup, new OnSplashImpl() {

                    @Override
                    public void onAdTimeTick(long tick) {

                    }

                    @Override
                    public void onAdShouldLaunch() {

                    }
            });
 }
```
##### 2.原生信息流
```java
ADTool.getADTool().getManager()
        .getWrapper()
        .loadBannerView(context, adContainer);
        
ADTool.getADTool().getManager()
        .getNativeWrapper()
        .loadNativeView(context, adContainer);
        
ADTool.getADTool().getManager()
        .getNativeWrapper()
        .loadSmallNativeView(context, adContainer);
        
ADTool.getADTool().getManager()
        .getNativeWrapper()
        .loadVideoView(context, adContainer);
```
##### 3.配置
```java
Map<String,String> mConfig= ADTool.getADTool()
        .getManager()
        .getConfigWrapper()
        .getConfig();
        
boolean hasAd=ADTool.getADTool()
        .getManager()
        .getConfigWrapper()
        .hasAd();
```
##### 4.交叉推广
```java
new SelfNativeAD(this, SelfADStyle.INTER_RECOMMEND)
        .setListener(new SelfNativeAD.ADListener() {
                @Override
                public void onAdLoad(List<SelfDataRef> dataRefs) {
                        if (dataRefs != null && dataRefs.size() > 0) {
                            SelfDataRef selfDataRef = dataRefs.get(0);
                            RecommendDialog recommendDialog = new RecommendDialog(
                                    context);
                            recommendDialog.setBundle(selfDataRef);
                            recommendDialog.show();
                        }
                    }

                @Override
                public void onAdFailed(int errorCode, @NonNull String errorMsg) {

                }
        }).loadAllADList();
```
##### 5.可能出现的冲突解决
```xml
<activity
            android:name="com.baidu.mobads.AppActivity"
            tools:replace="android:configChanges"
            android:configChanges="keyboard|keyboardHidden|orientation"/>
```
