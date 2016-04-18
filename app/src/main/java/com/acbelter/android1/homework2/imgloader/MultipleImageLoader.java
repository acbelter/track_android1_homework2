package com.acbelter.android1.homework2.imgloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import com.acbelter.android1.homework2.StreamUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class MultipleImageLoader implements ImageLoader {
    private static final String TAG = MultipleImageLoader.class.getSimpleName();
    private int mImageStubResId;
    private WeakReference<Context> mContextWeakRef;
    private LruCache<String, Bitmap> mMemoryCache;
    private ConcurrentHashMap<String, LoadImageTask> mTasksMap;
    private Map<String, Set<WeakReference<ImageView>>> mImageViewsMap;
    private int mRequiredWidth;
    private int mRequiredHeight;

    public MultipleImageLoader(Context context, int imageStubResId) {
        mImageStubResId = imageStubResId;
        mContextWeakRef = new WeakReference<>(context);
        mTasksMap = new ConcurrentHashMap<>();
        mImageViewsMap = new HashMap<>();
        initMemoryCache();
    }

    private void initMemoryCache() {
        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    @Override
    public void loadImage(String url, ImageView imageView) {
        if (mImageViewsMap.containsKey(url)) {
            mImageViewsMap.get(url).add(new WeakReference<>(imageView));
        } else {
            Set<WeakReference<ImageView>> newValue = new LinkedHashSet<>();
            newValue.add(new WeakReference<>(imageView));
            mImageViewsMap.put(url, newValue);
        }

        Bitmap cachedBitmap = getBitmapFromMemCache(url);
        if (cachedBitmap != null) {
            imageView.setImageBitmap(cachedBitmap);
        } else {
            imageView.setImageResource(mImageStubResId);
            Context context = mContextWeakRef.get();
            if (context != null) {
                if (!mTasksMap.containsKey(url)) {
                    LoadImageTask loadTask = new LoadImageTask(context, url);
                    mTasksMap.put(url, loadTask);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        loadTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        loadTask.execute();
                    }
                }
            }
        }
    }

    @Override
    public void setRequiredSize(int width, int height) {
        mRequiredWidth = width;
        mRequiredHeight = height;
        Log.d(TAG, "setRequiredSize: " + mRequiredWidth + "x" + mRequiredHeight);
    }


    private class LoadImageTask extends AsyncTask<Void, Void, Bitmap> {
        private WeakReference<Context> mContextWeakRef;
        private String mUrl;

        public LoadImageTask(Context context, String url) {
            mContextWeakRef = new WeakReference<>(context);
            mUrl = url;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            HttpURLConnection conn = null;
            try {
                Context context = mContextWeakRef.get();
                if (context != null) {
                    File file = new File(context.getCacheDir(), mUrl.replace("/", ""));
                    Bitmap bitmap;
                    if (!file.exists()) {
                        Log.d(TAG, "Load from net: " + mUrl);
                        URL url = new URL(mUrl);
                        conn = (HttpURLConnection) url.openConnection();
                        if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                            return null;
                        }

                        InputStream in = conn.getInputStream();
                        OutputStream out = new FileOutputStream(file);
                        StreamUtils.copyStream(in, out);
                        out.close();

                        bitmap = decodeImageFromFile(file);
                    } else {
                        Log.d(TAG, "Load from file: " + mUrl);
                        bitmap = decodeImageFromFile(file);
                    }
                    return bitmap;
                }
            } catch (IOException e) {
                Log.e(TAG, "IOException while downloading image from network", e);
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
            return null;
        }

        protected Bitmap decodeImageFromFile(File file) {
            try {
                InputStream in = new FileInputStream(file);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(in, null, options);
                in.close();
                options.inSampleSize = calculateInSampleSize(options, mRequiredWidth, mRequiredHeight);
                options.inJustDecodeBounds = false;
                return BitmapFactory.decodeStream(new FileInputStream(file), null, options);
            } catch (IOException e) {
                Log.e(TAG, "IOException while decoding image from file", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            mTasksMap.remove(mUrl);
            if (isCancelled()) {
                bitmap = null;
            }

            Bitmap cachedBitmap = getBitmapFromMemCache(mUrl);
            if (cachedBitmap == null && bitmap != null) {
                addBitmapToMemoryCache(mUrl, bitmap);
                cachedBitmap = bitmap;
            }

            Set<WeakReference<ImageView>> imageViews = mImageViewsMap.get(mUrl);
            for (WeakReference<ImageView> imageViewWekRef : imageViews) {
                ImageView imageView = imageViewWekRef.get();
                if (imageView != null) {
                    imageView.setImageBitmap(cachedBitmap);
                }
            }
        }
    }

}
