package me.zjc.zlibsample.network.download;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import me.zjc.zlib.common.utils.TextUtils;

/**
 * Created by ChuanZhangjiang on 2016/11/21.
 * 下载前的配置信息
 */

public final class DownloadConfig {
    private final String mUrl;
    private final String mFilename;
    private final String mPath;

    private DownloadConfig(String url, String filename, String path) {
        mUrl = url;
        mFilename = filename;
        mPath = path;
    }

    public static DownloadConfig getInstance(@NonNull String url,
                                             @Nullable String filename,
                                           @Nullable String path) {
        return new DownloadConfig(url, filename, path);
    }

    public String getUrl() {
        return mUrl;
    }

    public String getFilename() {
        return mFilename;
    }

    public String getPath() {
        return mPath;
    }

    public boolean isNoUrlInput() {
        if (TextUtils.isEmpty(mUrl))
            return true;
        return false;
    }

    @Override
    public String toString() {
        return "DownloadConfig{" +
                "mUrl='" + mUrl + '\'' +
                ", mFilename='" + mFilename + '\'' +
                ", mPath='" + mPath + '\'' +
                '}';
    }
}
