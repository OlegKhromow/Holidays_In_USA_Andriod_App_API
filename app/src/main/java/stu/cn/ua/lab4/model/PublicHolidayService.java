package stu.cn.ua.lab4.model;

import com.annimon.stream.Stream;

import java.io.IOException;
import java.time.Year;
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
import stu.cn.ua.lab4.model.network.CountryNetworkEntity;
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

    public Cancellable getCountry(String countryName, Callback<CountryNetworkEntity> callback){
        Future<?> future = executorService.submit(() -> {
            try {
                Response<List<CountryNetworkEntity>> response = publicHolidayApi.getCountries().execute();
                if (response.isSuccessful()){
                    CountryNetworkEntity country = findCountryCode(response.body(), countryName);
                    if (country != null)
                        callback.onResult(country);
                    else
                        callback.onError(new RuntimeException());
                } else
                    callback.onError(new RuntimeException());
            }catch (IOException e){
                callback.onError(e);
            }
        });
        return () -> future.cancel(true);
    }

    public Cancellable getHolidays(CountryNetworkEntity country, Callback<List<Holiday>> callback){
        Future<?> future = executorService.submit(() -> {
            try {
                List<HolidayDbEntity> entities = holidayDao.getHolidays();
                List<Holiday> holidays = convertToHolidays(entities);
                if (!holidays.isEmpty())
                    callback.onResult(holidays);

                Response<List<HolidayNetworkEntity>> response = publicHolidayApi.getHolidays(Year.now().getValue(), country.getCountryCode()).execute();
                if (response.isSuccessful()){
                    List<HolidayDbEntity> newDbHolidays = networkToDbEntities(response.body(), country.getName());
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

    private List<HolidayDbEntity> networkToDbEntities(List<HolidayNetworkEntity> entities, String country) {
        return Stream.of(entities).map(entity -> new HolidayDbEntity(entity, country)).toList();
    }


    private CountryNetworkEntity findCountryCode(List<CountryNetworkEntity> list, String name){
        if (name.length() < 2)
            return null;
        for (CountryNetworkEntity entity : list) {
            if (name.length() == 2){
                if(name.equalsIgnoreCase(entity.getCountryCode()))
                    return entity;
            } else {

                if (entity.getName().toLowerCase().matches(".*" + name.toLowerCase() + ".*"))
                    return entity;
            }
        }
        return null;
    }
}
