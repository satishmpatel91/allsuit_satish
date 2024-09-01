package com.allsuit.casual.suit.photo;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.LinearLayout;

import androidx.multidex.MultiDex;

import com.allsuit.casualsuit.R;
import com.allsuit.casual.suit.photo.model.Color;
import com.allsuit.casual.suit.photo.model.Font;
import com.allsuit.casual.suit.photo.utility.Constant;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;


public class ApplicationManager extends Application implements Application.ActivityLifecycleCallbacks {

    private InterstitialAd mInterstitialAd;
    AdRequest adRequest;

    Bitmap bitmap;
    Bitmap suitBitmap;
    Bitmap faceBitmap = null;
    Bitmap bmpTextSticker;


    public static ApplicationManager instance;
    public static String TAG_ADS = "ADMOB";

    Activity activity;
    int actionCount = 3;
    AppOpenManager appOpenManager;

    public Bitmap getSuitBitmap() {
        return suitBitmap;
    }

    public void setSuitBitmap(Bitmap suitBitmap) {
        this.suitBitmap = suitBitmap;
    }


    List<Font> fontList;

    List<Color> colorList;

    Bitmap savedSticker;

    private LinearLayout lnvAdsView;
    private AdView adView;
    @Override
    public void onCreate() {
        super.onCreate();
        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/1.ttf")
                                .build()))
                .build());

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                Log.e("Ads", "Initialization Done");
                setinteresitalads();
            }
        });

        //   appOpenManager=new AppOpenManager(this);
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        instance = this;
        registerActivityLifecycleCallbacks(this);
    }

    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }

    public static ApplicationManager getInstance() {
        return instance;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setinteresitalads() {
        List<String> testDevices = new ArrayList<>();
        testDevices.add(getString(R.string.test_id));
        RequestConfiguration requestConfiguration
                = new RequestConfiguration.Builder()
                .setTestDeviceIds(testDevices)
                .build();
        MobileAds.setRequestConfiguration(requestConfiguration);
        adRequest = new AdRequest.Builder().build();
        doGetAdmob();
    }


    public void doGetAdmob() {
        if (adRequest == null) {
            adRequest = new AdRequest.Builder().build();
        }
        InterstitialAd.load(getApplicationContext(), getString(R.string.admob_interestial_ads), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(InterstitialAd interstitialAd) {
                mInterstitialAd = interstitialAd;
                Log.i(TAG_ADS, "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                Log.i(TAG_ADS, loadAdError.getMessage());
                mInterstitialAd = null;
            }
        });


    }

    public void doDisplayIntrestial() {
        try
        {
            if (Constant.isAdsShow) {
                actionCount = actionCount + 1;
                if (mInterstitialAd == null) {
                    doGetAdmob();

                }else if (mInterstitialAd!=null && actionCount > 2) {
                    actionCount = 0;
                    mInterstitialAd.show(activity);
                    doGetAdmob();

                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }



    }


    public void displayBannerAds(Activity activity) {
        lnvAdsView= activity.findViewById(R.id.lnvAdsView);
        if (Constant.isAdsShow) {
            lnvAdsView.post(new Runnable() {
                @Override
                public void run() {
                    loadBanner();
                }
            });
            lnvAdsView.setVisibility(View.VISIBLE);
           /* AdView adView = (AdView) activity.findViewById(R.id.adView);
            //AdRequest adRequest = new AdRequest.Builder().build();
            List<String> testDevices = new ArrayList<>();
            testDevices.add(getString(R.string.test_id));
            RequestConfiguration requestConfiguration
                    = new RequestConfiguration.Builder()
                    .setTestDeviceIds(testDevices)
                    .build();
            MobileAds.setRequestConfiguration(requestConfiguration);
            adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);

            adView.setVisibility(View.VISIBLE);
            lnvAdsView.setVisibility(View.VISIBLE);*/
        } else {
            lnvAdsView.setVisibility(View.GONE);
        }

    }

    private void loadBanner() {
        // Create an ad request.
        adView = new AdView(this);
        adView.setAdUnitId(getString(R.string.admob_banner_ads));
        lnvAdsView.removeAllViews();
        lnvAdsView.addView(adView);

        AdSize adSize = getAdSize();
        adView.setAdSize(adSize);

        AdRequest adRequest = new AdRequest.Builder().build();

        // Start loading the ad in the background.
        adView.loadAd(adRequest);
    }

    private AdSize getAdSize() {
        // Determine the screen width (less decorations) to use for the ad width.
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = outMetrics.density;

        float adWidthPixels = lnvAdsView.getWidth();

        // If the ad hasn't been laid out, default to the full screen width.
        if (adWidthPixels == 0) {
            adWidthPixels = outMetrics.widthPixels;
        }

        int adWidth = (int) (adWidthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }
    Uri imagesavepath;

    public Uri getImagesavepath() {
        return imagesavepath;
    }

    public void setImagesavepath(Uri imagesavepath) {
        this.imagesavepath = imagesavepath;
    }

    public Bitmap getFaceBitmap() {
        return faceBitmap;
    }

    public void setFaceBitmap(Bitmap faceBitmap) {
        this.faceBitmap = faceBitmap;
    }


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        this.activity = activity;
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        Log.e("OnResume", activity.getLocalClassName().toString() + "   ------------------");
        if (this.activity != activity) {
            this.activity = activity;
        }

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }


    public List<Font> getFontList() {
        String folderName = "fonts";
        fontList = new ArrayList<>();
        String[] list;
        try {
            list = getAssets().list(folderName);
            if (list.length > 0) {
                // This is a folder
                Log.e("Path", list.length + "");
                for (String file : list) {

                    Font font = new Font();
                    font.setFontPath(folderName + "/" + file);
                    font.setFontName("Sample Font");
                    //    Log.e("Path", folderName + "/" + file);
                    fontList.add(font);
                }
            }
        } catch (IOException e) {

        }
        return fontList;


    }

    public List<Color> getColorList() {
        colorList = new ArrayList<>();
        int[] colorArray = getResources().getIntArray(R.array.colors);
        for (int i = 0; i < colorArray.length; i++) {
            Color color = new Color();
            color.setValue(colorArray[i]);
            colorList.add(color);
        }
        return colorList;
    }


}
