package org.andresoviedo.app.model3D.view;

import android.app.Activity;
import org.nmmu.R;
import org.andresoviedo.lang.Tokens;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class ImageActivity extends Activity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        imageView = findViewById(R.id.photo_view);

        Bundle b = getIntent().getExtras();
        if(b != null){
            try{
                String url = b.getString("url");
                Glide.with(this).load(url).into(imageView);
            } catch (Exception e){
                Toast.makeText(this, Tokens.imageNotFound, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, Tokens.noUrl, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}