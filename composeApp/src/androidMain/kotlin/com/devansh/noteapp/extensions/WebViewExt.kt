package com.devansh.noteapp.extensions

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.os.Message
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.devansh.noteapp.BuildConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("SetJavaScriptEnabled")
fun WebView.openWebView(
    activity: Activity,
    urlWebView: String,
    addJavaScriptInterFace: (() -> Unit)? = null,
    onPageFinishedCallback: (() -> Unit)? = null,
    onPageErrorCallback: (() -> Unit)? = null,
) {

    var isPageFullyLoaded = false
    this.apply {
        settings.javaScriptEnabled = true
        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.setSupportMultipleWindows(true)
        settings.domStorageEnabled = true
        settings.cacheMode = WebSettings.LOAD_NO_CACHE
        settings.loadWithOverviewMode = true
        settings.allowFileAccess = false
        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.mediaPlaybackRequiresUserGesture = false
        settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL

        webChromeClient = object : WebChromeClient() {
            override fun onCreateWindow(
                view: WebView, isDialog: Boolean,
                isUserGesture: Boolean, resultMsg: Message,
            ): Boolean {
                view.clearHistory()
                view.settings.javaScriptEnabled = true
                view.settings.javaScriptCanOpenWindowsAutomatically = true
                view.settings.setSupportMultipleWindows(true)
                view.settings.domStorageEnabled = true
                view.settings.cacheMode = WebSettings.LOAD_NO_CACHE
                view.settings.loadWithOverviewMode = true
                view.settings.allowFileAccess = false
                view.settings.javaScriptCanOpenWindowsAutomatically = true
                view.settings.mediaPlaybackRequiresUserGesture = false
                view.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                view.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL
                return true
            }
        }

        webViewClient = object : WebViewClient() {

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                isPageFullyLoaded = false

            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                Log.e("HomeScreenActivity", "shouldOverrideUrlLoading: $url")
                if (url.startsWith("${BuildConfig.BASE_URL}payment/confirmation")) {
                    CoroutineScope(Dispatchers.IO).launch {
                        delay(2000)
                        activity.runOnUiThread { activity.finish() }
                    }
                } else {
                    view.loadUrl(url)
                }
                return false
            }

            override fun onPageFinished(view: WebView, url: String) {
                Log.e("HomeScreenActivity", "onPageFinished: " + url)
                if (isPageFullyLoaded) {
                    onPageFinishedCallback?.invoke()
                } else {
                    isPageFullyLoaded = true
                }
            }

            override fun onReceivedError(
                view: WebView,
                request: WebResourceRequest,
                error: WebResourceError,
            ) {
                Log.e("HomeScreenActivity", "onReceivedError: " + view.url)
                super.onReceivedError(view, request, error)
                onPageErrorCallback?.invoke()
            }

            override fun shouldInterceptRequest(
                view: WebView?,
                request: WebResourceRequest?,
            ): WebResourceResponse? {
                return super.shouldInterceptRequest(view, request)
            }
        }
        addJavaScriptInterFace?.invoke()
        val map: Map<String, String> = HashMap()
        loadUrl(urlWebView, map)
        setOnLongClickListener { true }
        isLongClickable = false
    }
}