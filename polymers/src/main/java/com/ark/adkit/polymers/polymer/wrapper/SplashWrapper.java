package com.ark.adkit.polymers.polymer.wrapper;

import android.Manifest;
import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.ark.adkit.basics.configs.ADConfig;
import com.ark.adkit.basics.configs.ADOnlineConfig;
import com.ark.adkit.basics.configs.ADStyle;
import com.ark.adkit.basics.handler.Action;
import com.ark.adkit.basics.handler.Run;
import com.ark.adkit.basics.models.ADSplashModel;
import com.ark.adkit.basics.models.OnSplashImpl;
import com.ark.adkit.basics.utils.LogUtils;
import com.ark.adkit.polymers.R;
import com.ark.adkit.polymers.polymer.ADTool;
import com.ark.adkit.polymers.polymer.factory.ADSplashFactory;
import com.ark.adkit.polymers.polymer.timer.CountDownTimer;
import com.ark.adkit.polymers.polymer.timer.State;
import com.ark.utils.permissions.PermissionChecker;
import com.ark.utils.permissions.PermissionItem;
import com.ark.utils.permissions.PermissionSimpleCallback;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"WeakerAccess", "unused"})
public class SplashWrapper {

    protected List<PermissionItem> mPermissionItemArrayList = new ArrayList<>();
    protected boolean mRequestPermissions = true;
    protected boolean isVipSkipSplash;
    protected long delayMills = 3000, countDownMills = 5000, interval = 500;
    private Map<String, ADSplashModel> adSplashModelMap;
    private WeakReference<Activity> mActivityRef;
    private WeakReference<ViewGroup> mContainerRef;
    private WeakReference<ViewGroup> mRootViewRef;
    private WeakReference<TextView> mTextRef;
    private int index = 0;
    private OnSplashImpl onSplashImpl;
    private CountDownTimer countDownTimer;

