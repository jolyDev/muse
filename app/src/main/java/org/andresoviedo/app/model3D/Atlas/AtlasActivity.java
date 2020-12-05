package org.andresoviedo.app.model3D.Atlas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import org.andresoviedo.dddmodel2.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class AtlasActivity extends Activity {

    private final ArrayList<Integer> resourcesList = new ArrayList<>();

    private void addResourses() {
        resourcesList.add(R.drawable.p00);
        resourcesList.add(R.drawable.p01);
        resourcesList.add(R.drawable.p02);
        resourcesList.add(R.drawable.p03);
        resourcesList.add(R.drawable.p04);
        resourcesList.add(R.drawable.p05);
        resourcesList.add(R.drawable.p00);
        resourcesList.add(R.drawable.p01);
        resourcesList.add(R.drawable.p02);
        resourcesList.add(R.drawable.p03);
        resourcesList.add(R.drawable.p04);
        resourcesList.add(R.drawable.p05);
        resourcesList.add(R.drawable.p00);
        resourcesList.add(R.drawable.p01);
        resourcesList.add(R.drawable.p02);
        resourcesList.add(R.drawable.p03);
        resourcesList.add(R.drawable.p04);
        resourcesList.add(R.drawable.p05);
        resourcesList.add(R.drawable.p00);
        resourcesList.add(R.drawable.p01);
        resourcesList.add(R.drawable.p02);
        resourcesList.add(R.drawable.p03);
        resourcesList.add(R.drawable.p04);
        resourcesList.add(R.drawable.p05);
        resourcesList.add(R.drawable.p00);
        resourcesList.add(R.drawable.p01);
        resourcesList.add(R.drawable.p02);
        resourcesList.add(R.drawable.p03);
        resourcesList.add(R.drawable.p04);
        resourcesList.add(R.drawable.p05);
        resourcesList.add(R.drawable.p00);
        resourcesList.add(R.drawable.p01);
        resourcesList.add(R.drawable.p02);
        resourcesList.add(R.drawable.p03);
        resourcesList.add(R.drawable.p04);
        resourcesList.add(R.drawable.p05);
        resourcesList.add(R.drawable.p00);
        resourcesList.add(R.drawable.p01);
        resourcesList.add(R.drawable.p02);
        resourcesList.add(R.drawable.p03);
        resourcesList.add(R.drawable.p04);
        resourcesList.add(R.drawable.p05);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //curl animation
        setContentView(R.layout.curl_activity_main);

        addResourses();

        final LinearLayout layout = findViewById(R.id.curl_book);
        final ViewTreeObserver observer= layout.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        BookLayout bookLayout = new BookLayout(AtlasActivity.this.getApplicationContext(),
                                new Atlas(resourcesList, getResources(), layout.getWidth(), layout.getHeight()));
                        layout.addView(bookLayout);
                        bookLayout.setVisibility(View.VISIBLE);

                        layout.getViewTreeObserver().removeOnGlobalLayoutListener(
                                this);
                    }
                });
    }
}