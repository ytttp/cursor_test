package com.example.launcher

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.DragEvent
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
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
    private var pendingTargetPage: Int? = null

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

        adapter = IconGridAdapter(pageIndex = argsPageIndex)

        binding.iconRecyclerView.apply {
            layoutManager = GridLayoutManager(context, LauncherPageConfig.PAGE_COLUMNS)
            adapter = this@LauncherPageFragment.adapter
            if (itemDecorationCount == 0) {
                addItemDecoration(GridSpacingItemDecoration(resources.getDimensionPixelSize(R.dimen.icon_gap)))
            }
            setOnDragListener(dragListener)
        }

        viewModel.icons.observe(viewLifecycleOwner) {
            adapter.submitIcons(viewModel.pageItems(argsPageIndex))
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding?.iconRecyclerView?.setOnDragListener(null)
        binding = null
    }

    private val dragListener = View.OnDragListener { _, event ->
        val payload = event.localState as? DragPayload ?: return@OnDragListener false
        when (event.action) {
            DragEvent.ACTION_DRAG_STARTED -> {
                if (payload.fromPage == argsPageIndex) {
                    adapter.onDragStarted(payload.icon.id)
                }
                true
            }

            DragEvent.ACTION_DRAG_LOCATION -> {
                handleEdgeAutoScroll(event)
                true
            }

            DragEvent.ACTION_DROP -> {
                handleDrop(payload, event)
                true
            }

            DragEvent.ACTION_DRAG_ENDED -> {
                adapter.onDragEnded()
                pendingTargetPage = null
                true
            }

            else -> true
        }
    }

    private fun handleDrop(payload: DragPayload, event: DragEvent) {
        val recyclerView = binding?.iconRecyclerView ?: return
        val targetPosition = calculateTargetPosition(recyclerView, event.x, event.y)
        viewModel.moveIcon(
            fromPage = payload.fromPage,
            fromPosition = payload.fromPosition,
            targetPage = argsPageIndex,
            targetPositionInPage = targetPosition
        )
        dragAcrossPageListener?.onDragReleasedToPage(argsPageIndex)
    }

    private fun calculateTargetPosition(recyclerView: RecyclerView, x: Float, y: Float): Int {
        val child = recyclerView.findChildViewUnder(x, y)
        return when {
            child != null -> recyclerView.getChildAdapterPosition(child).coerceAtLeast(0)
            y < 0 -> 0
            else -> adapter.iconCount()
        }.coerceIn(0, LauncherPageConfig.PAGE_SIZE)
    }

    private fun handleEdgeAutoScroll(event: DragEvent) {
        val recyclerView = binding?.iconRecyclerView ?: return
        val width = recyclerView.width
        if (width == 0) return

        val threshold = width * EDGE_ACTIVATION_RATIO
        val x = event.x
        val totalPages = viewModel.pageCount()

        val requestedPage = when {
            x > width - threshold && argsPageIndex < totalPages - 1 -> argsPageIndex + 1
            x < threshold && argsPageIndex > 0 -> argsPageIndex - 1
            else -> null
        }

        if (requestedPage != pendingTargetPage) {
            pendingTargetPage = requestedPage
            if (requestedPage != null) {
                dragAcrossPageListener?.onDragTowardPage(requestedPage)
            }
        }

        if (requestedPage == null) {
            pendingTargetPage = null
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
