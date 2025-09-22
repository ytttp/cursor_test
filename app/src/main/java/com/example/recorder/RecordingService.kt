package com.example.recorder

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.core.content.ContextCompat
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class RecordingService(private val context: Context) {
    
    private var mediaRecorder: MediaRecorder? = null
    private var isRecording = false
    private var recordingFile: File? = null
    private var startTime: Long = 0
    
    companion object {
        private const val TAG = "RecordingService"
        private const val AUDIO_FORMAT = ".m4a"
    }
    
    fun startRecording(): Boolean {
        if (!hasPermission()) {
            Log.e(TAG, "No recording permission")
            return false
        }
        
        if (isRecording) {
            Log.w(TAG, "Already recording")
            return false
        }
        
        try {
            mediaRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                MediaRecorder(context)
            } else {
                @Suppress("DEPRECATION")
                MediaRecorder()
            }
            
            recordingFile = createRecordingFile()
            recordingFile?.let { file ->
                mediaRecorder?.apply {
                    setAudioSource(MediaRecorder.AudioSource.MIC)
                    setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                    setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                    setOutputFile(file.absolutePath)
                    prepare()
                    start()
                }
                
                isRecording = true
                startTime = System.currentTimeMillis()
                Log.i(TAG, "Recording started: ${file.absolutePath}")
                return true
            }
        } catch (e: IOException) {
            Log.e(TAG, "Failed to start recording", e)
            releaseRecorder()
        }
        
        return false
    }
    
    fun stopRecording(): File? {
        if (!isRecording) {
            Log.w(TAG, "Not recording")
            return null
        }
        
        try {
            mediaRecorder?.apply {
                stop()
                release()
            }
            mediaRecorder = null
            isRecording = false
            
            val file = recordingFile
            recordingFile = null
            Log.i(TAG, "Recording stopped: ${file?.absolutePath}")
            return file
        } catch (e: Exception) {
            Log.e(TAG, "Failed to stop recording", e)
            releaseRecorder()
            return null
        }
    }
    
    fun isRecording(): Boolean = isRecording
    
    fun getRecordingTime(): Long {
        return if (isRecording) {
            System.currentTimeMillis() - startTime
        } else {
            0
        }
    }
    
    fun getCurrentRecordingFile(): File? = recordingFile
    
    private fun hasPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    private fun createRecordingFile(): File? {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "RECORDING_$timeStamp$AUDIO_FORMAT"
        
        val storageDir = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            File(context.getExternalFilesDir(Environment.DIRECTORY_MUSIC), "Recordings")
        } else {
            File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC), "Recordings")
        }
        
        if (!storageDir.exists()) {
            storageDir.mkdirs()
        }
        
        return File(storageDir, fileName)
    }
    
    private fun releaseRecorder() {
        try {
            mediaRecorder?.release()
        } catch (e: Exception) {
            Log.e(TAG, "Error releasing recorder", e)
        }
        mediaRecorder = null
        isRecording = false
    }
    
    fun cleanup() {
        releaseRecorder()
    }
}