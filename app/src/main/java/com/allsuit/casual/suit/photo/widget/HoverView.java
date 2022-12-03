package com.allsuit.casual.suit.photo.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.net.Uri;
import androidx.core.internal.view.SupportMenu;
import androidx.core.view.ViewCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


import com.allsuit.casual.suit.photo.EraseActivity;
import com.allsuit.casualsuit.R;

import java.io.File;
import java.util.ArrayList;

public class HoverView extends View {

    public static final int ERASE_MODE = 0;

    public static int MAGIC_MODE = 2;
    public static int MAGIC_MODE_RESTORE = 3;
    private static final float MAX_ZOOM = 4.0f;
    private static final float MIN_ZOOM = 1.0f;

    public static int MOVING_MODE = 4;

    public static int UNERASE_MODE = 1;
    static final int ZOOM = 2;
    static Paint eraser;
    private static Path mPath;
    private static Path mPathErase;
    public static int mode = 0;
    static Canvas newCanvas;
    public static int temp_mode = 0;
    static Paint uneraser;
    PointF DownPT = new PointF();

    String TAG = "tri.dung";
    Bitmap bm;
    int bmHeight;
    int bmWidth;
    ArrayList<Boolean> checkMirrorStep;
    private Paint circlePaint;
    private Paint circlePaintFill;
    private int circleSpace = 0;
    Bitmap clippedBitmap;
    int currentIndex = -1;
    public PointF drawingPoint;
    private String filename;
    private int height;
    private boolean is_from_init = false;
    int[] lastBitmapData;
    private Paint mBitmapPaint;
    public Context mContext;
    private Paint mMaskPaint;
    private float mX;
    private float mX1 = 0.0f;
    private float mY;
    private float mY1 = 0.0f;
    Bitmap magicPointer;
    public int magicThreshold = 15;
    public int magicTouchRange = 200;
    Matrix matrix = new Matrix();
    PointF mid = new PointF();
    float oldDist = MIN_ZOOM;
    int[] saveBitmapData;
    Matrix savedMatrix = new Matrix();
    public float scale = MIN_ZOOM;
    private float scaleAll = MIN_ZOOM;
    ArrayList<int[]> stackChange;
    PointF start = new PointF();
    private int strokeWidth = 40;
    private float temp_y = 0.0f;
    private float temt_x = 0.0f;
    int touchMode = 0;
    public PointF touchPoint;
    int viewHeight;
    int viewWidth;
    private int width;

    public HoverView(Context context, Bitmap bm, int w, int h, int viewwidth, int viewheight) {
        super(context);
        this.mContext = context;
        this.viewWidth = viewwidth;
        this.viewHeight = viewheight;
        this.bmWidth = w;
        this.bmHeight = h;
        setLayerType(1, null);
        init(bm, w, h);
        this.is_from_init = true;
    }

