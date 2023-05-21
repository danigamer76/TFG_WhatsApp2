package com.example.tfg_whatsapp2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.tfg_whatsapp2.databinding.ActivityMainBinding
import com.example.tfg_whatsapp2.fragmentsMain.Chats
import com.example.tfg_whatsapp2.fragmentsMain.Status
import com.example.tfg_whatsapp2.fragmentsMain.Calls
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appPagerAdapter: AppPagerAdapter
    private lateinit var auth: FirebaseAuth

    private val titulos = arrayListOf("Chats", "Estados", "Llamadas")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        setTittle()
        setBar()
        setListeners()
    }

    private fun setListeners() {
        binding.btContacts.setOnClickListener {
            val intent = Intent(this,MenuActivity::class.java)
            intent.putExtra("OptionName", "friends")
            startActivity(intent)
        }
    }

    //Configuracion de la TabLayout
    private fun setBar() {
        appPagerAdapter = AppPagerAdapter(this)
        binding.viewPager2Main.adapter = appPagerAdapter
        TabLayoutMediator(binding.tabLayoutMain,binding.viewPager2Main){
                tab,position ->
            tab.text = titulos[position]
        }.attach()
    }
    //Configuracion de la ToolBar
    private fun setTittle() {
        binding.toolbarMain.title = "WhatsApp 2"
        setSupportActionBar(binding.toolbarMain)    }

    class AppPagerAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity){
        override fun getItemCount(): Int {
            return 3
        }

        override fun createFragment(position: Int): Fragment {
            return when(position){
                0 -> Chats()
                1 -> Status()
                2 -> Calls()
                else -> Chats()
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){

            R.id.profile -> {
                val intent = Intent(this, MenuActivity::class.java)
                intent.putExtra("OptionName","profile")
                startActivity(intent)
            }

            R.id.about -> {
                val intent = Intent(this, MenuActivity::class.java)
                intent.putExtra("OptionName","about")
                startActivity(intent)
            }

            R.id.logout -> {
                auth.signOut()
                val intent = Intent(this, AuthenticationActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.searchContacts -> {
                val intent = Intent(this, MenuActivity::class.java)
                intent.putExtra("OptionName","search")
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}