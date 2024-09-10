package com.android.skip.data

import android.content.Context
import android.graphics.Rect
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.skip.dataclass.ConfigLoadSchema
import com.android.skip.dataclass.ConfigReadSchema
import com.android.skip.dataclass.LoadSkipBound
import com.android.skip.dataclass.LoadSkipId
import com.android.skip.dataclass.LoadSkipText
import com.blankj.utilcode.util.ScreenUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.yaml.snakeyaml.Yaml
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.ceil
import kotlin.math.floor

@Singleton
class ConfigReadRepository @Inject constructor() {
    private val _configHashCode = MutableLiveData<Int>()
    val configHashCode: LiveData<Int> = _configHashCode

    private lateinit var configReadSchemaList: List<ConfigReadSchema>

    fun readConfig(context: Context) {
        val yamlContent = context.assets.open("skip_config_v3.yaml").use { input->
            val yaml = Yaml().load<List<ConfigReadSchema>>(input)
            yaml
        }

        val gson = Gson()
        val jsonStr = gson.toJson(yamlContent)
        val type = object : TypeToken<List<ConfigReadSchema>>() {}.type
        configReadSchemaList = gson.fromJson(jsonStr, type)

        _configHashCode.postValue(jsonStr.hashCode())
    }

    fun handleConfig(): Map<String, ConfigLoadSchema> {
        val screenWidth = ScreenUtils.getScreenWidth()
        val screenHeight = ScreenUtils.getScreenHeight()

        return configReadSchemaList.associate { config ->
            val newSkipTexts = config.skipTexts?.map { skipText ->
                val clickRect = skipText.click?.let { c ->
                    val (x, y) = c.position.split(",").map { it.toInt() }
                    convertRect(
                        Rect(x, y, x + 1, y + 1),
                        c.resolution,
                        screenWidth,
                        screenHeight
                    )
                }
                LoadSkipText(text = skipText.text, length = skipText.length, click = clickRect)
            }

            val newSkipIds = config.skipIds?.map { skipId ->
                val clickRect = skipId.click?.let { c ->
                    val (x, y) = c.position.split(",").map { it.toInt() }
                    convertRect(
                        Rect(x, y, x + 1, y + 1),
                        c.resolution,
                        screenWidth,
                        screenHeight
                    )
                }
                LoadSkipId(id = skipId.id, click = clickRect)
            }

            val newSkipBounds = config.skipBounds?.map { skipBound ->
                val (left, top, right, bottom) = skipBound.bound.split(",").map { it.toInt() }
                val clickBound = convertRect(
                    Rect(left, top, right, bottom),
                    skipBound.resolution,
                    screenWidth,
                    screenHeight
                )
                val clickRect = skipBound.click?.let { c ->
                    val (x, y) = c.position.split(",").map { it.toInt() }
                    convertRect(
                        Rect(x, y, x + 1, y + 1),
                        c.resolution,
                        screenWidth,
                        screenHeight
                    )
                }
                LoadSkipBound(bound = clickBound, click=clickRect)
            }

            config.activityName to ConfigLoadSchema(
                config.activityName,
                skipTexts = newSkipTexts,
                skipIds = newSkipIds,
                skipBounds = newSkipBounds
            )
        }
    }

    private fun convertRect(rect: Rect, resolution: String, widthA: Int, heightA: Int): Rect {
        val (widthB, heightB) = resolution.split("x").map { it.toInt() }
        val ratioWidth = widthB.toFloat() / widthA
        val ratioHeight = heightB.toFloat() / heightA
        return Rect(
            floor(rect.left * ratioWidth).toInt(),
            floor(rect.top * ratioHeight).toInt(),
            ceil(rect.right * ratioWidth).toInt(),
            ceil(rect.bottom * ratioHeight).toInt()
        )
    }
}