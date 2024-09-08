package com.allsuit.casual.suit.photo;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;


import com.allsuit.casual.suit.photo.R;
import com.allsuit.casual.suit.photo.adapter.MostDownloadAdapter;
import com.allsuit.casual.suit.photo.adapter.TopAppAdapter;
import com.allsuit.casual.suit.photo.utility.AppUtility;
import com.allsuit.casual.suit.photo.widget.ScrollableGridview;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Dikesh on 20/04/2018.
 */

public class AdsActivity extends Activity {

    private ImageView imgBack;

    Button btnRateApp,btnExitApp,btnCancel;
    ArrayList<HashMap<String, String>> topAppList;
    TopAppAdapter topAppAdapter;
    private ScrollableGridview topDownloadGridView;


    private ScrollableGridview mostDownloadGridView;
    ArrayList<HashMap<String, String>> mostDownloadAppList;
    MostDownloadAdapter mostDownloadAppAdapter;


    String topAppResult="";
    String mostDownloadResult="";
    AppUtility appUtility;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ads_activity);
        appUtility=new AppUtility(AdsActivity.this);
        initUI();
        initClickListner();
        initData();
    }


    private void initUI() {

        topDownloadGridView = (ScrollableGridview)findViewById( R.id.topDownloadGridView );
        mostDownloadGridView = (ScrollableGridview)findViewById( R.id.mostDownloadGridView );
        imgBack=(ImageView)findViewById(R.id.imgBack);
        btnRateApp=(Button)findViewById(R.id.btnRateApp);
        btnExitApp=(Button)findViewById(R.id.btnExitApp);
        btnCancel=(Button)findViewById(R.id.btnCancel);


        topAppList = new ArrayList<HashMap<String, String>>();
        mostDownloadAppList = new ArrayList<HashMap<String, String>>();

    }


    private void initClickListner() {

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setResult(RESULT_CANCELED);
                finish();
            }
        });
        topDownloadGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                String applink = topAppList.get(position).get("android_link");

                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri
                            .parse("market://details?id=" + applink)));
                } catch (ActivityNotFoundException anfe) {
                    startActivity(new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id="
                                    + applink)));
                }
            }
        });

        mostDownloadGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                String applink = mostDownloadAppList.get(position).get("android_link");

                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri
                            .parse("market://details?id=" + applink)));
                } catch (ActivityNotFoundException anfe) {
                    startActivity(new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id="
                                    + applink)));
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        btnRateApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                }
            }
        });
        btnExitApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK);
                finish();
            }
        });

    }
    private void initData() {
        topAppResult=appUtility.getTopApp();
        mostDownloadResult=appUtility.getMostDownloadApp();
        bindAdsData();
    }

    private void bindAdsData() {

        JSONArray jsonarray = null;
        try {
            jsonarray = new JSONArray(topAppResult);

            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jdata = jsonarray.getJSONObject(i);
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("app_name", jdata.getString("app_name"));
                map.put("android_link", jdata.getString("android_link"));
                map.put("app_logo", AppUtility.appImageUrl +""+jdata.getString("app_logo"));
                topAppList.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        topAppAdapter = new TopAppAdapter(AdsActivity.this, topAppList);
        topDownloadGridView.setAdapter(topAppAdapter);



        JSONArray jsonArrayMostDownload = null;
        try {
            jsonArrayMostDownload = new JSONArray(mostDownloadResult);
            Log.e("Count",jsonArrayMostDownload.length()+"==============");
            for (int i = 0; i < jsonArrayMostDownload.length(); i++) {
                JSONObject jdata = jsonArrayMostDownload.getJSONObject(i);
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("app_name", jdata.getString("app_name"));
                map.put("android_link", jdata.getString("android_link"));
                map.put("app_logo", AppUtility.appImageUrl +""+jdata.getString("app_logo"));
                mostDownloadAppList.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mostDownloadAppAdapter = new MostDownloadAdapter(AdsActivity.this, mostDownloadAppList);
        mostDownloadGridView.setAdapter(mostDownloadAppAdapter);
    }


}
