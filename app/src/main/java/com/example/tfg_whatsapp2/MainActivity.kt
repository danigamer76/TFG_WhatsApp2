package com.example.tfg_whatsapp2

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.tfg_whatsapp2.databinding.ActivityMainBinding
import com.example.tfg_whatsapp2.fragmentsMain.Calls
import com.example.tfg_whatsapp2.fragmentsMain.Chats
import com.example.tfg_whatsapp2.fragmentsMain.Status
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var viewPager2: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var appPagerAdapter: AppPagerAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var showContacts: FloatingActionButton
    private val titles = arrayListOf("Chats", "Status", "Calls")

    private val TIME_INTERVAL =
        2000 // # milliseconds, desired time passed between two back presses.

    private var mBackPressed: Long = 0




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.toolbarMain)
        tabLayout = findViewById(R.id.tabLayoutMain)
        viewPager2 = findViewById(R.id.viewPager2Main)
        showContacts = findViewById(R.id.btContacts)
        auth = FirebaseAuth.getInstance()
        toolbar.title = "WhatsappClone"
        setSupportActionBar(toolbar)
        appPagerAdapter = AppPagerAdapter(this)
        viewPager2.adapter = appPagerAdapter
        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.text = titles[position]
        }.attach()
        showContacts.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            intent.putExtra("OptionName", "friends")
            startActivity(intent)
        }
    }
    class AppPagerAdapter(fragmentActivity: FragmentActivity) :
        FragmentStateAdapter(fragmentActivity) {
        override fun getItemCount(): Int {
            return 3
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> Chats()
                1 -> Status()
                2 -> Calls()
                else -> Chats()
            }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.profile -> {
                val intent = Intent(this, MenuActivity::class.java)
                intent.putExtra("OptionName", "profile")
                startActivity(intent)
            }
            R.id.about -> {
                val intent = Intent(this, MenuActivity::class.java)
                intent.putExtra("OptionName", "about")
                startActivity(intent)
            }
            R.id.logout -> {
                auth.signOut()
                val intent = Intent(this, AuthenticationActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.searchContacts->{
                val intent = Intent(this, MenuActivity::class.java)
                intent.putExtra("OptionName", "search")
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed()
            finishAffinity()
        } else {
            Toast.makeText(baseContext, "Tap back button in order to exit", Toast.LENGTH_SHORT)
                .show()
        }
        mBackPressed = System.currentTimeMillis()
    }
}