package com.jschoi.develop.aop_part04_chapter03.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LocationLatLngEntity(
    val latitude: Float,
    val longitude: Float
) : Parcelable
