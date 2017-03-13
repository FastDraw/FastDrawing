package com.company.andrzej.fastdraw;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_EXTERNAL_STORAGE = 1;
    private static final int REQUEST_CODE_CAMERA = 2;
    private static int RESULT_LOAD_IMG = 1;

    @BindView(R.id.main_relative)
    RelativeLayout relativeLayout;

    private ImageButton acceptBtn, clearBtn, backgroundBtn, toogleToolbar, addPhotoBtn;
    private DrawingView drawingView;
    private EditText textField;
    private TextView waterMark;
    private BackgroundSelectFragment backgroundSelectFragment;
    private CustomBottomToolbarFragment customBottomToolbarFragment;
    private SOSFragment sosFragment;


    public static void startMainActivity(Context context) {
        Intent openLogin = new Intent(context, MainActivity.class);
        openLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(openLogin);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        checkStoragePermission();
        initViews();
        setUpListeners();
        setRelativeLayoutBackground();
        waterMarkConfiguration();
    }

    public void loadImagefromGallery(View view) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityIfNeeded(galleryIntent, RESULT_LOAD_IMG);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                Drawable d = new BitmapDrawable(getResources(), imgDecodableString);
                relativeLayout.setBackground(d);
            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void onBackPressed() {
        if (backgroundSelectFragment != null && backgroundSelectFragment.isVisible()) {
            hideFragmentBackground();
        } else if (customBottomToolbarFragment != null && customBottomToolbarFragment.isVisible()) {
            hideToolbarFragment();
            setButtonsVisible();
        } else {
            super.onBackPressed();
        }
    }

    public void changeBackground(int position) {
        if (position == 0) {
            relativeLayout.setBackgroundResource(R.drawable.farm);
        } else if (position == 1) {
            relativeLayout.setBackgroundResource(R.drawable.scroll);
        } else if (position == 2) {
            relativeLayout.setBackgroundResource(R.drawable.desert);
        } else if (position == 3) {
            relativeLayout.setBackgroundResource(R.drawable.bg_blue);
        } else if (position == 4) {
            relativeLayout.setBackgroundResource(R.drawable.city);
        } else if (position == 5) {
            relativeLayout.setBackgroundResource(R.drawable.clipboard);
        } else if (position == 6) {
            relativeLayout.setBackgroundResource(R.drawable.grass);
        } else if (position == 7) {
            relativeLayout.setBackgroundResource(R.drawable.green_leaves);
        } else if (position == 8) {
            relativeLayout.setBackgroundResource(R.drawable.moon_landscape);
        } else if (position == 9) {
            relativeLayout.setBackgroundResource(R.drawable.prismatic_floruis);
        } else if (position == 10) {
            relativeLayout.setBackgroundResource(R.drawable.technologic);
        } else if (position == 11) {
            relativeLayout.setBackgroundResource(R.color.white);
        } else if (position == 12) {
            relativeLayout.setBackgroundResource(R.color.red);
        } else if (position == 13) {
            relativeLayout.setBackgroundResource(R.color.blue);
        } else if (position == 14) {
            relativeLayout.setBackgroundResource(R.color.yellow);
        }
    }

    private void waterMarkConfiguration() {
        waterMark.setText(R.string.fast_drawing);
        waterMark.setVisibility(View.INVISIBLE);
        waterMark.setTextSize(16);
        waterMark.setTextColor(getResources().getColor(R.color.black));
    }

    public void openSOSFragment(){
        sosFragment = new SOSFragment();
        getFragmentManager().beginTransaction()
                .add(R.id.main_relative, sosFragment, "sos")
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .show(sosFragment)
                .commit();
    }

    public void closeSOSFragment(){
        getFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .hide(sosFragment)
                .commit();
    }


    public void showToolbarFragment() {
        getFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .show(customBottomToolbarFragment)
                .commit();
    }

    public void hideToolbarFragment() {
        getFragmentManager().beginTransaction()
                .hide(customBottomToolbarFragment)
                .commit();
    }

    private void initViews() {
        drawingView = (DrawingView) findViewById(R.id.drawing_canvas);
        clearBtn = (ImageButton) findViewById(R.id.btn_clear);
        acceptBtn = (ImageButton) findViewById(R.id.btn_accept);
        relativeLayout = (RelativeLayout) findViewById(R.id.main_relative);
        backgroundBtn = (ImageButton) findViewById(R.id.btn_background);
        toogleToolbar = (ImageButton) findViewById(R.id.btn_toolbar);
        addPhotoBtn = (ImageButton) findViewById(R.id.btn_addphoto);
        textField = (EditText) findViewById(R.id.text_field);
        waterMark = (TextView) findViewById(R.id.water_mark);
    }

    private void setUpListeners() {
        addPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadImagefromGallery(v);
            }
        });
        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        takeAScreenshot();
                        Toast.makeText(getApplicationContext(), "Image saved", LENGTH_SHORT).show();
                    } else {
                        checkStoragePermission();
                    }
                } else {
                    takeAScreenshot();
                }
            }
        });
        toogleToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customBottomToolbarFragment == null) {
                    customBottomToolbarFragment = new CustomBottomToolbarFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .add(R.id.main_relative, customBottomToolbarFragment, "custom")
                            .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                            .show(customBottomToolbarFragment)
                            .commit();
                    setButtonsInvisible();
                } else {
                    if (customBottomToolbarFragment.isHidden()) {
                        showToolbarFragment();
                        setButtonsInvisible();
                    } else {
                        hideToolbarFragment();
                        setButtonsVisible();
                    }
                }
            }
        });
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Ask user if he/she is sure as this cannot be undone
                drawingView.resetCanvas();
            }
        });
        backgroundBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (backgroundSelectFragment == null) {
                    backgroundSelectFragment = new BackgroundSelectFragment();
                    FragmentManager fm = getFragmentManager();
                    fm.beginTransaction()
                            .add(R.id.main_relative, backgroundSelectFragment, "background")
                            .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                            .show(backgroundSelectFragment)
                            .commit();
                } else {
                    if (backgroundSelectFragment.isHidden()) {
                        showFragmentBackground();
                    } else {
                        hideFragmentBackground();
                    }
                }
            }
        });
    }

    private void showFragmentBackground() {
        getFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .show(backgroundSelectFragment)
                .commit();
    }

    public void hideFragmentBackground() {
        getFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .hide(backgroundSelectFragment)
                .commit();
    }

    @TargetApi(23)
    public void checkCameraPermission(){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            return;
        }
        if (this.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);
        }
    }

    @TargetApi(23)
    public void checkStoragePermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        if (this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager
                .PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Thanks for your permission", LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "We need your permission to save image",
                            LENGTH_SHORT).show();
                }
                break;
            case REQUEST_CODE_CAMERA:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Thanks for your permission", LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "We need your permission to start SOS",
                            LENGTH_SHORT).show();
                }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    private void setButtonsEnabled(boolean ch) {
        acceptBtn.setEnabled(ch);
        clearBtn.setEnabled(ch);
        backgroundBtn.setEnabled(ch);
        addPhotoBtn.setEnabled(ch);
        toogleToolbar.setEnabled(ch);
    }

    public void setButtonsInvisible() {
        acceptBtn.setVisibility(View.INVISIBLE);
        clearBtn.setVisibility(View.INVISIBLE);
        toogleToolbar.setVisibility(View.INVISIBLE);
        backgroundBtn.setVisibility(View.INVISIBLE);
        addPhotoBtn.setVisibility(View.INVISIBLE);
    }

    public void setButtonsVisible() {
        acceptBtn.setVisibility(View.VISIBLE);
        clearBtn.setVisibility(View.VISIBLE);
        toogleToolbar.setVisibility(View.VISIBLE);
        backgroundBtn.setVisibility(View.VISIBLE);
        addPhotoBtn.setVisibility(View.VISIBLE);
    }

    private void takeAScreenshot() {
        setButtonsEnabled(false);
        setButtonsInvisible();
        waterMark.setVisibility(View.VISIBLE);
        String uniqueID = UUID.randomUUID().toString();
        String mPath = android.os.Environment.getExternalStorageDirectory().toString()
                + "/" + uniqueID + ".jpg";
        File imageFile = new File(mPath);
        try {
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        MediaScannerConnection.scanFile(MainActivity.this,
                new String[]{imageFile.toString()}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(final String path, final Uri uri) {
                        MainActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Intent shareIntent = new Intent();
                                shareIntent.setAction(Intent.ACTION_SEND);
                                shareIntent.putExtra(Intent.EXTRA_STREAM,
                                        uri);
                                shareIntent.setType("image/jpeg");
                                startActivity(Intent.createChooser(shareIntent,
                                        "Choose one"));
                                setButtonsVisible();
                                setButtonsEnabled(true);
                                waterMark.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                });
    }

    private void setRelativeLayoutBackground() {
        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.bg);
        relativeLayout.setBackground(drawable);
    }

    public ImageView getPointer() {
        return (ImageView)findViewById(R.id.pointer);
    }

    public EditText getTextField() {
        return textField;
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//
//        Log.d("TEST", "Key Down  :" + keyCode + " String : " + drawingView.getText());
//        drawingView.setText(drawingView.getText() + (char) event.getUnicodeChar());
//
//        return super.onKeyDown(keyCode, event);
//    }
}
