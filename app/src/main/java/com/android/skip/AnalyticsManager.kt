package com.android.skip

object AnalyticsManager {
    private var packageName = ""
    private var showToastCount = 0
    private var scanCount = 0
    private const val maxScanCount = 30

    fun isPerformScan(currentPackageName: String): Boolean {
        if (packageName != currentPackageName) {
            this.cleanScanCount()
            this.cleanShowToastCount()
            this.setPackageName(currentPackageName)
            return true
        } else if (scanCount <= maxScanCount) return true
        return false
    }

    fun isShowToast(): Boolean {
        return this.showToastCount == 0
    }

    private fun setPackageName(currentPackageName: String) {
        this.packageName = currentPackageName
    }

    fun setShowToastCount() {
        this.showToastCount = 1
    }

    private fun cleanShowToastCount() {
        this.showToastCount = 0
    }

    fun increaseScanCount() {
        this.scanCount += 1
    }

    private fun cleanScanCount() {
        this.scanCount = 0
    }
}