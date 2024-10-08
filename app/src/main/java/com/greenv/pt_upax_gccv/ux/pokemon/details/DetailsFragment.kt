package com.greenv.pt_upax_gccv.ux.pokemon.details

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.greenv.pt_upax_gccv.data.PokemonDB
import com.greenv.pt_upax_gccv.databinding.FragmentDetailsBinding
import com.greenv.pt_upax_gccv.network.NetworkModule
import com.greenv.pt_upax_gccv.network.models.DetailsModel
import com.greenv.pt_upax_gccv.repositories.DataSyncManager
import com.greenv.pt_upax_gccv.repositories.PokemonRepository
import com.greenv.pt_upax_gccv.viewModels.DataState
import com.greenv.pt_upax_gccv.viewModels.PokemonViewModel
import com.greenv.pt_upax_gccv.viewModels.PokemonViewModelFactory
import kotlinx.coroutines.launch

class DetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding
    private lateinit var viewModel: PokemonViewModel
    private lateinit var imageSliderAdapter: ImageSliderAdapter
    private lateinit var typeAdapter: TypeAdapter
    private var pokemonDetails: DetailsModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = PokemonDB.getDatabase(requireContext())
        val listPokemon = NetworkModule().pokemonApi
        val detailsPokemon = NetworkModule().detailsApi
        val connectivity =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val dataSync =
            DataSyncManager(listPokemon, detailsPokemon, database.getPokemonDao(), connectivity)
        val pokemonRepository = PokemonRepository(dataSync)
        val factory = PokemonViewModelFactory(pokemonRepository)
        viewModel = ViewModelProvider(this, factory).get(PokemonViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: DetailsFragmentArgs by navArgs()
        viewModel.getDetailsPokemon(args.id)
        typeAdapter = TypeAdapter(arrayListOf())
        binding.typeRecycler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        Log.d("*****PokemonDetails", "calls to services")
        observeViewModel()
        Log.d("*****PokemonDetails", "after observing viewmodel")
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.detailsState.observe(viewLifecycleOwner) { state ->
                when (state) {
                    is DataState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        Log.d("*****PokemonDetails", "loading state")
                    }

                    is DataState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        pokemonDetails = state.data
                        Log.d("*****PokemonDetails", "details: $pokemonDetails")
                        updateRecyclerViewIfNeeded()
                    }

                    is DataState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Log.d("*****PokemonDetails", "error state")
                        Toast.makeText(requireContext(), state.errorMessage, Toast.LENGTH_SHORT)
                            .show()
                    }

                    is DataState.Empty -> {
                        binding.progressBar.visibility = View.GONE
                        Log.d("*****PokemonList", "empty state")
                    }
                }
            }
        }
    }

    private fun updateRecyclerViewIfNeeded() {
        if (pokemonDetails != null) {
            setupRecyclerView(pokemonDetails!!)
        }
    }

    private fun setupRecyclerView(details: DetailsModel) {
        val availableList = details.sprites.getAvailableUrls()
        val types = details.types.map { it.type }

        binding.name.append(details.name)
        binding.id.append(details.id.toString())
        binding.height.append(details.height.toString())
        binding.weight.append(details.weight.toString())
        imageSliderAdapter = ImageSliderAdapter(availableList)
        binding.imageSlider.adapter = imageSliderAdapter
        typeAdapter = TypeAdapter(types)
        typeAdapter.notifyDataSetChanged()
//        Log.d("*****PokemonDetails", "types: $types")
        binding.typeRecycler.adapter = typeAdapter
//        typeAdapter = TypeAdapter(types)


    }
}