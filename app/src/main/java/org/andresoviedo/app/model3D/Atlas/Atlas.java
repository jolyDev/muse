package org.andresoviedo.app.model3D.Atlas;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.andresoviedo.app.model3D.DevTools.LocalStorageManager;
import org.andresoviedo.app.model3D.DevTools.NetworkManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Atlas {

    private int LAST_DOWNLOAD = 0;
    private int IMAGE_SIZE = 49;
    private int CAPACITY = 3;
    private ArrayList<Integer> images;
    private HashMap<Integer, Bitmap> bitmaps;
    private Resources resources;
    private int screenHeight, screenWidth;
    private int lastPreLoadPage;
    private DownloadManager manager = null;

    private HashMap<Integer, LoadingThread> loadingThreads;

    private class LoadingThread extends Thread {
        private int index;
        private DownloadManager manager = null;

        LoadingThread(int index, DownloadManager i_manager) {
            this.index = index;
            manager = i_manager;
        }

        public void run() {
            NetworkManager network = NetworkManager.GetInstance();
            LocalStorageManager storage = LocalStorageManager.GetInstance();

            Bitmap result = storage.GetBitmapForPage(index);

            if (index >= 0 && index < images.size())
                result = decodeSampledBitmapFromResource(resources,images.get(index));

            if (result == null)
            {
                network.DownloadPage(index, manager);
                for (int i = 0; result == null && i < 10; i++)
                {
                    try {
                        sleep(500);
                        result = storage.GetBitmapForPage(index);
                    } catch (InterruptedException e) {
                        result = storage.GetBitmapForPage(index);
                    }
                }
                if (result == null)
                    return;
            }

            LAST_DOWNLOAD = Math.max(LAST_DOWNLOAD, index);
            bitmaps.put(index, result);
            loadingThreads.remove(index);
        }
    }

    private class RecyclingThread extends Thread {
        private int previousEndIndex;
        private int followingStartIndex;

        RecyclingThread(int previousEndIndex, int followingStartIndex) {
            this.previousEndIndex = previousEndIndex;
            this.followingStartIndex = followingStartIndex;
        }

        public void run() {
            for (int i = 0; i < previousEndIndex; i++) {
                Bitmap bitmap = bitmaps.get(i);
                if (bitmap != null) { // if page loaded
                    synchronized(bitmap) {
                        if (bitmap != null) bitmap.recycle();
                        bitmaps.remove(i);
                    }
                }
            }
            for (int i = followingStartIndex + 1; i < IMAGE_SIZE; i++) {
                Bitmap bitmap = bitmaps.get(i);
                if (bitmap != null) { // if page loaded
                    synchronized(bitmap) {
                        if (bitmap != null) bitmap.recycle();
                        bitmaps.remove(i);
                    }
                }
            }
        }
    }

    public Atlas(ArrayList<Integer> resourcesList, DownloadManager i_manager, Resources resources, int screenWidth, int screenHeight) {
        images = resourcesList;
        bitmaps = new HashMap<>();
        loadingThreads = new HashMap<>();
        manager = i_manager;
        this.resources = resources;
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
        bitmaps.put(0, decodeSampledBitmapFromResource(this.resources, resourcesList.get(0)));
    }

    public int size() {
        return LAST_DOWNLOAD + 1;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public Bitmap get(int page) {
        if (page >= IMAGE_SIZE) return null;
        callPreviousLoadingStart(page);
        Bitmap bitmap = bitmaps.get(page); // try to get Bitmap
        if (bitmap == null) { // Bitmap not loaded
            load(page);
            return bitmaps.get(page); // try to get after loading
        }
        return bitmap;
    }

    private void load(int startPage, int endPage) {
        for (int i = startPage; i <= endPage && i < IMAGE_SIZE; i++) {
            load(i);
        }
    }

    private void load(int page) {
        LoadingThread loadingThread = loadingThreads.get(page);
        if (loadingThread == null) { // Bitmap didn't start loading - we start loading
            loadingThread = new LoadingThread(page, manager);
            loadingThread.start();
            loadingThreads.put(page, loadingThread);
        }
        // Bitmap loading - we wait for it to be loaded
        // wait for thread to end
        try {
            loadingThread.join();
        } catch (Exception e) {
            System.out.println("Interrupted");
        } finally {
            loadingThreads.remove(page);
        }
    }

    private void callPreviousLoadingStart(int curPage) {
        if (Math.abs(curPage - lastPreLoadPage) >= CAPACITY / 2) { // if moved to half of preloaded pages, preload again
            previousLoadingStart(curPage);
        }
    }

    private void previousLoadingStart(int curPage) { // start to load pages in advance
        lastPreLoadPage = curPage;
        int start = Math.max(curPage - 1, 0); // previous capacity/2 pages (or first page)
        int end = Math.min(curPage + (CAPACITY), IMAGE_SIZE - 1); // next capacity/2 pages (or last)
        recyclePages(start, end);
        for (int i = start; i <= end; i++) { // loading
            if (bitmaps.get(i) == null && loadingThreads.get(i) == null) { // if not loaded and not loading
                LoadingThread loadingThread = new LoadingThread(i, manager);
                loadingThread.start();
                loadingThreads.put(i, loadingThread);
            }
        }
    }

    private void recyclePages(int previousEndIndex, int followingStartIndex) {
        RecyclingThread recyclingThread = new RecyclingThread(previousEndIndex, followingStartIndex);
        recyclingThread.start();
    }

    private int calculateInSampleSize(BitmapFactory.Options options) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > screenHeight || width > screenWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > screenHeight
                    && (halfWidth / inSampleSize) > screenWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public Bitmap decodeSampledBitmapFromResource(Resources res, int resId) {
        try{

            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(res, resId, options);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inDither = true;
            return BitmapFactory.decodeResource(res, resId, options);

        } catch (OutOfMemoryError ex){

            System.gc();
            System.out.println("****"+ Arrays.toString(ex.getStackTrace()));
            return null;

        }
    }
}