package com.meitu.meitutietie;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * 下载完成提示框
 */
public class FinishDownloadDialog extends Dialog {

    private String themeName;
    private TextView info;
    private Button confirmButton;

    public FinishDownloadDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finish_download_dialog);

        info = findViewById(R.id.finsih_info);
        confirmButton = findViewById(R.id.confirm_btn);

        info.setText(themeName+"已被添加到各个分类素材下。");

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

}
