package me.zjc.zlib.common.io;

import android.os.Environment;

import java.io.File;

/**
 * Created by ChuanZhangjiang on 2016/8/7.
 * 路径工具，用于获取Android文件系统中的各种常用目录
 */
public final class PathUtils {
    private PathUtils() {
        throw new IllegalAccessError();
    }

    /**
     * 获取文件默认下载路径
     * @return 文件的默认下载路径
     */
    public static String getDefaultDownloadPath() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .getAbsolutePath() + "/";
    }

    /**
     * 如果不存在，则创建目录
     * 存在返回true
     * 创建成功返回true
     * 创建失败返回false
     */
    public static boolean ifDirNotExistsCreateIt(String dirPath) {
        final File downloadDir = new File(dirPath);
        if (!downloadDir.exists())
            if (!downloadDir.mkdirs())
                return false;
        return true;
    }
}
