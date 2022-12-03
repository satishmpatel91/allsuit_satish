package com.allsuit.casual.suit.photo;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener;
import com.google.android.material.tabs.TabLayout.Tab;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;


import com.allsuit.casualsuit.R;
import com.allsuit.casual.suit.photo.utility.Constant;
import com.allsuit.casual.suit.photo.utility.DisplayMetricsHandler;
import com.allsuit.casual.suit.photo.utility.SharedPrefs;
import com.allsuit.casual.suit.photo.widget.HoverView;
import com.warkiz.widget.IndicatorSeekBar;

/**
 * Created by CDG on 7/22/2017.
 */

public class EraseActivity extends AppCompatActivity {
    private RelativeLayout header;
    private TextView txtTitleText;
    private LinearLayout lnvEraseHeader;
    private TextView txtOffsetCount;
    private IndicatorSeekBar sbOffset;
    private ImageView imgBack;
    private ImageView imgDone;
    private RelativeLayout mainLayout;
    private LinearLayout lnvBottomLayout;


    private LinearLayout lnvAutoErase;
    private TextView txtAutoEraseCount;
    private IndicatorSeekBar sbAutoErasePortion;
    private LinearLayout lnvErase;
    private TextView txtEraseSizeCount;
    private IndicatorSeekBar sbEraseSize;
    private ImageView txtUndo;
    private ImageView txtRedo;
    private TabLayout simpleTabLayout;

    private TextView txtEraseSizeMessage;


    HoverView mHoverView;

    int final_width = 0;
    int image_height = 0;
    double mDensity;
    int actionBarHeight;
    int bottombarHeight;
    int viewHeight;
    int viewWidth;
    Bitmap bitmap;
    boolean is_light_bg = true;

    ApplicationManager applicationManager;


