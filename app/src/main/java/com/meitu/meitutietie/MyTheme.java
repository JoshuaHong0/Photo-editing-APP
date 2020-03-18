package com.meitu.meitutietie;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class MyTheme extends AppCompatActivity {

    private TextView editText;
    private TextView returnText;
    private ImageButton returnButton;
    private ImageButton editButton;
    private RecyclerView mRecyclerView;
    private MyThemeAdapter mMyThemeAdapter;
    private ArrayList<StickerTheme> themeList;
    private boolean inEditMode = false;
    private static final String EDITING = "取消";
    private static final String EDIT_NORMAL = "编辑";
    @Override
    protected void onResume() {
        super.onResume();

        boolean isOn = getSharedPreferences("THEMES", MODE_PRIVATE)
                .getBoolean("defaultIsOn", true);

        mMyThemeAdapter = new MyThemeAdapter(this);
        themeList = DBHelper.getDataFromDatabase(this);
        mMyThemeAdapter.setThemeList(themeList);
        mMyThemeAdapter.setDefaultStickersOn(isOn);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mMyThemeAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                ((LinearLayoutManager) layoutManager).getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 切换编辑/正常模式

                if (!inEditMode) {
                    inEditMode = true;
                    mMyThemeAdapter.setEditMode(true);
                    mMyThemeAdapter.notifyDataSetChanged();
                    enterEditMode();
                } else {
                    mMyThemeAdapter.setEditMode(false);
                    mMyThemeAdapter.notifyDataSetChanged();
                    enterNormalMode();
                    inEditMode = false;
                }
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_theme);

        // 隐藏上方状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        returnButton = findViewById(R.id.my_theme_ReturnButton);
        editButton = findViewById(R.id.my_theme_editButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mRecyclerView = findViewById(R.id.my_theme_recyclerView);
        editButton = findViewById(R.id.my_theme_editButton);
        editText = findViewById(R.id.edit_button_textview);
        returnText = findViewById(R.id.my_theme_returnText);
    }


    /**
     * 根据不同模式更新UI
     */
    private void enterEditMode(){
        returnText.setAlpha(0.0f);
        returnButton.setAlpha(0.0f);
        returnButton.setEnabled(false);
        editText.setText(EDITING);
    }

    private void enterNormalMode() {
        returnButton.setEnabled(true);
        returnButton.setAlpha(1.0f);
        returnText.setAlpha(1.0f);
        editText.setText(EDIT_NORMAL);
    }


}
