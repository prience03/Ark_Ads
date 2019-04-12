package com.ark.adkit.polymers.polymer.wrapper;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
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
import com.ark.adkit.polymers.polymer.utils.ClockTicker;
import com.ark.adkit.polymers.polymer.utils.SimpleActivityLifecycleCallbacks;
import com.ark.utils.permissions.PermissionChecker;
import com.ark.utils.permissions.PermissionItem;
import com.ark.utils.permissions.PermissionSimpleCallback;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SplashWrapper {

    protected List<PermissionItem> mPermissionItemArrayList = new ArrayList<>();
    protected boolean mRequestPermissions = true;
    private WeakReference<Activity> mActivityRef;
    private WeakReference<ViewGroup> mContainerRef;
    private WeakReference<TextView> mTextRef;
    private ClockTicker mClockTicker;
    private boolean splashClicked;
    private boolean splashLaunch;
    private int index = 0;

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
    public void hideSelfTimer(boolean hide) {
        if (hide && mClockTicker != null) {
            mClockTicker.release();
        }
        final TextView countTextView = getValidSkipTextView();
        if (countTextView != null) {
            countTextView.setVisibility(hide ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * 加载开屏
     *
     * @param mActivity    上下文
     * @param viewGroup    容器
     * @param rootView     根容器，最好和广告容器分开
     * @param onSplashImpl 监听
     */
    public void loadSplash(@NonNull Activity mActivity, @NonNull ViewGroup viewGroup,
            @NonNull ViewGroup rootView,
            @NonNull final OnSplashImpl onSplashImpl) {
        splashClicked = false;
        splashLaunch = false;
        mActivity.getApplication().registerActivityLifecycleCallbacks(
                new SimpleActivityLifecycleCallbacks() {

                    @Override
                    public void onActivityStopped(Activity activity) {
                        super.onActivityStopped(activity);
                        splashLaunch = true;
                    }

                    @Override
                    public void onActivityDestroyed(Activity activity) {
                        super.onActivityDestroyed(activity);
                        if (mClockTicker != null) {
                            mClockTicker.release();
                        }
                    }
                });
        mActivityRef = new WeakReference<>(mActivity);
        mContainerRef = new WeakReference<>(viewGroup);
        mTextRef = new WeakReference<>(addCountTextView(rootView, onSplashImpl));
        if (!mRequestPermissions || mPermissionItemArrayList == null) {
            checkPermissionsNext(onSplashImpl);
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
                        checkPermissionsNext(onSplashImpl);
                    }
                });
    }

    private void checkPermissionsNext(@NonNull final OnSplashImpl onSplashImpl) {
        mClockTicker = new ClockTicker();
        mClockTicker.setEndTime(System.currentTimeMillis() + 5500);
        mClockTicker.setClockListener(new ClockTicker.ClockListener() {
            @Override
            public void timeEnd() {
                LogUtils.i("倒计时结束跳转");
                if (!splashLaunch && !splashClicked) {
                    onSplashImpl.onAdShouldLaunch();
                }
                if (mClockTicker!=null){
                    mClockTicker.release();
                }
            }

            @Override
            public void onTick(long tick) {
                updateSkipText(tick);
                onSplashImpl.onAdTimeTick(tick);
            }
        });
        mClockTicker.start();
        ADConfig mADConfig = getConfig();
        if (!mADConfig.hasAD()) {
            LogUtils.e("开屏广告已被禁用,请检查在线配置");
            onSplashImpl.onAdDisable();
            return;
        }
        if (mADConfig.size() == 0) {
            onSplashImpl.onAdDisable();
            return;
        }
        //开始按顺序加载开屏
        index = 0;
        loadOneByOne(mADConfig, onSplashImpl);
    }

    private Activity getValidActivity() {
        if (mActivityRef != null) {
            Activity activity = mActivityRef.get();
            if (activity != null && !activity.isFinishing()) {
                return activity;
            }
        }
        return null;
    }

    private ViewGroup getValidViewGroup() {
        return mContainerRef == null ? null : mContainerRef.get();
    }

    private TextView getValidSkipTextView() {
        return mTextRef == null ? null : mTextRef.get();
    }

    /**
     * 实际加载开屏操作
     *
     * @param mADConfig    参数配置
     * @param onSplashImpl 监听
     */
    private void loadOneByOne(final @NonNull ADConfig mADConfig,
            final @NonNull OnSplashImpl onSplashImpl) {
        Activity activity = getValidActivity();
        ViewGroup viewGroup = getValidViewGroup();
        if (activity == null || viewGroup == null) {
            return;
        }
        if (index >= mADConfig.size()) {
            onSplashImpl.onAdDisable();
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
            final ADSplashModel adSplashModel = ADSplashFactory.createSplash(sortStr);
            if (adSplashModel == null) {
                Run.onUiAsync(new Action() {
                    @Override
                    public void call() {
                        loadOneByOne(mADConfig, onSplashImpl);
                    }
                });
            } else {
                Run.onUiAsync(new Action() {
                    @Override
                    public void call() {
                        splashModelLoad(adSplashModel, adOnlineConfig, mADConfig, onSplashImpl);
                    }
                });
            }
        } else {
            loadOneByOne(mADConfig, onSplashImpl);
        }
    }

    private void splashModelLoad(@NonNull ADSplashModel adSplashModel,
            @NonNull ADOnlineConfig adOnlineConfig,
            @NonNull final ADConfig mADConfig,
            @NonNull final OnSplashImpl onSplashImpl) {
        Activity activity = getValidActivity();
        ViewGroup viewGroup = getValidViewGroup();
        if (activity == null || viewGroup == null) {
            return;
        }
        adSplashModel.initModel(adOnlineConfig);
        adSplashModel.loadSplashAD(activity, viewGroup, new OnSplashImpl() {
            @Override
            public void onAdDisable() {
                super.onAdDisable();
                onSplashImpl.onAdDisable();
            }

            @Override
            public void onAdTimeTick(long tick) {
                if (tick == 0 && !splashLaunch) {
                    splashLaunch = true;
                    if (!splashClicked) {
                        if (mClockTicker != null) {
                            mClockTicker.release();
                        }
                    }
                    onSplashImpl.onAdShouldLaunch();
                    return;
                }
                updateSkipText(tick);
                if (!splashLaunch) {
                    onSplashImpl.onAdTimeTick(tick);
                }
            }

            @Override
            public void onAdWillLoad(@NonNull String platform) {
                super.onAdWillLoad(platform);
                onSplashImpl.onAdWillLoad(platform);
            }

            @Override
            public void onAdDisplay(@NonNull String platform, boolean hideSelfTicker) {
                super.onAdDisplay(platform, hideSelfTicker);
                hideSelfTimer(hideSelfTicker);
                onSplashImpl.onAdDisplay(platform, hideSelfTicker);
            }

            @Override
            public void onAdClicked(@NonNull String platform) {
                super.onAdClicked(platform);
                splashClicked = true;
                onSplashImpl.onAdClicked(platform);
            }

            @Override
            public void onAdClosed(@NonNull String platform) {
                super.onAdClosed(platform);
                onSplashImpl.onAdClosed(platform);
                if (!splashLaunch && !splashClicked) {
                    splashLaunch = true;
                    if (mClockTicker != null) {
                        mClockTicker.release();
                    }
                    onSplashImpl.onAdShouldLaunch();
                }
            }

            @Override
            public void onAdShouldLaunch() {
                splashLaunch = true;
                if (mClockTicker != null) {
                    mClockTicker.release();
                }
                onSplashImpl.onAdShouldLaunch();
            }

            @Override
            public void onAdFailed(@NonNull String platform, int errorCode,
                    @NonNull String errorMsg) {
                super.onAdFailed(platform, errorCode, errorMsg);
                onSplashImpl.onAdFailed(platform, errorCode, errorMsg);
                loadOneByOne(mADConfig, onSplashImpl);
            }
        });
    }

    private TextView addCountTextView(@NonNull ViewGroup container,
            @NonNull final OnSplashImpl onSplashImpl) {
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
                splashLaunch = true;
                if (mClockTicker != null) {
                    mClockTicker.release();
                }
                onSplashImpl.onAdShouldLaunch();
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
