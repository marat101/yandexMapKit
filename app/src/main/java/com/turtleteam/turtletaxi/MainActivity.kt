package com.turtleteam.turtletaxi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yandex.mapkit.MapKitFactory

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        MapKitFactory.setApiKey("99abf47c-61c9-4962-ba47-0f4c83918410")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}