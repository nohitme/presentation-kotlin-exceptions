package info.ericlin.kotlin.customviews

import android.arch.lifecycle.*
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import info.ericlin.kotlin.crashes.R
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity(), LifecycleRegistryOwner {

    private val lifecycleRegistry = LifecycleRegistry(this)

    override fun getLifecycle(): LifecycleRegistry = lifecycleRegistry

    private lateinit var toolbar: Toolbar

    // some "delegations"
    private val count by IntPreference(MainApplication.PREFS_COUNT)
    private val viewModel by bindViewModel<MainViewModel>()
    private val city by bindView<TextView>(R.id.weather_city)
    private val main by bindView<TextView>(R.id.weather_main)
    private val wind by bindView<TextView>(R.id.weather_wind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)
        with(toolbar) {
            title = "Kotlin Samples, Launch Count: $count"
        }

        viewModel.also {
            // bind UI here
            it.data.observe(this, Observer {
                bindUi(it!!)
            })
        }

        supportFragmentManager.perform {
            add(MainFragment(), "TAG")
        }
    }

    private fun bindUi(uiData: UiData) {
        when (uiData) {
            UiData.LOADING -> Unit
            UiData.ERROR -> Unit
            is UiData.Result -> {
                city.text = uiData.response.name
                wind.text = uiData.response.wind.speed.toString()
                main.text = uiData.response.weather
                        .sortedBy { it.main }
                        .map { "${it.main} - ${it.description}" }
                        .firstOrNull() ?: "unknown weather"
            }
        }
    }

    companion object {

        @JvmStatic
        fun newIntent(context: Context) = Intent(context, MainActivity::class.java).apply {
            putExtra("name", "extras")
        }
    }
}

class MainFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onGetLayoutInflater(savedInstanceState: Bundle): LayoutInflater {
        return super.onGetLayoutInflater(savedInstanceState)
    }
}

sealed class UiData {

    object LOADING : UiData()
    object ERROR : UiData()
    data class Result(val response: WeatherResponse) : UiData()
}

class MainViewModel : ViewModel() {

    // don't do this, use dependency instead!
    private val okHttpClient: OkHttpClient
    private val gson: Gson = GsonBuilder().create()
    private val service: WeatherService

    private val _data = MutableLiveData<UiData>()
    val data: LiveData<UiData> = _data // expose immutable type!

    init {
        val loggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { Log.d("okHttp", it) })
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
        okHttpClient = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()

        val retrofit = Retrofit.Builder()
                .baseUrl("http://samples.openweathermap.org")
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

        service = retrofit.create(WeatherService::class.java)

        service.get()
                .map { UiData.Result(it) as UiData }
                .toObservable()
                .startWith(UiData.LOADING)
                .onErrorReturnItem(UiData.ERROR)
                .doOnEach { Log.i("service", it.toString()) }
                .runInBackground()
                .subscribe { _data.value = it }
    }


}

fun <T : Any> Observable<T>.runInBackground(): Observable<T> = subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

inline private fun <reified T : ViewModel> AppCompatActivity.bindViewModel(): Lazy<T> {
    return lazy(LazyThreadSafetyMode.NONE) { ViewModelProviders.of(this).get(T::class.java) }
}

inline private fun <reified T : View> AppCompatActivity.bindView(@IdRes id: Int): Lazy<T> {
    return lazy(LazyThreadSafetyMode.NONE) { findViewById<T>(id) }
}

fun FragmentManager.perform(block: FragmentTransaction.() -> Unit) {
    val transaction = beginTransaction()
    block(transaction)
    transaction.commit()
}