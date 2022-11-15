package com.sonnt.fpmerchant.ui.merchantinfo

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.sonnt.fpmerchant.R
import com.sonnt.fpmerchant.databinding.FragmentMerchantInfoBinding
import com.sonnt.fpmerchant.network.dto.response.GetMerchantInfoResponse
import com.sonnt.fpmerchant.ui.base.BaseFragment
import java.time.LocalTime


class MerchantInfoFragment : BaseFragment<FragmentMerchantInfoBinding>() {
    override var layoutResId: Int = R.layout.fragment_merchant_info

    private val viewModel: MerchantInfoViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        bindViewModel()
        viewModel.getMerchantInfo()
    }

    fun setupViews() {

        binding.switchLayout.setOnClickListener {
            val opening = binding.openingSwitch.isChecked
            viewModel.changeActivityStatus(!opening)
        }

        binding.textOpeningHour.setOnClickListener {
            showTimePickerDialog {
                viewModel.changeActiveHour(it, null)
            }
        }

        binding.textClosingHour.setOnClickListener {
            showTimePickerDialog {
                viewModel.changeActiveHour(null, it)
            }
        }
    }

    fun showTimePickerDialog(onComplete: (LocalTime) -> Unit) {
        val dialog = TimePickerDialog(requireContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
            { view, hourOfDay, minute ->
                val time = LocalTime.of(hourOfDay, minute)
                onComplete(time)
            },7,0,true)
        dialog.show()
    }

    fun bindViewModel() {
        viewModel.merchantInfo.observe(viewLifecycleOwner) {
            bindMerchantInfo(it)
        }

        viewModel.changeActivityStatus.observe(viewLifecycleOwner) {success ->
            if (success) {
                binding.openingSwitch.isChecked = !binding.openingSwitch.isChecked
            }
        }

        viewModel.changeActiveHour.observe(viewLifecycleOwner) {hourInfo ->
            if(hourInfo.openingHour != null) {
                binding.textOpeningHour.text = hourInfo.openingHour
            }
            if(hourInfo.closingHour != null) {
                binding.textClosingHour.text = hourInfo.closingHour
            }
        }
    }

    fun bindMerchantInfo(merchantInfo: GetMerchantInfoResponse) {
        binding.apply {
            openingSwitch.isChecked = merchantInfo.opening
            textOpeningHour.text = merchantInfo.openingHour
            textClosingHour.text = merchantInfo.closingHour
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            MerchantInfoFragment()
    }
}