package com.android.skip.ui.whitelist

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.android.skip.R
import com.android.skip.ui.components.ScaffoldPage
import com.android.skip.ui.theme.AppTheme
import com.android.skip.ui.whitelist.list.AppListColumn
import com.android.skip.ui.whitelist.list.AppListViewModel
import com.android.skip.ui.whitelist.system.ShowSystemButton
import com.android.skip.ui.whitelist.system.ShowSystemViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WhiteListActivity : AppCompatActivity() {
    private val appListViewModel by viewModels<AppListViewModel>()

    private val showSystemViewModel by viewModels<ShowSystemViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                ScaffoldPage(R.string.whitelist, { finish() }, {
                    AppListColumn(appListViewModel)
                }, {
                    ShowSystemButton(showSystemViewModel)
                })
            }
        }

        showSystemViewModel.isShowSystem.observe(this) {
            appListViewModel.reloadData(it)
        }
    }
}