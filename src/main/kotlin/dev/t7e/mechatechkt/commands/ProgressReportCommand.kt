package dev.t7e.mechatechkt.commands

import dev.kord.common.entity.ChannelType
import dev.kord.common.entity.optional.OptionalSnowflake
import dev.kord.core.behavior.channel.asChannelOfOrNull
import dev.kord.core.behavior.createTextChannel
import dev.kord.core.behavior.interaction.respondPublic
import dev.kord.core.entity.channel.GuildMessageChannel
import dev.kord.core.entity.channel.TextChannel
import dev.kord.core.entity.interaction.ApplicationCommandInteraction
import dev.kord.rest.builder.interaction.GlobalMultiApplicationCommandBuilder
import dev.kord.rest.json.request.GuildChannelCreateRequest
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * Created by testusuke on 2023/08/05
 * @author testusuke
 */
object ProgressReportCommand : CommandHandler {
    override suspend fun handle(interaction: ApplicationCommandInteraction) {
        interaction.respondPublic { content = "進捗報告を作成します。" }
        //  channel
        val channel = interaction.channel.asChannelOfOrNull<TextChannel>()!!

        //  create channel
        val localDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy年 MM月 dd日 E曜日", Locale.JAPANESE)
        val message = channel.createMessage("${localDate.format(formatter)} 進捗報告")
        channel.startPublicThreadWithMessage(
            messageId = message.id,
            "${localDate.format(formatter)} 進捗報告"
        )
    }

    override fun register(builder: GlobalMultiApplicationCommandBuilder) {
        builder.input("shinchoku", "進捗報告メッセージ作成") {
            dmPermission = false
        }
    }
}