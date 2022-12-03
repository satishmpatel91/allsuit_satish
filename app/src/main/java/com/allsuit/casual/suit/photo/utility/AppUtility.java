package com.allsuit.casual.suit.photo.utility;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

public class AppUtility {


    public static String ServerUrl = "http://dikesh1989.pythonanywhere.com/";
    public static String PRIVACY_POLICY = "http://dikesh1989.pythonanywhere.com/static/extra/policy.html";
    public static String appListUrl = ServerUrl + "app_api_new/";
    public static String appImageUrl = ServerUrl + "static/logo/";

//    public static String ServerUrl = "http://allsuitmaster.co.in/adminapp_v2/";
//    public static String PRIVACY_POLICY = "http://allsuitmaster.co.in/policy.html";
//    public static String appListUrl = ServerUrl + "api.php?seq_key=MyCodeISGooDWhatYouRequiredHello&function=list&app_id=";
//    public static String appImageUrl = ServerUrl + "uploads/";

    public static String ADS_PUSH_REGISTER_URL = ServerUrl + "/register_gcm_api.php?";


    private boolean externalStorageAvailable, externalStorageWriteable;

    Context context;
    String TAG = "APPUTIL";
    Uri filesavepath = null;
    ProgressDialog pDialog;

    public static int REQERASECODE = 10001;
    public static int REQFRAMECODE = 10002;


    SharedPreferences sharedpreferences;
    ProgressDialog progressDialog;


    public static int HEIGHT;

    public AppUtility(Context context) {
        // TODO Auto-generated constructor stub
        this.context = context;
        pDialog = new ProgressDialog(context);
        sharedpreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
    }

