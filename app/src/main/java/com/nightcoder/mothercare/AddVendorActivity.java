package com.nightcoder.mothercare;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.nightcoder.mothercare.Models.User;
import com.nightcoder.mothercare.Models.Vendor;
import com.nightcoder.mothercare.Supports.Constants;
import com.nightcoder.mothercare.Supports.RealPathUtil;
import com.nightcoder.mothercare.Supports.UsersDBHelper;
import com.nightcoder.mothercare.Supports.VendorDBHelper;
import com.nightcoder.mothercare.databinding.ActivityAddVendorBinding;

public class AddVendorActivity extends AppCompatActivity {

    private ActivityAddVendorBinding binding;
    private UsersDBHelper dbHelper;
    private VendorDBHelper vendorDBHelper;
    private String imageUri = "null";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_vendor);
        init();
    }

    private void init() {
        binding.backButton.setOnClickListener(v -> onBackPressed());

        binding.addButton.setOnClickListener(v -> validate());

        binding.logo.setOnClickListener(v -> getImage());
        dbHelper = new UsersDBHelper(this);
        vendorDBHelper = new VendorDBHelper(this);
    }

    private void validate() {
        if (!binding.title.getText().toString().matches("[a-zA-Z- ]{2,20}")) {
            Toast.makeText(this, "Provide valid title", Toast.LENGTH_SHORT).show();
        } else if (binding.description.getText().toString().isEmpty()) {
            Toast.makeText(this, "Provide short description", Toast.LENGTH_SHORT).show();
        } else if (binding.address.getText().toString().length() < 10) {
            Toast.makeText(this, "Provide valid address", Toast.LENGTH_SHORT).show();
        } else if (binding.number.getText().toString().length() < 10) {
            Toast.makeText(this, "Provide valid phone number", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.email.getText().toString()).matches()) {
            Toast.makeText(this, "Provide valid E-mail address", Toast.LENGTH_SHORT).show();
        } else if (binding.password.getText().toString().length() < 8) {
            Toast.makeText(this, "Password must contains 8 character", Toast.LENGTH_SHORT).show();
        } else if (!binding.password.getText().toString().equals(binding.confirmPass.getText().toString())) {
            Toast.makeText(this, "Password doesn't match", Toast.LENGTH_SHORT).show();
        } else {
            addUser();
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

    private void addUser() {
        if (dbHelper.getUser(binding.email.getText().toString()) == null || vendorDBHelper.getVendor(binding.email.getText().toString()) == null) {
            User user = new User();
            user.userType = Constants.VENDOR;
            user.email = binding.email.getText().toString();
            user.password = binding.password.getText().toString();
            if (dbHelper.insertUser(user))
                addVendor();
            else Toast.makeText(this, "Try Again.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Vendor already exists", Toast.LENGTH_SHORT).show();
        }
    }

    private void addVendor() {
        Vendor vendor = new Vendor();
        vendor.email = binding.email.getText().toString();
        vendor.password = binding.password.getText().toString();
        vendor.title = binding.title.getText().toString();
        vendor.description = binding.description.getText().toString();
        vendor.address = binding.address.getText().toString();
        vendor.number = binding.number.getText().toString();
        vendor.website = binding.website.getText().toString();
        vendor.imageUri = imageUri;
        if (vendorDBHelper.insertVendor(vendor))
            finish();
        else Toast.makeText(this, "Try Again.", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            Glide.with(this).load(uri).into(binding.logo);
            imageUri = RealPathUtil.getRealPath(this, uri);
        } else if (requestCode == 100) {
            selectImage();
        }
    }
}