package com.adamkapus.hikingapp.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.adamkapus.hikingapp.databinding.FragmentDashboardBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : Fragment() {
    private lateinit var binding: FragmentDashboardBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        /* val navHostFragment = //binding.navHostFragment as NavHostFragment
             childFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
         val navController = navHostFragment.navController
         binding.bottomNav.setupWithNavController(navController)*/
        //setupBottomNavBar()
    }

    /* private fun setupBottomNavBar() = with(binding) {
         bottomNav.setOnItemSelectedListener {
             when (it.itemId) {
                 R.id.home -> {
                     findNavController().navigate(R.id.home_nav_graph)
                     true
                 }
                 R.id.map -> {
                     findNavController().navigate(R.id.map)
                     true
                 }
                 R.id.track -> {
                     findNavController().navigate(R.id.track)
                     true
                 }
                 R.id.camera -> {
                     findNavController().navigate(R.id.camera)
                     true
                 }
                 R.id.settings -> {
                     findNavController().navigate(R.id.settings_nav_graph)
                     true
                 }
                 else -> false
             }
         }
     }*/

}