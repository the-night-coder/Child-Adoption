package com.nightcoder.mothercare;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.PasswordTransformationMethod;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.snackbar.Snackbar;
import com.nightcoder.mothercare.Models.User;
import com.nightcoder.mothercare.Supports.Constants;
import com.nightcoder.mothercare.Supports.DialogSupport;
import com.nightcoder.mothercare.Supports.Prefs;
import com.nightcoder.mothercare.Supports.UsersDBHelper;
import com.nightcoder.mothercare.databinding.ActivitySignBinding;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        binding.visiblePass.setOnClickListener(v -> {
            if (binding.visiblePass.getTag().equals("GONE")){
                binding.password.setTransformationMethod(null);
                binding.visiblePass.setTag("VISIBLE");
                binding.visiblePass.setImageResource(R.drawable.ic_baseline_visibility_off_24);
            } else {
                binding.password.setTransformationMethod(new PasswordTransformationMethod());
                binding.visiblePass.setTag("GONE");
                binding.visiblePass.setImageResource(R.drawable.ic_baseline_visibility_24);
            }
        });
    }

    private void validate() {
        if (!emailValidator(binding.password.getText().toString().trim())) {
            Snackbar.make(binding.container, "Provide valid E-mail address", Snackbar.LENGTH_SHORT).show();
        } else if (binding.password.getText().toString().length() > 1) {
            if (binding.email.getText().toString().equals(Constants.ADMIN)) {
                if (binding.password.getText().toString().equals(Constants.ADMIN_PASS)) {
                    Prefs.putString(this, Prefs.KEY_USERNAME, Constants.ADMIN);
                    Dialog dialog = DialogSupport.materialDialog(this, R.layout.loading);
                    new Handler().postDelayed(() -> {
                        dialog.cancel();
                        startActivity(new Intent(this, AdminActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                        finish();
                    }, 3000);
                } else {
                    Snackbar.make(binding.container, "Password incorrect!", Snackbar.LENGTH_SHORT).show();
                }
            } else
                signIn();
        }
    }

    private void signIn() {
        if (dbHelper.getUser(binding.email.getText().toString()) != null) { // To check user exists

            User user = dbHelper.getUser(binding.email.getText().toString());

            if (binding.password.getText().toString().equals(user.password)) {

                Prefs.putString(this, Prefs.KEY_USERNAME, user.email);
                Prefs.putInt(this, Prefs.USER_TYPE, user.userType);
                Dialog dialog = DialogSupport.materialDialog(this, R.layout.loading);

                new Handler().postDelayed(() -> {
                    dialog.cancel();
                    if (user.userType == Constants.USER)
                        startActivity(new Intent(this, UserActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                    else startActivity(new Intent(this, VendorActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                    finish();
                }, 3000);

            } else {
                Snackbar.make(binding.container, "Password incorrect!", Snackbar.LENGTH_SHORT).show();
            }

        } else {
            Snackbar.make(binding.container, "User doesn't exist!", Snackbar.LENGTH_SHORT).show();
        }
    }

    public boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9]+(\\.[_A-Za-z0-9]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
}