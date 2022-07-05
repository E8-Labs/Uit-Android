package com.antizon.uit_android.utilities;

import android.app.Application;

import com.bumptech.glide.Glide;
import com.bumptech.glide.MemoryCategory;

import net.time4j.android.ApplicationStarter;


public class UitApplicationClass extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ApplicationStarter.initialize(this, true);

        Glide.get(this).setMemoryCategory(MemoryCategory.HIGH);

        androidx.multidex.MultiDex.install(this);

    }
}
