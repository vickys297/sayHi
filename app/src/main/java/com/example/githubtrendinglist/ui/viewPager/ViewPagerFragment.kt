package com.example.githubtrendinglist.ui.viewPager

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.githubtrendinglist.R
import com.example.githubtrendinglist.adapters.FullScreenAdapter
import com.example.githubtrendinglist.databinding.FragmentViewPagerBinding
import com.example.githubtrendinglist.model.Datum
import com.example.githubtrendinglist.model.MediaListResponse
import com.example.githubtrendinglist.ui.fullScreenView.FullScreenMediaViewFragment
import com.example.githubtrendinglist.utils.AppConstants
import com.example.githubtrendinglist.utils.AppViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal val TAG = ViewPagerFragment::class.java.simpleName

class ViewPagerFragment : Fragment(R.layout.fragment_view_pager) {


    private lateinit var observerMediaList: Observer<List<Datum>>
    private lateinit var fullScreenAdapter: FullScreenAdapter
    private lateinit var viewModel: ViewPagerViewModel
    private lateinit var binding: FragmentViewPagerBinding

    private lateinit var currentMediaItem: Datum

    private var listSize = 1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentViewPagerBinding.bind(view)
        setupViewModel()
        setupObservers()

        requireArguments().let {
            currentMediaItem = it.getParcelable(AppConstants.NavigationKey.DATUM_KEY)!!
        }

        fullScreenAdapter = FullScreenAdapter(requireActivity().supportFragmentManager, lifecycle)
        binding.viewPager.adapter = fullScreenAdapter

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                if (position > listSize - 3 && listSize > 3) {
                    getMediaList()
                }
            }
        })

        binding.imageButtonBack.setOnClickListener {
            findNavController().navigateUp()
        }


        viewModel.mutableMediaList.postValue(arrayListOf(currentMediaItem))
        getMediaList()
    }

    private fun setupObservers() {
        observerMediaList = Observer { it ->
            val fragmentList = it.map { FullScreenMediaViewFragment.getInstance(it) } as ArrayList
            fullScreenAdapter.updateDataSet(fragmentList)
            listSize += fragmentList.size
        }
        viewModel.mutableMediaList.observe(viewLifecycleOwner, observerMediaList)
    }

    private fun getMediaList() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            when (val response =
                viewModel.getFixedMediaList(lastItemId = currentMediaItem.id.toString())) {
                is MediaListResponse.Success -> {
                    viewModel.mutableMediaList.postValue(response.result)
                }
                is MediaListResponse.Failure -> {
                    Snackbar.make(binding.root, response.message, Toast.LENGTH_SHORT).show()
                }
                is MediaListResponse.HttpErrorCode.Exception -> {
                    Snackbar.make(binding.root, response.exception, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this@ViewPagerFragment,
            AppViewModelFactory()
        )[ViewPagerViewModel::class.java]
    }

}