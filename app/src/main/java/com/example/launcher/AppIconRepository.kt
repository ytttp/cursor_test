package com.example.launcher

import androidx.annotation.ColorRes

object AppIconRepository {

    fun loadLaunchableApps(): MutableList<AppIcon> {
        val fakeApps = listOf(
            "天气", "日历", "相册", "邮件", "设置", "音乐", "视频", "记事本",
            "浏览器", "文件管理", "电话", "短信", "相机", "联系人", "闹钟", "地图",
            "社交", "支付", "阅读", "健康", "购物", "打车", "翻译", "体育",
            "股票", "理财", "外卖", "美食", "打车司机", "旅游", "课堂", "门户",
            "云盘", "新闻", "学习", "播客", "短视频", "社区", "游戏中心", "工具箱",
            "安全", "儿童空间", "主题", "个性化", "笔记", "录音机", "手电筒", "扫一扫"
        )

        val colorCycler = ColorCycler()
        var idCounter = 0L
        return fakeApps.map { label ->
            AppIcon(
                id = idCounter++,
                label = label,
                iconRes = R.mipmap.ic_launcher,
                backgroundColorRes = colorCycler.next()
            )
        }.toMutableList()
    }

    private class ColorCycler {
        private val colors: List<@ColorRes Int> = listOf(
            R.color.icon_background_1,
            R.color.icon_background_2,
            R.color.icon_background_3,
            R.color.icon_background_4,
            R.color.icon_background_5,
            R.color.icon_background_6,
            R.color.icon_background_7,
            R.color.icon_background_8
        )

        private var index = 0

        fun next(): Int {
            val color = colors[index % colors.size]
            index++
            return color
        }
    }
}
