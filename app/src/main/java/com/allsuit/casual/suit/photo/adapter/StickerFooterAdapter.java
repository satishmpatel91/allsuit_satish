package com.allsuit.casual.suit.photo.adapter;

import android.content.Context;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;


import com.allsuit.casualsuit.R;
import com.allsuit.casual.suit.photo.StickerActivity;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.allsuit.casualsuit.R.color.rang;
import static com.allsuit.casualsuit.R.color.rang_dark;


public class StickerFooterAdapter extends RecyclerView.Adapter<StickerFooterAdapter.Holder>  {


    private Context mContext;
    private ArrayList<String> mStickerList;
    private int checkedPosition = 0;
    StickerActivity stickerActivity;
    public StickerFooterAdapter(Context mContext, ArrayList<String> mStickerList) {
        this.mContext = mContext;
        this.mStickerList = mStickerList;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.sticker_footer_item, parent, false);
        return new Holder(view);

    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {
        Log.e("POS",position+"");

         stickerActivity= (StickerActivity) mContext;
        final String wallpaper_image = mStickerList.get(position);
        holder.progressBar.setVisibility(View.VISIBLE);
        Picasso.with(mContext).load(wallpaper_image).into(holder.stickersIcon, new Callback() {
            @Override
            public void onSuccess() {
                holder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError() {

            }
        });

        holder.onSelection(holder);


    }


    @Override
    public int getItemCount() {
        return mStickerList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {


        private ImageView stickersIcon;
        private ProgressBar progressBar;
        private  View mView;

        public Holder(View view) {
            super(view);

            stickersIcon = (ImageView) view.findViewById(R.id.stickers_icon);
            progressBar=(ProgressBar)view.findViewById(R.id.progressBar);
            mView=view;

        }
        private void onSelection(Holder holder) {
            if (checkedPosition == -1) {
                holder.mView.setBackgroundColor(ContextCompat.getColor(mContext,rang));
                Log.e("TAG ","Default UnSelected");
            } else {
                if (checkedPosition == getAdapterPosition()) {
                    Log.e("TAG ","Selected");
                    holder.mView.setBackgroundColor(ContextCompat.getColor(mContext,rang_dark));
                } else {
                    holder.mView.setBackgroundColor(ContextCompat.getColor(mContext,rang));
                    Log.e("TAG ","UNSelected");
                }
            }
            stickersIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mView.setBackgroundColor(ContextCompat.getColor(mContext,rang_dark));
                    stickerActivity.showStickerList(getAdapterPosition());
                    if (checkedPosition != getAdapterPosition()) {
                        notifyItemChanged(checkedPosition);
                        checkedPosition = getAdapterPosition();
                    }
                }
            });
        }




    }
}
