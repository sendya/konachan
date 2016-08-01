package com.loacg.konachan.utils.multidown;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.concurrent.Callable;

/**
 * Project: konachan
 * Author: Sendya <18x@loacg.com>
 * Time: 8/1/2016 2:45 PM
 */
public class DownloadCallable implements Callable<String> {
    // 保存的文件路径
    private File file;
    // 开始下载位置
    private long start;
    // 结束下载位置
    private long end;
    // 下载地址
    private URL url;
    // 该文件总长度
    private long totalLength;

    public DownloadCallable(File file, long totalLength, long start, long end, URL url) {
        this.file = file;
        this.start = start;
        this.end = end;
        this.url = url;
        this.totalLength = totalLength;
    }

    public String call() throws Exception {
        HttpURLConnection conn = null;
        String result = null ;
        try {
            conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Range" , "bytes=" + start + "-" + end);

            if (conn.getResponseCode() == 206) {
                InputStream inputStream = conn.getInputStream();
                ReadableByteChannel inChannel = Channels.newChannel(inputStream);

                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rwd");
                randomAccessFile.setLength(totalLength);
                randomAccessFile.seek(start);
                FileChannel outChannel = randomAccessFile.getChannel();

                long writeCount = outChannel.transferFrom(inChannel, start, (end - start));

                MultithreadingDownload.threadPerformCount.getAndIncrement() ;

                result = "线程 " + Thread.currentThread().getName() + " 下载: " + start + "--->" + end + " 预计下载： " + (end - start) + ", 实际下载：" + writeCount ;
                inChannel.close();
                outChannel.close();
                randomAccessFile.close();
                inputStream.close();
            } else {
                result = "\n线程 " + Thread.currentThread().getName() + " 返回网络状态码（下载失败）：" +conn.getResponseCode();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
