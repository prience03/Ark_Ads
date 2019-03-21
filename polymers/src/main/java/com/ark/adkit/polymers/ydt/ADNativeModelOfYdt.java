package com.ark.adkit.polymers.ydt;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.ark.adkit.basics.models.ADNativeModel;
import com.ark.adkit.basics.utils.LogUtils;
import com.ark.adkit.polymers.ydt.constant.UrlConst;
import com.ark.adkit.polymers.ydt.entity.AdResp;
import com.ark.adkit.polymers.ydt.utils.YdtUtils;
import com.ark.net.urlconn.StringCallback;
import com.yanzhenjie.kalle.Kalle;
import com.yanzhenjie.kalle.StringBody;
import com.yanzhenjie.kalle.simple.SimpleResponse;

public class ADNativeModelOfYdt extends ADNativeModel {

    private YdtUtils ydtUtils;

    @Override
    public void loadData(@Nullable Context context, int count) {
        if (context == null) {
            LogUtils.e("拉取广告被终止,当前上下文已被销毁");
            return;
        }
        if (TextUtils.isEmpty(mConfig.subKey) || TextUtils
                .isEmpty(mConfig.platform)) {
            handleFailure(mConfig.platform, -1, "appkey or subKey or platform is invalid");
            return;
        }
        if (ydtUtils == null) {
            ydtUtils = new YdtUtils(context);
        }
        String body = ydtUtils.getBody(mConfig.subKey);
        Kalle.post(UrlConst.URL)
                .body(new StringBody(body))
                .perform(new StringCallback<String>(context) {
                    @Override
                    public void onResponse(SimpleResponse<String, String> response) {
                        AdResp entity = null;
                        if (response.isSucceed()) {
                            entity = AdResp.parseJson(response.succeed());
                        }
                        if (entity != null && entity.ads != null && !entity.ads.isEmpty()) {
                            if (mConfig.platform != null) {
                                handleSuccess(mConfig.platform, entity.ads);
                            }
                        } else {
                            if (mConfig.platform != null) {
                                handleFailure(mConfig.platform, -1, "没有可用物料");
                            }
                        }
                    }
                });
    }

    @Nullable
    @Override
    public Object getData(@Nullable final Context context) {
        if (context == null) {
            LogUtils.e("gdt取出广告被终止,当前上下文已被销毁");
            return null;
        }
        Object object = linkedQueue.poll();
        if (!isFast() && linkedQueue.size() < 3) {
            loadData(context, 3 - linkedQueue.size());
        }
        return object;
    }
}
