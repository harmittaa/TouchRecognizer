package com.github.harmittaa.touchobserver

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.swipe_fragment.*


class SwipeFragment : Fragment() {

    companion object {
        fun newInstance() = SwipeFragment()
    }

    private lateinit var viewModel: SwipeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.swipe_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SwipeViewModel::class.java)

        historyButton.setOnClickListener {
        }
    }

}