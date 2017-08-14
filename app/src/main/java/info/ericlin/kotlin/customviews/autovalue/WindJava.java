package info.ericlin.kotlin.customviews.autovalue;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import android.os.Parcelable;

@AutoValue
public abstract class WindJava implements Parcelable {

    public abstract double speed();

    public abstract int deg();

    public static TypeAdapter<WindJava> typeAdapter(Gson gson) {
        return new AutoValue_WindJava.GsonTypeAdapter(gson);
    }
}
