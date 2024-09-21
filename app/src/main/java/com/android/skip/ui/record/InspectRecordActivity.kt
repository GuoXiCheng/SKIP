package com.android.skip.ui.record

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.res.stringResource
import androidx.core.content.FileProvider
import com.android.skip.MyApp
import com.android.skip.R
import com.android.skip.service.AccessibilityInspectViewModel
import com.android.skip.ui.components.ScaffoldPage
import com.android.skip.ui.inspect.record.InspectRecordViewModel
import com.android.skip.ui.record.dialog.JpegDialog
import com.android.skip.ui.record.dialog.JpegDialogViewModel
import com.android.skip.ui.record.list.InspectListColumn
import com.android.skip.ui.record.list.InspectListViewModel
import com.android.skip.ui.settings.theme.SwitchThemeViewModel
import com.android.skip.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class InspectRecordActivity : AppCompatActivity() {
    private val inspectListViewModel by viewModels<InspectListViewModel>()

    private val jpegDialogViewModel by viewModels<JpegDialogViewModel>()

    private val accessibilityInspectViewModel by viewModels<AccessibilityInspectViewModel>()

    private val inspectRecordViewModel by viewModels<InspectRecordViewModel> ()

    private val switchThemeViewModel by viewModels<SwitchThemeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme(switchThemeViewModel) {
                ScaffoldPage(R.string.inspect_record_title, { finish() }, {
                    JpegDialog(jpegDialogViewModel)
                    InspectListColumn(inspectListViewModel) { fileId, menuType ->
                        when (menuType) {
                            R.string.record_look.toString() -> jpegDialogViewModel.changeJpegFileId(
                                fileId
                            )

                            R.string.record_share.toString() -> {
                                val file = File("${MyApp.context.filesDir}/capture/${fileId}.zip")
                                val uri = FileProvider.getUriForFile(
                                    this,
                                    "${packageName}.fileprovider",
                                    file
                                )
                                val shareIntent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    type = "application/zip"
                                    putExtra(Intent.EXTRA_STREAM, uri)
                                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                }
                                startActivity(
                                    Intent.createChooser(
                                        shareIntent,
                                        getString(R.string.record_please_choose)
                                    )
                                )
                            }

                            R.string.record_delete.toString() -> inspectListViewModel.changeDeleteFileId(
                                fileId
                            )
                        }
                    }
                }, {
                    DropdownMenuItem(
                        leadingIcon = { Icon(Icons.Outlined.Delete, contentDescription = null) },
                        text = { Text(stringResource(id = R.string.record_delete_all)) },
                        onClick = {
                            inspectListViewModel.deleteAllFile()
                            inspectListViewModel.reloadData()
                            inspectRecordViewModel.changeZipFileCount()
                        })
                })
            }
        }

        inspectListViewModel.delFileId.observe(this) {
            inspectListViewModel.deleteByFileId(it)
            inspectListViewModel.reloadData()
        }

        accessibilityInspectViewModel.accessibilityInspectSuccess.observe(this) {
            inspectListViewModel.reloadData()
        }
    }
}
