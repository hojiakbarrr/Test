package com.example.test.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.test.pojo.MealsByCategory
import com.example.test.pojo.MealsByCategoryList
import com.example.test.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryMealsViewModel(): ViewModel() {
    private var categoryItemLiveData = MutableLiveData<List<MealsByCategory>>()


    fun getCategoryMeal(categoryName:String){
        RetrofitInstance.api.getPopularItems(categoryName).enqueue(object : Callback<MealsByCategoryList>{
            override fun onResponse(
                call: Call<MealsByCategoryList>,
                response: Response<MealsByCategoryList>,
            ) {
                if (response.isSuccessful){
                    categoryItemLiveData.value = response.body()?.meals
                }
            }

            override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    fun observeCategoryList(): LiveData<List<MealsByCategory>> {
        return categoryItemLiveData
    }

}