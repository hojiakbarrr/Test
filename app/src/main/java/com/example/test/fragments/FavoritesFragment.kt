package com.example.test.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.test.R
import com.example.test.activities.MainActivity
import com.example.test.activities.MealActivity
import com.example.test.adapters.FavoritesMealAdapter
import com.example.test.databinding.FragmentFavoritesBinding
import com.example.test.databinding.FragmentHomeBinding
import com.example.test.viewmodel.HomeViewModel

class FavoritesFragment : Fragment() {
    private val binding: FragmentFavoritesBinding by lazy {
        FragmentFavoritesBinding.inflate(layoutInflater)
    }
    private lateinit var viewmodel: HomeViewModel
    private lateinit var favoritesAdapter: FavoritesMealAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewmodel = (activity as MainActivity).viewModel
        favoritesAdapter = FavoritesMealAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preparRecycler()
        observeFavorites()
        onclick()
    }

    private fun onclick() {
        favoritesAdapter.onClick = {t ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(HomeFragment.MEAL_ID, t.idMeal)
            intent.putExtra(HomeFragment.MEAL_NAME, t.strMeal)
            intent.putExtra(HomeFragment.MEAL_THUMB, t.strMealThumb)
            startActivity(intent)
        }
    }

    private fun preparRecycler() {
        binding.rvFavorites.apply {
            layoutManager = GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
            adapter = favoritesAdapter
        }
    }

    private fun observeFavorites() {
        viewmodel.observeFavoritesMealsLiveData().observe(requireActivity(), Observer { meals ->
            favoritesAdapter.diffor.submitList(meals)
            meals.forEach {
                Log.d("Fav", it.idMeal)
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