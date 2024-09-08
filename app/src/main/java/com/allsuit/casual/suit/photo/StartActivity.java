package com.allsuit.casual.suit.photo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.core.content.FileProvider;



import com.allsuit.casual.suit.photo.adapter.TradingAppAdapter;
import com.allsuit.casual.suit.photo.utility.AppUtility;
import com.allsuit.casual.suit.photo.utility.Constant;
import com.allsuit.casual.suit.photo.utility.DisplayMetricsHandler;
import com.allsuit.casual.suit.photo.utility.JSONParser;



import com.canhub.cropper.CropImage;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

//import com.appodeal.ads.Appodeal;


public class StartActivity extends Activity {


    ArrayList<HashMap<String, String>> tradingAppList;
    GridView tradingAppGridView;
    TradingAppAdapter tradingAppAdapter;


    /*ProgressDialog pDialog;*/
    JSONParser ServerHandler;
    ApplicationManager applicationManager;

    private static int RESULT_LOAD_IMAGE = 1;
    private static int RESULT_LOAD_FACE = 2;
    private static final String TAG = "Touch";


    private static final int CAMERA_REQUEST = 1888;

    AppUtility appUtility;
    String mCurrentPhotoPath;
    File mPhotoFile;
    Uri mCapturePhotoUri;


    private ImageView imgCamera;
    private ImageView imgGallery;
    private LinearLayout lnvRateApp;
    private LinearLayout lnvMyAlbum;
    private LinearLayout lnvShareApp;
    private LinearLayout lnvPrivacyPolicy;
    Uri resultUri;

    String faceUri;


