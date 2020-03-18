package com.meitu.meitutietie;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MyThemeAdapter extends RecyclerView.Adapter<MyThemeAdapter.ViewHolder> {

    private ArrayList<StickerTheme> themeList;
    private final Activity mActivity;
    private static final String DEFAULT_STICKER = "内置贴图";
    private static final int DEFAULT_STICKER_CNT = 146;
    private boolean defaultStickersOn;
    private boolean editMode = false;

    public void setDefaultStickersOn(boolean defaultStickersOn) {
        this.defaultStickersOn = defaultStickersOn;
    }

    public void setThemeList(ArrayList<StickerTheme> themeList) {
        this.themeList = themeList;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public MyThemeAdapter(Activity activity) {
        mActivity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.my_theme_item_view, viewGroup, false);
        return new MyThemeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        if (i == 0) { // 内置贴图主题

            Glide.with(mActivity)
                    .load(mActivity.getResources().getIdentifier("icon_sucai_default", "drawable", mActivity.getPackageName()))
                    .override(150, 150)
                    .into(viewHolder.getThumbnail());
            viewHolder.getThemeName().setText(DEFAULT_STICKER);
            viewHolder.getCount().setText("("+DEFAULT_STICKER_CNT+"个)");
            viewHolder.getSwitch().setChecked(defaultStickersOn);

            // 编辑模式
            if (editMode) {
                viewHolder.getSwitch().setAlpha(0.0f);
                viewHolder.getSwitch().setEnabled(false);
                return;
            }

            // 正常模式
            viewHolder.getDeleteButtonLayout().setVisibility(View.GONE);
            viewHolder.getSwitch().setAlpha(1.0f);
            viewHolder.getSwitch().setEnabled(true);
            viewHolder.getSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    SharedPreferences.Editor editor = mActivity.getSharedPreferences("THEMES",mActivity.MODE_PRIVATE).edit();
                    if (isChecked) {
                        editor.putBoolean("defaultIsOn",true);
                        editor.apply();
                    } else {
                        editor.putBoolean("defaultIsOn",false);
                        editor.apply();
                    }
                }
            });

        } else { // 下载的主题

            // 获取主题信息
            final int index = i - 1;
            final StickerTheme theme = themeList.get(index);
            String thumbnailPath = theme.getThumbnailUrl();
            String name = theme.getName();
            int count = theme.getCount();
            boolean isOn = theme.isTurnOn();

            // 更新UI
            Glide.with(mActivity)
                    .load(thumbnailPath)
                    .override(150, 150)
                    .into(viewHolder.getThumbnail());
            viewHolder.getThemeName().setText(name);
            viewHolder.getCount().setText("("+count+"个)");

            // 编辑模式
            if (editMode) {
                viewHolder.getSwitch().setAlpha(0.0f);
                viewHolder.getSwitch().setEnabled(false);
                viewHolder.getDeleteButtonLayout().setVisibility(View.VISIBLE);
                return;
            }

            // 正常模式
            viewHolder.getDeleteButtonLayout().setVisibility(View.GONE);
            viewHolder.getSwitch().setAlpha(1.0f);
            viewHolder.getSwitch().setEnabled(true);
            viewHolder.getSwitch().setChecked(isOn);
            viewHolder.getSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        DBHelper.switchUpdate(mActivity, true, theme.getId());
                    } else {
                        DBHelper.switchUpdate(mActivity,false,theme.getId());
                    }
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return themeList.size() + 1;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView thumbnail;
        private final TextView themeName;
        private final TextView count;
        private final Switch mSwitch;
        private final FrameLayout deleteButtonLayout;
        private final ImageButton deleteButton;

        public ViewHolder(View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.my_theme_thumbnail);
            themeName = itemView.findViewById(R.id.my_theme_name);
            count = itemView.findViewById(R.id.my_theme_cnt);
            mSwitch = itemView.findViewById(R.id.my_theme_switch);
            deleteButtonLayout = itemView.findViewById(R.id.delete_btn_layout);
            deleteButton = itemView.findViewById(R.id.my_theme_delete_btn);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = getAdapterPosition();
                    int themeID = themeList.get(index - 1).getId();
                    themeList.remove(index - 1);
                    notifyItemRemoved(index);

                    DBHelper.deleteTheme(mActivity, themeID);
                }
            });

        }

        public ImageView getThumbnail() {
            return thumbnail;
        }

        public TextView getThemeName() {
            return themeName;
        }

        public TextView getCount() {
            return count;
        }

        public Switch getSwitch() {
            return mSwitch;
        }

        public FrameLayout getDeleteButtonLayout() {
            return deleteButtonLayout;
        }

        public ImageButton getDeleteButton() {
            return deleteButton;
        }
    }

}
