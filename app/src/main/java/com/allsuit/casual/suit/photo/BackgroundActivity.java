package com.allsuit.casual.suit.photo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;


import com.allsuit.casual.suit.photo.R;
import com.allsuit.casual.suit.photo.adapter.BackgroundAdapter;
import com.allsuit.casual.suit.photo.utility.AppUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BackgroundActivity extends Activity {


	
	private BackgroundAdapter adapter;
	private GridView gridView;
	public ArrayList<String> template_array;
	
	ApplicationManager applicationManager;

	private ImageView imgBack;
	private TextView txtTitle;

	AppUtility appUtility;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.background);
		appUtility=new AppUtility(BackgroundActivity.this);


		applicationManager=(ApplicationManager)getApplication();
		applicationManager.displayBannerAds(this);
		applicationManager.doDisplayIntrestial();

		template_array=new ArrayList<String>();
		
		gridView = (GridView) findViewById(R.id.grid_view);
		txtTitle=(TextView)findViewById(R.id.txtTitle);
		imgBack=(ImageView) findViewById(R.id.imgBack);

		txtTitle.setText("Select Background");

		template_array=new ArrayList<String>();
		template_array.add("");
		JSONArray jsonArray;
		try {
			jsonArray = new JSONArray(appUtility.getBackgrounds());
			for(int i=0;i<jsonArray.length();i++)
			{
				JSONObject jsonObject=jsonArray.getJSONObject(i);
				template_array.add(jsonObject.getString("backgrounds"));

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		adapter = new BackgroundAdapter(BackgroundActivity.this, template_array);
		gridView.setAdapter(adapter);

		imgBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setResult(RESULT_CANCELED);
				finish();
			}
		});
	}
	
	
}
