package com.android.skip.data.config

import android.graphics.Rect
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.skip.R
import com.android.skip.data.network.MyApiNetwork
import com.android.skip.dataclass.ConfigLoadSchema
import com.android.skip.dataclass.ConfigPostSchema
import com.android.skip.dataclass.ConfigReadSchema
import com.android.skip.dataclass.ConfigState
import com.android.skip.dataclass.LoadSkipBound
import com.android.skip.dataclass.LoadSkipId
import com.android.skip.dataclass.LoadSkipText
import com.android.skip.dataclass.ReadClick
import com.android.skip.util.DataStoreUtils
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.StringUtils.getString
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.yaml.snakeyaml.Yaml
import java.net.URL
import java.security.MessageDigest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConfigReadRepository @Inject constructor(
    private val myApiNetwork: MyApiNetwork
) {
    private val _configPostState = MutableLiveData<ConfigPostSchema>()
    val configPostState: LiveData<ConfigPostSchema> = _configPostState

    suspend fun readConfig() = withContext(Dispatchers.IO) {
        val customContent = DataStoreUtils.getSyncData(
            getString(R.string.store_custom_config),
            getString(R.string.store_default_config)
        )
        getFromJson(getFromYaml(getFromUrl(customContent)))
    }

    private suspend fun getFromUrl(customContent: String): String {
        return try {
            val parsedUrl = URL(customContent)
            if (parsedUrl.protocol == "http" || parsedUrl.protocol == "https") {
                return myApiNetwork.fetchConfigFromUrl(customContent)
            }
            customContent
        } catch (e: Exception) {
            customContent
        }
    }

    private fun getFromYaml(customContent: String): String {
        return try {
            val yamlContent = Yaml().load<List<ConfigReadSchema>>(customContent)
            val gson = Gson()
            gson.toJson(yamlContent)
        } catch (e: Exception) {
            customContent
        }
    }

    private fun getFromJson(customContent: String): ConfigPostSchema {
        return try {
            val gson = Gson()
            val type = object : TypeToken<List<ConfigReadSchema>>() {}.type

            val configReadSchemaList = gson.fromJson<List<ConfigReadSchema>>(customContent, type)

            ConfigPostSchema(ConfigState.SUCCESS, md5(customContent), configReadSchemaList)
        } catch (e: Exception) {
            ConfigPostSchema(ConfigState.FAIL, "无效的配置", null)
        }
    }

    fun changeConfigPostState(configPostSchema: ConfigPostSchema) {
        _configPostState.postValue(configPostSchema)
    }

    fun handleConfig(configPostSchema: ConfigPostSchema): Map<String, ConfigLoadSchema>? {
        val screenWidth = ScreenUtils.getScreenWidth()
        val screenHeight = ScreenUtils.getScreenHeight()

        return configPostSchema.configReadSchemaList?.associate { config ->
            val newSkipTexts = config.skipTexts?.map { skipText ->
                val clickRect = skipText.click?.let { c ->
                    convertClick(c, screenWidth, screenHeight)
                }
                LoadSkipText(
                    text = skipText.text,
                    activityName = skipText.activityName,
                    length = skipText.length,
                    click = clickRect
                )
            }

            val newSkipIds = config.skipIds?.map { skipId ->
                val clickRect = skipId.click?.let { c ->
                    convertClick(c, screenWidth, screenHeight)
                }
                LoadSkipId(id = skipId.id, activityName = skipId.activityName, click = clickRect)
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
                    convertClick(c, screenWidth, screenHeight)
                }
                LoadSkipBound(
                    bound = clickBound,
                    activityName = skipBound.activityName,
                    click = clickRect
                )
            }

            config.packageName to ConfigLoadSchema(
                config.packageName,
                skipTexts = newSkipTexts,
                skipIds = newSkipIds,
                skipBounds = newSkipBounds
            )
        }
    }

    private fun convertRect(rect: Rect, resolution: String, widthA: Int, heightA: Int): Rect {
        val (widthB, heightB) = resolution.split("x").map { it.toFloat() }
        val ratioWidth = widthA / widthB
        val ratioHeight = heightA / heightB
        return Rect(
            (rect.left * ratioWidth).toInt(),
            (rect.top * ratioHeight).toInt(),
            (rect.right * ratioWidth).toInt(),
            (rect.bottom * ratioHeight).toInt()
        )
    }

    private fun convertClick(readClick: ReadClick, screenWidth: Int, screenHeight: Int): Rect {
        val (x, y) = readClick.position.split(",").map { it.toInt() }
        return convertRect(
            Rect(x, y, x + 1, y + 1),
            readClick.resolution,
            screenWidth,
            screenHeight
        )
    }

    private fun md5(input: String): String {
        val bytes = MessageDigest.getInstance("MD5").digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}