package ucoc.weatherapp.Util;

import android.location.Location;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {
    public static final String APP_KEY="0fe55ead167099a22ed5786a85adcdf8";
    public static Location current_location=null;

    public static String convertUnixToDate(long dt){
        Date date = new Date(dt*1000L);
        SimpleDateFormat sdf= new SimpleDateFormat("HH:mm dd EEE mm yyyy");
        String formated=sdf.format(date);

        return formated;
    }

    public static String convertUnixToHour(long dt) {
        Date date = new Date(dt*1000L);
        SimpleDateFormat sdf= new SimpleDateFormat("HH:mm ");
        String formated=sdf.format(date);

        return formated;
    }
}
