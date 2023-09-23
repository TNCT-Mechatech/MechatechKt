package dev.t7e.mechatechkt

import dev.kord.core.Kord
import dev.kord.core.event.gateway.ReadyEvent
import dev.kord.core.event.interaction.ApplicationCommandInteractionCreateEvent
import dev.kord.core.on
import dev.kord.gateway.Intent
import dev.kord.gateway.Intents
import dev.kord.gateway.PrivilegedIntent
import dev.t7e.mechatechkt.commands.CreateOJTChannelCommand
import dev.t7e.mechatechkt.commands.CreatePrivateChannelCommand
import dev.t7e.mechatechkt.commands.ProgressReportCommand
import dev.t7e.mechatechkt.config.BotConfig
import dev.t7e.mechatechkt.config.BotStatus
import dev.t7e.mechatechkt.unit.ProgressReport

lateinit var client: Kord

@OptIn(PrivilegedIntent::class)
suspend fun main(args: Array<String>) {

    //  load config
    BotConfig
    BotStatus

    client = Kord(BotConfig.config.botToken)

    val commands = mapOf(
        "mentoring" to CreatePrivateChannelCommand,
        "shinchoku" to ProgressReportCommand,
        "create-ojt" to CreateOJTChannelCommand
    )

    client.createGlobalApplicationCommands {
        commands.values.distinct().forEach { it.register(this) }
    }

    client.on<ApplicationCommandInteractionCreateEvent> {
        if (interaction.user.isBot) return@on
        commands.forEach { (name, command) ->
            if (interaction.invokedCommandName == name) {
                command.handle(interaction)
            }
        }
    }

    client.on<ReadyEvent> {
        println("Logged in as ${kord.getSelf().username}!")
    }

    //  schedule progress report
    ProgressReport.scheduleProgressReport()
    
    //  start bot
    client.login {
        this.intents = Intents(
            Intent.MessageContent,
        )
    }
}