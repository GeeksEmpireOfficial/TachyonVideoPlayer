package co.geeksempire.premium.tachyon.videoplayer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import co.geeksempire.premium.tachyon.videoplayer.databinding.PlayerLayoutBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem

class PlayerActivity : AppCompatActivity() {

    private lateinit var exoPlayer: ExoPlayer

    private var playWhenReady = true

    private var currentWindow = 0
    private var playbackPosition = 0L

    private val playerLayoutBinding by lazy(LazyThreadSafetyMode.NONE) {
        PlayerLayoutBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(playerLayoutBinding.root)



    }

    override fun onStart() {
        super.onStart()

        initializeExoPlayer(mediaUri = "https://media.istockphoto.com/videos/defocused-lights-of-a-fun-fair-video-id950201516")

        repeat(3) {

            addMediaToPlaylist("https://media.istockphoto.com/videos/defocused-lights-of-a-fun-fair-video-id950201516")

        }

    }

    override fun onPause() {
        super.onPause()

        releaseExoPlayer()

    }

    private fun initializeExoPlayer(mediaUri: String) {

        exoPlayer = ExoPlayer.Builder(this)
            .build()
            .also { exoPlayer ->

                playerLayoutBinding.videoPlayerView.player = exoPlayer

                val mediaItem = MediaItem.fromUri(mediaUri)
                exoPlayer.addMediaItem(mediaItem)

            }

        exoPlayer.playWhenReady = playWhenReady
        exoPlayer.seekTo(currentWindow, playbackPosition)
        exoPlayer.prepare()

    }

    private fun releaseExoPlayer() {

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

            val mediaItem = MediaItem.fromUri(mediaUri)
            exoPlayer.addMediaItem(mediaItem)

        }

    }

}