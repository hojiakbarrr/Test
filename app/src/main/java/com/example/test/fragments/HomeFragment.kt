package com.example.test.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.test.activities.MealActivity
import com.example.test.adapters.CategoriesAdapter
import com.example.test.adapters.MostPopularAdapter
import com.example.test.databinding.FragmentHomeBinding
import com.example.test.pojo.MealsByCategory
import com.example.test.pojo.Meal
import com.example.test.viewmodel.HomeViewModel

class HomeFragment : Fragment() {
    private val binding: FragmentHomeBinding by lazy {
        FragmentHomeBinding.inflate(layoutInflater)
    }
    private lateinit var homeMVVM : HomeViewModel
    private lateinit var randomMeal: Meal
    companion object{
        const val MEAL_ID = "com.example.test.fragments_IDMeal"
        const val MEAL_NAME = "com.example.test.fragments_nameMeal"
        const val MEAL_THUMB = "com.example.test.fragments_thumbMeal"
    }
    private lateinit var popularAdapter: MostPopularAdapter
    private lateinit var categoryAdapter: CategoriesAdapter




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeMVVM = ViewModelProviders.of(this)[HomeViewModel::class.java]
        popularAdapter = MostPopularAdapter()
        categoryAdapter = CategoriesAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding.randomMeal.setOnClickListener {
            onclicck()
        }
        return binding.root
    }



















    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        popularRecycler()
        homeMVVM.getRandomMeal()
        observerRandomMeal()

        homeMVVM.getPopularItems()
        observePopularItemLiveData()
        onPopularItemClick()

        preparCategoriesRecyclerView()
        homeMVVM.getCategories()
        observeCategoryItemLiveData()

    }

    private fun preparCategoriesRecyclerView() {
        binding.recyclerCategories.apply {
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL,false)
            adapter = categoryAdapter
        }
    }

    private fun onclicck() {
        val intent = Intent(activity, MealActivity::class.java)
        intent.putExtra(MEAL_ID,randomMeal.idMeal)
        intent.putExtra(MEAL_NAME,randomMeal.strMeal)
        intent.putExtra(MEAL_THUMB,randomMeal.strMealThumb)
        startActivity(intent)
    }

    private fun observeCategoryItemLiveData() {
        homeMVVM.observeCategoriesLiveData().observe(viewLifecycleOwner){categories ->
                categoryAdapter.setCategoryList(categories)

        }
    }

    private fun onPopularItemClick() {
        popularAdapter.onClick = {meal ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID,meal.idMeal)
            intent.putExtra(MEAL_NAME,meal.strMeal)
            intent.putExtra(MEAL_THUMB,meal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun popularRecycler() {
        binding.apply {
            recViewMealsPopular.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
            recViewMealsPopular.adapter = popularAdapter
        }
    }

    private fun observePopularItemLiveData() {
        homeMVVM.observePopularItem().observe(viewLifecycleOwner){
            t ->
            popularAdapter.setMeals(t as ArrayList<MealsByCategory>)
        }
    }

    private fun observerRandomMeal() {
        homeMVVM.observeRandomMealLivedata().observe(viewLifecycleOwner
        ) { t ->
            Glide.with(this@HomeFragment)
                .load(t!!.strMealThumb)
                .into(binding.imgRandomMeal)
            this.randomMeal = t
        }
    }
}