package com.ark.adkit.polymers.polymer.wiget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.androidquery.AQuery;
import com.ark.adkit.basics.data.ADMetaData;
import com.ark.adkit.basics.utils.LogUtils;
import com.ark.adkit.polymers.R;
import com.ark.adkit.polymers.polymer.ADTool;

public class SplashAdView extends FrameLayout {

    private Context mContext;
    private ADMetaData mADData;
    private int mDownX, mDownY, mUpX, mUpY;
    private ImageView ivBg;
    private TextView tvPlatform;
    private SplashCallBack splashCallBack;

    public SplashAdView(@NonNull Context context) {
        this(context, null);
    }

    public SplashAdView(@NonNull Context context,
            @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SplashAdView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SplashAdView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr,
            int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    /**
     * 设置开屏监听
     *
     * @param splashCallBack SplashCallBack
     * @return SplashAdView.this
     */
    @NonNull
    public SplashAdView setCallback(SplashCallBack splashCallBack) {
        this.splashCallBack = splashCallBack;
        return this;
    }

    /**
     * 初始化布局
     *
     * @param context 上下文
     */
    private void initView(Context context) {
        this.mContext = context;
        inflate(context, R.layout.sdk_widget_layout_splashadview, this);
        ivBg = (ImageView) findViewById(R.id.iv_bg);
        tvPlatform = (TextView) findViewById(R.id.ad_platform);
        //点击了广告
        ivBg
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LogUtils.v(
                                "onTouch:mDownX=" + mDownX + ",mDownY=" + mDownY + ",mUpX=" + mUpX
                                        + ",mUpY=" + mUpY);
                        if (mADData != null) {
                            mADData.setClickPosition(mDownX, mDownY, mUpX, mUpY);
                            mADData.setClickView(SplashAdView.this, ivBg);
                            mADData.handleClick(SplashAdView.this);
                        }
                        if (splashCallBack != null) {
                            splashCallBack.onAdClick(mADData != null &&
                                            !TextUtils.isEmpty(mADData.getImgUrl()),
                                    mADData != null && mADData.isApp());
                        }
                    }
                });
        //记录手指按下抬起的坐标
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mDownX = (int) event.getX();
                        mDownY = (int) event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        mUpX = (int) event.getX();
                        mUpY = (int) event.getY();
                        v.performClick();
                        break;
                }
                return true;
            }
        });
    }

    /**
     * 刷新数据
     *
     * @param adMetas ADMetaData
     */
    public void setData(ADMetaData adMetas) {
        this.mADData = adMetas;
        if (mADData != null) {
            updateUi(mADData);
        }
    }

    /**
     * 更新ui
     *
     * @param metaData ADMetaData
     */
    private void updateUi(@NonNull ADMetaData metaData) {
        //背景图片
        if (!TextUtils.isEmpty(metaData.getImgUrl())) {
            AQuery query = new AQuery(ivBg);
            query.image(metaData.getImgUrl(), true, true);
        }
        if (ADTool.getADTool().isDebugMode()) {
            tvPlatform.setVisibility(VISIBLE);
            tvPlatform.setText(metaData.getPlatform());
        }
        metaData.handleView(this);
    }

    public interface SplashCallBack {

        void onAdDisplay();

        void onFailure(int code, String msg);

        void onAdClick(boolean isAd, boolean isApp);
    }
}
