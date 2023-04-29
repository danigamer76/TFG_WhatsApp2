package com.example.tfg_whatsapp2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.example.tfg_whatsapp2.databinding.ActivitySplashBinding
import java.net.Authenticator

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val splashScreenTime = 4000
    private lateinit var topAnim: Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        asignarAnimacion()
    }

    private fun asignarAnimacion() {
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_anim)
        binding.splashGif.animation = topAnim
        Handler(Looper.getMainLooper()).postDelayed(
            {
                val intent = Intent(this@SplashActivity,AuthenticationActivity::class.java)
                startActivity(intent)
                finish()
            },splashScreenTime.toLong()
        )
    }
}