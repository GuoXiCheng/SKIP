package com.android.skip

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.android.skip.compose.FlatButton
import com.android.skip.compose.RowContent
import com.android.skip.compose.ScaffoldPage
import com.android.skip.dataclass.AppInfo
import com.android.skip.manager.WhitelistManager
import com.android.skip.utils.DataStoreUtils
import com.android.skip.utils.InstalledAppUtils
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WhitelistActivity : BaseActivity() {

    @Composable
    override fun ProvideContent() {
        WhitelistInterface(onBackClick = {
            finish()
        })

    }
}

@Composable
fun WhitelistInterface(onBackClick: () -> Unit) {
    val context = LocalContext.current
    val appInfoList = remember { mutableStateListOf<AppInfo>() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch(Dispatchers.IO) {
            val installedApps = InstalledAppUtils.getCachedApps(
                DataStoreUtils.getSyncData(
                    SKIP_INCLUDE_SYSTEM_APPS,
                    false
                )
            )

            val apps = installedApps.map { app ->
                AppInfo(
                    appName = app.name,
                    packageName = app.packageName,
                    appIcon = app.icon,
                    checked = mutableStateOf(
                        DataStoreUtils.getSyncData(
                            WHITELIST_DOT + app.packageName,
                            false
                        )
                    )
                )
            }.sortedWith(compareByDescending { it.checked.value })
            withContext(Dispatchers.Main) {
                appInfoList.addAll(apps)
            }
        }
    }

    ScaffoldPage(
        barTitle = stringResource(id = R.string.whitelist),
        onBackClick = onBackClick,
        content = {
            LazyColumn {
                items(appInfoList.size) { index ->
                    FlatButton(content = {
                        RowContent(
                            title = appInfoList[index].appName,
                            subTitle = appInfoList[index].packageName,
                            {
                                Icon(
                                    painter = rememberDrawablePainter(drawable = appInfoList[index].appIcon),
                                    contentDescription = null,
                                    tint = Color.Unspecified
                                )
                            },
                            checked = appInfoList[index].checked.value,
                            {
                                appInfoList[index].checked.value = it
                                val key = WHITELIST_DOT + appInfoList[index].packageName
                                if (it) DataStoreUtils.putSyncData(
                                    key,
                                    true
                                ) else DataStoreUtils.removeSync(key)
                                WhitelistManager.setWhitelist(scope, context)
                            }
                        )
                    })
                }
            }
        })
}