package com.allsuit.casual.suit.photo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.allsuit.casual.suit.photo.R;
import com.allsuit.casual.suit.photo.adapter.StickerAdapter;
import com.allsuit.casual.suit.photo.adapter.StickerFooterAdapter;
import com.allsuit.casual.suit.photo.model.StickerImage;
import com.allsuit.casual.suit.photo.utility.AppUtility;
import com.allsuit.casual.suit.photo.widget.SpacesItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Dikesh on 08/10/2017.
 */

public class StickerActivity extends Activity {

    private RecyclerView recyclerStickers;
    private RecyclerView recyclerFooterSticker;

    ApplicationManager applicationManager;

    private ImageView imgBack;
    private TextView txtTitle;


    AppUtility appUtility;

    ArrayList<String> stickerFooterList;
    StickerFooterAdapter stickerFooterAdapter;

    EventBus mEventBus;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEventBus = EventBus.getDefault();
        if (!mEventBus.isRegistered(this)) {
            mEventBus.register(this);
        }
        setContentView(R.layout.sticker_activity);
        appUtility=new AppUtility(this);
        recyclerStickers = (RecyclerView)findViewById( R.id.recyclerStickers );
        recyclerStickers.addItemDecoration(new SpacesItemDecoration(2));
        recyclerFooterSticker = (RecyclerView)findViewById( R.id.recyclerFooterSticker);
        applicationManager=(ApplicationManager)getApplication();

        applicationManager.doDisplayIntrestial();
        txtTitle=(TextView)findViewById(R.id.txtTitle);
        imgBack=(ImageView) findViewById(R.id.imgBack);
        applicationManager.displayBannerAds(StickerActivity.this);
        initStickerList();

        txtTitle.setText("Select Sticker");

        imgBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                finish();
            }
        });

    }

    @Subscribe
    public void noEvent(boolean isEditMode) {

    }
    private void initStickerList() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        layoutManager.scrollToPosition(0);
        recyclerFooterSticker.setLayoutManager(layoutManager);
        recyclerFooterSticker.setHasFixedSize(true);
        setHeaderStickerList();
    }

    public void setHeaderStickerList() {
        stickerFooterList = new ArrayList<>();
        stickerFooterList.clear();
        try {

            JSONArray stickersArray =new JSONArray(appUtility.getStickers());
            for (int i = 0; i < stickersArray.length(); i++) {
                JSONObject stickers = stickersArray.getJSONObject(i);
                String stickerUrl = stickers.getString("sticker_header");
                stickerFooterList.add(stickerUrl);
            }


            stickerFooterAdapter = new StickerFooterAdapter(StickerActivity.this, stickerFooterList);
            recyclerFooterSticker.setAdapter(stickerFooterAdapter);

            if(stickersArray.length()>0)
            {
                showStickerList(0);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void showStickerList(int position) {

        try {
            ArrayList<String> stickerList = new ArrayList<>();
            StickerAdapter stickerAdapter;
            GridLayoutManager gridLayoutManager;
            int noOfCol = 4;
            gridLayoutManager = new GridLayoutManager(StickerActivity.this, noOfCol, LinearLayoutManager.VERTICAL, false);

            recyclerStickers.setLayoutManager(gridLayoutManager);

            recyclerFooterSticker.setHasFixedSize(true);

            JSONArray stickersArray = new JSONArray(appUtility.getStickers());
            JSONObject stickersObject = stickersArray.getJSONObject(position);
            JSONArray jsonDataArray = stickersObject.getJSONArray("category_data");
            for (int i = 0; i < jsonDataArray.length(); i++) {
                String url = jsonDataArray.getJSONObject(i).getString("stickers");
                stickerList.add(url);
            }
            stickerAdapter = new StickerAdapter(StickerActivity.this, stickerList);
            recyclerStickers.setAdapter(stickerAdapter);
        } catch (Exception e) {

        }
    }

    public void doAddSticker(Bitmap bitmap) {

        StickerImage stickerImage=new StickerImage();
        stickerImage.setBitmap(bitmap);
        mEventBus.postSticky(stickerImage);
      //  applicationManager.setStickerBitmap(bitmap);
       // setResult(RESULT_OK);
        finish();
    }


    @Override
    public void onStart() {
        super.onStart();
        if (!mEventBus.isRegistered(this)) {
            mEventBus.register(this);
        }
    }

    @Override
    public void onStop() {
        mEventBus.unregister(this);
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mEventBus.isRegistered(this)) {
            mEventBus.register(this);
        }
    }

    @Override
    protected void onDestroy() {
        if (mEventBus.isRegistered(this)) {
            mEventBus.unregister(this);
        }
        super.onDestroy();
    }
}
