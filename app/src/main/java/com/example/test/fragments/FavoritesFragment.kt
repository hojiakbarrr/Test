package com.example.test.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.test.R
import com.example.test.activities.MainActivity
import com.example.test.databinding.FragmentFavoritesBinding
import com.example.test.databinding.FragmentHomeBinding
import com.example.test.viewmodel.HomeViewModel

class FavoritesFragment : Fragment() {
    private val binding: FragmentFavoritesBinding by lazy {
        FragmentFavoritesBinding.inflate(layoutInflater)
    }
    private lateinit var viewmodel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewmodel = (activity as MainActivity).viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeFavorites()
    }

    private fun observeFavorites() {
        viewmodel.observeFavoritesMealsLiveData().observe(requireActivity(), Observer {meals ->
            meals.forEach{
                Log.d("Fav",it.idMeal)

            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }


}