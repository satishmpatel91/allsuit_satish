package com.allsuit.casual.suit.photo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.allsuit.casualsuit.R;
import com.allsuit.casual.suit.photo.model.StickerData;
import com.allsuit.casual.suit.photo.model.StickerImage;
import com.allsuit.casual.suit.photo.sticker.DrawableSticker;
import com.allsuit.casual.suit.photo.sticker.Sticker;
import com.allsuit.casual.suit.photo.sticker.StickerView;
import com.allsuit.casual.suit.photo.utility.AppUtility;
import com.allsuit.casual.suit.photo.utility.Constant;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class Gallery_Image extends Activity {

    AppUtility appUtility;
    ApplicationManager applicationManager;
    FrameLayout frm_preview;

    ImageView background_image_view;
    ImageView photoPreview;

    LinearLayout lnvAddDress, lnvFaceEdit, lnvAddSticker, lnvEditDress, lnvNext;

    LinearLayout lnvBackground, lnvBack, lnvFlip, lnvText, lnvSave;


    LinearLayout lnvMenu, lnvMenu2;

    public ArrayList<String> img_templates = new ArrayList<String>();

    private static int RESULT_BACKGROUND_GALLERY_IMAGE = 2;
    private static final String TAG = "Touch";
    // These matrices will be used to move and zoom image
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();

    // We can be in one of these 3 states
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    // Remember some things for zooming
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;

    float[] lastEvent = null;
    float d = 0f;
    float newRot = 0f;

    int windowwidth;
    int windowheight;

    int height, width;


    ArrayList<String> template_array;
    ArrayList<String> template_overlay__array;
    ArrayList<String> background_array;
    ImageView imgTemplate;
    int position = 0;


    boolean isFlip = true;


    File file;


    boolean isErased = false;


    private ArrayList<View> mStickerList;
    StickerView stickerView;
    boolean isEdit = false;
    EventBus mEventBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mEventBus = EventBus.getDefault();
        setContentView(R.layout.gallery_image);


        stickerView = findViewById(R.id.stickerView);
        stickerView.configDefaultIcons();
        height = getWindowManager().getDefaultDisplay().getHeight();
        width = getWindowManager().getDefaultDisplay().getWidth();
        InitUI();

        applicationManager.displayBannerAds(Gallery_Image.this);
        applicationManager.doDisplayIntrestial();
        Log.i("TAG", "Resume");
        template_array = new ArrayList<String>();
        template_overlay__array = new ArrayList<String>();
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(appUtility.getTemplate());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                template_array.add(jsonObject.getString("suits"));
                template_overlay__array.add(jsonObject.getString("overlay"));

            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        background_array = new ArrayList<String>();
        background_array.add("");
        JSONArray jsonArray2;
        try {
            jsonArray2 = new JSONArray(appUtility.getBackgrounds());
            for (int i = 0; i < jsonArray2.length(); i++) {
                JSONObject jsonObject = jsonArray2.getJSONObject(i);
                background_array.add(jsonObject.getString("backgrounds"));

            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        changeImageInTemplate();


        // InitPager();
        DisplayImage();
        setonClickListners();

    }

	
	/*@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}
*/


    public void changeImageInTemplate() {

        try {

            // Picasso.with(Gallery_Image.this).load(template_array.get(position)).placeholder(R.drawable.progress).into(imgTemplate);

            imgTemplate.setImageBitmap(applicationManager.getSuitBitmap());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void DisplayImage() {

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        // get data via the key
        String image_uri = extras.getString("uri");
        if (image_uri != null) {


            Bitmap bitmap = applicationManager.getBitmap();
            photoPreview.setImageBitmap(bitmap);


            file = new File(image_uri);


        }

    }

    private void InitUI() {
        appUtility = new AppUtility(getApplicationContext());
        applicationManager = (ApplicationManager) getApplication();
        frm_preview = (FrameLayout) findViewById(R.id.frm_preview);

        background_image_view = (ImageView) findViewById(R.id.background_image_view);
        photoPreview = findViewById(R.id.photoPreview);

        mStickerList = new ArrayList<>();

        lnvAddSticker = (LinearLayout) findViewById(R.id.lnvAddSticker);
        lnvFaceEdit = (LinearLayout) findViewById(R.id.lnvFaceEdit);
        lnvEditDress = (LinearLayout) findViewById(R.id.lnvEditDress);
        lnvAddDress = (LinearLayout) findViewById(R.id.lnvAddDress);
        lnvBackground = (LinearLayout) findViewById(R.id.lnvBackground);
        lnvNext = (LinearLayout) findViewById(R.id.lnvNext);


        lnvBack = (LinearLayout) findViewById(R.id.lnvBack);
        lnvFlip = (LinearLayout) findViewById(R.id.lnvFlip);
        lnvText = (LinearLayout) findViewById(R.id.lnvText);
        lnvSave = (LinearLayout) findViewById(R.id.lnvSave);

        lnvMenu = (LinearLayout) findViewById(R.id.lnvMenu);
        lnvMenu2 = (LinearLayout) findViewById(R.id.lnvMenu2);


        imgTemplate = (ImageView) findViewById(R.id.imgTemplate);


    }


    private void setonClickListners() {

        stickerView.setOnStickerOperationListener(new com.allsuit.casual.suit.photo.sticker.StickerView.OnStickerOperationListener() {
            @Override
            public void onStickerAdded(@NonNull Sticker sticker) {

            }

            @Override
            public void onStickerClicked(@NonNull Sticker sticker) {

            }

            @Override
            public void onStickerDeleted(@NonNull Sticker sticker) {

            }

            @Override
            public void onStickerDragFinished(@NonNull Sticker sticker) {

            }

            @Override
            public void onStickerTouchedDown(@NonNull Sticker sticker) {

            }

            @Override
            public void onStickerZoomFinished(@NonNull Sticker sticker) {

            }

            @Override
            public void onStickerFlipped(@NonNull Sticker sticker) {

            }

            @Override
            public void onStickerDoubleTapped(@NonNull Sticker sticker) {
                if (sticker.isText()) {
                    //  isEdit = true;
                    //  StickerData stickerData= sticker.getStickerData();

                  /*  Intent intent = new Intent(Gallery_Image.this, TextActivity.class);
                    intent.putExtra("isEdit", isEdit);
                    startActivityForResult(intent, 100);*/

                    ///  intent.putExtra("isEdit", isEdit);
                    Intent intent = new Intent(Gallery_Image.this, TextActivity.class);
                    startActivity(intent);
                    EventBus.getDefault().postSticky(sticker.getStickerData().getFontStyle());

                }

            }
        });
        lnvAddDress.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Gallery_Image.this,
                        TemplateActivity.class), Constant.SELECT_TAMPLET_CODE);

            }
        });

        lnvEditDress.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Gallery_Image.this, DressEraseActivity.class);
                startActivityForResult(intent, Constant.TAMPLET_ERASE_CODE);
            }
        });

        lnvFaceEdit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Gallery_Image.this, EraseActivity.class);
                startActivityForResult(intent, AppUtility.REQERASECODE);

            }
        });
        lnvAddSticker.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                isEdit = true;
                Intent intent = new Intent(Gallery_Image.this, StickerActivity.class);
                intent.putExtra("isEdit", isEdit);
                startActivity(intent);


                /*Intent intent = new Intent(Gallery_Image.this, StickerActivity.class);
                startActivityForResult(intent, 120);*/
            }
        });
        lnvNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                lnvMenu.setVisibility(View.GONE);
                lnvMenu2.setVisibility(View.VISIBLE);

            }
        });
        lnvBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                lnvMenu.setVisibility(View.VISIBLE);
                lnvMenu2.setVisibility(View.GONE);
            }
        });
        lnvFlip.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Bitmap flipBitmap;
                if (isFlip) {
                    //   photoPreview.setScaleX(-1.0f);
                    flipBitmap = createFlippedBitmap(applicationManager.getBitmap(), true, false);
                    isFlip = false;
                    photoPreview.setImageBitmap(flipBitmap);
                } else {
                    //  photoPreview.setScaleX(1.0f);
                    isFlip = true;
                    flipBitmap = createFlippedBitmap(applicationManager.getBitmap(), true, false);
                    photoPreview.setImageBitmap(flipBitmap);
                }
                applicationManager.setBitmap(flipBitmap);

            }
        });

        lnvText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent = new Intent(Gallery_Image.this, AddTextActivity.class);
                startActivityForResult(intent, Constant.REQ_ADD_TEXT_CODE);*/
                isEdit = false;
                Intent intent = new Intent(Gallery_Image.this, TextActivity.class);
                intent.putExtra("isEdit", isEdit);
                startActivity(intent);
                //     startActivityForResult(intent, 100);

            }
        });
        lnvSave.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub


                if (stickerView != null) {
                    stickerView.setLocked(true);
                }


                View v1 = frm_preview;
                v1.setDrawingCacheEnabled(true);
                Bitmap bm = v1.getDrawingCache();
                int layoutHight = v1.getHeight();
                int layoutWidth = v1.getWidth();
                Log.e("HHHH", "Height" + layoutHight + "wid" + windowwidth);

                Uri saveimage = appUtility.savePhoto(bm, layoutWidth, layoutHight);

                applicationManager.setImagesavepath(saveimage);
                Toast.makeText(getApplicationContext(),
                        "Image Saved:-" + appUtility.getfilesavedname().getPath(),
                        Toast.LENGTH_LONG).show();

                if (applicationManager.getFaceBitmap() != null) {
                    appUtility.saveFace(applicationManager.getFaceBitmap());

                }

                startActivity(new Intent(Gallery_Image.this,
                        ResultActivity.class));
                finish();


            }
        });
        lnvBackground.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Gallery_Image.this, BackgroundActivity.class);
                startActivityForResult(intent, AppUtility.REQFRAMECODE);
            }
        });


        photoPreview.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ImageView view = (ImageView) v;
                view.setScaleType(ImageView.ScaleType.MATRIX);
                float scale;

                // Dump touch event to log
                dumpEvent(event);

                // Handle touch events here...
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN: // first finger down only
                        savedMatrix.set(matrix);
                        start.set(event.getX(), event.getY());
                        Log.e(TAG, "mode=DRAG");
                        mode = DRAG;
                        break;

                    case MotionEvent.ACTION_POINTER_DOWN:
                        oldDist = spacing(event);
                        if (oldDist > 10f) {
                            savedMatrix.set(matrix);
                            midPoint(mid, event);
                            mode = ZOOM;
                        }
                        lastEvent = new float[4];
                        lastEvent[0] = event.getX(0);
                        lastEvent[1] = event.getX(1);
                        lastEvent[2] = event.getY(0);
                        lastEvent[3] = event.getY(1);
                        d = rotation(event);
                        break;

                    case MotionEvent.ACTION_UP: // first finger lifted
                    case MotionEvent.ACTION_POINTER_UP: // second finger lifted
                        mode = NONE;
                        Log.e(TAG, "mode=NONE");
                        break;

                    case MotionEvent.ACTION_MOVE:
                        if (mode == DRAG) {
                            // ...
                            matrix.set(savedMatrix);
                            matrix.postTranslate(event.getX() - start.x,
                                    event.getY() - start.y);
                        } else if (mode == ZOOM && event.getPointerCount() == 2) {
                            float newDist = spacing(event);
                            matrix.set(savedMatrix);
                            if (newDist > 10f) {
                                scale = newDist / oldDist;
                                matrix.postScale(scale, scale, mid.x, mid.y);
                            }
                            if (lastEvent != null) {
                                newRot = rotation(event);
                                float r = newRot - d;
                                matrix.postRotate(r, view.getMeasuredWidth() / 2,
                                        view.getMeasuredHeight() / 2);
                            }
                        }
                        break;

                }
                // Perform the transformation
                view.setImageMatrix(matrix);

                return true; // indicate event was handled

            }

            private float rotation(MotionEvent event) {
                double delta_x = (event.getX(0) - event.getX(1));
                double delta_y = (event.getY(0) - event.getY(1));
                double radians = Math.atan2(delta_y, delta_x);

                return (float) Math.toDegrees(radians);
            }

            private float spacing(MotionEvent event) {
                float x = event.getX(0) - event.getX(1);
                float y = event.getY(0) - event.getY(1);
                return (float) Math.sqrt(x * x + y * y);

            }

            private void midPoint(PointF point, MotionEvent event) {
                float x = event.getX(0) + event.getX(1);
                float y = event.getY(0) + event.getY(1);
                point.set(x / 2, y / 2);

            }


            private void dumpEvent(MotionEvent event) {
                String names[] = {"DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE",
                        "POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?"};
                StringBuilder sb = new StringBuilder();
                int action = event.getAction();
                int actionCode = action & MotionEvent.ACTION_MASK;
                sb.append("event ACTION_").append(names[actionCode]);
                if (actionCode == MotionEvent.ACTION_POINTER_DOWN
                        || actionCode == MotionEvent.ACTION_POINTER_UP) {
                    sb.append("(pid ").append(
                            action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
                    sb.append(")");
                }

                sb.append("[");

                for (int i = 0; i < event.getPointerCount(); i++) {
                    sb.append("#").append(i);
                    sb.append("(pid ").append(event.getPointerId(i));
                    sb.append(")=").append((int) event.getX(i));
                    sb.append(",").append((int) event.getY(i));
                    if (i + 1 < event.getPointerCount())

                        sb.append(";");
                }

                sb.append("]");
                Log.e(TAG, sb.toString());
            }

        });

    }

    public static Bitmap createFlippedBitmap(Bitmap source, boolean xFlip, boolean yFlip) {
        Matrix matrix = new Matrix();
        matrix.postScale(xFlip ? -1 : 1, yFlip ? -1 : 1, source.getWidth() / 2f, source.getHeight() / 2f);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppUtility.REQERASECODE) {
            //Toast.makeText(getApplicationContext(),"gggg",Toast.LENGTH_SHORT).show();
            photoPreview.setImageBitmap(applicationManager.getBitmap());

        } else if (requestCode == AppUtility.REQFRAMECODE && resultCode == RESULT_OK) {

            int pos = data.getIntExtra("position", 0);

            if (pos == 0) {
                background_image_view.setVisibility(View.GONE);
            } else {
                background_image_view.setVisibility(View.VISIBLE);
                Picasso.with(Gallery_Image.this).load(background_array.get(pos)).into(background_image_view);
            }

        } else if (requestCode == RESULT_BACKGROUND_GALLERY_IMAGE && resultCode == RESULT_OK
                && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            final Uri selectedUri = data.getData();
            if (selectedUri != null) {

                CropImage.activity(selectedUri)
                        .start(this);
            } else {
                Toast.makeText(Gallery_Image.this, "Error on selected image", Toast.LENGTH_SHORT).show();
            }


		/*	Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
			background_image_view.setImageBitmap(Bitmap.createScaledBitmap(bitmap,
					width, height, false));*/

        } else if (requestCode == Constant.SELECT_TAMPLET_CODE && resultCode == RESULT_OK) {
            // position = data.getIntExtra("position", 0);
            changeImageInTemplate();


        } else if (requestCode == Constant.TAMPLET_ERASE_CODE && resultCode == RESULT_OK) {
            // position = data.getIntExtra("position", 0);
            changeImageInTemplate();


        }

        if (resultCode == RESULT_OK && requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            Uri resultUri = result.getUri();
            background_image_view.setImageURI(resultUri);

        }

    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm");
        builder.setMessage("Are you sure you want do discard changes?");

        builder.setPositiveButton("DISCARD", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();

    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void getStickerData(StickerData stickerData) {

        if (stickerData.isEdit()) {
            Log.e("Event", "EDIT");

            Drawable drawable = new BitmapDrawable(stickerData.getBitmap());
            DrawableSticker drawableSticker = new DrawableSticker(drawable, true);
            drawableSticker.setStickerData(stickerData);
            stickerView.replace(drawableSticker);

            // stickerView.replace(new DrawableSticker(d,true));
        } else {
            Log.e("Event", "ADD");
            Drawable drawable = new BitmapDrawable(stickerData.getBitmap());
            DrawableSticker drawableSticker = new DrawableSticker(drawable, true);
            drawableSticker.setStickerData(stickerData);
            stickerView.addSticker(drawableSticker);

          /*  Drawable d = new BitmapDrawable(stickerData.getBitmap());
            stickerView.addSticker(new DrawableSticker(d,true));*/
        }
        mEventBus.removeStickyEvent(stickerData);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void getStickerImage(StickerImage stickerImage) {
        Log.e("Event", "ADD");
        Drawable d = new BitmapDrawable(stickerImage.getBitmap());
        stickerView.addSticker(new DrawableSticker(d, false));
        mEventBus.removeStickyEvent(stickerImage);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!mEventBus.isRegistered(this)) {
            mEventBus.register(this);
        }
    }

    @Override
    public void onStop() {
        mEventBus.unregister(this);
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mEventBus.isRegistered(this)) {
            mEventBus.register(this);
        }
    }

    @Override
    protected void onDestroy() {
        if (mEventBus.isRegistered(this)) {
            mEventBus.unregister(this);
        }
        super.onDestroy();
    }
}
