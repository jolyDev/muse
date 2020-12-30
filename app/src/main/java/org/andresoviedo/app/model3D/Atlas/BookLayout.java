package org.andresoviedo.app.model3D.Atlas;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import org.nmmu.R;

import java.util.Arrays;

public class BookLayout extends CustomRelativeLayout {

    public static int PageCount = 0;

    Atlas atlas;

    private BookView bookView;

    private final Context mContext;

    private int totalPages;

    int screenWidth;
    int screenHeight;

    ZoomImageView book_page1_zoomImageView;
    ImageView page1_bg_ImageView, page2_bg_image;

    private Bitmap mBitmap;
    private Bitmap page1, page2;

    private GestureDetector gestures;

    private boolean bBlockTouchInput = false;

    public BookLayout(Context context, Atlas atlas) {
        super(context);
        mContext = context;
        this.atlas = atlas;
        init();
    }

    /*public BookLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        atlas = new ArrayList<>();
    }*/

    @SuppressLint("ClickableViewAccessibility")
    public void init() {
        totalPages = 2;
        screenHeight = atlas.getScreenHeight();
        screenWidth = atlas.getScreenWidth();

        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        ImageView page1_bg = new ImageView(mContext);
        page1_bg.setId(R.id.page1_bg);
        page1_bg.setScaleType(ImageView.ScaleType.FIT_XY);
        page1_bg.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        ImageView page2_bg = new ImageView(mContext);
        page2_bg.setId(R.id.page2_bg);
        page2_bg.setScaleType(ImageView.ScaleType.FIT_XY);
        page2_bg.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        linearLayout.addView(page1_bg, screenWidth,screenHeight);
        linearLayout.addView(page2_bg, screenWidth,screenHeight);

        ScrollView scrollView = new ScrollView(mContext);
        scrollView.setId(R.id.scrollView);
        scrollView.addView(linearLayout);
        scrollView.setVerticalScrollBarEnabled(false);
        scrollView.setHorizontalScrollBarEnabled(false);
        scrollView.setFadingEdgeLength(0);
        scrollView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        addView(scrollView);
        //
        this.atlas = atlas;

        // add true view and some actions
        LayoutInflater factory = LayoutInflater.from(mContext);

        View book_page1 = factory.inflate(R.layout.book_page, null);
        addView(book_page1, screenWidth, screenHeight);

        book_page1_zoomImageView = book_page1.findViewById(R.id.book_page1_image);
        page1_bg_ImageView= findViewById(R.id.page1_bg);
        page2_bg_image= findViewById(R.id.page2_bg);

        Bitmap bitmap_page = atlas.get(0);

        book_page1_zoomImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        book_page1_zoomImageView.setOnTouchListener(onTouchListener);

        book_page1_zoomImageView.setImageBitmap(bitmap_page);
        page1_bg_ImageView.setImageBitmap(bitmap_page);

        bitmap_page = atlas.get(1);
        page2_bg_image.setImageBitmap(bitmap_page);
        bitmap_page=null;

        GestureListener listener = new GestureListener();
        gestures = new GestureDetector(mContext, listener, null, true);

        bookView = new BookView(mContext, screenWidth, screenHeight);
        addView(bookView);
        bookView.setView(book_page1);
    }

    public void next() {

        if (PageCount + 1 < atlas.size()) {

            resetBitmap(true);

            PageCount = PageCount + 1;
            book_page1_zoomImageView.resetZoom();

            Bitmap bitmap_page=null;
            bitmap_page=((BitmapDrawable)page2_bg_image.getDrawable()).getBitmap();

            //((BitmapDrawable)book_page1_touchImageView.getDrawable()).getBitmap().recycle();
            book_page1_zoomImageView.setImageBitmap(bitmap_page);

            //((BitmapDrawable)page1_bg_ImageView.getDrawable()).getBitmap().recycle();
            page1_bg_ImageView.setImageBitmap(bitmap_page);

            bitmap_page=atlas.get(PageCount+1);
            page2_bg_image.setImageBitmap(bitmap_page);
            bitmap_page=null;

        }
    }

