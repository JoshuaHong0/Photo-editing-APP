package com.meitu.meitutietie;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RadioButton;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ChooseStickerActivity extends AppCompatActivity implements View.OnClickListener {

    private RadioButton cuteButton;
    private RadioButton wordButton;
    private RadioButton faceButton;
    private RadioButton shadeButton;
    private RadioButton cartoonButton;

    private ImageButton moreMaterialBtn;

    private static final String CUTE = "cute";
    private static final String WORD = "word";
    private static final String FACE = "face";
    private static final String SHADE = "shade";
    private static final String CARTOON = "cartoon";
    private static final String ASSETS_PATH = "file:///android_asset/";
    private static final String FOLDER_NAME = "DownloadStickers";

    String[] folderNames = new String[]{CUTE, WORD, FACE, SHADE, CARTOON};

    private RecyclerView mRecyclerView;

    private ImageButton cancelButton;

    private Map<String, ArrayList<String>> imageFolders;
    private StickerAdapter mStickerAdapter;

    @Override
    protected void onResume() {
        super.onResume();

        initImageFolders();

        SharedPreferences prefs = getSharedPreferences("THEMES", MODE_PRIVATE);
        boolean defaultIsOn = prefs.getBoolean("defaultIsOn", true);

        if (defaultIsOn) { // 判断内置贴图是否被选中激活
            loadAssetFile();
        }

        Set<Integer> s = DBHelper.getValidThemes(this);

        loadSDcardFile(s);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mStickerAdapter = new StickerAdapter(this);
        mStickerAdapter.setImageFolders(imageFolders);
        mStickerAdapter.setFolderName(CUTE);
        mRecyclerView.setAdapter(mStickerAdapter);
        cuteButton.setChecked(true);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_sticker);

        // 隐藏上方状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initViews();
        initEvent();

    }

    private void initImageFolders() {
        imageFolders = new HashMap<>();
        for (String name : folderNames) {
            ArrayList<String> list = new ArrayList<>();
            imageFolders.put(name, list);
        }
    }


    /**
     * 从assets读取本地贴纸数据
     */
    private void loadAssetFile() {
        for (String name : folderNames) {

            try {
                String[] fileList = getAssets().list(name);
                for (String file: fileList) {
                    String filePath = ASSETS_PATH + name + File.separator + file;
                    imageFolders.get(name).add(filePath);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 读取下载的主题中的贴纸 (仅读取被激活的主题)
     */
    private void loadSDcardFile(Set<Integer> validThemes) {

        String targetDirectory = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + FOLDER_NAME;

        for (String name : folderNames) {
            String directory = targetDirectory + File.separator + name;
            File allFiles = new File(directory);
            File[] files = allFiles.listFiles();
            if (allFiles == null || files == null) continue;
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                int id = Integer.valueOf(file.getName().split("_")[0]);
                if (validThemes.contains(id)) {
                    imageFolders.get(name).add(file.getPath());
                }
            }
        }


    }

    /**
     * 初始化控件
     */
    private void initViews() {

        cuteButton = findViewById(R.id.cuteButton);
        wordButton = findViewById(R.id.wordButton);
        faceButton = findViewById(R.id.faceButton);
        shadeButton = findViewById(R.id.shadeButton);
        cartoonButton = findViewById(R.id.cartoonButton);
        mRecyclerView = findViewById(R.id.sticker_recyclerView);
        cancelButton = findViewById(R.id.cancelButton);
        moreMaterialBtn = findViewById(R.id.more_material_btn);
        cuteButton.setChecked(true);

    }

    private void initEvent() {
        cuteButton.setOnClickListener(this);
        wordButton.setOnClickListener(this);
        faceButton.setOnClickListener(this);
        shadeButton.setOnClickListener(this);
        cartoonButton.setOnClickListener(this);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        moreMaterialBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseStickerActivity.this, MoreMaterialsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cuteButton:
                select(0);
                break;
            case R.id.wordButton:
                select(1);
                break;
            case R.id.faceButton:
                select(2);
                break;
            case R.id.shadeButton:
                select(3);
                break;
            case R.id.cartoonButton:
                select(4);
                break;
            default:
                break;
        }
    }

    private void select(int i) {
        switch (i) {
            case 0:
                mStickerAdapter.setFolderName(CUTE);
                mRecyclerView.setAdapter(mStickerAdapter);
                break;
            case 1:
                mStickerAdapter.setFolderName(WORD);
                mRecyclerView.setAdapter(mStickerAdapter);
                break;
            case 2:
                mStickerAdapter.setFolderName(FACE);
                mRecyclerView.setAdapter(mStickerAdapter);
                break;
            case 3:
                mStickerAdapter.setFolderName(SHADE);
                mRecyclerView.setAdapter(mStickerAdapter);
                break;
            case 4:
                mStickerAdapter.setFolderName(CARTOON);
                mRecyclerView.setAdapter(mStickerAdapter);
                break;
            default:
                break;
        }
    }
}
