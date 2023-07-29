package dev.t7e.mechatechkt.commands

import dev.kord.common.entity.Permission
import dev.kord.common.entity.Permissions
import dev.kord.core.behavior.createTextChannel
import dev.kord.core.behavior.interaction.respondEphemeral
import dev.kord.core.entity.interaction.ApplicationCommandInteraction
import dev.kord.rest.builder.channel.addMemberOverwrite
import dev.kord.rest.builder.channel.addRoleOverwrite
import dev.kord.rest.builder.interaction.GlobalMultiApplicationCommandBuilder
import dev.kord.rest.builder.interaction.role
import dev.kord.rest.builder.interaction.user

/**
 * Created by testusuke on 2023/07/29
 * @author testusuke
 */
object CreatePrivateChannelCommand : CommandHandler {
    override suspend fun handle(interaction: ApplicationCommandInteraction) {
        val guild = interaction.channel.getGuildOrNull()!!
        val channel = interaction.channel.asChannel()
        //  get category channel what text channel is in
        val category = channel.data.parentId!!.let { guild.getChannelOrNull(it.value!!) }

        //  arguments
        val userId = interaction.getOptionSnowflake("user")!!
        val roleId = interaction.getOptionSnowflake("role")!!
        val user = guild.getMember(userId)

        //  create permission
        val permissions = Permissions()
            .apply {
                plus(Permission.ViewChannel)
                plus(Permission.SendMessages)
                plus(Permission.ReadMessageHistory)
            }

        //  create channel
        guild.createTextChannel("${user.nickname} メンター") {
            this.parentId = category?.id
            this.addMemberOverwrite(userId) {
                this.allowed = permissions
            }
            this.addRoleOverwrite(roleId) {
                this.allowed = permissions
            }
        }

        interaction.respondEphemeral {
            content = "プライベートチャンネルを作成しました。"
        }
    }

    override fun register(builder: GlobalMultiApplicationCommandBuilder) {
        builder.input("createPrivateChannel", "メンター用プライベートチャンネル作成") {
            dmPermission = false

            user("user", "対象ユーザー") {
                required = true
            }

            role("role", "メンターのロール") {
                required = true
            }
        }
    }
}