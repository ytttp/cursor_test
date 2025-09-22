package com.example.recorder

import android.media.MediaPlayer
import android.util.Log
import java.io.File

class PlaybackService {
    
    private var mediaPlayer: MediaPlayer? = null
    private var isPlaying = false
    private var currentFile: File? = null
    
    companion object {
        private const val TAG = "PlaybackService"
    }
    
    fun playRecording(file: File): Boolean {
        if (isPlaying) {
            Log.w(TAG, "Already playing")
            return false
        }
        
        if (!file.exists()) {
            Log.e(TAG, "File does not exist: ${file.absolutePath}")
            return false
        }
        
        try {
            mediaPlayer = MediaPlayer().apply {
                setDataSource(file.absolutePath)
                prepare()
                start()
                setOnCompletionListener {
                    isPlaying = false
                    currentFile = null
                    Log.i(TAG, "Playback completed")
                }
                setOnErrorListener { _, what, extra ->
                    Log.e(TAG, "Playback error: what=$what, extra=$extra")
                    isPlaying = false
                    currentFile = null
                    true
                }
            }
            
            isPlaying = true
            currentFile = file
            Log.i(TAG, "Playback started: ${file.absolutePath}")
            return true
        } catch (e: Exception) {
            Log.e(TAG, "Failed to start playback", e)
            releasePlayer()
            return false
        }
    }
    
    fun pausePlayback(): Boolean {
        if (!isPlaying) {
            Log.w(TAG, "Not playing")
            return false
        }
        
        try {
            mediaPlayer?.pause()
            isPlaying = false
            Log.i(TAG, "Playback paused")
            return true
        } catch (e: Exception) {
            Log.e(TAG, "Failed to pause playback", e)
            return false
        }
    }
    
    fun resumePlayback(): Boolean {
        if (isPlaying) {
            Log.w(TAG, "Already playing")
            return false
        }
        
        try {
            mediaPlayer?.start()
            isPlaying = true
            Log.i(TAG, "Playback resumed")
            return true
        } catch (e: Exception) {
            Log.e(TAG, "Failed to resume playback", e)
            return false
        }
    }
    
    fun stopPlayback(): Boolean {
        if (!isPlaying && mediaPlayer == null) {
            Log.w(TAG, "Not playing")
            return false
        }
        
        try {
            mediaPlayer?.stop()
            isPlaying = false
            currentFile = null
            Log.i(TAG, "Playback stopped")
            return true
        } catch (e: Exception) {
            Log.e(TAG, "Failed to stop playback", e)
            return false
        }
    }
    
    fun isPlaying(): Boolean = isPlaying
    
    fun getCurrentFile(): File? = currentFile
    
    fun getDuration(): Int {
        return try {
            mediaPlayer?.duration ?: 0
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get duration", e)
            0
        }
    }
    
    fun getCurrentPosition(): Int {
        return try {
            mediaPlayer?.currentPosition ?: 0
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get current position", e)
            0
        }
    }
    
    fun seekTo(position: Int): Boolean {
        return try {
            mediaPlayer?.seekTo(position) ?: false
        } catch (e: Exception) {
            Log.e(TAG, "Failed to seek to position", e)
            false
        }
    }
    
    private fun releasePlayer() {
        try {
            mediaPlayer?.release()
        } catch (e: Exception) {
            Log.e(TAG, "Error releasing player", e)
        }
        mediaPlayer = null
        isPlaying = false
        currentFile = null
    }
    
    fun cleanup() {
        releasePlayer()
    }
}