package com.android.skip

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.android.skip.manager.RectManager
import com.android.skip.ui.theme.AppTheme
import com.android.skip.utils.DataStoreUtils

const val SKIP_APP_THEME = "SKIP_APP_THEME"

const val SKIP_AUTO_SYNC_CONFIG = "SKIP_AUTO_SYNC_CONFIG"

const val SKIP_AUTO_CHECK_UPDATE = "SKIP_AUTO_CHECK_UPDATE"

const val WHITELIST_DOT = "whitelist."

const val SKIP_PERMIT_NOTICE = "SKIP_PERMIT_NOTICE"

val themeTypeState: MutableState<Int> by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
    mutableStateOf(DataStoreUtils.getSyncData(SKIP_APP_THEME, Configuration.UI_MODE_NIGHT_NO))
}

val permitNoticeState: MutableState<Boolean> by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
    mutableStateOf(DataStoreUtils.getSyncData(SKIP_PERMIT_NOTICE, false))
}

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataStoreUtils.init(applicationContext)
        setContent {
            AppTheme(darkTheme = isDarkTheme()) {
                ProvideContent()
            }
        }

        RectManager.setMaxRect(this)

        // 清理临时文件
        val directory = this.getExternalFilesDir(null)
        directory?.let {
            it.listFiles()?.forEach { file ->
                file.delete()
            }
        }
    }

    @Composable
    abstract fun ProvideContent()

    private fun isDarkTheme(): Boolean {
        return when (themeTypeState.value) {
            Configuration.UI_MODE_NIGHT_YES -> true
            Configuration.UI_MODE_NIGHT_NO -> false
            Configuration.UI_MODE_NIGHT_UNDEFINED ->
                (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
            else -> false
        }
    }

}