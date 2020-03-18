package com.meitu.meitutietie;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

/**
 * 导航页面/主界面
 */
public class NavigatorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 隐藏上方状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.navigator);


        // 仅在初次启动app显示欢迎页
        boolean isFirstLaunch = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstLaunch", true);

        if (isFirstLaunch) {
            startActivity(new Intent(NavigatorActivity.this, MainActivity.class));
        }

        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putBoolean("isFirstLaunch", false).commit();


        if (Build.VERSION.SDK_INT >= 23) {
            initPermission();
        }

        final ImageButton mainButton1 = findViewById(R.id.mainButton1);
        final ImageButton mainButton2 = findViewById(R.id.mainButton2);
        ImageButton mainButton3 = findViewById(R.id.mainButton3);
        ImageButton mainButton4 = findViewById(R.id.mainButton4);
        ImageButton mainIcon1 = findViewById(R.id.mainIcon1);
        ImageButton mainIcon2 = findViewById(R.id.mainIcon2);

        mainButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NavigatorActivity.this, ImageEditorActivity.class);
                startActivity(intent);
            }
        });

    }


    String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    List<String> mPermissionList = new ArrayList<>();

    private final int mRequestCode = 100;//权限请求码

    //权限判断和申请
    private void initPermission() {

        mPermissionList.clear();//清空没有通过的权限

        //逐个判断你要的权限是否已经通过
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);//添加还未授予的权限
            }
        }

        //申请权限
        if (mPermissionList.size() > 0) {//有权限没有通过，需要申请
            ActivityCompat.requestPermissions(this, permissions, mRequestCode);
        } else {
//            initFolders();
        }
    }

    //请求权限后回调的方法
    //参数： requestCode  是我们自己定义的权限请求码
    //参数： permissions  是我们请求的权限名称数组
    //参数： grantResults 是我们在弹出页面后是否允许权限的标识数组，数组的长度对应的是权限名称数组的长度，数组的数据0表示允许权限，-1表示我们点击了禁止权限
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean hasPermissionDismiss = false;//有权限没有通过
        if (mRequestCode == requestCode) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == -1) {
                    hasPermissionDismiss = true;
                }
            }
            //如果有权限没有被允许
            if (hasPermissionDismiss) {
                showPermissionDialog();//跳转到系统设置权限页面，或者直接关闭页面，不让他继续访问
            } else {
//                initFolders();
            }
        }

    }

    /**
     * 不再提示权限时的展示对话框
     */
    AlertDialog mPermissionDialog;
    String mPackName = "com.meitu.meitutietie";

    private void showPermissionDialog() {
        if (mPermissionDialog == null) {
            mPermissionDialog = new AlertDialog.Builder(this)
                    .setMessage("已禁用权限，请手动授予")
                    .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cancelPermissionDialog();

                            Uri packageURI = Uri.parse("package:" + mPackName);
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //关闭页面或者做其他操作
                            cancelPermissionDialog();

                        }
                    })
                    .create();
        }
        mPermissionDialog.show();
    }

    //关闭对话框
    private void cancelPermissionDialog() {
        mPermissionDialog.cancel();
    }

}
