package com.example.tfg_whatsapp2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.tfg_whatsapp2.databinding.ActivityAuthenticationBinding
import com.example.tfg_whatsapp2.fragmentsAuthentication.Login
import com.example.tfg_whatsapp2.fragmentsAuthentication.Register
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth

class AuthenticationActivity : AppCompatActivity(), FirebaseAuth.AuthStateListener {

    private lateinit var binding: ActivityAuthenticationBinding
    private lateinit var viewPagerAdapter: AuthenticationPageAdapter
    private val titulos = arrayListOf("Inicio de Sesion", "Registro")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewPagerAdapter = AuthenticationPageAdapter(this)
        binding.viewPagerAuthentication.adapter = viewPagerAdapter
        TabLayoutMediator(binding.tabLayoutAuthentication,binding.viewPagerAuthentication){tab,position->
            tab.text = titulos[position]
        }.attach()
    }

    class AuthenticationPageAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity){
        override fun getItemCount(): Int {
            return 2
        }

        override fun createFragment(position: Int): Fragment {
            return when(position){
                0 -> Login()
                1 -> Register()
                else -> Login()
            }
        }

    }

    override fun onStart() {
        super.onStart()
        FirebaseAuth.getInstance().addAuthStateListener(this)
        if (FirebaseAuth.getInstance().currentUser!=null){
            startMainActivity()
        }
    }

    override fun onAuthStateChanged(p0: FirebaseAuth) {
        if(p0.currentUser!=null){
            startMainActivity()
        }
    }

    override fun onStop() {
        super.onStop()
        FirebaseAuth.getInstance().removeAuthStateListener(this)
    }

    private fun startMainActivity() {
        val intent = Intent(this@AuthenticationActivity,MainActivity::class.java)
        startActivity(intent)
    }
}