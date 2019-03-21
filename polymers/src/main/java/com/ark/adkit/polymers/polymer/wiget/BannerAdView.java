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
import com.ark.adkit.polymers.R;
import com.ark.adkit.polymers.polymer.ADTool;

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

    /**
     * 更新数据
     *
     * @param adMetas AdMetas
     */
    public void setData(ADMetaData adMetas) {
        this.adMetaData = adMetas;
        if (adMetaData != null) {
            updateUi(adMetaData);
        }
    }

    /**
     * 初始化布局
     *
     * @param context 上下文
     */
    private void initView(Context context) {
        this.mContext = context;
        inflate(context, R.layout.sdk_item_ad_banner_match_width, this);
        logoView = (ImageView) findViewById(R.id.ad_app_logo);
        adTitleView = (TextView) findViewById(R.id.ad_app_title);
        adAppDownload = (TextView) findViewById(R.id.ad_app_download);
        adSubTitleView = (TextView) findViewById(R.id.ad_app_desc);
        tvPlatform = (TextView) findViewById(R.id.ad_platform);
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
                        if (adMetaData != null) {
                            adMetaData.handleClick(BannerAdView.this);
                            adMetaData.handleClick(BannerAdView.this, adAppDownload, mDownX, mDownY, mUpX, mUpY);
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
     * @param metaData ADMetaData
     */
    private void updateUi(@NonNull ADMetaData metaData) {
        //如果logo不存在就用图片代替
        AQuery aQuery = new AQuery(logoView);
        if (!TextUtils.isEmpty(metaData.getLogoUrl())) {
            aQuery.image(metaData.getLogoUrl(), true, true);
        } else if (!TextUtils.isEmpty(metaData.getImgUrl())) {
            aQuery.image(metaData.getImgUrl(), true, true);
        }
        String title = metaData.getTitle();
        String subTitle = metaData.getSubTitle();
        if (!TextUtils.isEmpty(title)) {
            adTitleView.setText(title);
        }
        if (!TextUtils.isEmpty(subTitle)) {
            adSubTitleView.setText(metaData.getSubTitle());
        }
        adAppDownload.setText(metaData.isApp() ? "下载" : "浏览");
        if (ADTool.getADTool().isDebugMode()) {
            tvPlatform.setVisibility(VISIBLE);
            tvPlatform.setText(metaData.getPlatform());
        }
        metaData.handleView(this);
    }
}
