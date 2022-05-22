package com.example.githubtrendinglist.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.githubtrendinglist.ui.fullScreenView.FullScreenMediaViewFragment

class FullScreenAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    private val fragmentList: ArrayList<Fragment> = arrayListOf()

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

    fun updateDataSet(newDataSet: List<FullScreenMediaViewFragment>?) {
        newDataSet?.let {
            val lastPosition = fragmentList.size
            fragmentList.addAll(it)
            notifyItemRangeInserted(lastPosition, it.size)
        }
    }


}