package dev.t7e.mechatechkt.config

import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import java.io.File

/**
 * Created by testusuke on 2023/07/29
 * @author testusuke
 */
@Serializable
data class BotConfig(
    val botToken: String = "<bot token here>"
) {
    companion object {
        private val yaml = Yaml(configuration = YamlConfiguration(encodeDefaults = true, strictMode = false))

        val config: BotConfig = File("config/bot.yml").let { file ->
            if (!file.parentFile.exists()) file.parentFile.mkdirs()
            if (!file.exists()) file.writeText(yaml.encodeToString(BotConfig()))
            yaml.decodeFromString(serializer(), file.readText())
        }
    }
}