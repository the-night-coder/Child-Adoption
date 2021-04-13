package com.nightcoder.mothercare;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.databinding.DataBindingUtil;

import com.nightcoder.mothercare.Models.Vendor;
import com.nightcoder.mothercare.databinding.ActivityOpenVendorBinding;
import com.squareup.picasso.Picasso;

import java.io.File;

public class OpenVendorActivity extends AppCompatActivity {

    private ActivityOpenVendorBinding binding;

    public static Vendor vendor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_open_vendor);
        init();
    }

    private void init() {
        Picasso.get().load(new File(vendor.imageUri)).into(binding.image);
        binding.address.setText(vendor.address);
        binding.title.setText(vendor.title);
        binding.website.setText(vendor.website);
        binding.description.setText(vendor.description);
        binding.number.setText(vendor.number);
        binding.email.setText(vendor.email);
        if (vendor.website.equals("null"))
            binding.website.setVisibility(View.GONE);

        binding.edit.setOnClickListener(v -> edit());
        binding.call.setOnClickListener(v -> call());
        binding.mail.setOnClickListener(v -> email());
        binding.web.setOnClickListener(v -> browse());
    }

    private void call() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + vendor.number));
        startActivity(intent);
    }

    private void email() {
        try {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_EMAIL, vendor.email);
            startActivity(intent);

        } catch (Exception e) {
            Toast.makeText(this, "G-mail not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void browse() {
        if (vendor.website.equals("null")) {
            Toast.makeText(this, "Website not available", Toast.LENGTH_SHORT).show();
        } else {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(vendor.website));
            startActivity(i);
        }
    }

    private void edit() {
        EditVendorActivity.vendor = vendor;
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, binding.image, "image");
        startActivity(new Intent(this, EditVendorActivity.class), options.toBundle());
    }
}