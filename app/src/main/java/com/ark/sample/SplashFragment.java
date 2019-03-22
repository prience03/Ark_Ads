package com.ark.sample;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

public class SplashFragment extends DialogFragment {

    private OnPreparedListener mListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, R.style.Dialog_FullScreen);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        Window window;
        if (dialog != null && (window = dialog.getWindow()) != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            window.setLayout(width, height);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.splash_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewGroup rootView;
        ViewGroup adContainer;
        rootView = view.findViewById(R.id.root_view);
        adContainer = view.findViewById(R.id.ad_container);
        if (mListener != null) {
            mListener.onCreateView(rootView, adContainer);
        }
    }

    public void setOnPreparedListener(OnPreparedListener impl) {
        this.mListener = impl;
    }

    @Override
    public void dismiss() {
        dismissAllowingStateLoss();
    }

    public interface OnPreparedListener {

        void onCreateView(ViewGroup rootView, ViewGroup adContainer);
    }
}
