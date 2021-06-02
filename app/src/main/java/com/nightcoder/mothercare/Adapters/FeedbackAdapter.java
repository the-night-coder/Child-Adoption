package com.nightcoder.mothercare.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nightcoder.mothercare.Models.Feedback;
import com.nightcoder.mothercare.Models.User;
import com.nightcoder.mothercare.R;
import com.nightcoder.mothercare.Supports.Time;
import com.nightcoder.mothercare.Supports.UsersDBHelper;
import com.nightcoder.mothercare.databinding.ItemFeedbackBinding;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.ViewHolder> {

    private final Cursor cursor;
    private final Context context;
    private final UsersDBHelper dbHelper;

    public FeedbackAdapter(Context context, Cursor cursor) {
        this.cursor = cursor;
        this.context = context;
        this.dbHelper = new UsersDBHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feedback, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        cursor.moveToPosition(position);
        Feedback feedback = new Feedback();
        feedback.message = cursor.getString(cursor.getColumnIndex("message"));
        feedback.user = cursor.getString(cursor.getColumnIndex("user"));
        feedback.vendor = cursor.getString(cursor.getColumnIndex("vendor"));
        feedback.timestamp = cursor.getLong(cursor.getColumnIndex("timestamp"));
        User user = dbHelper.getUserDetails(feedback.user);
        assert holder.binding != null;
        holder.binding.feedback.setText(feedback.message);
        Glide.with(context).load(user.photoUrl).into(holder.binding.image);
        holder.binding.name.setText(user.fullName);
        holder.binding.time.setText(Time.getTimeFullText(feedback.timestamp));
    }

    @Override
    public int getItemCount() {
        return cursor == null ? 0 : cursor.getCount();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemFeedbackBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
