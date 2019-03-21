package com.ark.adkit.polymers.polymer.wrapper;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
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
import com.ark.adkit.basics.models.ADSplashModel;
import com.ark.adkit.basics.models.OnSplashImpl;
import com.ark.adkit.basics.utils.GpsUtil;
import com.ark.adkit.basics.utils.LogUtils;
import com.ark.adkit.polymers.R;
import com.ark.adkit.polymers.polymer.ADTool;
import com.ark.adkit.polymers.polymer.factory.ADSplashFactory;
import com.ark.adkit.polymers.polymer.utils.ClockTicker;
import com.ark.utils.permissions.PermissionCallback;
import com.ark.utils.permissions.PermissionChecker;
import com.ark.utils.permissions.PermissionItem;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SplashWrapper {

    public static int RequestCodeSettings = 200;//权限请求码
    public static String PermissionTip1 = "亲爱的用户 \n\n软件部分功能需要请求您的手机权限，请允许以下权限：\n\n";//权限提醒
    public static String PermissionTip2 = "\n请到 “应用信息 -> 权限” 中授予！";//权限提醒
    public static String PermissionDialogPositiveButton = "去手动授权";
    public static String PermissionDialogNegativeButton = "取消";
    private WeakReference<Activity> mActivityRef;
    private WeakReference<ViewGroup> mContainerRef;
    private WeakReference<TextView> mTextRef;
    private List<PermissionItem> permissionItems = new ArrayList<>();
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
    public void loadSplash(@NonNull final Activity mActivity, @NonNull ViewGroup viewGroup,
            @NonNull ViewGroup rootView,
            @NonNull final OnSplashImpl onSplashImpl) {
        splashClicked = false;
        splashLaunch = false;
        mActivity.getApplication().registerActivityLifecycleCallbacks(
                new Application.ActivityLifecycleCallbacks() {
                    @Override
                    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

                    }

                    @Override
                    public void onActivityStarted(Activity activity) {

                    }

                    @Override
                    public void onActivityResumed(Activity activity) {

                    }

                    @Override
                    public void onActivityPaused(Activity activity) {

                    }

                    @Override
                    public void onActivityStopped(Activity activity) {
                        splashLaunch = true;
                    }

                    @Override
                    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

                    }

                    @Override
                    public void onActivityDestroyed(Activity activity) {
                        if (mClockTicker != null) {
                            mClockTicker.release();
                        }
                    }
                });
        mActivityRef = new WeakReference<>(mActivity);
        mContainerRef = new WeakReference<>(viewGroup);
        mTextRef = new WeakReference<>(addCountTextView(rootView, onSplashImpl));
        permissionItems.clear();
        permissionItems.add(new PermissionItem(Manifest.permission.WRITE_EXTERNAL_STORAGE, "存储权限"));
        permissionItems.add(new PermissionItem(Manifest.permission.ACCESS_FINE_LOCATION, "定位权限"));
        permissionItems.add(new PermissionItem(Manifest.permission.READ_PHONE_STATE, "手机状态"));
        PermissionChecker.create(mActivity)
                .permissions(permissionItems)
                .checkMultiPermission(new PermissionCallback() {
                    private static final long serialVersionUID = 5170045860554631205L;

                    @Override
                    public void onClose() {
                        mActivity.finish();
                    }

                    @Override
                    public void onFinish() {
                        loadSplashWithPermissions(mActivity, onSplashImpl);
                    }

                    @Override
                    public void onDeny(@NonNull String permission, int position) {
                        LogUtils.i("onDeny" + permission);
                    }

                    @Override
                    public void onGuarantee(@NonNull String permission,
                            int position) {
                        LogUtils.i("onGuarantee" + permission);
                    }
                });
    }

    private void loadSplashWithPermissions(@NonNull Activity activity,
            @NonNull final OnSplashImpl onSplashImpl) {
        boolean start = true;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Location location = GpsUtil.requestLocation(activity);
            if (location == null) {
                boolean isOpen = GpsUtil.isOPen(activity);
                if (!isOpen) {
                    start = false;
                    openAppDetails(activity, "-定位", true);
                }
            }
        }
        if (start) {
            mClockTicker = new ClockTicker();
            mClockTicker.setEndTime(System.currentTimeMillis() + 5500);
            mClockTicker.setClockListener(new ClockTicker.ClockListener() {
                @Override
                public void timeEnd() {
                    LogUtils.i("倒计时结束跳转");
                    if (!splashLaunch && !splashClicked) {
                        onSplashImpl.onAdShouldLaunch();
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
    }

    /**
     * 打开APP详情页面，引导用户去设置权限
     *
     * @param activity        页面对象
     * @param permissionNames 权限名称（如是多个，使用\n分割）
     */
    private void openAppDetails(@NonNull final Activity activity, @NonNull String permissionNames,
            final boolean finish) {
        if (activity.isFinishing()) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(PermissionTip1);
        sb.append(permissionNames);
        sb.append(PermissionTip2);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(sb.toString());
        builder.setCancelable(false);
        builder.setPositiveButton(PermissionDialogPositiveButton,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent locationIntent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        if (!activity.isFinishing()) {
                            activity.startActivityForResult(locationIntent, RequestCodeSettings);
                        }
                    }
                });
        builder.setNegativeButton(PermissionDialogNegativeButton,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (finish && !activity.isFinishing()) {
                            activity.finish();
                        }
                    }
                });
        AlertDialog mDialog = builder.create();
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
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
        final Activity activity = getValidActivity();
        final ViewGroup viewGroup = getValidViewGroup();
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
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadOneByOne(mADConfig, onSplashImpl);
                    }
                });
            } else {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        splashModelLoad(adSplashModel, activity, viewGroup, adOnlineConfig,
                                mADConfig, onSplashImpl);
                    }
                });
            }
        } else {
            loadOneByOne(mADConfig, onSplashImpl);
        }
    }

    private void splashModelLoad(@NonNull ADSplashModel adSplashModel,
            @NonNull Activity activity, @NonNull ViewGroup viewGroup,
            @NonNull ADOnlineConfig adOnlineConfig,
            @NonNull final ADConfig mADConfig,
            @NonNull final OnSplashImpl onSplashImpl) {
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