    public void switchMode(int _mode) {
        mode = _mode;
        resetPath();
        saveLastMaskData();
        if (mode == MAGIC_MODE || mode == MAGIC_MODE_RESTORE) {
            this.magicPointer = BitmapFactory.decodeResource(getResources(), R.drawable.magic_erase_pointer);
        } else if (mode == 0 || mode == UNERASE_MODE) {
            this.magicPointer = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.color_select_icon), this.strokeWidth + 5, this.strokeWidth + 5, false);
        }
        temp_mode = mode;
        Log.e("is_from_init", this.is_from_init + "");
        if (temp_mode == 0 || temp_mode == UNERASE_MODE || temp_mode == MOVING_MODE) {
            this.is_from_init = true;
        }
        if (!this.is_from_init) {
            Log.e("TAG", "switchMode if");
            this.mX1 = this.temt_x;
            this.mY1 = this.temp_y - ((float) this.circleSpace);
        }
        invalidate();
    }

    public int getMode() {
        return mode;
    }

    public void setMagicThreshold(int value) {
        this.magicThreshold = value;
    }


    public Bitmap save() {
        return saveDrawnBitmap();
    }

    private void refreshGallery(File file) {
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        mediaScanIntent.setData(Uri.fromFile(file));
        this.mContext.sendBroadcast(mediaScanIntent);
    }

    public void setEraseOffset(int offSet) {
        this.strokeWidth = offSet;
        eraser.setStrokeWidth((float) offSet);
        uneraser.setStrokeWidth((float) offSet);
        this.magicPointer = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.color_select_icon), offSet + 5, offSet + 5, false);
        mPath.reset();
        resetPath();
        invalidate();
    }

    void init(Bitmap bitmap, int w, int h) {
        mPath = new Path();
        mPathErase = new Path();
        eraser = new Paint(1);
        eraser.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
        eraser.setAntiAlias(true);
        eraser.setFilterBitmap(true);
        eraser.setDither(true);
        eraser.setStyle(Style.STROKE);
        eraser.setStrokeJoin(Join.ROUND);
        eraser.setStrokeCap(Cap.ROUND);
        eraser.setStrokeWidth((float) this.strokeWidth);
        uneraser = new Paint();
        uneraser.setXfermode(new PorterDuffXfermode(Mode.SRC_ATOP));
        uneraser.setAntiAlias(true);
        uneraser.setStyle(Style.STROKE);
        uneraser.setStrokeJoin(Join.ROUND);
        uneraser.setStrokeCap(Cap.ROUND);
        uneraser.setStrokeWidth((float) this.strokeWidth);
        this.circlePaint = new Paint();
        this.circlePaint.setAntiAlias(true);
        this.circlePaint.setColor(0);
        this.circlePaint.setStyle(Style.FILL);
        this.circlePaint.setStrokeJoin(Join.ROUND);
        this.circlePaint.setStrokeWidth(8.0f);
        this.circlePaintFill = new Paint();
        this.circlePaintFill.setAntiAlias(true);
        this.circlePaintFill.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.circlePaintFill.setStyle(Style.STROKE);
        this.circlePaintFill.setStrokeJoin(Join.ROUND);
        this.circlePaintFill.setStrokeCap(Cap.ROUND);
        this.circlePaintFill.setStrokeWidth(2.0f);
        this.matrix.postTranslate((float) ((this.viewWidth - w) / 2), (float) ((this.viewHeight - h) / 2));
        this.mBitmapPaint = new Paint();
        this.mBitmapPaint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        this.mBitmapPaint.setAntiAlias(true);
        this.mMaskPaint = new Paint();
        this.mMaskPaint.setAntiAlias(true);
        this.bm = bitmap;
        this.bm = this.bm.copy(Config.ARGB_8888, true);
        this.clippedBitmap = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        newCanvas = new Canvas(this.clippedBitmap);
        newCanvas.save();
        newCanvas.drawARGB(255, 255, 255, 255);
        this.magicTouchRange = w > h ? h / 2 : w / 2;
        this.saveBitmapData = new int[(w * h)];
        this.bm.getPixels(this.saveBitmapData, 0, this.bm.getWidth(), 0, 0, this.bm.getWidth(), this.bm.getHeight());
        this.lastBitmapData = new int[(w * h)];
        this.magicPointer = BitmapFactory.decodeResource(getResources(), R.drawable.color_select_icon);
        this.touchPoint = new PointF((float) (w / 2), (float) (h / 2));
        this.drawingPoint = new PointF((float) (w / 2), (float) (h / 2));
        this.mX1 = (float) (w / 2);
        this.mY1 = (float) (h / 2);
        saveLastMaskData();
        this.stackChange = new ArrayList();
        this.checkMirrorStep = new ArrayList();
        addToStack(false);
        this.filename = "img_" + String.format("%d.jpg", new Object[]{Long.valueOf(System.currentTimeMillis())});
    }

    void addToStack(boolean isMirror) {
        if (this.stackChange.size() >= 10) {
            this.stackChange.remove(0);
            if (this.currentIndex > 0) {
                this.currentIndex--;
            }
        }
        if (this.stackChange != null) {
            if (this.currentIndex == 0) {
                for (int i = this.stackChange.size() - 1; i > 0; i--) {
                    this.stackChange.remove(i);
                    this.checkMirrorStep.remove(i);
                }
            }
            int[] pix = new int[(this.clippedBitmap.getWidth() * this.clippedBitmap.getHeight())];
            this.clippedBitmap.getPixels(pix, 0, this.clippedBitmap.getWidth(), 0, 0, this.clippedBitmap.getWidth(), this.clippedBitmap.getHeight());
            this.stackChange.add(pix);
            this.checkMirrorStep.add(Boolean.valueOf(isMirror));
            this.currentIndex = this.stackChange.size() - 1;
        }
    }

    public void redo() {
        Log.d(this.TAG, "Redo");
        resetPath();
        if (this.stackChange != null && this.stackChange.size() > 0 && this.currentIndex < this.stackChange.size() - 1) {
            this.currentIndex++;
            if (((Boolean) this.checkMirrorStep.get(this.currentIndex)).booleanValue()) {
                Matrix matrix = new Matrix();
                matrix.preScale(-1.0f, MIN_ZOOM);
                this.bm = Bitmap.createBitmap(this.bm, 0, 0, this.bm.getWidth(), this.bm.getHeight(), matrix, true);
                this.bm.getPixels(this.saveBitmapData, 0, this.bm.getWidth(), 0, 0, this.bm.getWidth(), this.bm.getHeight());
            }
            this.clippedBitmap.setPixels((int[]) this.stackChange.get(this.currentIndex), 0, this.bmWidth, 0, 0, this.bmWidth, this.bmHeight);
            invalidate();
        }
    }

    public void undo() {
        Log.d(this.TAG, "Undo");
        resetPath();
        if (this.stackChange != null && this.stackChange.size() > 0 && this.currentIndex > 0) {
            this.currentIndex--;
            if (((Boolean) this.checkMirrorStep.get(this.currentIndex + 1)).booleanValue()) {
                Matrix matrix = new Matrix();
                matrix.preScale(-1.0f, MIN_ZOOM);
                this.bm = Bitmap.createBitmap(this.bm, 0, 0, this.bm.getWidth(), this.bm.getHeight(), matrix, true);
                this.bm.getPixels(this.saveBitmapData, 0, this.bm.getWidth(), 0, 0, this.bm.getWidth(), this.bm.getHeight());
            }
            this.clippedBitmap.setPixels((int[]) this.stackChange.get(this.currentIndex), 0, this.bmWidth, 0, 0, this.bmWidth, this.bmHeight);
            invalidate();
        }
    }

    public boolean checkUndoEnable() {
        if (this.stackChange == null || this.stackChange.size() <= 0 || this.currentIndex <= 0) {
            return false;
        }
        return true;
    }

    public boolean checkRedoEnable() {
        if (this.stackChange == null || this.stackChange.size() <= 0 || this.currentIndex >= this.stackChange.size() - 1) {
            return false;
        }
        return true;
    }

    public Bitmap drawBitmap(Bitmap bmpDraw) {
        if (mode == 0 || mode == UNERASE_MODE) {
            if (mode == 0) {
                uneraser.setXfermode(new PorterDuffXfermode(Mode.SRC_ATOP));
            } else {
                uneraser.setXfermode(new PorterDuffXfermode(Mode.SRC));
            }
            float strokeRatio = MIN_ZOOM;
            if (this.scale > MIN_ZOOM) {
                strokeRatio = this.scale;
            }
            eraser.setStrokeWidth(((float) this.strokeWidth) / strokeRatio);
            uneraser.setStrokeWidth(((float) this.strokeWidth) / strokeRatio);
            newCanvas.drawPath(mPath, eraser);
            newCanvas.drawPath(mPathErase, uneraser);
        }
        return this.clippedBitmap;
    }

    public Bitmap saveDrawnBitmap() {
        Bitmap saveBitmap = Bitmap.createBitmap(this.bm.getWidth(), this.bm.getHeight(), Config.ARGB_8888);
        Paint paint = new Paint();
        Canvas cv = new Canvas(saveBitmap);
        cv.save();
        cv.drawBitmap(this.bm, 0.0f, 0.0f, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        cv.drawBitmap(this.clippedBitmap, 0.0f, 0.0f, paint);
        return saveBitmap;
    }

    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(this.bm, this.matrix, this.mMaskPaint);
        canvas.drawBitmap(drawBitmap(this.bm), this.matrix, this.mBitmapPaint);
        if (mode == MAGIC_MODE || mode == MAGIC_MODE_RESTORE || mode == 0 || mode == UNERASE_MODE) {
            canvas.drawBitmap(this.magicPointer, this.drawingPoint.x - ((float) (this.magicPointer.getWidth() / 2)), this.drawingPoint.y - ((float) (this.magicPointer.getHeight() / 2)), this.mMaskPaint);
        }
        if (mode == 0 || mode == UNERASE_MODE) {
            this.circlePaint.setColor(SupportMenu.CATEGORY_MASK);
            canvas.drawCircle(this.mX1, this.mY1 + ((float) this.circleSpace), 10.0f, this.circlePaint);
        }
    }



    public void setCircleSpace(int circleSpace) {
       this.circleSpace = circleSpace;

        invalidate();
    }

    public Bitmap magicEraseBitmap() {
        int mWidth = this.clippedBitmap.getWidth();
        int mHeight = this.clippedBitmap.getHeight();
        if (this.touchPoint == null) {
            return this.clippedBitmap;
        }
        int[] pix = new int[(this.clippedBitmap.getWidth() * this.clippedBitmap.getHeight())];
        this.clippedBitmap.getPixels(pix, 0, this.clippedBitmap.getWidth(), 0, 0, this.clippedBitmap.getWidth(), this.clippedBitmap.getHeight());
        int xTouch = (int) this.touchPoint.x;
        int yTouch = (int) this.touchPoint.y;
        if (xTouch > mWidth || xTouch < 0 || yTouch > mHeight || yTouch < 0) {
            return this.clippedBitmap;
        }
        int aT = (pix[(yTouch * mWidth) + xTouch] >> 24) & 255;
        int rT = (this.saveBitmapData[(yTouch * mWidth) + xTouch] >> 16) & 255;
        int gT = (this.saveBitmapData[(yTouch * mWidth) + xTouch] >> 8) & 255;
        int bT = this.saveBitmapData[(yTouch * mWidth) + xTouch] & 255;
        int right = mWidth;
        int bottom = mHeight;
        for (int y = 0; y < bottom; y++) {
            for (int x = 0; x < right; x++) {
                int index = (y * mWidth) + x;
                int aMask = (pix[index] >> 24) & 255;
                int a = (this.saveBitmapData[index] >> 24) & 255;
                int r = (this.saveBitmapData[index] >> 16) & 255;
                int g = (this.saveBitmapData[index] >> 8) & 255;
                int b = this.saveBitmapData[index] & 255;
                int lastAlphaMask = (this.lastBitmapData[index] >> 24) & 255;
                if (aMask > 0 && Math.abs(r - rT) < this.magicThreshold && Math.abs(g - gT) < this.magicThreshold && Math.abs(b - bT) < this.magicThreshold) {
                    pix[index] = 0;
                } else if (lastAlphaMask > 0 && aMask == 0 && (Math.abs(r - rT) >= this.magicThreshold || Math.abs(g - gT) >= this.magicThreshold || Math.abs(b - bT) >= this.magicThreshold)) {
                    pix[index] = (((r << 16) | (g << 8)) | b) | (a << 24);
                }
            }
        }
        this.clippedBitmap.setPixels(pix, 0, mWidth, 0, 0, mWidth, mHeight);
        return this.clippedBitmap;
    }

    public Bitmap magicRestoreBitmap() {
        int mWidth = this.clippedBitmap.getWidth();
        int mHeight = this.clippedBitmap.getHeight();
        if (this.touchPoint == null) {
            return this.clippedBitmap;
        }
        int[] pix = new int[(this.clippedBitmap.getWidth() * this.clippedBitmap.getHeight())];
        this.clippedBitmap.getPixels(pix, 0, this.clippedBitmap.getWidth(), 0, 0, this.clippedBitmap.getWidth(), this.clippedBitmap.getHeight());
        int xTouch = (int) this.touchPoint.x;
        int yTouch = (int) this.touchPoint.y;
        if (xTouch > mWidth || xTouch < 0 || yTouch > mHeight || yTouch < 0) {
            return this.clippedBitmap;
        }
        int rT = (this.saveBitmapData[(yTouch * mWidth) + xTouch] >> 16) & 255;
        int gT = (this.saveBitmapData[(yTouch * mWidth) + xTouch] >> 8) & 255;
        int bT = this.saveBitmapData[(yTouch * mWidth) + xTouch] & 255;
        int right = mWidth;
        int bottom = mHeight;
        for (int y = 0; y < bottom; y++) {
            for (int x = 0; x < right; x++) {
                int index = (y * mWidth) + x;
                int aMask = (pix[index] >> 24) & 255;
                int lastAlphaValue = (this.lastBitmapData[index] >> 24) & 255;
                int a;
                int g;
                int b;
                if (aMask == 0) {
                    a = (this.saveBitmapData[index] >> 24) & 255;
                    int r = (this.saveBitmapData[index] >> 16) & 255;
                    g = (this.saveBitmapData[index] >> 8) & 255;
                    b = this.saveBitmapData[index] & 255;
                    if (Math.abs(r - rT) < this.magicThreshold && Math.abs(g - gT) < this.magicThreshold && Math.abs(b - bT) < this.magicThreshold) {
                        pix[index] = (((r << 16) | (g << 8)) | b) | (a << 24);
                    }
                } else if (aMask > 0 && lastAlphaValue == 0) {
                    a = (this.saveBitmapData[index] >> 24) & 255;
                    g = (this.saveBitmapData[index] >> 8) & 255;
                    b = this.saveBitmapData[index] & 255;
                    if (Math.abs(((this.saveBitmapData[index] >> 16) & 255) - rT) >= this.magicThreshold || Math.abs(g - gT) >= this.magicThreshold || Math.abs(b - bT) >= this.magicThreshold) {
                        pix[index] = 0;
                    }
                }
            }
        }
        this.clippedBitmap.setPixels(pix, 0, mWidth, 0, 0, mWidth, mHeight);
        return this.clippedBitmap;
    }

    public void saveLastMaskData() {
        this.clippedBitmap.getPixels(this.lastBitmapData, 0, this.clippedBitmap.getWidth(), 0, 0, this.clippedBitmap.getWidth(), this.clippedBitmap.getHeight());
    }

    public void resetPath() {
        mPath.reset();
        mPathErase.reset();
    }

    public void invalidateView() {
        invalidate();
    }

    private void touch_start(float x, float y) {
        mPath.reset();
        mPathErase.reset();
        if (mode == 0) {
            mPath.moveTo(x, y);
        } else {
            mPathErase.moveTo(x, y);
        }
        this.mX = x;
        this.mY = y;
    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - this.mX);
        float dy = Math.abs(y - this.mY);
        if (dx >= 4.0f || dy >= 4.0f) {
            if (mode == 0) {
                mPath.quadTo(this.mX, this.mY, (this.mX + x) / 2.0f, (this.mY + y) / 2.0f);
            } else {
                mPathErase.quadTo(this.mX, this.mY, (this.mX + x) / 2.0f, (this.mY + y) / 2.0f);
            }
            this.mX = x;
            this.mY = y;
        }
    }

    private void touch_up() {
        if (mode == 0) {
            mPath.lineTo(this.mX, this.mY);
        } else {
            mPathErase.lineTo(this.mX, this.mY);
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if (mode == 0 || mode == UNERASE_MODE) {
            y -= (float) this.circleSpace;
        }
        if (!this.is_from_init) {
            this.mX1 = x;
            this.mY1 = y;
        }
        if (mode == MAGIC_MODE || mode == MAGIC_MODE_RESTORE || mode == 0 || mode == UNERASE_MODE) {
            this.mX1 = x;
            this.mY1 = y;
            this.drawingPoint.x = x;
            this.drawingPoint.y = y;
        }
        if (mode != MOVING_MODE) {
            float[] v = new float[9];
            this.matrix.getValues(v);
            float mScalingFactor = v[0];
            RectF r = new RectF();
            this.matrix.mapRect(r);
            x = (x - r.left) / mScalingFactor;
            y = (y - r.top) / mScalingFactor;
        }
        switch (event.getActionMasked()) {
            case 0:
                this.savedMatrix.set(this.matrix);
                this.start.set(event.getX(), event.getY());
                this.touchMode = 1;
                if (mode == 0 || mode == UNERASE_MODE) {
                    touch_start(x, y);
                } else if (mode == MOVING_MODE) {
                    this.DownPT.x = event.getX();
                    this.DownPT.y = event.getY();
                }
                invalidate();
                break;
            case 1:
                if (mode == 0 || mode == UNERASE_MODE) {
                    touch_up();
                    this.is_from_init = false;
                    this.temt_x = event.getX();
                    this.temp_y = event.getY();
                    Log.d(this.TAG, "add to stack");
                    addToStack(false);
                } else if (mode == MAGIC_MODE || mode == MAGIC_MODE_RESTORE) {
                    this.touchPoint.x = x;
                    this.touchPoint.y = y;
                    this.is_from_init = true;
                    saveLastMaskData();
                   ((EraseActivity) this.mContext).resetSeekBar();
                }
               ((EraseActivity) this.mContext).updateUndoButton();
             ((EraseActivity) this.mContext).updateRedoButton();
                invalidate();
                resetPath();
                break;
            case 2:
                if (this.touchMode != 1) {
                    if (this.touchMode == 2 && mode == MOVING_MODE) {
                        float newDist = spacing(event);
                        if (newDist > 10.0f) {
                            this.scale = newDist / this.oldDist;
                            if (this.scale * this.scaleAll > 4.0f) {
                                this.scale = 4.0f / this.scaleAll;
                            }
                            if (this.scale * this.scaleAll < MIN_ZOOM) {
                                this.scale = MIN_ZOOM / this.scaleAll;
                            }
                            this.scaleAll *= this.scale;
                            this.matrix.postScale(this.scale, this.scale, this.mid.x, this.mid.y);
                        }
                        invalidate();
                        break;
                    }
                }
                if (mode == 0 || mode == UNERASE_MODE) {
                    touch_move(x, y);
                } else if (mode == MOVING_MODE) {
                    PointF mv = new PointF(event.getX() - this.DownPT.x, event.getY() - this.DownPT.y);
                    this.matrix.postTranslate(mv.x, mv.y);
                    this.DownPT.x = event.getX();
                    this.DownPT.y = event.getY();
                } else if (mode == MAGIC_MODE || mode == MAGIC_MODE_RESTORE) {
                    this.touchPoint.x = x;
                    this.touchPoint.y = y;
                }
                invalidate();
                break;

            case 5:
                this.oldDist = spacing(event);
                if (this.oldDist > 5.0f) {
                    this.savedMatrix.set(this.matrix);
                    midPoint(this.mid, event);
                    this.touchMode = 2;
                    Log.d(this.TAG, "mode=ZOOM");
                    break;
                }
                break;
            case 6:
                if (this.touchMode == 2 && mode == MOVING_MODE) {
                    Log.d(this.TAG, "ACTION_POINTER_UP");
                }
                this.touchMode = 0;
                Log.d(this.TAG, "mode=NONE");
                break;
        }
        return true;
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt((double) ((x * x) + (y * y)));
    }

    private void midPoint(PointF point, MotionEvent event) {
        point.set((event.getX(0) + event.getX(1)) / 2.0f, (event.getY(0) + event.getY(1)) / 2.0f);
    }
}
