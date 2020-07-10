package com.smartsoftwaresolutions.ifix.Pager;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Picasso;

public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private String[] imageurl;

  public   ViewPagerAdapter(Context context,String[] imageurl){
        this.context=context;
        this.imageurl=imageurl;
    }
    @Override
    public int getCount() {
        return imageurl.length;
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
    imageView.setScaleType(ImageView.ScaleType.MATRIX);
//        imageView.getLayoutParams().height = 100; // OR
//        imageView.getLayoutParams().width = 100;
        Picasso.get()
                .load(imageurl[position])
            //    .centerInside()
               // .centerCrop()
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
