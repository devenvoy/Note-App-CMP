package com.devansh.noteapp.ui.screens.categoryFolder

import androidx.lifecycle.ViewModel
import com.devansh.noteapp.domain.entity.CategoryEntity

class CategoryViewModel : ViewModel() {

    val categories = mutableListOf<CategoryEntity>()
}