package com.allsuit.casual.suit.photo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.RecyclerView;

import com.allsuit.casual.suit.photo.R;
import com.allsuit.casual.suit.photo.StickerActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;


public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.Holder> {


    private Context mContext;
    private ArrayList<String> mStickerList;

    public StickerAdapter(Context mContext, ArrayList<String> mStickerList) {
        this.mContext = mContext;
        this.mStickerList = mStickerList;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.sticker_item, parent, false);
        return new Holder(view);

    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {


        final StickerActivity stickerActivity = (StickerActivity) mContext;
        final String sticker_image = mStickerList.get(position);
        holder.progressBar.setVisibility(View.VISIBLE);
     //   Log.e("URL",sticker_image);

        Picasso.with(mContext).load(sticker_image).into(holder.stickersIcon, new Callback() {
            @Override
            public void onSuccess() {
                holder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError() {

            }
        });
        holder.stickersIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Picasso.with(mContext).load(sticker_image).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                       stickerActivity.doAddSticker(bitmap);


                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return mStickerList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {


        private ImageView stickersIcon;
        private ProgressBar progressBar;


        public Holder(View view) {
            super(view);

            stickersIcon = (ImageView) view.findViewById(R.id.stickers_icon);
            progressBar=(ProgressBar)view.findViewById(R.id.progressBar);

        }
    }
}
