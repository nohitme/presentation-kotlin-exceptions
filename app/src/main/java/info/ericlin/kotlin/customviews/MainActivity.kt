package info.ericlin.kotlin.customviews

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import info.ericlin.kotlin.crashes.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
