package com.example.test.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.test.pojo.*
import com.example.test.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel():ViewModel() {

    private var randomMealLiveData = MutableLiveData<Meal>()
    private var popularItemLiveData = MutableLiveData<List<MealsByCategory>>()
    private var categoriesLiveData = MutableLiveData<List<Category>>()

    fun getRandomMeal(){

        RetrofitInstance.api.getRandomMeal().enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.isSuccessful){
                    val randomMeal: Meal = response.body()?.meals!![0]
                       Log.d("Test", response.body()!!.meals.toString())
                    randomMealLiveData.value = randomMeal
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    fun getPopularItems(){
        RetrofitInstance.api.getPopularItems("Seafood").enqueue(object : Callback<MealsByCategoryList>{
            override fun onResponse(call: Call<MealsByCategoryList>, response: Response<MealsByCategoryList>) {
                if (response.isSuccessful){
                    popularItemLiveData.value = response.body()!!.meals
                }
            }

            override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    fun getCategories(){
        RetrofitInstance.api.getCategories().enqueue(object : Callback<CategoryList>{
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
                response.body()?.let {categoryList ->
                    categoriesLiveData.postValue(categoryList.categories)
                }
            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    fun observePopularItem():LiveData<List<MealsByCategory>>{
        return popularItemLiveData
    }

    fun observeRandomMealLivedata():LiveData<Meal>{
        return randomMealLiveData
    }

    fun observeCategoriesLiveData():LiveData<List<Category>>{
        return categoriesLiveData
    }

}