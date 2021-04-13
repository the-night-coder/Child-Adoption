package com.nightcoder.mothercare;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.nightcoder.mothercare.Supports.Constants;
import com.nightcoder.mothercare.Supports.Prefs;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (Prefs.getString(this, Prefs.KEY_USERNAME, null) == null) {
            new Handler().postDelayed(launchSign, 1500);
        } else if (Prefs.getString(this, Prefs.KEY_USERNAME, null).equals(Constants.ADMIN)) {
            new Handler().postDelayed(launchAdmin, 1000);
        } else if (Prefs.getInt(this, Prefs.USER_TYPE, Constants.USER) == Constants.VENDOR) {
            new Handler().postDelayed(launchVendor, 1500);
        } else {
            new Handler().postDelayed(launchMain, 1500);
        }
    }

    private final Runnable launchMain = () -> {
        startActivity(new Intent(SplashActivity.this, UserActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
    };

    private final Runnable launchSign = () -> {
        startActivity(new Intent(SplashActivity.this, SignActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
    };
    private final Runnable launchAdmin = () -> {
        startActivity(new Intent(SplashActivity.this, AdminActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
    };

    private final Runnable launchVendor = () -> {
        startActivity(new Intent(SplashActivity.this, VendorActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
    };
}