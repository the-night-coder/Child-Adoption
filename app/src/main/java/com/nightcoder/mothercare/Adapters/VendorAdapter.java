package com.nightcoder.mothercare.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.nightcoder.mothercare.AdminActivity;
import com.nightcoder.mothercare.Models.Vendor;
import com.nightcoder.mothercare.OpenVendorActivity;
import com.nightcoder.mothercare.R;
import com.nightcoder.mothercare.VendorViewActivity;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class VendorAdapter extends RecyclerView.Adapter<VendorAdapter.ViewHolder> {

    private final Cursor cursor;
    private final Context mContext;

    public VendorAdapter(Context context, Cursor cursor) {
        this.cursor = cursor;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vendor, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        cursor.moveToPosition(position);
        Vendor vendor = new Vendor();
        vendor.imageUri = cursor.getString(cursor.getColumnIndex("photoUrl"));
        vendor.title = cursor.getString(cursor.getColumnIndex("title"));
        vendor.description = cursor.getString(cursor.getColumnIndex("description"));
        vendor.website = cursor.getString(cursor.getColumnIndex("website"));
        vendor.email = cursor.getString(cursor.getColumnIndex("email"));
        vendor.number = cursor.getString(cursor.getColumnIndex("number"));
        vendor.address = cursor.getString(cursor.getColumnIndex("address"));
        vendor.password = cursor.getString(cursor.getColumnIndex("password"));
        Glide.with(mContext).load(vendor.imageUri).into(holder.imageView);
        Log.d("URL", vendor.imageUri);
        holder.title.setText(vendor.title);
        holder.description.setText(vendor.description);
        holder.website.setText(vendor.website);

        if (vendor.website.equals("null")) {
            holder.website.setText(vendor.number);
        }

        holder.cardView.setOnClickListener(v -> {
            if (mContext instanceof AdminActivity) {
                OpenVendorActivity.vendor = vendor;
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation((Activity) mContext, holder.imageView, "image");
                mContext.startActivity(new Intent(mContext, OpenVendorActivity.class), options.toBundle());
            } else {
                VendorViewActivity.vendor = vendor;
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation((Activity) mContext, holder.imageView, "image");
                mContext.startActivity(new Intent(mContext, VendorViewActivity.class), options.toBundle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return cursor == null ? 0 : cursor.getCount();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView description;
        private final TextView website;
        private final CircleImageView imageView;
        private final MaterialCardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            cardView = itemView.findViewById(R.id.container);
            description = itemView.findViewById(R.id.description);
            website = itemView.findViewById(R.id.website);
            imageView = itemView.findViewById(R.id.image);
        }
    }
}
