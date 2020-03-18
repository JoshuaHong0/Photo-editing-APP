package com.meitu.meitutietie;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 欢迎页面
 */
public class MainActivity extends AppCompatActivity {

    private ArrayList<Integer> images;
    private View lastView;
    ArrayList<Integer> backgrounds;

    private final String CUTE = "cute";
    private final String WORD = "word";
    private final String FACE = "face";
    private final String SHADE = "shade";
    private final String CARTOON = "cartoon";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        ViewPager mViewPager = findViewById(R.id.viewpager);
        PagerAdapter adapter = new MyAdapter(images, backgrounds, lastView, getApplicationContext());
        mViewPager.setAdapter(adapter);

        //实现底部TAB
        TabLayout mTabLayout = findViewById(R.id.tabDots);
        mTabLayout.setupWithViewPager(mViewPager, true);
    }

    /**
     * 初始化相同view的资源数组+实现最后一个页面的功能
     */
    public void init() {

        LayoutInflater inflater = getLayoutInflater();

        images = new ArrayList<>(Arrays.asList(
                R.drawable.guide_page_1,
                R.drawable.guide_page_2,
                R.drawable.guide_page_3,
                R.drawable.guide_page_5
        ));

        backgrounds = new ArrayList<>(Arrays.asList(
                R.drawable.guide_bg_1,
                R.drawable.guide_bg_1,
                R.drawable.guide_bg_1,
                R.drawable.guide_bg_2
        ));

        lastView = inflater.inflate(R.layout.guide5, null);

        // 实现确认页面功能
        implementGuide(lastView);

    }

    public void implementGuide(final View view) {

        final ImageButton startButton = view.findViewById(R.id.imageButtonStartApp);
        final ImageView startLogo = view.findViewById(R.id.StartLogo);
        final ImageView secondLogo = view.findViewById(R.id.secondLogo);
        final ImageView meituLogo = view.findViewById(R.id.meituLogo);
        final ImageView bottomImage = view.findViewById(R.id.bottomImage);
        final TabLayout mTabLayout = findViewById(R.id.tabDots);
        final CheckBox mCheckBox = view.findViewById(R.id.checkBoxStart);
        final ImageView background = view.findViewById(R.id.new_bg);

        // 淡出
        final ValueAnimator fadeOut = new ValueAnimator().ofFloat(1f, 0f);
        fadeOut.setDuration(1500);
        fadeOut.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currVal = (float) animation.getAnimatedValue();
                startLogo.setAlpha(currVal);
                startButton.setAlpha(currVal);
                mCheckBox.setAlpha(currVal);
            }
        });

        // 上移
        final ValueAnimator moveUp = new ValueAnimator().ofFloat(0f, -300f);
        moveUp.setDuration(1500);
        moveUp.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currVal = (float) animation.getAnimatedValue();
                startLogo.setTranslationY(currVal);
            }
        });

        // 下移
        final ValueAnimator moveDown = new ValueAnimator().ofFloat(0f, 300f);
        moveDown.setDuration(1500);
        moveDown.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currVal = (float) animation.getAnimatedValue();
                startButton.setTranslationY(currVal);
                mCheckBox.setTranslationY(currVal);
            }
        });

        // 过渡界面控件淡入淡出
        final ValueAnimator fadeIn = new ValueAnimator().ofFloat(0f, 1f, 0f);
        fadeIn.setDuration(3000);
        fadeIn.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currVal = (float) animation.getAnimatedValue();
                secondLogo.setAlpha(currVal);
                meituLogo.setAlpha(currVal);
                bottomImage.setAlpha(currVal);
                background.setAlpha(currVal);
            }
        });
        fadeIn.setStartDelay(1500);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 隐藏底部TAB
                mTabLayout.setVisibility(View.GONE);
                // 首界面淡出
                fadeOut.start();
                moveUp.start();
                moveDown.start();
                // 新界面淡入淡出
                fadeIn.start();
                // 跳转到导航页面
                switchPage();
            }
        });

    }

    // 延时页面跳转
    private void switchPage() {

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 4500);

    }


}
