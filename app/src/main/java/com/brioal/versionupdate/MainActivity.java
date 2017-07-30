package com.brioal.versionupdate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.brioal.versionlib.ApkDownLoadBuild;

public class MainActivity extends AppCompatActivity {
    private String url = "http://001file.liqucn.com/upload/2014/xitong/gruv.poweroff2.poweroff2_2.0.0_liqucn.com.apk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void downApk(View view) {
        ApkDownLoadBuild build = new ApkDownLoadBuild(this);
        build.setApkName("test.apk")
                .setApkUrl(url)
                .build();
    }
}
