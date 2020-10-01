package com.github.harmittaa.touchobserver.screens.game

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import androidx.fragment.app.Fragment
import com.github.harmittaa.touchobserver.activity.MainActivity
import com.github.harmittaa.touchobserver.databinding.ScreenGameBinding
import com.github.harmittaa.touchobserver.view.TouchWebView
import java.util.Locale

class GameFragment : Fragment(), TouchWebView.Listener {
    private lateinit var binding: ScreenGameBinding
    private val assetsPath = "file:///android_asset/2048/index.html?lang="

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as? MainActivity)?.let {
            it.storeEvents = true
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ScreenGameBinding.inflate(inflater, container, false)
        val settings: WebSettings = binding.gameView.getSettings()
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.databaseEnabled = true

        if (savedInstanceState != null) {
            binding.gameView.restoreState(savedInstanceState)
        } else {
            // Load webview with current Locale language
            binding.gameView.loadUrl(assetsPath + Locale.getDefault().language)
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.gameView.loadUrl(assetsPath + Locale.getDefault().language)
    }

    override fun onTouchEvent(ev: MotionEvent) {
        (activity as MainActivity).onTouchEvent(ev)
    }
}
