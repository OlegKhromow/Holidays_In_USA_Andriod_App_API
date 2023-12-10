package stu.cn.ua.lab4.model.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import stu.cn.ua.lab4.model.network.HolidayNetworkEntity;

@Entity(tableName = "holidays")
public class HolidayDbEntity {
    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo
    private String date;
    @ColumnInfo
    private String name;
    @ColumnInfo
    private String localName;
    @ColumnInfo
    private String countryName;
    @ColumnInfo
    private String type;

    public HolidayDbEntity() {
    }

    public HolidayDbEntity(HolidayNetworkEntity holidayNetwork, String countryName) {
        this.date = holidayNetwork.getDate();
        this.name = holidayNetwork.getName();
        this.localName = holidayNetwork.getLocalName();
        this.countryName = countryName;
        this.type = holidayNetwork.getTypes();
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

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocalName() {
        return localName;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
}
