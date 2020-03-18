package com.meitu.meitutietie;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Map;

public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.ViewHolder> {

    private final Activity mActivity;
    private Map<String, ArrayList<String>> imageFolders;
    private String folderName;

    public void setImageFolders(Map<String, ArrayList<String>> imageFolders) {
        this.imageFolders = imageFolders;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public StickerAdapter(Activity activity) {
        mActivity = activity;
    }


    @Override
    public ViewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.choose_sticker_itemview, viewGroup, false);
        return new StickerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        String path = imageFolders.get(folderName).get(i);
        Glide.with(mActivity)
                .load(path)
                .into(viewHolder.getSticker());
    }

    @Override
    public int getItemCount() {
        return imageFolders.get(folderName) == null ? 0 : imageFolders.get(folderName).size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView sticker;

        public ViewHolder(View itemView) {
            super(itemView);
            sticker = itemView.findViewById(R.id.stickerImageView);
        }

        public ImageView getSticker() {
            return sticker;
        }
    }
}
