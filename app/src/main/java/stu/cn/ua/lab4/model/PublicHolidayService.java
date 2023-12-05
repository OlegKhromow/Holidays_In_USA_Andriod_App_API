package stu.cn.ua.lab4.model;

import com.annimon.stream.Stream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import retrofit2.Call;
import retrofit2.Response;
import stu.cn.ua.lab4.model.db.HolidayDao;
import stu.cn.ua.lab4.model.db.HolidayDbEntity;
import stu.cn.ua.lab4.model.network.HolidayNetworkEntity;
import stu.cn.ua.lab4.model.network.PublicHolidayApi;

public class PublicHolidayService {
    private PublicHolidayApi publicHolidayApi;
    private HolidayDao holidayDao;
    private ExecutorService executorService;

    public PublicHolidayService(PublicHolidayApi publicHolidayApi, HolidayDao holidayDao, ExecutorService executorService) {
        this.publicHolidayApi = publicHolidayApi;
        this.holidayDao = holidayDao;
        this.executorService = executorService;
    }

    public Cancellable getHolidays(Callback<List<Holiday>> callback){
        Future<?> future = executorService.submit(() -> {
            try {
                List<HolidayDbEntity> entities = holidayDao.getHolidays();
                List<Holiday> holidays = convertToHolidays(entities);
                if (!holidays.isEmpty())
                    callback.onResult(holidays);

                Response<List<HolidayNetworkEntity>> response = publicHolidayApi.getHolidays().execute();
                if (response.isSuccessful()){
                    List<HolidayDbEntity> newDbHolidays = networkToDbEntities(response.body());
                    holidayDao.updateHolidaysForUsername(newDbHolidays);
                    entities = holidayDao.getHolidays();
                    callback.onResult(convertToHolidays(entities));
                } else if(holidays.isEmpty()){
                    callback.onError(new RuntimeException("Something happened"));
                }
            } catch (IOException e) {
                callback.onError(e);
            }
        });
        return () -> future.cancel(true);
    }

    public Cancellable getHolidayById(long id, Callback<Holiday> callback) {
        Future<?> future = executorService.submit(() -> {
            try {
                HolidayDbEntity dbEntity = holidayDao.getById(id);
                Holiday holiday = new Holiday(dbEntity);
                callback.onResult(holiday);
            } catch (Exception e) {
                callback.onError(new RuntimeException(e));
            }
        });
        return () -> future.cancel(true);
    }


        private List<Holiday> convertToHolidays(List<HolidayDbEntity> entities) {
        return Stream.of(entities).map(Holiday::new).toList();
    }

    private List<HolidayDbEntity> networkToDbEntities(List<HolidayNetworkEntity> entities) {
        return Stream.of(entities).map(HolidayDbEntity::new).toList();
    }
}
