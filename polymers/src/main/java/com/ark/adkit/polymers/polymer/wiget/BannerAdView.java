package com.ark.adkit.polymers.polymer.wiget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.androidquery.AQuery;
import com.ark.adkit.basics.data.ADMetaData;
import com.ark.adkit.basics.utils.LogUtils;
import com.ark.adkit.polymers.R;
import com.ark.adkit.polymers.polymer.ADTool;
import com.ark.adkit.polymers.ydt.utils.ScreenUtils;

public class BannerAdView extends FrameLayout {

    private TextView adTitleView, adSubTitleView, adAppDownload, tvPlatform;
    private ImageView logoView;
    private ADMetaData adMetaData;
    private int mDownX, mDownY, mUpX, mUpY;
    private Context mContext;

    public BannerAdView(Context context) {
        this(context, null);
    }

    public BannerAdView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerAdView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BannerAdView(Context context, @Nullable AttributeSet attrs, int defStyleAttr,
                        int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    public void attachViewGroup(ViewGroup viewGroup, ADMetaData adMetas) {
        if (viewGroup.getChildCount() > 0) {
            viewGroup.removeAllViews();
        }
        ViewGroup.LayoutParams layoutParams = viewGroup.getLayoutParams();
        layoutParams.width = ScreenUtils.getScreenWidth(mContext);
        layoutParams.height = dip2px(mContext, 65);
        viewGroup.addView(this);
        this.adMetaData = adMetas;
    }

    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 初始化布局
     *
     * @param context 上下文
     */
    private void initView(Context context) {
        this.mContext = context;
        inflate(context, R.layout.sdk_widget_layout_banneradview, this);
        logoView = findViewById(R.id.ad_app_logo);
        adTitleView = findViewById(R.id.ad_app_title);
        adAppDownload = findViewById(R.id.ad_app_download);
        adSubTitleView = findViewById(R.id.ad_app_desc);
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
                            adMetaData.setClickView(BannerAdView.this, adAppDownload);
                            adMetaData.handleClick(BannerAdView.this);
                        }
                        break;
                }
                return true;
            }
        });
    }

    /**
     * 更新布局
     */
    public void handleView() {
        if (adMetaData == null) {
            return;
        }
        //如果logo不存在就用图片代替
        AQuery aQuery = new AQuery(logoView);
        if (!TextUtils.isEmpty(adMetaData.getLogoUrl())) {
            aQuery.image(adMetaData.getLogoUrl(), true, true);
        } else if (!TextUtils.isEmpty(adMetaData.getImgUrl())) {
            aQuery.image(adMetaData.getImgUrl(), true, true);
        }
        String title = adMetaData.getTitle();
        String subTitle = adMetaData.getSubTitle();
        if (!TextUtils.isEmpty(title)) {
            adTitleView.setText(title);
        }
        if (!TextUtils.isEmpty(subTitle)) {
            adSubTitleView.setText(adMetaData.getSubTitle());
        }
        adAppDownload.setText(adMetaData.isApp() ? "下载" : "浏览");
        if (ADTool.getADTool().isDebugMode()) {
            tvPlatform.setVisibility(VISIBLE);
            tvPlatform.setText(adMetaData.getPlatform());
        }
        adMetaData.handleView(this);
    }
}
