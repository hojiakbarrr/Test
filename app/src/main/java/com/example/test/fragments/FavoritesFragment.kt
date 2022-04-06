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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.test.activities.MainActivity
import com.example.test.activities.MealActivity
import com.example.test.adapters.MealsAdapter
import com.example.test.databinding.FragmentFavoritesBinding
import com.example.test.viewmodel.HomeViewModel
import com.google.android.material.snackbar.Snackbar

class FavoritesFragment : Fragment() {
    private val binding: FragmentFavoritesBinding by lazy {
        FragmentFavoritesBinding.inflate(layoutInflater)
    }
    private lateinit var viewmodel: HomeViewModel
    private lateinit var favoritesAdapter: MealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewmodel = (activity as MainActivity).viewModel
        favoritesAdapter = MealsAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preparRecycler()
        observeFavorites()
        onclick()
        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ) = true

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                viewmodel.deleteMeal(favoritesAdapter.diffor.currentList[position])

                Snackbar.make(requireView(),"Meal deleted", Snackbar.LENGTH_LONG).setAction(
                    "Undo",
                    View.OnClickListener {
                        viewmodel.insertMeal(favoritesAdapter.diffor.currentList[position])
                    }
                ).show()
            }
        }

        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.rvFavorites)

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