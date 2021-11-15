package com.karry.managermyphone.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentSnapshot
import com.karry.managermyphone.model.Device
import com.karry.managermyphone.repository.FirebaseDatabaseInstance

class DatabaseViewModel : ViewModel() {
    private val instance = FirebaseDatabaseInstance()
    lateinit var devices: LiveData<ArrayList<Device>>
    lateinit var location: LiveData<DocumentSnapshot>

    fun fetchDevices(myDeviceId: String) {
        devices = instance.fetchDevices(myDeviceId)
    }

    fun fetchLocation(deviceId: String) {
        location = instance.fetchLocation(deviceId)
    }
}