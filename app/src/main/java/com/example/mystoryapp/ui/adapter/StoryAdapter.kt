package com.example.mystoryapp.ui.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mystoryapp.data.model.ListStory
import com.example.mystoryapp.databinding.RowStoryItemBinding
import com.example.mystoryapp.ui.activity.DetailActivity

class StoryAdapter : PagingDataAdapter<ListStory, StoryAdapter.ViewHolder>(StoryDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RowStoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val story = getItem(position)
        if (story != null) {
            holder.bind(story)
        }
    }

    inner class ViewHolder(private val binding: RowStoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStory) {
            binding.apply {
                Glide.with(itemView.context)
                    .load(story.photoUrl)
                    .circleCrop()
                    .into(ivStory)
                tvName.text = story.name
                tvDescription.text = story.description

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DetailActivity::class.java)
                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            itemView.context as Activity,
                            Pair(ivStory, "image"),
                            Pair(tvName, "name"),
                            Pair(tvDescription, "description"),
                        )
                    intent.putExtra(DetailActivity.EXTRA_DETAIL, story)
                    itemView.context.startActivity(intent, optionsCompat.toBundle())
                }
            }
        }
    }

    companion object StoryDiffCallback : DiffUtil.ItemCallback<ListStory>() {
        override fun areItemsTheSame(oldItem: ListStory, newItem: ListStory): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ListStory, newItem: ListStory): Boolean {
            return oldItem == newItem
        }
    }
}
