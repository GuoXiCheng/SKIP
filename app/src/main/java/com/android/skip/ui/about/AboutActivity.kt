package com.android.skip.ui.about

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import com.android.skip.MyApp
import com.android.skip.R
import com.android.skip.ui.components.FlatButton
import com.android.skip.ui.components.RowContent
import com.android.skip.ui.components.ScaffoldPage
import com.android.skip.ui.theme.AppTheme
import com.android.skip.ui.webview.WebViewActivity

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                ScaffoldPage(R.string.about, { finish() },{
                    AboutGithub {
                        val intent = Intent(MyApp.context, WebViewActivity::class.java).apply {
                            putExtra("url", R.string.about_github_subtitle)
                        }
                        startActivity(intent)
                    }

                    AboutDocs {
                        val intent = Intent(MyApp.context, WebViewActivity::class.java).apply {
                            putExtra("url", R.string.about_docs_url)
                        }
                        startActivity(intent)
                    }
                })
            }
        }
    }
}

@Composable
fun AboutGithub(onClick: () -> Unit) {
    FlatButton(content = {
        RowContent(
            title = R.string.about_github_title,
            subTitle = R.string.about_github_subtitle
        )
    }, onClick = onClick)
}

@Composable
fun AboutDocs(onClick: () -> Unit) {
    FlatButton(content = {
        RowContent(
            title = R.string.about_docs_title,
            subTitle = R.string.about_docs_subtitle
        )
    }, onClick = onClick)
}