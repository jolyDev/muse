package org.andresoviedo.app.model3D.view;

import android.app.Activity;

import org.andresoviedo.lang.LanguageManager;
import org.nmmu.R;
import org.andresoviedo.lang.Tokens;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

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
                ProgressDialog dialog = new ProgressDialog(ImageActivity.this);
                dialog.setMessage(LanguageManager.GetInstance().Get(Tokens.loading));
                dialog.setCancelable(false);
                dialog.show();
                String url = b.getString("url");
                Glide.with(this)
                        .load(url)
                        .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        return false;
                    }
                }).into(imageView);
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