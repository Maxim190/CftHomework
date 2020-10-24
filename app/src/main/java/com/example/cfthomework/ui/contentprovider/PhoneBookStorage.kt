package com.example.cfthomework.ui.contentprovider

import android.content.Context
import android.provider.ContactsContract
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*

class PhoneBookStorage(val context: Context) {

    private val profileColumns = arrayOf(
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
            ContactsContract.Contacts.PHOTO_THUMBNAIL_URI
    )

    fun getProfiles(): LiveData<List<MyProfile>> {
        val result = MutableLiveData<List<MyProfile>>()
        GlobalScope.launch {
            val cursor = context.contentResolver?.query(
                    ContactsContract.Contacts.CONTENT_URI,
                    profileColumns,
                    null,
                    null,
                    null
            )
            val profiles: ArrayList<MyProfile> = ArrayList()
            cursor?.use {
                while (it.moveToNext()) {
                    val id = it.getInt(0)
                    val name = it.getString(1)
                    val photoUrl = it.getString(2)

                    var phoneNum: String? = ""
                    val numCursor = context.contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),
                        "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
                        arrayOf("$id"), null
                    )
                    numCursor?.use { it2 ->
                        while(it2.moveToNext()) {
                            phoneNum = it2.getString(0)
                        }
                    }

                    profiles.add(MyProfile(name, phoneNum, photoUrl))
                }
            }
            result.postValue(profiles)
        }
        return result
    }
}