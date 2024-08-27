package com.allsuit.casual.suit.photo.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.allsuit.casual.suit.photo.holder.StrokeHolder;
import com.allsuit.casual.suit.photo.leodroidcoder.genericadapter.GenericRecyclerViewAdapter;
import com.allsuit.casual.suit.photo.leodroidcoder.genericadapter.OnRecyclerItemClickListener;
import com.allsuit.casual.suit.photo.model.Color;
import com.allsuit.casualsuit.R;

import org.jetbrains.annotations.NotNull;

public class StrokeAdapter extends GenericRecyclerViewAdapter<Color, OnRecyclerItemClickListener, StrokeHolder> {
    public StrokeAdapter(Context context, OnRecyclerItemClickListener listener) {
        super(context, listener);
    }

    @NotNull
    @Override
    public StrokeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StrokeHolder(inflate(R.layout.stroke_item, parent), getListener());
    }


}
