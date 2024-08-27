package com.allsuit.casual.suit.photo.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.allsuit.casual.suit.photo.holder.FontHolder;
import com.allsuit.casual.suit.photo.leodroidcoder.genericadapter.GenericRecyclerViewAdapter;
import com.allsuit.casual.suit.photo.leodroidcoder.genericadapter.OnRecyclerItemClickListener;
import com.allsuit.casual.suit.photo.model.Font;
import com.allsuit.casualsuit.R;

public class FontAdapter extends GenericRecyclerViewAdapter<Font, OnRecyclerItemClickListener, FontHolder> {
    public FontAdapter(Context context, OnRecyclerItemClickListener listener) {
        super(context, listener);
    }

    @Override
    public FontHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FontHolder(inflate(R.layout.font_item, parent), getListener());
    }
}
