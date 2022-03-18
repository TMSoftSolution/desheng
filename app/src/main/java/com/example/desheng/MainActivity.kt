package com.example.desheng

import android.app.Dialog
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var dialog: Dialog
    private lateinit var refresh: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webView)
        webView.webViewClient = MyWebViewClient()
        webView.settings.javaScriptEnabled = true

        dialog = Dialog(this, R.style.SplashTheme)

        refresh = findViewById(R.id.refresh)
        refresh.setOnClickListener{
            getUrl()
        }
        getUrl();
    }


    private fun getUrl() {
        showDialog()
        val retrofit = RetrofitClient.getInstance()
        val apiInterface = retrofit.create(ApiInterface::class.java)
        lifecycleScope.launchWhenCreated {
            try {
                val response = apiInterface.getUrl()
                if (response.code() == 200){
                    val url = bin2String(response.body().toString())
                    Log.d("MainActivity - ", url)
                    webView.loadUrl(url)
                } else {
                    Toast.makeText(this@MainActivity, response.message(), Toast.LENGTH_LONG)
                }

            } catch (Ex: Exception){
                Toast.makeText(this@MainActivity, Ex.localizedMessage, Toast.LENGTH_LONG)
                Log.d("MainActivity - ", Ex.localizedMessage)
            }
        }
    }


    fun bin2String(hex: String): String {
        val output = StringBuilder()
        var i = 0
        while (i < hex.length) {
            val str = hex.substring(i, i + 2)
            output.append(str.toInt(16).toChar())
            i += 2
        }
        return output.toString()
    }

    private fun showDialog() {
//        val dialog = Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        if (!dialog.isShowing){
            Log.d("MainActivity - ", "show dialog.")
            dialog.show()
        }

    }

    private fun hideDialog() {
        if (dialog.isShowing){
            dialog.dismiss()
            Log.d("MainActivity - ", "hide dialog.")
        }
    }

    // Overriding WebViewClient functions
    inner class MyWebViewClient : WebViewClient() {

        // Load the URL
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return false
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            Log.d("MainActivity - ", "onPageStarted.")
            showDialog()
        }

        // Dialog will disappear once page is loaded
//        override fun onPageFinished(view: WebView, url: String) {
//            super.onPageFinished(view, url)
//            Log.d("MainActivity - ", "onPageFinished.")
//            hideDialog();
//        }

        // Dialog will disappear once page is loaded
        override fun onPageCommitVisible(view: WebView?, url: String?) {
            super.onPageCommitVisible(view, url)
            hideDialog()
        }
    }

}