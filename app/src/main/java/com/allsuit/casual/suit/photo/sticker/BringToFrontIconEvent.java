package com.allsuit.casual.suit.photo.sticker;

import android.view.MotionEvent;

/**
 * @author wupanjie
 */

public class BringToFrontIconEvent implements StickerIconEvent {
    @Override
    public void onActionDown(StickerView stickerView, MotionEvent event) {

    }

    @Override
    public void onActionMove(StickerView stickerView, MotionEvent event) {

    }

    @Override
    public void onActionUp(StickerView stickerView, MotionEvent event) {

        stickerView.bringChildToFront(stickerView);
        stickerView.invalidate();

    }
}
