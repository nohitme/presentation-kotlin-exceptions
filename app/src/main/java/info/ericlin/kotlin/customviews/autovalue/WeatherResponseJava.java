package info.ericlin.kotlin.customviews.autovalue;

import java.util.List;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import android.support.annotation.NonNull;

@AutoValue
public abstract class WeatherResponseJava {

    @NonNull
    public abstract String name();

    @NonNull
    public abstract List<WeatherJava> weather();

    @NonNull
    public abstract List<WindJava> wind();

    public static TypeAdapter<WeatherResponseJava> typeAdapter(Gson gson) {
        return new AutoValue_WeatherResponseJava.GsonTypeAdapter(gson);
    }
}
