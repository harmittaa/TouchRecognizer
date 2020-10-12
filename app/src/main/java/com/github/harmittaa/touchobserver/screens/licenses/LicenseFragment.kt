package com.github.harmittaa.touchobserver.screens.licenses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.harmittaa.touchobserver.databinding.LicenseFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val LICENSES_FILE = "licenses.json"

class LicenseFragment : Fragment() {
    private val viewModel: LicenseViewModel by viewModel()
    private lateinit var binding: LicenseFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LicenseFragmentBinding.inflate(inflater, container, false)
        viewModel.generateLicensePojos(requireContext().assets.open(LICENSES_FILE))

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.libraries.observe(viewLifecycleOwner) { libraries ->
            binding.librariesText.text = "$libraries"
        }
    }
}
