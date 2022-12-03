package com.allsuit.casual.suit.photo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;

import com.allsuit.casualsuit.R;
import com.allsuit.casual.suit.photo.utility.AppUtility;
import com.allsuit.casual.suit.photo.utility.Constant;
import com.allsuit.casual.suit.photo.utility.JSONParser;
import com.allsuit.casual.suit.photo.utility.PermissionHelper;

/**
 * Created by Dikesh on 5/17/2017.
 */

public class SpleshActivity extends Activity {





    ApplicationManager applicationManager;
    JSONParser ServerHandler;
    AppUtility appUtility;
    PermissionHelper permissionHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        ServerHandler = new JSONParser();
        appUtility=new AppUtility(SpleshActivity.this);

        applicationManager=(ApplicationManager)getApplication();
      //  applicationManager.appOpenManager.fetchAd();
        applicationManager.doGetAdmob();

        if(appUtility.isVersionGraterQ())
        {
            permissionHelper = new PermissionHelper(SpleshActivity.this, new String[]{Manifest.permission.CAMERA}, Constant.ALL_PERMISSION);
        }else
        {
            permissionHelper = new PermissionHelper(SpleshActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, Constant.ALL_PERMISSION);
        }

        takePermission();


    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
    public void takePermission() {

        permissionHelper.request(new PermissionHelper.PermissionCallback() {
            @Override
            public void onPermissionGranted() {
                Log.d("Permission", "onPermissionGranted() called");
                proceedAfterPermission();

            }

            @Override
            public void onIndividualPermissionGranted(String[] grantedPermission) {

            }

            @Override
            public void onPermissionDenied() {

              takePermission();

            }

            @Override
            public void onPermissionDeniedBySystem() {

                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, Constant.CAMERA_PERMISSION);
            }
        });
    }
    private void proceedAfterPermission() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SpleshActivity.this,StartActivity.class);
                startActivity(intent);
                finish();
            }
        },2000);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissionHelper != null) {
            permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }



}