package com.sonnt.fpmerchant.ui.home

import android.os.Bundle
import android.view.View
import com.google.android.material.tabs.TabLayoutMediator
import com.sonnt.fpmerchant.R
import com.sonnt.fpmerchant.databinding.FragmentHomeBinding
import com.sonnt.fpmerchant.ui.base.BaseFragment

class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    override var layoutResId: Int = R.layout.fragment_home
    private lateinit var pagerAdapter: OrderListPagerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {
        pagerAdapter = OrderListPagerAdapter(this)
        binding.viewPager.offscreenPageLimit = 2
        binding.viewPager.adapter = pagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when(position) {
                0 -> "Đang làm"
                1 -> "Đã xong"
                else -> ""
            }
        }.attach()
    }

}