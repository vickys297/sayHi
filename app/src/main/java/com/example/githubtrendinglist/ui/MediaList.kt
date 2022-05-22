package com.example.githubtrendinglist.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.githubtrendinglist.R
import com.example.githubtrendinglist.adapters.AdapterLoadState
import com.example.githubtrendinglist.adapters.MediaListAdapter
import com.example.githubtrendinglist.databinding.FragmentMediaListBinding
import com.example.githubtrendinglist.model.Datum
import com.example.githubtrendinglist.utils.AppConstants
import com.example.githubtrendinglist.utils.AppInterface
import com.example.githubtrendinglist.utils.AppViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MediaList : Fragment(R.layout.fragment_media_list), View.OnClickListener {

    private lateinit var binding: FragmentMediaListBinding
    private lateinit var viewModel: RepositoryListViewModel

    private lateinit var mediaListAdapter: MediaListAdapter


    private val mediaListCallback = object : AppInterface.MediaItemCallback {
        override fun onItemClick(item: Datum, position: Int) {
            val bundle = Bundle()
            bundle.putParcelable(AppConstants.NavigationKey.DATUM_KEY, item)
            findNavController().navigate(R.id.action_repositoryList_to_viewPagerFragment, bundle)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMediaListBinding.bind(view)
        setupViewModel()

        mediaListAdapter = MediaListAdapter(callback = mediaListCallback)

        binding.recyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = mediaListAdapter.withLoadStateHeaderAndFooter(
                header = AdapterLoadState { mediaListAdapter.retry() },
                footer = AdapterLoadState { mediaListAdapter.retry() }
            )
            addOnChildAttachStateChangeListener(object :
                RecyclerView.OnChildAttachStateChangeListener {
                override fun onChildViewAttachedToWindow(view: View) {
//                    if (findContainingViewHolder(view)?.itemViewType == 1) {
//                        (findContainingViewHolder(view) as VideoViewHolder).startPlayer()
//                    }
                }

                override fun onChildViewDetachedFromWindow(view: View) {
//                    if (findContainingViewHolder(view)?.itemViewType == 1) {
//                        (findContainingViewHolder(view) as VideoViewHolder).stopAndReleasePlayer()
//                    }
                }

            })
        }

        binding.buttonPost.setOnClickListener(this@MediaList)

        getRepositoryList()
    }

    private fun getRepositoryList() {
        viewLifecycleOwner.lifecycleScope.launch {
            mediaListAdapter.loadStateFlow.collectLatest { loadStates ->
                Log.i(TAG, "getRepositoryList: $loadStates")
                val refreshState = loadStates.refresh
                binding.recyclerView.isVisible = refreshState is LoadState.NotLoading
                binding.progressBar.isVisible = refreshState is LoadState.Loading
                binding.buttonRetry.isVisible = refreshState is LoadState.Error
                handleError(loadStates)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getMediaList().collectLatest {
                mediaListAdapter.submitData(it)
            }
        }
    }

    private fun handleError(loadState: CombinedLoadStates) {
        val errorState = loadState.source.append as? LoadState.Error
            ?: loadState.source.prepend as? LoadState.Error
            ?: loadState.mediator?.refresh as? LoadState.Error

        errorState?.let {
            Toast.makeText(requireContext(), it.error.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupViewModel() {
        val viewModelProvider = AppViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelProvider)[RepositoryListViewModel::class.java]

    }

    override fun onClick(id: View?) {
        id?.let {
            when (id.id) {
                binding.buttonPost.id -> {
                    findNavController().navigate(R.id.action_repositoryList_to_fileSelectorFragment)
                }
            }
        }
    }

}
