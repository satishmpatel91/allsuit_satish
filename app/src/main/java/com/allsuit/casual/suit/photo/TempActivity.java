package com.allsuit.casual.suit.photo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.allsuit.casual.suit.photo.R;

public class TempActivity extends Activity {

  // ApplicationManager applicationManager;

    private ImageView imageView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);
       // applicationManager=(ApplicationManager)getApplication();
        findViews();
       // imageView.setImageBitmap(applicationManager.getSuitBitmap());
    }


    private void findViews() {
        imageView = (ImageView)findViewById( R.id.imageView );
    }

}
