package com.prince.testapp

import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.prince.testapp.pojo.NetworkDataResponse
import com.prince.testapp.pojo.Row
import com.prince.testapp.ui.DataAdapter
import com.prince.testapp.ui.MainActivityViewModel
import com.prince.testapp.utils.LiveDataResult
import com.prince.testapp.utils.Utils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val dataAdapter = DataAdapter(arrayListOf())
    private lateinit var mainActivityViewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        adapterInitialize()
        this.mainActivityViewModel = getViewModel()
        this.mainActivityViewModel.networkLiveData.observe(this,dataObserver)
        this.mainActivityViewModel.loadingLiveData.observe(this, this.loadingObserver)
        fetchData()
        swipeRefresh()

    }

    /**
     * Swipe Refresh function
     * Reference :- https://developer.android.com/reference/androidx/swiperefreshlayout/widget/SwipeRefreshLayout.OnRefreshListener
     */

    private fun swipeRefresh(){
        swipeContainer.setOnRefreshListener {
            fetchData()
            swipeContainer.isRefreshing = false

        }
    }

    /**
 * Adapter initialization
 */
    private fun adapterInitialize(){
        rvData.apply {
            layoutManager = StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL)
            adapter = dataAdapter
        }

    }

    private fun fetchData(){
        if (this.isConnected)
            mainActivityViewModel.fetchDataFromNetwork()
        else
            Toast.makeText(this,"Please check network connectivity",Toast.LENGTH_LONG).show()
    }
    private val loadingObserver = Observer<Boolean> { visibile ->
        // Show hide a progress

    }

    private val dataObserver = Observer<LiveDataResult<NetworkDataResponse>> { result ->
        when (result?.status) {
            LiveDataResult.Status.LOADING -> {
                // Loading data
                progressBar.visibility = View.VISIBLE

            }

            LiveDataResult.Status.ERROR -> {
                // Error for http request
                progressBar.visibility = View.GONE
            }

            LiveDataResult.Status.SUCCESS -> {
                // Data from API
                progressBar.visibility = View.GONE
                var netWorkData = result.data as NetworkDataResponse

                dataAdapter.updateData(netWorkData.rows as ArrayList<Row>)


            }
        }
    }

    /**
     * Create View Model refrence
     */

    private fun getViewModel(): MainActivityViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MainActivityViewModel((this@MainActivity.application as TestAppApplication).retrofit) as T
            }
        })[MainActivityViewModel::class.java]
    }

    /**
     * Extension finction to check network connectivity
     */
    public val Context.isConnected: Boolean
        get() {
            return (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
                .activeNetworkInfo?.isConnected == true
        }


}
