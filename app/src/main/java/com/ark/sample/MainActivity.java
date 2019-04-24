package com.ark.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ark.adkit.basics.configs.ADPlatform;
import com.ark.adkit.basics.handler.Action;
import com.ark.adkit.basics.handler.Run;
import com.ark.adkit.basics.utils.LogUtils;
import com.ark.adkit.polymers.polymer.ADTool;
import com.ark.adkit.polymers.polymer.interrcmd.RecommendDialog;
import com.ark.adkit.polymers.self.SelfADStyle;
import com.ark.adkit.polymers.self.SelfDataRef;
import com.ark.adkit.polymers.self.SelfNativeAD;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FrameLayout fl;
    private FrameLayout banner;
    private int mIndex = 1;
    private TextView mTextView;
    private List<String> mStringList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fl = findViewById(R.id.fl_container);
        banner = findViewById(R.id.fl_banner);
        mTextView = findViewById(R.id.text);
    }

    private void loadNative() {
        ADTool.getADTool()
                .getManager()
                .getNativeWrapper()
                .loadVideoView(this, fl);
    }

    private void loadBanner() {
        ADTool.getADTool()
                .getManager()
                .getNativeWrapper()
                .loadBannerView(this, banner);
    }

    public void refresh(View view) {
        loadNative();
        loadBanner();
    }

    public void rcmd(View view) {
        new SelfNativeAD(this, SelfADStyle.INTER_RECOMMEND)
                .setListener(new SelfNativeAD.ADListener() {
                    @Override
                    public void onAdLoad(List<SelfDataRef> dataRefs) {
                        if (dataRefs != null && dataRefs.size() > 0) {
                            SelfDataRef selfDataRef = dataRefs.get(0);
                            RecommendDialog recommendDialog = new RecommendDialog(
                                    MainActivity.this);
                            recommendDialog.setBundle(selfDataRef);
                            recommendDialog.show();
                        }
                    }

                    @Override
                    public void onAdFailed(int errorCode, @NonNull String errorMsg) {

                    }
                }).loadAllADList();
    }

    public void rotate(View view) {
        Run.onBackground(new Action() {
            @Override
            public void call() {
                LogUtils.e("call thread:" + Thread.currentThread().getName());
                mStringList.clear();
                mStringList.add("1:" + ADPlatform.GDT);
                mStringList.add("2:" + ADPlatform.IFLY);
                mStringList.add("3:" + ADPlatform.LYJH);
                mStringList.add("4:" + ADPlatform.SELF);
                mStringList.add("5:" + ADPlatform.TTAD);
                mStringList.add("6:" + ADPlatform.WSKJ);
                mStringList.add("7:" + ADPlatform.YDT);
                if (mIndex-- <= Integer.MIN_VALUE) {
                    mIndex = 0;
                }
                Collections.rotate(mStringList, mIndex);
                Run.onUiAsync(new Action() {
                    @Override
                    public void call() {
                        LogUtils.e("ui thread:" + Thread.currentThread().getName());
                        mTextView.setText(mStringList.toString());
                    }
                });
            }
        });
    }

    public void cycle(View view) {
        Run.onBackground(new Action() {
            @Override
            public void call() {
                mStringList.clear();
                mStringList.add("1:" + ADPlatform.GDT);
                mStringList.add("2:" + ADPlatform.IFLY);
                mStringList.add("3:" + ADPlatform.LYJH);
                mStringList.add("4:" + ADPlatform.SELF);
                mStringList.add("5:" + ADPlatform.TTAD);
                mStringList.add("6:" + ADPlatform.WSKJ);
                mStringList.add("7:" + ADPlatform.YDT);
                Collections.shuffle(mStringList);
                Run.onUiAsync(new Action() {
                    @Override
                    public void call() {
                        mTextView.setText(mStringList.toString());
                    }
                });
            }
        });
    }

    public void list(View view) {
        startActivity(new Intent(this, ListActivity.class));
        finish();
    }

    public void splash(View view) {
        startActivity(new Intent(this, SplashActivity.class));
        finish();
    }
}
