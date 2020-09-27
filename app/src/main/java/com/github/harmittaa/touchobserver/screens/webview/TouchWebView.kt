package com.github.harmittaa.touchobserver.screens.webview

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.webkit.WebView
import timber.log.Timber

class TouchWebView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0
) : WebView(context, attrs, defStyleAttr) {
    var listener: Listener? = null

    override fun dispatchGenericMotionEvent(event: MotionEvent?): Boolean {
        Timber.d("View: dispatchGenericMotionEvent $event")
        return super.dispatchGenericMotionEvent(event)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        val event = ev
        Timber.d("EVENT: $event")
        if (ev != null) {
            listener?.onTouchEvent(ev)
        }
        return true
        //return super.onInterceptTouchEvent(ev)
    }

    interface Listener {
        fun onTouchEvent(ev: MotionEvent)
    }
}