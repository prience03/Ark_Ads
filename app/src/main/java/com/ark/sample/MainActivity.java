package com.ark.sample;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;
import com.ark.adkit.basics.models.OnSplashImpl;
import com.ark.adkit.polymers.polymer.ADTool;
import com.ark.adkit.polymers.polymer.interrcmd.RecommendDialog;
import com.ark.adkit.polymers.self.SelfADStyle;
import com.ark.adkit.polymers.self.SelfDataRef;
import com.ark.adkit.polymers.self.SelfNativeAD;
import com.ark.utils.permissions.PermissionCallback;
import com.ark.utils.permissions.PermissionChecker;
import com.ark.utils.permissions.PermissionItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SplashFragment.OnPreparedListener {

    private FrameLayout fl;
    private FrameLayout banner;
    private SplashFragment mFragment;

    private OnSplashImpl mOnSplashImpl = new OnSplashImpl() {
        @Override
        public void onAdTimeTick(long tick) {

        }

        @Override
        public void onAdShouldLaunch() {
            if (mFragment != null) {
                mFragment.dismissAllowingStateLoss();
                mFragment = null;
            }
            loadBanner();
            loadNative();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFragment = new SplashFragment();
        mFragment.setOnPreparedListener(this);
        mFragment.show(getSupportFragmentManager(), "splash");
        fl = findViewById(R.id.fl_container);
        banner = findViewById(R.id.fl_banner);
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

    @Override
    public void onCreateView(final ViewGroup rootView, final ViewGroup adContainer) {
        List<PermissionItem> list = new ArrayList<>();
        list.add(new PermissionItem(Manifest.permission.ACCESS_FINE_LOCATION, "定位"));
        list.add(new PermissionItem(Manifest.permission.WRITE_EXTERNAL_STORAGE, "存储"));
        list.add(new PermissionItem(Manifest.permission.READ_PHONE_STATE, "打电话"));
        list.add(new PermissionItem(Manifest.permission.CAMERA, "相机"));
        list.add(new PermissionItem(Manifest.permission.RECORD_AUDIO, "录音"));
        PermissionChecker.create(this).permissions(list)
                .checkMultiPermission(new PermissionCallback() {
                    private static final long serialVersionUID = -6487903471146122873L;

                    @Override
                    public void onClose() {
                        finish();
                    }

                    @Override
                    public void onFinish() {
                        Toast.makeText(MainActivity.this, "所有权限已经获取", Toast.LENGTH_SHORT).show();
                        loadSplash(rootView, adContainer);
                    }

                    @Override
                    public void onDeny(@NonNull String permission, int position) {
                        Log.e("logger", "onDeny" + permission);
                    }

                    @Override
                    public void onGuarantee(@NonNull String permission, int position) {
                        Log.e("logger", "onGuarantee" + permission);
                    }
                });
    }

    private void loadSplash(final ViewGroup rootView, final ViewGroup adContainer) {
        ADTool.getADTool()
                .getManager()
                .getSplashWrapper()
                .loadSplash(this, adContainer, rootView, mOnSplashImpl);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mFragment != null) {
            mFragment.dismissAllowingStateLoss();
            mFragment = null;
        }
    }

    public void splash(View view) {
        if (mFragment == null) {
            mFragment = new SplashFragment();
            mFragment.setOnPreparedListener(this);
        }
        mFragment.show(getSupportFragmentManager(), "splash");
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
}
