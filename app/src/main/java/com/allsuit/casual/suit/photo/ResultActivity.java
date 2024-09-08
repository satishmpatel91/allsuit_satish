package com.allsuit.casual.suit.photo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.allsuit.casual.suit.photo.R;
import com.allsuit.casual.suit.photo.utility.AppUtility;

import java.io.File;


public class ResultActivity extends Activity{
	

	
	Button btn_wallpaper,btn_tryagain,btn_share_photo;
	ImageView photoPreview;
	AppUtility appUtility;
	ApplicationManager applicationManager;
	
	Uri selectedImage=null;
	ProgressDialog pDialog;

	private Button btnFeedback;
	private Button btnRate;

	private ImageView imgBack;
	private TextView txtTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.resultimage);
		
		appUtility=new AppUtility(getApplicationContext());
		applicationManager=(ApplicationManager)getApplication();
		applicationManager.displayBannerAds(ResultActivity.this);
		applicationManager.doDisplayIntrestial();
		InitUI();
		DislayImage();
		ClickListners();

	}


	
	
	private void InitUI() {
		txtTitle=(TextView)findViewById(R.id.txtTitle);
		imgBack=(ImageView) findViewById(R.id.imgBack);
		btn_share_photo=(Button)findViewById(R.id.btn_share_photo);
		btn_tryagain=(Button)findViewById(R.id.btn_tryagain);
		btn_wallpaper=(Button)findViewById(R.id.btn_wallpaper);
		photoPreview=(ImageView)findViewById(R.id.photoPreview);
		btnFeedback = (Button)findViewById( R.id.btnFeedback );
		btnRate = (Button)findViewById( R.id.btnRate );

		txtTitle.setText("Share Image");
	}
	
	private void ClickListners() {
		imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		btnFeedback.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
						"mailto",getString(R.string.email), null));
				emailIntent.putExtra(Intent.EXTRA_SUBJECT, "FEEDBACK "+ getString(R.string.app_name));
				emailIntent.putExtra(Intent.EXTRA_TEXT, "");
				startActivity(Intent.createChooser(emailIntent, "Send email..."));
			}
		});
		btnRate.setOnClickListener(new OnClickListener() {
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

		btn_wallpaper.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(ResultActivity.this,MyPhotoActivity.class);
				startActivity(intent);
					
			}
		});
		
		btn_tryagain.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		btn_share_photo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {


				try {
					Intent shareIntent = new Intent(Intent.ACTION_SEND);
					Uri phototUri = selectedImage;
					File file = new File(phototUri.getPath());
					if(file.exists()) {
						// file create success

					} else {
						// file create fail
					}
					shareIntent.setData(phototUri);
					shareIntent.setType("image/png");
					shareIntent.putExtra(Intent.EXTRA_STREAM, phototUri);
					shareIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id="+getPackageName());
					startActivity(Intent.createChooser(shareIntent, "Share Via"));
				}catch (Exception e)
				{
					Toast.makeText(ResultActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
				}


			}
		});
		
	}
	
	
	private void DislayImage() {
		try {
			Uri path = applicationManager.getImagesavepath();
			selectedImage = path;
			Log.e("Path",applicationManager.getImagesavepath().toString());
			photoPreview.setImageURI(selectedImage);
		}catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	//interstitial.show();
	    	
	    	finish();
	        return true;
	    }

	    return super.onKeyDown(keyCode, event);
	}

}
