package com.meitu.meitutietie;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ViewCropImageActivity extends AppCompatActivity {

    private LinearLayout trinketButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 隐藏上方状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_view_crop_image);

        trinketButton = findViewById(R.id.trinketButton);

        trinketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewCropImageActivity.this, ChooseStickerActivity.class);
                startActivity(intent);
            }
        });

        ImageView imageView = findViewById(R.id.imageView);
        imageView.setImageBitmap((Bitmap) MemCache.get("BITMAP"));

    }
}
