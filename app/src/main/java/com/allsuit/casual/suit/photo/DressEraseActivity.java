package com.allsuit.casual.suit.photo;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.allsuit.casual.suit.photo.utility.Constant;
import com.allsuit.casual.suit.photo.utility.DisplayMetricsHandler;
import com.allsuit.casual.suit.photo.utility.SharedPrefs;
import com.allsuit.casual.suit.photo.widget.HoverView;
import com.allsuit.casual.suit.photo.widget.HoverViewDress;
import com.allsuit.casualsuit.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener;
import com.google.android.material.tabs.TabLayout.Tab;
import com.warkiz.widget.IndicatorSeekBar;

/**
 * Created by CDG on 7/22/2017.
 */

public class DressEraseActivity extends AppCompatActivity {
    private RelativeLayout header;
    private TextView txtTitleText;
    private LinearLayout lnvEraseHeader;
    private TextView txtOffsetCount;
    private SeekBar sbOffset;
    private ImageView imgBack;
    private ImageView imgDone;
    private RelativeLayout mainLayout;
    private LinearLayout lnvBottomLayout;


    private LinearLayout lnvAutoErase;
    private TextView txtAutoEraseCount;
    private SeekBar sbAutoErasePortion;
    private LinearLayout lnvErase;
    private TextView txtEraseSizeCount;
    private SeekBar sbEraseSize;
    private ImageView txtUndo;
    private ImageView txtRedo;
    private TabLayout simpleTabLayout;

    private  TextView txtEraseSizeMessage;



    HoverViewDress mHoverView;

    int final_width = 0;
    int image_height = 0;
    double mDensity;
    int actionBarHeight;
    int bottombarHeight;
    int viewHeight;
    int viewWidth;
    Bitmap suitBitmap;
    boolean is_light_bg = true;

