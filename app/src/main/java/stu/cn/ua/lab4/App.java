package stu.cn.ua.lab4;

import android.app.Application;

import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import stu.cn.ua.lab4.model.PublicHolidayService;
import stu.cn.ua.lab4.model.db.DataBase;
import stu.cn.ua.lab4.model.db.HolidayDao;
import stu.cn.ua.lab4.model.network.PublicHolidayApi;

public class App extends Application {
    private static final String BASE_URL = "https://date.nager.at/api/";
    private ViewModelProvider.Factory viewModelFactory;

    @Override
    public void onCreate() {
        super.onCreate();

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        PublicHolidayApi publicHolidayApi = retrofit.create(PublicHolidayApi.class);

        ExecutorService executorService = Executors.newCachedThreadPool();

        DataBase dataBase = Room.databaseBuilder(this, DataBase.class, "database.db").build();
        HolidayDao holidayDao = dataBase.getHolidayDao();
        PublicHolidayService publicHolidayService = new PublicHolidayService(publicHolidayApi, holidayDao, executorService);
        viewModelFactory = new ViewModelFactory(publicHolidayService);
    }

    public ViewModelProvider.Factory getViewModelFactory(){
        return viewModelFactory;
    }
}
