package com.meitu.meitutietie;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import java.io.File;

public class CropImageActivity extends AppCompatActivity {

    private ImageCropper mImageCropper;
    private CustomDialogClass customDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 隐藏上方状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // 通过传过来的图片地址获取Bitmap
        Intent intent = getIntent();
        String imagePath = intent.getStringExtra("ImagePath");

        File imageFile = new File(imagePath);
        Bitmap mBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());

        // 获取屏幕宽高
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int screenWidth = metric.widthPixels;
        int screenHeight = metric.heightPixels;

        // 剪裁框初始化设置
        setContentView(R.layout.activity_crop_image);
        mImageCropper = findViewById(R.id.imageCropper);
        mImageCropper.setScreenSize(screenWidth,screenHeight);
        mImageCropper.setBitmap(mBitmap);
        mImageCropper.setCropRatio(mImageCropper.getOriginalRatio());
        mImageCropper.setCropDrawable(new ColorDrawable(0x00000000));
        mImageCropper.setCoverDrawable(new ColorDrawable(0x88000000));
        mImageCropper.setMinCropDrawableRectSideLength(UnitHelper.dipToPx(this, 100));
        mImageCropper.setCropBorderWidth(UnitHelper.dipToPx(this, 10));
        mImageCropper.setMultiSamplingEnabled(true);

        ImageButton returnButton = findViewById(R.id.chooseRangeReturnButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageButton nextStep = findViewById(R.id.nextStepBtn);
        Button ratioButton = findViewById(R.id.ratioButton);

        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = mImageCropper.getCroppedBitmap();
                MemCache.put("BITMAP", bitmap);
                startActivity(new Intent(CropImageActivity.this, ViewCropImageActivity.class));
            }
        });

        // 设置dialog样式
        customDialog = new CustomDialogClass(CropImageActivity.this);
        customDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        customDialog.getWindow().setGravity(Gravity.BOTTOM);
        customDialog.getWindow().setDimAmount(0f);
        customDialog.setImageCropper(mImageCropper);
        customDialog.setButton(ratioButton);

        ratioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.show();
                WindowManager windowManager = getWindowManager();
                Display display = windowManager.getDefaultDisplay();
                WindowManager.LayoutParams lp = customDialog.getWindow().getAttributes();
                lp.width = display.getWidth();
                customDialog.getWindow().setAttributes(lp);
            }
        });

    }

}
