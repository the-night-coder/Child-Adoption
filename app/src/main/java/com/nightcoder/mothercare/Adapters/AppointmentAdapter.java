package com.nightcoder.mothercare.Adapters;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.nightcoder.mothercare.Interfaces.OnRefresh;
import com.nightcoder.mothercare.Models.Appointment;
import com.nightcoder.mothercare.R;
import com.nightcoder.mothercare.Supports.AppointmentDBHelper;
import com.nightcoder.mothercare.Supports.Constants;
import com.nightcoder.mothercare.Supports.Tables;
import com.nightcoder.mothercare.Supports.Time;
import com.nightcoder.mothercare.VendorActivity;
import com.nightcoder.mothercare.databinding.ItemAppointmentsBinding;
import com.nightcoder.mothercare.databinding.ManageAppointBinding;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder> {

    private final Cursor cursor;
    private final Context mContext;
    private final OnRefresh onRefresh;

    public AppointmentAdapter(Context context, Cursor cursor) {
        this.cursor = cursor;
        this.mContext = context;
        this.onRefresh = (OnRefresh) mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appointments, parent, false));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Appointment appointment = new Appointment();
        cursor.moveToPosition(position);
        appointment.name = cursor.getString(cursor.getColumnIndex("clientName"));
        appointment.address = cursor.getString(cursor.getColumnIndex("address"));
        appointment.number = cursor.getString(cursor.getColumnIndex("number"));
        appointment.need = cursor.getString(cursor.getColumnIndex("need"));
        appointment.user = cursor.getString(cursor.getColumnIndex("user"));
        appointment.vendor = cursor.getString(cursor.getColumnIndex("vendor"));
        appointment.schedule = cursor.getString(cursor.getColumnIndex("schedule"));
        appointment.photoUrl = cursor.getString(cursor.getColumnIndex("phototUrl"));
        appointment.status = cursor.getInt(cursor.getColumnIndex("status"));
        appointment._id = cursor.getInt(cursor.getColumnIndex("_id"));
        appointment.timestamp = cursor.getLong(cursor.getColumnIndex("timestamp"));
        holder.binding.username.setText(appointment.name);

        holder.binding.address.setText("Address: " + appointment.address);
        holder.binding.email.setText("Email: " + appointment.user);
        holder.binding.number.setText("Phone: " + appointment.number);
        holder.binding.description.setText("Message: " + appointment.need);
        if (appointment.schedule.contains("Reason")) {
            holder.binding.time.setText(appointment.schedule);
        } else if (appointment.schedule.equals("null")) {
            holder.binding.time.setVisibility(View.GONE);
        } else {
            holder.binding.time.setText("Scheduled: " + appointment.schedule);
        }

        Glide.with(mContext).load(appointment.photoUrl).into(holder.binding.image);
        Log.d("PhotoUrl", appointment.photoUrl);

        holder.binding.ago.setText(Time.getTimeFullText(appointment.timestamp));

        if (appointment.status == Constants.STATUS_APPROVE) {
            holder.binding.status.setText(Html.fromHtml("Status: <b> Approved <b>"));
            holder.binding.card.setCardBackgroundColor(mContext.getResources().getColor(R.color.green, mContext.getTheme()));
        } else if (appointment.status == Constants.STATUS_PENDING) {
            holder.binding.status.setText(Html.fromHtml("Status: <b> Pending <b>"));
            holder.binding.card.setCardBackgroundColor(mContext.getResources().getColor(R.color.yellow, mContext.getTheme()));
        } else {
            holder.binding.status.setText(Html.fromHtml("Status: <b> Declined <b>"));
            holder.binding.card.setCardBackgroundColor(mContext.getResources().getColor(R.color.red, mContext.getTheme()));
        }

        if (appointment.need == null) {
            holder.binding.description.setVisibility(View.GONE);
        }

        holder.binding.card.setOnClickListener(v -> {
            if (mContext instanceof VendorActivity) {
                ManageAppointBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.manage_appoint, null, false);
                BottomSheetDialog dialog = new BottomSheetDialog(mContext);
                dialog.setContentView(binding.getRoot());
                dialog.setCanceledOnTouchOutside(true);
                binding.time.setOnClickListener(v1 -> {
                    TimePickerDialog timePickerDialog = new TimePickerDialog(mContext, (view, hourOfDay, minute) -> {
                        String am_pm;
                        if (hourOfDay > 12) {
                            hourOfDay = hourOfDay - 12;
                            am_pm = " PM";
                        } else {
                            am_pm = " AM";
                        }
                        if (hourOfDay == 0) {
                            hourOfDay = 12;
                        }
                        holder.time = hourOfDay + ":" + (minute == 0 ? "00" : minute) + am_pm;
                        binding.time.setText(holder.time);
                    }, 24, 60, false);
                    timePickerDialog.show();
                });
                binding.radioGroup.check(R.id.approve);
                binding.cancelLay.setVisibility(View.GONE);
                binding.schedule.setVisibility(View.VISIBLE);

                binding.date.setOnClickListener(v1 -> {
                    DatePickerDialog dpd = new DatePickerDialog(mContext);
                    dpd.show();

                    dpd.setOnDateSetListener((view, year, month, dayOfMonth) -> {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.YEAR, year);
                        holder.date = new SimpleDateFormat("dd MMM yyyy", Locale.US).format(calendar.getTime());
                        binding.date.setText(holder.date);
                    });
                });

                dialog.show();

                binding.radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                    if (checkedId == R.id.cancel) {
                        binding.cancelLay.setVisibility(View.VISIBLE);
                        binding.schedule.setVisibility(View.GONE);
                    } else {
                        binding.cancelLay.setVisibility(View.GONE);
                        binding.schedule.setVisibility(View.VISIBLE);
                    }
                });

                binding.save.setOnClickListener(v1 -> {
                    AppointmentDBHelper dbHelper = new AppointmentDBHelper(mContext);
                    SQLiteDatabase db = dbHelper.getReadableDatabase();
                    if (binding.radioGroup.getCheckedRadioButtonId() == R.id.approve) {
                        if (holder.time != null && holder.date != null) {
                            String time = holder.date + " at " + holder.time;
                            db.execSQL("UPDATE " + Tables.APPOINTMENT_TABLE + " SET schedule='" + time + "', status=" + Constants.STATUS_APPROVE + " WHERE _id=" + appointment._id);
                            dialog.cancel();
                            onRefresh.onRefresh();
                        } else {
                            Toast.makeText(mContext, "Pick time and date", Toast.LENGTH_SHORT).show();
                        }
                    } else if (binding.radioGroup.getCheckedRadioButtonId() == R.id.cancel) {
                        if (!binding.reason.getText().toString().isEmpty()) {
                            String text = "Reason " + binding.reason.getText().toString();
                            db.execSQL("UPDATE " + Tables.APPOINTMENT_TABLE + " SET status=" + Constants.STATUS_DECLINED + ", schedule='" + text + "' WHERE _id=" + appointment._id);
                            dialog.cancel();
                            onRefresh.onRefresh();
                        } else {
                            Toast.makeText(mContext, "Write a reason", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return cursor == null ? 0 : cursor.getCount();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemAppointmentsBinding binding;
        private String time, date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
