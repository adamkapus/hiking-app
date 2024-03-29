package com.adamkapus.hikingapp.ui.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.adamkapus.hikingapp.R
import com.adamkapus.hikingapp.databinding.FragmentAuthenticationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthenticationFragment : Fragment() {

    private lateinit var binding: FragmentAuthenticationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthenticationBinding.inflate(layoutInflater)
        binding.authenticationScreenComposable.apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
            setContent {
                AuthenticationScreen(
                    onSuccessfulRegistration = {
                        findNavController()
                            .navigate(R.id.action_authenticationNavGraph_to_homeNavGraph)
                    },
                    onSuccessfulLogin = {
                        findNavController()
                            .navigate(R.id.action_authenticationNavGraph_to_homeNavGraph)
                    }
                )
            }
        }
        return binding.root
    }
}