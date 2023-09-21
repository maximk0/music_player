package com.example.musicplayer

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder

class MusicPlayerService : Service() {

    private var music: MediaPlayer? = null
    private var isPaused = true
    override fun onCreate() {
        super.onCreate()
        music = MediaPlayer.create(this, listOfTracks.first())
        music?.start()
    }

    override fun onBind(p0: Intent): IBinder? = null

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (intent.action == Constants.ACTION.START_FOREGROUND_ACTION) {
            startForeground(NOTIFICATION_ID, createNotification())
            playMusic()
        } else if (intent.action == Constants.ACTION.STOP_FOREGROUND_ACTION) {
            stopMusic()
            stopSelf()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun createNotification(): Notification {
        val stopIntent = Intent(this, MainActivity::class.java)
        stopIntent.action = Constants.ACTION.STOP_FOREGROUND_ACTION
        val pendingIntent =
            PendingIntent.getService(this, 0, stopIntent, PendingIntent.FLAG_IMMUTABLE)

        return Notification.Builder(this, Constants.NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Music Player")
            .setContentText("Playing")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .build()
    }

    private fun playMusic() {
        isPaused = false
        listOfTracks.forEach{track ->
            music = MediaPlayer.create(this, track)
            music?.start()
        }

    }

    private fun stopMusic() {
        isPaused = true
        music?.stop()
        music?.release()
        music = null
    }

    companion object {
        const val NOTIFICATION_ID = 1

        val listOfTracks = listOf(
            R.raw.nikelback_we_will_rock_you,
            R.raw.imagine_dragons_im_so_sorry,
            R.raw.merelin_menson_sweet_dreems
        )
    }
}

object Constants {
    object ACTION {
        const val START_FOREGROUND_ACTION = "com.example.musicplayer.action.START_FOREGROUND"
        const val STOP_FOREGROUND_ACTION = "com.example.musicplayer.action.STOP_FOREGROUND"
    }

    const val NOTIFICATION_CHANNEL_ID = "musicplayer_channel"
}