package com.ark.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.ark.adkit.basics.models.OnSplashImpl;
import com.ark.adkit.polymers.polymer.ADTool;

public class SplashActivity extends AppCompatActivity {


	private OnSplashImpl mOnSplashImpl = new OnSplashImpl() {
		@Override
		public void onAdTimeTick(long tick) {

		}

		@Override
		public void onAdShouldLaunch() {

			if (!isFinishing()) {
				startActivity(new Intent(SplashActivity.this, MainActivity.class));
				finish();
			}
		}
	};

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		FrameLayout rootView = findViewById(R.id.rootView);
		ViewGroup adContainer = findViewById(R.id.adContainer);
		if (!isTaskRoot()) {
			finish();
			return;
		}
		loadSplash(rootView, adContainer);
	}


	private void loadSplash(final FrameLayout rootView, final ViewGroup adContainer) {
		ADTool.getADTool()
				.getManager()
				.getSplashWrapper()
				.needPermissions(true)
				.loadSplash(this, adContainer, rootView, mOnSplashImpl);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		if (!isFinishing()) {
			startActivity(new Intent(SplashActivity.this, MainActivity.class));
			finish();
		}
	}
}