    public void pre() {

        if (PageCount - 1 >= 0) {

            PageCount = PageCount - 1;
            book_page1_zoomImageView.resetZoom();

            Bitmap bitmap_page=null;
            bitmap_page=((BitmapDrawable)page1_bg_ImageView.getDrawable()).getBitmap();

            //((BitmapDrawable)page2_bg_image.getDrawable()).getBitmap().recycle();
            page2_bg_image.setImageBitmap(bitmap_page);

            bitmap_page=atlas.get(PageCount);

            book_page1_zoomImageView.setImageBitmap(bitmap_page);
            page1_bg_ImageView.setImageBitmap(bitmap_page);
            bitmap_page=null;

            resetBitmap(false);

        }
    }


    public void getPageContent() {
        if(mBitmap==null){
            setBitmap();
        }
    }

    private void setBitmap() {
        try{
            ScrollView scrollView = this.findViewById(R.id.scrollView);
            mBitmap = Bitmap.createBitmap(screenWidth, screenHeight*totalPages, Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(mBitmap);
            scrollView.draw(canvas);

            page1 = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.RGB_565);
            Canvas canvas1 = new Canvas(page1);
            canvas1.drawBitmap(mBitmap, 0, 0, new Paint());

            page2 = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.RGB_565);
            Canvas canvas2 = new Canvas(page2);
            canvas2.drawBitmap(mBitmap, 0, -screenHeight, new Paint());

            scrollView.setVisibility(View.GONE);
            bookView.init(page1, page2);

        } catch (OutOfMemoryError ex) {

            //System.gc();
            System.out.println("****"+ Arrays.toString(ex.getStackTrace()));

        }
    }

    private void resetBitmap(boolean isNext) {
        try{
            ScrollView scrollView = this.findViewById(R.id.scrollView);
            scrollView.setVisibility(View.VISIBLE);
            if(mBitmap == null){
                mBitmap = Bitmap.createBitmap(screenWidth, screenHeight*totalPages, Bitmap.Config.RGB_565);
            }
            Canvas canvas=new Canvas(mBitmap);
            scrollView.draw(canvas);
            if (isNext){
                page1=page2;
                page2 = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.RGB_565);
            } else {
                page2=page1;
                page1 = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.RGB_565);
            }

            Canvas canvas1 = new Canvas(page1);
            canvas1.drawBitmap(mBitmap, 0, 0, new Paint());

            Canvas canvas2 = new Canvas(page2);
            canvas2.drawBitmap(mBitmap, 0, -screenHeight, new Paint());

            scrollView.setVisibility(View.GONE);
            bookView.setPage(page1, page2);

            if (isNext) {
                // No input when flipping
                bBlockTouchInput = true;
                bookView.next();
                bBlockTouchInput = false;
            } else {
                bBlockTouchInput = true;
                bookView.pre();
                bBlockTouchInput = false;
            }

        } catch (OutOfMemoryError ex) {
            //System.gc();
            System.out.println("****"+ Arrays.toString(ex.getStackTrace()));
        }

    }

    View.OnTouchListener onTouchListener = new View.OnTouchListener() {

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View view, MotionEvent event) {

            gestures.onTouchEvent(event);
            return true;
        }
    };

    class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_MIN_DISTANCE = 50;
        private static final int SWIPE_MAX_OFF_PATH = 400;
        private static final int SWIPE_THRESHOLD_VELOCITY = 50;

        public GestureListener() {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            //try {
            if (bBlockTouchInput || Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                return false;
            // right to left swipe
            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {

                if (!book_page1_zoomImageView.isZoomed()) {
                    next();
                }

            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {

                if (!book_page1_zoomImageView.isZoomed()) {
                    pre();
                }

            }
            /*} catch (Exception ex) {

                System.out.println("******Error GestureListener:" + ex.getMessage());

            }*/
            return false;
        }
    }
}