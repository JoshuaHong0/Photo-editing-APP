package com.meitu.meitutietie;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * 相册选择页面recyclerview适配器
 */

public class MediaStoreAdapter extends RecyclerView.Adapter<MediaStoreAdapter.ViewHolder> {

    private final Activity mActivity;
    private ArrayList<ImageFolder> mImageFolders;
    private static ClickListener clickListener;

    public MediaStoreAdapter(Activity mActivity) {
        this.mActivity = mActivity;
        mImageFolders = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.activity_gallery_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        ImageFolder imageFolder = mImageFolders.get(i);
        if (imageFolder.getNumOfImages() != 0) {
            String path = imageFolder.getFirstImagePath();
            Uri mediaUri = Uri.parse("file://" + path);
            Glide.with(mActivity)
                    .load(mediaUri)
                    .centerCrop()
                    .override(210, 210)
                    .into(viewHolder.getImageView());
        }
        viewHolder.getFilePath().setText(imageFolder.getDir());
        viewHolder.getNumOfImages().setText("(" + imageFolder.getNumOfImages() + ")");
        viewHolder.getFileName().setText(imageFolder.getFolderName());
    }

    @Override
    public int getItemCount() {
        return mImageFolders.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final ImageView mImageView;
        private final TextView fileName;
        private final TextView filePath;
        private final TextView numOfImages;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mImageView = itemView.findViewById(R.id.folderImage);
            fileName = itemView.findViewById(R.id.folderName);
            filePath = itemView.findViewById(R.id.folderPath);
            numOfImages = itemView.findViewById(R.id.numOfImages);
        }

        public ImageView getImageView() {
            return mImageView;
        }

        public TextView getFileName() {
            return fileName;
        }

        public TextView getFilePath() {
            return filePath;
        }

        public TextView getNumOfImages() {
            return numOfImages;
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setmImageFolders(ArrayList<ImageFolder> mImageFolders) {
        this.mImageFolders = mImageFolders;
    }

    public interface ClickListener {
        void onItemClick (int position, View v);
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        MediaStoreAdapter.clickListener = clickListener;
    }
}
