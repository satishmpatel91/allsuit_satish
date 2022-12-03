package com.allsuit.casual.suit.photo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.allsuit.casualsuit.R;
import com.squareup.picasso.Picasso;
import com.warkiz.widget.IndicatorSeekBar;

public class SuitColorActivity extends Activity {
    private ImageView imgBack;
    private TextView txtTitle;
    private ImageView imgDone;
    private ImageView imgTemplate;
    private ImageView imgTemplateOwerlay;
    private IndicatorSeekBar barRed;
    private IndicatorSeekBar barGreen;
    private IndicatorSeekBar barBlue;

    private ProgressBar progress_circular;

    SeekBar seekBar;

    private FrameLayout frmLayout;
    String templateUrl = "";
    String templateOverlayUrl = "";
    ImageView imgTemporary;

    ApplicationManager applicationManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.suit_color_activity);

        applicationManager = (ApplicationManager) getApplication();


        findViews();
        initData();
        initClickEvents();

    }

    private void initClickEvents() {

        barRed.setOnSeekChangeListener(onColorSeekBarChangeListener);
        barGreen.setOnSeekChangeListener(onColorSeekBarChangeListener);
        barBlue.setOnSeekChangeListener(onColorSeekBarChangeListener);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setResult(RESULT_CANCELED);
                finish();
            }
        });
        imgDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bitmap bmpTemp = getViewBitmap(frmLayout);
                applicationManager.setSuitBitmap(bmpTemp);
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    private void initData() {

        if (getIntent() != null) {
            templateUrl = getIntent().getStringExtra("template");
            templateOverlayUrl = getIntent().getStringExtra("overlay");


            Picasso.with(this)
                    .load(templateUrl)
                    .into(imgTemplate, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {


                            Picasso.with(SuitColorActivity.this)
                                    .load(templateOverlayUrl)
                                    .into(imgTemplateOwerlay, new com.squareup.picasso.Callback() {
                                        @Override
                                        public void onSuccess() {

                                            progress_circular.setVisibility(View.GONE);

                                            imgTemplateOwerlay.setVisibility(View.VISIBLE);
                                            imgTemplate.setVisibility(View.VISIBLE);
                                        }

                                        @Override
                                        public void onError() {
                                            //do smth when there is picture loading error
                                        }
                                    });

                        }

                        @Override
                        public void onError() {
                            //do smth when there is picture loading error
                        }
                    });


            //   Picasso.with(this).load(templateUrl).placeholder(R.drawable.progress).into(imgTemplate);

            // Picasso.with(this).load(templateOverlayUrl).placeholder(R.drawable.progress).into(imgTemplateOwerlay);
        }
    }

    private void findViews() {
        imgBack = (ImageView) findViewById(R.id.imgBack);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        imgDone = (ImageView) findViewById(R.id.imgDone);
        imgTemplate = (ImageView) findViewById(R.id.imgTemplate);
        imgTemplateOwerlay = (ImageView) findViewById(R.id.imgTemplateOwerlay);
        barRed = findViewById(R.id.barRed);
        barGreen = findViewById(R.id.barGreen);
        barBlue = findViewById(R.id.barBlue);
        imgTemporary = (ImageView) findViewById(R.id.imgTemporary);
        frmLayout = (FrameLayout) findViewById(R.id.frmLayout);
        progress_circular=findViewById(R.id.progress_circular);

    }


    IndicatorSeekBar.OnSeekBarChangeListener onColorSeekBarChangeListener = new IndicatorSeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(IndicatorSeekBar seekBar, int progress, float progressFloat, boolean fromUserTouch) {
            setColorFilter();
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
    };

    public void setColorFilter() {
        Log.e("TEST", "COLOR");
        float redValue = ((float) barRed.getProgress()) / 255;
        float greenValue = ((float) barGreen.getProgress()) / 255;
        float blueValue = ((float) barBlue.getProgress()) / 255;


        float[] colorMatrix = {
                redValue, 0, 0, 0, 0,  //red
                0, greenValue, 0, 0, 0, //green
                0, 0, blueValue, 0, 0,  //blue
                0, 0, 0, 1, 0    //alpha
        };

        ColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);

        imgTemplateOwerlay.setColorFilter(colorFilter);
    }


    public Bitmap getViewBitmap(View v) {
        v.clearFocus();
        v.setPressed(false);

        boolean willNotCache = v.willNotCacheDrawing();
        v.setWillNotCacheDrawing(false);

        // Reset the drawing cache background color to fully transparent
        // for the duration of this operation
        int color = v.getDrawingCacheBackgroundColor();
        v.setDrawingCacheBackgroundColor(0);

        if (color != 0) {
            v.destroyDrawingCache();
        }
        v.buildDrawingCache();
        Bitmap cacheBitmap = v.getDrawingCache();
        if (cacheBitmap == null) {
            Log.e("SUIT COLOR", "failed getViewBitmap(" + v + ")", new RuntimeException());
            return null;
        }

        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);

        // Restore the view
        v.destroyDrawingCache();
        v.setWillNotCacheDrawing(willNotCache);
        v.setDrawingCacheBackgroundColor(color);

        return bitmap;
    }
}




