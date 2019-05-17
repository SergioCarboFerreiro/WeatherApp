package ucoc.weatherapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ucoc.weatherapp.Models.WeatherForecastResult;
import ucoc.weatherapp.R;
import ucoc.weatherapp.Util.Util;

public class WeatherForecastAdapter extends RecyclerView. Adapter<WeatherForecastAdapter.MyViewHolder> {
    Context context;
    WeatherForecastResult weatherForecastResult;


    public WeatherForecastAdapter(Context context, WeatherForecastResult weatherForecastResult) {
        this.context = context;
        this.weatherForecastResult = weatherForecastResult;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_weather_forecast,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //Load image
        Picasso.get().load(new StringBuilder("https://openweathermap.org/img/w/")
                .append(weatherForecastResult.list.get(position).weather.get(0).getIcon())
                .append(".png").toString()).into(holder.img_weather);

        holder.txt_date_time.setText(new StringBuilder(Util.convertUnixToDate(weatherForecastResult
        .list.get(position).dt)));

        holder.txt_description.setText(new StringBuilder(weatherForecastResult.list.get(position)
        .weather.get(0).getDescription()));

        holder.txt_temperature.setText(new StringBuilder(String.valueOf(weatherForecastResult.list.get(position)
        .main.getTemp())).append("ºC"));
    }

    @Override
    public int getItemCount() {
        return weatherForecastResult.list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txt_date_time,txt_description,txt_temperature;
        ImageView img_weather;
        public MyViewHolder(View itemView) {
            super(itemView);
            img_weather = itemView.findViewById(R.id.img_weather_forecast);
            txt_date_time=itemView.findViewById(R.id.txt_date);
            txt_description=itemView.findViewById(R.id.txt_description_forecast);
            txt_temperature=itemView.findViewById(R.id.txt_temperature_forecast);


        }
    }
}
