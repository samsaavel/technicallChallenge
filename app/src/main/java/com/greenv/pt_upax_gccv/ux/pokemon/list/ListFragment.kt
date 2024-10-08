package com.greenv.pt_upax_gccv.ux.pokemon.list

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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.greenv.pt_upax_gccv.data.PokemonDB
import com.greenv.pt_upax_gccv.databinding.FragmentListBinding
import com.greenv.pt_upax_gccv.network.NetworkModule
import com.greenv.pt_upax_gccv.repositories.DataSyncManager
import com.greenv.pt_upax_gccv.repositories.PokemonRepository
import com.greenv.pt_upax_gccv.viewModels.DataState
import com.greenv.pt_upax_gccv.viewModels.PokemonViewModel
import com.greenv.pt_upax_gccv.viewModels.PokemonViewModelFactory
import kotlinx.coroutines.launch

class ListFragment : Fragment(), PokemonSelectedListener {

    private lateinit var binding: FragmentListBinding
    private lateinit var viewModel: PokemonViewModel
    private lateinit var pokemonAdapter: ListAdapter

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
        binding = FragmentListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pokemonAdapter = ListAdapter(arrayListOf(), this@ListFragment)
        Log.d("*****PokemonList", "view created")
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        setupRecyclerView()
        viewModel.fetchNextPokemonPage()
        observeViewModel()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.pokemonResponse.observe(viewLifecycleOwner) { state ->
                when (state) {
                    DataState.Empty -> {
                        Log.d("*****PokemonList", "Empty state")
                    }

                    is DataState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), state.errorMessage, Toast.LENGTH_SHORT)
                            .show()
                    }

                    DataState.Loading -> {
                        Log.d("*****PokemonList", "loading state")
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is DataState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val pokemonList = state.data
                        //val adapter = ListAdapter(pokemonList.result, "")
                        pokemonAdapter.addData(newPokemonList = pokemonList.result)
                        //binding.recyclerView.adapter = ListAdapter(pokemonList.result, "")
                        Log.d("*****PokemonList", "$pokemonList")
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = pokemonAdapter
        }
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = binding.recyclerView.layoutManager as? LinearLayoutManager
                layoutManager?.let {
                    val visibleItemCount = it.childCount
                    val totalItemCount = it.itemCount
                    val pastVisibleItems = it.findFirstVisibleItemPosition()
                    if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                        viewModel.fetchNextPokemonPage()
                    }
                }
            }
        })
    }

    override fun onPokemonSelected(id: Int) {
        val action = ListFragmentDirections.actionListFragmentToDetailsFragment(id)
        findNavController().navigate(action)
    }
}