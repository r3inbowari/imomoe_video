package com.r3inbowari.imo.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.v7.widget.DrawableUtils;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.r3inbowari.imo.R;
import com.r3inbowari.imo.base.BaseRvAdapter;
import com.r3inbowari.imo.imomoeAPI.ImomoeSearch;
import com.r3inbowari.imo.ui.PlayerActivity;
import com.r3inbowari.imo.ui.viewholder.SearchListViewHolder;
import com.r3inbowari.imo.ui.viewholder.SearchBannerViewHolder;
import com.r3inbowari.imo.utils.GlideRoundTransform;

import java.util.ArrayList;

/**
 * Created by tanbiheng on 2018/1/24.
 */

public class SearchTitleBarAdapter extends BaseRvAdapter {
    private final int TYPE_BANNER = 1;
    private final int TYPE_NORMAL = 2;

    private Context context = null;
    private ArrayList<ImomoeSearch> dataList = null;

    private OnItemClickListener onItemClickListener;

    public SearchTitleBarAdapter(Context context, ArrayList<ImomoeSearch> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder holder = null;
        if (viewType == TYPE_BANNER) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_search_banner,
                    parent, false);
            holder = new SearchBannerViewHolder(view);
            ImageView a = holder.itemView.findViewById(R.id.image_title);
            a.setImageResource(R.mipmap.image1);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_search_list,
                    parent, false);
            holder = new SearchListViewHolder(view);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        if (position != 0) {
            TextView a = holder.itemView.findViewById(R.id.bangumi_item_alt2);
            TextView b = holder.itemView.findViewById(R.id.bangumi_item_detail2);
            TextView c = holder.itemView.findViewById(R.id.bangumi_item_update2);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(position);
                    }
                }
            });

            a.setText(dataList.get(position).alt);
            b.setText(dataList.get(position).description);
            c.setText(dataList.get(position).update);
            Glide.with(holder.itemView.getContext()).load(dataList.get(position).img).transform(new CenterCrop(holder.itemView.getContext()), new GlideRoundTransform(holder.itemView.getContext(), 3)).diskCacheStrategy(DiskCacheStrategy.ALL).crossFade().crossFade().into((ImageView) holder.itemView.findViewById(R.id.bangumi_item_image2));
        }
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_BANNER;
        } else {
            return TYPE_NORMAL;
        }
    }
}
