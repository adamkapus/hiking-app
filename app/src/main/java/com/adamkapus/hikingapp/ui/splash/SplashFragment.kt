package com.adamkapus.hikingapp.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.adamkapus.hikingapp.R
import kotlinx.coroutines.launch

class SplashFragment : Fragment() {

    private val viewModel: SplashViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.checkIfUserIsSignedIn()

        lifecycleScope.launch {
            viewModel.userIsSignedInEvent.collect {
                onUserIsSignedIn(it)
            }
        }
        lifecycleScope.launch {
            viewModel.userIsSignedOutEvent.collect {
                onUserIsSignedOut(it)
            }
        }

    }

    private fun onUserIsSignedIn(isSignedIn: Boolean) {
        if (isSignedIn) {
            viewModel.handledUserIsSignedInEvent()
            findNavController().navigate(R.id.action_splashNavGraph_to_homeNavGraph)
        }
    }

    private fun onUserIsSignedOut(isSignedOut: Boolean) {
        if (isSignedOut) {
            viewModel.handledUserIsSignedOutEvent()
            findNavController().navigate(R.id.action_splashNavGraph_to_authenticationNavGraph)
        }
    }
}