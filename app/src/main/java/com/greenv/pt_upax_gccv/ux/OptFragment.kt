package com.greenv.pt_upax_gccv.ux

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.greenv.pt_upax_gccv.R
import com.greenv.pt_upax_gccv.databinding.FragmentOptBinding

class OptFragment : Fragment() {

    private lateinit var binding: FragmentOptBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentOptBinding.inflate(layoutInflater, container, false)
        binding.profileButton.setOnClickListener {
            findNavController().navigate(R.id.action_option_fragment_to_profileFragment)
        }
        binding.pokemonButton.setOnClickListener {
            findNavController().navigate(R.id.action_option_fragment_to_listFragment)
        }
        return binding.root
    }
}