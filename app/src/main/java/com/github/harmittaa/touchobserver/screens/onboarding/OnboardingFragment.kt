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
import com.github.harmittaa.touchobserver.activity.MainActivity
import com.github.harmittaa.touchobserver.databinding.ScreenOnboardingBinding
import com.github.harmittaa.touchobserver.screens.onboarding.pages.OnboardingFirstScreen
import com.github.harmittaa.touchobserver.screens.onboarding.pages.OnboardingFourthScreen
import com.github.harmittaa.touchobserver.screens.onboarding.pages.OnboardingSecondScreen
import com.github.harmittaa.touchobserver.screens.onboarding.pages.OnboardingSplashScreen
import com.github.harmittaa.touchobserver.screens.onboarding.pages.OnboardingThirdScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

private const val ONBOARDING_PAGE_COUNT = 5

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
        bindViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pagerAdapter = OnboardingPagerAdapter(this)
        binding.pager.adapter = pagerAdapter
        binding.pager.isUserInputEnabled = false
    }

    private fun bindViewModel() {
        viewModel.canSkipLogin.observe(viewLifecycleOwner) { resource ->
            CoroutineScope(Dispatchers.Main).launch {
                delay(2300)
                if (resource) {
                    navigateOnwards()
                    return@launch
                }
                if (binding.pager.currentItem == 0) {
                    showNextPagerScreen()
                }
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

        viewModel.onContinueInvoked.observe(viewLifecycleOwner) {
            if (binding.pager.currentItem == ONBOARDING_PAGE_COUNT - 1) {
                viewModel.onConsentGiven()
            } else {
                showNextPagerScreen()
            }
        }
    }

    private fun showNextPagerScreen() {
        binding.pager.apply {
            setCurrentItem(currentItem + 1, true)
        }
    }

    private fun navigateOnwards() {
        view?.findNavController()
            ?.navigate(OnboardingFragmentDirections.actionOnboardingFragmentToGameFragment())
    }

    class OnboardingPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

        override fun getItemCount(): Int = ONBOARDING_PAGE_COUNT

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> OnboardingSplashScreen()
                1 -> OnboardingFirstScreen()
                2 -> OnboardingSecondScreen()
                3 -> OnboardingThirdScreen()
                4 -> OnboardingFourthScreen()
                else -> {
                    OnboardingFirstScreen()
                }
            }
        }
    }
}
