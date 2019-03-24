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
import com.ark.adkit.polymers.self.ADMetaDataOfSelf;

public class SmallNativeView extends FrameLayout {

    private ImageView imageView;
    private ADMetaData mADData;
    private int mDownX, mDownY, mUpX, mUpY;
    private Context mContext;
    private TextView tvPlatform;

    public SmallNativeView(Context context) {
        this(context, null);
    }

    public SmallNativeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SmallNativeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SmallNativeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr,
            int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    public void attachViewGroup(ViewGroup viewGroup, ADMetaData adMetas) {
        if (viewGroup.getChildCount() > 0) {
            viewGroup.removeAllViews();
        }
        viewGroup.addView(this);
        this.mADData = adMetas;
        LayoutParams layoutParams = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.height = (int) (getWidth() * 1.5);
        imageView.setLayoutParams(layoutParams);
    }

    /**
     * 初始化布局
     *
     * @param context 上下文
     */
    private void initView(Context context) {
        this.mContext = context;
        inflate(context, R.layout.sdk_widget_layout_smalladview, this);
        imageView = findViewById(R.id.ad_app_image);
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
                        if (mADData != null) {
                            mADData.setClickPosition(mDownX, mDownY, mUpX, mUpY);
                            mADData.setClickView(SmallNativeView.this, imageView);
                            mADData.handleClick(SmallNativeView.this);
                        }
                        break;
                }
                return true;
            }
        });
    }

    public void handleView() {
        if (mADData == null) {
            return;
        }
        String url = mADData.getImgUrl();
        if (mADData instanceof ADMetaDataOfSelf) {
            url = ((ADMetaDataOfSelf) mADData).getVerticalImage();
        }
        if (!TextUtils.isEmpty(url)) {
            new AQuery(imageView).image(url, true, true);
        }
        if (ADTool.getADTool().isDebugMode()) {
            tvPlatform.setVisibility(VISIBLE);
            tvPlatform.setText(mADData.getPlatform());
        }
        mADData.handleView(this);
    }
}
