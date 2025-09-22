package com.devansh.noteapp.feature.notes.presentation.category

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.devansh.noteapp.core.database.repo.CategoryDataSource
import com.devansh.noteapp.core.utils.onFailure
import com.devansh.noteapp.core.utils.onSuccess
import com.devansh.noteapp.data.models.dto.Category
import com.devansh.noteapp.data.repository.AppCacheSetting
import com.devansh.noteapp.data.repository.repo.CategoryService
import com.devansh.noteapp.feature.notes.presentation.add_edit.UiEvent
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
                categoryService.createCategory(category)
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
                categoryService.updateCategory(category)
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
            categoryService.deleteCategory(it.id)
        }
    }

    fun getAllCategories() {
        viewModelScope.launch {
            try {
                isRefreshing.value = true
                val result = categoryService.getCategories()

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