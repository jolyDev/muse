package org.andresoviedo.app.model3D.Atlas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class BookView extends View {

    private static final int DEFAULT_FLIP_VALUE = 20;
    private static final int FLIP_SPEED = 50;

    private long mMoveDelay = 1000 / 30;

    float xTouchValue = DEFAULT_FLIP_VALUE, yTouchValue = DEFAULT_FLIP_VALUE;

    class FlippingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            BookView.this.flip();
        }

        public void sleep(long delayMillis) {

            this.removeMessages(0);

            sendMessageDelayed(obtainMessage(0), 0);

        }
    }

    FlippingHandler flippingHandler;
    int width;
    int height;

    float oldTouchX, oldTouchY;
    boolean flipping = false;
    boolean next;

    public static class Point {
        float x;
        float y;

        public Point(float x, float y){
            this.x = x;
            this.y = y;
        }
    }

    Point A, B, C, D, E, F;

    Bitmap visiblePage;
    Bitmap invisiblePage;
    Paint flipPagePaint;

    boolean flip = false;

    Context context;

    boolean onloading = true;
    boolean onMoving = false;

    public BookView(Context context, int width, int height) {
        super(context);
        this.context = context;
        this.width = width;
        this.height = height;
    }
    public void init(Bitmap page1, Bitmap page2) {

        int paperColor = Color.rgb(223, 205, 191);
        flippingHandler = new FlippingHandler();
        flipPagePaint = new Paint();
        flipPagePaint.setColor(paperColor);

        flipPagePaint.setShadowLayer(5, -5, 5, 0x99000000);
        A = new Point(10, 0);
        B = new Point(width, height);
        C = new Point(width, 0);
        D = new Point(0, 0);
        E = new Point(0, 0);
        F = new Point(0, 0);

        xTouchValue = yTouchValue = DEFAULT_FLIP_VALUE;
        visiblePage = page1;
        invisiblePage = page2;
        onMoving = false;
        flipping = false;
        loadData();
    }
    private void loadData() {
        onloading = false;
    }

    public void setPage(Bitmap page1, Bitmap page2){
        visiblePage = page1;
        invisiblePage = page2;
    }

    private View visibleView;

    public void setView(View book_page1){
        visibleView = book_page1;
        visibleView.setVisibility(VISIBLE);
        this.setVisibility(GONE);
    }

    public void next(){
        visibleView.setVisibility(GONE);
        this.setVisibility(VISIBLE);
        flipping = true;
        next = true;
        flip();
    }
    public void pre(){

        visibleView.setVisibility(GONE);
        this.setVisibility(VISIBLE);
        flipping = true;
        next = false;
        xTouchValue = width-30;
        flip();
    }

    public void flip() {
        if (flipping) {
            if (xTouchValue > width || xTouchValue < DEFAULT_FLIP_VALUE) {
                flipping = false;
                if (!flipping) {

                    flip = false;
                    xTouchValue = DEFAULT_FLIP_VALUE;
                    yTouchValue = DEFAULT_FLIP_VALUE;
                    swap2View();

                }
                return;
            }
            if (next) {
                xTouchValue += FLIP_SPEED;
            } else {
                xTouchValue -= FLIP_SPEED;

            }
            this.invalidate();
            flippingHandler.sleep(mMoveDelay); //TODO
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int paperColor = Color.rgb(223, 205, 191);
        width = getWidth();
        height = getHeight();

        if (flipping) {
            pointGenerate(xTouchValue, width, height);
        } else {
            pointGenerate(xTouchValue, width, height);
        }
        // First Page render
        Paint paint = new Paint();
        canvas.drawColor(paperColor);
        canvas.drawBitmap(visiblePage, 0, 0, paint);

        // Second Page Render
        Path pathX = pathOfTheMask();
        canvas.clipPath(pathX);
        canvas.drawBitmap(invisiblePage, 0, 0, paint);
        canvas.save();
        canvas.restore();
        // Flip Page render

        Path pathX2 = pathOfFlippedPaper();
        canvas.drawPath(pathX2, flipPagePaint);
        pathX = null;
        pathX2 = null;
        paint = null;
    }

    private Path pathOfTheMask() {
        Path path = new Path();
        path.moveTo(A.x, A.y);
        path.lineTo(B.x, B.y);
        path.lineTo(C.x, C.y);
        path.lineTo(D.x, D.y);
        path.lineTo(A.x, A.y);

        return path;
    }

    private Path pathOfFlippedPaper() {
        Path path = new Path();
        path.moveTo(A.x, A.y);
        path.lineTo(D.x, D.y);
        path.lineTo(E.x, E.y);
        path.lineTo(F.x, F.y);
        path.lineTo(A.x, A.y);
        return path;
    }

    private void pointGenerate(float distance, int width, int height) {
        float xA = width - distance;

        float xD = 0;
        float yD = 0;
        if (xA > width / 2.0) {
            xD = width;
            yD = height - (width - xA) * height / xA;
        } else {
            xD = 2 * xA;
            yD = 0;
        }
        double a = (height - yD) / (xD + distance - width);
        double alpha = Math.atan(a);
        double _cos = Math.cos(2 * alpha), _sin = Math.sin(2 * alpha);
        // E
        float xE = (float) (xD + _cos * (width - xD));
        float yE = (float) -(_sin * (width - xD));
        // F
        float xF = (float) (width - distance + _cos * distance);
        float yF = (float) (height - _sin * distance);
        //
        if (xA > width / 2.0) {
            xE = xD;
            yE = yD;
        }
        A.x = xA;
        A.y = height;
        D.x = xD;
        D.y = yD;
        E.x = xE;
        E.y = yE;
        F.x = xF;
        F.y = yF;
    }

    private void swap2View(){
        visibleView.setVisibility(VISIBLE);
        this.setVisibility(GONE);
    }

}