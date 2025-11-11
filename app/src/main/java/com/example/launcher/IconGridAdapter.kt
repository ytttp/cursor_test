package com.example.launcher

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.launcher.databinding.ItemAppIconBinding

class IconGridAdapter(
    private val onLongPress: (RecyclerView.ViewHolder) -> Unit
) : RecyclerView.Adapter<IconGridAdapter.IconViewHolder>() {

    private val icons: MutableList<AppIcon> = mutableListOf()

    init {
        setHasStableIds(true)
    }

    fun submitIcons(data: List<AppIcon>) {
        icons.clear()
        icons.addAll(data)
        notifyDataSetChanged()
    }

    fun swap(fromPosition: Int, toPosition: Int): Boolean {
        if (fromPosition == toPosition) return false
        if (fromPosition !in icons.indices || toPosition !in icons.indices) return false
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                java.util.Collections.swap(icons, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                java.util.Collections.swap(icons, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
        return true
    }

    fun getIcon(position: Int): AppIcon? = icons.getOrNull(position)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconViewHolder {
        val binding = ItemAppIconBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val holder = IconViewHolder(binding)
        binding.iconCard.setOnLongClickListener {
            onLongPress(holder)
            true
        }
        return holder
    }

    override fun onBindViewHolder(holder: IconViewHolder, position: Int) {
        holder.bind(icons[position])
    }

    override fun getItemCount(): Int = icons.size

    override fun getItemId(position: Int): Long = icons[position].id

    inner class IconViewHolder(
        private val binding: ItemAppIconBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(icon: AppIcon) = with(binding) {
            appIcon.setImageResource(icon.iconRes)
            appLabel.text = icon.label
            iconCard.setCardBackgroundColor(root.context.getColor(icon.backgroundColorRes))
        }
    }
}
