package com.prince.testapp.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.prince.testapp.pojo.NetworkDataResponse
import com.prince.testapp.pojo.Row
import com.prince.testapp.remote.APIInterface
import com.prince.testapp.utils.LiveDataResult
import io.reactivex.Maybe
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.ClassRule
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.spy
import org.mockito.MockitoAnnotations
import java.net.SocketException

@RunWith(JUnit4::class)
class MainActivityViewModelTest {

    companion object {
        @ClassRule
        @JvmField
        val schedulers = RxImmediateSchedulerRule()
    }
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    @Mock
    lateinit var networkService: APIInterface

    @Mock
    lateinit var networkDataResponse: NetworkDataResponse


    lateinit var mainActivityViewModel: MainActivityViewModel

    @Before
    fun setUp() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler -> Schedulers.trampoline() }
        MockitoAnnotations.initMocks(this)
        this.mainActivityViewModel = MainActivityViewModel(this.networkService)
    }


    @Test
    fun getNetworkLiveDataWithNoError() {
        Mockito.`when`(this.networkService.getData()).thenAnswer {
            return@thenAnswer Maybe.just(
                if (ArgumentMatchers.any<Class<NetworkDataResponse>>() == null) {
                    networkDataResponse
                }else{
                    ArgumentMatchers.any<Class<NetworkDataResponse>>()
                }
            )
        }

        val observer = Mockito.mock(Observer::class.java) as Observer<LiveDataResult<NetworkDataResponse>>
        this.mainActivityViewModel.networkLiveData.observeForever(observer)

        this.mainActivityViewModel.fetchDataFromNetwork()

        assertNotNull(this.mainActivityViewModel.networkLiveData.value)
        assertEquals(LiveDataResult.Status.SUCCESS, this.mainActivityViewModel.networkLiveData.value?.status)
    }


    @Test
    fun fetchDataWithError() {
        Mockito.`when`(this.networkService.getData()).thenAnswer {
            return@thenAnswer  Maybe.error<SocketException>(SocketException("No network here"))
        }

        val observer = Mockito.mock(Observer::class.java) as Observer<LiveDataResult<NetworkDataResponse>>
        this.mainActivityViewModel.networkLiveData.observeForever(observer)


        this.mainActivityViewModel.fetchDataFromNetwork()

        assertNotNull(this.mainActivityViewModel.networkLiveData.value)
        assertEquals(LiveDataResult.Status.ERROR, this.mainActivityViewModel.networkLiveData.value?.status)
        assert(this.mainActivityViewModel.networkLiveData.value?.err is Throwable)
    }

    @Test
    fun checkLoadingVisibility_OnSucess(){
        Mockito.`when`(this.networkService.getData()).thenAnswer {
            return@thenAnswer Maybe.just(NetworkDataResponse(getListRow(),"Data"))
        }
        val spiedViewModel = spy(this.mainActivityViewModel)

        spiedViewModel.fetchDataFromNetwork()
        Mockito.verify(spiedViewModel, Mockito.times(2)).setLoadingVisibility(ArgumentMatchers.anyBoolean())
    }

    @Test
    fun checkLoadingVisisbility_OnError(){
        Mockito.`when`(this.networkService.getData()).thenAnswer {
            return@thenAnswer Maybe.error<SocketException>(SocketException())
        }

        val spiedViewModel = spy(this.mainActivityViewModel)

        spiedViewModel.fetchDataFromNetwork()
        Mockito.verify(spiedViewModel, Mockito.times(2)).setLoadingVisibility(ArgumentMatchers.anyBoolean())
    }


    fun getListRow():List<Row>{
        var data = ArrayList<Row>()
        return data
    }

}