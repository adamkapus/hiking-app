package com.adamkapus.hikingapp.ui.camera.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.adamkapus.hikingapp.databinding.RecognitionItemBinding
import com.adamkapus.hikingapp.ui.camera.Recognition
import com.adamkapus.hikingapp.utils.FlowerResolver

class RecognitionAdapter(private val ctx: Context) :
    ListAdapter<Recognition, RecognitionAdapter.RecognitionViewHolder>(RecognitionDiffUtil()) {

    private val flowerResolver: FlowerResolver = FlowerResolver()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecognitionViewHolder {
        val inflater = LayoutInflater.from(ctx)
        val binding = RecognitionItemBinding.inflate(inflater, parent, false)
        return RecognitionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecognitionViewHolder, position: Int) {
        val recognition: Recognition = getItem(position)
        //konvertálás megjelenítési névre a label névről
        holder.displayName.text =
            holder.itemView.context.getString(flowerResolver.getDisplayName(recognition.label))
        holder.probability.text = recognition.probabilityString;
    }

    private class RecognitionDiffUtil : DiffUtil.ItemCallback<Recognition>() {
        override fun areItemsTheSame(oldItem: Recognition, newItem: Recognition): Boolean {
            return oldItem.label == newItem.label
        }

        override fun areContentsTheSame(oldItem: Recognition, newItem: Recognition): Boolean {
            return oldItem.confidence == newItem.confidence
        }
    }

    inner class RecognitionViewHolder(private val binding: RecognitionItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val displayName: TextView = binding.recognitionDisplayName
        val probability: TextView = binding.recognitionProb
    }
}