package com.example.sqlmanager

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sqlmanager.sql.MyProfile
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {

    private var data: List<MyProfile>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!checkPermissions()) {
            askPermissions()
        }
        else {
            recycler_view.layoutManager = LinearLayoutManager(this)

            progress_bar.visibility = View.VISIBLE

            val dataFromDb = getFromDb()
            if (dataFromDb != null && dataFromDb.isNotEmpty()) {
                recycler_view.adapter = PhoneBookAdapter(dataFromDb)
                progress_bar.visibility = View.INVISIBLE
            }
            else {
                val storage = PhoneBookManager(this)
                storage.getProfiles().observe(this) {
                    data = it
                    recycler_view.adapter = PhoneBookAdapter(it)
                    progress_bar.visibility = View.INVISIBLE
                }
            }
        }

    }

    private fun getFromDb(): List<MyProfile>? {
        val deferred = GlobalScope.async {
            App.database?.getProfileDao()?.getAll()
        }
        return runBlocking { deferred.await() }
    }

    private fun saveToDb(data: List<MyProfile>) {
        GlobalScope.launch {
            App.database?.getProfileDao()?.let { db ->
                data.forEach {
                    db.insert(it)
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        data?.let { saveToDb(it) }
    }

    private fun checkPermissions(): Boolean {
        return PackageManager.PERMISSION_GRANTED ==
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
    }

    private fun askPermissions() {
        //requestCode ??
        requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), 123)
    }
}