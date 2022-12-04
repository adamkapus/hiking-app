package com.adamkapus.hikingapp.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.adamkapus.hikingapp.R
import com.adamkapus.hikingapp.databinding.FragmentHomeBinding
import com.adamkapus.hikingapp.domain.model.map.Route
import com.adamkapus.hikingapp.ui.list.HomeUiState.Initial
import com.adamkapus.hikingapp.ui.list.HomeUiState.RoutesLoaded
import com.adamkapus.hikingapp.ui.list.adapter.HomeAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: HomeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        lifecycleScope.launch {
            viewModel.uiState.collect {
                render(it)
            }
        }

        viewModel.loadRoutes()
    }

    private fun render(uiState: HomeUiState) {
        when (uiState) {
            is Initial -> {}
            is RoutesLoaded -> {
                adapter.replaceList(uiState.routes)
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = HomeAdapter()
        adapter.setOnItemClickListener(this::onItemClick)

        val rv = binding.homeList
        rv.layoutManager = LinearLayoutManager(context)
        rv.adapter = adapter
    }

    private fun onItemClick(route: Route) {
        val bundle = bundleOf("userRouteId" to route.id.toString())
        findNavController().setGraph(
            R.navigation.map,
            bundle
        )//navigate(R.id.action_homeNavGraph_to_MapFragment, bundle)
    }
}