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
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ark.adkit.basics.data.ADMetaData;
import com.ark.adkit.basics.utils.LogUtils;
import com.ark.adkit.polymers.R;
import com.ark.adkit.polymers.polymer.ADTool;
import com.ark.adkit.polymers.ydt.utils.ScreenUtils;

public class NativeAdView extends FrameLayout {

    private TextView adTitleView, adSubTitleView, tvPlatform;
    private ImageView imageView;
    private ImageView logoView;
    private View rlBottom;
    private ADMetaData adMetaData;
    private int mDownX, mDownY, mUpX, mUpY;
    private Context mContext;

    public NativeAdView(Context context) {
        this(context, null);
    }

    public NativeAdView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NativeAdView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NativeAdView(Context context, @Nullable AttributeSet attrs, int defStyleAttr,
            int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    public void attachViewGroup(ViewGroup viewGroup,ADMetaData adMetas) {
        if (viewGroup.getChildCount() > 0) {
            viewGroup.removeAllViews();
        }
        viewGroup.addView(this);
        this.adMetaData = adMetas;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.width = ScreenUtils.getScreenWidth(mContext);
        layoutParams.height = (int) (ScreenUtils.getScreenWidth(mContext) * 0.5625);
        imageView.setLayoutParams(layoutParams);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    /**
     * 初始化布局
     *
     * @param context 上下文
     */
    private void initView(Context context) {
        this.mContext = context;
        inflate(context, R.layout.sdk_widget_layout_nativeadview, this);
        imageView = findViewById(R.id.ad_app_image);
        logoView = findViewById(R.id.ad_app_logo);
        adTitleView = findViewById(R.id.ad_app_title);
        adSubTitleView = findViewById(R.id.ad_app_desc);
        rlBottom = findViewById(R.id.rl_bottom);
        tvPlatform = findViewById(R.id.ad_platform);
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
                        LogUtils.v(
                                "onTouch:mDownX=" + mDownX + ",mDownY=" + mDownY + ",mUpX=" + mUpX
                                        + ",mUpY=" + mUpY);
                        if (adMetaData != null) {
                            adMetaData.setClickPosition(mDownX, mDownY, mUpX, mUpY);
                            adMetaData.setClickView(NativeAdView.this, imageView);
                            adMetaData.handleClick(NativeAdView.this);
                        }
                        break;
                }
                return true;
            }
        });
    }

    /**
     * 更新布局
     *
     */
    public void handleView() {
        if (!TextUtils.isEmpty(adMetaData.getImgUrl())) {
            ADTool.getADTool().getManager().loadImage(imageView,adMetaData.getImgUrl());
        }
        //如果logo不存在就用图片代替
        if (!TextUtils.isEmpty(adMetaData.getLogoUrl())) {
            ADTool.getADTool().getManager().loadImage(logoView,adMetaData.getLogoUrl());
        } else if (!TextUtils.isEmpty(adMetaData.getImgUrl())) {
            ADTool.getADTool().getManager().loadImage(logoView,adMetaData.getImgUrl());
        }
        String title = adMetaData.getTitle();
        String subTitle = adMetaData.getSubTitle();
        //没有标题的话不显示底部
        if (!TextUtils.isEmpty(title)) {
            adTitleView.setText(title);
            rlBottom.setVisibility(VISIBLE);
        }
        if (!TextUtils.isEmpty(subTitle)) {
            adSubTitleView.setText(adMetaData.getSubTitle());
        }
        if (ADTool.getADTool().isDebugMode()) {
            tvPlatform.setVisibility(VISIBLE);
            tvPlatform.setText(adMetaData.getPlatform());
        }
        adMetaData.handleView(this);
    }
}
