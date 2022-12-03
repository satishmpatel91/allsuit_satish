package com.allsuit.casual.suit.photo.utility;

import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.Log;

public class BitmapManager {
	public static final String LOG_PROV = "LibWallBitLog";
	public static final String LOG_NAME = "WallpaperBitmaps Library: ";
	public static final double multiplier = 1.50;

	public Bitmap loadBitmap(final String filename,
			final WallpaperManager wallpaperManager) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filename, options);
		options.inSampleSize = calculateInSampleSize(options, wallpaperManager);
		options.inPreferQualityOverSpeed = true;
		options.inJustDecodeBounds = false;
		// options.inMutable = true;
		return BitmapFactory.decodeFile(filename, options);
	}

	// Calculate best sample-size to load Bitmap in to memory
	public int calculateInSampleSize(final BitmapFactory.Options options,
			final WallpaperManager wallpaperManager) {
		final int rawHeight = options.outHeight; // height of source Bitmap
		final int rawWidth = options.outWidth; // width of source Bitmap
		final int reqHeight = (int) (wallpaperManager.getDesiredMinimumHeight() * multiplier);
		final int reqWidth = (int) (wallpaperManager.getDesiredMinimumWidth() * multiplier);
		int inSampleSize = 1;
		if (rawHeight > reqHeight || rawWidth > reqWidth) {
			inSampleSize = 2;
		}
		if (rawHeight > reqHeight * 2 || rawWidth > reqWidth * 2) {
			inSampleSize = 4;
		}
		Log.i(LOG_PROV, LOG_NAME + "inSampleSize is: " + inSampleSize
				+ "\nHeight req: " + reqHeight + " - Height bitmap: "
				+ rawHeight + "\nWidth req: " + reqWidth + " - Width bitmap: "
				+ rawWidth);
		return inSampleSize;
	}

	public Bitmap scaleBitmap(Bitmap sampleBitmap, final String setWallAspect,
			final WallpaperManager wallpaperManager) {
		final double heightBm = sampleBitmap.getHeight();
		final double widthBm = sampleBitmap.getWidth();
		final double heightDh = wallpaperManager.getDesiredMinimumHeight();
		final double widthDh = wallpaperManager.getDesiredMinimumWidth();
		double factor = 1.0;
		double width = 0;
		double height = 0;

		if (setWallAspect.equals("height")) {
			factor = heightDh / heightBm * 1;
			height = heightDh;
			width = Math.round(widthBm * factor);
		} else if (setWallAspect.equals("width")) {
			factor = widthDh / widthBm * 1;
			width = widthDh;
			height = Math.round(heightBm * factor);
		} else if (setWallAspect.equals("autofit")) {
			if (heightBm >= widthBm) {
				factor = heightDh / heightBm * 1;
				height = heightDh;
				width = Math.round(widthBm * factor);
			} else {
				factor = widthDh / widthBm * 1;
				width = widthDh;
				height = Math.round(heightBm * factor);
			}
		} else if (setWallAspect.equals("autofill")) {
			if (heightBm >= widthBm) {
				factor = widthDh / widthBm * 1;
				width = widthDh;
				height = Math.round(heightBm * factor);
			} else {
				factor = heightDh / heightBm * 1;
				height = heightDh;
				width = Math.round(widthBm * factor);
			}
		}
		sampleBitmap = Bitmap.createScaledBitmap(sampleBitmap, (int) width,
				(int) height, true);
		Log.i(LOG_PROV, LOG_NAME + "Scaled Bitmap to fit width (" + (int) width
				+ "x" + (int) height + ") in scaleBitmap, with aspect "
				+ setWallAspect);
		return sampleBitmap;
	}

	public Bitmap prepareBitmap(final Bitmap sampleBitmap,
			final WallpaperManager wallpaperManager) {
		Bitmap changedBitmap = null;
		final int heightBm = sampleBitmap.getHeight();
		final int widthBm = sampleBitmap.getWidth();
		final int heightDh = wallpaperManager.getDesiredMinimumHeight();
		final int widthDh = wallpaperManager.getDesiredMinimumWidth();
		if (widthDh > widthBm || heightDh > heightBm) {
			final int xPadding = Math.max(0, widthDh - widthBm) / 2;
			final int yPadding = Math.max(0, heightDh - heightBm) / 2;
			changedBitmap = Bitmap.createBitmap(widthDh, heightDh,
					Bitmap.Config.ARGB_8888);
			final int[] pixels = new int[widthBm * heightBm];
			sampleBitmap.getPixels(pixels, 0, widthBm, 0, 0, widthBm, heightBm);
			changedBitmap.setPixels(pixels, 0, widthBm, xPadding, yPadding,
					widthBm, heightBm);
			Log.i(LOG_PROV,
					LOG_NAME
							+ ": Inflated size of Bitmap to fit device height/width in prepareBitmap");
		} else if (widthBm > widthDh || heightBm > heightDh) {
			changedBitmap = Bitmap.createBitmap(widthDh, heightDh,
					Bitmap.Config.ARGB_8888);
			int cutLeft = 0;
			int cutTop = 0;
			int cutRight = 0;
			int cutBottom = 0;
			final Rect desRect = new Rect(0, 0, widthDh, heightDh);
			Rect srcRect = new Rect();
			if (widthBm > widthDh) { // crop width (left and right)
				cutLeft = (widthBm - widthDh) / 2;
				cutRight = (widthBm - widthDh) / 2;
				srcRect = new Rect(cutLeft, 0, widthBm - cutRight, heightBm);
			} else if (heightBm > heightDh) { // crop height (top and bottom)
				cutTop = (heightBm - heightDh) / 2;
				cutBottom = (heightBm - heightDh) / 2;
				srcRect = new Rect(0, cutTop, widthBm, heightBm - cutBottom);
			}
			final Canvas canvas = new Canvas(changedBitmap);
			canvas.drawBitmap(sampleBitmap, srcRect, desRect, null);
			Log.i(LOG_PROV,
					LOG_NAME
							+ "Cropped size of Bitmap to fit device height/width in prepareBitmap");
		} else {
			changedBitmap = sampleBitmap;
			Log.i(LOG_PROV, LOG_NAME
					+ "Did NOT inflate or crop Bitmap in prepareBitmap");
		}
		return changedBitmap;
	}

	public static Bitmap resizeBitmap(Bitmap bitmap) {
		int scaleSize=1020;
		Bitmap resizedBitmap = null;
		int originalWidth = bitmap.getWidth();
		int originalHeight = bitmap.getHeight();
		int newWidth = -1;
		int newHeight = -1;
		float multFactor = -1.0F;
		if(originalHeight > originalWidth) {
			newHeight = scaleSize ;
			multFactor = (float) originalWidth/(float) originalHeight;
			newWidth = (int) (newHeight*multFactor);
		} else if(originalWidth > originalHeight) {
			newWidth = scaleSize ;
			multFactor = (float) originalHeight/ (float)originalWidth;
			newHeight = (int) (newWidth*multFactor);
		} else if(originalHeight == originalWidth) {
			newHeight = scaleSize ;
			newWidth = scaleSize ;
		}
		resizedBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, false);
		return resizedBitmap;
	}

	public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
		int width = bm.getWidth();
		int height = bm.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// CREATE A MATRIX FOR THE MANIPULATION
		Matrix matrix = new Matrix();
		// RESIZE THE BIT MAP
		matrix.postScale(scaleWidth, scaleHeight);

		// "RECREATE" THE NEW BITMAP
		Bitmap resizedBitmap = Bitmap.createBitmap(
				bm, 0, 0, width, height, matrix, false);
		bm.recycle();
		return resizedBitmap;
	}
}
