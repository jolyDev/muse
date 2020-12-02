package org.andresoviedo.app.model3D.Atlas;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Atlas {

    private int CAPACITY_HALF = 4;
    private ArrayList<Integer> images;
    private HashMap<Integer, Bitmap> bitmaps;
    private Resources resources;
    private int screenHeight, screenWidth;
    private int lastPreLoadPage;

    private HashMap<Integer, LoadingThread> loadingThreads;

    private class LoadingThread extends Thread {
        private int index;

        LoadingThread(int index) {
            this.index = index;
        }

        public void run() {
            bitmaps.put(index, decodeSampledBitmapFromResource(resources,images.get(index)));
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
            for (int i = followingStartIndex + 1; i < images.size(); i++) {
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

    public Atlas(ArrayList<Integer> resourcesList, Resources resources, int screenWidth, int screenHeight) {
        images = resourcesList;
        bitmaps = new HashMap<>();
        loadingThreads = new HashMap<>();
        this.resources = resources;
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
        init();
    }

    public int size() {
        return images.size();
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setCapacity(int capacity) {
        CAPACITY_HALF = capacity/2;
    }

    private void init() {
        load(0, CAPACITY_HALF);
        lastPreLoadPage = 0;
    }

    public Bitmap get(int page) {
        if (page >= images.size()) return null;
        callPreviousLoadingStart(page);
        Bitmap bitmap = bitmaps.get(page); // try to get Bitmap
        if (bitmap == null) { // Bitmap not loaded
            load(page);
            return bitmaps.get(page); // try to get after loading
        }
        return bitmap;
    }

    private void load(int startPage, int endPage) {
        for (int i = startPage; i <= endPage && i < images.size(); i++) {
            load(i);
        }
    }

    private void load(int page) {
        LoadingThread loadingThread = loadingThreads.get(page);
        if (loadingThread == null) { // Bitmap didn't start loading - we start loading
            loadingThread = new LoadingThread(page);
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
        if (Math.abs(curPage - lastPreLoadPage) >= CAPACITY_HALF / 2) { // if moved to half of preloaded pages, preload again
            previousLoadingStart(curPage);
        }
    }

    private void previousLoadingStart(int curPage) { // start to load pages in advance
        lastPreLoadPage = curPage;
        int start = Math.max(curPage - (CAPACITY_HALF), 0); // previous capacity/2 pages (or first page)
        int end = Math.min(curPage + (CAPACITY_HALF), images.size() - 1); // next capacity/2 pages (or last)
        recyclePages(start, end);
        for (int i = start; i <= end; i++) { // loading
            if (bitmaps.get(i) == null && loadingThreads.get(i) == null) { // if not loaded and not loading
                LoadingThread loadingThread = new LoadingThread(i);
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