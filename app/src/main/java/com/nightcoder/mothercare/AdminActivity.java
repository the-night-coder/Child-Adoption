package com.nightcoder.mothercare;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.nightcoder.mothercare.Adapters.VendorAdapter;
import com.nightcoder.mothercare.Supports.Prefs;
import com.nightcoder.mothercare.Supports.VendorDBHelper;
import com.nightcoder.mothercare.databinding.ActivityAdminBinding;

public class AdminActivity extends AppCompatActivity {

    private ActivityAdminBinding binding;
    private VendorDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_admin);

        init();
    }

    private void init() {
        binding.addButton.setOnClickListener(v -> addVendor());
        binding.refresh.setOnRefreshListener(() -> {
            showVendors();
            binding.refresh.setRefreshing(false);
        });
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setHasFixedSize(true);
        dbHelper = new VendorDBHelper(this);

        binding.logout.setOnClickListener(v -> logOut());
    }

    private void logOut() {
        Prefs.putString(this, Prefs.KEY_USERNAME, null);
        startActivity(new Intent(this, SignActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
    }

    private void addVendor() {
        startActivity(new Intent(this, AddVendorActivity.class));
    }

    private void showVendors() {
        VendorAdapter adapter = new VendorAdapter(this, dbHelper.getVendors());
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showVendors();
    }
}