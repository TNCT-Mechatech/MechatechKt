package dev.t7e.mechatechkt.unit

import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.channel.asChannelOf
import dev.kord.core.behavior.channel.asChannelOfOrNull
import dev.kord.core.entity.channel.TextChannel
import dev.t7e.mechatechkt.client
import dev.t7e.mechatechkt.config.BotStatus
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.timerTask

/**
 * Created by testusuke on 2023/08/19
 * @author testusuke
 */
object ProgressReport {

    suspend fun TextChannel.postProgressReport() {
        val localDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy年 MM月 dd日 E曜日", Locale.JAPANESE)
        val message = this.createMessage("${localDate.format(formatter)} 進捗報告")
        this.startPublicThreadWithMessage(
            messageId = message.id,
            "${localDate.format(formatter)} 進捗報告"
        )
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun scheduleProgressReport() {
        val timer = Timer()

        val task = timerTask {
            //  check enabled
            if (!BotStatus.config.enabledProgressReport) return@timerTask

            GlobalScope.launch {
                //  check today is not weekend
                val today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
                if (today != Calendar.SATURDAY && today != Calendar.SUNDAY) {
                    //  get channel
                    val channel = client.getChannel(Snowflake(BotStatus.config.progressReportChannel))
                    //  post
                    channel?.asChannelOf<TextChannel>()?.postProgressReport()
                }
            }
        }

        val now = Calendar.getInstance()
        val runTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 17)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        // if now is after runTime, add 1 day
        if (now.after(runTime)) {
            runTime.add(Calendar.DAY_OF_YEAR, 1)
        }

        timer.scheduleAtFixedRate(task, runTime.time, TimeUnit.DAYS.toMillis(1))

        println("set progress report timer")
    }
}