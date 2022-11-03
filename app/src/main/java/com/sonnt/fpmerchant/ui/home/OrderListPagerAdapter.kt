package com.sonnt.fpmerchant.ui.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sonnt.fpmerchant.ui.activeorders.ActiveOrdersFragment
import com.sonnt.fpmerchant.ui.doneorders.DoneOrdersFragment
import com.sonnt.fpmerchant.ui.orderlist.OrderListFragment

class OrderListPagerAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> ActiveOrdersFragment.newInstance()
            1 -> DoneOrdersFragment.newInstance()
            else -> throw IllegalArgumentException()
        }
    }
}