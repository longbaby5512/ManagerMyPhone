package com.karry.managermyphone.repository

import android.provider.Settings
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.karry.managermyphone.DeviceListFragment
import com.karry.managermyphone.adapter.DeviceAdapter
import com.karry.managermyphone.model.Device
import java.util.HashMap

class FirebaseDatabaseInstance {
    private val database = FirebaseFirestore.getInstance()

    fun fetchDevices(myDeviceId: String):  MutableLiveData<ArrayList<Device>> {
        val result = MutableLiveData<ArrayList<Device>>()
        database.collection("Devices").get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful && task.result != null) {
                    val devices = ArrayList<Device>()
                    for (snapshot in task.result!!) {
                        if (myDeviceId == snapshot.getString("Device ID")) {
                            continue
                        }
                        val device = Device(
                            deviceId = snapshot.getString("Device ID")!!,
                            deviceName = snapshot.getString("Device Name")!!,
                            fcmToken = snapshot.getString("FCM Token")!!,
                            timestamp = snapshot.getLong("Last Update")!!
                        )
                        Log.d(TAG, "getDevices: $device")
                        devices.add(device)
                    }
                    result.value = devices
                }
            }
        return result
    }

    fun fetchLocation(deviceId: String): MutableLiveData<DocumentSnapshot> {
        val result =  MutableLiveData<DocumentSnapshot>()
        database.collection("Location").whereEqualTo("Device ID", deviceId).get()
            .addOnCompleteListener {
                if (it.isSuccessful && it.result != null && it.result!!.documents.size > 0) {
//                    for (document in it.result!!.documents) {
//                        Log.d("Location", document.toString())
//                        val latitude = d
//                    }
                    result.value =it.result!!.documents[0]
                }
            }
        return result
    }

    companion object {
        private const val TAG = "FirebaseDatabase"
    }
}