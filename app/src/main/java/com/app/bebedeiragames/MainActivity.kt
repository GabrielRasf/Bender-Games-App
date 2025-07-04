package com.app.bebedeiragames

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.app.bebedeiragames.ui.theme.BebedeiraGamesTheme
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds

@Suppress("DEPRECATION")
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configuração para tela cheia
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )

        // Mantém a tela ligada (opcional)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        // Pré-carrega os anúncios ANTES de setar o conteúdo
        MobileAds.initialize(this) {
            // Carrega os anúncios imediatamente após inicialização
            loadInitialAds()
        }

        setContent {
            BebedeiraGamesTheme {
                AdsAndWebView()
            }
        }
    }

    private fun loadInitialAds() {
        // Pré-carrega os anúncios em background
        val topAdRequest = AdRequest.Builder().build()
        val bottomAdRequest = AdRequest.Builder().build()

        // Pode adicionar aqui qualquer configuração adicional de targeting
    }

    @Composable
    private fun AdsAndWebView() {
        val context = LocalContext.current
        var adLoaded by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Banner Superior (com estado para controle)
            if (adLoaded) {
                AndroidView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    factory = { ctx ->
                        AdView(ctx).apply {
                            setAdSize(AdSize.BANNER)
                            adUnitId = "ca-app-pub-3940256099942544/6300978111" // Teste
                            loadAd(AdRequest.Builder().build())
                        }
                    },
                    update = { adView ->
                        adView.loadAd(AdRequest.Builder().build())
                    }
                )
            }

            // WebView
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .systemBarsPadding()
            ) {
                AndroidView(
                    factory = { ctx ->
                        WebView(ctx).apply {
                            webViewClient = WebViewClient()
                            settings.javaScriptEnabled = true
                            loadUrl("file:///android_asset/index.html")
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Banner Inferior (com estado para controle)
            if (adLoaded) {
                AndroidView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    factory = { ctx ->
                        AdView(ctx).apply {
                            setAdSize(AdSize.BANNER)
                            adUnitId = "ca-app-pub-3940256099942544/6300978111" // Teste
                            loadAd(AdRequest.Builder().build())
                        }
                    },
                    update = { adView ->
                        adView.loadAd(AdRequest.Builder().build())
                    }
                )
            }
        }

        // Dispara o carregamento dos anúncios após a composição inicial
        LaunchedEffect(Unit) {
            adLoaded = true
        }
    }

    override fun onResume() {
        super.onResume()
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )
    }
}