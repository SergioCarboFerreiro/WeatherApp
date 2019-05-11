package ucoc.weatherapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.internal.service.Common;
import com.squareup.picasso.Picasso;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import ucoc.weatherapp.Models.WeatherResult;
import ucoc.weatherapp.Retrofit.IOpenWeatherMap;
import ucoc.weatherapp.Retrofit.RetrofitClient;
import ucoc.weatherapp.Util.Util;


/**
 * A simple {@link Fragment} subclass.
 */
public class TodayWeatherFragment extends Fragment {
    private static final String TAG ="TodayWeatherFragment";
    
    ImageView img_weather;
    TextView txt_city_name,txt_humidity,txt_sunrise,txt_sunset,txt_pressure,txt_temperature;
    TextView txt_description,txt_date_time,txt_wind,txt_geo_coord;
    LinearLayout weather_panel;
    ProgressBar loading;

    CompositeDisposable compositeDisposable;
    IOpenWeatherMap mService;

    static TodayWeatherFragment instance;

        public static TodayWeatherFragment getInstance(){

            if (instance== null){
                instance=new TodayWeatherFragment();
            }return instance;
    }

    public TodayWeatherFragment() {
        // Required empty public constructor
        compositeDisposable = new CompositeDisposable();
        Retrofit retrofit= RetrofitClient.getInstance();
        mService= retrofit.create(IOpenWeatherMap.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_today_weather, container, false);

        img_weather=itemView.findViewById(R.id.img_weather);
        txt_city_name=itemView.findViewById(R.id.txt_city_name);
        txt_humidity=itemView.findViewById(R.id.txt_humidity);
        txt_sunrise=itemView.findViewById(R.id.txt_sunrise);
        txt_sunset=itemView.findViewById(R.id.txt_sunset);
        txt_pressure=itemView.findViewById(R.id.txt_pressure);
        txt_temperature=itemView.findViewById(R.id.txt_temperature);
        txt_description=itemView.findViewById(R.id.txt_description);
        txt_date_time=itemView.findViewById(R.id.txt_date_time);
        txt_wind=itemView.findViewById(R.id.txt_wind);
        txt_geo_coord=itemView.findViewById(R.id.txt_geo_code);

        weather_panel=itemView.findViewById(R.id.weather_panel);
        loading =itemView.findViewById(R.id.loading);

        getWeatherInformation();

        return itemView;
    }

    private void getWeatherInformation() {
        compositeDisposable.add(mService.getWeatherbyLatLng(String.valueOf(Util.current_location.getLatitude()),
                String.valueOf(Util.current_location.getLongitude()),
                Util.APP_KEY,
                "metric")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherResult>() {
                    @Override
                    public void accept(WeatherResult weatherResult) throws Exception {
                        Log.d(TAG, "accept:Load information");
                        Picasso.get().load(new StringBuilder("https://openweathermap.org/img/w/")
                                .append(weatherResult.getWeather().get(0).getIcon())
                        .append(".png").toString()).into(img_weather);

                        txt_city_name.setText(weatherResult.getName());
                        txt_description.setText(new StringBuilder("Weather in ")
                        .append(weatherResult.getName()).toString());
                        txt_temperature.setText(new StringBuilder(
                                String.valueOf(weatherResult.getMain().getTemp())).append("ºC").toString());
                        txt_date_time.setText(Util.convertUnixToDate(weatherResult.getDt()));
                        txt_pressure.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getPressure())).append("hpa").toString());
                        txt_humidity.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getHumidity())).append(" %").toString());
                        txt_sunrise.setText(Util.convertUnixToHour(weatherResult.getSys().getSunrise()));
                        txt_sunset.setText(Util.convertUnixToHour(weatherResult.getSys().getSunset()));
                        txt_geo_coord.setText(new StringBuilder("[").append(weatherResult.getCoord().toString()).append("]").toString());

                        //Display panel
                        Log.d(TAG, "accept:Display panel");
                        weather_panel.setVisibility(View.VISIBLE);
                        loading.setVisibility(View.GONE);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(getContext(), ""+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }));

    }

}
