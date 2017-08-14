package info.ericlin.kotlin.customviews.autovalue;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import android.support.annotation.NonNull;

@AutoValue
public abstract class WeatherJava {

    @NonNull
    public abstract String name();

    @NonNull
    public abstract String description();

    public static TypeAdapter<WeatherJava> typeAdapter(Gson gson) {
        return new AutoValue_WeatherJava.GsonTypeAdapter(gson);
    }
}
