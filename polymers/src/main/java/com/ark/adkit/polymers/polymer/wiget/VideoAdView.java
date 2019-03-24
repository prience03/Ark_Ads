package com.ark.adkit.polymers.polymer.wiget;

import android.annotation.TargetApi;
import android.app.Activity;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ark.adkit.basics.data.ADMetaData;
import com.ark.adkit.basics.utils.LogUtils;
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
    private int mDownX, mDownY, mUpX, mUpY;

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

    public void attachViewGroup(ViewGroup viewGroup, ADMetaData adMetas) {
        viewGroup.addView(this);
        this.mVideoData = adMetas;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.width = ScreenUtils.getScreenWidth(mContext);
        layoutParams.height = (int) (ScreenUtils.getScreenWidth(mContext) * 0.5625);
        videoView.setLayoutParams(layoutParams);
    }

    /**
     * 初始化布局
     *
     * @param context 上下文
     */
    private void initView(Context context) {
        this.mContext = context;
        inflate(context, R.layout.sdk_widget_layout_videoadview, this);
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
                        /*if (mVideoData != null) {
                            mVideoData.setClickPosition(mDownX, mDownY, mUpX, mUpY);
                            mVideoData.setClickView(VideoAdView.this, v);
                            mVideoData.handleClick(VideoAdView.this);
                        }*/
                        break;
                }
                return true;
            }
        });
    }

    public void handleView() {
        if (mVideoData == null) {
            return;
        }
        if (mVideoData.getAdView() != null) {
            videoView.addView(mVideoData.getAdView());
        }
        //如果logo不存在就用图片代替
        if (!TextUtils.isEmpty(mVideoData.getLogoUrl())) {
            ADTool.getADTool().getManager().loadImage(logoView, mVideoData.getLogoUrl());
        } else if (!TextUtils.isEmpty(mVideoData.getImgUrl())) {
            ADTool.getADTool().getManager().loadImage(logoView, mVideoData.getImgUrl());
        }
        String title = mVideoData.getTitle();
        String subTitle = mVideoData.getSubTitle();
        //没有标题的话不显示底部
        if (!TextUtils.isEmpty(title)) {
            adTitleView.setText(title);
            rlBottom.setVisibility(VISIBLE);
        }
        if (!TextUtils.isEmpty(subTitle)) {
            adSubTitleView.setText(mVideoData.getSubTitle());
        }
        if (ADTool.getADTool().isDebugMode()) {
            tvPlatform.setVisibility(VISIBLE);
            tvPlatform.setText(mVideoData.getPlatform());
        }
        if (mContext instanceof Activity) {
            Object o = mVideoData.getData();
            try {
                if (o instanceof TTFeedAd) {
                    ((TTFeedAd) o).setActivityForDownloadApp((Activity) mContext);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mVideoData.handleView(videoView);
    }
}
