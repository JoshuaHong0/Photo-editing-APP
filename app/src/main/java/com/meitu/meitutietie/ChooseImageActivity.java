package com.meitu.meitutietie;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * 进入某一相册后的选择页面
 */
public class ChooseImageActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ArrayList<String> imagePaths;
    private ImageButton returnButton2;
    private TextView header;
    private String position;
    private ChangeObserver changeObserver;
    private ChooseImageAdapter chooseImageAdapter;

    @Override
    protected void onResume() {
        super.onResume();
        if (changeObserver.contentChanged()) {
            LoadData loadTask = new LoadData();
            loadTask.execute();
            changeObserver.setContentChanged(false);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 隐藏上方状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.media_layout);

        imagePaths = new ArrayList<>(); // 此相册中图像路径集合
        returnButton2 = findViewById(R.id.returnButton2); // 左上角返回按钮
        header = findViewById(R.id.headerText); // Header文字

        returnButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 传入上个界面点击item的id
        Intent intent = getIntent();
        position = intent.getStringExtra("position");

        // 获取对应的ImageFolder对象
        ImageFolder imageFolder = MediaDataManager.getInstance().getFolderList().get(Integer.valueOf(position));
        header.setText(imageFolder.getFolderName());
        // 获取该文件夹下图片路径集合
        imagePaths = imageFolder.getImagePaths();

        mRecyclerView = findViewById(R.id.imageRecyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        chooseImageAdapter = new ChooseImageAdapter(this, imagePaths); // pass something here
        chooseImageAdapter.setmRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(chooseImageAdapter);

        // 注册媒体库更新监听
        changeObserver = new ChangeObserver(new Handler());
        getContentResolver().registerContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                true, changeObserver);
    }


    public class LoadData extends AsyncTask<String, String, String> {

        private Cursor cursor;

        @Override
        protected String doInBackground(String... strings) {
            String[] projection = {
                    MediaStore.Files.FileColumns._ID,
                    MediaStore.Files.FileColumns.DATE_ADDED,
                    MediaStore.Files.FileColumns.DATA,
                    MediaStore.Files.FileColumns.MEDIA_TYPE,
                    MediaStore.Images.ImageColumns.BUCKET_ID
            };
            String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                    + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
            cursor = getApplicationContext().getContentResolver().query(
                    MediaStore.Files.getContentUri("external"),
                    projection,
                    selection,
                    null,
                    MediaStore.Files.FileColumns.DATE_ADDED + " DESC");

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // 子线程查询数据后在主线程更新UI
            MediaDataManager mediaDataManager = MediaDataManager.getInstance();
            mediaDataManager.clear();
            mediaDataManager.decode(cursor);
            ImageFolder imageFolder = MediaDataManager.getInstance().getFolderList().get(Integer.valueOf(position));
            header.setText(imageFolder.getFolderName());
            imagePaths = imageFolder.getImagePaths();
            chooseImageAdapter.setImagePaths(imagePaths);
            mRecyclerView.setAdapter(chooseImageAdapter);
        }
    }

}
