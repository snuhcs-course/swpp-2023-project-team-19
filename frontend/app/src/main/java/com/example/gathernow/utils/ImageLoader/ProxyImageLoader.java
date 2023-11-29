package com.example.gathernow.utils.ImageLoader;

import android.widget.ImageView;

public class ProxyImageLoader implements ImageLoader{
    private RealImageLoader realImageLoader;
    private String imageUrl;
    private int resourceId;

    public ProxyImageLoader(String imageUrl, int resourceId) {
        this.imageUrl = imageUrl;
        this.resourceId = resourceId;
    }

    @Override
    public void displayImage(ImageView event_photo) {
        if (realImageLoader == null) {
            realImageLoader = new RealImageLoader(imageUrl, resourceId);
        }
        realImageLoader.displayImage(event_photo);
    }
}
