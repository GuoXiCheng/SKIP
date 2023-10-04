package com.android.skip.manager


object AnalyticsManager {
    private var packageName = ""
    private var showToastCount = 0
    private var scanCount = 0
    private const val maxScanCount = 20

    fun isPerformScan(currentPackageName: String): Boolean {
        if (packageName != currentPackageName) {
            cleanScanCount()
            cleanShowToastCount()
            setPackageName(currentPackageName)
            return true
        } else if (scanCount <= maxScanCount) return true
        return false
    }

    fun isShowToast(): Boolean {
        return showToastCount == 0
    }

    private fun setPackageName(currentPackageName: String) {
        packageName = currentPackageName
    }

    fun setShowToastCount() {
        showToastCount = 1
    }

    private fun cleanShowToastCount() {
        showToastCount = 0
    }

    fun increaseScanCount() {
        scanCount += 1
    }

    private fun cleanScanCount() {
        scanCount = 0
    }
}