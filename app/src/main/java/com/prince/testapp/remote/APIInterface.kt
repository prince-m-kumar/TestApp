package com.prince.testapp.remote
import com.prince.testapp.pojo.*
import io.reactivex.Maybe
import retrofit2.http.GET
import retrofit2.http.Path

interface APIInterface{


    /**
     * @return it return NetworkResponse.java
     */
    @GET("s/2iodh4vg0eortkl/facts.json")
    fun getData(): Maybe<NetworkDataResponse>
}