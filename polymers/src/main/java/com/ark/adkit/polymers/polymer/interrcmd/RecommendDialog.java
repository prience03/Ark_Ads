package com.ark.adkit.polymers.polymer.interrcmd;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.ark.adkit.basics.utils.AppUtils;
import com.ark.adkit.basics.utils.FileUtils;
import com.ark.adkit.basics.utils.LogUtils;
import com.ark.adkit.polymers.R;
import com.ark.adkit.polymers.polymer.ADTool;
import com.ark.adkit.polymers.self.SelfADStyle;
import com.ark.adkit.polymers.self.SelfDataRef;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.File;
import java.util.Random;

public class RecommendDialog extends Dialog implements View.OnClickListener {

    private SelfDataRef selfDataRef;
    private Context context;
    private TextView tvAppTitle, tvAppDesc, tvAppRank;
    private ImageView btnClose, appIcon;
    private Button btnInstall;
    private ViewGroup flDialog;

    public RecommendDialog(@NonNull Context context) {
        this(context, R.style.UpdateDialog);
    }

    public RecommendDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    public RecommendDialog setBundle(SelfDataRef selfDataRef) {
        this.selfDataRef = selfDataRef;
        init(context);
        return this;
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.sdk_dialog_ad_rcmd, null);
        setContentView(view);
        setWindowSize(context);
        initView(view);
    }

    private int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) {
            return context.getResources().getDisplayMetrics().widthPixels;
        }
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.getDefaultDisplay().getRealSize(point);
        } else {
            wm.getDefaultDisplay().getSize(point);
        }
        return point.x;
    }

    private void setWindowSize(Context context) {
        setCanceledOnTouchOutside(false);
        Window dialogWindow = this.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (getScreenWidth(context) * 0.75f);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialogWindow.setAttributes(lp);
    }

    private void initView(View view) {
        tvAppTitle = view.findViewById(R.id.tv_app_title);
        tvAppDesc = view.findViewById(R.id.tv_app_desc);
        tvAppRank = view.findViewById(R.id.tv_app_rank);
        btnClose = view.findViewById(R.id.btn_close);
        btnInstall = view.findViewById(R.id.btn_app_install);
        appIcon = view.findViewById(R.id.iv_app_icon);
        flDialog = view.findViewById(R.id.fl_dialog);
        Animation shake = AnimationUtils.loadAnimation(getContext(), R.anim.sdk_anim_shake);
        btnInstall.startAnimation(shake);
        btnClose.bringToFront();
        btnClose.setOnClickListener(this);
        btnInstall.setOnClickListener(this);
        if (selfDataRef != null) {
            ADTool.getADTool().getManager().loadImage(appIcon, selfDataRef.getAppLogo());
            tvAppTitle.setText(selfDataRef.getAppTitle());
            tvAppDesc.setText(selfDataRef.getAppDesc());
            Random random = new Random();
            int randNum = random.nextInt(9000) + 1000;
            tvAppRank.setText("(" + String.valueOf(randNum) + ")");
            selfDataRef.analysisView(SelfADStyle.INTER_RECOMMEND);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_close) {
            dismiss();
        } else if (i == R.id.btn_app_install) {
            if (selfDataRef != null && flDialog != null) {
                selfDataRef.analysisClick(SelfADStyle.INTER_RECOMMEND);
                selfDataRef.analysisDownload(SelfADStyle.INTER_RECOMMEND);
                String url = selfDataRef.getTargetUrl();
                Toast.makeText(context, "正在下载" + selfDataRef.getAppTitle(), Toast.LENGTH_LONG)
                        .show();
                File dir = FileUtils.getFileDir(context, "download");
                Ion.with(context)
                        .load(url)
                        .write(new File(dir, selfDataRef.getId() + ".apk"))
                        .setCallback(new FutureCallback<File>() {
                            @Override
                            public void onCompleted(Exception e, File result) {
                                if (result != null && result.exists()) {
                                    selfDataRef.recordInstall(SelfADStyle.INTER_RECOMMEND);
                                    AppUtils.installApk(context, result);
                                } else {
                                    LogUtils.e("下载失败" + e.getLocalizedMessage());
                                    Toast.makeText(context, selfDataRef.getAppTitle() + "下载失败",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
            dismiss();
        }
    }
}
