package com.nightcoder.mothercare;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.nightcoder.mothercare.Adapters.VendorAdapter;
import com.nightcoder.mothercare.Models.User;
import com.nightcoder.mothercare.Supports.Prefs;
import com.nightcoder.mothercare.Supports.UsersDBHelper;
import com.nightcoder.mothercare.Supports.VendorDBHelper;
import com.nightcoder.mothercare.databinding.ActivityMainBinding;
import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private VendorDBHelper dbHelper;
    private UsersDBHelper usersDBHelper;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        init();
    }


    private void init() {
        binding.refresh.setOnRefreshListener(() -> {
            showVendors();
            binding.refresh.setRefreshing(false);
        });
        binding.profile.setOnClickListener(v -> openProfile());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setHasFixedSize(true);
        dbHelper = new VendorDBHelper(this);
        usersDBHelper = new UsersDBHelper(this);
        user = usersDBHelper.getUserDetails(Prefs.getString(this, Prefs.KEY_USERNAME, null));
        Glide.with(this).load(user.photoUrl).into(binding.profile);
        showVendors();
    }

    private void openProfile() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.profile_layout);
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        assert window != null;
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.CENTER);
        window.setWindowAnimations(R.style.DialogAnimation);
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.9f;
        window.setAttributes(lp);
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        CircleImageView image = dialog.findViewById(R.id.imageView);
        TextView username = dialog.findViewById(R.id.name);
        TextView email = dialog.findViewById(R.id.email);
        Button logOut = dialog.findViewById(R.id.logOut);
        logOut.setOnClickListener(v -> {
            logOut();
            dialog.cancel();
        });
        username.setText(user.fullName);
        email.setText(user.email);
        Glide.with(this).load(user.photoUrl).into(image);
        dialog.show();
    }

    private void logOut() {
        Prefs.putString(this, Prefs.KEY_USERNAME, null);
        startActivity(new Intent(this, SignActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
    }

    private void showVendors() {
        VendorAdapter adapter = new VendorAdapter(this, dbHelper.getVendors());
        binding.recyclerView.setAdapter(adapter);
    }
}