    int saveFinalHeight=0;
    int saveFinalWidth=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_erase);

        applicationManager = (ApplicationManager) getApplication();
        applicationManager.doDisplayIntrestial();
        if (applicationManager.getBitmap() != null) {
            bitmap = applicationManager.getBitmap();
            saveFinalHeight=bitmap.getHeight();
            saveFinalWidth=bitmap.getWidth();
            Log.e("Tag", "H " + bitmap.getHeight() + " W" + bitmap.getWidth() + " " + bitmap.getDensity());
        }
        initUI();


        initImageErase();
        initClickEvents();


    }


    public void initUI() {
        header = (RelativeLayout) findViewById(R.id.header);
        txtTitleText = (TextView) findViewById(R.id.txtTitleText);
        lnvEraseHeader = (LinearLayout) findViewById(R.id.lnvEraseHeader);
        txtOffsetCount = (TextView) findViewById(R.id.txtOffsetCount);
        sbOffset = findViewById(R.id.sb_Offset);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgDone = (ImageView) findViewById(R.id.imgDone);
        mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);
        lnvBottomLayout = (LinearLayout) findViewById(R.id.lnvBottomLayout);


        lnvAutoErase = (LinearLayout) findViewById(R.id.lnvAutoErase);
        txtAutoEraseCount = (TextView) findViewById(R.id.txtAutoEraseCount);
        sbAutoErasePortion = findViewById(R.id.sb_AutoErasePortion);
        lnvErase = (LinearLayout) findViewById(R.id.lnvErase);
        txtEraseSizeCount = (TextView) findViewById(R.id.txtEraseSizeCount);
        sbEraseSize = findViewById(R.id.sbEraseSize);
        txtUndo = (ImageView) findViewById(R.id.imgundo);
        txtRedo = (ImageView) findViewById(R.id.imgredo);
        simpleTabLayout = (TabLayout) findViewById(R.id.simpleTabLayout);
        txtEraseSizeMessage = (TextView) findViewById(R.id.txtEraseSizeMessage);


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


                         /*   MagicTab.getIcon().setTint(getColor(R.color.rang));
                            EraseTab.getIcon().setTint(getColor(R.color.gray));
                            HistoryTab.getIcon().setTint(getColor(R.color.gray));
                            ZoomTab.getIcon().setTint(getColor(R.color.gray));
*/
                            return;
                        case 1:
                            mHoverView.switchMode(HoverView.ERASE_MODE);
                            txtTitleText.setVisibility(View.GONE);
                            lnvEraseHeader.setVisibility(View.VISIBLE);
                            lnvAutoErase.setVisibility(View.GONE);
                            lnvErase.setVisibility(View.VISIBLE);
                            txtEraseSizeMessage.setText("Eraser Brush Size");


                          /*  MagicTab.getIcon().setTint(getColor(R.color.gray));
                            EraseTab.getIcon().setTint(getColor(R.color.rang));
                            HistoryTab.getIcon().setTint(getColor(R.color.gray));
                            ZoomTab.getIcon().setTint(getColor(R.color.gray));*/


                            return;
                        case 2:
                            mHoverView.switchMode(HoverView.UNERASE_MODE);
                            txtTitleText.setVisibility(View.GONE);
                            lnvEraseHeader.setVisibility(View.VISIBLE);
                            lnvAutoErase.setVisibility(View.GONE);
                            lnvErase.setVisibility(View.VISIBLE);
                            txtEraseSizeMessage.setText("Repair Brush Size");

                          /*  MagicTab.getIcon().setTint(getColor(R.color.gray));
                            EraseTab.getIcon().setTint(getColor(R.color.gray));
                            HistoryTab.getIcon().setTint(getColor(R.color.rang));
                            ZoomTab.getIcon().setTint(getColor(R.color.gray));*/

                            return;
                        case 3:
                            mHoverView.switchMode(HoverView.MOVING_MODE);

                            txtTitleText.setVisibility(View.VISIBLE);
                            lnvEraseHeader.setVisibility(View.GONE);
                            lnvAutoErase.setVisibility(View.GONE);
                            lnvErase.setVisibility(View.VISIBLE);
                            txtTitleText.setVisibility(View.VISIBLE);
                            txtTitleText.setText("Zoom & Move");


                            /*MagicTab.getIcon().setTint(getColor(R.color.gray));
                            EraseTab.getIcon().setTint(getColor(R.color.gray));
                            HistoryTab.getIcon().setTint(getColor(R.color.gray));
                            ZoomTab.getIcon().setTint(getColor(R.color.rang));*/

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

            sbOffset.setOnSeekChangeListener(new IndicatorSeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(IndicatorSeekBar seekBar, int progress, float progressFloat, boolean fromUserTouch) {
                    if (mHoverView.getMode() == 0 || HoverView.UNERASE_MODE == mHoverView.getMode()) {
                        mHoverView.setCircleSpace(progress);
                        SharedPrefs.save(EraseActivity.this, SharedPrefs.ERASER_SIZE, progress);
                        Log.e("OFFSET", progress + "");
                    }
                }

                @Override
                public void onSectionChanged(IndicatorSeekBar seekBar, int thumbPosOnTick, String tickBelowText, boolean fromUserTouch) {

                }

                @Override
                public void onStartTrackingTouch(IndicatorSeekBar seekBar, int thumbPosOnTick) {

                }

                @Override
                public void onStopTrackingTouch(IndicatorSeekBar seekBar) {

                }
            });
            sbEraseSize.setOnSeekChangeListener(new IndicatorSeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(IndicatorSeekBar seekBar, int progress, float progressFloat, boolean fromUserTouch) {
                    txtEraseSizeCount.setText(String.format("%02d", new Object[]{Integer.valueOf(progress / 2)}));
                    mHoverView.setEraseOffset(progress);
                }

                @Override
                public void onSectionChanged(IndicatorSeekBar seekBar, int thumbPosOnTick, String tickBelowText, boolean fromUserTouch) {

                }

                @Override
                public void onStartTrackingTouch(IndicatorSeekBar seekBar, int thumbPosOnTick) {

                }

                @Override
                public void onStopTrackingTouch(IndicatorSeekBar seekBar) {

                }
            });

            sbAutoErasePortion.setOnSeekChangeListener(new IndicatorSeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(IndicatorSeekBar seekBar, int progress, float progressFloat, boolean fromUserTouch) {
                    try {


                        txtAutoEraseCount.setText(String.format("%02d", new Object[]{Integer.valueOf(seekBar.getProgress())}));
                        mHoverView.setMagicThreshold(seekBar.getProgress());
                        int mode = mHoverView.getMode();
                        HoverView hoverView = mHoverView;
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
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onSectionChanged(IndicatorSeekBar seekBar, int thumbPosOnTick, String tickBelowText, boolean fromUserTouch) {

                }

                @Override
                public void onStartTrackingTouch(IndicatorSeekBar seekBar, int thumbPosOnTick) {

                }

                @Override
                public void onStopTrackingTouch(IndicatorSeekBar seekBar) {

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
          /*  changeSeekbarColor(this.sbOffset, getResources().getColor(R.color.seekColor), -1, -1);
            changeSeekbarColor(this.sbEraseSize, getResources().getColor(R.color.seekColor), -1, -1);
            changeSeekbarColor(this.sbAutoErasePortion, getResources().getColor(R.color.seekColor), -1, -1);*/
        } catch (Exception ex) {
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
                    Bitmap bitmapTemp = mHoverView.save();
                    applicationManager.setFaceBitmap(bitmapTemp);
                  // Bitmap tempBitmap=Bitmap.createScaledBitmap(bitmapTemp,saveFinalWidth,saveFinalHeight,false);
                  //  applicationManager.setBitmap(tempBitmap);
                    applicationManager.setBitmap(bitmapTemp);

                    Intent intent = getIntent();

                    setResult(RESULT_OK, intent);

                    finish();
                } catch (Exception e) {
                    Toast.makeText(EraseActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
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
                Log.e("TAG", "onClickUndo");
                try {
                    EraseActivity.this.mHoverView.undo();
                    if (EraseActivity.this.mHoverView.checkUndoEnable()) {
                        Log.e("TAG", "if");
                        EraseActivity.this.txtUndo.setEnabled(true);
                        EraseActivity.this.txtUndo.setAlpha(1.0f);
                    } else {
                        Log.e("TAG", "else");
                        EraseActivity.this.txtUndo.setEnabled(false);
                        EraseActivity.this.txtUndo.setAlpha(0.3f);
                    }
                    updateUndoButton();
                    updateRedoButton();
                } catch (Exception e) {
                    Toast.makeText(EraseActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }

            }
        });
        txtRedo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Log.e("TAG", "onClickRedo");
                    EraseActivity.this.mHoverView.redo();
                    updateUndoButton();
                    updateRedoButton();
                } catch (Exception e) {
                    Toast.makeText(EraseActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }

            }
        });


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
                    //int m = image_height;
                    int m = DisplayMetricsHandler.getScreenHeight();
                    int n = (DisplayMetricsHandler.getScreenWidth() * bitmap.getHeight()) / bitmap.getWidth();
                    if (n <= m) {
                        Constant.HEIGHT = n;
                    } else {
                        Constant.HEIGHT = m;
                    }
                    final_width = (int) Math.ceil((double) ((((float) Constant.HEIGHT) * ((float) bitmap.getWidth())) / ((float) bitmap.getHeight())));
                    bitmap = Bitmap.createScaledBitmap(bitmap, final_width, Constant.HEIGHT, false);
                    mHoverView = new HoverView(EraseActivity.this, bitmap, final_width, Constant.HEIGHT, viewWidth, viewHeight);
                    LayoutParams layoutParams = new LayoutParams(viewWidth, viewHeight);
                    layoutParams.addRule(13);
                    mHoverView.setLayoutParams(layoutParams);
                    mainLayout.addView(EraseActivity.this.mHoverView);
                    mHoverView.switchMode(HoverView.MAGIC_MODE);

                    mHoverView.setCircleSpace(20);
                    initTabLayout();

                } catch (Exception e) {
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
