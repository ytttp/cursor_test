package com.example.launcher

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.launcher.LauncherPageConfig.PAGE_ROWS
import com.example.launcher.LauncherPageConfig.PAGE_SIZE
import com.example.launcher.databinding.ActivityMainBinding
import kotlin.math.ceil

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appsAdapter: AppsAdapter
    private lateinit var itemTouchHelper: ItemTouchHelper
    private val pagerSnapHelper = PagerSnapHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
        setupPagerIndicators()
    }

    private fun setupRecyclerView() {
        appsAdapter = AppsAdapter { viewHolder ->
            itemTouchHelper.startDrag(viewHolder)
        }
        appsAdapter.submitItems(AppIconRepository.loadLaunchableApps())

        val gridLayoutManager = GridLayoutManager(
            this,
            PAGE_ROWS,
            RecyclerView.HORIZONTAL,
            false
        )

        binding.launcherRecyclerView.apply {
            layoutManager = gridLayoutManager
            adapter = appsAdapter
            setHasFixedSize(true)
            addItemDecoration(GridSpacingItemDecoration(resources.getDimensionPixelSize(R.dimen.icon_gap)))
        }

        pagerSnapHelper.attachToRecyclerView(binding.launcherRecyclerView)

        itemTouchHelper = ItemTouchHelper(createDragCallback())
        itemTouchHelper.attachToRecyclerView(binding.launcherRecyclerView)

        binding.launcherRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    updateIndicatorForVisiblePage()
                }
            }
        })
    }

    private fun setupPagerIndicators() {
        val pages = ceil(appsAdapter.itemCount / PAGE_SIZE.toDouble()).toInt().coerceAtLeast(1)
        updateIndicatorDots(pages)
        highlightIndicator(0)
    }

    private fun updateIndicatorDots(pageCount: Int) {
        binding.pageIndicatorContainer.removeAllViews()
        repeat(pageCount) { index ->
            binding.pageIndicatorContainer.addView(createIndicatorDot(index))
        }
    }

    private fun createIndicatorDot(index: Int): ImageView {
        val dotSize = resources.getDimensionPixelSize(R.dimen.indicator_size)
        val dotMargin = resources.getDimensionPixelSize(R.dimen.indicator_margin)
        return ImageView(this).apply {
            layoutParams = LinearLayout.LayoutParams(dotSize, dotSize).also { params ->
                params.setMargins(dotMargin, dotMargin, dotMargin, dotMargin)
            }
            setImageResource(R.drawable.indicator_dot)
            ViewCompat.setElevation(this, 2f)
            setColorFilter(ContextCompat.getColor(this@MainActivity, if (index == 0) R.color.indicator_active else R.color.indicator_inactive))
        }
    }

    private fun highlightIndicator(pageIndex: Int) {
        for (i in 0 until binding.pageIndicatorContainer.childCount) {
            val view = binding.pageIndicatorContainer.getChildAt(i)
            val active = i == pageIndex
            val tintColor = if (active) {
                ContextCompat.getColor(this, R.color.indicator_active)
            } else {
                ContextCompat.getColor(this, R.color.indicator_inactive)
            }
            view.alpha = if (active) 1f else 0.6f
            view.scaleX = if (active) 1.1f else 1f
            view.scaleY = if (active) 1.1f else 1f
            if (view is ImageView) {
                view.setColorFilter(tintColor)
            }
        }
    }

    private fun createDragCallback(): ItemTouchHelper.Callback {
        return object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,
            0
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val from = viewHolder.bindingAdapterPosition
                val to = target.bindingAdapterPosition
                val moved = appsAdapter.moveItem(from, to)
                if (moved) {
                    recyclerView.post { updateIndicatorForVisiblePage() }
                }
                return moved
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // No swipe actions supported.
            }

            override fun isLongPressDragEnabled(): Boolean = false

            override fun canDropOver(
                recyclerView: RecyclerView,
                current: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                return makeMovementFlags(dragFlags, 0)
            }
        }
    }

    private fun updateIndicatorForVisiblePage() {
        val layoutManager = binding.launcherRecyclerView.layoutManager as? GridLayoutManager ?: return
        val snapView = pagerSnapHelper.findSnapView(layoutManager) ?: return
        val position = binding.launcherRecyclerView.getChildAdapterPosition(snapView)
        if (position == RecyclerView.NO_POSITION) return
        val pageIndex = position / PAGE_SIZE
        highlightIndicator(pageIndex)
    }
}
