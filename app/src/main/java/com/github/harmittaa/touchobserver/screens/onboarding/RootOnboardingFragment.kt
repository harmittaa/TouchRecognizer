package com.github.harmittaa.touchobserver.screens.onboarding

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.TransitionDrawable
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
import com.github.harmittaa.touchobserver.model.ScreenSpecifications
import com.github.harmittaa.touchobserver.screens.onboarding.pages.OnboardingApprovalScreen
import com.github.harmittaa.touchobserver.screens.onboarding.pages.OnboardingConsentScreen
import com.github.harmittaa.touchobserver.screens.onboarding.pages.OnboardingGenderScreen
import com.github.harmittaa.touchobserver.screens.onboarding.pages.OnboardingHandednessScreen
import com.github.harmittaa.touchobserver.screens.onboarding.pages.OnboardingSplashScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

private const val ONBOARDING_PAGE_COUNT = 5

enum class ScreenType {
    SPLASH, CONSENT, GENDER, HANDEDNESS, APPROVAL
}

class RootOnboardingFragment : Fragment() {
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
        viewModel.screenSpecs = getScreenSpecifications()
        viewModel.appVersion = getAppVersion()

        viewModel.canSkipLogin.observe(viewLifecycleOwner) { skipLogin ->
            CoroutineScope(Dispatchers.Main).launch {
                delay(2000)
                if (skipLogin) {
                    navigateOnwards()
                    return@launch
                }
                if (binding.pager.currentItem == 0) {
                    showNextPagerScreen(ScreenType.SPLASH)
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

        viewModel.onContinueInvoked.observe(viewLifecycleOwner) { type ->
            when (type) {
                ScreenType.APPROVAL -> viewModel.onConsentGiven()
                else -> showNextPagerScreen(type)
            }
        }
    }

    private fun showNextPagerScreen(type: ScreenType) {
        binding.pager.apply {
            if (currentItem % 2 != 0) {
                (binding.rootBackground.background as TransitionDrawable).startTransition(250)
            } else {
                (binding.rootBackground.background as TransitionDrawable).reverseTransition(250)
            }

            val nextItem = when (type) {
                ScreenType.SPLASH -> 1
                ScreenType.CONSENT -> 2
                ScreenType.GENDER -> 3
                ScreenType.HANDEDNESS -> 4
                ScreenType.APPROVAL -> 1
            }
            setCurrentItem(nextItem, true)
        }
    }

    private fun navigateOnwards() {
        view?.findNavController()
            ?.navigate(RootOnboardingFragmentDirections.actionOnboardingFragmentToGameFragment())
    }

    private fun getScreenSpecifications(): ScreenSpecifications {
        val metrics = requireContext().resources.displayMetrics
        return ScreenSpecifications(metrics.density, metrics.heightPixels, metrics.widthPixels)
    }

    private fun getAppVersion(): String {
        return try {
            requireContext().packageManager.getPackageInfo(
                requireContext().packageName,
                0
            ).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            Timber.d("Couldn't get package name $e")
            ""
        }
    }

    inner class OnboardingPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

        override fun getItemCount(): Int = ONBOARDING_PAGE_COUNT

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> OnboardingSplashScreen()
                1 -> OnboardingConsentScreen()
                2 -> OnboardingGenderScreen()
                3 -> OnboardingHandednessScreen()
                4 -> OnboardingApprovalScreen()
                else -> {
                    OnboardingConsentScreen()
                }
            }
        }
    }
}
