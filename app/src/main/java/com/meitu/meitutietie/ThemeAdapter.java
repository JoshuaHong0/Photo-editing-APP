package com.meitu.meitutietie;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ThemeAdapter extends RecyclerView.Adapter {

    private static final int ITEM_VIEW_TYPE_HEADER = 0;
    private static final int ITEM_VIEW_TYPE_ITEM = 1;

    private Activity activity;
    private ArrayList<StickerTheme> itemList;
    private ArrayList<StickerTheme> headerList;
    private RecyclerView mRecyclerView;


    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int index = mRecyclerView.getChildLayoutPosition(v) - 1;
            StickerTheme theme = itemList.get(index);
            nextPage(theme);
        }
    };


    public ThemeAdapter(Activity activity) {
        this.activity = activity;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        if (viewType == ITEM_VIEW_TYPE_HEADER) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.sticker_theme_header_view, viewGroup, false);
            return new ThemeAdapter.HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.sticker_theme_item_view, viewGroup, false);
            view.setOnClickListener(mOnClickListener);
            return new ThemeAdapter.ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {


        if (getItemViewType(i) == ITEM_VIEW_TYPE_HEADER) {
            String leftPath = headerList.get(0).getTopThumbnailUrl();
            final String rightPath = headerList.get(1).getTopThumbnailUrl();
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) viewHolder;
            Glide.with(activity)
                    .load(leftPath)
                    .override(450, 400)
                    .into(headerViewHolder.getLeftImage());

            Glide.with(activity)
                    .load(rightPath)
                    .override(450, 400)
                    .into(headerViewHolder.getRightImage());


            headerViewHolder.getLeftImage().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StickerTheme leftTheme = headerList.get(0);
                    nextPage(leftTheme);
                }
            });

            headerViewHolder.getRightImage().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StickerTheme rightTheme = headerList.get(1);
                    nextPage(rightTheme);
                }
            });


        } else {
            String path = itemList.get(i - 1).getThumbnailUrl();
            String name = itemList.get(i - 1).getName();
            ItemViewHolder itemViewHolder = (ItemViewHolder) viewHolder;

            Glide.with(activity)
                    .load(path)
                    .override(150, 150)
                    .into(itemViewHolder.getThumbnail());

            itemViewHolder.getName().setText(name);

            if (itemList.get(i - 1).isDownloaded()) {
                itemViewHolder.getFree().setText("已下载");
                itemViewHolder.getFree().setTextColor(Color.parseColor("#DCDCDC"));
            } else {
                itemViewHolder.getFree().setTextColor(Color.parseColor("#808080"));
                itemViewHolder.getFree().setText(itemList.get(i - 1).isFree() ? "免费" : "付费");
            }
        }
    }


    @Override
    public int getItemViewType(int position) {
        return position == 0 ? ITEM_VIEW_TYPE_HEADER : ITEM_VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return (!itemList.isEmpty() && !headerList.isEmpty()) ? itemList.size() + 1 : 0;
    }

    public void setItemList(ArrayList<StickerTheme> itemList) {
        this.itemList = itemList;
    }

    public void setHeaderList(ArrayList<StickerTheme> headerList) {
        this.headerList = headerList;
    }


    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        private ImageView leftImage;
        private ImageView rightImage;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            leftImage = itemView.findViewById(R.id.leftTheme);
            rightImage = itemView.findViewById(R.id.rightTheme);
        }

        public ImageView getLeftImage() {
            return leftImage;
        }

        public ImageView getRightImage() {
            return rightImage;
        }
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView thumbnail;
        private TextView name;
        private TextView free;

        public ItemViewHolder(View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.theme_thumbnail);
            name = itemView.findViewById(R.id.theme_name);
            free = itemView.findViewById(R.id.is_free);
        }

        public ImageView getThumbnail() {
            return thumbnail;
        }

        public TextView getName() {
            return name;
        }

        public TextView getFree() {
            return free;
        }
    }


    private void nextPage(StickerTheme theme) {
        Intent intent = new Intent(activity, DetailedMaterialActivity.class);
        intent.putExtra("thumbnailPath", theme.getThumbnailUrl());
        intent.putExtra("size", theme.getZipSize());
        intent.putExtra("count", theme.getCount());
        intent.putExtra("name", theme.getName());
        intent.putExtra("preview", theme.getPreviewUrl());
        intent.putExtra("id",theme.getId());
        intent.putExtra("zipUrl",theme.getZipUrl());
        activity.startActivity(intent);
    }




}
