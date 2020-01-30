package com.prince.testapp.pojo


import com.google.gson.annotations.SerializedName


/**
 * Data Class after Fetch data from Network
 */
data class NetworkDataResponse(
    @SerializedName("rows")
    val rows: List<Row>,
    @SerializedName("title")
    val title: String
)