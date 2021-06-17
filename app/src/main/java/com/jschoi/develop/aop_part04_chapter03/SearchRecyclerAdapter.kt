package com.jschoi.develop.aop_part04_chapter03

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jschoi.develop.aop_part04_chapter03.databinding.ItemSearchResultBinding
import com.jschoi.develop.aop_part04_chapter03.model.SearchResultEntity


class SearchRecyclerAdapter() :
    RecyclerView.Adapter<SearchRecyclerAdapter.SearchResultItemViewHolder>() {

    private lateinit var searchResultClickListener: (SearchResultEntity) -> Unit
    private var searchResultList: List<SearchResultEntity> = listOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchRecyclerAdapter.SearchResultItemViewHolder {
        val view =
            ItemSearchResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchResultItemViewHolder(view, searchResultClickListener)
    }


    override fun onBindViewHolder(
        holder: SearchRecyclerAdapter.SearchResultItemViewHolder,
        position: Int
    ) {
        holder.bind(searchResultList[position])
    }

    override fun getItemCount(): Int {
        return searchResultList.size
    }

    fun setSearchResultListener(
        searchResultList: List<SearchResultEntity>,
        searchResultClickListener: (SearchResultEntity) -> Unit
    ) {
        this.searchResultList = searchResultList
        this.searchResultClickListener = searchResultClickListener
        notifyDataSetChanged()
    }

    inner class SearchResultItemViewHolder(
        val binding: ItemSearchResultBinding,
        val searchResultClickListener: (SearchResultEntity) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: SearchResultEntity) = with(binding) {
            titleTextView.text = data.name
            subTextView.text = data.fullAddress

            root.setOnClickListener {
                searchResultClickListener(data)
            }
        }
    }
}