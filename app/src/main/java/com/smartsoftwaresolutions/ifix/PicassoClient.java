package com.smartsoftwaresolutions.ifix;

import android.content.Context;

import android.widget.ImageView;


import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class PicassoClient {
    public static void downloadImage(Context c, String url, ImageView img)
    {
        if(url != null && url.length()>0)
        {
            Picasso.get().load(url).placeholder(R.drawable.splash).into(img);

        }else {
            Picasso.get().load(R.drawable.splash).into(img);
        }


    } public static void downloadImage2(Context c, String url, CircleImageView img)
    {
        if(url != null && url.length()>0)
        {
            Picasso.get().load(url).placeholder(R.drawable.splash).into(img);

        }else {
            Picasso.get().load(R.drawable.splash).into(img);
        }


    }

}

