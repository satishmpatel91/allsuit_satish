package com.allsuit.casual.suit.photo.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.allsuit.casual.suit.photo.holder.ColorHolder;
import com.allsuit.casual.suit.photo.leodroidcoder.genericadapter.GenericRecyclerViewAdapter;
import com.allsuit.casual.suit.photo.leodroidcoder.genericadapter.OnRecyclerItemClickListener;
import com.allsuit.casual.suit.photo.model.Color;
import com.allsuit.casualsuit.R;

import org.jetbrains.annotations.NotNull;

public class ColorAdapter extends GenericRecyclerViewAdapter<Color, OnRecyclerItemClickListener, ColorHolder> {
    public ColorAdapter(Context context, OnRecyclerItemClickListener listener) {
        super(context, listener);
    }

    @NotNull
    @Override
    public ColorHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ColorHolder(inflate(R.layout.color_item, parent), getListener());
    }


}
