package com.allsuit.casual.suit.photo.holder;

import android.view.View;

import com.allsuit.casualsuit.R;
import com.allsuit.casual.suit.photo.model.Color;
import com.leodroidcoder.genericadapter.BaseViewHolder;
import com.leodroidcoder.genericadapter.OnRecyclerItemClickListener;
import com.mikhaellopez.circleview.CircleView;

public class ColorHolder extends BaseViewHolder<Color, OnRecyclerItemClickListener> {

  public   CircleView cvColorView;


    public ColorHolder(View itemView, final OnRecyclerItemClickListener listener) {
        super(itemView, listener);
        cvColorView = itemView.findViewById(R.id.cvColorView);

        if (listener != null) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(ColorHolder.this.getAdapterPosition());
                }
            });
        }
    }

    @Override
    public void onBind(Color item) {
        cvColorView.setCircleColor(item.getValue());
        if (item.isSelected()) {
            cvColorView.setBorderWidth(10.0f);
            cvColorView.setBorderColor(R.color.Black);
            cvColorView.setShadowEnable(true);
            cvColorView.setShadowColor(R.color.White);
            cvColorView.setShadowRadius(1.0f);
            cvColorView.setShadowGravity(CircleView.ShadowGravity.CENTER);
        } else {
            cvColorView.setBorderWidth(0.0f);
            cvColorView.setShadowEnable(false);
        }
    }
}
