package com.gones.foodinventorykotlin.ui.pages.home

sealed class HomeEvent {
    data class CategorySelected(val categoryId: Int?) : HomeEvent()
}