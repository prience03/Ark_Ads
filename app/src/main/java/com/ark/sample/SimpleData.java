package com.ark.sample;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class SimpleData implements MultiItemEntity {

    public static final int TYPE_AD = 111;
    public static final int TYPE_DATA = 222;

    public int itemType;


    @Override
    public int getItemType() {
        return itemType;
    }
}
