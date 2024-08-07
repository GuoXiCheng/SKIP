package com.android.skip

import android.app.Application
import com.blankj.utilcode.util.Utils

class SKIPApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Utils.init(this)
    }
}