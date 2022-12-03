package com.allsuit.casual.suit.photo.model;

import android.net.Uri;

/**
 * Created by Dikesh on 20/04/2018.
 */

public class FileModel {

    long id;
    Uri filePath;
    boolean isChecked;
    String displayName;

    public Uri getFilePath() {
        return filePath;
    }

    public void setFilePath(Uri filePath) {
        this.filePath = filePath;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
