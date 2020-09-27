package com.github.harmittaa.touchobserver.screens.swipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.github.harmittaa.touchobserver.R
import com.github.harmittaa.touchobserver.databinding.SwipeFragmentBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class SwipeFragment : Fragment() {
    private val viewModel: SwipeViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = SwipeFragmentBinding.inflate(inflater, container, false).apply {
            viewModel = this@SwipeFragment.viewModel
            lifecycleOwner = this@SwipeFragment.viewLifecycleOwner
        }

        binding.swipeLayoutNextDestinationButton.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_swipeFragment_to_gameFragment)
        }

        return binding.root
    }
}