package com.company.andrzej.fastdraw;

import android.content.SharedPreferences;

/**
 * Created by Andrzej on 2017-01-28.
 */

public class Settings {


    private static final String FIRST_LAUNCH = "first_launch";
    private static final String FIRST_SPLASH_LUNCH = "first_splash_lunch";

    private Settings(){

    }


    private static SharedPreferences getPreferences() {
        return SplashActivity.sharedPreferences;
    }


    public static boolean isFirstLaunch() {
        return getPreferences().getBoolean(FIRST_LAUNCH, true);
    }

    public static void setFirstLaunch(boolean firstLaunch) {
        getPreferences().edit()
                .putBoolean(FIRST_LAUNCH, firstLaunch)
                .apply();
    }

    public static boolean isFirstSplashLunch(){
        return getPreferences().getBoolean(FIRST_SPLASH_LUNCH, true);
    }

    public static void setFirstSplashLaunch(boolean firstLaunch) {
        getPreferences().edit()
                .putBoolean(FIRST_SPLASH_LUNCH, firstLaunch)
                .apply();
    }
}
