package com.android.skip.ui.main.disclaimer

import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.android.skip.R
import io.noties.markwon.Markwon

@Composable
fun DisclaimerDialog(
    disclaimerViewModel: DisclaimerViewModel,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    val isShowDialog = disclaimerViewModel.isShowDialog.observeAsState()

    if (isShowDialog.value == true) {
        val markdownText = """
            # 免责声明

            感谢您信任并使用「SKIP」，为了保障您的权益，请在使用「SKIP」前，仔细阅读本免责声明。

            本项目遵循 GNU Affero 通用公共许可证第 3 版（AGPLv3）发布。在使用本项目的过程中，请注意以下事项：

            ## 一、无任何担保

            本项目“按现状”（as-is）提供，开发者不对其做出任何明示或暗示的担保。无论是适销性、特定用途的适用性，还是非侵权性，本项目均不做任何承诺。使用本项目的风险完全由您自己承担。

            ## 二、不承担任何责任

            在适用法律允许的最大范围内，项目的开发者、贡献者不对因使用或无法使用本项目或其衍生物而引起的任何形式的损失、损害或法律责任负责。这包括但不限于直接损失、间接损失、特殊损害、偶然损害或惩罚性赔偿。

            ## 三、知识产权声明

            本项目中所涉及的任何代码、文档或其他文件均受 AGPLv3 许可证的约束。您可以自由使用、修改和分发本项目，但必须保留原始许可条款，并且在适用的情况下，任何分发的衍生作品也必须遵循相同的许可证。

            ## 四、责任自负

            您在使用本项目的过程中，可能需要遵守您所在国家或地区的相关法律和规定。请确保您在使用、修改或分发本项目之前，已经了解并遵循所有适用的法律要求。项目的开发者和贡献者不承担您在使用本项目时产生的任何法律后果。

            ## 五、第三方依赖

            本项目包含来自第三方的开源组件或依赖项，这些第三方的许可条款可能与 AGPLv3 不同。请在使用这些组件时仔细阅读并遵守相应的许可证要求。

        """.trimIndent()
        val context = LocalContext.current
        val markwon = remember { Markwon.create(context) }
        AlertDialog(
            containerColor = MaterialTheme.colorScheme.background,
            onDismissRequest = { /*TODO*/ },
            confirmButton = {
                TextButton(onClick = onConfirm) {
                    Text(text = stringResource(id = R.string.dialog_approve))
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .heightIn(max = 500.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    AndroidView(
                        modifier = Modifier.padding(8.dp),
                        factory = { context ->
                            TextView(context).apply {
                                setLineSpacing(1.2f, 1.2f)
                                isSingleLine = false
                                layoutParams = ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                                )
                            }
                        },
                        update = { textView ->
                            markwon.setMarkdown(textView, markdownText)
                        }
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text(text = stringResource(id = R.string.dialog_reject))
                }
            }
        )
    }
}
