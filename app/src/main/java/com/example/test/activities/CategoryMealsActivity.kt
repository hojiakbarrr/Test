package com.example.test.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.test.R
import com.example.test.adapters.CategoriesAdapter
import com.example.test.adapters.CategoryMealsAdapter
import com.example.test.databinding.ActivityCategoryMealsBinding
import com.example.test.databinding.ActivityMainBinding
import com.example.test.fragments.HomeFragment
import com.example.test.viewmodel.CategoryMealsViewModel
import com.example.test.viewmodel.HomeViewModel

class CategoryMealsActivity : AppCompatActivity() {
    private val binding: ActivityCategoryMealsBinding by lazy {
        ActivityCategoryMealsBinding.inflate(layoutInflater)
    }
    private lateinit var categoryMVVM: CategoryMealsViewModel
    private lateinit var categoryMealsAdapter: CategoryMealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        preparRecyclerView()
        categoryMVVM = ViewModelProviders.of(this)[CategoryMealsViewModel::class.java]
        categoryMVVM.getCategoryMeal(intent.getStringExtra(HomeFragment.CATEGORY_NAME)!!)
        categoryMVVM.observeCategoryList().observe(this, Observer {mealsList->
            categoryMealsAdapter.setMealsList(mealsList)
        })
        onClick()
    }

    private fun onClick() {
        categoryMealsAdapter.onItemClick = {t ->
            val intent = Intent(this, MealActivity::class.java)
            intent.putExtra(HomeFragment.MEAL_ID, t.idMeal)
            intent.putExtra(HomeFragment.MEAL_NAME, t.strMeal)
            intent.putExtra(HomeFragment.MEAL_THUMB, t.strMealThumb)
            startActivity(intent)
        }
    }


    private fun preparRecyclerView() {
        categoryMealsAdapter = CategoryMealsAdapter()
        binding.rvMeals.apply {
            layoutManager = GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
            adapter = categoryMealsAdapter

        }
    }
}