package com.example.launcher

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.example.launcher.databinding.ItemAppIconBinding
import com.example.launcher.LauncherPageConfig.PAGE_COLUMNS
import com.example.launcher.LauncherPageConfig.PAGE_ROWS
import kotlin.math.roundToInt

class AppsAdapter(
    private val onStartDrag: (RecyclerView.ViewHolder) -> Unit
) : RecyclerView.Adapter<AppsAdapter.AppIconViewHolder>() {

    private val items: MutableList<AppIcon> = mutableListOf()

    init {
        setHasStableIds(true)
    }

    fun submitItems(appIcons: List<AppIcon>) {
        items.clear()
        items.addAll(appIcons)
        notifyDataSetChanged()
    }

    fun getItems(): List<AppIcon> = items

    fun moveItem(fromPosition: Int, toPosition: Int): Boolean {
        if (fromPosition == toPosition) return false
        if (fromPosition !in items.indices || toPosition !in items.indices) return false
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                java.util.Collections.swap(items, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                java.util.Collections.swap(items, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
        return true
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppIconViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAppIconBinding.inflate(inflater, parent, false)
        val displayMetrics = parent.context.resources.displayMetrics
        val width = parent.width.takeIf { it > 0 } ?: displayMetrics.widthPixels
        val height = parent.height.takeIf { it > 0 } ?: (displayMetrics.heightPixels * 0.65).roundToInt()

        val itemWidth = width / PAGE_COLUMNS
        val itemHeight = height / PAGE_ROWS
        binding.root.updateLayoutParams<ViewGroup.LayoutParams> {
            this.width = itemWidth
            this.height = itemHeight
        }

        val holder = AppIconViewHolder(binding)
        binding.iconCard.setOnLongClickListener {
            onStartDrag(holder)
            true
        }
        return holder
    }

    override fun onBindViewHolder(holder: AppIconViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    override fun getItemId(position: Int): Long = items[position].id

    inner class AppIconViewHolder(
        private val binding: ItemAppIconBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(appIcon: AppIcon) = with(binding) {
            setAppIcon(appIcon.iconRes)
            appLabel.text = appIcon.label
            iconCard.setCardBackgroundColor(
                root.context.getColor(appIcon.backgroundColorRes)
            )
        }

        private fun setAppIcon(@androidx.annotation.DrawableRes icon: Int) {
            binding.appIcon.setImageResource(icon)
        }
    }
}
