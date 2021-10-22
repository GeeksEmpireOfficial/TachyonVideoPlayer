package co.geeksempire.premium.tachyon.videoplayer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import co.geeksempire.premium.tachyon.videoplayer.databinding.PlayerLayoutBinding
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer

class PlayerActivity : AppCompatActivity() {

    private var playWhenReady = true

    private var currentWindow = 0
    private var playbackPosition = 0L

    private val playerLayoutBinding by lazy(LazyThreadSafetyMode.NONE) {
        PlayerLayoutBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(playerLayoutBinding.root)

        val simpleExoPlayer: SimpleExoPlayer = SimpleExoPlayer.Builder(this)
            .build()
            .also { exoPlayer ->

                playerLayoutBinding.videoPlayerView.player = exoPlayer

                val mediaItem = MediaItem.fromUri("https://media.istockphoto.com/videos/defocused-lights-of-a-fun-fair-video-id950201516")
                exoPlayer.setMediaItem(mediaItem)

            }

        simpleExoPlayer.playWhenReady = playWhenReady
        simpleExoPlayer.seekTo(currentWindow, playbackPosition)
        simpleExoPlayer.prepare()

    }

}