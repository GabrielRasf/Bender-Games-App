package com.app.bebedeiragames

import android.os.Bundle
import android.util.Log
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
import com.google.android.gms.ads.RequestConfiguration
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import kotlinx.coroutines.*

@Suppress("DEPRECATION")
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Thread {
            try {
                val adInfo = AdvertisingIdClient.getAdvertisingIdInfo(this)
                val deviceId = adInfo.id
                Log.i("DEVICE_ID_LOG", "Device Advertising ID: $deviceId")
            } catch (e: Exception) {
                Log.e("DEVICE_ID_LOG", "Erro ao pegar Device ID: ${e.message}")
            }
        }.start()

        // *** INICIO - BUSCAR ADVERTISING ID e LOGAR no Logcat ***
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val adInfo = AdvertisingIdClient.getAdvertisingIdInfo(applicationContext)
                val advertisingId = adInfo?.id
                Log.i("DEVICE_ID_LOG", "Advertising ID: $advertisingId")
            } catch (e: Exception) {
                Log.e("DEVICE_ID_LOG", "Erro ao pegar Advertising ID", e)
            }
        }
        // *** FIM - BUSCAR ADVERTISING ID ***

        // Configura IDs dos dispositivos de teste - você vai substituir depois pelo ID que aparecer no log
        // val testDeviceIds = listOf("daec9d76-5917-42da-95ca-60a9a4d85b59")
        // val configuration = RequestConfiguration.Builder()
        //    .setTestDeviceIds(testDeviceIds)
        //    .build()
        // MobileAds.setRequestConfiguration(configuration)


        // Inicializa o MobileAds (uma única vez)
        MobileAds.initialize(this) {
            // Após inicialização, pode pré-carregar anúncios ou outras ações
            loadInitialAds()
        }

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

        // Seta o conteúdo da UI com Compose
        setContent {
            BebedeiraGamesTheme {
                AdsAndWebView()
            }
        }
    }

    private fun loadInitialAds() {
        // Pré-carrega os anúncios em background, se quiser
        val topAdRequest = AdRequest.Builder().build()
        val bottomAdRequest = AdRequest.Builder().build()
        // Aqui pode salvar para usar depois, se quiser
    }

    @Composable
    private fun AdsAndWebView() {
        var adLoaded by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Banner Superior
            if (adLoaded) {
                AndroidView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    factory = { ctx ->
                        AdView(ctx).apply {
                            setAdSize(AdSize.BANNER)

                            adUnitId = "ca-app-pub-4356165451655513/3492200019"
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

            // Banner Inferior
            if (adLoaded) {
                AndroidView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    factory = { ctx ->
                        AdView(ctx).apply {
                            setAdSize(AdSize.BANNER)

                            adUnitId = "ca-app-pub-4356165451655513/8174756451"
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
