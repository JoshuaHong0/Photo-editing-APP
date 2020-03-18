package com.meitu.meitutietie;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import okhttp3.Response;

public class DetailedMaterialActivity extends AppCompatActivity {

    private ImageView thumbnail;
    private WebView preview;
    private TextView title;
    private TextView description;
    private ImageButton returnButton;
    private Button downloadButton;
    private ProgressBar mProgressBar;
    private MyDatabaseHelper dbHelper;
    private FinishDownloadDialog mDialog;

    private static final String CUTE = "cute";
    private static final String WORD = "word";
    private static final String FACE = "face";
    private static final String SHADE = "shade";
    private static final String CARTOON = "cartoon";

    private static final String CUTE_PREFIX = "keai";
    private static final String WORD_PREFIX = "wenzi";
    private static final String FACE_PREFIX = "gaoxiaobiaoqing";
    private static final String SHADE_PREFIX = "zhedang";
    private static final String CARTOON_PREFIX = "katongxingxiang";
    private static final String FOLDER_NAME = "DownloadStickers";

    private int index = 0;
    private boolean maxSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_material);

        // 隐藏上方状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // 初始化页面控件
        initView();

        // 获取从上个页面传来的主题包相关数据
        Intent intent = getIntent();
        final String thumbnailPath = intent.getStringExtra("thumbnailPath"); //
        final String name = intent.getStringExtra("name"); //
        final String previewPath = intent.getStringExtra("preview");
        float size = (float) intent.getIntExtra("size", 0);
        final int count = intent.getIntExtra("count", 0); //
        final int id = intent.getIntExtra("id", 0);
        final String zipUrl = intent.getStringExtra("zipUrl");

        // 主题id - 用于追踪主题是否被下载
        index = id;

        size /= 1.049e+6;

        // 填充控件内容
        Glide.with(this)
                .load(thumbnailPath)
                .into(thumbnail);

        preview.getSettings().setJavaScriptEnabled(true);
        preview.loadUrl(previewPath);
        title.setText(name);
        description.setText(String.format("%.2f", size) + "M " + count + "个");
        mDialog.setThemeName(name);

        if (DBHelper.queryDownload(id, DetailedMaterialActivity.this)) {
            downloadButton.setEnabled(false);
            downloadButton.setText("已下载");
            downloadButton.setTextColor(Color.parseColor("#B0B0B0"));
            downloadButton.setBackground(getResources().getDrawable(R.drawable.round_corner_pressed));
        }

        // 点击下载按钮事件
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mProgressBar.setAlpha(1.0f);
                downloadButton.setAlpha(0.0f);

                // 下载zip并解压
                OkGo.getInstance().init(getApplication());
                OkGo.get(zipUrl)
                        .tag(this)
                        .connTimeOut(30000)
                        .execute(fileCallback);

                // 将主题包已下载状态存入数据库
                DBHelper.addDownload(
                        id,
                        thumbnailPath,
                        name,
                        count,
                        DetailedMaterialActivity.this
                );

            }
        });

    }

    /**
     * 初始化控件
     */
    private void initView() {
        thumbnail = findViewById(R.id.detail_thumbnail);
        preview = findViewById(R.id.detail_preview);
        title = findViewById(R.id.detail_title);
        description = findViewById(R.id.detail_description);
        returnButton = findViewById(R.id.detailedMaterialReturnButton);
        downloadButton = findViewById(R.id.detail_download_button);
        mProgressBar = findViewById(R.id.progressBar);
        mDialog = new FinishDownloadDialog(DetailedMaterialActivity.this);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    public FileCallback fileCallback = new FileCallback() {

        @Override
        public void onSuccess(File file, okhttp3.Call call, Response response) {
            try {
                initFolders();
                unzip(file, Environment.getExternalStorageDirectory().getAbsolutePath()
                        + File.separator + FOLDER_NAME);
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(okhttp3.Call call, Response response, Exception e) {
            super.onError(call, response, e);
            Log.d("Hzz", String.valueOf(e));
            response.close();
        }

        @Override
        public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
            super.downloadProgress(currentSize, totalSize, progress, networkSpeed);
            if (!maxSet) {
                mProgressBar.setMax((int) totalSize);
                maxSet = true;
            }
            mProgressBar.setProgress((int) currentSize);

            if (currentSize == totalSize) {
                mDialog.show();
                mProgressBar.setAlpha(0.0f);
                downloadButton.setAlpha(1.0f);
                downloadButton.setEnabled(false);
                downloadButton.setText("已下载");
                downloadButton.setTextColor(Color.parseColor("#B0B0B0"));
                downloadButton.setBackground(getResources().getDrawable(R.drawable.round_corner_pressed));
            }
        }

    };

    /**
     * 文件解压缩
     *
     * @param zipFile
     * @param targetDirectory
     * @throws IOException
     */
    public void unzip(File zipFile, String targetDirectory) throws IOException {
        ZipInputStream zis = new ZipInputStream(
                new BufferedInputStream(new FileInputStream(zipFile)));
        try {
            ZipEntry ze;
            int count;
            byte[] buffer = new byte[8192];
            while ((ze = zis.getNextEntry()) != null) {

                String filename = ze.getName();
                String path;
                if (filename.startsWith(CUTE_PREFIX)) {
                    path = targetDirectory + "/" + CUTE;
                } else if (filename.startsWith(FACE_PREFIX)) {
                    path = targetDirectory + "/" + FACE;
                } else if (filename.startsWith(WORD_PREFIX)) {
                    path = targetDirectory + "/" + WORD;
                } else if (filename.startsWith(SHADE_PREFIX)) {
                    path = targetDirectory + "/" + SHADE;
                } else if (filename.startsWith(CARTOON_PREFIX)) {
                    path = targetDirectory + "/" + CARTOON;
                } else {
                    continue;
                }

                File target = new File(path);

                File file = new File(target, index + "_" + ze.getName());
                File dir = ze.isDirectory() ? file : file.getParentFile();
                if (!dir.isDirectory() && !dir.mkdirs())
                    throw new FileNotFoundException("Failed to ensure directory: " +
                            dir.getAbsolutePath());
                if (ze.isDirectory())
                    continue;
                FileOutputStream fout = new FileOutputStream(file);
                try {
                    while ((count = zis.read(buffer)) != -1)
                        fout.write(buffer, 0, count);
                } finally {
                    fout.close();
                }
            }
        } finally {
            zis.close();
        }
    }


    /**
     * 在sd卡中创建用于储存下载内容的文件夹
     */
    public void initFolders() {

        String directory = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + FOLDER_NAME;

        File mainFolder = new File(directory);

        if (!mainFolder.exists()) {
            mainFolder.mkdirs();
        }

        String[] subFolderNames = new String[]{CUTE, WORD, FACE, SHADE, CARTOON};

        for (String subFolderName : subFolderNames) {
            String path = directory + File.separator + subFolderName;
            File subFolder = new File(path);
            if (!subFolder.exists()) {
                subFolder.mkdirs();
            }
        }

    }


}
