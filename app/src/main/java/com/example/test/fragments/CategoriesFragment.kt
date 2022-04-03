package com.example.test.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.test.R
import com.example.test.activities.CategoryMealsActivity
import com.example.test.adapters.CategoriesAdapter
import com.example.test.databinding.FragmentCategoriesBinding
import com.example.test.databinding.FragmentFavoritesBinding
import com.example.test.viewmodel.CategoriesFragmentViewModel
import com.example.test.viewmodel.CategoryMealsViewModel
import com.example.test.viewmodel.HomeViewModel

class CategoriesFragment : Fragment() {
    private val binding: FragmentCategoriesBinding by lazy {
        FragmentCategoriesBinding.inflate(layoutInflater)
    }
    private lateinit var viewModel: CategoriesFragmentViewModel
    private lateinit var categoryAdapter: CategoriesAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryAdapter = CategoriesAdapter()
        viewModel = ViewModelProviders.of(this)[CategoriesFragmentViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        preparCategoriesRecyclerView()
        viewModel.getCategoriesfragment()
        observeCategoryItemLiveData()
        onCategoryClick()
        return binding.root
    }

    private fun observeCategoryItemLiveData() {
        viewModel.observeCategoriesLiveData().observe(viewLifecycleOwner) { categories ->
            categoryAdapter.setCategoryList(categories)

        }
    }

    private fun preparCategoriesRecyclerView() {
        binding.recFavoritesFrag.apply {
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            adapter = categoryAdapter
        }
    }

    private fun onCategoryClick() {
        categoryAdapter.onItemClick = { t->
            val intent = Intent(activity, CategoryMealsActivity::class.java)
            intent.putExtra(HomeFragment.CATEGORY_NAME,t.strCategory)
            startActivity(intent)
        }
    }


}