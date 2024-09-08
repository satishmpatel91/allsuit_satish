package com.allsuit.casual.suit.photo.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.allsuit.casual.suit.photo.R;
import com.allsuit.casual.suit.photo.utility.AppUtility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class MostDownloadAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;


    public MostDownloadAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        String image_url;


        if(convertView==null)
        {
        	vi = inflater.inflate(R.layout.ads_most_download_item, null);
        }

        TextView title = (TextView)vi.findViewById(R.id.txtTitle); // title
        TextView txtRating = (TextView)vi.findViewById(R.id.txtRating);
        TextView txtSize = (TextView)vi.findViewById(R.id.txtSize); // title
        ImageView thumb_image=(ImageView)vi.findViewById(R.id.imgIcon); // thumb image
        HashMap<String, String> apps = new HashMap<String, String>();
        apps = data.get(position);

        image_url=apps.get("app_logo");
        title.setText(apps.get("app_name").toString());
        txtSize.setText(AppUtility.getAppRandomSize());
        txtRating.setText(AppUtility.getAppRandomRating());
        Picasso.with(activity).load(image_url).placeholder(R.drawable.progress).into(thumb_image);
     //   ImageLoader.getInstance().displayImage(image_url, thumb_image);
        return vi;
    }
}