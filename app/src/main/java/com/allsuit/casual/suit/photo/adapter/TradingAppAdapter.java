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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class TradingAppAdapter extends BaseAdapter {
    
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    
    
    public TradingAppAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
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
        String app_url = null;
        float rate;
        if(convertView==null)
        {
        	vi = inflater.inflate(R.layout.ads_trading_item, null);
        }
            

        TextView title = (TextView)vi.findViewById(R.id.title); // title
        ImageView thumb_image=(ImageView)vi.findViewById(R.id.app_image); // thumb image
        HashMap<String, String> apps = new HashMap<String, String>();
        apps = data.get(position);
        app_url=apps.get("android_link");
        image_url=apps.get("app_logo");
        title.setText(apps.get("app_name").toString());
        Picasso.with(activity).load(image_url).placeholder(R.drawable.progress).into(thumb_image);
     //   ImageLoader.getInstance().displayImage(image_url, thumb_image);
        return vi;
    }
}