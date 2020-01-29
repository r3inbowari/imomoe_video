package com.r3inbowari.imo.base;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.r3inbowari.imo.R;
import com.r3inbowari.imo.imomoeAPI.ImomoeBangumiSource;

import java.util.List;

public class PlayerAdapter extends BaseQuickAdapter<ImomoeBangumiSource, BaseViewHolder> {
    /**
     * 构造方法：
     *
     * @param layoutResId：
     * @param data：
     */
    public PlayerAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }

    /**
     * 设置数据
     *
     * @param helper ：holder
     * @param item   :item的数据
     */
    @Override
    protected void convert(BaseViewHolder helper, ImomoeBangumiSource item) {
        helper.setText(R.id.bangumi_id, "第" + (helper.getLayoutPosition()) + "话");//item布局的控件id、item数据

        //item布局的控件id、item数据

        helper.addOnClickListener(R.id.bangumi_id);

    }
}
