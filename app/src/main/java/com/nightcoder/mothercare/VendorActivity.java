package com.nightcoder.mothercare;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.nightcoder.mothercare.Adapters.AppointmentAdapter;
import com.nightcoder.mothercare.Interfaces.OnRefresh;
import com.nightcoder.mothercare.Models.Vendor;
import com.nightcoder.mothercare.Supports.AppointmentDBHelper;
import com.nightcoder.mothercare.Supports.Prefs;
import com.nightcoder.mothercare.Supports.VendorDBHelper;
import com.nightcoder.mothercare.databinding.ActivityVendorBinding;

import java.io.File;

public class VendorActivity extends AppCompatActivity implements OnRefresh {

    ActivityVendorBinding binding;
    private Vendor vendor;
    private AppointmentDBHelper dbHelper;
    private VendorDBHelper vendorDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_vendor);

        init();
    }

    private void init() {
        vendorDBHelper = new VendorDBHelper(this);
        Log.e("USER", Prefs.getString(this, Prefs.KEY_USERNAME, null));
        vendor = vendorDBHelper.getVendor(Prefs.getString(this, Prefs.KEY_USERNAME, null));
        if (vendor == null)
            finish();
        Glide.with(this).load(vendor.imageUri).into(binding.image);
        binding.address.setText(vendor.address);
        binding.title.setText(vendor.title);
        binding.website.setText(vendor.website);
        binding.description.setText(vendor.description);
        binding.number.setText(vendor.number);
        binding.email.setText(vendor.email);
        if (vendor.website.equals("null"))
            binding.website.setVisibility(View.GONE);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setHasFixedSize(true);
        dbHelper = new AppointmentDBHelper(this);
        binding.logout.setOnClickListener(v -> logOut());
        binding.feedback.setOnClickListener(v -> startActivity(new Intent(VendorActivity.this, FeedbackActivity.class)
                .putExtra("vendor", vendor.email)));
    }

    private void logOut() {
        Prefs.putString(this, Prefs.KEY_USERNAME, null);
        startActivity(new Intent(this, SignActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
    }

    private void setAppointments() {
        AppointmentAdapter adapter = new AppointmentAdapter(this,
                dbHelper.getAppointments(vendor.email));
        binding.recyclerView.setAdapter(adapter);
        if (adapter.getItemCount() <= 0) {
            binding.noApp.setVisibility(View.VISIBLE);
            binding.recyclerView.setVisibility(View.GONE);
        } else {
            binding.recyclerView.setVisibility(View.VISIBLE);
            binding.noApp.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setAppointments();
    }

    @Override
    public void onRefresh() {
        setAppointments();
    }
}