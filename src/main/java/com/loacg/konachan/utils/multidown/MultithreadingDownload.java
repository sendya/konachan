package com.loacg.konachan.utils.multidown;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Project: konachan
 * Author: Sendya <18x@loacg.com>
 * Time: 8/1/2016 2:44 PM
 */
public class MultithreadingDownload {

    private List<Future<String>> futureList = new ArrayList<Future<String>>();
    public static AtomicInteger threadPerformCount = new AtomicInteger(0);

    public void download(File file, long len, int threadNum, URL url) {
        long start = 0L;
        long end = 0L;
        long block = len % threadNum == 0 ? len / threadNum : len / threadNum + 1;

        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(threadNum);
        for (int i = 0; i < threadNum; i++) {
            start = i * block;
            end = start + (block - 1);
            System.out.println("start:" + start + " --> end:" + end);
            Future<String> future = fixedThreadPool.submit(new DownloadCallable(file, len, start, end, url));
            futureList.add(future);
        }

        for (Future fu : futureList) {
            try {
                System.out.println(fu.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } finally {
                fixedThreadPool.shutdown();
            }
        }
    }

}
