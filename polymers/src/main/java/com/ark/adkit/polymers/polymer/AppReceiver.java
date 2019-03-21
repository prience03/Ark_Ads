package com.ark.adkit.polymers.polymer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ark.adkit.basics.utils.LogUtils;
import com.ark.adkit.polymers.self.SelfUtils;
import com.ark.adkit.polymers.ydt.utils.InstallUtils;
import com.ark.adkit.polymers.ydt.utils.Logger;

public class AppReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getData() != null) {
            if (TextUtils.equals(intent.getAction(), Intent.ACTION_PACKAGE_ADDED)) {
                String packageName = intent.getData().getSchemeSpecificPart();
                LogUtils.i("安装" + packageName);
                SelfUtils.anaInstall(packageName);
                InstallUtils.Data data = InstallUtils.getData(packageName);
                if (data != null) {
                    for (String s : data.installUrls) {
                        doGet(context, s, "安装成功统计成功");
                    }
                }
            } else if (TextUtils.equals(intent.getAction(), Intent.ACTION_PACKAGE_REPLACED)) {
                String packageName = intent.getData().getSchemeSpecificPart();
                LogUtils.i("替换" + packageName);
            } else if (TextUtils.equals(intent.getAction(), Intent.ACTION_PACKAGE_REMOVED)) {
                String packageName = intent.getData().getSchemeSpecificPart();
                LogUtils.i("卸载" + packageName);
            }
        }
    }

    private void doGet(Context mContext, String url, final String msg) {
        AQuery aQuery=new AQuery(mContext);
        aQuery.ajax(url,String.class,new AjaxCallback<String>(){
            @Override
            public void callback(String url, String object, AjaxStatus status) {
                super.callback(url, object, status);
                Logger.e("logger", msg + "," + object);
            }
        });
    }
}
