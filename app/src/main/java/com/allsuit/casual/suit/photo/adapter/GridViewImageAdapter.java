package com.allsuit.casual.suit.photo.adapter;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import com.allsuit.casual.suit.photo.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GridViewImageAdapter extends BaseAdapter {

    private Activity _activity;
    private ArrayList<String> _filePaths;
    Context context;
    private LayoutInflater inflater;

    public GridViewImageAdapter(Activity activity, ArrayList<String> filePaths
    ) {
        this._activity = activity;
        this._filePaths = filePaths;

        this.context = activity.getApplicationContext();
    }

    @Override
    public int getCount() {
        return this._filePaths.size();
    }

    @Override
    public Object getItem(int position) {
        return this._filePaths.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        Log.i("POS", position + "");
        ImageView imageView;


        if (convertView == null) {
            inflater = (LayoutInflater) _activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.template_grid_item, null);
        }


        imageView = (ImageView) convertView.findViewById(R.id.img_template);
        String fnm = _filePaths.get(position);

        Picasso.with(context).load(fnm).placeholder(R.drawable.progress).into(imageView);
       // imageView.setOnClickListener(new OnImageClickListener(position));

        return convertView;
    }

  /*  class OnImageClickListener implements OnClickListener {

        int _postion;

        // constructor
        public OnImageClickListener(int position) {
            this._postion = position;
        }

        @Override
        public void onClick(View v) {

            Bundle conData = new Bundle();
            conData.putInt("param_result", 104);
            Intent intent = new Intent();
            intent.putExtras(conData);
            intent.putExtra("position", _postion);
            _activity.setResult(_activity.RESULT_OK, intent);
            _activity.finish();

        }

    }*/


}
