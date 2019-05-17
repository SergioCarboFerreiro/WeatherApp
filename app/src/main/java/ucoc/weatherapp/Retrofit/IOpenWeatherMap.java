package ucoc.weatherapp.Retrofit;

import java.util.Observable;

import retrofit2.http.GET;
import retrofit2.http.Query;
import ucoc.weatherapp.Models.WeatherForecastResult;
import ucoc.weatherapp.Models.WeatherResult;

public interface IOpenWeatherMap {
    @GET("weather")
    io.reactivex.Observable<WeatherResult> getWeatherbyLatLng(@Query("lat") String lat,
                                                              @Query("lon") String lng,
                                                              @Query("appid") String appid,
                                                              @Query("units") String unit
    );
    @GET("forecast")
    io.reactivex.Observable<WeatherForecastResult>getForecastWeatherbyLatLng(@Query("lat") String lat,
                                                                             @Query("lon") String lng,
                                                                             @Query("appid") String appid,
                                                                             @Query("units") String unit
    );
}
