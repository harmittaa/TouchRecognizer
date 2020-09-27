package com.github.harmittaa.touchobserver.screens.onboarding

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.github.harmittaa.touchobserver.MainActivity
import com.github.harmittaa.touchobserver.R
import com.github.harmittaa.touchobserver.databinding.ScreenOnboardingBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class OnboardingFragment : Fragment() {
    private lateinit var binding: ScreenOnboardingBinding
    private val viewModel: OnboardingViewModel by viewModel()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as? MainActivity)?.let {
            it.storeEvents = false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ScreenOnboardingBinding.inflate(inflater, container, false)

        binding.consentButton.setOnClickListener {
            viewModel.onConsentGiven()
        }
        bindViewModel()
        return binding.root
    }

    private fun bindViewModel() {
        viewModel.canSkipLogin.observe(viewLifecycleOwner) { resource ->
            if (resource) {
                navigateOnwards()
            }
        }

        viewModel.initResult.observe(viewLifecycleOwner) { resource ->
            Toast.makeText(requireContext(), "Problems ${resource.reason}", Toast.LENGTH_SHORT)
                .show()
        }

        viewModel.showNextScreen.observe(viewLifecycleOwner) { shouldShow ->
            if (shouldShow) {
                navigateOnwards()
            }
        }

        viewModel.showLoading.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                binding.consentButton.isEnabled = false
                binding.onboardingLoadingIndicator.visibility = View.VISIBLE
            } else {
                binding.onboardingLoadingIndicator.visibility = View.GONE
                binding.consentButton.isEnabled = true
            }
        }
    }

    private fun navigateOnwards() {
        view?.findNavController()
            ?.navigate(OnboardingFragmentDirections.actionOnboardingFragmentToGameFragment())
    }
}