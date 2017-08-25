package com.brioal.versionupdate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.brioal.versionlib.ApkDownLoadBuild;

public class MainActivity extends AppCompatActivity {
    private String url = "https://res.wx.qq.com/open/zh_CN/htmledition/res/dev/download/sdk/Gen_Signature_Android2.apk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void downApk(View view) {
        ApkDownLoadBuild build = new ApkDownLoadBuild(this);
        build
                .setApkUrl(url)
                .build();
    }
}
