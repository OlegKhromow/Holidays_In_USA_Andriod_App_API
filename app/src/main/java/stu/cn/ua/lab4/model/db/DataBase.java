package stu.cn.ua.lab4.model.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(version = 1, entities = {HolidayDbEntity.class})
public abstract class DataBase extends RoomDatabase {
    public abstract HolidayDao getHolidayDao();
}
