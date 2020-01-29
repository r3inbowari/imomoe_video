package com.r3inbowari.imo.base;

import android.graphics.Color;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.r3inbowari.imo.R;
import com.r3inbowari.imo.imomoeAPI.ImomoeSearch;
import com.r3inbowari.imo.utils.GlideRoundTransform;

import java.util.List;

public class HomeAdapter extends BaseQuickAdapter<ImomoeSearch, BaseViewHolder> {

    /**
     * 构造方法：
     *
     * @param layoutResId：
     * @param data：
     */
    public HomeAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }

    /**
     * 设置数据
     *
     * @param helper ：holder
     * @param item   :item的数据
     */
    @Override
    protected void convert(BaseViewHolder helper, ImomoeSearch item) {
        helper.setText(R.id.bangumi_item_alt, item.alt);//item布局的控件id、item数据
        helper.setText(R.id.bangumi_item_update, item.update);
        helper.setText(R.id.bangumi_item_detail, item.description);
        Glide.with(mContext).load(item.img).transform(new CenterCrop(helper.itemView.getContext()), new GlideRoundTransform(helper.itemView.getContext(), 3)).diskCacheStrategy(DiskCacheStrategy.ALL).crossFade().crossFade().into((ImageView) helper.getView(R.id.bangumi_item_image));

        //item布局的控件id、item数据

        helper.addOnClickListener(R.id.bangumi_item_alt);
        helper.addOnLongClickListener(R.id.bangumi_item_update);

        //getLayoutPosition() 获取当前item的position
//        if (helper.getAdapterPosition() % 2 == 0) {
//            helper.setTextColor(R.id.bangumi_item_alt, Color.RED);
//        } else {
//            helper.setTextColor(R.id.bangumi_item_update, Color.YELLOW);
//        }
    }

}
