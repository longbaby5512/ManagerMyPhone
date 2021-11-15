package com.karry.managermyphone.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Device(
    val deviceId: String,
    val deviceName: String,
    val fcmToken: String,
    val timestamp: Long): Parcelable
