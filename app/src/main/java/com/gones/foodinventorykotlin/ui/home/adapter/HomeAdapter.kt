package com.gones.foodinventorykotlin.ui.home.adapter

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gones.foodinventorykotlin.R
import com.gones.foodinventorykotlin.databinding.ItemProductBinding
import com.gones.foodinventorykotlin.domain.entity.Product
import timber.log.Timber
import java.util.Date

class HomeAdapter : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {
    inner class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_product,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        Timber.d("DLOG:: getItemCount: ${differ.currentList.size}")
        return differ.currentList.size
    }

    private var onItemClickListener: ((Product) -> Unit)? = null

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        Timber.d("DLOG:: onBindViewHolder: ${differ.currentList[position].productName}")
        val binding = ItemProductBinding.bind(holder.itemView)
        val product = differ.currentList[position]

        holder.itemView.apply {
            Glide.with(this).load(product.imageUrl).into(binding.imageViewProduct)
            binding.productBrands.text = product.brands
            binding.productName.text = product.productName

            Timber.d("DLOG:: onBindViewHolder: Date ${product.expiries_dates}")

            val expiriesDates: MutableList<String> = mutableListOf()
            product.expiries_dates?.let {
                it.split(",").forEach { date ->
                    val dateFormat = DateFormat.getDateFormat(context)
                    expiriesDates += dateFormat.format(Date(date.toLong()))
                }
            }

            binding.productDate.text = expiriesDates.joinToString(", ")

            product.quantity?.let {
                binding.productQuantity.text = it.toString()
            }

            setOnClickListener {
                onItemClickListener?.let { it(product) }
            }
        }
    }

    fun setOnItemClickListener(listener: (Product) -> Unit) {
        onItemClickListener = listener
    }
}