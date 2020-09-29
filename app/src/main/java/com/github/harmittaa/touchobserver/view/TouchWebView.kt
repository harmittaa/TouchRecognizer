package com.github.harmittaa.touchobserver.view

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
        if (ev != null) {
            listener?.onTouchEvent(ev)
        }
        return true
    }

    interface Listener {
        fun onTouchEvent(ev: MotionEvent)
    }
}
