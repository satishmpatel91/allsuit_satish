package com.allsuit.casual.suit.photo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.allsuit.casualsuit.R;
import com.allsuit.casual.suit.photo.adapter.TemplateAdapter;
import com.allsuit.casual.suit.photo.utility.AppUtility;
import com.allsuit.casual.suit.photo.utility.Constant;
import com.allsuit.casual.suit.photo.widget.ItemOffsetDecoration;
import com.allsuit.casual.suit.photo.widget.RecyclerItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TemplateActivity extends Activity {


    // private GridViewImageAdapter adapter;
    private TemplateAdapter templateAdapter;
    //   private GridView gridView;

    RecyclerView templateRecyclerView;
    public ArrayList<String> template_array;
    public ArrayList<String> template_overlay_array;
    ApplicationManager applicationManager;

    private ImageView imgBack;
    private TextView txtTitle;


    AppUtility appUtility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.template);

        Log.e("Click", "Template Activity");

        appUtility = new AppUtility(TemplateActivity.this);
        applicationManager = (ApplicationManager) getApplication();
        applicationManager.displayBannerAds(this);
        applicationManager.doDisplayIntrestial();

        template_array = new ArrayList<String>();

        templateRecyclerView = findViewById(R.id.templateRecyclerView);
        templateRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        imgBack = (ImageView) findViewById(R.id.imgBack);


        txtTitle.setText("Select Suit");

        template_array = new ArrayList<String>();
        template_overlay_array = new ArrayList<String>();
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(appUtility.getTemplate());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Log.e("Suit", jsonObject.getString("overlay") + "--");
                template_array.add(jsonObject.getString("suits"));
                template_overlay_array.add(jsonObject.getString("overlay"));

            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        //adapter = new GridViewImageAdapter(TemplateActivity.this, template_array);
        templateAdapter = new TemplateAdapter(TemplateActivity.this, template_array);
        templateRecyclerView.setAdapter(templateAdapter);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(TemplateActivity.this, R.dimen.space_column);
        templateRecyclerView.addItemDecoration(itemDecoration);


        imgBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });


        templateRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(TemplateActivity.this, templateRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        String template = template_array.get(position);
                        String overlay = template_overlay_array.get(position);


                        final Dialog dialog = new Dialog(TemplateActivity.this);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        dialog.setContentView(R.layout.confirmation_dialog);
                        dialog.setCancelable(false);

                        // dialog. getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                        DisplayMetrics displayMetrics = new DisplayMetrics();
                        WindowManager windowmanager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
                        int width = displayMetrics.widthPixels;
                        ;
                        dialog.getWindow().setLayout(((width / 100) * 95), LinearLayout.LayoutParams.WRAP_CONTENT);
                        dialog.getWindow().setGravity(Gravity.CENTER);

                        Log.e("Overlay", overlay);
                        if (!overlay.equalsIgnoreCase("")) {
                            dialog.show();

                        } else {
                            try {
                                ImageView imageView = (ImageView) view.findViewById(R.id.img_template);
                                Bitmap suitBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                                applicationManager.setSuitBitmap(suitBitmap);
                                setResult(RESULT_OK);
                                finish();
                            } catch (Exception e) {
                                Toast.makeText(TemplateActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            }
                        }


                        TextView txtTitle = (TextView) dialog.findViewById(R.id.txtTitle);
                        TextView txtNo = (TextView) dialog.findViewById(R.id.txtNo);
                        TextView txtYes = (TextView) dialog.findViewById(R.id.txtYes);


                        txtTitle.setText("Do You want to change dress color?");

                        //AdRequest adRequest = new AdRequest.Builder().build();


                        txtYes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();


                                Intent intent = new Intent(TemplateActivity.this, SuitColorActivity.class);
                                intent.putExtra("template", template);
                                intent.putExtra("overlay", overlay);
                                startActivityForResult(intent, Constant.SELECT_TAMPLET_EDIT_CODE);
                            }
                        });


                        txtNo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                try {
                                    ImageView imageView = (ImageView) view.findViewById(R.id.img_template);
                                    Bitmap suitBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                                    applicationManager.setSuitBitmap(suitBitmap);
                                    setResult(RESULT_OK);
                                    finish();
                                } catch (Exception e) {
                                    Toast.makeText(TemplateActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                }


                            }
                        });
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );


        //templateRecyclerView.setOnItemClickListener(onItemClickListener);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == Constant.SELECT_TAMPLET_EDIT_CODE) {

            Intent intent = getIntent();
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {

        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
        }
        return super.onKeyDown(keyCode, event);
    }

}
