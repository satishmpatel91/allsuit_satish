package com.allsuit.casual.suit.photo;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.allsuit.casualsuit.R;
import com.allsuit.casual.suit.photo.adapter.MySavedFaceAdapter;
import com.allsuit.casual.suit.photo.model.FileModel;
import com.allsuit.casual.suit.photo.utility.AppUtility;
import com.allsuit.casual.suit.photo.utility.Constant;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dikesh on 5/11/2017.
 */

public class MySavedFaceActivity extends Activity {


    AppUtility appUtility;
    private GridView photoList;
    MySavedFaceAdapter mySavedFaceAdapter;


    private File[] listFile;

    ArrayList<FileModel> fileList;

    private ImageView imgBack;
    private TextView txtTitle;
    ApplicationManager applicationManager;
    Button btnDelete;
    public static int REQUEST_PERM_DELETE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_saved_face);
        applicationManager = (ApplicationManager) getApplication();
        initComponent();
        initAddListner();


        applicationManager.displayBannerAds(MySavedFaceActivity.this);
        applicationManager.doDisplayIntrestial();

        txtTitle.setText("Select Your Face To Edit");

    }


    void initComponent() {
        // TODO Auto-generated method stub
        appUtility = new AppUtility(MySavedFaceActivity.this);
        fileList = new ArrayList<FileModel>();

        photoList = (GridView) findViewById(R.id.photoList);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        btnDelete = (Button) findViewById(R.id.btnDelete);

        if (appUtility.isExternalStorageAvailable()) {

            if (appUtility.isExternalStorageAvailableAndWriteable()) {
                File mediaStorageDir = new File(
                        Environment
                                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                        Constant.ALLSUIT_FACE);
                if (!mediaStorageDir.exists()) {
                    if (!mediaStorageDir.mkdirs()) {
                        Log.d("MyCameraApp", "failed to create directory");

                    }
                }
                Log.e("File Path", mediaStorageDir.getAbsolutePath());

                if (appUtility.isVersionGraterQ()) {
                    String selection = MediaStore.Images.Media.DATA + " like ? ";
                    String[] selectionArgs = new String[]{"%" + Constant.ALLSUIT_FACE + "%"};
                    Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, selection, selectionArgs, null);
                    if (cursor != null) {
                        while (cursor.moveToNext()) {
                            long id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID));
                            Uri uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

                            FileModel fileModel = new FileModel();
                            fileModel.setFilePath(uri);
                            fileModel.setChecked(false);
                            fileList.add(fileModel);
                        }
                        cursor.close();
                    }
                } else {
                    listFile = mediaStorageDir.listFiles();
                    if (listFile != null) {
                        for (int i = 0; i < listFile.length; i++) {
                            FileModel fileModel = new FileModel();
                            fileModel.setFilePath(Uri.fromFile(listFile[i].getAbsoluteFile()));
                            fileModel.setChecked(false);
                            fileList.add(fileModel);
                            Log.e("File path Absulute", listFile[i].getAbsoluteFile().getAbsolutePath());
                        }

                    }
                }

                if (fileList != null) {
                    mySavedFaceAdapter = new MySavedFaceAdapter(MySavedFaceActivity.this,
                            fileList);
                    photoList.setAdapter(mySavedFaceAdapter);
                }


            }
        }
    }


    void initAddListner() {
        // TODO Auto-generated method stub
        photoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                File file = null;
                Uri fileUri = null;
                if (appUtility.isVersionGraterQ()) {

                    try {
                        file = appUtility.getFile(getApplicationContext(), fileList.get(position).getFilePath());
                        fileUri = fileList.get(position).getFilePath();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    file = new File(fileList.get(position).getFilePath().getPath());
                    fileUri = Uri.fromFile(file);
                }


                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap bitmap = BitmapFactory.decodeFile(file.getPath(), options);
                applicationManager.setBitmap(bitmap);


                Intent intent = new Intent();
                intent.setData(fileUri);
                setResult(RESULT_OK, intent);
                finish();


            }

        });
        imgBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });


        btnDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (appUtility.isVersionGraterQ() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    List<Uri> urisToModify = new ArrayList<>();
                    for (int i = fileList.size() - 1; i >= 0; i--) {
                        FileModel fileModel = fileList.get(i);
                        if (fileModel.isChecked()) {

                            urisToModify.add(fileModel.getFilePath());
                            Log.e("Count", fileModel.getFilePath().getPath() + "--------");
                        }
                    }
                    PendingIntent editPendingIntent = MediaStore.createDeleteRequest(getContentResolver(), urisToModify);
                    try {
                        startIntentSenderForResult(editPendingIntent.getIntentSender(),
                                REQUEST_PERM_DELETE, null, 0, 0, 0);
                    } catch (Exception e) {
                        Toast.makeText(MySavedFaceActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    try {
                        for (int i = fileList.size() - 1; i >= 0; i--) {

                            FileModel fileModel = fileList.get(i);
                            if (fileModel.isChecked()) {
                                String[] id = {String.valueOf(fileModel.getId())};
                                getApplication().getContentResolver().delete(
                                        fileModel.getFilePath(),
                                        null,
                                        null);
                                fileList.remove(i);
                                Log.e("Count", fileModel.getFilePath().getPath() + "--------");
                            }
                        }
                        for (int i = 0; i < fileList.size(); i++) {
                            fileList.get(i).setChecked(false);
                        }
                        mySavedFaceAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        Toast.makeText(MySavedFaceActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }

                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_PERM_DELETE) {
            for (int i = fileList.size() - 1; i >= 0; i--) {
                FileModel fileModel = fileList.get(i);
                if (fileModel.isChecked()) {
                    fileList.remove(i);
                    Log.e("Count", fileModel.getFilePath().getPath() + "--------");
                }
            }
            for (int i = 0; i < fileList.size(); i++) {
                fileList.get(i).setChecked(false);
            }
            mySavedFaceAdapter.notifyDataSetChanged();
        }
    }
}