package com.ark.adkit.polymers.ydt;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.ark.adkit.basics.handler.Action;
import com.ark.adkit.basics.handler.Run;
import com.ark.adkit.basics.models.ADNativeModel;
import com.ark.adkit.basics.utils.LogUtils;
import com.ark.adkit.polymers.ydt.constant.UrlConst;
import com.ark.adkit.polymers.ydt.entity.AdResp;
import com.ark.adkit.polymers.ydt.utils.YdtUtils;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.AsyncHttpPost;
import com.koushikdutta.ion.Ion;

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
        Ion.with(context)
                .load(AsyncHttpPost.METHOD, UrlConst.URL)
                .setStringBody(body)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        AdResp entity = null;
                        if (!TextUtils.isEmpty(result)) {
                            entity = AdResp.parseJson(result);
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
        if (!isFast() && linkedQueue.peek() == null) {
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    loadData(context, 3);
                }
            });
        }
        return object;
    }
}
