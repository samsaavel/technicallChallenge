package com.greenv.pt_upax_gccv.ux.pokemon.details

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.greenv.pt_upax_gccv.R
import com.greenv.pt_upax_gccv.databinding.ImageItemBinding

class ImageSliderAdapter(private val imageUrls: List<String>) :
    RecyclerView.Adapter<ImageSliderAdapter.ImageSliderViewHolder>() {

    inner class ImageSliderViewHolder(val binding: ImageItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageSliderViewHolder {
        val binding = ImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageSliderViewHolder(binding)
    }

    override fun getItemCount(): Int = imageUrls.size

    override fun onBindViewHolder(holder: ImageSliderViewHolder, position: Int) {
        val imageUrl = imageUrls[position]
        Log.d("*****ImageSlider", "imageUrl = ${imageUrl}")
        if (imageUrl.isNullOrEmpty()) {
            Glide.with(holder.binding.imageView.context)
                .load(R.drawable.psyduck)
                .into(holder.binding.imageView)
        } else {
            Glide.with(holder.binding.imageView.context)
                .load(imageUrl)
                .into(holder.binding.imageView)
        }

    }

}