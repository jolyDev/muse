package org.andresoviedo.app.model3D.Atlas;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class StaticPagerAdapter extends PagerAdapter {
    private final Context context;
    private final Atlas atlas;

    public StaticPagerAdapter(Context context, Atlas atlas) {
        this.context = context;
        this.atlas = atlas;
    }

    @Override
    public int getCount() {
        return atlas.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ZoomImageView imageView = new ZoomImageView(context);
        imageView.setImageBitmap(atlas.get(position));
        container.addView(imageView);
        return imageView;
        /*ImageView avatarImageView;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.pager, container,false);

        rankTextView = itemView.findViewById(R.id.textViewRank);
        nameTextView = itemView.findViewById(R.id.textViewName);
        counterTextView = itemView.findViewById(R.id.textViewCount);

        rankTextView.setText(mRanks[position]);
        nameTextView.setText(mCatNames[position]);
        counterTextView.setText(mCounters[position]);

        avatarImageView = itemView.findViewById(R.id.imageViewAvatar);
        avatarImageView.setImageBitmap(pictures[position]);

        container.addView(itemView);

        return itemView;*/
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
