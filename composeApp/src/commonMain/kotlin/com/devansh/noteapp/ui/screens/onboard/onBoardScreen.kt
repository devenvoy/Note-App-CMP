package com.devansh.noteapp.ui.screens.onboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.devansh.noteapp.domain.repo.AppCacheSetting
import com.devansh.noteapp.domain.utils.UnitCBF
import com.devansh.noteapp.navigation.NavRoute
import com.devansh.noteapp.ui.components.button.BackButton
import kotlinx.coroutines.launch
import note_app_cmp.composeapp.generated.resources.Res
import note_app_cmp.composeapp.generated.resources.onboard1
import note_app_cmp.composeapp.generated.resources.onboard2
import note_app_cmp.composeapp.generated.resources.onboard3
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject

fun NavGraphBuilder.onBoardScreen(mainNavController: NavHostController) {
    composable<NavRoute.OnBoardScreen> {
        OnBoardScreen(
            navToLogin = {
                mainNavController.navigate(NavRoute.Auth) {
                    popUpTo(NavRoute.OnBoardScreen) { inclusive = true }
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun OnBoardScreen(navToLogin: UnitCBF) {
    val pref = koinInject<AppCacheSetting>()
    val pagerState = rememberPagerState(pageCount = { 3 })
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            when (page) {
                0 -> BaseOnBoardPage(
                    onBack = null,
                    onSkip = {
                        scope.launch {
                            pagerState.animateScrollToPage(2)
                        }
                    },
                    onContinue = {
                        scope.launch {
                            pagerState.animateScrollToPage(1)
                        }
                    },
                    header = "Manage your notes easily",
                    subTitle = "A completely easy way to manage and customize your notes.",
                    image = Res.drawable.onboard1
                )

                1 -> BaseOnBoardPage(
                    onBack = {
                        scope.launch {
                            pagerState.animateScrollToPage(0)
                        }
                    },
                    onSkip = {
                        scope.launch {
                            pagerState.animateScrollToPage(2)
                        }
                    },
                    onContinue = {
                        scope.launch {
                            pagerState.animateScrollToPage(2)
                        }
                    },
                    header = "Organize your thoughts",
                    subTitle = "Most beautiful note taking application.",
                    image = Res.drawable.onboard2
                )

                2 -> BaseOnBoardPage(
                    onBack = {
                        scope.launch {
                            pagerState.animateScrollToPage(1)
                        }
                    },
                    onSkip = null,
                    onContinue = {
                        scope.launch {
                            pref.setOnBoardStatus(flag = true)
                            navToLogin()
                        }
                    },
                    header = "Create cards and easy styling",
                    subTitle = "Making your content legible has never been easier.",
                    image = Res.drawable.onboard3
                )
            }
        }

        // Page indicators

        LinearWavyProgressIndicator(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .widthIn(max = 540.dp, min = Dp.Infinity)
                .align(Alignment.Center)
                .offset(y = 250.dp),
            progress = { ((pagerState.currentPage + 1) / 3f) - 0.05f }
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun BaseOnBoardPage(
    onBack: UnitCBF?,
    onSkip: UnitCBF?,
    onContinue: UnitCBF,
    header: String,
    subTitle: String,
    image: DrawableResource
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top navigation
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back button
            if (onBack != null) {
                BackButton(onClick = onBack, showBackText = true)
            } else {
                Spacer(modifier = Modifier.width(48.dp))
            }

            if (onSkip != null) {
                TextButton(onClick = onSkip) {
                    Text(
                        text = "Skip",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(64.dp))
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        Box(
            modifier = Modifier
                .size(300.dp)
                .background(
                    color = Color(0xFFF0F4FF),
                    shape = RoundedCornerShape(20.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(image),
                contentDescription = null,
                modifier = Modifier.size(200.dp),
                contentScale = ContentScale.Fit
            )
        }

        Spacer(modifier = Modifier.height(60.dp))

        Text(
            text = header,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            lineHeight = 36.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = subTitle,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = Color(0xFF6C7B7F),
            textAlign = TextAlign.Center,
            lineHeight = 24.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onContinue,
            modifier = Modifier
                .widthIn(max = 440.dp, min = Dp.Infinity)
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            shapes = ButtonDefaults.shapes()
        ) {
            Text(
                text = if (onSkip == null) "Get Started" else "Next",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}
