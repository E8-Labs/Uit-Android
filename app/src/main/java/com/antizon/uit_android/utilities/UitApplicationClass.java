package com.antizon.uit_android.utilities;

import android.app.Application;

import com.antizon.uit_android.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.MemoryCategory;
import com.google.android.exoplayer2.database.DatabaseProvider;
import com.google.android.exoplayer2.database.StandaloneDatabaseProvider;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.google.android.exoplayer2.util.Util;

import net.time4j.android.ApplicationStarter;

import java.io.File;


public class UitApplicationClass extends Application {
    public static SimpleCache simpleCache;
    public static TrackSelector trackSelectorDef;
    public static CacheDataSource.Factory dataSourceFactory;


    @Override
    public void onCreate() {
        super.onCreate();

        //video cache
        LeastRecentlyUsedCacheEvictor leastRecentlyUsedCacheEvictor = new LeastRecentlyUsedCacheEvictor(6485L * 1024L * 1024L);
        DatabaseProvider databaseProvider = new StandaloneDatabaseProvider(this);
        if (simpleCache == null) {
            simpleCache = new SimpleCache(new File(this.getCacheDir(), "UIT_APP"), leastRecentlyUsedCacheEvictor, databaseProvider);
        }

        if (trackSelectorDef == null) {
            trackSelectorDef = new DefaultTrackSelector(this);
            dataSourceFactory = new CacheDataSource.Factory().setCache(simpleCache).setUpstreamDataSourceFactory(new CacheDataSource.Factory().setCache(simpleCache).setUpstreamDataSourceFactory(new DefaultHttpDataSource.Factory().setUserAgent(Util.getUserAgent(this, this.getString(R.string.app_name)))));
        }


        ApplicationStarter.initialize(this, true);
        Glide.get(this).setMemoryCategory(MemoryCategory.HIGH);
        androidx.multidex.MultiDex.install(this);

    }
}
