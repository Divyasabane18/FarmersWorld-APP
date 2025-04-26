package com.example.farmersworld;

import android.app.Application;
import com.cloudinary.android.MediaManager;
import java.util.HashMap;
import java.util.Map;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Configure Cloudinary settings
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dqdl7u2z5");       // Replace with your Cloud Name
        config.put("upload_preset", "Farmers world");     // Replace with your Upload Preset (exact name)

        // Initialize Cloudinary MediaManager
        MediaManager.init(this, config);
    }
}