package com.example.launcher

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.launcher.databinding.FragmentLauncherPageBinding

class LauncherPageFragment : Fragment(R.layout.fragment_launcher_page) {

    interface DragAcrossPageListener {
        fun onDragTowardPage(targetPage: Int)
        fun onDragReleasedToPage(targetPage: Int)
    }

    private val argsPageIndex: Int
        get() = requireArguments().getInt(ARG_PAGE_INDEX)

    private var dragAcrossPageListener: DragAcrossPageListener? = null

    private var binding: FragmentLauncherPageBinding? = null
    private val viewModel: LauncherViewModel by activityViewModels()

    private lateinit var adapter: IconGridAdapter
    private var itemTouchHelper: ItemTouchHelper? = null

    private var pendingTargetPage: Int? = null
    private var dragStartPosition: Int = RecyclerView.NO_POSITION

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dragAcrossPageListener = context as? DragAcrossPageListener
    }

    override fun onDetach() {
        super.onDetach()
        dragAcrossPageListener = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentLauncherPageBinding.bind(view)
        this.binding = binding

        adapter = IconGridAdapter { holder ->
            itemTouchHelper?.startDrag(holder)
        }

        binding.iconRecyclerView.apply {
            layoutManager = GridLayoutManager(context, LauncherPageConfig.PAGE_COLUMNS)
            adapter = this@LauncherPageFragment.adapter
            if (itemDecorationCount == 0) {
                addItemDecoration(GridSpacingItemDecoration(resources.getDimensionPixelSize(R.dimen.icon_gap)))
            }
        }

        setupTouchHelper(binding.iconRecyclerView)

        viewModel.icons.observe(viewLifecycleOwner) {
            adapter.submitIcons(viewModel.pageItems(argsPageIndex))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun setupTouchHelper(recyclerView: RecyclerView) {
        val callback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,
            0
        ) {
            override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                super.onSelectedChanged(viewHolder, actionState)
                if (actionState == ItemTouchHelper.ACTION_STATE_DRAG && viewHolder != null) {
                    dragStartPosition = viewHolder.bindingAdapterPosition
                    pendingTargetPage = null
                } else if (actionState == ItemTouchHelper.ACTION_STATE_IDLE) {
                    dragStartPosition = RecyclerView.NO_POSITION
                    pendingTargetPage = null
                }
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val fromPosition = viewHolder.bindingAdapterPosition
                val toPosition = target.bindingAdapterPosition
                if (fromPosition == RecyclerView.NO_POSITION || toPosition == RecyclerView.NO_POSITION) {
                    return false
                }
                val swapped = adapter.swap(fromPosition, toPosition)
                if (swapped) {
                    viewModel.moveWithinPage(argsPageIndex, fromPosition, toPosition)
                }
                return swapped
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // No swipe support.
            }

            override fun isLongPressDragEnabled(): Boolean = false

            override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
                super.clearView(recyclerView, viewHolder)
                pendingTargetPage?.let { target ->
                    if (target != argsPageIndex) {
                        if (dragStartPosition != RecyclerView.NO_POSITION) {
                            viewModel.moveToPage(
                                fromPage = argsPageIndex,
                                fromPosition = dragStartPosition.coerceAtLeast(0),
                                targetPage = target,
                                insertAtStart = target > argsPageIndex
                            )
                            dragAcrossPageListener?.onDragReleasedToPage(target)
                        }
                        dragStartPosition = RecyclerView.NO_POSITION
                    }
                }
                pendingTargetPage = null
                dragStartPosition = RecyclerView.NO_POSITION
            }

            override fun onChildDraw(
                c: android.graphics.Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                if (actionState != ItemTouchHelper.ACTION_STATE_DRAG || !isCurrentlyActive) return
                handleEdgeDragging(recyclerView, dX, viewHolder)
            }
        }

        itemTouchHelper = ItemTouchHelper(callback).also { it.attachToRecyclerView(recyclerView) }
    }

    private fun handleEdgeDragging(recyclerView: RecyclerView, dX: Float, viewHolder: RecyclerView.ViewHolder) {
        val width = recyclerView.width
        if (width == 0) return

        val view = viewHolder.itemView
        val draggedCenter = view.left + view.width / 2f + dX
        val threshold = width * EDGE_ACTIVATION_RATIO
        val effectivePage = pendingTargetPage ?: argsPageIndex
        val lastPageIndex = viewModel.pageCount() - 1

        val newTarget = when {
            draggedCenter > width - threshold && effectivePage < lastPageIndex ->
                effectivePage + 1
            draggedCenter < threshold && effectivePage > 0 ->
                effectivePage - 1
            else -> null
        }

        if (newTarget != pendingTargetPage) {
            pendingTargetPage = newTarget
            if (newTarget != null) {
                dragAcrossPageListener?.onDragTowardPage(newTarget)
            }
        }
    }

    companion object {
        private const val ARG_PAGE_INDEX = "page_index"
        private const val EDGE_ACTIVATION_RATIO = 0.15f

        fun newInstance(pageIndex: Int): LauncherPageFragment {
            return LauncherPageFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PAGE_INDEX, pageIndex)
                }
            }
        }
    }
}
