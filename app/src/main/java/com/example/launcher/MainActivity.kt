package com.example.launcher

import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.example.launcher.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), LauncherPageFragment.DragAcrossPageListener {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: LauncherViewModel by viewModels()
    private lateinit var pagerAdapter: LauncherPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViewPager()
        setupIndicators()
    }

    private fun setupViewPager() {
        pagerAdapter = LauncherPagerAdapter(this, viewModel)
        binding.launcherViewPager.apply {
            adapter = pagerAdapter
            offscreenPageLimit = 1
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    highlightIndicator(position)
                }
            })
        }

        viewModel.icons.observe(this, Observer {
            pagerAdapter.notifyDataSetChanged()
            val pageCount = viewModel.pageCount()
            updateIndicatorDots(pageCount)
            val clamped = binding.launcherViewPager.currentItem.coerceIn(0, pageCount - 1)
            if (clamped != binding.launcherViewPager.currentItem) {
                binding.launcherViewPager.setCurrentItem(clamped, false)
            }
            highlightIndicator(clamped)
        })
    }

    private fun setupIndicators() {
        updateIndicatorDots(viewModel.pageCount())
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
        if (binding.pageIndicatorContainer.childCount == 0) return
        val safeIndex = pageIndex.coerceIn(0, binding.pageIndicatorContainer.childCount - 1)
        for (i in 0 until binding.pageIndicatorContainer.childCount) {
            val view = binding.pageIndicatorContainer.getChildAt(i)
            val active = i == safeIndex
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

    override fun onDragTowardPage(targetPage: Int) {
        binding.launcherViewPager.setCurrentItem(targetPage, false)
    }

    override fun onDragReleasedToPage(targetPage: Int) {
        binding.launcherViewPager.setCurrentItem(targetPage, false)
        highlightIndicator(targetPage)
    }
}
