package com.android.skip.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.WindowManager
import com.android.skip.R
import com.android.skip.util.DebounceHelper
import com.android.skip.util.MyToast
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ServiceUtils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FloatingWindowService : Service() {

    private lateinit var windowManager: WindowManager

    private lateinit var floatingView: android.view.View

    @Inject
    lateinit var accessibilityInspectRepository: AccessibilityInspectRepository

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate() {
        super.onCreate()
        // 获取 WindowManager 服务
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        // 加载悬浮窗布局
        floatingView = LayoutInflater.from(this).inflate(R.layout.floating_window, null)

        // 设置悬浮窗参数
        val layoutParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            android.graphics.PixelFormat.TRANSLUCENT
        )

        // 设置悬浮窗的位置
        layoutParams.gravity = Gravity.TOP or Gravity.START
        layoutParams.x = 300
        layoutParams.y = 1000

        // 将悬浮窗添加到 WindowManager
        windowManager.addView(floatingView, layoutParams)

        // 记录触摸事件的起始位置
        var initialX = 0
        var initialY = 0
        var initialTouchX = 0f
        var initialTouchY = 0f
        var isDragging = false

        // 设置触摸监听器
        floatingView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // 记录初始位置
                    initialX = layoutParams.x
                    initialY = layoutParams.y
                    initialTouchX = event.rawX
                    initialTouchY = event.rawY
                    isDragging = false
                    true
                }

                MotionEvent.ACTION_MOVE -> {
                    // 计算移动距离
                    val deltaX = (event.rawX - initialTouchX).toInt()
                    val deltaY = (event.rawY - initialTouchY).toInt()

                    // 更新悬浮窗位置
                    layoutParams.x = initialX + deltaX
                    layoutParams.y = initialY + deltaY
                    windowManager.updateViewLayout(floatingView, layoutParams)

                    // 标记为拖动事件
                    if (deltaX != 0 || deltaY != 0) {
                        isDragging = true
                    }
                    true
                }

                MotionEvent.ACTION_UP -> {
                    // 如果未发生明显移动，则认为是点击事件
                    if (!isDragging) {
                        floatingView.performClick()
                    }
                    true
                }

                else -> false
            }
        }

        val debounceHelper = DebounceHelper(1000) // 1秒防抖间隔
        floatingView.setOnClickListener {
            debounceHelper.run {
                val isServiceRunning = ServiceUtils.isServiceRunning(InspectService::class.java)
                if (isServiceRunning) {
                    accessibilityInspectRepository.startAccessibilityInspect()
                } else {
                    MyToast.show(R.string.toast_start_inspect)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // 移除悬浮窗
        if (::floatingView.isInitialized) {
            windowManager.removeView(floatingView)
        }
    }
}