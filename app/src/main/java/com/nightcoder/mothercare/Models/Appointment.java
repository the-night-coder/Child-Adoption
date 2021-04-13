package com.nightcoder.mothercare.Models;

import com.nightcoder.mothercare.Supports.Constants;

public class Appointment {
    public String user;
    public String vendor;
    public String schedule;
    public String address;
    public String number;
    public String need;
    public long timestamp;
    public String photoUrl;
    public String name;
    public int _id;
    public int status = Constants.STATUS_PENDING;
}