    public String getRandomFileName() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());

        return "IMG_" + timeStamp + ".png";
    }

    @SuppressLint("SimpleDateFormat")
    public Uri savePhoto(Bitmap bitmap, int width, int height) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            OutputStream fos;
            try {
                ContentResolver contentResolver = context.getContentResolver();
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, getRandomFileName());
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + Constant.ALLSUIT_PHOTO);
                Uri imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

                fos = contentResolver.openOutputStream(imageUri);
                bitmap.compress(CompressFormat.PNG, 100, fos);
                Objects.requireNonNull(fos);
                filesavepath = imageUri;

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    Constant.ALLSUIT_PHOTO);
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.e("MyCameraApp", "failed to create directory");
                    return null;
                }
            }
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                    .format(new Date());
            File mediaFile;
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
            Log.e("PATH", mediaFile.getAbsolutePath());
            bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
            FileOutputStream fos;
            try {
                fos = new FileOutputStream(mediaFile);
                bitmap.compress(CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
            } catch (IOException e) {
               e.printStackTrace();
            }
            filesavepath=Uri.fromFile(mediaFile);

        }
        return filesavepath;
    }

    public Uri saveFace(Bitmap bitmap) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            OutputStream fos;
            try {
                ContentResolver contentResolver = context.getContentResolver();
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, getRandomFileName());
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + Constant.ALLSUIT_FACE);
                Uri imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

                fos = contentResolver.openOutputStream(imageUri);
                bitmap.compress(CompressFormat.PNG, 100, fos);
                Objects.requireNonNull(fos);
                filesavepath = imageUri;

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    Constant.ALLSUIT_FACE);
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.e("MyCameraApp", "failed to create directory");
                    File mediaFile;
                    mediaFile = new File(mediaStorageDir.getPath() + File.separator
                            + ".nomedia");
                }
            }

            File mediaFile;
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    getRandomFileName());
            Log.e("PATH", mediaFile.getAbsolutePath());
            FileOutputStream fos;
            try {
                fos = new FileOutputStream(mediaFile);
                bitmap.compress(CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {
                Log.e("GREC", e.getMessage(), e);
            } catch (IOException e) {
                Log.e("GREC", e.getMessage(), e);
            }
            filesavepath = Uri.fromFile(mediaFile);
        }
        return filesavepath;

    }

    public boolean isExistFaceFolder() {
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                Constant.ALLSUIT_FACE);


        if (mediaStorageDir.exists()) {
            return true;
        }
        return false;
    }

    public Uri getfilesavedname() {
        return filesavepath;
    }

    public boolean isConnectingToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }


    private void checkStorage() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            externalStorageAvailable = externalStorageWriteable = true;
        } else if (state.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
            externalStorageAvailable = true;
            externalStorageWriteable = false;
        } else {
            externalStorageAvailable = externalStorageWriteable = false;
        }
    }

    public void showAdsLoadingDialog() {
        Log.e("APPUTIL", "Display Dialog " + context.getClass().getSimpleName().toString() + "----");

        pDialog.setCancelable(false);
        pDialog.setMessage("Please wait Ads Display...");
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.show();
    }

    public void hideProgressDialog() {
        if (pDialog != null) {
            Log.e("APPUTIL", "Close Dialog " + context.getClass().getSimpleName().toString() + "----");
            pDialog.dismiss();

        }
    }

    public boolean isExternalStorageAvailable() {
        checkStorage();
        return externalStorageAvailable;
    }

    public boolean isExternalStorageWriteable() {
        checkStorage();
        return externalStorageWriteable;
    }

    public boolean isExternalStorageAvailableAndWriteable() {
        checkStorage();
        if (!externalStorageAvailable) {
            return false;
        } else if (!externalStorageWriteable) {
            return false;
        } else {
            return true;
        }
    }

    public void setTemplate(String template) {
        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString("Template", template);

        editor.commit();

    }

    public String getTemplate() {
        String template = sharedpreferences.getString("Template", "");

        return template;

    }


    public void setTopApp(String json) {
        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString("TopApp", json);

        editor.commit();

    }

    public String getTopApp() {
        return sharedpreferences.getString("TopApp", "");
    }


    public void setMostDownloadApp(String json) {
        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString("MostDownload", json);

        editor.commit();

    }

    public String getMostDownloadApp() {
        String json = sharedpreferences.getString("MostDownload", "");
        return json;
    }


    public void setBackgrounds(String template) {
        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString("backgrounds", template);

        editor.commit();

    }

    public String getBackgrounds() {
        String template = sharedpreferences.getString("backgrounds", "");

        return template;

    }

    public void setStickers(String stickers) {
        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString("stickers", stickers);

        editor.commit();

    }

    public String getStickers() {
        String template = sharedpreferences.getString("stickers", "");

        return template;

    }

    public static String getAppRandomRating() {
        float random = 4.0f + (new Random().nextFloat() * (5.0f - 4.0f));
        return String.format("%.1f", new Object[]{Float.valueOf(random)});
    }

    public static String getAppRandomSize() {
       // float random = 2.0f + (new Random().nextFloat() * (5.0f - 2.0f));
        //return String.format("%.1f", new Object[]{Float.valueOf(random)}) + " MB";
        return "12 MB";
    }

    public void setServerUrl(String serverUrl) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("ServerUrl", serverUrl);
        editor.commit();
    }

    public String getServerUrl() {
        String serverUrl = sharedpreferences.getString("ServerUrl", "");
        return serverUrl;
    }

    public void setPrivacyPolicyrUrl(String privacyPolicyrUrl) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("PRIVACY_POLICY", privacyPolicyrUrl);
        editor.commit();
    }

    public String getPrivacyPolicyrUrl() {
        String privacyPolicyrUrl = sharedpreferences.getString("PRIVACY_POLICY", "");
        return privacyPolicyrUrl;
    }

    public void setAppListUrlUrl(String appListUrl) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("appListUrl", appListUrl);
        editor.commit();
    }

    public String getAppListUrl() {
        String appListUrl = sharedpreferences.getString("appListUrl", "");
        return appListUrl;
    }


    public void setUrl(boolean val) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean("urlIsSet", val);
        editor.commit();
    }

    public boolean isSetURL() {
        return sharedpreferences.getBoolean("urlIsSet", false);

    }
    public static File getFile(Context context, Uri uri) throws IOException {
        File destinationFilename = new File(context.getFilesDir().getPath() + File.separatorChar + queryName(context, uri));
        try (InputStream ins = context.getContentResolver().openInputStream(uri)) {
            createFileFromStream(ins, destinationFilename);
        } catch (Exception ex) {
            Log.e("Save File", ex.getMessage());
            ex.printStackTrace();
        }
        return destinationFilename;
    }

    public static void createFileFromStream(InputStream ins, File destination) {
        try (OutputStream os = new FileOutputStream(destination)) {
            byte[] buffer = new byte[4096];
            int length;
            while ((length = ins.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            os.flush();
        } catch (Exception ex) {
            Log.e("Save File", ex.getMessage());
            ex.printStackTrace();
        }
    }

    private static String queryName(Context context, Uri uri) {
        Cursor returnCursor =
                context.getContentResolver().query(uri, null, null, null, null);
        assert returnCursor != null;
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String name = returnCursor.getString(nameIndex);
        returnCursor.close();
        return name;
    }

    public boolean isVersionGraterQ(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            return true;
        else
            return false;
    }

    public boolean isVersionEQUALQ(){
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q)
            return true;
        else
            return false;
    }

}
