package com.example.recorder

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.recorder.databinding.ActivityMainBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private lateinit var recordingService: RecordingService
    private lateinit var playbackService: PlaybackService
    private lateinit var shareService: ShareService
    
    private var currentRecordingFile: File? = null
    private val handler = Handler(Looper.getMainLooper())
    private var timeUpdateRunnable: Runnable? = null
    
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, "录音权限已授予", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "需要录音权限才能使用此功能", Toast.LENGTH_LONG).show()
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        initializeServices()
        setupUI()
        checkPermissions()
    }
    
    private fun initializeServices() {
        recordingService = RecordingService(this)
        playbackService = PlaybackService()
        shareService = ShareService(this)
    }
    
    private fun setupUI() {
        binding.apply {
            btnRecord.setOnClickListener {
                if (recordingService.isRecording()) {
                    stopRecording()
                } else {
                    startRecording()
                }
            }
            
            btnPlay.setOnClickListener {
                if (playbackService.isPlaying()) {
                    pausePlayback()
                } else {
                    playRecording()
                }
            }
            
            btnShare.setOnClickListener {
                shareRecording()
            }
            
            btnShareWeChat.setOnClickListener {
                shareToWeChat()
            }
            
            btnShareQQ.setOnClickListener {
                shareToQQ()
            }
        }
        
        updateUI()
    }
    
    private fun checkPermissions() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED -> {
                // 权限已授予
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
        }
    }
    
    private fun startRecording() {
        if (recordingService.startRecording()) {
            startTimeUpdate()
            updateUI()
            Toast.makeText(this, "开始录音", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "录音启动失败", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun stopRecording() {
        val file = recordingService.stopRecording()
        if (file != null) {
            currentRecordingFile = file
            stopTimeUpdate()
            updateUI()
            Toast.makeText(this, "录音已保存: ${file.name}", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "录音停止失败", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun playRecording() {
        val file = currentRecordingFile ?: recordingService.getCurrentRecordingFile()
        if (file != null && file.exists()) {
            if (playbackService.playRecording(file)) {
                updateUI()
                Toast.makeText(this, "开始播放", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "播放失败", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "没有可播放的录音文件", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun pausePlayback() {
        if (playbackService.pausePlayback()) {
            updateUI()
            Toast.makeText(this, "暂停播放", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun shareRecording() {
        val file = currentRecordingFile ?: recordingService.getCurrentRecordingFile()
        if (file != null && file.exists()) {
            shareService.shareRecording(file)
        } else {
            Toast.makeText(this, "没有可分享的录音文件", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun shareToWeChat() {
        val file = currentRecordingFile ?: recordingService.getCurrentRecordingFile()
        if (file != null && file.exists()) {
            shareService.shareToWeChat(file)
        } else {
            Toast.makeText(this, "没有可分享的录音文件", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun shareToQQ() {
        val file = currentRecordingFile ?: recordingService.getCurrentRecordingFile()
        if (file != null && file.exists()) {
            shareService.shareToQQ(file)
        } else {
            Toast.makeText(this, "没有可分享的录音文件", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun startTimeUpdate() {
        timeUpdateRunnable = object : Runnable {
            override fun run() {
                updateRecordingTime()
                handler.postDelayed(this, 1000)
            }
        }
        handler.post(timeUpdateRunnable!!)
    }
    
    private fun stopTimeUpdate() {
        timeUpdateRunnable?.let { handler.removeCallbacks(it) }
        timeUpdateRunnable = null
    }
    
    private fun updateRecordingTime() {
        val time = recordingService.getRecordingTime()
        val timeString = formatTime(time)
        binding.tvRecordingTime.text = getString(R.string.recording_time, timeString)
    }
    
    private fun updateUI() {
        binding.apply {
            when {
                recordingService.isRecording() -> {
                    btnRecord.text = getString(R.string.stop_recording)
                    btnRecord.setBackgroundColor(ContextCompat.getColor(this@MainActivity, R.color.red))
                    tvRecordingStatus.text = getString(R.string.recording_status, getString(R.string.recording_recording))
                    btnPlay.isEnabled = false
                    btnShare.isEnabled = false
                    btnShareWeChat.isEnabled = false
                    btnShareQQ.isEnabled = false
                }
                playbackService.isPlaying() -> {
                    btnRecord.text = getString(R.string.start_recording)
                    btnRecord.setBackgroundColor(ContextCompat.getColor(this@MainActivity, R.color.green))
                    tvRecordingStatus.text = getString(R.string.recording_status, getString(R.string.recording_playing))
                    btnPlay.text = getString(R.string.pause_recording)
                    btnRecord.isEnabled = false
                    btnShare.isEnabled = false
                    btnShareWeChat.isEnabled = false
                    btnShareQQ.isEnabled = false
                }
                else -> {
                    btnRecord.text = getString(R.string.start_recording)
                    btnRecord.setBackgroundColor(ContextCompat.getColor(this@MainActivity, R.color.green))
                    tvRecordingStatus.text = getString(R.string.recording_status, getString(R.string.recording_idle))
                    btnPlay.text = getString(R.string.play_recording)
                    btnRecord.isEnabled = true
                    btnPlay.isEnabled = currentRecordingFile != null || recordingService.getCurrentRecordingFile() != null
                    btnShare.isEnabled = currentRecordingFile != null || recordingService.getCurrentRecordingFile() != null
                    btnShareWeChat.isEnabled = currentRecordingFile != null || recordingService.getCurrentRecordingFile() != null
                    btnShareQQ.isEnabled = currentRecordingFile != null || recordingService.getCurrentRecordingFile() != null
                }
            }
        }
    }
    
    private fun formatTime(milliseconds: Long): String {
        val seconds = milliseconds / 1000
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return String.format("%02d:%02d", minutes, remainingSeconds)
    }
    
    override fun onDestroy() {
        super.onDestroy()
        stopTimeUpdate()
        recordingService.cleanup()
        playbackService.cleanup()
    }
}