package dev.t7e.mechatechkt.commands

import dev.kord.common.entity.Overwrite
import dev.kord.common.entity.OverwriteType
import dev.kord.common.entity.Permission
import dev.kord.common.entity.Permissions
import dev.kord.core.behavior.createTextChannel
import dev.kord.core.behavior.interaction.respondEphemeral
import dev.kord.core.entity.interaction.ApplicationCommandInteraction
import dev.kord.rest.builder.channel.addMemberOverwrite
import dev.kord.rest.builder.interaction.GlobalMultiApplicationCommandBuilder
import dev.kord.rest.builder.interaction.user

/**
 * Created by testusuke on 2023/07/29
 * @author testusuke
 */
object CreatePrivateChannelCommand : CommandHandler {
    override suspend fun handle(interaction: ApplicationCommandInteraction) {
        interaction.respondEphemeral { content = "プライベートチャンネルを作成しています..." }

        val guild = interaction.channel.getGuildOrNull()!!
        val channel = interaction.channel.asChannel()
        //  get category channel what text channel is in
        val category = channel.data.parentId!!.let { guild.getChannelOrNull(it.value!!) }

        //  arguments
        val userId = interaction.getOptionSnowflake("user")!!
        val user = guild.getMember(userId)

        //  create channel
        val userName = user.nickname ?: user.username
        guild.createTextChannel("$userName メンター") {
            this.parentId = category?.id
            this.addMemberOverwrite(userId) {
                this.allowed = Permissions(Permission.ViewChannel)
                this.denied = Permissions()
            }
            channel.data.permissionOverwrites.value?.forEach {
                this.addOverwrite(it)
            }
        }
    }

    override fun register(builder: GlobalMultiApplicationCommandBuilder) {
        builder.input("mentoring", "メンター用プライベートチャンネル作成") {
            dmPermission = false

            user("user", "対象ユーザー") {
                required = true
            }
        }
    }
}