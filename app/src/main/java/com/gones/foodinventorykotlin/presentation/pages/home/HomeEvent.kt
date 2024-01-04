package com.gones.foodinventorykotlin.presentation.pages.home

sealed class HomeEvent {
    data class CategorySelected(val categoryId: Int?) : HomeEvent()
}