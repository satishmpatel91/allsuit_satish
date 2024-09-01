package com.allsuit.casual.suit.photo;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.allsuit.casual.suit.photo.adapter.ColorAdapter;
import com.allsuit.casual.suit.photo.adapter.FontAdapter;
import com.allsuit.casual.suit.photo.leodroidcoder.genericadapter.OnRecyclerItemClickListener;
import com.allsuit.casual.suit.photo.model.FontStyle;
import com.allsuit.casual.suit.photo.model.StickerData;
import com.allsuit.casual.suit.photo.utility.DoubleClickListener;
import com.allsuit.casual.suit.photo.utility.TextViewOutline;
import com.allsuit.casualsuit.R;
import com.google.android.material.tabs.TabLayout;
import com.warkiz.widget.IndicatorSeekBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class TextActivity extends AppCompatActivity  {


    RecyclerView rvFont;
    FontAdapter fontAdapter;

    RecyclerView rvColor;
    ColorAdapter colorAdapter;

    RecyclerView rvShadowOuter;
    ColorAdapter shadowAdapterOuter;



    RecyclerView rvStroke;
    ColorAdapter strokeAdapter;

    ApplicationManager fontApp;
    TabLayout tabMenuLayout;

    LinearLayout  lnvTextSize;
    LinearLayout lnvStrokeSize;
    TextViewOutline tvSample;
    int lastColorPos = 0, lastOuterShadowPos = 0, lastInnerShadowPos = 0, lastStokePosition = 0;
    ConstraintLayout CLShadowSettingOuter;

    FontStyle fontStyle = null;
    IndicatorSeekBar sbRadiusOuter, sbStrokeSize, sbRadiusXPOSOuter, sbRadiusYPOSOuter, sbTextSize;

    //IndicatorSeekBar sbRadiusInner, sbRadiusXPOSInner, sbRadiusYPOSInner;

    boolean isEditMode = false;
    EventBus mEventBus;
    ImageView imgBack, imgDone;


    TabLayout tabTextMenu,  tabStrokeMenu;
    FrameLayout frmTextContainer, frmShadowColorContainer, frmShadowSettingContainer, frmStrokeContainer;

    //int SELECTED_SHADOW_TYPE = Constant.OUTER_SHADOW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEventBus = EventBus.getDefault();
        if (!mEventBus.isRegistered(this)) {
            mEventBus.register(this);
        }
        fontApp = (ApplicationManager) getApplication();
        setContentView(R.layout.activity_text);


        initView();
        initData();
        initClickEvents();
        initTextTab();

        initStrokeTab();
    }

    private void initView() {
        tabMenuLayout = findViewById(R.id.tabMenuLayout);

        tabTextMenu = findViewById(R.id.tabTextMenu);

        tabStrokeMenu = findViewById(R.id.tabStrokeMenu);

        frmTextContainer = findViewById(R.id.frmTextContainer);
        frmShadowColorContainer = findViewById(R.id.frmShadowColorContainer);
        frmShadowSettingContainer = findViewById(R.id.frmShadowSettingContainer);
        frmStrokeContainer = findViewById(R.id.frmStrokeContainer);

        tvSample = findViewById(R.id.tvSample);

        lnvTextSize = findViewById(R.id.lnvTextSize);

        lnvStrokeSize = findViewById(R.id.lnvStrokeSize);

        CLShadowSettingOuter = findViewById(R.id.CLShadowSettingOuter);
        sbRadiusOuter = findViewById(R.id.sbRadiusOuter);
        sbRadiusXPOSOuter = findViewById(R.id.sbRadiusXPOSOuter);
        sbRadiusYPOSOuter = findViewById(R.id.sbRadiusYPOSOuter);





        sbStrokeSize = findViewById(R.id.sbStrokeSize);

        sbTextSize = findViewById(R.id.sbTextSize);
        imgBack = findViewById(R.id.imgBack);
        imgDone = findViewById(R.id.imgDone);

        sbRadiusOuter.addOnProgressChangeListener(progress -> {
            fontStyle.setShadowRadiusOuter((int) progress);
            tvSample.setShadowLayer(fontStyle.getShadowRadiusOuter(), fontStyle.getShadowDXOuter(), fontStyle.getShadowDYOuter(), fontStyle.getShadowOuterColor().getValue());
            tvSample.invalidate();
        });
        sbRadiusXPOSOuter.addOnProgressChangeListener(progress -> {
            fontStyle.setShadowDXOuter((int) progress);
            tvSample.setShadowLayer(fontStyle.getShadowRadiusOuter(), fontStyle.getShadowDXOuter(), fontStyle.getShadowDYOuter(), fontStyle.getShadowOuterColor().getValue());
            tvSample.invalidate();
        });
        sbRadiusYPOSOuter.addOnProgressChangeListener(progress -> {
            fontStyle.setShadowDYOuter((int) progress);
            tvSample.setShadowLayer(fontStyle.getShadowRadiusOuter(), fontStyle.getShadowDXOuter(), fontStyle.getShadowDYOuter(), fontStyle.getShadowOuterColor().getValue());
            tvSample.invalidate();
        });
        sbStrokeSize.addOnProgressChangeListener(progress -> {
            if (fontStyle.getStrokeColor().getValue() != 0) {
                fontStyle.setStrokeSize((int)progress);
                tvSample.setOutlineColor( fontStyle.getStrokeColor().getValue());
                tvSample.setOutlineSize(fontStyle.getStrokeSize());
                tvSample.setShadowLayer(fontStyle.getShadowRadiusOuter(), fontStyle.getShadowDXOuter(), fontStyle.getShadowDYOuter(), fontStyle.getShadowOuterColor().getValue());
                tvSample.invalidate();
            } else {
                fontStyle.setStrokeSize((int)progress);
                //     tvSample.setStroke(progress, Color.BLACK);
                tvSample.setShadowLayer(fontStyle.getShadowRadiusOuter(), fontStyle.getShadowDXOuter(), fontStyle.getShadowDYOuter(), fontStyle.getShadowOuterColor().getValue());
                tvSample.invalidate();
            }
        });


        sbTextSize.addOnProgressChangeListener(progress -> {
            tvSample.setTextSize(TypedValue.COMPLEX_UNIT_SP, progress);
            fontStyle.setTextSize((int) progress);
            tvSample.setShadowLayer(fontStyle.getShadowRadiusOuter(), fontStyle.getShadowDXOuter(), fontStyle.getShadowDYOuter(), fontStyle.getShadowOuterColor().getValue());
            tvSample.invalidate();
        });

        rvFont = findViewById(R.id.rvFont);
        fontAdapter = new FontAdapter(this, fontOnRecyclerItemClickListener);
        rvFont.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvFont.setAdapter(fontAdapter);


        rvColor = findViewById(R.id.rvColor);
        colorAdapter = new ColorAdapter(this, colorOnRecyclerItemClickListener);
        rvColor.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvColor.setAdapter(colorAdapter);

        rvShadowOuter = findViewById(R.id.rvShadowOuter);
        shadowAdapterOuter = new ColorAdapter(this, outerShadowOnRecyclerItemClickListener);
        rvShadowOuter.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        rvShadowOuter.setAdapter(shadowAdapterOuter);




        rvStroke = findViewById(R.id.rvStroke);
        strokeAdapter = new ColorAdapter(this, strokeOnRecyclerItemClickListener);
        rvStroke.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        rvStroke.setAdapter(strokeAdapter);


    }

    private void initData() {

        if (fontStyle == null) {
            fontStyle = new FontStyle();
        } else if (isEditMode) {
            if (fontStyle.getShadowOuterColor() != null) {
                tvSample.setShadowLayer(fontStyle.getShadowRadiusOuter(), fontStyle.getShadowDXOuter(), fontStyle.getShadowDYOuter(), fontStyle.getShadowOuterColor().getValue());
            }
            if (fontStyle.getStrokeColor() != null) {
                tvSample.setOutlineColor(fontStyle.getStrokeColor().getValue());
                tvSample.setOutlineSize(fontStyle.getStrokeSize());
            }
            if (fontStyle.getFont() != null) {
                if (fontStyle.getFont().getFontPath() != null) {
                    Typeface tf = Typeface.createFromAsset(getAssets(), fontStyle.getFont().getFontPath());
                    tvSample.setTypeface(tf);
                }

            }
            if (fontStyle.getTextColor().getValue() != 0) {
                Log.e("TextColor", fontStyle.getTextColor().getValue() + "----");
                tvSample.setTextColor(fontStyle.getTextColor().getValue());

            } else {
                Log.e("TextColor", "black ----");
                tvSample.setTextColor(Color.BLACK);
            }

            tvSample.setTextSize(fontStyle.getTextSize());
            sbTextSize.setProgress(fontStyle.getTextSize());

         /*   tvSample.setAlpha(fontStyle.getOpacity());
            sbOpacity.setProgress(fontStyle.getOpacity() * 225f);*/
            tvSample.invalidate();

        }
        loadFontData();
        loadColorData();

    }

    private void initTextTab() {

        TabLayout.Tab fontTab = tabTextMenu.newTab();
        fontTab.setText("Font");
        tabTextMenu.addTab(fontTab);
        fontTab.select();

        TabLayout.Tab colorTab = tabTextMenu.newTab();
        colorTab.setText("Color");
        tabTextMenu.addTab(colorTab);

       /* TabLayout.Tab sizeTab = tabTextMenu.newTab();
        sizeTab.setText("Opacity");
        tabTextMenu.addTab(sizeTab);*/
        TabLayout.Tab opacityTab = tabTextMenu.newTab();
        opacityTab.setText("Size");
        tabTextMenu.addTab(opacityTab);

        tabTextMenu.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        rvFont.setVisibility(View.VISIBLE);
                        rvColor.setVisibility(View.GONE);

                        lnvTextSize.setVisibility(View.GONE);
                        return;
                    case 1:
                        rvFont.setVisibility(View.GONE);
                        rvColor.setVisibility(View.VISIBLE);

                        lnvTextSize.setVisibility(View.GONE);
                        return;
                    case 2:
                        rvFont.setVisibility(View.GONE);
                        rvColor.setVisibility(View.GONE);

                        lnvTextSize.setVisibility(View.VISIBLE);
                        return;

                    default:
                        return;
                }
            }

            public void onTabUnselected(TabLayout.Tab tab) {
            }

            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }






    private void initStrokeTab() {

        TabLayout.Tab colorTab = tabStrokeMenu.newTab();
        colorTab.setText("Color");
        tabStrokeMenu.addTab(colorTab);
        colorTab.select();

        TabLayout.Tab sizeTab = tabStrokeMenu.newTab();
        sizeTab.setText("Size");
        tabStrokeMenu.addTab(sizeTab);


        tabStrokeMenu.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        rvStroke.setVisibility(View.VISIBLE);
                        lnvStrokeSize.setVisibility(View.GONE);

                        return;
                    case 1:
                        rvStroke.setVisibility(View.GONE);
                        lnvStrokeSize.setVisibility(View.VISIBLE);
                        return;
                    default:
                        return;
                }
            }

            public void onTabUnselected(TabLayout.Tab tab) {
            }

            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void initClickEvents() {

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        tvSample.setOnClickListener(new DoubleClickListener(500) {
            @Override
            public void onDoubleClick() {
                showEnterTextDialog();
            }
        });

        imgDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (fontStyle.getText().trim().isEmpty()) {
                    Toast.makeText(TextActivity.this, "Please enter text", Toast.LENGTH_LONG).show();
                } else {

                    tvSample.setDrawingCacheEnabled(true);

                    Bitmap bitmap = Bitmap.createBitmap(tvSample.getDrawingCache());
                    tvSample.destroyDrawingCache();
                    StickerData stickerData = new StickerData();
                    stickerData.setEdit(isEditMode);
                    stickerData.setBitmap(bitmap);
                    stickerData.setFontStyle(fontStyle);
                    EventBus.getDefault().postSticky(stickerData);
                    setResult(RESULT_OK);
                    finish();
                }

            }
        });
        tabMenuLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                switch (tab.getPosition()) {
                    case 0:
                        frmTextContainer.setVisibility(View.VISIBLE);
                        frmShadowColorContainer.setVisibility(View.GONE);
                        frmShadowSettingContainer.setVisibility(View.GONE);
                        frmStrokeContainer.setVisibility(View.GONE);

                        tabTextMenu.setVisibility(View.VISIBLE);

                        tabStrokeMenu.setVisibility(View.GONE);
                        break;
                    case 1:
                        frmTextContainer.setVisibility(View.GONE);
                        frmShadowColorContainer.setVisibility(View.VISIBLE);
                        frmShadowSettingContainer.setVisibility(View.GONE);
                        frmStrokeContainer.setVisibility(View.GONE);

                        tabTextMenu.setVisibility(View.GONE);

                        tabStrokeMenu.setVisibility(View.GONE);
                        break;
                    case 2:
                        frmTextContainer.setVisibility(View.GONE);
                        frmShadowColorContainer.setVisibility(View.GONE);
                        frmShadowSettingContainer.setVisibility(View.VISIBLE);
                        frmStrokeContainer.setVisibility(View.GONE);

                        tabTextMenu.setVisibility(View.GONE);

                        tabStrokeMenu.setVisibility(View.GONE);
                        break;
                    case 3:
                        frmTextContainer.setVisibility(View.GONE);
                        frmShadowColorContainer.setVisibility(View.GONE);
                        frmShadowSettingContainer.setVisibility(View.GONE);
                        frmStrokeContainer.setVisibility(View.VISIBLE);

                        tabTextMenu.setVisibility(View.GONE);

                        tabStrokeMenu.setVisibility(View.VISIBLE);
                        break;

                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    private void loadFontData() {
        fontAdapter.addAll(fontApp.getFontList());

        //    Log.e("Count", fontApp.getFontList().size() + "---");
    }

    private void loadColorData() {
        colorAdapter.addAll(fontApp.getColorList());
        shadowAdapterOuter.addAll(fontApp.getColorList());
        strokeAdapter.addAll(fontApp.getColorList());

    }


    OnRecyclerItemClickListener outerShadowOnRecyclerItemClickListener = new OnRecyclerItemClickListener() {
        @Override
        public void onItemClick(int position) {
            if (lastOuterShadowPos != position) {
                shadowAdapterOuter.getItem(position).setSelected(true);
                shadowAdapterOuter.getItem(lastOuterShadowPos).setSelected(false);
                shadowAdapterOuter.notifyItemChanged(position);
                shadowAdapterOuter.notifyItemChanged(lastOuterShadowPos);
                lastOuterShadowPos = position;
                fontStyle.setShadowOuterColor(shadowAdapterOuter.getItem(position));
                tvSample.setShadowLayer(fontStyle.getShadowRadiusOuter(), fontStyle.getShadowDXOuter(), fontStyle.getShadowDYOuter(), shadowAdapterOuter.getItem(position).getValue());
                //tvSample.addOuterShadow(fontStyle.getShadowRadiusOuter(), fontStyle.getShadowDXOuter(), fontStyle.getShadowDYOuter(), fontStyle.getShadowOuterColor().getValue());
                tvSample.invalidate();

            }
        }
    };

    /*OnRecyclerItemClickListener innerShadowOnRecyclerItemClickListener = new OnRecyclerItemClickListener() {
        @Override
        public void onItemClick(int position) {
            if (lastInnerShadowPos != position) {
                shadowAdapterInner.getItem(position).setSelected(true);
                shadowAdapterInner.getItem(lastInnerShadowPos).setSelected(false);
                shadowAdapterInner.notifyItemChanged(position);
                shadowAdapterInner.notifyItemChanged(lastInnerShadowPos);
                lastInnerShadowPos = position;
                fontStyle.setShadowInnerColor(shadowAdapterInner.getItem(position));
                tvSample.addInnerShadow(fontStyle.getShadowRadiusInner(), fontStyle.getShadowDXInner(), fontStyle.getShadowDYInner(), fontStyle.getShadowInnerColor().getValue());
                tvSample.invalidate();

            }
        }
    };*/


    OnRecyclerItemClickListener strokeOnRecyclerItemClickListener = new OnRecyclerItemClickListener() {
        @Override
        public void onItemClick(int position) {
            if (lastStokePosition != position) {
                strokeAdapter.getItem(position).setSelected(true);
                strokeAdapter.getItem(lastStokePosition).setSelected(false);
                strokeAdapter.notifyItemChanged(position);
                strokeAdapter.notifyItemChanged(lastStokePosition);

                fontStyle.setStrokeColor(strokeAdapter.getItem(position));
                tvSample.setOutlineColor( fontStyle.getStrokeColor().getValue());
                tvSample.setOutlineSize(fontStyle.getStrokeSize());
            //    tvSample.setStroke(fontStyle.getStrokeSize(), strokeAdapter.getItem(position).getValue());
                tvSample.setShadowLayer(fontStyle.getShadowRadiusOuter(), fontStyle.getShadowDXOuter(), fontStyle.getShadowDYOuter(), fontStyle.getShadowOuterColor().getValue());
                tvSample.invalidate();

                lastStokePosition = position;
                Log.e("Recyler Stroke", "Clicked" + position + "last " + lastStokePosition + "Stroke Size " + fontStyle.getStrokeSize());


            }
        }
    };
    OnRecyclerItemClickListener fontOnRecyclerItemClickListener = new OnRecyclerItemClickListener() {
        @Override
        public void onItemClick(int position) {
            Typeface tf = Typeface.createFromAsset(getAssets(), fontAdapter.getItem(position).getFontPath());
            tvSample.setTypeface(tf);

            fontStyle.setFont(fontAdapter.getItem(position));
            tvSample.setShadowLayer(fontStyle.getShadowRadiusOuter(), fontStyle.getShadowDXOuter(), fontStyle.getShadowDYOuter(), fontStyle.getShadowOuterColor().getValue());
            tvSample.invalidate();
        }
    };
    OnRecyclerItemClickListener colorOnRecyclerItemClickListener = new OnRecyclerItemClickListener() {
        @Override
        public void onItemClick(int position) {
            if (lastColorPos != position) {
                colorAdapter.getItem(position).setSelected(true);
                colorAdapter.getItem(lastColorPos).setSelected(false);
                colorAdapter.notifyItemChanged(position);
                colorAdapter.notifyItemChanged(lastColorPos);
                lastColorPos = position;
                tvSample.setTextColor(colorAdapter.getItem(position).getValue());
                fontStyle.setTextColor(colorAdapter.getItem(position));

                tvSample.setShadowLayer(fontStyle.getShadowRadiusOuter(), fontStyle.getShadowDXOuter(), fontStyle.getShadowDYOuter(), fontStyle.getShadowOuterColor().getValue());
                tvSample.invalidate();
            }
        }
    };


    public void showEnterTextDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_enter_text);

        final EditText edText = dialog.findViewById(R.id.edText);
        Button btSave = dialog.findViewById(R.id.btSave);
        Button btCancel = dialog.findViewById(R.id.btCancel);

        if (!fontStyle.getText().isEmpty()) {
            edText.setText(fontStyle.getText());
        }

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edText.getText().toString().isEmpty()) {
                    Toast.makeText(TextActivity.this, "Enter Text", Toast.LENGTH_LONG).show();

                } else {
                    tvSample.setText(edText.getText().toString().trim());
                    fontStyle.setText(edText.getText().toString().trim());
                    tvSample.setShadowLayer(fontStyle.getShadowRadiusOuter(), fontStyle.getShadowDXOuter(), fontStyle.getShadowDYOuter(), fontStyle.getShadowOuterColor().getValue());
                    tvSample.invalidate();
                    dialog.dismiss();
                }

            }
        });

        dialog.show();

    }





    @Subscribe
    public void noEvent(boolean isEditMode) {
        Log.e("Event", "Home");
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void editTextEvent(FontStyle fontStyle) {
        this.fontStyle = fontStyle;

        isEditMode = true;
        showEnterTextDialog();
        Log.e("Event", "EDIT");
        mEventBus.removeStickyEvent(fontStyle);

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