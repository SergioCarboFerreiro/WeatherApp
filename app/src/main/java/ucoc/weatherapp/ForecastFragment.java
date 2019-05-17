package ucoc.weatherapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import ucoc.weatherapp.Adapters.WeatherForecastAdapter;
import ucoc.weatherapp.Models.Weather;
import ucoc.weatherapp.Models.WeatherForecastResult;
import ucoc.weatherapp.Retrofit.IOpenWeatherMap;
import ucoc.weatherapp.Retrofit.RetrofitClient;
import ucoc.weatherapp.Util.Util;


/**
 * A simple {@link Fragment} subclass.
 */
public class ForecastFragment extends Fragment {
    private static final String TAG = "ForecastFragment";
    CompositeDisposable compositeDisposable;
    IOpenWeatherMap mService;

    TextView txt_city_name,txt_geo_coord;
    RecyclerView recycler_forecast;

    static ForecastFragment instance;

    public static ForecastFragment getInstance(){
        if(instance==null){
            instance= new ForecastFragment();

        }return instance;
    }
    public ForecastFragment() {
        compositeDisposable = new CompositeDisposable();
        Retrofit retrofit= RetrofitClient.getInstance();
        mService= retrofit.create(IOpenWeatherMap.class);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View itemView= inflater.inflate(R.layout.fragment_forecast,container,false);

        txt_city_name=itemView.findViewById(R.id.txt_city_name_forecast);
        txt_geo_coord=itemView.findViewById(R.id.txt_geo_code_forecast);

        recycler_forecast=itemView.findViewById(R.id.recycler_forecast);
        recycler_forecast.setHasFixedSize(true);
        recycler_forecast.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

        getForecastWeatherInformation();
        return itemView;
    }

    private void getForecastWeatherInformation() {
        compositeDisposable.add(mService.getForecastWeatherbyLatLng(
                String.valueOf(Util.current_location.getLatitude()),
                String.valueOf(Util.current_location.getLongitude()),
                Util.APP_KEY,
                "metric")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherForecastResult>() {
                    @Override
                    public void accept(WeatherForecastResult weatherForecastResult) throws Exception {
                        displayForecastWeather(weatherForecastResult);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "accept: ERROR"+throwable.getMessage());

                    }
                })
        );
    }

    private void displayForecastWeather(WeatherForecastResult weatherForecastResult) {
        txt_city_name.setText(new StringBuilder(weatherForecastResult.city.name));
        txt_geo_coord.setText(new StringBuilder(weatherForecastResult.city.coord.toString()));

        WeatherForecastAdapter adapter = new WeatherForecastAdapter(getContext(),weatherForecastResult);
        recycler_forecast.setAdapter(adapter);

    }

}
