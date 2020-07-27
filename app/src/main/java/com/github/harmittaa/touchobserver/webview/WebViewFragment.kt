package com.github.harmittaa.touchobserver.webview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.github.harmittaa.touchobserver.databinding.FragmentWebviewBinding
import com.github.harmittaa.touchobserver.logic.TouchRegisterViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class WebViewFragment : Fragment(), TouchWebView.Listener {
    private val webViewModel: TouchRegisterViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentWebviewBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = this@WebViewFragment.viewLifecycleOwner
        }

        binding.webview.settings.apply {
            setJavaScriptEnabled(true);
            setAllowContentAccess(true);
            setAppCacheEnabled(true);
            setDomStorageEnabled(true);
            setUseWideViewPort(true);
        }
        binding.webview.apply {
            webViewClient = WebViewClient();
            loadUrl("https://en.wikipedia.org/wiki/Spanish_naming_customs")
        }

        return binding.root
    }

    override fun onTouchEvent(ev: MotionEvent) {
        webViewModel.processEvent(ev)
    }


}