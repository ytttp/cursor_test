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

    fun moveIcon(
        fromPage: Int,
        fromPosition: Int,
        targetPage: Int,
        targetPositionInPage: Int
    ) {
        val current = _icons.value?.toMutableList() ?: return
        if (current.isEmpty()) return

        val pageSize = LauncherPageConfig.PAGE_SIZE
        val totalSize = current.size
        val fromIndex = (fromPage * pageSize + fromPosition).coerceIn(0, totalSize - 1)
        val targetIndexBeforeRemoval = (targetPage * pageSize + targetPositionInPage).coerceIn(0, totalSize)

        val item = current.removeAt(fromIndex)

        var adjustedTargetIndex = targetIndexBeforeRemoval
        if (fromIndex < targetIndexBeforeRemoval) {
            adjustedTargetIndex--
        }
        if (adjustedTargetIndex < 0) adjustedTargetIndex = 0
        if (adjustedTargetIndex > current.size) adjustedTargetIndex = current.size

        current.add(adjustedTargetIndex, item)
        _icons.value = current
    }
}
