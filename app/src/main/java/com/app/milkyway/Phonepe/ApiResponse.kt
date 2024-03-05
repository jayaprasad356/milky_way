package com.papayacoders.phonepesdk

import android.provider.ContactsContract


data class ApiResponse(
    val code: String,
    val `data`: ContactsContract.Contacts.Data,
    val message: String,
    val success: Boolean
)