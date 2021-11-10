package co.geeksempire.premium.tachyon.videoplayer

import android.os.Bundle
import android.os.ResultReceiver
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import co.geeksempire.premium.tachyon.videoplayer.databinding.PlayerLayoutBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector.CaptionCallback
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.util.MimeTypes


class PlayerActivity : AppCompatActivity() {

    private lateinit var exoPlayer: ExoPlayer

    private var playWhenReady = true

    private var currentWindow = 0
    private var playbackPosition = 0L

    private var mediaSession: MediaSessionCompat? = null
    private var mediaSessionConnector: MediaSessionConnector? = null

    private val playerLayoutBinding by lazy(LazyThreadSafetyMode.NONE) {
        PlayerLayoutBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(playerLayoutBinding.root)



    }

    override fun onStart() {
        super.onStart()

        initializeExoPlayer()

    }

    override fun onDestroy() {
        super.onDestroy()

        releaseExoPlayer()

    }

    private fun initializeExoPlayer() {

        mediaSession?.isActive = true

        val trackSelector = DefaultTrackSelector(this).apply {
            setParameters(buildUponParameters().setMaxVideoSizeSd())
        }

        exoPlayer = ExoPlayer.Builder(this)
            .setTrackSelector(trackSelector)
            .build()

        exoPlayer.also { exoPlayer ->

            playerLayoutBinding.videoPlayerView.player = exoPlayer

            addMediaToPlaylist("https://samplelib.com/lib/preview/mp4/sample-10s.mp4")
            addMediaToPlaylist("https://media.istockphoto.com/videos/defocused-lights-of-a-fun-fair-video-id950201516")
            addMediaToPlaylist("https://media.istockphoto.com/videos/portrait-of-highland-straight-fluffy-cat-with-long-hair-and-round-in-video-id1161210058")
            addMediaToPlaylist("https://media.istockphoto.com/videos/drawing-watercolor-abstract-background-video-id1144161461")
            addMediaToPlaylist("https://media.istockphoto.com/videos/pink-and-blue-ink-splash-in-super-slow-motion-video-id1269967019")

            mediaSession = MediaSessionCompat(this, "Tachyon Online Video Player")
            mediaSessionConnector = MediaSessionConnector(mediaSession!!)

            mediaSessionConnector!!.setPlayer(exoPlayer)
            mediaSessionConnector!!.setEnabledPlaybackActions(
                PlaybackStateCompat.ACTION_PLAY_PAUSE
                        or PlaybackStateCompat.ACTION_PLAY
                        or PlaybackStateCompat.ACTION_PAUSE
                        or PlaybackStateCompat.ACTION_SEEK_TO
                        or PlaybackStateCompat.ACTION_FAST_FORWARD
                        or PlaybackStateCompat.ACTION_REWIND
                        or PlaybackStateCompat.ACTION_STOP
                        or PlaybackStateCompat.ACTION_SET_REPEAT_MODE
                        or PlaybackStateCompat.ACTION_SET_SHUFFLE_MODE
            )

            mediaSessionConnector!!.setQueueNavigator(object : TimelineQueueNavigator(mediaSession!!) {

                override fun getMediaDescription(player: Player, windowIndex: Int): MediaDescriptionCompat {

                    return MediaDescriptionCompat.Builder()
                        .setTitle(player.currentMediaItem!!.mediaMetadata.title)
                        .setDescription("Description $windowIndex")
                        .setSubtitle("Subtitle")
                        .build()
                }

            })

            mediaSessionConnector!!.setCaptionCallback(object : CaptionCallback {

                override fun onCommand(player: Player, command: String, extras: Bundle?, resultReceiver: ResultReceiver?): Boolean {

                    return false
                }

                override fun onSetCaptioningEnabled(player: Player, enabled: Boolean) {

                }

                override fun hasCaptions(player: Player): Boolean {
                    return true
                }

            })

        }

        exoPlayer.playWhenReady = playWhenReady
        exoPlayer.seekTo(currentWindow, playbackPosition)
        exoPlayer.addListener(object : Player.Listener {

            override fun onPlaybackStateChanged(playbackState: Int) {

                val stateString: String = when (playbackState) {
                    ExoPlayer.STATE_IDLE -> {



                        "ExoPlayer.STATE_IDLE"
                    }
                    ExoPlayer.STATE_BUFFERING -> {



                        "ExoPlayer.STATE_BUFFERING"
                    }
                    ExoPlayer.STATE_READY -> {

                        val videoPlayerLayoutParameters = playerLayoutBinding.videoPlayerView.layoutParams as ConstraintLayout.LayoutParams

                        videoPlayerLayoutParameters.height = ConstraintLayout.LayoutParams.WRAP_CONTENT

                        "ExoPlayer.STATE_READY"
                    }
                    ExoPlayer.STATE_ENDED -> {



                        "ExoPlayer.STATE_ENDED"
                    }
                    else -> {



                        "UNKNOWN_STATE"
                    }
                }

                println("Exo Player ::: $stateString")

            }

        })
        exoPlayer.prepare()

    }

    private fun releaseExoPlayer() {

        mediaSession?.release()

        if (::exoPlayer.isInitialized) {

            exoPlayer.run {
                playbackPosition = this.currentPosition
                currentWindow = this.currentWindowIndex
                playWhenReady = this.playWhenReady
                release()
            }

        }

    }

    private fun addMediaToPlaylist(mediaUri: String) {

        if (::exoPlayer.isInitialized) {

            val mediaItem = MediaItem.Builder()
                .setUri(mediaUri)
                .setMimeType(MimeTypes.APPLICATION_MP4)
                .build()

            exoPlayer.addMediaItem(mediaItem)

        }

    }

}