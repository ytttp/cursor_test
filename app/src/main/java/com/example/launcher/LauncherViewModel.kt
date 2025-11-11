package com.example.launcher

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LauncherViewModel : ViewModel() {

    private val _icons = MutableLiveData<List<AppIcon>>(AppIconRepository.loadLaunchableApps())
    val icons: LiveData<List<AppIcon>> = _icons

    fun pageCount(): Int {
        val total = _icons.value?.size ?: 0
        return if (total == 0) 1 else (total + LauncherPageConfig.PAGE_SIZE - 1) / LauncherPageConfig.PAGE_SIZE
    }

    fun pageItems(pageIndex: Int): List<AppIcon> {
        val current = _icons.value.orEmpty()
        val fromIndex = (pageIndex * LauncherPageConfig.PAGE_SIZE).coerceAtMost(current.size)
        val toIndex = ((pageIndex + 1) * LauncherPageConfig.PAGE_SIZE).coerceAtMost(current.size)
        return if (fromIndex < toIndex) current.subList(fromIndex, toIndex) else emptyList()
    }

    fun moveWithinPage(pageIndex: Int, fromPosition: Int, toPosition: Int) {
        val current = _icons.value?.toMutableList() ?: return
        val from = pageIndex * LauncherPageConfig.PAGE_SIZE + fromPosition
        val to = pageIndex * LauncherPageConfig.PAGE_SIZE + toPosition
        if (from !in current.indices || to !in current.indices) return
        val item = current.removeAt(from)
        val adjustedTo = when {
            from < to -> (to - 1).coerceAtMost(current.size)
            from > to -> to.coerceAtLeast(0)
            else -> to
        }
        val insertIndex = adjustedTo.coerceIn(0, current.size)
        current.add(insertIndex, item)
        _icons.value = current
    }

    fun moveToPage(
        fromPage: Int,
        fromPosition: Int,
        targetPage: Int,
        insertAtStart: Boolean
    ) {
        val current = _icons.value?.toMutableList() ?: return
        val from = fromPage * LauncherPageConfig.PAGE_SIZE + fromPosition
        if (from !in current.indices) return
        val item = current.removeAt(from)

        val targetStart = (targetPage * LauncherPageConfig.PAGE_SIZE).coerceAtMost(current.size)
        val itemsRemaining = (current.size - targetStart).coerceAtLeast(0)
        val targetCount = itemsRemaining.coerceAtMost(LauncherPageConfig.PAGE_SIZE)

        val insertionIndex = if (insertAtStart || targetCount == 0) {
            targetStart
        } else {
            (targetStart + targetCount - 1).coerceIn(targetStart, current.size)
        }

        val index = insertionIndex.coerceIn(0, current.size)
        current.add(index, item)
        _icons.value = current
    }
}
