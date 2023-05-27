package com.example.tfg_whatsapp2

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.tfg_whatsapp2.databinding.ActivitySplashBinding

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val splashScreenTime = 4000
    private lateinit var imageGif: ImageView
    private lateinit var topAnim: Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_splash)
        imageGif = findViewById(R.id.splashGif)
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_anim)
        imageGif.animation = topAnim
        Handler(Looper.getMainLooper()).postDelayed(
            {
                val intent = Intent(this@SplashActivity, AuthenticationActivity::class.java)
                startActivity(intent)
                finish()
            }, splashScreenTime.toLong()
        )
    }
}