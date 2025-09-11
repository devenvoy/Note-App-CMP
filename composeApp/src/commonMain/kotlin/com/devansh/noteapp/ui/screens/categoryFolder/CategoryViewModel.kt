package com.devansh.noteapp.ui.screens.categoryFolder

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.devansh.noteapp.domain.model.Category
import com.devansh.noteapp.domain.repo.AppCacheSetting
import com.devansh.noteapp.domain.repo.CategoryDataSource
import com.devansh.noteapp.domain.repo.CategoryService
import com.devansh.noteapp.domain.utils.onFailure
import com.devansh.noteapp.domain.utils.onSuccess
import com.devansh.noteapp.ui.screens.add_edit_note.UiEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CategoryViewModel(
    private val pref: AppCacheSetting,
    private val categoryService: CategoryService,
    private val categoryDataSource: CategoryDataSource
) : ViewModel() {

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories = _categories.asStateFlow()

    var isRefreshing = mutableStateOf(false)

    init {
        getAllCategories()
        viewModelScope.launch {
            categoryDataSource.getAllCategories()
                .collect { newList -> _categories.update { newList } }
        }
    }

    fun addCategory(category: Category) {
        viewModelScope.launch {
            try {
                categoryService.createCategory(category, pref.accessToken.toString())
                    .onSuccess { categoryDataSource.insertCategory(it, true) }
                    .onFailure { categoryDataSource.insertCategory(category, false) }
            } catch (e: Exception) {
                UiEvent.ShowSnackbar(message = e.message ?: "Couldn't Save Note")
            }
        }
    }

    fun updateCategory(category: Category) {
        viewModelScope.launch {
            try {
                categoryService.updateCategory(category, pref.accessToken.toString())
                    .onSuccess { categoryDataSource.insertCategory(it, true) }
                    .onFailure { categoryDataSource.insertCategory(category, false) }
            } catch (e: Exception) {
                UiEvent.ShowSnackbar(message = e.message ?: "Couldn't Save Note")
            }
        }
    }

    fun deleteCategory(it: Category) {
        viewModelScope.launch {
            categoryDataSource.deleteCategoryById(it.id)
            categoryService.deleteCategory(it.id, pref.accessToken.toString())
        }
    }

    fun getAllCategories() {
        viewModelScope.launch {
            try {
                isRefreshing.value = true
                val result = categoryService.getCategories(pref.accessToken.toString())

                result.onSuccess { response ->
                    response.forEach { category ->
                        categoryDataSource.insertCategory(category, true)
                    }
                }.onFailure {
                    Logger.e("SyncError", null) { "Failed to sync data" }
                }

            } catch (e: Exception) {
                Logger.e("SyncError", e) { "Unexpected error occurred during sync" }
            } finally {
                isRefreshing.value = false
            }
        }
    }
}