package com.ark.adkit.polymers.polymer.wiget;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ark.adkit.basics.data.ADMetaData;
import com.ark.adkit.polymers.R;
import com.ark.adkit.polymers.polymer.ADTool;
import com.ark.adkit.polymers.ydt.utils.ScreenUtils;
import com.bytedance.sdk.openadsdk.TTFeedAd;

public class VideoAdView extends FrameLayout {

    private TextView adTitleView, adSubTitleView, tvPlatform;
    private FrameLayout videoView;
    private ImageView logoView;
    private View rlBottom;
    private ADMetaData mVideoData;
    private Context mContext;

    public VideoAdView(Context context) {
        this(context, null);
    }

    public VideoAdView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoAdView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public VideoAdView(Context context, @Nullable AttributeSet attrs, int defStyleAttr,
            int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    /**
     * 更新数据
     *
     * @param videoData ADMetaData
     */
    public void setData(ADMetaData videoData) {
        this.mVideoData = videoData;
        if (mVideoData != null) {
            updateUi(mVideoData);
        }
    }

    /**
     * 初始化布局
     *
     * @param context 上下文
     */
    private void initView(Context context) {
        this.mContext = context;
        inflate(context, R.layout.sdk_item_ad_ratio_match_width_video, this);
        videoView = findViewById(R.id.ad_app_video);
        logoView = findViewById(R.id.ad_app_logo);
        adTitleView = findViewById(R.id.ad_app_title);
        adSubTitleView = findViewById(R.id.ad_app_desc);
        rlBottom = findViewById(R.id.rl_bottom);
        tvPlatform = findViewById(R.id.ad_platform);
        rlBottom.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVideoData != null) {
                    mVideoData.handleClick(VideoAdView.this);
                }
            }
        });
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.width = ScreenUtils.getScreenWidth(mContext);
        layoutParams.height = (int) (ScreenUtils.getScreenWidth(mContext) * 0.5625);
        videoView.setLayoutParams(layoutParams);
    }

    public void refreshVideoView() {
        if (mVideoData != null && mVideoData.getAdView() != null) {
            videoView.removeAllViews();
            videoView.addView(mVideoData.getAdView());
            mVideoData.handleView(videoView);
        }
    }

    /**
     * 更新布局
     *
     * @param videoData ADMetaData
     */
    private void updateUi(@NonNull ADMetaData videoData) {
        //如果logo不存在就用图片代替
        if (!TextUtils.isEmpty(videoData.getLogoUrl())) {
            ADTool.getADTool().getManager().loadImage(logoView, videoData.getLogoUrl());
        } else if (!TextUtils.isEmpty(videoData.getImgUrl())) {
            ADTool.getADTool().getManager().loadImage(logoView, videoData.getImgUrl());
        }
        String title = videoData.getTitle();
        String subTitle = videoData.getSubTitle();
        //没有标题的话不显示底部
        if (!TextUtils.isEmpty(title)) {
            adTitleView.setText(title);
            rlBottom.setVisibility(VISIBLE);
        }
        if (!TextUtils.isEmpty(subTitle)) {
            adSubTitleView.setText(videoData.getSubTitle());
        }
        if (ADTool.getADTool().isDebugMode()) {
            tvPlatform.setVisibility(VISIBLE);
            tvPlatform.setText(videoData.getPlatform());
        }
        if (mContext instanceof Activity) {
            Object o = videoData.getData();
            try {
                if (o instanceof TTFeedAd) {
                    ((TTFeedAd) o).setActivityForDownloadApp((Activity) mContext);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
