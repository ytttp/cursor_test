package com.example.launcher

import android.content.ClipData
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.launcher.databinding.ItemAppIconBinding

class IconGridAdapter(
    private val pageIndex: Int
) : RecyclerView.Adapter<IconGridAdapter.IconViewHolder>() {

    private val icons: MutableList<AppIcon> = mutableListOf()
    private var draggingItemId: Long? = null

    init {
        setHasStableIds(true)
    }

    fun submitIcons(data: List<AppIcon>) {
        icons.clear()
        icons.addAll(data)
        notifyDataSetChanged()
    }

    fun onDragStarted(itemId: Long) {
        draggingItemId = itemId
        notifyDataSetChanged()
    }

    fun onDragEnded() {
        draggingItemId = null
        notifyDataSetChanged()
    }

    fun iconCount(): Int = icons.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconViewHolder {
        val binding = ItemAppIconBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IconViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IconViewHolder, position: Int) {
        val icon = icons[position]
        holder.bind(icon)
        holder.setDragState(icon.id == draggingItemId)
    }

    override fun getItemCount(): Int = icons.size

    override fun getItemId(position: Int): Long = icons[position].id

    inner class IconViewHolder(
        private val binding: ItemAppIconBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.iconCard.setOnLongClickListener { view ->
                val position = bindingAdapterPosition
                if (position == RecyclerView.NO_POSITION) return@setOnLongClickListener false
                val icon = icons.getOrNull(position) ?: return@setOnLongClickListener false
                val payload = DragPayload(
                    icon = icon,
                    fromPage = pageIndex,
                    fromPosition = position
                )
                val clipData = ClipData.newPlainText(CLIP_LABEL, icon.id.toString())
                val shadow = View.DragShadowBuilder(view)
                val started = view.startDragAndDrop(
                    clipData,
                    shadow,
                    payload,
                    View.DRAG_FLAG_GLOBAL
                )
                if (started) {
                    onDragStarted(icon.id)
                }
                started
            }
        }

        fun bind(icon: AppIcon) = with(binding) {
            appIcon.setImageResource(icon.iconRes)
            appLabel.text = icon.label
            iconCard.setCardBackgroundColor(root.context.getColor(icon.backgroundColorRes))
        }

        fun setDragState(isDragging: Boolean) {
            val alpha = if (isDragging) 0.3f else 1f
            binding.root.alpha = alpha
        }
    }

    companion object {
        private const val CLIP_LABEL = "app_icon_drag"
    }
}
