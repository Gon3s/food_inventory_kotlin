package com.gones.foodinventorykotlin.ui.home.adapter

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gones.foodinventorykotlin.R
import com.gones.foodinventorykotlin.databinding.ItemProductBinding
import com.gones.foodinventorykotlin.domain.entity.Product
import java.util.Date

class HomeAdapter :
    ListAdapter<Product, HomeAdapter.HomeViewHolder>(DiffCallback()) {
    inner class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_product,
                parent,
                false
            )
        )
    }

    private var onItemClickListener: ((Product) -> Unit)? = null

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val binding = ItemProductBinding.bind(holder.itemView)
        val product = getItem(position)

        holder.itemView.apply {
            Glide.with(this).load(product.imageUrl).into(binding.imageViewProduct)
            binding.productBrands.text = product.brands
            binding.productName.text = product.productName

            val dateFormat = DateFormat.getDateFormat(context)
            val date = Date(product.expiry_date)

            binding.productDate.text = dateFormat.format(date)

            setOnClickListener {
                onItemClickListener?.let { it(product) }
            }
        }
    }

    fun setOnItemClickListener(listener: (Product) -> Unit) {
        onItemClickListener = listener
    }

    class DiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Product, newItem: Product) =
            oldItem == newItem
    }
}