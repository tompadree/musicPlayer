package com.example.musicplayer.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.data.models.SessionObject
import com.example.musicplayer.databinding.ItemHomeBinding
import kotlinx.android.extensions.LayoutContainer

/**
 * @author Tomislav Curis
 */
class HomeAdapter
    : ListAdapter<SessionObject, HomeViewHolder>(HomeDiffUtil()) {

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder.from(parent)
    }
}

class HomeDiffUtil : DiffUtil.ItemCallback<SessionObject>() {
    override fun areContentsTheSame(oldItem: SessionObject, newItem: SessionObject): Boolean {
        return oldItem.listener_count == newItem.listener_count &&
                oldItem.current_track == newItem.current_track
    }

    override fun areItemsTheSame(oldItem: SessionObject, newItem: SessionObject): Boolean {
        return oldItem.name == newItem.name
    }
}

class HomeViewHolder private constructor(val binding: ItemHomeBinding)
    : RecyclerView.ViewHolder(binding.root), LayoutContainer {

    override val containerView = binding.root

    companion object {
        fun from(parent: ViewGroup): HomeViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemHomeBinding.inflate(layoutInflater, parent, false)

            return HomeViewHolder(binding)
        }
    }

    fun bind(sessionObject: SessionObject) {
        binding.sessionObject = sessionObject
        binding.executePendingBindings()
    }

}