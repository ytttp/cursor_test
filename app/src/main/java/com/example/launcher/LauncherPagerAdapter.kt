package com.example.launcher

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class LauncherPagerAdapter(
    activity: FragmentActivity,
    private val viewModel: LauncherViewModel
) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = viewModel.pageCount()

    override fun createFragment(position: Int): Fragment {
        return LauncherPageFragment.newInstance(position)
    }

    override fun getItemId(position: Int): Long = position.toLong()

    override fun containsItem(itemId: Long): Boolean {
        return itemId < viewModel.pageCount()
    }
}
