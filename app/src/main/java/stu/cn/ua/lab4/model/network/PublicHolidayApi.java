package stu.cn.ua.lab4.model.network;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface PublicHolidayApi {

    @GET("v3/PublicHolidays/2023/US")
    Call<List<HolidayNetworkEntity>> getHolidays();
}
