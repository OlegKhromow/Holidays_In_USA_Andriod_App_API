package stu.cn.ua.lab4.model;

import android.os.Parcel;
import android.os.Parcelable;

import stu.cn.ua.lab4.model.db.HolidayDbEntity;
import stu.cn.ua.lab4.model.network.HolidayNetworkEntity;

public class Holiday {
    private long id;
    private String date;
    private String name;
    private String localName;
    private String type;

    public Holiday(long id, String date, String name, String localName, String type) {
        this.id = id;
        this.date = date;
        this.name = name;
        this.localName = localName;
        this.type = type;
    }

    public Holiday(HolidayDbEntity holidayDb) {
        this(holidayDb.getId(), holidayDb.getDate(), holidayDb.getName(), holidayDb.getLocalName(), holidayDb.getType());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public String getLocalName() {
        return localName;
    }

    public String getType() {
        return type;
    }
}
