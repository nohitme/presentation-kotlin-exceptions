package info.ericlin.kotlin.customviews

import android.app.Application
import android.content.Context
import android.util.Log
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class MainApplication : Application() {

    private var count by IntPreference(PREFS_COUNT)

    override fun onCreate() {
        super.onCreate()

        count += 1
        Log.i("application", "launch count: $count")
    }

    companion object {

        const val PREFS_COUNT = "count"
    }
}

class IntPreference(val name: String) : ReadWriteProperty<Context, Int> {

    override fun getValue(thisRef: Context, property: KProperty<*>): Int {
        val preferences = thisRef.getSharedPreferences(MainApplication::javaClass.name, Context.MODE_PRIVATE)
        return preferences.getInt(name, 0)
    }

    override fun setValue(thisRef: Context, property: KProperty<*>, value: Int) {
        val preferences = thisRef.getSharedPreferences(MainApplication::javaClass.name, Context.MODE_PRIVATE)
        preferences.edit().putInt(name, value).apply()
    }

}

