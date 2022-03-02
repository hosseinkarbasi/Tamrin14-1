package com.example.flickerpagination

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.flickerpagination.databinding.SearchPhotoBinding
import com.example.flickerpagination.network.NetworkManager
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import searchimage.SearchImage

class SearchPhoto : Fragment(R.layout.search_photo) {

    private lateinit var binding: SearchPhotoBinding
    private lateinit var userWordSearch: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DataBindingUtil.bind(view)!!

        val names = arrayOf("Leo Messi", "Xavi Hernandez", "Andres Inesita")
        val adapter: ArrayAdapter<String> =
            ArrayAdapter(requireContext(), android.R.layout.simple_expandable_list_item_1, names)
        binding.userList.adapter = adapter

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.searchView.clearFocus()
                if (names.contains(query)) {
                    adapter.filter.filter(query)
                } else {
                    Toast.makeText(requireContext(), "Item Not Found", Toast.LENGTH_LONG).show()
                }
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                adapter.filter.filter(query)
                return false
            }
        })

        binding.userList.setOnItemClickListener { _, _, p2, _ ->
            userWordSearch = binding.userList.getItemAtPosition(p2).toString()
            fetchJson()
        }
    }

    private fun fetchJson() {
        NetworkManager.service.searchImage(
            "1c04e05bce6e626247758d120b372a73",
            "flickr.photos.search",
            userWordSearch,
            "url_s",
            "json",
            "1"

        ).enqueue(object : Callback<SearchImage> {
            override fun onResponse(call: Call<SearchImage>, response: Response<SearchImage>) {
                Log.d("Tag", response.body().toString())

                val url = response.body()?.photos?.photo?.get(0)?.url_s
                Picasso.with(requireContext())
                    .load(url)
                    .into(binding.searchImageview)
            }
            override fun onFailure(call: Call<SearchImage>, t: Throwable) {
                Log.d("Tag", t.message.toString())
            }
        })
    }
}

