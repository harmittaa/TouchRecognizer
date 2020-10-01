package com.github.harmittaa.touchobserver.screens.onboarding.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.harmittaa.touchobserver.databinding.FragmentOnboardingFirstBinding
import com.github.harmittaa.touchobserver.databinding.FragmentOnboardingFourthBinding
import com.github.harmittaa.touchobserver.databinding.FragmentOnboardingSecondBinding
import com.github.harmittaa.touchobserver.databinding.FragmentOnboardingSplashBinding
import com.github.harmittaa.touchobserver.databinding.FragmentOnboardingThirdBinding
import com.github.harmittaa.touchobserver.screens.onboarding.OnboardingViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class OnboardingSplashScreen : Fragment() {
    private lateinit var binding: FragmentOnboardingSplashBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnboardingSplashBinding.inflate(inflater, container, false)
        return binding.root
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
