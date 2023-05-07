package com.example.mystoryapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mystoryapp.R
import com.example.mystoryapp.data.model.LoginResponse
import com.example.mystoryapp.data.model.StorySession
import com.example.mystoryapp.databinding.ActivityMainBinding
import com.example.mystoryapp.di.ViewModelFactory
import com.example.mystoryapp.ui.adapter.LoadingStateAdapter
import com.example.mystoryapp.ui.adapter.StoryAdapter
import com.example.mystoryapp.ui.viewmodel.MainViewModel
import com.google.android.material.elevation.SurfaceColors

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var pref: StorySession
    private val storyAdapter = StoryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val colorActionBar = SurfaceColors.SURFACE_5.getColor(this)
        window.statusBarColor = colorActionBar
        window.navigationBarColor = colorActionBar

        pref = StorySession(this)

        setUpAdapter()
        setUpAction()
    }

    private fun setUpAdapter() {
        binding.apply {
            rvStories.adapter = storyAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    storyAdapter.retry()
                },
            )
            rvStories.layoutManager = LinearLayoutManager(this@MainActivity)
            addStory.setOnClickListener {
                val intent = Intent(this@MainActivity, StoryActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun setUpAction() {
        pref = StorySession(this)
        val user: String? = pref.getToken()

        mainViewModel.getStories("Bearer $user").observe(this) {
            storyAdapter.submitData(lifecycle, it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.maps -> {
                startActivity(Intent(this, MapsActivity::class.java))
                true
            }
            R.id.logout -> {
                pref.saveUser(
                    LoginResponse(
                        name = null,
                        token = null,
                        isLogin = false,
                    ),
                )
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
                true
            }
            else -> false
        }
    }
}
