package com.ivor.tools

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.RemoteViews
import java.text.SimpleDateFormat
import java.util.*

class CountdownNoteWidget : AppWidgetProvider() {
    
    companion object {
        const val ACTION_UPDATE_WIDGET = "com.ivor.tools.UPDATE_WIDGET"
        const val PREFS_NAME = "countdown_widget_prefs"
        const val PREF_NOTE_TEXT = "note_text"
        const val PREF_TARGET_TIME = "target_time"
        
        private val handler = Handler(Looper.getMainLooper())
        private var updateRunnable: Runnable? = null
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        Log.d("CountdownNoteWidget", "onUpdate called with ${appWidgetIds.size} widgets")
        
        try {
            for (appWidgetId in appWidgetIds) {
                Log.d("CountdownNoteWidget", "Updating widget ID: $appWidgetId")
                updateAppWidget(context, appWidgetManager, appWidgetId)
            }
            
            // 启动定期更新
            startPeriodicUpdate(context, appWidgetManager, appWidgetIds)
        } catch (e: Exception) {
            Log.e("CountdownNoteWidget", "Error in onUpdate", e)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("CountdownNoteWidget", "onReceive called with action: ${intent.action}")
        
        try {
            super.onReceive(context, intent)
            
            if (ACTION_UPDATE_WIDGET == intent.action) {
                val appWidgetManager = AppWidgetManager.getInstance(context)
                val appWidgetIds = appWidgetManager.getAppWidgetIds(
                    android.content.ComponentName(context, CountdownNoteWidget::class.java)
                )
                
                Log.d("CountdownNoteWidget", "Manual update requested for ${appWidgetIds.size} widgets")
                onUpdate(context, appWidgetManager, appWidgetIds)
            }
        } catch (e: Exception) {
            Log.e("CountdownNoteWidget", "Error in onReceive", e)
        }
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        Log.d("CountdownNoteWidget", "onDeleted called for ${appWidgetIds.size} widgets")
        
        try {
            val prefs = context.getSharedPreferences("widget_prefs", Context.MODE_PRIVATE)
            val editor = prefs.edit()
            
            for (appWidgetId in appWidgetIds) {
                Log.d("CountdownNoteWidget", "Deleting data for widget ID: $appWidgetId")
                editor.remove("note_text_$appWidgetId")
                editor.remove("target_time_$appWidgetId")
            }
            
            editor.apply()
            Log.d("CountdownNoteWidget", "Widget data cleanup completed")
        } catch (e: Exception) {
            Log.e("CountdownNoteWidget", "Error in onDeleted", e)
        }
        
        // 清理资源
        stopPeriodicUpdate()
    }

    override fun onDisabled(context: Context) {
        // 停止更新
        stopPeriodicUpdate()
    }

    internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        Log.d("CountdownNoteWidget", "updateAppWidget called for widget ID: $appWidgetId")
        
        try {
            val views = RemoteViews(context.packageName, R.layout.countdown_note_widget)
            
            // 获取保存的数据
            val prefs = context.getSharedPreferences("widget_prefs", Context.MODE_PRIVATE)
            val noteText = prefs.getString("note_text_$appWidgetId", "倒计时记事本")
            val targetTime = prefs.getLong("target_time_$appWidgetId", 0)
            
            Log.d("CountdownNoteWidget", "Widget $appWidgetId - Note: $noteText, Target: $targetTime")
            
            // 设置记事内容
            views.setTextViewText(R.id.widget_note_text, noteText ?: "倒计时记事本")
            
            // 设置倒计时显示
            if (targetTime > 0) {
                val currentTime = System.currentTimeMillis()
                val timeRemaining = targetTime - currentTime
                
                if (timeRemaining > 0) {
                    val formattedTime = formatTimeRemaining(timeRemaining)
                    views.setTextViewText(R.id.widget_countdown_text, formattedTime)
                    views.setTextColor(R.id.widget_countdown_text, 0xFF2196F3.toInt())
                } else {
                    views.setTextViewText(R.id.widget_countdown_text, "时间到!")
                    views.setTextColor(R.id.widget_countdown_text, 0xFFF44336.toInt())
                }
            } else {
                views.setTextViewText(R.id.widget_countdown_text, "点击设置")
                views.setTextColor(R.id.widget_countdown_text, 0xFF666666.toInt())
            }
            
            // 设置点击事件
            val intent = Intent(context, CountdownNoteConfigActivity::class.java)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            val pendingIntent = PendingIntent.getActivity(
                context, 
                appWidgetId, 
                intent, 
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widget_container, pendingIntent)
            
            // 更新小工具
            appWidgetManager.updateAppWidget(appWidgetId, views)
            Log.d("CountdownNoteWidget", "Widget $appWidgetId updated successfully")
            
        } catch (e: Exception) {
            Log.e("CountdownNoteWidget", "Error updating widget $appWidgetId", e)
        }
    }
    
    private fun formatTimeRemaining(millisUntilFinished: Long): String {
        val days = millisUntilFinished / (1000 * 60 * 60 * 24)
        val hours = (millisUntilFinished % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60)
        val minutes = (millisUntilFinished % (1000 * 60 * 60)) / (1000 * 60)
        val seconds = (millisUntilFinished % (1000 * 60)) / 1000
        
        return when {
            days > 0 -> "${days}天 ${hours}小时 ${minutes}分"
            hours > 0 -> "${hours}小时 ${minutes}分 ${seconds}秒"
            minutes > 0 -> "${minutes}分 ${seconds}秒"
            else -> "${seconds}秒"
        }
    }
    
    private fun startPeriodicUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        stopPeriodicUpdate()
        
        updateRunnable = object : Runnable {
            override fun run() {
                for (appWidgetId in appWidgetIds) {
                    updateAppWidget(context, appWidgetManager, appWidgetId)
                }
                handler.postDelayed(this, 1000) // 每秒更新一次
            }
        }
        handler.post(updateRunnable!!)
    }
    
    private fun stopPeriodicUpdate() {
        updateRunnable?.let {
            handler.removeCallbacks(it)
            updateRunnable = null
        }
    }
}