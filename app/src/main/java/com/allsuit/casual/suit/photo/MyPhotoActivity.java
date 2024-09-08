package com.allsuit.casual.suit.photo;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
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

import androidx.annotation.RequiresApi;

import com.allsuit.casual.suit.photo.R;
import com.allsuit.casual.suit.photo.adapter.MyPhotoAdapter;
import com.allsuit.casual.suit.photo.model.FileModel;
import com.allsuit.casual.suit.photo.utility.AppUtility;
import com.allsuit.casual.suit.photo.utility.Constant;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MyPhotoActivity extends Activity {


    AppUtility appUtility;
    private GridView photoList;
    MyPhotoAdapter myPhotoAdapter;


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
        setContentView(R.layout.my_photo_activity);
        applicationManager = (ApplicationManager) getApplication();

        initComponent();
        initAddListner();


        applicationManager.displayBannerAds(MyPhotoActivity.this);
        applicationManager.doDisplayIntrestial();

        txtTitle.setText("My Saved Photo");

    }


    void initComponent() {
        // TODO Auto-generated method stub
        appUtility = new AppUtility(MyPhotoActivity.this);
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
                        Constant.ALLSUIT_PHOTO);
                if (!mediaStorageDir.exists()) {
                    if (!mediaStorageDir.mkdirs()) {
                        Log.d("MyCameraApp", "failed to create directory");

                    }
                }
                Log.e("File Path", mediaStorageDir.getAbsolutePath());

                if (appUtility.isVersionGraterQ()) {
                    String selection = MediaStore.Images.Media.DATA + " like ? ";
                    String[] selectionArgs = new String[]{"%" + Constant.ALLSUIT_PHOTO + "%"};
                    Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, selection, selectionArgs, null);
                    if (cursor != null) {
                        while (cursor.moveToNext()) {
                            long id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID));
                            Uri uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

                            FileModel fileModel = new FileModel();
                            fileModel.setFilePath(uri);
                            fileModel.setChecked(false);
                            fileModel.setId(id);
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
                    myPhotoAdapter = new MyPhotoAdapter(MyPhotoActivity.this,
                            fileList);
                    photoList.setAdapter(myPhotoAdapter);
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

                applicationManager.setImagesavepath(fileList.get(position).getFilePath());
                Intent intent = new Intent(MyPhotoActivity.this, ResultActivity.class);


                startActivity(intent);

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


            @RequiresApi(api = Build.VERSION_CODES.R)
            @Override
            public void onClick(View view) {
                if (appUtility.isVersionGraterQ() && !appUtility.isVersionEQUALQ()) {
                    List<Uri> urisToModify = new ArrayList<>();
                    for (int i = fileList.size() - 1; i >= 0; i--) {
                        FileModel fileModel = fileList.get(i);
                        if (fileModel.isChecked()) {
                      /*  File file=new File(fileModel.getFilePath().getPath());
                        file.delete();
                        fileList.remove(i);*/
                            urisToModify.add(fileModel.getFilePath());
                            Log.e("Count", fileModel.getFilePath().getPath() + "--------");
                        }
                    }
                    PendingIntent editPendingIntent = MediaStore.createDeleteRequest(getContentResolver(), urisToModify);
                    try {
                        startIntentSenderForResult(editPendingIntent.getIntentSender(),
                                REQUEST_PERM_DELETE, null, 0, 0, 0);
                    } catch (Exception e) {
                        Toast.makeText(MyPhotoActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                    }
                } else if(appUtility.isVersionEQUALQ()) {

                  for (int i = fileList.size() - 1; i >= 0; i--) {

                        FileModel fileModel = fileList.get(i);
                        if (fileModel.isChecked()) {
                            String[] id={String.valueOf(fileModel.getId())};
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
                    myPhotoAdapter.notifyDataSetChanged();
                }else
                {
                      for (int i = fileList.size() - 1; i >= 0; i--) {

                        FileModel fileModel = fileList.get(i);
                        if (fileModel.isChecked()) {
                            File file = new File(fileModel.getFilePath().getPath());

                             file.delete();
                            fileList.remove(i);
                            Log.e("Count", fileModel.getFilePath().getPath() + "--------");
                        }
                    }
                    for (int i = 0; i < fileList.size(); i++) {
                        fileList.get(i).setChecked(false);
                    }
                    myPhotoAdapter.notifyDataSetChanged();
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
            myPhotoAdapter.notifyDataSetChanged();
        }
    }
}