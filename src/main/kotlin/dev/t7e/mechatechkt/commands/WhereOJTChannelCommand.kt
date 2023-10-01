package dev.t7e.mechatechkt.commands

import dev.kord.core.behavior.interaction.respondEphemeral
import dev.kord.core.entity.interaction.ApplicationCommandInteraction
import dev.kord.rest.builder.interaction.GlobalMultiApplicationCommandBuilder
import dev.kord.rest.builder.interaction.user
import kotlinx.coroutines.flow.toList

/**
 * Created by testusuke on 2023/10/01
 * @author testusuke
 */
object WhereOJTChannelCommand : CommandHandler {
    override suspend fun handle(interaction: ApplicationCommandInteraction) {
        val guild = interaction.channel.getGuildOrNull()!!
        //  get all channel
        val channels = guild.channels.toList()

        //  arguments
        val userId = interaction.getOptionSnowflake("user")!!

        //  get ojt channel
        val channel = channels.find {
            val description = it.data.topic.value ?: return@find false
            description.contains(userId.toString())
        }

        if (channel == null) {
            interaction.respondEphemeral { content = "OJTチャンネルが見つかりませんでした" }
            return
        }

        interaction.respondEphemeral { content = channel.mention }
    }

    override fun register(builder: GlobalMultiApplicationCommandBuilder) {
        builder.input("ojt", "OJTチャンネルを検索します") {
            dmPermission = false

            user("user", "ユーザー") {
                required = true
            }
        }
    }
}