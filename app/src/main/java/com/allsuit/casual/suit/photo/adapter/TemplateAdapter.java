package com.allsuit.casual.suit.photo.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.RecyclerView;

import com.allsuit.casual.suit.photo.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TemplateAdapter extends RecyclerView.Adapter<TemplateAdapter.ViewHolder> {
    private Activity _activity;
    private ArrayList<String> _filePaths;
    public TemplateAdapter (Activity _activity,ArrayList<String> _filePaths){
        this._filePaths = _filePaths;
        this._activity = _activity;
    }

    @Override
    public TemplateAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.template_grid_item, parent, false);
        return new ViewHolder(rowItem);
    }

    @Override
    public void onBindViewHolder(TemplateAdapter.ViewHolder holder, int position) {
        holder.progress_circular.setVisibility(View.VISIBLE);
        String fnm = _filePaths.get(position);
       Picasso.with(_activity)
                .load(fnm)
                .into(holder.imageView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {

                        holder.progress_circular.setVisibility(View.GONE);

                    }

                    @Override
                    public void onError() {
                        //do smth when there is picture loading error
                    }
                });



    }

    @Override
    public int getItemCount() {
        return this._filePaths.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private ProgressBar progress_circular;

        public ViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.img_template);
            progress_circular=view.findViewById(R.id.progress_circular);
        }

    }
}
