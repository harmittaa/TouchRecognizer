package com.github.harmittaa.touchobserver.screens.information

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.github.harmittaa.touchobserver.databinding.ScreenInformationBinding
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class InformationFragment : Fragment() {
    private lateinit var binding: ScreenInformationBinding
    private val viewModel: InformationViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ScreenInformationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.dataRemovalButton.setOnClickListener {
            Timber.d("Removal button clicked!")
            viewModel.onDataRemovalClicked()
        }

        binding.infoBackButton.setOnClickListener {
            this@InformationFragment.view?.findNavController()
                ?.navigateUp()
        }

        binding.infoLicensesButton.setOnClickListener {
            navigateToLicenses()
        }

        binding.infoAppVersion.text = "App version ${getVersion() ?: ""}"

        bindViewModel()
    }

    private fun navigateToLicenses() {
        startActivity(Intent(requireContext(), OssLicensesMenuActivity::class.java))
    }

    private fun bindViewModel() {
        viewModel.removalRequestError.observe(viewLifecycleOwner) {
            showToastOnDataRemovalRequestFailed()
        }

        viewModel.removalRequestSuccess.observe(viewLifecycleOwner) { didSucceed ->
            if (didSucceed) {
                Toast.makeText(
                    requireContext(),
                    "Data removal request sent",
                    Toast.LENGTH_SHORT
                ).show()
                binding.dataRemovalButton.isEnabled = false
            } else {
                showToastOnDataRemovalRequestFailed()
            }
        }
    }

    private fun showToastOnDataRemovalRequestFailed() {
        Toast.makeText(
            requireContext(),
            "Data removal request failed! Try again later",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun getVersion(): String? {
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
}
