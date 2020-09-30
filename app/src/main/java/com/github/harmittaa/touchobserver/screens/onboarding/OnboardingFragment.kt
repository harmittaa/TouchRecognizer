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
import com.github.harmittaa.touchobserver.databinding.FragmentOnboardingFirstBinding
import com.github.harmittaa.touchobserver.databinding.FragmentOnboardingFourthBinding
import com.github.harmittaa.touchobserver.databinding.FragmentOnboardingSecondBinding
import com.github.harmittaa.touchobserver.databinding.FragmentOnboardingThirdBinding
import com.github.harmittaa.touchobserver.databinding.ScreenOnboardingBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class OnboardingFragment : Fragment() {
    private lateinit var binding: ScreenOnboardingBinding
    private val viewModel: OnboardingViewModel by sharedViewModel()

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
        binding.pager.isUserInputEnabled = false
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

        viewModel.onContinueInvoked.observe(viewLifecycleOwner) {
            if (binding.pager.currentItem == 3) {
                viewModel.onConsentGiven()
            } else {
                binding.pager.apply {
                    setCurrentItem(currentItem + 1, true)
                }
            }
        }
    }

    private fun navigateOnwards() {
        view?.findNavController()
            ?.navigate(OnboardingFragmentDirections.actionOnboardingFragmentToGameFragment())
    }

    class DemoCollectionAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

        override fun getItemCount(): Int = 4

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> OnboardingFirstScreen()
                1 -> OnboardingSecondScreen()
                2 -> OnboardingThirdScreen()
                3 -> OnboardingFourthScreen()
                else -> {
                    OnboardingFirstScreen()
                }
            }
        }
    }

    class OnboardingFirstScreen : Fragment() {
        private lateinit var binding: FragmentOnboardingFirstBinding
        private val viewModel: OnboardingViewModel by sharedViewModel()

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            binding = FragmentOnboardingFirstBinding.inflate(inflater, container, false)
            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            binding.continueButton.setOnClickListener {
                if (it.isEnabled) {
                    viewModel.onContinueButtonClicked()
                }
            }
        }
    }

    class OnboardingSecondScreen : Fragment() {
        private lateinit var binding: FragmentOnboardingSecondBinding
        private val viewModel: OnboardingViewModel by sharedViewModel()

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            binding = FragmentOnboardingSecondBinding.inflate(inflater, container, false)
            binding.apply {
                this.lifecycleOwner = this@OnboardingSecondScreen.viewLifecycleOwner
                this.viewModel = this@OnboardingSecondScreen.viewModel
            }
            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            binding.maleButton.setOnClickListener {
                viewModel.maleSelected()
            }
            binding.femaleButton.setOnClickListener {
                viewModel.femaleSelected()
            }
            binding.continueButton.setOnClickListener {
                if (it.isEnabled) {
                    viewModel.onContinueButtonClicked()
                }
            }
        }
    }

    class OnboardingThirdScreen : Fragment() {
        private lateinit var binding: FragmentOnboardingThirdBinding
        private val viewModel: OnboardingViewModel by sharedViewModel()

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            binding = FragmentOnboardingThirdBinding.inflate(inflater, container, false)
            binding.apply {
                this.lifecycleOwner = this@OnboardingThirdScreen.viewLifecycleOwner
                this.viewModel = this@OnboardingThirdScreen.viewModel
            }
            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            binding.leftButton.setOnClickListener {
                viewModel.leftSelected()
            }
            binding.rightButton.setOnClickListener {
                viewModel.rightSelected()
            }
            binding.continueButton.setOnClickListener {
                if (it.isEnabled) {
                    viewModel.onContinueButtonClicked()
                }
            }
        }
    }

    class OnboardingFourthScreen : Fragment() {
        private lateinit var binding: FragmentOnboardingFourthBinding
        private val viewModel: OnboardingViewModel by sharedViewModel()

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            binding = FragmentOnboardingFourthBinding.inflate(inflater, container, false)
            binding.apply {
                this.lifecycleOwner = this@OnboardingFourthScreen.viewLifecycleOwner
                this.viewModel = this@OnboardingFourthScreen.viewModel
            }
            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            binding.continueButton.setOnClickListener {
                if (it.isEnabled) {
                    viewModel.onContinueButtonClicked()
                }
            }
        }
    }
}
