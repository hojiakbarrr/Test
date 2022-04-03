package com.example.test.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.test.R
import com.example.test.databinding.ActivityMealBinding
import com.example.test.databinding.FragmentHomeBinding
import com.example.test.db.MealDataBase
import com.example.test.fragments.HomeFragment
import com.example.test.pojo.Meal
import com.example.test.utils.toast
import com.example.test.viewmodel.MealViewModel
import com.example.test.viewmodel.MealViewModelfactory

class MealActivity : AppCompatActivity() {
    private val binding: ActivityMealBinding by lazy {
        ActivityMealBinding.inflate(layoutInflater)
    }
    private lateinit var mealId:String
    private lateinit var mealName:String
    private lateinit var mealThumb:String
    private lateinit var mealMVVM: MealViewModel
    private lateinit var youTubeLink: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mealDatabase = MealDataBase.getInstance(this)
        val viewModelFactory = MealViewModelfactory(mealDatabase)
        mealMVVM = ViewModelProvider(this,viewModelFactory)[MealViewModel::class.java]

//        mealMVVM = ViewModelProviders.of(this)[MealViewModel::class.java]

        getMealInformationFromIntent()
        setInformationViews()
        loadingCase()
        mealMVVM.getMealDetail(mealId)
        observeMealDetailsLivedata()
        binding.imgYoutube.setOnClickListener {
            openYouTube()
        }
        onFavoriteClick()

        setContentView(binding.root)
    }

    private fun onFavoriteClick() {
        binding.addToFav.setOnClickListener {
            mealToSave?.let {
                mealMVVM.insertMeal(it)
                toast("Meal is save")
            }
        }
    }

    private fun openYouTube() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youTubeLink))
        startActivity(intent)
    }

    private var mealToSave: Meal? = null
    private fun observeMealDetailsLivedata() {
        mealMVVM.observeMealDetailLiveData().observe(this,object  : Observer<Meal>{
            @SuppressLint("SetTextI18n")
            override fun onChanged(t: Meal?) {
                val meal = t
                mealToSave = meal
                binding.tvCategory.text = "Category : ${meal!!.strCategory}"
                binding.tvArea.text = "Area : ${meal.strArea}"
                binding.tvInstructionsSteps.text = meal.strInstructions
                youTubeLink = meal.strYoutube!!
                responseCase()
            }

        })
    }

    private fun setInformationViews() {
        Glide.with(applicationContext)
            .load(mealThumb)
            .into(binding.imgMealDetail)
        binding.collapsingToolbar.title = mealName
        binding.collapsingToolbar.setCollapsedTitleTextColor(resources.getColor(R.color.white))
        binding.collapsingToolbar.setExpandedTitleColor(resources.getColor(R.color.white))
    }


    private fun getMealInformationFromIntent() {
        val intent = intent
        mealId = intent.getStringExtra(HomeFragment.MEAL_ID)!!
        mealName = intent.getStringExtra(HomeFragment.MEAL_NAME)!!
        mealThumb = intent.getStringExtra(HomeFragment.MEAL_THUMB)!!
    }

    private fun loadingCase(){
        binding.apply {
            progressBar.visibility = View.VISIBLE
            addToFav.visibility = View.INVISIBLE
            tvInstructions.visibility = View.INVISIBLE
            tvCategory.visibility = View.INVISIBLE
            tvArea.visibility = View.INVISIBLE
            imgYoutube.visibility = View.INVISIBLE
        }
    }

    private fun responseCase(){
        binding.apply {
            progressBar.visibility = View.INVISIBLE
            addToFav.visibility = View.VISIBLE
            tvInstructions.visibility = View.VISIBLE
            tvCategory.visibility = View.VISIBLE
            tvArea.visibility = View.VISIBLE
            imgYoutube.visibility = View.VISIBLE
        }
    }


}