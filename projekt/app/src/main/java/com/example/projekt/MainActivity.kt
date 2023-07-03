package com.example.projekt

import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private var mp: MediaPlayer? = null
    private var currentSong = mutableListOf<Int>()
    private val images: ArrayList<Drawable> = ArrayList()
    private lateinit var playButton: FloatingActionButton
    private lateinit var pauseButton: FloatingActionButton
    private lateinit var previousButton: FloatingActionButton
    private lateinit var nextButton: FloatingActionButton
    private lateinit var seekBar: SeekBar
    private lateinit var textView: TextView
    private lateinit var backgroundImage: ImageView
    private var currentSongNr = 0
    private var isSeekBarTracking = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        playButton = findViewById(R.id.play_button)
        pauseButton = findViewById(R.id.pause_button)
        previousButton = findViewById(R.id.previous_button)
        nextButton = findViewById(R.id.next_button)
        seekBar = findViewById(R.id.seekbar)
        textView = findViewById(R.id.textView)
        backgroundImage = findViewById(R.id.background_image)


        currentSong.add(R.raw.lego2)
        currentSong.add(R.raw.hobbit)
        currentSong.add(R.raw.forest)
        currentSong.add(R.raw.fortnitesong)

        images.add(resources.getDrawable(R.drawable.forest))
        images.add(resources.getDrawable(R.drawable.hobbit))
        images.add(resources.getDrawable(R.drawable.lego2))
        images.add(resources.getDrawable(R.drawable.fortnite))


        controlSound(currentSong[currentSongNr])
    }

    private fun controlSound(id: Int) {
        if (mp == null) {
            mp = MediaPlayer.create(this, id)
            textView.text="MP3 PLAYER"
            initializeSeekBar()
        }

        playButton.setOnClickListener {
            mp?.start()
            setText()
            changeBackground()
        }

        pauseButton.setOnClickListener {
            mp?.pause()
        }

        nextButton.setOnClickListener {
            mp?.reset()
            currentSongNr++
            if (currentSongNr >= currentSong.size) {
                currentSongNr = 0
            }
            mp?.release()
            mp = MediaPlayer.create(this, currentSong[currentSongNr])
            setText()
            initializeSeekBar()
            mp?.start()
            changeBackground()
        }

        previousButton.setOnClickListener {
            mp?.reset()
            currentSongNr--
            if (currentSongNr < 0) {
                currentSongNr = currentSong.size - 1
            }
            mp?.release()
            mp = MediaPlayer.create(this, currentSong[currentSongNr])
            setText()
            initializeSeekBar()
            mp?.start()
            changeBackground()
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (seekBar != null) {
                    mp?.seekTo(seekBar.progress)
                }
                isSeekBarTracking = false
            }

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    isSeekBarTracking = true
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                isSeekBarTracking = true
            }
        })
    }

    private fun setText() {
        val getName = resources.getResourceEntryName(currentSong[currentSongNr])
        textView.text = getName
    }


    private fun changeBackground()
    {
        if (currentSongNr >= images.size) {
            currentSongNr = 0
        }
        backgroundImage.setImageDrawable(images[currentSongNr])
    }


    private fun initializeSeekBar() {
        seekBar.max = mp!!.duration

        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                try {
                    if (!isSeekBarTracking) {
                        seekBar.progress = mp!!.currentPosition
                    }
                    handler.postDelayed(this, 100)
                } catch (e: Exception) {
                    seekBar.progress = 0
                }
            }
        }, 0)
    }
}