    ApplicationManager applicationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_erase);

        applicationManager=(ApplicationManager)getApplication();
        applicationManager.doDisplayIntrestial();
        if(applicationManager.getSuitBitmap()!=null)
        {
            suitBitmap=applicationManager.getSuitBitmap();
            Log.e("Tag","H "+ suitBitmap.getHeight()+" W"+suitBitmap.getWidth()+" "+suitBitmap.getDensity());
        }
        initUI();


        initImageErase();
        initClickEvents();


    }




    public void initUI() {
        header = (RelativeLayout)findViewById( R.id.header );
        txtTitleText = (TextView)findViewById( R.id.txtTitleText );
        lnvEraseHeader = (LinearLayout)findViewById( R.id.lnvEraseHeader );
        txtOffsetCount = (TextView)findViewById( R.id.txtOffsetCount );
        sbOffset = findViewById( R.id.sb_Offset );
        imgBack = (ImageView)findViewById( R.id.imgBack );
        imgDone = (ImageView)findViewById( R.id.imgDone );
        mainLayout = (RelativeLayout)findViewById( R.id.mainLayout );
        lnvBottomLayout = (LinearLayout)findViewById( R.id.lnvBottomLayout );


        lnvAutoErase = (LinearLayout)findViewById( R.id.lnvAutoErase );
        txtAutoEraseCount = (TextView)findViewById( R.id.txtAutoEraseCount );
        sbAutoErasePortion = findViewById( R.id.sb_AutoErasePortion );
        lnvErase = (LinearLayout)findViewById( R.id.lnvErase );
        txtEraseSizeCount = (TextView)findViewById( R.id.txtEraseSizeCount );
        sbEraseSize =findViewById( R.id.sbEraseSize );
        txtUndo = (ImageView)findViewById( R.id.imgundo );
        txtRedo = (ImageView)findViewById( R.id.imgredo );
        simpleTabLayout = (TabLayout)findViewById( R.id.simpleTabLayout );
        txtEraseSizeMessage=(TextView)findViewById(R.id.txtEraseSizeMessage);


    }


    @TargetApi(Build.VERSION_CODES.M)
    private void initTabLayout() {
        try {
            final Tab MagicTab = this.simpleTabLayout.newTab();
            MagicTab.setText("Magic");
            MagicTab.setIcon((int) R.drawable.auto_erase_icon);
            this.simpleTabLayout.addTab(MagicTab);
         //   MagicTab.select();

            final Tab EraseTab = this.simpleTabLayout.newTab();
            EraseTab.setText("Erase");
            EraseTab.setIcon((int) R.drawable.erase_tab_icon);

            this.simpleTabLayout.addTab(EraseTab);
           EraseTab.select();

            final Tab HistoryTab = this.simpleTabLayout.newTab();
            HistoryTab.setText("Repair");
            HistoryTab.setIcon((int) R.drawable.repair_icon);
            this.simpleTabLayout.addTab(HistoryTab);

            final Tab ZoomTab = this.simpleTabLayout.newTab();
            ZoomTab.setText("Zoom");
            ZoomTab.setIcon((int) R.drawable.zoom_icon);
            this.simpleTabLayout.addTab(ZoomTab);
            mHoverView.switchMode(HoverView.ERASE_MODE);

            simpleTabLayout.addOnTabSelectedListener(new OnTabSelectedListener() {
                public void onTabSelected(Tab tab) {
                    switch (tab.getPosition()) {
                        case 0:
                            mHoverView.switchMode(HoverView.MAGIC_MODE);
                            resetSeekBar();
                            txtTitleText.setVisibility(View.VISIBLE);
                            txtTitleText.setText("Select color you want erase");
                            lnvEraseHeader.setVisibility(View.GONE);
                            lnvAutoErase.setVisibility(View.VISIBLE);
                            lnvErase.setVisibility(View.GONE);



                            return;
                        case 1:
                            mHoverView.switchMode(HoverView.ERASE_MODE);
                            txtTitleText.setVisibility(View.GONE);
                            lnvEraseHeader.setVisibility(View.VISIBLE);
                            lnvAutoErase.setVisibility(View.GONE);
                            lnvErase.setVisibility(View.VISIBLE);
                            txtEraseSizeMessage.setText("Eraser Brush Size");





                            return;
                        case 2:
                            mHoverView.switchMode(HoverView.UNERASE_MODE);
                            txtTitleText.setVisibility(View.GONE);
                            lnvEraseHeader.setVisibility(View.VISIBLE);
                            lnvAutoErase.setVisibility(View.GONE);
                            lnvErase.setVisibility(View.VISIBLE);
                            txtEraseSizeMessage.setText("Repair Brush Size");



                            return;
                        case 3:
                            mHoverView.switchMode(HoverView.MOVING_MODE);

                            txtTitleText.setVisibility(View.VISIBLE);
                            lnvEraseHeader.setVisibility(View.GONE);
                            lnvAutoErase.setVisibility(View.GONE);
                            lnvErase.setVisibility(View.VISIBLE);
                           txtTitleText.setVisibility(View.VISIBLE);
                            txtTitleText.setText("Zoom & Move");



                            return;
                        default:
                            return;
                    }
                }

                public void onTabUnselected(Tab tab) {
                }

                public void onTabReselected(Tab tab) {
                }
            });

            sbOffset.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (mHoverView.getMode() == 0 || HoverView.UNERASE_MODE == mHoverView.getMode()) {
                        mHoverView.setCircleSpace((int) progress);
                        SharedPrefs.save(DressEraseActivity.this, SharedPrefs.ERASER_SIZE, progress);
                        Log.e("OFFSET",progress+"");
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });


            sbEraseSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    txtEraseSizeCount.setText(String.format("%02d", new Object[]{Integer.valueOf((int) (progress / 2))}));
                    mHoverView.setEraseOffset( progress);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            sbAutoErasePortion.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    txtAutoEraseCount.setText(String.format("%02d", new Object[]{Integer.valueOf((int) progress)}));
                    mHoverView.setMagicThreshold( progress);
                    int mode = mHoverView.getMode();
                    HoverViewDress hoverView = mHoverView;
                    if (mode == HoverView.MAGIC_MODE) {
                        mHoverView.magicEraseBitmap();
                    } else {
                        mode = mHoverView.getMode();
                        hoverView = mHoverView;
                        if (mode == HoverView.MAGIC_MODE_RESTORE) {
                            mHoverView.magicRestoreBitmap();
                        }
                    }
                    mHoverView.invalidateView();
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });



            resetCircleSize();
            sbEraseSize.setProgress(50);
            this.mHoverView.setEraseOffset(this.sbEraseSize.getProgress());
            this.txtEraseSizeCount.setText("" + (this.sbEraseSize.getProgress() / 2));
            if (SharedPrefs.contain(this, SharedPrefs.ERASER_OFFSET)) {
                this.sbOffset.setProgress(SharedPrefs.getInt(this, SharedPrefs.ERASER_OFFSET));
            }
            Log.e("TAG", "sb_offset:==>" + (this.sbOffset.getProgress() / 2));
            this.mHoverView.setCircleSpace(this.sbOffset.getProgress());
            this.txtOffsetCount.setText("" + (this.sbOffset.getProgress() / 2));
           /* changeSeekbarColor(this.sbOffset, getResources().getColor(R.color.seekColor), -1, -1);
            changeSeekbarColor(this.sbEraseSize, getResources().getColor(R.color.seekColor), -1, -1);
            changeSeekbarColor(this.sbAutoErasePortion, getResources().getColor(R.color.seekColor), -1, -1);*/
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        resetSeekBar();



    }
    private void initImageErase() {
        ViewTreeObserver viewTreeObserver = this.mainLayout.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                public void onGlobalLayout() {
                    if (Build.VERSION.SDK_INT >= 16) {
                        mainLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        image_height = mainLayout.getMeasuredHeight();
                        Log.e("TAG", "relative height view tree:==>" + image_height);
                        Log.e("TAG", "isAlive baaar");
                        setBitmapHeightAndWidth();
                    }
                }
            });
        }
    }


    public void initClickEvents() {

        imgDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    Bitmap bitmapTemp=mHoverView.save();
                    applicationManager.setSuitBitmap(bitmapTemp);

                    Intent intent = getIntent();

                    setResult(RESULT_OK, intent);

                    finish();
                }catch (Exception e)
                {
                    Toast.makeText(DressEraseActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }


            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        txtUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Log.e("TAG", "onClickUndo");
                    DressEraseActivity.this.mHoverView.undo();
                    if (DressEraseActivity.this.mHoverView.checkUndoEnable()) {
                        Log.e("TAG", "if");
                        DressEraseActivity.this.txtUndo.setEnabled(true);
                        DressEraseActivity.this.txtUndo.setAlpha(1.0f);
                    } else {
                        Log.e("TAG", "else");
                        DressEraseActivity.this.txtUndo.setEnabled(false);
                        DressEraseActivity.this.txtUndo.setAlpha(0.3f);
                    }
                    updateUndoButton();
                    updateRedoButton();
                }catch (Exception e)
                {
                    Toast.makeText(DressEraseActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }

            }
        });
        txtRedo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("TAG", "onClickRedo");
                DressEraseActivity.this.mHoverView.redo();
                updateUndoButton();
                updateRedoButton();
            }
        });


    }
    @TargetApi(16)
    public void changeSeekbarColor(SeekBar s, int colorp, int colors, int colorb) {
        Mode mMode = Mode.SRC_ATOP;
        LayerDrawable layerDrawable = (LayerDrawable) s.getProgressDrawable();
        Drawable progress = layerDrawable.findDrawableByLayerId(16908301);
        Drawable secondary = layerDrawable.findDrawableByLayerId(16908303);
        Drawable background = layerDrawable.findDrawableByLayerId(16908288);
        Drawable th = s.getThumb();
        progress.setColorFilter(colorp, mMode);
        secondary.setColorFilter(colors, mMode);
        background.setColorFilter(colorb, mMode);
        th.setColorFilter(colors, mMode);
        layerDrawable.setDrawableByLayerId(16908301, progress);
        layerDrawable.setDrawableByLayerId(16908303, secondary);
        layerDrawable.setDrawableByLayerId(16908288, background);
    }

    public void resetCircleSize() {

            if (SharedPrefs.contain(this, SharedPrefs.ERASER_SIZE) && this.mHoverView.getMode() == 0) {
                this.sbEraseSize.setProgress(SharedPrefs.getInt(this, SharedPrefs.ERASER_SIZE));

            } else if (SharedPrefs.contain(this, SharedPrefs.REPAIR_SIZE) && this.mHoverView.getMode() == HoverView.UNERASE_MODE) {
                this.sbEraseSize.setProgress(SharedPrefs.getInt(this, SharedPrefs.REPAIR_SIZE));
            }

    }
    private void setBitmapHeightAndWidth() {
        this.mDensity = (double) getResources().getDisplayMetrics().density;
        this.actionBarHeight = (int) (110.0d * this.mDensity);
        this.bottombarHeight = (int) (60.0d * this.mDensity);
        Log.e("TAG", "bottom bar height:==>" + this.lnvBottomLayout.getLayoutParams().height);
        this.mainLayout.post(new Runnable() {
            public void run() {
                try {

                viewWidth = getResources().getDisplayMetrics().widthPixels;
                viewHeight = image_height;
                int m = image_height;
                int n = (DisplayMetricsHandler.getScreenWidth() * suitBitmap.getHeight()) / suitBitmap.getWidth();
                if (n <= m) {
                    Constant.HEIGHT = n;
                } else {
                    Constant.HEIGHT = m;
                }
                final_width = (int) Math.ceil((double) ((((float) Constant.HEIGHT) * ((float) suitBitmap.getWidth())) / ((float) suitBitmap.getHeight())));
                    suitBitmap = Bitmap.createScaledBitmap(suitBitmap, final_width, Constant.HEIGHT, false);
                mHoverView = new HoverViewDress(DressEraseActivity.this, suitBitmap, final_width, Constant.HEIGHT, viewWidth, viewHeight);
                LayoutParams layoutParams = new LayoutParams(viewWidth, viewHeight);
                layoutParams.addRule(13);
                mHoverView.setLayoutParams(layoutParams);
                mainLayout.addView(DressEraseActivity.this.mHoverView);
                mHoverView.switchMode(HoverView.MAGIC_MODE);

                mHoverView.setCircleSpace(20);
                initTabLayout();

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }




    public void updateUndoButton() {
        if (this.mHoverView.checkUndoEnable()) {
            Log.e("TAG", "updateUndoButton if");
            this.txtUndo.setEnabled(true);
            this.txtUndo.setAlpha(1.0f);
            return;
        }
        Log.e("TAG", "updateUndoButton else");
        this.txtUndo.setEnabled(false);
        this.txtUndo.setAlpha(0.3f);
    }

    public void updateRedoButton() {
        if (this.mHoverView.checkRedoEnable()) {
            Log.e("TAG", "updateRedoButton if");
            this.txtRedo.setEnabled(true);
            this.txtRedo.setAlpha(1.0f);
            return;
        }
        Log.e("TAG", "updateRedoButton else");
        this.txtRedo.setEnabled(false);
        this.txtRedo.setAlpha(0.3f);
    }

    public void resetSeekBar() {
        this.sbAutoErasePortion.setProgress(0);
        this.mHoverView.setMagicThreshold(0);
    }
}
