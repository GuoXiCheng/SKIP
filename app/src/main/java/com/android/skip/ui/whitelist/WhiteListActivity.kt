package com.android.skip.ui.whitelist

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.ui.res.stringResource
import com.android.skip.MyApp
import com.android.skip.R
import com.android.skip.ui.components.ResourceIcon
import com.android.skip.ui.components.ScaffoldPage
import com.android.skip.ui.components.expandMenuItems
import com.android.skip.ui.settings.theme.SwitchThemeViewModel
import com.android.skip.ui.theme.AppTheme
import com.android.skip.ui.webview.WebViewActivity
import com.android.skip.ui.whitelist.list.AppListColumn
import com.android.skip.ui.whitelist.list.AppListViewModel
import com.android.skip.ui.whitelist.system.ShowSystemButton
import com.android.skip.ui.whitelist.system.ShowSystemViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WhiteListActivity : AppCompatActivity() {
    private val appListViewModel by viewModels<AppListViewModel>()

    private val showSystemViewModel by viewModels<ShowSystemViewModel>()

    private val whiteListViewModel by viewModels<WhiteListViewModel>()

    private val switchThemeViewModel by viewModels<SwitchThemeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme(switchThemeViewModel) {
                ScaffoldPage(R.string.whitelist, { finish() }, {
                    AppListColumn(appListViewModel, whiteListViewModel)
                }, {
                    DropdownMenuItem(
                        leadingIcon = { ResourceIcon(iconResource = R.drawable.help) },
                        text = { Text(stringResource(id = R.string.whitelist_function_intro)) },
                        onClick = {
                            val intent = Intent(MyApp.context, WebViewActivity::class.java).apply {
                                putExtra("url", R.string.whitelist_function_intro_url)
                            }
                            startActivity(intent)
                            expandMenuItems = false
                        })
                    ShowSystemButton(showSystemViewModel)
                })
            }
        }

        showSystemViewModel.isShowSystem.observe(this) {
            appListViewModel.reloadData(it)
        }
    }
}