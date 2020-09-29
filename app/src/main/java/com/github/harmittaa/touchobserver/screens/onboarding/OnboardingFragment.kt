package com.github.harmittaa.touchobserver.screens.onboarding

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.github.harmittaa.touchobserver.MainActivity
import com.github.harmittaa.touchobserver.R
import com.github.harmittaa.touchobserver.databinding.ScreenOnboardingBinding
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val ARG_OBJECT = "object"

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val demoCollectionAdapter = DemoCollectionAdapter(this)
        binding.pager.adapter = demoCollectionAdapter
        TabLayoutMediator(binding.pagerTabs, binding.pager) { tab, position ->
            // Some implementation
        }.attach()
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

    class DemoCollectionAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment {
            // Return a NEW fragment instance in createFragment(int)
            val fragment = if (position == 0) {
                OnboardingFirstScreen()
            } else {
                OnboardingSecondScreen()
            }
            fragment.arguments = Bundle().apply {
                // Our object is just an integer :-P
                putInt(ARG_OBJECT, position + 1)
            }
            return fragment
        }
    }

    class OnboardingFirstScreen : Fragment() {

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            return inflater.inflate(R.layout.fragment_onboarding_first, container, false)
        }
    }

    class OnboardingSecondScreen : Fragment() {

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            return inflater.inflate(R.layout.fragment_onboarding_second, container, false)
        }
    }
}
