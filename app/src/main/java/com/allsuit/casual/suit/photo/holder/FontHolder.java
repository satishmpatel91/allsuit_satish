package com.allsuit.casual.suit.photo.holder;

import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

import com.allsuit.casual.suit.photo.leodroidcoder.genericadapter.BaseViewHolder;
import com.allsuit.casual.suit.photo.leodroidcoder.genericadapter.OnRecyclerItemClickListener;
import com.allsuit.casual.suit.photo.model.Font;
import com.allsuit.casualsuit.R;

import io.github.inflationx.calligraphy3.TypefaceUtils;

public class FontHolder extends BaseViewHolder<Font, OnRecyclerItemClickListener> {

    TextView tvName;

    public FontHolder(View itemView, final OnRecyclerItemClickListener listener) {
        super(itemView, listener);
        tvName=itemView.findViewById(R.id.tvName);
        if (listener != null) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(FontHolder.this.getAdapterPosition());
                }
            });
        }
    }

    @Override
    public void onBind(Font item) {
      Typeface tf= TypefaceUtils.load(itemView.getContext().getAssets(), item.getFontPath());
        tvName.setTypeface(tf);
        tvName.setText(item.getFontName());

    }
}
