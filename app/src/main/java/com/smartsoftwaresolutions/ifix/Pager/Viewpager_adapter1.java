package com.smartsoftwaresolutions.ifix.Pager;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Viewpager_adapter1  extends PagerAdapter {
    private Context context;
    private ArrayList<String> imageurl;

    public   Viewpager_adapter1(Context context,ArrayList<String > imageurl){
        this.context=context;
        this.imageurl=imageurl;
    }
    @Override
    public int getCount() {
        return imageurl.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView=new ImageView(context);
        //   imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        //  imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //  imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        Picasso.get()
                .load(imageurl.get(position))
                .centerCrop()
                .fit()
                .into(imageView);
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

}
