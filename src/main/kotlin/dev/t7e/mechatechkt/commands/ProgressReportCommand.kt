package dev.t7e.mechatechkt.commands

import dev.kord.core.behavior.channel.asChannelOfOrNull
import dev.kord.core.behavior.interaction.respondEphemeral
import dev.kord.core.behavior.interaction.respondPublic
import dev.kord.core.entity.channel.TextChannel
import dev.kord.core.entity.interaction.ApplicationCommandInteraction
import dev.kord.rest.builder.interaction.GlobalMultiApplicationCommandBuilder
import dev.kord.rest.builder.interaction.boolean
import dev.kord.rest.builder.interaction.subCommand
import dev.t7e.mechatechkt.config.BotStatus
import dev.t7e.mechatechkt.unit.ProgressReport.postProgressReport

/**
 * Created by testusuke on 2023/08/05
 * @author testusuke
 */
object ProgressReportCommand : CommandHandler {
    override suspend fun handle(interaction: ApplicationCommandInteraction) {

        if (interaction.isSubCommand("on")) {
            interaction.respondPublic { content = "進捗報告を有効化します。" }

            BotStatus.config.enabledProgressReport = true
            BotStatus.save()

            return
        }

        if (interaction.isSubCommand("off")) {
            interaction.respondPublic { content = "進捗報告を無効化します。" }

            BotStatus.config.enabledProgressReport = false
            BotStatus.save()

            return
        }

        if (interaction.isSubCommand("register")) {
            interaction.respondPublic { content = "チャネルを登録します。" }

            val channel = interaction.channel.asChannelOfOrNull<TextChannel>()!!
            BotStatus.config.progressReportChannel = channel.id.value
            BotStatus.save()

            return
        }

        interaction.respondPublic { content = "進捗報告を作成します。" }
        //  channel
        val channel = interaction.channel.asChannelOfOrNull<TextChannel>()!!

        //  create channel
        channel.postProgressReport()
    }

    private fun ApplicationCommandInteraction.isSubCommand(name: String): Boolean {
        return this.data.data.options.value?.any { it.name == name } ?: false
    }


    override fun register(builder: GlobalMultiApplicationCommandBuilder) {
        builder.input("shinchoku", "進捗報告メッセージ作成") {
            dmPermission = false

            subCommand("on", "自動投稿を有効化します")
            subCommand("off", "自動投稿を無効化します")
            subCommand("register", "チャネルを登録します")
        }
    }
}