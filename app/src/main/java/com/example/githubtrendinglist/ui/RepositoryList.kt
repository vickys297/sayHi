package com.example.githubtrendinglist.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubtrendinglist.R
import com.example.githubtrendinglist.adapters.GitRepositoryAdapter
import com.example.githubtrendinglist.databinding.FragmentRepositoryListBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RepositoryList : Fragment(R.layout.fragment_repository_list) {

    private lateinit var binding: FragmentRepositoryListBinding
    private lateinit var viewModel: RepositoryListViewModel

    private lateinit var adapter: GitRepositoryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRepositoryListBinding.bind(view)
        setupViewModel()

        adapter = GitRepositoryAdapter()

        binding.recyclerView.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = adapter
        }

        getRepositoryList()
    }

    private fun getRepositoryList() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getRepositoryList().collectLatest {
                Log.i(TAG, "getRepositoryList: $it")
                adapter.submitData(it)
            }
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[RepositoryListViewModel::class.java]

    }


}