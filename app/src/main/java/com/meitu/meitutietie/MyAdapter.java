package com.meitu.meitutietie;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;


/**
 * Viewpager 适配器
 */
public class MyAdapter extends PagerAdapter {

    private Queue<View> viewPool;
    private ArrayList<Integer> imageList;
    private ArrayList<Integer> backgrounds;
    private LayoutInflater mLayoutInflater;
    private View lastView;

    public MyAdapter(ArrayList<Integer> images, ArrayList<Integer> backgrounds, View lastView, Context mContext) {
        this.lastView = lastView;
        this.imageList = images;
        this.backgrounds = backgrounds;
        viewPool = new LinkedList<>();
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return imageList.size() + 1;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
        if (position != 4) viewPool.offer((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view;
        ViewHolder viewHolder;

        if (position == 4) { // 独立处理唯一一个不同的view
            container.addView(lastView);
            return lastView;
        }

        // 如果view池非空，则从view池中取用，否则inflate一个新的view
        if (viewPool.size() > 0) {
            view = viewPool.poll();
            viewHolder = (ViewHolder) view.getTag();
        } else {
            view = mLayoutInflater.inflate(R.layout.guide1, container, false);
            viewHolder = new ViewHolder();
            viewHolder.imageView = view.findViewById(R.id.imageView);
            view.setTag(viewHolder);
        }

        // 修改对应view的图像与背景
        viewHolder.imageView.setImageResource(imageList.get(position));
        view.setBackgroundResource(backgrounds.get(position));
        container.addView(view);
        return view;
    }

    public final class ViewHolder {
        ImageView imageView;
    }


}
