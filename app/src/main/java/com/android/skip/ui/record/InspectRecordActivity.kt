package com.android.skip.ui.record

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.android.skip.R
import com.android.skip.ui.components.ScaffoldPage
import com.android.skip.ui.record.list.InspectListColumn
import com.android.skip.ui.record.list.InspectListViewModel
import com.android.skip.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InspectRecordActivity : AppCompatActivity() {
    private val inspectListViewModel by viewModels<InspectListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                ScaffoldPage(R.string.inspect_record_title, { finish() }) {
                    InspectListColumn(inspectListViewModel)
                }
            }
        }
    }
}
