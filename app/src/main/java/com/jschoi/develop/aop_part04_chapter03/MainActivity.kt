package com.jschoi.develop.aop_part04_chapter03

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.jschoi.develop.aop_part04_chapter03.databinding.ActivityMainBinding
import com.jschoi.develop.aop_part04_chapter03.model.LocationLatLngEntity
import com.jschoi.develop.aop_part04_chapter03.model.SearchResultEntity
import com.jschoi.develop.aop_part04_chapter03.responce.search.Poi
import com.jschoi.develop.aop_part04_chapter03.responce.search.Pois
import com.jschoi.develop.aop_part04_chapter03.utility.RetrofitClient
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * Simple Google Map App
 */
class MainActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: SearchRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        job = Job()

        initAdapter()
        initViews()
        bindViews()
        initData()
    }

    private fun initAdapter() {
        adapter = SearchRecyclerAdapter()
    }

    private fun initViews() = with(binding) {
        emptyResultTextView.isVisible = false
        recyclerView.adapter = adapter
    }

    private fun bindViews() = with(binding) {
        searchButton.setOnClickListener {
            searchKeyword(searchKeywordEditText.text.toString())
        }
    }

    private fun initData() {
        adapter.notifyDataSetChanged()
    }

    private fun setData(pois: Pois) {
        val dataList = pois.poi.map {
            SearchResultEntity(
                name = it.name ?: "빌딩명 없음",
                fullAddress = makeMainAdress(it),
                locationLatLng = LocationLatLngEntity(
                    it.noorLat,
                    it.noorLon
                )
            )
        }
        adapter.setSearchResultListener(dataList) {
            val intent = Intent(this, MapActivity::class.java)
            intent.putExtra(MapActivity.SEARCH_RESULT_EXTRA_KEY, it)
            startActivity(intent)
        }
    }

    private fun searchKeyword(keyword: String) {
        // Main Thread 시작
        launch(coroutineContext) {
            try {
                withContext(Dispatchers.IO) {
                    val responce = RetrofitClient.apiService.getSearchLocation(
                        keyword = keyword
                    )
                    if (responce.isSuccessful) {
                        val body = responce.body()
                        withContext(Dispatchers.Main) {
                            body?.let {
                                setData(it.searchPoiInfo.pois)
                            }
                        }
                    }
                }
            } catch (e: Exception) {

            }
        }
    }

    private fun makeMainAdress(poi: Poi): String =
        if (poi.secondNo?.trim().isNullOrEmpty()) {
            (poi.upperAddrName?.trim() ?: "") + " " +
                    (poi.middleAddrName?.trim() ?: "") + " " +
                    (poi.lowerAddrName?.trim() ?: "") + " " +
                    (poi.detailAddrName?.trim() ?: "") + " " +
                    poi.firstNo?.trim()
        } else {
            (poi.upperAddrName?.trim() ?: "") + " " +
                    (poi.middleAddrName?.trim() ?: "") + " " +
                    (poi.lowerAddrName?.trim() ?: "") + " " +
                    (poi.detailAddrName?.trim() ?: "") + " " +
                    (poi.firstNo?.trim() ?: "") + " " +
                    poi.secondNo?.trim()
        }
}