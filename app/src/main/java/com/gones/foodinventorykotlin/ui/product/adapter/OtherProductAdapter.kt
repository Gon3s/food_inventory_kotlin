package com.gones.foodinventorykotlin.ui.product.adapter

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gones.foodinventorykotlin.R
import com.gones.foodinventorykotlin.databinding.ItemOtherProductBinding
import com.gones.foodinventorykotlin.domain.entity.Product
import java.util.Date

class OtherProductAdapter :
    ListAdapter<Product, OtherProductAdapter.OtherProductViewHolder>(DiffCallback()) {
    inner class OtherProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OtherProductViewHolder {
        return OtherProductViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_other_product,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: OtherProductViewHolder, position: Int) {
        val binding = ItemOtherProductBinding.bind(holder.itemView)
        val product = getItem(position)

        holder.itemView.apply {
            binding.productName.text = product.productName

            product.expiry_date?.let {
                binding.productDate.text = DateFormat.getDateFormat(context).format(Date(it))
            } ?: run {
                binding.productDate.text = context.getString(R.string.no_expiry_date)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Product, newItem: Product) =
            oldItem == newItem
    }
}