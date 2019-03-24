package com.ark.sample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.ark.adkit.basics.handler.Action;
import com.ark.adkit.basics.handler.Run;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

public class ListAdFragment extends FullDialogFragment {

    private SimpleListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        adapter = new SimpleListAdapter(new ArrayList<SimpleData>());
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        adapter.setPreLoadNumber(6);
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getData();
            }
        }, recyclerView);
        getData();
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
