package me.zjc.zlib.network.download;

/**
 * download info class
 * include download info
 */
public final class DownloadInfo {

    private long progress;
    private final long contentLength;


    DownloadInfo(long progress, long contentLength) {
        this.progress = progress;
        this.contentLength = contentLength;
    }

    /**
     * get current download progress
     * @return current download progress
     */
    public long getProgress() {
        return progress;
    }

    /**
     * get contentLength of current source
     * @return contentLength of current source
     */
    public long getContentLength() {
        return contentLength;
    }


    DownloadInfo setProgress(Long progress) {
        this.progress = progress;
        return this;
    }

    @Override
    public String toString() {
        return "DownloadInfo{" +
                "progress=" + progress +
                ", contentLength=" + contentLength +
                '}';
    }
}
