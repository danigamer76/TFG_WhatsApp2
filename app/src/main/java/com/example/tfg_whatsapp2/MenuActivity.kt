package com.example.tfg_whatsapp2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.example.tfg_whatsapp2.databinding.ActivityMenuBinding
import com.example.tfg_whatsapp2.fragmentsMenu.About
import com.example.tfg_whatsapp2.fragmentsMenu.Contacts
import com.example.tfg_whatsapp2.fragmentsMenu.Profile

class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding
    private lateinit var optionName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        if (intent != null) {
            optionName = intent.getStringExtra("OptionName").toString()
            when (optionName) {
                "profile" -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayoutMenu, Profile()).commit()
                    binding.toolbarMenu.title = "Perfil"
                }
                "about" -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayoutMenu, About())
                        .commit()
                    binding.toolbarMenu.title = "Sobre Nosotros"
                }
                "contacts" -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayoutMenu,
                        Contacts()
                    )
                        .commit()
                    binding.toolbarMenu.title = "Contacts"
                }
            }
        }
    }
}