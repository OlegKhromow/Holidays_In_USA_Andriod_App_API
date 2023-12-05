package stu.cn.ua.lab4.model.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface HolidayDao {

    @Query("SELECT * FROM holidays")
    List<HolidayDbEntity> getHolidays();

    @Query("SELECT * FROM holidays WHERE id = :id")
    HolidayDbEntity getById(long id);

    @Insert
    void insertHolidays(List<HolidayDbEntity> entities);
    @Query("DELETE FROM holidays")
    void deleteHolidays();

    @Transaction
    default void updateHolidaysForUsername(List<HolidayDbEntity> entities){
        deleteHolidays();
        insertHolidays(entities);
    }
}
