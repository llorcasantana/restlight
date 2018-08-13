package restlight.widget;

import restlight.widget.ImageLoader.ImageCache;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.util.LruCache;

public class LruImageCache implements ImageCache {
	
	private final LruCache<String, Bitmap> mLruCache;

	public LruImageCache() {
		this(getDefaultLruCacheSize());
	}

	public LruImageCache(int sizeInKiloBytes) {
		mLruCache = new LruCache<String, Bitmap>(sizeInKiloBytes) {
			@SuppressLint("NewApi")
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				// The cache size will be measured in kilobytes rather than
				// number of items.
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
					return bitmap.getByteCount() / 1024;
				} else {
					return (bitmap.getRowBytes() * bitmap.getHeight()) / 1024;
				}
			}
		};
	}

	@Override
	public Bitmap getBitmap(String url) {
		return mLruCache.get(url);
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		mLruCache.put(url, bitmap);
	}

	private static int getDefaultLruCacheSize() {
		int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024L);
		int cacheSize = maxMemory / 8;
		return cacheSize;
	}
}
