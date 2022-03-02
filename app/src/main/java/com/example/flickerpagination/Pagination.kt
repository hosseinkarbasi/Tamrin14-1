package com.example.flickerpagination

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flickerpagination.data.pagination.pagination.FlickerResult
import com.example.flickerpagination.databinding.PaginationBinding
import com.example.flickerpagination.network.NetworkManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Pagination : Fragment(R.layout.pagination) {

    private lateinit var binding: PaginationBinding
    private lateinit var adapter: RecyclerAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private var page = 1
    var isLoading = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DataBindingUtil.bind(view)!!

        layoutManager = LinearLayoutManager(activity)
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
        Handler(Looper.getMainLooper()).postDelayed({
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
                }

                override fun onFailure(call: Call<FlickerResult>, t: Throwable) {
                    Log.d("Tag", t.message.toString())
                    binding.progressBar.visibility = View.GONE
                    isLoading = false
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

}