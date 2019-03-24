package com.ark.sample;

import android.widget.FrameLayout;
import com.ark.adkit.polymers.polymer.ADTool;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class SimpleListAdapter extends BaseMultiItemQuickAdapter<SimpleData, BaseViewHolder> {

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public SimpleListAdapter(List<SimpleData> data) {
        super(data);
        addItemType(SimpleData.TYPE_AD, R.layout.item_simple_list);
        addItemType(SimpleData.TYPE_DATA, R.layout.item_simple_list);
    }

    @Override
    protected void convert(BaseViewHolder helper, SimpleData item) {
        FrameLayout frameLayout = helper.getView(R.id.root_view);
        switch (helper.getItemViewType()) {
            case SimpleData.TYPE_AD:
                frameLayout.removeAllViews();
                ADTool.getADTool().getManager()
                        .getNativeWrapper()
                        .loadVideoView(mContext, frameLayout);
                break;
            case SimpleData.TYPE_DATA:
                break;
        }
    }
}
