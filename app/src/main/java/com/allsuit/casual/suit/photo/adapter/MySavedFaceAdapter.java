package com.allsuit.casual.suit.photo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;


import com.allsuit.casual.suit.photo.R;
import com.allsuit.casual.suit.photo.model.FileModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class MySavedFaceAdapter extends BaseAdapter {

	private Context context;
	/*private String[] filepath;
	private String[] filename;*/
	private ArrayList<FileModel> fileList;


	private static LayoutInflater inflater = null;


	public MySavedFaceAdapter(Context a, ArrayList<FileModel> fileList) {
		context = a;
		this.fileList=fileList;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return fileList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return fileList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null)
		{
			vi = inflater.inflate(R.layout.my_photo_item, null);
		}

		ImageView imgPhotoPreview = (ImageView) vi.findViewById(R.id.imgPhotoPreview);
		CheckBox chkIsPhotoSelect = (CheckBox) vi.findViewById(R.id.chkIsPhotoSelect);

		Picasso.with(context).load(fileList.get(position).getFilePath()).into(imgPhotoPreview);

		chkIsPhotoSelect.setChecked(fileList.get(position).isChecked());
		chkIsPhotoSelect.setTag(fileList.get(position));

		Log.e("Ad SIZE",fileList.size()+"----");
		chkIsPhotoSelect.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				CheckBox cb=(CheckBox) view;
				FileModel fModel=(FileModel) cb.getTag();
				fModel.setChecked(cb.isChecked());
				fileList.get(position).setChecked(cb.isChecked());


			}
		});

		return vi;
	}



}
