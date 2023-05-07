package com.example.mystoryapp.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.mystoryapp.data.model.ListStory
import com.example.mystoryapp.databinding.ActivityDetailBinding

@Suppress("DEPRECATION")
class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val detailStory: ListStory? = intent.getParcelableExtra(EXTRA_DETAIL)
        binding.apply {
            if (detailStory != null) {
                Glide.with(this@DetailActivity)
                    .load(detailStory.photoUrl)
                    .circleCrop()
                    .into(binding.ivDetailPhoto)
                tvDetailName.text = detailStory.name
                tvDetailDescription.text = detailStory.description
            }
        }
    }

    companion object {
        const val EXTRA_DETAIL = "EXTRA_DETAIL"
    }
}
