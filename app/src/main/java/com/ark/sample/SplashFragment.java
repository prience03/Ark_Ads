package com.ark.sample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SplashFragment extends FullDialogFragment {

    private OnPreparedListener mListener;

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
