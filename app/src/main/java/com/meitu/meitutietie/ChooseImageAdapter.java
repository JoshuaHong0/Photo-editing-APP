package com.meitu.meitutietie;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * 进入某个相册后图片选择页面适配器
 */
public class ChooseImageAdapter extends RecyclerView.Adapter<ChooseImageAdapter.ViewHolder> {

    private Activity activity;
    private ArrayList<String> imagePaths;
    private RecyclerView mRecyclerView;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int index = mRecyclerView.getChildLayoutPosition(v);
            String imagePath = imagePaths.get(index);
            Intent intent = new Intent(activity,CropImageActivity.class);
            intent.putExtra("ImagePath",imagePath);
            activity.startActivity(intent);
        }
    };

    public ChooseImageAdapter(Activity activity, ArrayList<String> imagePaths) {
        this.activity = activity;
        this.imagePaths = imagePaths;
    }

    public void setImagePaths(ArrayList<String> imagePaths) {
        this.imagePaths = imagePaths;
    }

    public void setmRecyclerView(RecyclerView mRecyclerView) {
        this.mRecyclerView = mRecyclerView;
    }

    @NonNull
    @Override
    public ChooseImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.media_layout_view, viewGroup, false);
        view.setOnClickListener(mOnClickListener);
        return new ChooseImageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChooseImageAdapter.ViewHolder viewHolder, int i) {
        String path = imagePaths.get(i);
        Uri mediaUri = Uri.parse("file://" + path);
        Glide.with(activity)
                .load(mediaUri)
                .centerCrop()
                .override(250, 250)
                .into(viewHolder.getImageView());
    }

    @Override
    public int getItemCount() {
        return imagePaths.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.mediastoreImageView);
        }

        public ImageView getImageView() {
            return imageView;
        }
    }
}
