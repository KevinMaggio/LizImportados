package com.refactoringlife.lizimportados.features.login.presenter.screens

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.refactoringlife.lizimportados.R
import com.refactoringlife.lizimportados.features.login.presenter.viewmodel.LoginUiState
import com.refactoringlife.lizimportados.features.login.presenter.viewmodel.LoginViewModel
import com.refactoringlife.lizimportados.ui.theme.ColorWhiteLipsy
import androidx.compose.ui.graphics.Color

@Preview
@Composable
fun LoginScreen(
    onGoogleClick: () -> Unit = {},
    onGoogleSignInClick: (Intent) -> Unit = {},
    viewModel: LoginViewModel
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    
    // Inicializar el AuthManager
    LaunchedEffect(Unit) {
        viewModel.initializeAuthManager(context)
    }
    
    // Manejar estados de la UI
    LaunchedEffect(uiState) {
        when (uiState) {
            is LoginUiState.Success -> {
                onGoogleClick() // Navegar a Home
                viewModel.resetState()
            }
            is LoginUiState.Error -> {
                errorMessage = (uiState as LoginUiState.Error).message
                showErrorDialog = true
                viewModel.resetState()
            }
            else -> {}
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorWhiteLipsy)
    ) {
        Image(
            painter = painterResource(R.drawable.liz_importados),
            contentDescription = "",
            modifier = Modifier
                .size(250.dp)
                .align(Alignment.TopCenter)
        )

        Image(
            painter = painterResource(R.drawable.background_login),
            contentDescription = "",
            modifier = Modifier
                .height(600.dp)
                .align(Alignment.BottomStart)
        )

        Image(
            painter = painterResource(R.drawable.icon_login_butterfly),
            contentDescription = "",
            modifier = Modifier
                .padding(top = 70.dp, end = 40.dp)
                .size(40.dp)
                .align(Alignment.TopEnd)
        )

        Image(
            painter = painterResource(R.drawable.icon_login_google),
            contentDescription = "Iniciar sesión con Google",
            modifier = Modifier
                .padding(bottom = 150.dp)
                .size(120.dp)
                .align(Alignment.BottomCenter)
                .clickable { 
                    val intent = viewModel.signInWithGoogle(context)
                    if (intent != null) {
                        onGoogleSignInClick(intent)
                    }
                }
        )

        LottieAnimationButterfly(modifier = Modifier.align(Alignment.BottomCenter)
            .padding(top = 70.dp, start = 50.dp, bottom = 140.dp))
        
        // Indicador de carga
        if (uiState is LoginUiState.Loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White.copy(alpha = 0.8f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
    
    // Dialog de error
    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text("Error de Autenticación") },
            text = { Text(errorMessage) },
            confirmButton = {
                TextButton(onClick = { showErrorDialog = false }) {
                    Text("Aceptar")
                }
            }
        )
    }
}

@Composable
fun LottieAnimationButterfly(modifier: Modifier = Modifier) {
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("lootie_butterfly.json"))
    val progress by animateLottieCompositionAsState(composition,
        iterations = LottieConstants.IterateForever)

    LottieAnimation(
        composition,
        progress,
        modifier = modifier
            .size(60.dp)
    )
}