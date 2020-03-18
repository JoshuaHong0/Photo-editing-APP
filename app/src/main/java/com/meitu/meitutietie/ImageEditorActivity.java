package com.meitu.meitutietie;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import java.util.ArrayList;

/**
 * 相册选择页面
 */
public class ImageEditorActivity extends AppCompatActivity {

    private ImageButton returnButton;
    private RecyclerView mRecyclerView;
    private MediaStoreAdapter mMediaStoreAdapter;
    private ArrayList<ImageFolder> folders;
    private ChangeObserver changeObserver;

    @Override
    protected void onResume() {
        super.onResume();
        if(changeObserver.contentChanged()){
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

        setContentView(R.layout.activity_gallery);

        mRecyclerView = findViewById(R.id.imageRecyclerView);
        returnButton = findViewById(R.id.returnButton);

        // 返回上级界面
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mMediaStoreAdapter = new MediaStoreAdapter(this);

        /**
         * 图像点击事件监听 - 选择相册
         */
        mMediaStoreAdapter.setOnItemClickListener(new MediaStoreAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Intent intent = new Intent(ImageEditorActivity.this, ChooseImageActivity.class);
                intent.putExtra("position", String.valueOf(position));
                startActivity(intent);
            }
        });

        // 初次启动读取数据
        LoadData loadTask = new LoadData();
        loadTask.execute("MyType");

        // 注册媒体库更新监听
        changeObserver = new ChangeObserver(new Handler());
        getContentResolver().registerContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                true,changeObserver);

    }

    public class LoadData extends AsyncTask<String, String, String> {

        private Cursor cursor;

        @Override
        protected String doInBackground(String... strings) {
            // 数据查询
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
            // 查询数据后更新UI
            MediaDataManager mediaDataManager = MediaDataManager.getInstance();
            mediaDataManager.clear();
            mediaDataManager.decode(cursor);
            folders = mediaDataManager.getFolderList();
            mMediaStoreAdapter.setmImageFolders(folders);
            mRecyclerView.setAdapter(mMediaStoreAdapter);
            mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
        }
    }

}
