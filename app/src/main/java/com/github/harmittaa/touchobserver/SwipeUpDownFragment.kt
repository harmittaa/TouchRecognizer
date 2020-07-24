package com.github.harmittaa.touchobserver

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import com.github.harmittaa.touchobserver.databinding.FragmentSwipeUpDownBinding

class SwipeUpDownFragment : Fragment() {
    private val viewModel: SwipeViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSwipeUpDownBinding.inflate(inflater, container, false).apply {
            viewModel = this@SwipeUpDownFragment.viewModel
            lifecycleOwner = this@SwipeUpDownFragment.viewLifecycleOwner
        }

        binding.swipeLayoutNextDestinationButton.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_swipeFragment_to_swipeUpDownFragment)
        }

        return binding.root
    }

}