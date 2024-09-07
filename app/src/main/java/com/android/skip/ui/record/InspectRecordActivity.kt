package com.android.skip.ui.record

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.core.content.FileProvider
import com.android.skip.MyApp
import com.android.skip.R
import com.android.skip.ui.components.ScaffoldPage
import com.android.skip.ui.record.list.InspectListColumn
import com.android.skip.ui.record.list.InspectListViewModel
import com.android.skip.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class InspectRecordActivity : AppCompatActivity() {
    private val inspectListViewModel by viewModels<InspectListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                ScaffoldPage(R.string.inspect_record_title, { finish() }, {
                    InspectListColumn(inspectListViewModel) {
                        val file = File("${MyApp.context.filesDir}/capture/${it}.zip")
                        val uri = FileProvider.getUriForFile(this, "${packageName}.fileprovider", file)
                        val shareIntent = Intent().apply{
                            action = Intent.ACTION_SEND
                            type = "application/zip"
                            putExtra(Intent.EXTRA_STREAM, uri)
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        }
                        startActivity(Intent.createChooser(shareIntent, "选择一个应用来分享文件"))
                    }
                }) {
                    DropdownMenuItem(
                        text = { Text(text = "全部删除") },
                        onClick = { /*TODO*/ }
                    )
                }
            }
        }
    }
}
