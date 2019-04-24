package com.ark.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ark.adkit.basics.handler.Action;
import com.ark.adkit.basics.handler.Run;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

	private SimpleListAdapter adapter;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		RecyclerView recyclerView = findViewById(R.id.recyclerView);
		adapter = new SimpleListAdapter(new ArrayList<SimpleData>());
		recyclerView.setLayoutManager(
				new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
		recyclerView.setAdapter(adapter);
		adapter.setPreLoadNumber(6);
		adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
				back();
			}
		});
		adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
			@Override
			public void onLoadMoreRequested() {
				getData();
			}
		}, recyclerView);
		getData();
	}

	private void back() {
		if (!isFinishing()) {
			startActivity(new Intent(ListActivity.this, SplashActivity.class));
			finish();
		}
	}

	@Override
	public void onBackPressed() {
		back();
	}

	public void getData() {
		Run.onBackground(new Action() {
			@Override
			public void call() {
				final List<SimpleData> list = new ArrayList<>();
				for (int i = 0; i < 7; i++) {
					SimpleData simpleData = new SimpleData();
					simpleData.itemType = SimpleData.TYPE_DATA;
					list.add(simpleData);
				}
				SimpleData simpleData = new SimpleData();
				simpleData.itemType = SimpleData.TYPE_AD;
				list.add(simpleData);
				Run.onUiAsync(new Action() {
					@Override
					public void call() {
						adapter.addData(list);
						adapter.loadMoreComplete();
					}
				});
			}
		});
	}
}
