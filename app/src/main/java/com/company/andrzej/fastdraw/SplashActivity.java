package com.company.andrzej.fastdraw;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.RelativeLayout;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setBackgroundofSplash();
        handlerTimeofSplash();
    }

    private void setBackgroundofSplash(){
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.activity_splash);
        relativeLayout.setBackgroundResource(R.drawable.splash_bg);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
    }

    private void handlerTimeofSplash() {
        int secondsDelayed = 3;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, secondsDelayed * 1000);
    }
}