    boolean isCamera = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.start_activity);

        applicationManager = (ApplicationManager) getApplication();
        /*   applicationManager.appOpenManager.showAdIfAvailable()*/
        appUtility = new AppUtility(StartActivity.this);

        try {
            applicationManager.doDisplayIntrestial();
        } catch (Exception e) {
            Toast.makeText(StartActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }


        imgCamera = (ImageView) findViewById(R.id.imgCamera);
        imgGallery = (ImageView) findViewById(R.id.imgGallery);
        lnvRateApp = (LinearLayout) findViewById(R.id.lnvRateApp);
        lnvMyAlbum = (LinearLayout) findViewById(R.id.lnvMyAlbum);
        lnvShareApp = (LinearLayout) findViewById(R.id.lnvShareApp);
        lnvPrivacyPolicy = (LinearLayout) findViewById(R.id.lnvPrivacyPolicy);


        applicationManager.displayBannerAds(this);
        tradingAppList = new ArrayList<HashMap<String, String>>();
        tradingAppGridView = (GridView) findViewById(R.id.offerlist);
        ServerHandler = new JSONParser();
        new loaddata().execute();

        tradingAppGridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                String applink = tradingAppList.get(position).get("android_link");

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


        lnvPrivacyPolicy.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                try {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(AppUtility.PRIVACY_POLICY));
                    startActivity(browserIntent);
                } catch (Exception e) {
                    Toast.makeText(StartActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }

            }
        });

        imgGallery.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (appUtility.isVersionGraterQ()) {
                    choosePhoto();

                } else {
                    isCamera = false;

                    choosePhoto();
                }


            }
        });

        imgCamera.setOnClickListener(new OnClickListener() {


            @Override
            public void onClick(View v) {
                isCamera = true;

                capturePhoto();

            }

        });


        lnvMyAlbum.setOnClickListener(new OnClickListener() {

            @SuppressLint("SdCardPath")
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(StartActivity.this, MyPhotoActivity.class);
                startActivity(intent);

            }
        });

        lnvRateApp.setOnClickListener(new OnClickListener() {
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

        lnvShareApp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String shareBody = "Hey friends i like this app i must recommend to you try https://play.google.com/store/apps/details?id=" + getPackageName();
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Download the application from Google Play Store");

                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

    }


    public void choosePhoto() {
        try {
            if (!isFaceAvailable()) {


                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            } else {
                final CharSequence[] items = {"Select From Gallery", "Choose From Saved Face",
                        "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(StartActivity.this);
                //		builder.setTitle("Add Photo!");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {

                        if (items[item].equals("Select From Gallery")) {

                            Intent i = new Intent(
                                    Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                            startActivityForResult(i, RESULT_LOAD_IMAGE);
                        } else if (items[item].equals("Choose From Saved Face")) {
                            Intent intent = new Intent(StartActivity.this, MySavedFaceActivity.class);

                            startActivityForResult(intent, RESULT_LOAD_FACE);

                        } else if (items[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void capturePhoto() {
        try {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                // Create the File where the photo should go
                File photoFile = null;

                photoFile = createImageFile();

                if (photoFile != null) {
                    mCapturePhotoUri = FileProvider.getUriForFile(StartActivity.this,
                              getPackageName()+".provider",
                            photoFile);
                    mPhotoFile = photoFile;
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturePhotoUri);
                    startActivityForResult(takePictureIntent, CAMERA_REQUEST);
                }

        } catch (Exception ex) {
            ex.printStackTrace();
            // Error occurred while creating the File
        }
    }


    public boolean isFaceAvailable() {
        boolean isAvailable = false;
        File[] listFile;
        if (appUtility.isExternalStorageAvailable()) {

            if (appUtility.isExternalStorageAvailableAndWriteable()) {

                if (appUtility.isVersionGraterQ()) {
                    String selection = MediaStore.Images.Media.DATA + " like ? ";
                    String[] selectionArgs = new String[]{"%" + Constant.ALLSUIT_FACE + "%"};
                    Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, selection, selectionArgs, null);
                    if (cursor != null) {
                        Log.e("Count", cursor.getCount() + "---");
                        if (cursor.getCount() > 0) {
                            isAvailable = true;
                        }


                        cursor.close();

                    }
                } else {
                    File mediaStorageDir = new File(
                            Environment
                                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                            Constant.ALLSUIT_FACE);

                    listFile = mediaStorageDir.listFiles();

                    if (listFile != null) {
                        if (listFile.length < 1) {
                            isAvailable = false;

                        } else {
                            isAvailable = true;
                        }
                    }
                }


            }
        }
        return isAvailable;
    }



    public class loaddata extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            /*pDialog = new ProgressDialog(StartActivity.this);
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();*/
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                String app_url = AppUtility.appListUrl + getString(R.string.APP_ID);

                String image_url = AppUtility.appImageUrl;
                List<NameValuePair> parameter = new ArrayList<NameValuePair>();
                JSONObject json = ServerHandler.makeHttpRequest(
                        app_url, "GET", parameter);

                String data = json.toString();

                Log.e("data", "---------" + data);
                if (data != "") {
                    JSONObject jobj = new JSONObject(data);
                    if (jobj.get("status").equals("true")) {
                        JSONArray jsonarray = jobj.getJSONArray("trading_apps");
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jdata = jsonarray.getJSONObject(i);
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("app_name", jdata.getString("app_name"));
                            map.put("android_link", jdata.getString("android_link"));
                            map.put("app_logo", image_url + "" + jdata.getString("app_logo"));
                            tradingAppList.add(map);
                        }
                        JSONArray suitsarray = jobj.getJSONArray("suits");
                        JSONArray backgroundsarray = jobj.getJSONArray("backgrounds");
                        JSONArray stickerArray = jobj.getJSONArray("stickers");
                        JSONArray topAppArray = jobj.getJSONArray("top_apps");
                        Log.e("Count", topAppArray.length() + "-----------------");
                        JSONArray mostDownloadArray = jobj.getJSONArray("most_download_apps");
                        appUtility.setTopApp(topAppArray.toString());
                        appUtility.setMostDownloadApp(mostDownloadArray.toString());
                        appUtility.setTemplate(suitsarray.toString());
                        appUtility.setBackgrounds(backgroundsarray.toString());
                        appUtility.setStickers(stickerArray.toString());
                    }
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            try {
                tradingAppAdapter = new TradingAppAdapter(StartActivity.this, tradingAppList);

                tradingAppGridView.setAdapter(tradingAppAdapter);
                /*pDialog.dismiss();*/
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onCancelled() {
            // TODO Auto-generated method stub
            super.onCancelled();
            /*pDialog.dismiss();*/
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

      /*  if (requestCode == REQUEST_PERMISSION_CAMERA_SETTING && resultCode == RESULT_OK) {
            if (ActivityCompat.checkSelfPermission(StartActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                capturePhoto();
            }
        } else if (requestCode == REQUEST_PERMISSION_GALLERY_SETTING && resultCode == RESULT_OK) {
            if (ActivityCompat.checkSelfPermission(StartActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                choosePhoto();
            }
        }*/
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
                && null != data) {


            final Uri selectedUri = data.getData();
            if (selectedUri != null) {

                CropImage.activity(selectedUri)
                        .start(this);

            } else {
                Toast.makeText(StartActivity.this, "Error on selected image", Toast.LENGTH_SHORT).show();
            }
        } else if (resultCode == RESULT_OK && requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            Bitmap bitmap = null;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;


            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            resultUri = result.getUri();

            Intent intent = new Intent(StartActivity.this, TemplateActivity.class);

            System.out.println(resultUri.getPath());

          //  bitmap = BitmapFactory.decodeFile(resultUri.getPath(), options);
                    bitmap=getBitmap(StartActivity.this,resultUri);
            int m = DisplayMetricsHandler.getScreenHeight();
            int n = (DisplayMetricsHandler.getScreenWidth() * bitmap.getHeight()) / bitmap.getWidth();
            if (n <= m) {
                Constant.HEIGHT = n;
            } else {
                Constant.HEIGHT = m;
            }
           int final_width = (int) Math.ceil((double) ((((float) Constant.HEIGHT) * ((float) bitmap.getWidth())) / ((float) bitmap.getHeight())));
           Bitmap tempBitmap = Bitmap.createScaledBitmap(bitmap, final_width, Constant.HEIGHT, false);


          applicationManager.setBitmap(tempBitmap);
            startActivityForResult(intent, Constant.SELECT_TAMPLET_CODE);
        } else if (resultCode == RESULT_OK && requestCode == Constant.SELECT_TAMPLET_CODE) {


            Intent intent = new Intent(StartActivity.this, Gallery_Image.class);
            if (resultUri != null) {
                intent.putExtra("uri", resultUri.getPath());
            } else {
                intent.putExtra("uri", faceUri);
            }
            startActivity(intent);
        } else if (resultCode == RESULT_OK && requestCode == RESULT_LOAD_FACE) {

            faceUri = data.getData().toString();
            Log.e("Uri Selected Fce", faceUri);


            Intent intent = new Intent(StartActivity.this, TemplateActivity.class);
            startActivityForResult(intent, Constant.SELECT_TAMPLET_CODE);
        } else if (requestCode == 102 && resultCode == RESULT_OK) {
            finish();
            System.exit(0);
        } else if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {


            if (mCapturePhotoUri != null) {
                CropImage.activity(mCapturePhotoUri)
                        .start(this);
            } else {
                Toast.makeText(StartActivity.this, "Error on selected image", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public static Bitmap getBitmap(Context context, Uri imageURI) {
        try {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                return ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.getContentResolver(), imageURI));
            } else {
                InputStream
                        inputStream = context.getContentResolver().openInputStream(imageURI);
                return BitmapFactory.decodeStream(inputStream);

            }
        } catch (Exception e) {
            return null;
        }

    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onBackPressed() {

        if (this.appUtility.isConnectingToInternet()) {
            startActivityForResult(new Intent(this, AdsActivity.class), 102);
        } else {
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();

        }
        return false;
    }


}
