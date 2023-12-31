package com.gones.foodinventorykotlin.ui.pages.manageCategories

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gones.foodinventorykotlin.R
import com.gones.foodinventorykotlin.common.UiText
import com.gones.foodinventorykotlin.domain.usecase.CategoryUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber

class ManageCategoriesViewModel(
    private val categoryUseCase: CategoryUseCase,
) : ViewModel() {
    var state = mutableStateOf(ManageCategoriesState())
        private set

    var eventFlow = MutableSharedFlow<UiEvent>()
        private set

    private var getCategoriesJob: Job? = null

    fun getCategories() {
        Timber.d("DLOG: getCategories")
        getCategoriesJob?.cancel()
        getCategoriesJob = categoryUseCase.getAllCategories().onEach { categories ->
            state.value = state.value.copy(categories = categories, isLoading = false)
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: ManageCategoriesEvent) {
        when (event) {
            is ManageCategoriesEvent.EnteredName -> {
                state.value =
                    state.value.copy(category = state.value.category.copy(name = event.name))
            }

            is ManageCategoriesEvent.SaveCategory -> {
                CoroutineScope(Dispatchers.IO).launch {
                    val result = categoryUseCase.addCategory(state.value.category)

                    if (result.successful) {
                        eventFlow.emit(UiEvent.CategoryCreated)
                    } else {
                        eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = result.errorMessage
                                    ?: UiText.StringResource(R.string.error_on_save)
                            )
                        )
                    }
                }
            }

            is ManageCategoriesEvent.CloseDialog -> {
                state.value = state.value.copy(openDialog = false)
            }

            is ManageCategoriesEvent.OpenDialog -> {
                state.value = state.value.copy(openDialog = true)
            }

            is ManageCategoriesEvent.DeleteCategory -> {
                CoroutineScope(Dispatchers.IO).launch {
                    val result = categoryUseCase.deleteCategory(event.category)

                    if (result.successful) {
                        eventFlow.emit(UiEvent.CategoryCreated)
                    } else {
                        eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = result.errorMessage
                                    ?: UiText.StringResource(R.string.error_on_delete)
                            )
                        )
                    }
                }
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: UiText) : UiEvent()
        object CategoryCreated : UiEvent()
    }
}