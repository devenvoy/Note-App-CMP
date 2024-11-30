package com.devansh.noteapp.ui.composable.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import com.devansh.noteapp.ui.composable.core.Constants

class HomeScreen : Screen {
    @Composable
    override fun Content() {

        val homeScreenModel = rememberScreenModel { HomeScreenModel(Constants.dbClient) }
        HomeScreenContent(
            homeScreenModel = homeScreenModel
        )
    }
}

@Composable
fun HomeScreenContent(homeScreenModel: HomeScreenModel) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Hello World!!")
    }
}
