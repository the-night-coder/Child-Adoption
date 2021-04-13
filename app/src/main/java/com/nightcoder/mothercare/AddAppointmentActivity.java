package com.nightcoder.mothercare;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.nightcoder.mothercare.Models.Appointment;
import com.nightcoder.mothercare.Models.User;
import com.nightcoder.mothercare.Models.Vendor;
import com.nightcoder.mothercare.Supports.AppointmentDBHelper;
import com.nightcoder.mothercare.Supports.Prefs;
import com.nightcoder.mothercare.Supports.UsersDBHelper;
import com.nightcoder.mothercare.databinding.ActivityAddAppointmentBinding;
import com.squareup.picasso.Picasso;

import java.io.File;

public class AddAppointmentActivity extends AppCompatActivity {

    private ActivityAddAppointmentBinding binding;
    private AppointmentDBHelper dbHelper;
    public static Vendor vendor;
    public User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_appointment);
        vendor = VendorViewActivity.vendor;
        Picasso.get().load(new File(vendor.imageUri)).into(binding.image);
        init();
        setViews();
    }

    private void init() {
        binding.create.setOnClickListener(v -> validate());
        UsersDBHelper usersDBHelper = new UsersDBHelper(this);
        user = usersDBHelper.getUserDetails(Prefs.getString(this, Prefs.KEY_USERNAME, null));
        dbHelper = new AppointmentDBHelper(this);
    }

    private void setViews() {
        if (user != null) {
            binding.firstName.setText(user.fullName);
        }
    }

    private void validate() {
        if (binding.firstName.getText().toString().length() < 4) {
            Toast.makeText(this, "Provide valid Name", Toast.LENGTH_SHORT).show();
        } else if (binding.address.getText().toString().length() < 10) {
            Toast.makeText(this, "Provide valid address", Toast.LENGTH_SHORT).show();
        } else if (binding.address.getText().toString().length() < 10) {
            Toast.makeText(this, "Provide valid phone number", Toast.LENGTH_SHORT).show();
        } else {
            create();
        }
    }

    private void create() {
        Appointment appointment = new Appointment();
        appointment.address = binding.address.getText().toString();
        appointment.number = binding.number.getText().toString();
        appointment.user = user.email;
        appointment.photoUrl = user.photoUrl;
        appointment.vendor = vendor.email;
        appointment.timestamp = System.currentTimeMillis();
        appointment.need = binding.need.getText().toString();
        appointment.name = (binding.firstName.getText().toString() + " " + binding.lastName.getText().toString()).trim();
        if (!dbHelper.insertAppointment(appointment))
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
        else finish();
    }
}