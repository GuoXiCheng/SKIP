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
import com.android.skip.util.DataStoreUtils
import com.blankj.utilcode.util.LogUtils
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
    private val _configPostState =
        MutableLiveData(ConfigPostSchema(ConfigState.FAIL, getString(R.string.invalid_config)))
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

            ConfigPostSchema(ConfigState.SUCCESS, md5(unicodeToChinese(customContent)), configReadSchemaList)
        } catch (e: Exception) {
            ConfigPostSchema(ConfigState.FAIL, getString(R.string.invalid_config), null)
        }
    }

    fun changeConfigPostState(configPostSchema: ConfigPostSchema) {
        _configPostState.postValue(configPostSchema)
    }

    fun handleConfig(configPostSchema: ConfigPostSchema): Map<String, ConfigLoadSchema>? {
        return try {
            configPostSchema.configReadSchemaList?.associate { config ->
                val newSkipTexts = config.skipTexts?.map { skipText ->
                    val clickRect = skipText.click?.let { c ->
                        convertClick(c)
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
                        convertClick(c)
                    }
                    LoadSkipId(
                        id = skipId.id,
                        activityName = skipId.activityName,
                        click = clickRect
                    )
                }

                val newSkipBounds = config.skipBounds?.map { skipBound ->
                    val clickRect = skipBound.click?.let { c ->
                        convertClick(c)
                    }
                    LoadSkipBound(
                        bound = convertBound(skipBound.bound),
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
        } catch (e: Exception) {
            LogUtils.e(e)
            null
        }
    }

    private fun convertClick(click: String): Rect {
        val (x, y) = click.split(",").map { it.toInt() }
        return Rect(x, y, x + 1, y + 1)
    }

    private fun convertBound(bound: String): Rect {
        val (left, top, right, bottom) = bound.split(",").map { it.toInt() }
        return Rect(left, top, right, bottom)
    }

    // #region MD5
    private fun md5(input: String): String {
        val bytes = MessageDigest.getInstance("MD5").digest(input.toByteArray(Charsets.UTF_8))
        return bytes.joinToString("") { "%02x".format(it) }
    }
    // #endregion MD5

    private fun unicodeToChinese(unicodeStr: String): String {
        val unicodeRegex = Regex("""\\u([0-9A-Fa-f]{4})""")

        return unicodeRegex.replace(unicodeStr) { matchResult ->
            val unicodeValue = matchResult.groupValues[1].toInt(16)
            unicodeValue.toChar().toString()
        }
    }
}