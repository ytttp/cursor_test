package com.example.recorder

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import java.io.File

class ShareService(private val context: Context) {
    
    companion object {
        private const val TAG = "ShareService"
        private const val AUTHORITY = "com.example.recorder.fileprovider"
    }
    
    fun shareRecording(file: File) {
        if (!file.exists()) {
            Log.e(TAG, "File does not exist: ${file.absolutePath}")
            return
        }
        
        try {
            val uri = FileProvider.getUriForFile(context, AUTHORITY, file)
            
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "audio/*"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_TEXT, "分享一个录音文件")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            
            val chooserIntent = Intent.createChooser(shareIntent, "分享录音文件")
            chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            
            context.startActivity(chooserIntent)
            Log.i(TAG, "Share intent created for file: ${file.absolutePath}")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to share file", e)
        }
    }
    
    fun shareToSpecificApp(file: File, packageName: String) {
        if (!file.exists()) {
            Log.e(TAG, "File does not exist: ${file.absolutePath}")
            return
        }
        
        try {
            val uri = FileProvider.getUriForFile(context, AUTHORITY, file)
            
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "audio/*"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_TEXT, "分享一个录音文件")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                setPackage(packageName)
            }
            
            context.startActivity(shareIntent)
            Log.i(TAG, "Share intent created for $packageName with file: ${file.absolutePath}")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to share file to $packageName", e)
        }
    }
    
    fun shareToWeChat(file: File) {
        shareToSpecificApp(file, "com.tencent.mm")
    }
    
    fun shareToQQ(file: File) {
        shareToSpecificApp(file, "com.tencent.mobileqq")
    }
}