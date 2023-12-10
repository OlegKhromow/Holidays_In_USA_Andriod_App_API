package stu.cn.ua.lab4.model.network;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PublicHolidayApi {

    @GET("v3/PublicHolidays/{year}/{countryCode}")
    Call<List<HolidayNetworkEntity>> getHolidays(@Path("year") int year, @Path("countryCode") String countryCode);

    @GET("v3/AvailableCountries")
    Call<List<CountryNetworkEntity>> getCountries();
}
