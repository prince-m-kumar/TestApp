package com.prince.testapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.prince.testapp.pojo.NetworkDataResponse
import com.prince.testapp.remote.APIInterface
import com.prince.testapp.utils.LiveDataResult
import io.reactivex.MaybeObserver
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MainActivityViewModel (private val networkClient:APIInterface):ViewModel(){


    private val _networkLiveData = MutableLiveData<LiveDataResult<NetworkDataResponse>>()
    val networkLiveData: LiveData<LiveDataResult<NetworkDataResponse>>
        get() = _networkLiveData


    private val  _loadingLiveData = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean>
        get() = _loadingLiveData


    fun fetchDataFromNetwork() {
        this.setLoadingVisibility(true)
        this.networkClient.getData().subscribeOn(Schedulers.io()).subscribe(GetDataConsumer())
    }


    fun setLoadingVisibility(visible: Boolean) {
        _loadingLiveData.postValue(visible)
    }


    inner class GetDataConsumer : MaybeObserver<NetworkDataResponse> {
        override fun onSubscribe(d: Disposable) {
            this@MainActivityViewModel._networkLiveData.postValue(LiveDataResult.loading())
        }

        override fun onError(e: Throwable) {
            this@MainActivityViewModel._networkLiveData.postValue(LiveDataResult.error(e))
            this@MainActivityViewModel.setLoadingVisibility(false)
        }

        override fun onSuccess(t: NetworkDataResponse) {
            this@MainActivityViewModel._networkLiveData.postValue(LiveDataResult.succes(t))
            this@MainActivityViewModel.setLoadingVisibility(false)
        }

        override fun onComplete() {
            this@MainActivityViewModel.setLoadingVisibility(false)
        }

    }

}