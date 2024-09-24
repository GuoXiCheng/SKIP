package com.android.skip.ui.about.version

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import com.android.skip.R
import com.android.skip.data.version.ApkVersionViewModel
import com.android.skip.ui.components.FlatButton
import com.android.skip.ui.components.RowContent
import com.android.skip.util.DataStoreUtils
import com.blankj.utilcode.util.StringUtils.getString

@Composable
fun ApkVersionButton(apkVersionViewModel: ApkVersionViewModel) {
    val versionPostState = apkVersionViewModel.versionPostState.observeAsState()

    FlatButton(content = {
        RowContent(
            title = R.string.about_app_version,
            subTitle = versionPostState.value?.value
        )
    }, {
        DataStoreUtils.removeSync(getString(R.string.store_not_update))
        apkVersionViewModel.checkVersion()
    })
}