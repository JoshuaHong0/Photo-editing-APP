package com.meitu.meitutietie;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CustomDialogClass extends Dialog implements View.OnClickListener {

    private Context mContext;
    private ImageCropper mImageCropper;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog);

        TextView textView1 = findViewById(R.id.dialog_item_1);
        TextView textView2 = findViewById(R.id.dialog_item_2);
        TextView textView3 = findViewById(R.id.dialog_item_3);
        TextView textView4 = findViewById(R.id.dialog_item_4);
        TextView textView5 = findViewById(R.id.dialog_item_5);
        TextView textView6 = findViewById(R.id.dialog_item_6);

        textView1.setOnClickListener(this);
        textView2.setOnClickListener(this);
        textView3.setOnClickListener(this);
        textView4.setOnClickListener(this);
        textView5.setOnClickListener(this);
        textView6.setOnClickListener(this);

    }

    public CustomDialogClass(Context context) {
        super(context);
        this.mContext = context;
    }

    public CustomDialogClass(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected CustomDialogClass(Context context, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public void setImageCropper(ImageCropper mImageCropper) {
        this.mImageCropper = mImageCropper;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_item_1:
                mImageCropper.setCropRatio(mImageCropper.getOriginalRatio());
                button.setText("原图");
                this.dismiss();
                break;
            case R.id.dialog_item_2:
                mImageCropper.setCropRatio(mImageCropper.getViewRatio());
                button.setText("适应软件");
                this.dismiss();
                break;
            case R.id.dialog_item_3:
                mImageCropper.setCropRatio(0f);
                button.setText("任意");
                this.dismiss();
                break;
            case R.id.dialog_item_4:
                mImageCropper.setCropRatio(1);
                button.setText("1:1");
                this.dismiss();
                break;
            case R.id.dialog_item_5:
                mImageCropper.setCropRatio(0.75f);
                button.setText("3:4");
                this.dismiss();
                break;
            case R.id.dialog_item_6:
                this.dismiss();
        }
    }
}
