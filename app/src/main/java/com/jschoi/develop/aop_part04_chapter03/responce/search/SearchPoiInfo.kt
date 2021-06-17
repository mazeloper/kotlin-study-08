package com.jschoi.develop.aop_part04_chapter03.responce.search

data class SearchPoiInfo(
    val totalCount: String,
    val count: String,
    val page: String,
    val pois: Pois
)