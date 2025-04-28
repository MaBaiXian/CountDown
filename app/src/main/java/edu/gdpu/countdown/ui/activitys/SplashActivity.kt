package edu.gdpu.countdown.ui.activitys

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import edu.gdpu.countdown.R



@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    val sleepTime: Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        loading(sleepTime)
    }

    fun loading(sleepTime: Long) {
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, sleepTime)
    }
}