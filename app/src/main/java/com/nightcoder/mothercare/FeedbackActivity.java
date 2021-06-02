package com.nightcoder.mothercare;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.nightcoder.mothercare.Adapters.FeedbackAdapter;
import com.nightcoder.mothercare.Models.Feedback;
import com.nightcoder.mothercare.Supports.FeedbackDBHelper;
import com.nightcoder.mothercare.Supports.Prefs;
import com.nightcoder.mothercare.databinding.ActivityFeedbackBinding;
import com.nightcoder.mothercare.databinding.FeedbackDialogBinding;

public class FeedbackActivity extends AppCompatActivity {

    private ActivityFeedbackBinding binding;
    private FeedbackDBHelper dbHelper;
    private String vendor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_feedback);
        init();
        setFeed();
    }

    private void init() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dbHelper = new FeedbackDBHelper(this);
        vendor = getIntent().getStringExtra("vendor");

        if (vendor.equals(Prefs.getString(this, Prefs.KEY_USERNAME, null))) {
            binding.add.setVisibility(View.GONE);
        }

        binding.backButton.setOnClickListener(v -> onBackPressed());
        binding.add.setOnClickListener(v -> openDialog());
    }

    private void setFeed() {
        FeedbackAdapter adapter = new FeedbackAdapter(this, dbHelper.getFeedback(vendor));
        binding.recyclerView.setAdapter(adapter);
        if (adapter.getItemCount() == 0) {
            binding.noContent.setVisibility(View.VISIBLE);
        } else {
            binding.noContent.setVisibility(View.GONE);
        }
    }

    private void openDialog() {
        Dialog dialog = new BottomSheetDialog(this);
        FeedbackDialogBinding dialogBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.feedback_dialog, null, false);
        dialog.setContentView(dialogBinding.getRoot());

        dialogBinding.cancel.setOnClickListener(v -> dialog.cancel());
        dialogBinding.send.setOnClickListener(v -> {
            if (!dialogBinding.feedback.getText().toString().isEmpty()) {
                Feedback feedback = new Feedback();
                feedback.vendor = vendor;
                feedback.user = Prefs.getString(FeedbackActivity.this, Prefs.KEY_USERNAME, null);
                feedback.message = dialogBinding.feedback.getText().toString().trim();
                feedback.timestamp = System.currentTimeMillis();
                if (dbHelper.addFeedback(feedback)) {
                    Toast.makeText(this, "Feedback added !", Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                    setFeed();
                } else {
                    Toast.makeText(this, "Try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }
}