package com.nightcoder.mothercare;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.snackbar.Snackbar;
import com.nightcoder.mothercare.Models.User;
import com.nightcoder.mothercare.Supports.Constants;
import com.nightcoder.mothercare.Supports.Prefs;
import com.nightcoder.mothercare.Supports.UsersDBHelper;
import com.nightcoder.mothercare.databinding.ActivitySignBinding;

public class SignActivity extends AppCompatActivity {

    private ActivitySignBinding binding;
    private UsersDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign);
        init();
    }

    private void init() {
        binding.register.setOnClickListener(v -> startActivity(new Intent(SignActivity.this, RegisterActivity.class)));
        binding.sign.setOnClickListener(v -> validate());
        dbHelper = new UsersDBHelper(this);
    }

    private void validate() {
        if (!binding.email.getText().toString().contains("@")) {
            Snackbar.make(binding.container, "Provide valid E-mail address", Snackbar.LENGTH_SHORT).show();
        } else if (binding.email.getText().toString().length() > 1) {
            if (binding.email.getText().toString().equals(Constants.ADMIN)) {
                if (binding.password.getText().toString().equals(Constants.ADMIN_PASS)) {
                    Prefs.putString(this, Prefs.KEY_USERNAME, Constants.ADMIN);
                    startActivity(new Intent(this, AdminActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                    finish();
                } else {
                    Snackbar.make(binding.container, "Password incorrect!", Snackbar.LENGTH_SHORT).show();
                }
            } else
                signIn();
        }
    }

    private void signIn() {
        if (dbHelper.getUser(binding.email.getText().toString()) != null) {
            User user = dbHelper.getUser(binding.email.getText().toString());
            if (binding.password.getText().toString().equals(user.password)) {
                Prefs.putString(this, Prefs.KEY_USERNAME, user.email);
                Prefs.putInt(this, Prefs.USER_TYPE, user.userType);
                if (user.userType == Constants.USER)
                    startActivity(new Intent(this, UserActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                else startActivity(new Intent(this, VendorActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            } else {
                Snackbar.make(binding.container, "Password incorrect!", Snackbar.LENGTH_SHORT).show();
            }
        } else {
            Snackbar.make(binding.container, "User doesn't exist!", Snackbar.LENGTH_SHORT).show();
        }
    }
}