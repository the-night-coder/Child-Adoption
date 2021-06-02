package com.nightcoder.mothercare;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.nightcoder.mothercare.Models.User;
import com.nightcoder.mothercare.Supports.Constants;
import com.nightcoder.mothercare.Supports.RealPathUtil;
import com.nightcoder.mothercare.Supports.UsersDBHelper;
import com.nightcoder.mothercare.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private UsersDBHelper dbHelper;
    private String imageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        init();
    }

    private void init() {
        binding.join.setOnClickListener(v -> validate());
        dbHelper = new UsersDBHelper(this);
        binding.image.setOnClickListener(v -> getImage());

        binding.visiblePass.setOnClickListener(v -> {
            if (binding.visiblePass.getTag().equals("GONE")){
                binding.password.setTransformationMethod(null);
                binding.confirmPass.setTransformationMethod(null);
                binding.visiblePass.setTag("VISIBLE");
                binding.visiblePass.setImageResource(R.drawable.ic_baseline_visibility_off_24);
            } else {
                binding.password.setTransformationMethod(new PasswordTransformationMethod());
                binding.confirmPass.setTransformationMethod(new PasswordTransformationMethod());
                binding.visiblePass.setTag("GONE");
                binding.visiblePass.setImageResource(R.drawable.ic_baseline_visibility_24);
            }
        });
    }

    private void validate() {
        if (!binding.firstName.getText().toString().matches("[a-zA-Z-]{2,20}")) {
            Snackbar.make(binding.container, "Provide valid Name", Snackbar.LENGTH_SHORT).show();
        } else if (!binding.lastName.getText().toString().trim().matches("[a-zA-Z-\\.]{1,20}")) {
            Snackbar.make(binding.container, "Provide valid last name", Snackbar.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.email.getText().toString()).matches()) {
            Snackbar.make(binding.container, "Provide valid E-mail address", Snackbar.LENGTH_SHORT).show();
        } else if (binding.password.getText().toString().length() < 8) {
            Snackbar.make(binding.container, "Password must contains least 8 character", Snackbar.LENGTH_SHORT).show();
        } else if (!binding.password.getText().toString().equals(binding.confirmPass.getText().toString())) {
            Snackbar.make(binding.container, "Password not match", Snackbar.LENGTH_SHORT).show();
        } else if (imageUri == null) {
            Snackbar.make(binding.container, "Choose a profile photo", Snackbar.LENGTH_SHORT).show();
        } else {
            join();
        }
    }

    private void join() {
        if (dbHelper.getUser(binding.email.getText().toString()) == null || dbHelper.getUserDetails(binding.email.getText().toString()) == null) { ///check already user exists
            User user = new User();
            user.fullName = binding.firstName.getText().toString() + " " + binding.lastName.getText().toString();
            user.password = binding.password.getText().toString();
            user.email = binding.email.getText().toString();
            user.userType = Constants.USER;
            user.photoUrl = imageUri;
            if (dbHelper.insertUser(user)) {
                createUser(user);
            } else {
                Toast.makeText(this, "Try Again.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Snackbar.make(binding.container, "User already exist!", Snackbar.LENGTH_SHORT).show();
        }
    }

    private void createUser(User user) {
        if (dbHelper.insertUserDetails(user)) {
            Toast.makeText(this, "Registered", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Try Again.", Toast.LENGTH_SHORT).show();
        }
    }

    private void getImage() {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            selectImage();
        } else {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        }
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            Glide.with(this).load(uri).into(binding.image);
            imageUri = RealPathUtil.getRealPath(this, uri);
        } else if (requestCode == 100) {
            selectImage();
        }
    }
}