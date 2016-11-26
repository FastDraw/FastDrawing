package com.company.andrzej.fastdraw;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.FragmentManager;
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
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_EXTERNAL_STORAGE = 1;
    private static int RESULT_LOAD_IMG = 1;

    @BindView(R.id.main_relative)
    RelativeLayout relativeLayout;


    private ImageButton acceptBtn, clearBtn, backgroundBtn, toogleToolbar, addPhotoBtn;
    private ToggleButton eraserBtn;
    private DrawingView drawingView;
    private BackgroundSelectFragment backgroundSelectFragment;
    private CustomBottomToolbarFragment customBottomToolbarFragment;

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
        setButtonsEnabled(true);
        setRelativeLayoutBackground();
        customBottomToolbarFragment = new CustomBottomToolbarFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.main_relative, customBottomToolbarFragment, "custom")
                .hide(customBottomToolbarFragment)
                .commit();
        backgroundSelectFragment = new BackgroundSelectFragment();
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction()
                .add(R.id.main_relative, backgroundSelectFragment, "background")
                .hide(backgroundSelectFragment)
                .commit();

    }

    public void loadImagefromGallery(View view) {
        //create intent to open image applications
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //start the intent
        startActivityIfNeeded(galleryIntent, RESULT_LOAD_IMG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                Drawable d = new BitmapDrawable(getResources(), imgDecodableString);
                drawingView.setBackground(d);
            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
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

    private void showToolbarFragment() {
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
                if (customBottomToolbarFragment.isHidden()) {
                    showToolbarFragment();
                    setButtonsInvisible();
                } else {
                    hideToolbarFragment();
                    setButtonsVisible();
                }
            }
        });
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawingView.resetCanvas();
            }
        });
        backgroundBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (backgroundSelectFragment.isHidden()) {
                    drawingView.resetCanvas();
                    showFragmentBackground();
                } else {
                    hideFragmentBackground();
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
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
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    private void setButtonsEnabled(boolean ch) {
        acceptBtn.setEnabled(ch);
        clearBtn.setEnabled(ch);
        backgroundBtn.setEnabled(ch);
    }

    public void setButtonsInvisible() {
        acceptBtn.setVisibility(View.INVISIBLE);
        clearBtn.setVisibility(View.INVISIBLE);
        toogleToolbar.setVisibility(View.INVISIBLE);

    }

    public void setButtonsVisible() {
        acceptBtn.setVisibility(View.VISIBLE);
        clearBtn.setVisibility(View.VISIBLE);
        toogleToolbar.setVisibility(View.VISIBLE);
    }

    private void takeAScreenshot() {
        setButtonsEnabled(false);
        setButtonsInvisible();
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
                                // FIXME Why reseting canvas? Now it causes deleting content before picture is saved
                                drawingView.resetCanvas();
                                hideFragmentBackground();
                            }
                        });
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void setRelativeLayoutBackground() {
        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.bg);
        relativeLayout.setBackground(drawable);
    }
}
