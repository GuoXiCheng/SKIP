package com.android.skip.ui.whitelist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WhiteListRepository @Inject constructor() {
    private val _whiteList: MutableList<String> = mutableListOf()

    fun updateWhiteList(packageName: String, checked: Boolean) {
        if (checked) {
            _whiteList.add(packageName)
        } else {
            _whiteList.remove(packageName)
        }
    }

    fun isAppInWhiteList(packageName: String): Boolean {
        return _whiteList.indexOf(packageName) != -1
    }
}