    /**
     * 开始倒计时
     *
     * @param lastReadUntilFinish 剩余毫秒
     */
    private void startTimer(long lastReadUntilFinish) {
        countDownTimer = new CountDownTimer(lastReadUntilFinish, interval) {

            @Override
            protected void onTick(long millisUntilFinished) {
                super.onTick(millisUntilFinished);
                int current = (int) (millisUntilFinished / 1000);
                LogUtils.i("timer--->onTick:" + current);
                Activity activity = getValidActivity();
                if (activity == null) {
                    return;
                }
                updateSkipText(current);
                if (onSplashImpl != null) {
                    onSplashImpl.onAdTimeTick(current);
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                LogUtils.i("timeTick--->onFinish");
                Activity activity = getValidActivity();
                if (activity == null) {
                    return;
                }
                if (onSplashImpl != null) {
                    LogUtils.i("倒计时结束跳转");
                    onSplashImpl.onAdShouldLaunch();
                }
            }
        };
        countDownTimer.start();
    }

    public boolean isTimerRunning() {
        return countDownTimer != null && countDownTimer.getState() == State.TIMER_RUNNING;
    }

    public boolean isTimerNotStart() {
        return countDownTimer == null || countDownTimer.getState() == State.TIMER_NOT_START;
    }

    public boolean isTimerPaused() {
        return countDownTimer != null && countDownTimer.getState() == State.TIMER_PAUSED;
    }

    /**
     * 暂停倒计时
     */
    public void pauseTimer() {
        if (isTimerRunning()) {
            LogUtils.i("timer--->pauseTimer");
            countDownTimer.pause();
        }
    }

    /**
     * 继续倒计时
     */
    public void resumeTimer() {
        if (countDownTimer != null
                && countDownTimer.getState() == State.TIMER_PAUSED) {
            LogUtils.i("timer--->resumeTimer");
            countDownTimer.resume();
        }
    }

    /**
     * 销毁倒计时
     */
    public void releaseTimer() {
        if (countDownTimer != null) {
            LogUtils.i("timer--->releaseTimer");
            countDownTimer.cancel();
        }
    }

    /**
     * 配置参数
     *
     * @return ADConfig
     */
    @NonNull
    public final ADConfig getConfig() {
        Map<String, String> appKeyMap = ADTool.getADTool().getManager()
                .getConfigWrapper()
                .getAppKeyMap();
        Map<String, String> subKeyMap = ADTool.getADTool().getManager()
                .getConfigWrapper()
                .getSubKeyMap(ADStyle.POS_SPLASH);
        List<String> pList = new ArrayList<>(ADTool.getADTool().getManager()
                .getConfigWrapper()
                .getSplashSort());
        boolean hasAd = ADTool.getADTool().getManager()
                .getConfigWrapper()
                .hasAd();
        return new ADConfig()
                .setHasAD(hasAd)
                .setPlatformList(pList)
                .setAppKey(appKeyMap)
                .setSubKey(subKeyMap);
    }

    /**
     * 更新倒计时
     *
     * @param tick 倒计时秒
     */
    private void updateSkipText(final long tick) {
        final TextView countTextView = getValidSkipTextView();
        if (countTextView != null) {
            countTextView.post(new Runnable() {
                @Override
                public void run() {
                    countTextView.setText(String.format(countTextView.getContext()
                            .getString(R.string.sdk_ad_cancel), (int) tick));
                }
            });
        }
    }

    /**
     * 取消隐藏自己的倒计时
     */
    private void resetCount() {
        releaseTimer();
        startTimer(countDownMills + interval);
        TextView countTextView = getValidSkipTextView();
        ViewGroup viewGroup = getValidRootView();
        if (countTextView == null && viewGroup != null) {
            mTextRef = new WeakReference<>(addCountTextView(viewGroup));
        }
    }

    /**
     * 获取回调
     *
     * @return OnSplashImpl
     */
    @Nullable
    protected OnSplashImpl getOnSplashImpl() {
        return onSplashImpl;
    }

    /**
     * 获取已经加载过的开屏广告
     *
     * @return Map
     */
    @NonNull
    protected Map<String, ADSplashModel> getModels() {
        return adSplashModelMap == null ? new HashMap<String, ADSplashModel>() : adSplashModelMap;
    }

    /**
     * 获取开屏有效宿主Activity
     *
     * @return Activity
     */
    protected Activity getValidActivity() {
        if (mActivityRef != null) {
            Activity activity = mActivityRef.get();
            if (activity != null && !activity.isFinishing()) {
                return activity;
            }
        }
        return null;
    }

    protected ViewGroup getValidViewGroup() {
        return mContainerRef == null ? null : mContainerRef.get();
    }

    protected ViewGroup getValidRootView() {
        return mRootViewRef == null ? null : mRootViewRef.get();
    }

    protected TextView getValidSkipTextView() {
        return mTextRef == null ? null : mTextRef.get();
    }

    /**
     * 释放已经加载了的开屏广告
     */
    protected void releaseSplashModels() {
        if (adSplashModelMap != null) {
            for (String key : adSplashModelMap.keySet()) {
                ADSplashModel adSplashModel = adSplashModelMap.get(key);
                adSplashModel.release();
            }
        }
    }

    /**
     * 释放引用
     */
    protected void releaseSplashRefs() {
        if (mActivityRef != null) {
            mActivityRef.clear();
            mActivityRef = null;
        }
        if (mRootViewRef != null) {
            mRootViewRef.clear();
            mRootViewRef = null;
        }
        if (mContainerRef != null) {
            mContainerRef.clear();
            mContainerRef = null;
        }
    }

    /**
     * 释放资源
     */
    public void release() {
        releaseSplashModels();
        releaseTimer();
        releaseSplashRefs();
    }

    /**
     * 加载开屏
     *
     * @param mActivity    上下文
     * @param adContainer  容器
     * @param rootView     根容器，最好和广告容器分开(不要使用LinearLayout，否则跳过按钮可能不可见)
     * @param onSplashImpl 监听
     */
    public void loadSplash(@NonNull final Activity mActivity, @NonNull ViewGroup adContainer,
                           @NonNull ViewGroup rootView,
                           @NonNull OnSplashImpl onSplashImpl) {
        if (mActivity instanceof AppCompatActivity) {
            ((AppCompatActivity) mActivity).getLifecycle().addObserver(new LifecycleObserver() {

                @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
                void onResume() {
                    LogUtils.i("onActivityResumed");
                    resumeTimer();
                }

                @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
                void onStop() {
                    LogUtils.i("onActivityStopped");
                }

                @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
                void onPause() {
                    LogUtils.i("onActivityPaused");
                    pauseTimer();
                }

                @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
                void onDestroy() {
                    LogUtils.i("onActivityDestroyed");
                    release();
                    LogUtils.i("splash destroy ,release SplashADs");
                }
            });
        }
        adSplashModelMap = new HashMap<>();
        this.onSplashImpl = onSplashImpl;
        if (rootView instanceof LinearLayout) {
            LogUtils.e("不要使用LinearLayout，否则跳过按钮可能不可见");
        }
        mActivityRef = new WeakReference<>(mActivity);
        mContainerRef = new WeakReference<>(adContainer);
        mRootViewRef = new WeakReference<>(rootView);
        mTextRef = new WeakReference<>(addCountTextView(rootView));
        if (!mRequestPermissions || mPermissionItemArrayList == null) {
            checkPermissionsNext();
            return;
        }
        //如果需要权限但是没有设置，则加入两个默认权限
        if (mPermissionItemArrayList.isEmpty()) {
            mPermissionItemArrayList
                    .add(new PermissionItem(Manifest.permission.WRITE_EXTERNAL_STORAGE, "存储权限"));
            mPermissionItemArrayList
                    .add(new PermissionItem(Manifest.permission.READ_PHONE_STATE, "手机状态"));
        }
        PermissionChecker.create(mActivity)
                .permissions(mPermissionItemArrayList)
                .checkMultiPermission(new PermissionSimpleCallback() {
                    private static final long serialVersionUID = 5170045860554631205L;

                    @Override
                    public void onClose() {
                        Activity validAct = getValidActivity();
                        if (validAct != null) {
                            validAct.finish();
                        }
                    }

                    @Override
                    public void onFinish() {
                        checkPermissionsNext();
                    }
                });
    }

    private void checkPermissionsNext() {
        ADConfig mADConfig = getConfig();
        if (!mADConfig.hasAD()) {
            LogUtils.e("开屏广告已被禁用,请检查在线配置");
            if (onSplashImpl != null) {
                onSplashImpl.onAdDisable();
            }
            return;
        }
        if (mADConfig.size() == 0) {
            if (onSplashImpl != null) {
                onSplashImpl.onAdDisable();
            }
            return;
        }
        //开始按顺序加载开屏
        index = 0;
        if (isVipSkipSplash) {
            startTimer(delayMills + interval);
            return;
        }
        loadOneByOne(mADConfig);
    }

    /**
     * 实际加载开屏操作
     *
     * @param mADConfig 参数配置
     */
    private void loadOneByOne(final @NonNull ADConfig mADConfig) {
        Activity activity = getValidActivity();
        ViewGroup viewGroup = getValidViewGroup();
        if (activity == null || viewGroup == null) {
            return;
        }
        if (index >= mADConfig.size()) {
            if (onSplashImpl != null) {
                onSplashImpl.onAdDisable();
            }
            return;
        }
        final String sortStr = mADConfig.getSortList().get(index);
        index++;
        if (!TextUtils.isEmpty(sortStr)) {
            final ADOnlineConfig adOnlineConfig = new ADOnlineConfig();
            adOnlineConfig.appKey = mADConfig.getAppKey(sortStr);
            adOnlineConfig.subKey = mADConfig.getSubKey(sortStr);
            adOnlineConfig.platform = sortStr;
            adOnlineConfig.adStyle = ADStyle.POS_SPLASH;
            ADSplashModel adSplashModel = ADSplashFactory.createSplash(sortStr);
            getModels().put(sortStr, adSplashModel);
            if (adSplashModel == null) {
                Run.onUiAsync(new Action() {
                    @Override
                    public void call() {
                        loadOneByOne(mADConfig);
                    }
                });
            } else {
                Run.onUiAsync(new Action() {
                    @Override
                    public void call() {
                        splashModelLoad(sortStr, adOnlineConfig, mADConfig);
                    }
                });
            }
        } else {
            loadOneByOne(mADConfig);
        }
    }

    private void splashModelLoad(@NonNull String sortStr,
                                 @NonNull final ADOnlineConfig adOnlineConfig,
                                 @NonNull final ADConfig mADConfig) {
        Activity activity = getValidActivity();
        ViewGroup viewGroup = getValidViewGroup();
        if (activity == null || viewGroup == null) {
            return;
        }
        ADSplashModel adSplashModel;
        if ((adSplashModel = getModels().get(sortStr)) == null) {
            LogUtils.w("splash ad can not load because of released");
            return;
        }
        adSplashModel.initModel(adOnlineConfig);
        adSplashModel.loadSplashAD(activity, viewGroup, new OnSplashImpl() {
            @Override
            public void onAdDisable() {
                super.onAdDisable();
                LogUtils.w("splash disable");
                if (onSplashImpl != null) {
                    onSplashImpl.onAdDisable();
                }
            }

            @Override
            public void onAdTimeTick(long tick) {
                LogUtils.v("splash tick:" + tick);
            }

            @Override
            public void onAdWillLoad(@NonNull String platform) {
                super.onAdWillLoad(platform);
                LogUtils.i("splash willLoad,platform:" + adOnlineConfig.platform + ",appkey=" +
                        adOnlineConfig.appKey + "/subkey=" + adOnlineConfig.subKey);
                resetCount();
                if (onSplashImpl != null) {
                    onSplashImpl.onAdWillLoad(platform);
                }
            }

            @Override
            public void onAdDisplay(@NonNull String platform) {
                super.onAdDisplay(platform);
                LogUtils.w("splash display,platform:" + adOnlineConfig.platform);
                if (onSplashImpl != null) {
                    onSplashImpl.onAdDisplay(platform);
                }
            }

            @Override
            public void onAdClicked(@NonNull String platform) {
                super.onAdClicked(platform);
                LogUtils.i("splash clicked,platform:" + adOnlineConfig.platform);
                if (onSplashImpl != null) {
                    onSplashImpl.onAdClicked(platform);
                }
            }

            @Override
            public void onAdClosed(@NonNull String platform) {
                super.onAdClosed(platform);
                LogUtils.i("splash closed,platform:" + adOnlineConfig.platform);
                if (onSplashImpl != null) {
                    onSplashImpl.onAdClosed(platform);
                }
            }

            @Override
            public void onAdShouldLaunch() {
                if (onSplashImpl != null) {
                    LogUtils.i("splash closed,platform:" + adOnlineConfig.platform);
                    onSplashImpl.onAdShouldLaunch();
                }
            }

            @Override
            public void onAdFailed(@NonNull String platform, int errorCode,
                                   @NonNull String errorMsg) {
                super.onAdFailed(platform, errorCode, errorMsg);
                LogUtils.w("splash failed,platform:" + adOnlineConfig.platform + ",appkey=" +
                        adOnlineConfig.appKey + "/subkey=" + adOnlineConfig.subKey);
                if (onSplashImpl != null) {
                    onSplashImpl.onAdFailed(platform, errorCode, errorMsg);
                }
                loadOneByOne(mADConfig);
            }
        });
    }

    private TextView addCountTextView(@NonNull ViewGroup container) {
        Context context = container.getContext();
        TextView countTextView = new TextView(context);
        countTextView.setId(R.id.text_skip);
        container.addView(countTextView);
        countTextView.setBackgroundResource(R.drawable.sdk_ad_count_cancel);
        countTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        countTextView.setMinHeight(dp2px(context, 34));
        countTextView.setMinWidth(dp2px(context, 88));
        countTextView.setGravity(Gravity.CENTER);
        countTextView.setText(
                String.format(countTextView.getContext().getString(R.string.sdk_ad_cancel), 5));
        countTextView.setTextColor(Color.BLACK);
        countTextView.setPadding(dp2px(context, 12),
                dp2px(context, 4),
                dp2px(context, 12),
                dp2px(context, 4));
        countTextView.setClickable(true);
        countTextView.setFocusable(true);
        countTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSplashImpl != null) {
                    onSplashImpl.onAdShouldLaunch();
                }
            }
        });
        if (container instanceof RelativeLayout) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.rightMargin = dp2px(context, 6);
            params.topMargin = dp2px(context, 16);
            countTextView.setLayoutParams(params);
        } else if (container instanceof FrameLayout) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.TOP | Gravity.END;
            params.rightMargin = dp2px(context, 6);
            params.topMargin = dp2px(context, 16);
            countTextView.setLayoutParams(params);
        } else if (container instanceof LinearLayout) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.TOP | Gravity.END;
            params.rightMargin = dp2px(context, 6);
            params.topMargin = dp2px(context, 16);
            countTextView.setLayoutParams(params);
        } else if (container instanceof ConstraintLayout) {
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone((ConstraintLayout) container);
            constraintSet.constrainWidth(countTextView.getId(),
                    ConstraintLayout.LayoutParams.WRAP_CONTENT);
            constraintSet.constrainHeight(countTextView.getId(),
                    ConstraintLayout.LayoutParams.WRAP_CONTENT);
            constraintSet.connect(countTextView.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID,
                    ConstraintSet.END, dp2px(context, 6));
            constraintSet.connect(countTextView.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID,
                    ConstraintSet.TOP, dp2px(context, 16));
            constraintSet.applyTo((ConstraintLayout) container);
        }
        return countTextView;
    }

    private int dp2px(Context context, float value) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (value * scale + 0.5f);
    }
}
