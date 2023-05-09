package com.example.tfg_whatsapp2

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg_whatsapp2.Adapter.Search.SearchAdapter
import com.example.tfg_whatsapp2.databinding.ActivityMenuBinding
import com.example.tfg_whatsapp2.fragmentsMenu.About
import com.example.tfg_whatsapp2.fragmentsMenu.Profile
import com.example.tfg_whatsapp2.modelo.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class MenuActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private lateinit var binding: ActivityMenuBinding
    private lateinit var optionName: String
    private lateinit var queryTerm: String

    private lateinit var searchRecycler: RecyclerView
    private lateinit var searchLayoutManager: RecyclerView.LayoutManager
    private lateinit var searchAdapter: SearchAdapter

    private val register : ListenerRegistration?=null

    private val searchInfo = arrayListOf<UserModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        searchRecycler = binding.recyclerViewSearch
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        if (intent != null) {
            optionName = intent.getStringExtra("OptionName").toString()
            when (optionName) {
                "profile" -> {
                    binding.frameLayoutMenu.visibility = View.VISIBLE
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayoutMenu, Profile()).commit()
                    binding.toolbarMenu.title = "Perfil"
                }
                "about" -> {
                    binding.frameLayoutMenu.visibility = View.VISIBLE
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayoutMenu, About())
                        .commit()
                    binding.toolbarMenu.title = "Sobre Nosotros"
                }
                "contacts" -> {
                    searchLayoutManager = LinearLayoutManager(this)
                    binding.recyclerViewSearch.visibility = View.VISIBLE
                    binding.toolbarMenu.title = "Contactos"
                    setSupportActionBar(binding.toolbarMenu)
                    searchRecycler.addItemDecoration(
                        DividerItemDecoration(
                            searchRecycler.context,
                            (searchLayoutManager as LinearLayoutManager).orientation))
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search, menu)
        val searchView = menu?.findItem(R.id.search)?.actionView as SearchView
        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(this)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            queryTerm = query
            if (queryTerm.isNotEmpty()) {
                searchUsers()
            }
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) {
            queryTerm = newText
            if (queryTerm.isNotEmpty()) {
                searchUsers()
            }
        }
        return true
    }

    private fun searchUsers() {
        //Toast.makeText(this,"Clicked on $queryTerm", Toast.LENGTH_SHORT).show()
        searchInfo.clear()
        FirebaseFirestore.getInstance()
            .collection("users")
            .whereGreaterThanOrEqualTo("userName", queryTerm)
            .whereLessThan("userName", queryTerm + "\uF7FF") // Este es un valor que garantiza que no se incluya la siguiente letra
            .orderBy("userName")
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Log.e("onError", "Some Error Occured")
                } else {
                    if (!snapshot?.isEmpty!!) {
                        val searchList = snapshot.documents
                        for (doc in searchList) {

                            if (FirebaseAuth.getInstance().currentUser!!.uid == doc.id){
                                Log.d("onSuccess", "User Running the app")
                            }else{
                            val contact = UserModel(
                                doc.id,
                                doc.getString("userName").toString(),
                                doc.getString("userEmail").toString(),
                                doc.getString("userStatus").toString(),
                                doc.getString("userProfilePhoto").toString()
                            )
                            searchInfo.add(contact)
                            searchAdapter = SearchAdapter(this, searchInfo)
                            searchRecycler.adapter = searchAdapter
                            searchRecycler.layoutManager = searchLayoutManager
                            }
                        }
                    }
                }

            }
    }

    override fun onDestroy() {
        register?.remove()
        super.onDestroy()
    }
}