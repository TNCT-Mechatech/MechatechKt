package dev.t7e.mechatechkt.commands

import dev.kord.core.behavior.createTextChannel
import dev.kord.core.behavior.interaction.respondEphemeral
import dev.kord.core.entity.interaction.ApplicationCommandInteraction
import dev.kord.rest.builder.interaction.GlobalMultiApplicationCommandBuilder
import kotlinx.coroutines.flow.toList

/**
 * Created by testusuke on 2023/09/23
 * @author testusuke
 */
object CreateOJTChannelCommand : CommandHandler {
    override suspend fun handle(interaction: ApplicationCommandInteraction) {
        interaction.respondEphemeral { content = "OJTチャンネルを作成しています..." }

        val guild = interaction.channel.getGuildOrNull()!!
        val channel = interaction.channel.asChannel()
        //  get category channel what text channel is in
        val category = channel.data.parentId!!.let { guild.getChannelOrNull(it.value!!) }


        //  get all channel
        val channels = guild.channels.toList().filter {
            val categoryId = it.data.parentId?.value ?: return@filter false
            categoryId == category?.id
        }

        //  get all user
        val users = guild.members.toList()
            .filter {
                !it.isBot
            }
            .filter { member ->
                !channels.any { channel ->
                    val description = channel.data.topic.value ?: return@any false

                    description.contains(member.id.toString())
                }
            }

        users.forEach { user ->
            //  create ojt channel
            val userName = user.nickname ?: user.username
            guild.createTextChannel("ojt-$userName") {
                this.parentId = category?.id
                this.topic = user.id.toString()
            }
        }

    }

    override fun register(builder: GlobalMultiApplicationCommandBuilder) {
        builder.input("create-ojt", "OJTチャンネル作成") {
            dmPermission = false
        }
    }
}