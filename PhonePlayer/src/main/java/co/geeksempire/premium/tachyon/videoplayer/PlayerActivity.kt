package co.geeksempire.premium.tachyon.videoplayer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import co.geeksempire.premium.tachyon.videoplayer.databinding.PlayerLayoutBinding

class PlayerActivity : AppCompatActivity() {

    lateinit var playerLayoutBinding: PlayerLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playerLayoutBinding = PlayerLayoutBinding.inflate(layoutInflater)
        setContentView(playerLayoutBinding.root)



    }

}