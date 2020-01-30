package com.prince.testapp

import android.app.Application
import com.prince.testapp.remote.APIClient
import com.prince.testapp.remote.APIInterface

class TestAppApplication :Application(){
    lateinit var retrofit: APIInterface

    override fun onCreate() {
        super.onCreate()
        retrofit = APIClient.getClient()
    }
}
