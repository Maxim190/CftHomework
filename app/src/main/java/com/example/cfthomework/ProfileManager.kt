package com.example.cfthomework

import android.content.Context
import android.provider.ContactsContract

class ProfileManager(private val context: Context) {
    private val profileColumns = arrayOf(
            ContactsContract.Profile._ID,
            ContactsContract.Profile.DISPLAY_NAME_PRIMARY,
            ContactsContract.Profile.CONTACT_STATUS,
            ContactsContract.Profile.PHOTO_THUMBNAIL_URI
    )


}