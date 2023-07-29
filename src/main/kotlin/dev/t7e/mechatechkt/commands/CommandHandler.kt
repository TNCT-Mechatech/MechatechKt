package dev.t7e.mechatechkt.commands

import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.channel.GuildChannelBehavior
import dev.kord.core.behavior.channel.MessageChannelBehavior
import dev.kord.core.entity.interaction.ApplicationCommandInteraction
import dev.kord.core.entity.interaction.Interaction
import dev.kord.rest.builder.interaction.GlobalMultiApplicationCommandBuilder

interface CommandHandler {
    suspend fun handle(interaction: ApplicationCommandInteraction)

    fun register(builder: GlobalMultiApplicationCommandBuilder)
}

suspend fun MessageChannelBehavior.getGuildOrNull() = if (this is GuildChannelBehavior) this.getGuildOrNull() else null

private fun Interaction.getOptionAny(name: String): Any? {
    return if (this is ApplicationCommandInteraction) {
        this.data
            .data
            .options
            .value
            ?.find { it.name == name }
            ?.value
            ?.value
            ?.value
    } else {
        null
    }
}

fun Interaction.getOptionString(name: String): String? {
    return getOptionAny(name) as? String
}

fun Interaction.getOptionSnowflake(name: String): Snowflake? {
    return getOptionString(name)?.toULong()?.let { Snowflake(it) }
}
