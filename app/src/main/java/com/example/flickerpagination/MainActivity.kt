package com.example.flickerpagination

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.flickerpagination.data.FlickerResult
import com.example.flickerpagination.databinding.ActivityMainBinding
import com.example.flickerpagination.network.NetworkManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var adapter: RecyclerAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private var page = 1
    var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        layoutManager = LinearLayoutManager(this)
        binding.swipeRefresh.setOnRefreshListener(this)
        fetchJson(false)
        setupRecyclerView()

        binding.recyclerViewMain.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                val visibleItemCount = layoutManager.childCount
                val pastVisibleItem = layoutManager.findFirstVisibleItemPosition()
                val total = binding.recyclerViewMain.adapter?.itemCount
                if (!isLoading && page >= 1) {
                    if (visibleItemCount + pastVisibleItem >= total!!) {
                        page++
                        fetchJson(false)
                    }
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }

    private fun fetchJson(isOnRefresh: Boolean) {
        isLoading = true
        if (!isOnRefresh) binding.progressBar.visibility = View.VISIBLE
        Handler(mainLooper).postDelayed({
            NetworkManager.service.getImage(
                "1c04e05bce6e626247758d120b372a73",
                "flickr.photos.getPopular",
                "34427466731@N01",
                "url_s",
                "json",
                "1",
                "30",
                page.toString()
            ).enqueue(object : Callback<FlickerResult> {
                override fun onResponse(
                    call: Call<FlickerResult>,
                    response: Response<FlickerResult>
                ) {
                    Log.d("Tag", response.body().toString())

                    val listResponse = response.body()?.photos?.photo
                    if (listResponse != null) {
                        adapter.addList(listResponse)
                    }

                    binding.progressBar.visibility = View.GONE
                    isLoading = false
                    binding.swipeRefresh.isRefreshing = false
                }

                override fun onFailure(call: Call<FlickerResult>, t: Throwable) {
                    Log.d("Tag", t.message.toString())

                    binding.progressBar.visibility = View.GONE
                    isLoading = false
                    binding.swipeRefresh.isRefreshing = false
                }
            })
        }, 2000)
    }

    private fun setupRecyclerView() {
        binding.recyclerViewMain.setHasFixedSize(true)
        binding.recyclerViewMain.layoutManager = layoutManager
        adapter = RecyclerAdapter()
        binding.recyclerViewMain.adapter = adapter
    }

    override fun onRefresh() {
        adapter.clear()
        page = 1
        fetchJson(true)
    }

}