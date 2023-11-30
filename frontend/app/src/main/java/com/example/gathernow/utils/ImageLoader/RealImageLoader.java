package com.example.gathernow.utils.ImageLoader;

import android.widget.ImageView;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

public class RealImageLoader implements ImageLoader {
    private String imageUrl;
    private int resourceId;

    public RealImageLoader(String imageUrl, int resourceId) {
        this.imageUrl = imageUrl;
        this.resourceId = resourceId;
        // You could load the image here if it's not expensive
    }

    @Override
    public void displayImage(ImageView event_photo) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            String eventThumbnail = "http://20.2.88.70:8000" + imageUrl;
            Picasso.get().load(eventThumbnail).into(event_photo);
        } else {
            event_photo.setImageResource(resourceId);
        }
    }
}

