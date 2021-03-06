package com.brioal.versionlib;

import android.content.Context;
import android.content.Intent;

/**
 * email:brioal@foxmail.com
 * github:https://github.com/Brioal
 * Created by Brioal on 2017/7/30.
 */

public class ApkDownLoadBuild {
    private Context mContext;
    private String mApkUrl = "";//apk的下载地址


    public ApkDownLoadBuild(Context context) {
        mContext = context;
    }


    public ApkDownLoadBuild setApkUrl(String apkUrl) {
        mApkUrl = apkUrl;
        return this;
    }

    //开始下载
    public void build() {
        Intent intent = new Intent(mContext, ApkDownLoadService.class);
        intent.putExtra("mApkUrl", mApkUrl);
        mContext.startService(intent);
    }


}
