package com.brioal.versionlib;

import android.app.DownloadManager;
import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;

/**
 * email:brioal@foxmail.com
 * github:https://github.com/Brioal
 * Created by Brioal on 2017/7/30.
 */

public class ApkDownLoadService extends IntentService {
    private long mTaskId = -1;//下载任务的ID
    private DownloadManager mDownloadManager;
    private String mApkPath = "";
    private String TAG = "下载操作";
    String mApkUrl;
    String mApkName;

    public ApkDownLoadService() {
        super("");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        mApkUrl = intent.getStringExtra("mApkUrl");
        mApkName = System.currentTimeMillis() + ".apk";
        if (mApkName == null || mApkUrl == null) {
            ToastUtils.showToastL(this, "下载失败,请检查网络后重试");
            return;
        }
        //创建下载任务,downloadUrl就是下载链接
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(mApkUrl));
        //指定下载路径和下载文件名
        request.setDestinationInExternalPublicDir("/download/", mApkName);
        //获取下载管理器
        mDownloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        //将下载任务加入下载队列，否则不会进行下载
        mTaskId = mDownloadManager.enqueue(request);
        //注册广播接收者，监听下载状态
        getApplicationContext().registerReceiver(receiver,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            checkDownloadStatus();//检查下载状态
            getApplicationContext().unregisterReceiver(this);
        }
    };

    //检查下载状态
    private void checkDownloadStatus() {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(mTaskId);//筛选下载任务，传入任务ID，可变参数
        Cursor c = mDownloadManager.query(query);
        if (c.moveToFirst()) {
            int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
            switch (status) {
                case DownloadManager.STATUS_PAUSED:
                    Log.i(TAG, ">>>下载暂停");
                case DownloadManager.STATUS_PENDING:
                    Log.i(TAG, ">>>下载延迟");
                case DownloadManager.STATUS_RUNNING:
                    Log.i(TAG, ">>>正在下载");
                    break;
                case DownloadManager.STATUS_SUCCESSFUL:
                    Log.i(TAG, ">>>下载完成");
                    ToastUtils.showToast(this, "下载成功,请安装新版本");
                    //下载完成安装APK
                    mApkPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + mApkName;
                    installAPK(new File(mApkPath));
                    break;
                case DownloadManager.STATUS_FAILED:
                    Log.i(TAG, ">>>下载失败");
                    ToastUtils.showToast(this, "下载失败,请检查网络后重试");
                    break;
            }
        }
    }

    //下载到本地后执行安装
    protected void installAPK(File file) {
        if (!file.exists()) return;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data;
        // 判断版本大于等于7.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // "net.csdn.blog.ruancoder.fileprovider"即是在清单文件中配置的authorities
            data = FileProvider.getUriForFile(this, "com.brioal.versionlib.fileprovider", file);
        } else {
            data = Uri.fromFile(file);
        }
        intent.setDataAndType(data, "application/vnd.android.package-archive");
        // 给目标应用一个临时授权
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


}
