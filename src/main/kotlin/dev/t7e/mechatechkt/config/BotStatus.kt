package dev.t7e.mechatechkt.config

import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import java.io.File

/**
 * Created by testusuke on 2023/08/02
 * @author testusuke
 */
@Serializable
data class BotStatus(
    var progressReportChannel: ULong = "0".toULong(),
    var enabledProgressReport: Boolean = "false".toBoolean()
) {
    companion object {
        private val yaml = Yaml(configuration = YamlConfiguration(encodeDefaults = true, strictMode = false))

        val config: BotStatus = File("config/bot_status.yml").let { file ->
            if (!file.parentFile.exists()) file.parentFile.mkdirs()
            if (!file.exists()) file.writeText(yaml.encodeToString(BotStatus()))
            yaml.decodeFromString(serializer(), file.readText())
        }

        fun save() {
            File("config/bot_status.yml").writeText(yaml.encodeToString(config))
        }
    }
}