package info.ericlin.kotlin.customviews;

import java.util.List;

import android.support.annotation.NonNull;

import io.reactivex.Observable;

public class RandomJava {

    public static void example(@NonNull List<Weather> weathers) {
        // calling static method from MainActivity
        final io.reactivex.Observable<Integer> observable = MainActivityKt.runInBackground(Observable.just(1));

        // calling static method from "WeatherUtilJava"
        final String displayText = WeatherUtilJava.toDisplayText(weathers);
    }
}
